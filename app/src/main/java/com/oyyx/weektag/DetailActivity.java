package com.oyyx.weektag;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.litepal.LitePal;

import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class DetailActivity extends AppCompatActivity {

    private String uuid;

    private CountdownView mCountdownView;
    private FloatingActionButton exportToCalendar;
    private ImageView detail_iv;

    private Transactionn transactionn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        LitePal.initialize(this);

        Intent intent = getIntent();

        transactionn = intent.getParcelableExtra("transaction");

        String title = transactionn.getTitle();
        long targetTime = transactionn.getTime();
        String memo = transactionn.getMemo();
        int color = transactionn.getColour();
        Uri uri = Uri.parse(transactionn.getUri());
        uuid = transactionn.getUUID();
        Log.e("---__--_", "" + uri);

        long remainingTime = targetTime - (new Date()).getTime();


        mCountdownView = (CountdownView) findViewById(R.id.cv_remaining_time);

        exportToCalendar = (FloatingActionButton) findViewById(R.id.export_to_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_layout);
        detail_iv = (ImageView) findViewById(R.id.detail_iv);
        TextView detail_tv = (TextView) findViewById(R.id.detail_memo);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(title);
        if(remainingTime>=0){
            mCountdownView.start(remainingTime);
        }

        if (memo != null) {
            detail_tv.setText(memo);
        }

        if (uri != null) {
            Glide.with(this).load(uri).into(detail_iv);
        }

        exportToCalendar.setBackgroundTintList(ColorStateList.valueOf(color));

        exportToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.addCalendarEvent(DetailActivity.this,transactionn.getTitle(),transactionn.getMemo(),transactionn.getTime());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
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
