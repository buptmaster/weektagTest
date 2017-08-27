package com.oyyx.weektag;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Vibrator;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.util.Date;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;

/**
 * 展示详细内容的一个Activity
 *
 */

public class DetailActivity extends AppCompatActivity {

    //每个transaction的特定uuid
    private String uuid;

    //计时器
    private CountdownView mCountdownView;
    //浮动按钮
    private FloatingActionButton exportToCalendar;

    private ImageView detail_iv;

    private NestedScrollView mNestedScrollView;

    private Transactionn transactionn;

    private WaveView mWaveView;

    //照片的特定uri
    private String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //初始化数据库
        LitePal.initialize(this);

        Intent intent = getIntent();

        //获取从MainActivity的intent并从其中获取transaction的对象
        transactionn = intent.getParcelableExtra("transaction");

        //标题
        String title = transactionn.getTitle();
        //目标时间
        long targetTime = transactionn.getTime();
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


        mCountdownView = (CountdownView) findViewById(R.id.cv_remaining_time);

        exportToCalendar = (FloatingActionButton) findViewById(R.id.export_to_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        detail_iv = (ImageView) findViewById(R.id.detail_iv);
        TextView detail_tv = (TextView) findViewById(R.id.detail_memo);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.sv);
        mWaveView = (WaveView) findViewById(R.id.wave);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mWaveView.performClick();
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

        if (!memo.equals("")) {
            detail_tv.setText(memo);
        }else {
            int[] str = {R.string.good_thing,R.string.bad_thing};
            Random r = new Random();
            detail_tv.setText(str[r.nextInt(1)]);
        }

        if (uri != null) {
            Glide.with(this).load(uri).into(detail_iv);
        }

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
                startActivity(Intent.createChooser(intent,"分享"));
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
}
