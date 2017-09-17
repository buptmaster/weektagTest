package com.oyyx.weektag.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.oyyx.weektag.R;
import com.oyyx.weektag.dateBase.TransactionLab;
import com.oyyx.weektag.dateBase.Transactionn;
import com.oyyx.weektag.utils.CalendarUtils;
import com.oyyx.weektag.view.WaveView;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;

/**
 * 展示详细内容的一个Activity
 *
 */

public class DetailActivity extends AppCompatActivity {

    private static final int SELECT_FROM_ALBUM = 0;
    private static final int TAKE_PHOTOS = 1;

    //每个transaction的特定uuid
    private String uuid;

    //计时器
    private CountdownView mCountdownView;
    //浮动按钮
    private FloatingActionButton exportToCalendar;

    private ImageView detail_iv;

    private NestedScrollView mNestedScrollView;

    //事件
    private Transactionn transactionn;

    //波浪效果
    private WaveView mWaveView;

    //照片的特定uri
    private String uri;

    private File mPhotoFile;

    private CardView memo_cv;

    private TextView detail_memo;

    private TextView detail_date;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Aesthetic.attach(this);
        super.onCreate(savedInstanceState);
        setTheme(getApplication().getSharedPreferences("theme",MODE_PRIVATE).getInt("theme",R.style.myTheme));
        setContentView(R.layout.activity_detail);
        //初始化数据库
        LitePal.initialize(this);

        Intent intent = getIntent();

        //获取从MainActivity的intent并从其中获取transaction的对象
        transactionn = TransactionLab.get().getTransactionns().get(intent.getIntExtra("position",0));

        //标题
        String title = transactionn.getTitle();
        //目标时间
        final long targetTime = transactionn.getTime();
        //备注
        String memo = transactionn.getMemo();
        //为此事件选定的颜色
        int color = transactionn.getColour();
        //照片的路径
        uri = transactionn.getUri();
        //transaction唯一标识
        uuid = transactionn.getUUID();

        //剩余时间
        long remainingTime = targetTime - (new Date()).getTime();


        //初始化view
        mCountdownView = (CountdownView) findViewById(R.id.cv_remaining_time);
        exportToCalendar = (FloatingActionButton) findViewById(R.id.export_to_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        detail_iv = (ImageView) findViewById(R.id.detail_iv);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.sv);
        memo_cv = (CardView)findViewById(R.id.memo_cv);
        mWaveView = (WaveView) findViewById(R.id.wave);
        detail_memo = (TextView) findViewById(R.id.detail_memo);
        detail_date = (TextView) findViewById(R.id.detail_date);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mWaveView.performClick();
            }
        });

        detail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = {"从相册中选择", "拍照"};
                new AlertDialog.Builder(DetailActivity.this)
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
                                    mPhotoFile = TransactionLab.get().getPhotoFile(transactionn, DetailActivity.this);
                                    Uri uri = Uri.fromFile(mPhotoFile);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    startActivityForResult(intent,TAKE_PHOTOS);
                                }
                            }
                        }).create().show();
            }
        });

        memo_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(DetailActivity.this);
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("更改备忘")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String memo = editText.getText().toString();
                                if (!memo.equals("")) {
                                    transactionn.setMemo(memo);
                                    transactionn.save();
                                    detail_memo.setText(memo);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        //设置返回键
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(title);
        //启动计时器
        if(remainingTime>=0){
            mCountdownView.start(remainingTime);
        }

        if (memo != null) {
            detail_memo.setText(memo);
        } else {
            int[] str = {R.string.good_thing, R.string.bad_thing};
            Random r = new Random();
            detail_memo.setText(str[r.nextInt(1)]);
        }



        if (uri != null) {
            Glide.with(this).load(uri).into(detail_iv);
        }

        detail_date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(transactionn.getTime())));

        exportToCalendar.setBackgroundTintList(ColorStateList.valueOf(color));

        //导出至日历的操作
        exportToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.addCalendarEvent(DetailActivity.this,transactionn.getTitle(),transactionn.getMemo(),transactionn.getTime());
                Snackbar.make(getWindow().getDecorView(), "导出成功！", Snackbar.LENGTH_LONG).show();
                ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(200);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回
            case android.R.id.home:
                finish();
                return true;
            //分享
            case R.id.action_share:
                long[] times = CalendarUtils.getTime(transactionn.getTime());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"悄悄告诉你，直至" +
                        transactionn.getTitle()+
                        "还有"+times[0]+
                        "天哦！");
                startActivity(Intent.createChooser(intent,"分享至"));
                return true;
            //删除
            case R.id.action_delete:
                TransactionLab.get().deleteTransaction(uuid);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //相册的回传
            if(requestCode == SELECT_FROM_ALBUM) {
                uri = data.getData().toString();
                Glide.with(this)
                        .load(uri)
                        .into(detail_iv);
                transactionn.setUri(uri);
                transactionn.save();
            } else if (requestCode == TAKE_PHOTOS) {
                //相机的回传
                if (mPhotoFile != null) {
                    Glide.with(this)
                            .load(mPhotoFile)
                            .into(detail_iv);
                    try {
                        transactionn.setUri(mPhotoFile.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    transactionn.save();
                }
            }
        }else {
            Snackbar.make(getWindow().getDecorView(),"加载失败",Snackbar.LENGTH_LONG).show();
        }
    }
}
