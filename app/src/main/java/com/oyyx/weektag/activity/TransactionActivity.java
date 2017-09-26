package com.oyyx.weektag.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.oyyx.weektag.R;
import com.oyyx.weektag.dateBase.TransactionLab;
import com.oyyx.weektag.dateBase.Transactionn;
import com.oyyx.weektag.utils.CalendarUtils;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.litepal.LitePal;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置事件的Activity
 *
 */

public class TransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int SELECT_FROM_ALBUM = 0;
    private static final int TAKE_PHOTOS = 1;

    private int mSelectColor;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;

    private Uri uri;


    @BindView(R.id.date_fab)
    FloatingActionButton fab_date;

    @BindView(R.id.color_picker_fab)
    FloatingActionButton fab_color_picker;

    @BindView(R.id.time_fab)
    FloatingActionButton fab_time;

    private Calendar calendar = Calendar.getInstance();
    //日期
    private DatePickerDialog datePickerDialog;
    //颜色选择器
    private ColorPickerDialog colorPickerDialog;
    //时间
    private TimePickerDialog timePickerDialog;

    //显示日期和具体时间
    @BindView(R.id.date_tv)
    TextView tv_date;

    @BindView(R.id.time_tv)
    TextView tv_time;

    //自定义照片
    @BindView(R.id.photo_iv)
    ImageView iv_photo;


    //标题和备注
    @BindView(R.id.title_til)
    TextInputLayout til_title;

    @BindView(R.id.memo_til)
    TextInputLayout til_memo;

    private Transactionn transactionn;


    private File mPhotoFile;

    //响应toolbar item点击事件
    private Toolbar.OnMenuItemClickListener onMenuClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_done) {
                String title = til_title.getEditText().getText().toString();
                String memo = til_memo.getEditText().getText().toString();
                if(title.equals("")) {
                    til_title.setError("标题不能为空");
                    return true;
                }

                //存入数据库
                transactionn.setTime(CalendarUtils.timeToDate(year,month,day,hour,min));
                transactionn.setColour(mSelectColor);
                transactionn.setTitle(title);
                transactionn.setMemo(memo);
                if(uri != null) {
                    transactionn.setUri(uri.toString());
                }else if(mPhotoFile != null){
                    try {
                        //防止Uri.parse报空指针
                        transactionn.setUri(mPhotoFile.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                transactionn.save();
                finish();
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(getApplication().getSharedPreferences("theme",MODE_PRIVATE).getInt("theme",R.style.myTheme));
        setContentView(R.layout.activity_transaction);
        ButterKnife.bind(this);
        LitePal.initialize(this);

        mSelectColor = ContextCompat.getColor(this, R.color.flamingo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加事件");

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuClick);

        transactionn = new Transactionn();






        fab_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int[] colors = getResources().getIntArray(R.array.default_rainbow);

                colorPickerDialog = ColorPickerDialog.newInstance(
                        R.string.color_picker_default_title,
                        colors,
                        mSelectColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL,
                        true
                );

                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mSelectColor = color;
                        fab_color_picker.setBackgroundTintList(ColorStateList.valueOf(mSelectColor));
                    }
                });

                colorPickerDialog.show(getFragmentManager(),"color_picker");
            }
        });

        fab_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(TransactionActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.setYearRange(calendar.get(Calendar.YEAR),2037);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(),"datePicker");
            }
        });

        fab_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = TimePickerDialog.newInstance(TransactionActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(),"timePicker");
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = {"从相册中选择", "拍照"};
                new AlertDialog.Builder(TransactionActivity.this)
                        .setTitle("选择图片来源")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == SELECT_FROM_ALBUM) {
                                    //从相册选择图片
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"选择图片"),SELECT_FROM_ALBUM);
                                }else {
                                    //拍摄照片
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    mPhotoFile = TransactionLab.get().getPhotoFile(transactionn, TransactionActivity.this);
                                    Uri uri = Uri.fromFile(mPhotoFile);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    startActivityForResult(intent,TAKE_PHOTOS);
                                }
                            }
                        }).create().show();
            }
        });


        tv_date.setText(getDate());
        tv_time.setText(getTime());
    }

    @Override
    protected void onResume() {
        //防止动画问题留存
        til_title.setHint("标题");
        til_memo.setHint("备注");
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //防止动画问题留存
        til_title.setHint("");
        til_memo.setHint("");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //相册的回传
            if(requestCode == SELECT_FROM_ALBUM) {
                uri = data.getData();
                iv_photo.setBackgroundColor(Color.WHITE);
                Glide.with(this)
                        .load(uri)
                        .into(iv_photo);
            } else if (requestCode == TAKE_PHOTOS) {
                //相机的回传
                if (mPhotoFile != null) {
                    Glide.with(this)
                            .load(mPhotoFile)
                            .into(iv_photo);
                }
            }
        }else {
            Snackbar.make(getWindow().getDecorView(),"加载失败",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_transaction_menu,menu);
        return true;
    }


    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        return sdf.format(new Date());
    }

    private String getTime(){
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        return ""+(calendar.get(Calendar.HOUR_OF_DAY)+1)+":00";
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;

        tv_date.setText(this.year+"年"+this.month+"月"+this.day+"日");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hour = hourOfDay;
        min = minute;
        String hourstr = hour+"";
        String minstr = min + "";
        if (hour < 10) {
            hourstr = "0"+hourstr;
        }
        if (min < 10) {
            minstr = "0" + minstr;
        }
        tv_time.setText(hourstr+":"+minstr);
    }
}
