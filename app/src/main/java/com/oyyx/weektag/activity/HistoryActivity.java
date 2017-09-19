package com.oyyx.weektag.activity;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.oyyx.weektag.R;
import com.oyyx.weektag.adapter.HistoryAdapter;
import com.oyyx.weektag.adapter.HistoryTodayAdapter;
import com.oyyx.weektag.dateBase.HistoryToday;
import com.oyyx.weektag.dateBase.ListBean;
import com.oyyx.weektag.net.HistoryApi;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 历史上的今天
 */
public class HistoryActivity extends AppCompatActivity {

    //历史上的今天key
    private static final String SECRET_CODE = "13ab6c3c3d3a43189ec8f83c04ad9928";

    //历史上的今天id
    private static final String APP_ID = "45484";

    private RecyclerView mRecyclerView;

    //数据
    private ArrayList<ListBean> mListBeen;

    private HistoryTodayAdapter mHistoryTodayAdapter;

    //网络请求
    private Retrofit mRetrofit;

    private SharedPreferences sp;
    private SharedPreferences themeSp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LitePal.initialize(this);
        super.onCreate(savedInstanceState);

        mListBeen = (ArrayList<ListBean>) DataSupport.findAll(ListBean.class);

        //主题的sp，获取当前主题
        themeSp = getApplicationContext().getSharedPreferences("theme", MODE_PRIVATE);
        setTheme(themeSp.getInt("theme", R.style.myTheme));
        sp = getApplicationContext().getSharedPreferences("token", MODE_PRIVATE);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://route.showapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryApi historyApi = mRetrofit.create(HistoryApi.class);

        Call<HistoryToday> call = historyApi.getHistoryToday(APP_ID, SECRET_CODE);

        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("历史上的今天");

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_history);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mListBeen != null) {
            mHistoryTodayAdapter = new HistoryTodayAdapter(mListBeen);
            Log.e("3423423423423", mListBeen.toString());
        }


        if(sp.getString("token",null)==null||!sp.getString("token","date").equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {

            //发起网络请求
            call.enqueue(new Callback<HistoryToday>() {
                @Override
                public void onResponse(Call<HistoryToday> call, Response<HistoryToday> response) {
                    //noinspection ConstantConditions
                    mListBeen = (ArrayList<ListBean>) response.body().getShowapi_res_body().getList();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DataSupport.deleteAll(ListBean.class);
                            mHistoryTodayAdapter = new HistoryTodayAdapter(mListBeen);
                            mRecyclerView.setAdapter(mHistoryTodayAdapter);
                            for (ListBean listBean : mListBeen) {
                                listBean.save();
                            }
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("token",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            editor.apply();

                        }
                    });
                }


                @Override
                public void onFailure(Call<HistoryToday> call, Throwable t) {
                    Toast.makeText(HistoryActivity.this,"无法连接网络",Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            });
        }
            mRecyclerView.setAdapter(mHistoryTodayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
