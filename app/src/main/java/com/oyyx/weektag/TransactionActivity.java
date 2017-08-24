package com.oyyx.weektag;


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
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.litepal.LitePal;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



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


    private FloatingActionButton fab_date;
    private FloatingActionButton fab_color_picker;
    private FloatingActionButton fab_time;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private ColorPickerDialog colorPickerDialog;
    private TimePickerDialog timePickerDialog;

    private TextView tv_date;
    private TextView tv_time;

    private ImageView iv_photo;


    private TextInputLayout til_title;
    private TextInputLayout til_memo;

    private Transactionn transactionn;

    private File mPhotoFile;

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

                transactionn.setTime(CalendarUtils.timeToDate(year,month,day,hour,min));
                transactionn.setColour(mSelectColor);
                transactionn.setTitle(title);
                transactionn.setMemo(memo);
                if(uri != null) {
                    transactionn.setUri(uri.toString());
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
        setContentView(R.layout.activity_transaction);
        LitePal.initialize(this);

        mSelectColor = ContextCompat.getColor(this, R.color.flamingo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加事件");

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuClick);

        transactionn = new Transactionn();

        til_title = (TextInputLayout) findViewById(R.id.title_til);
        til_memo = (TextInputLayout) findViewById(R.id.memo_til);

        tv_date = (TextView) findViewById(R.id.date_tv);
        tv_time = (TextView) findViewById(R.id.time_tv);

        tv_date.setText(getDate());
        tv_time.setText(getTime());

        fab_color_picker = (FloatingActionButton) findViewById(R.id.color_picker_fab);
        fab_date = (FloatingActionButton) findViewById(R.id.date_fab);
        fab_time = (FloatingActionButton) findViewById(R.id.time_fab);

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

        iv_photo = (ImageView) findViewById(R.id.photo_iv);
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
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"选择图片"),SELECT_FROM_ALBUM);
                                }else {
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if(requestCode == SELECT_FROM_ALBUM) {
                uri = data.getData();
                iv_photo.setBackgroundColor(Color.WHITE);
                Glide.with(this)
                        .load(uri)
                        .into(iv_photo);
            } else if (requestCode == TAKE_PHOTOS) {
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
        month = calendar.get(Calendar.MONTH);
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
        this.month = month;
        this.day = day;
        TextPaint paint = tv_date.getPaint();
        paint.setFakeBoldText(true);
        tv_date.setText(year+"年"+month+"月"+day+"日");
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
        TextPaint paint = tv_time.getPaint();
        paint.setFakeBoldText(true);
        tv_time.setText(hourstr+":"+minstr);
    }
}
