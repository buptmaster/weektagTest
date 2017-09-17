package com.oyyx.weektag.activity;

import android.annotation.TargetApi;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import com.oyyx.weektag.R;
import com.oyyx.weektag.adapter.ChatAdapter;
import com.oyyx.weektag.dateBase.ChatRobot;
import com.oyyx.weektag.model.ChatModel;
import com.oyyx.weektag.model.ItemModel;
import com.oyyx.weektag.net.RobotApi;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 小机器人的功能
 */
public class RobotActivity extends AppCompatActivity {

    private static final String KEY = "57009182cbaa480eb19bbca87f35124a";

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private TextView tvSend;
    private String content;

    private SharedPreferences sp;

    private Retrofit retrofit;

    private RobotApi robotApi;

    private ArrayList<ItemModel> data;
    private volatile ArrayList<ItemModel> temp;

    private SharedPreferences themeSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        themeSp = getApplicationContext().getSharedPreferences("theme", MODE_PRIVATE);

        setTheme(themeSp.getInt("theme", R.style.myTheme));

        setContentView(R.layout.activity_robot_acitivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("小机器人");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);
        sp = getApplicationContext().getSharedPreferences("userInfo", MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.tuling123.com/openapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        robotApi = retrofit.create(RobotApi.class);


        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = (EditText) findViewById(R.id.et);
        tvSend = (TextView) findViewById(R.id.tvSend);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter = new ChatAdapter());
        if (data == null) {
            data = new ArrayList<>();
        }
        adapter.replaceAll(data);
        initData();
    }


    private void initData() {
        tvSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                content = et.getText().toString();
                temp = new ArrayList<>();
                data.addAll(temp);
                ChatModel model = new ChatModel();
                String path = sp.getString("userimage", null);
                if (path != null) {
                    model.setIcon(path);
                }
                model.setContent(content);
                temp.add(new ItemModel(ItemModel.CHAT_B, model));
                adapter.addAll(temp);
                et.setText("");

                Log.e("sdfasdfasdfasdfasfasdf", content);

                Call<ChatRobot> call = robotApi.getChatInfo(KEY, content);
                call.enqueue(new Callback<ChatRobot>() {
                    @Override
                    public void onResponse(@NonNull Call<ChatRobot> call, @NonNull Response<ChatRobot> response) {
                        @SuppressWarnings("ConstantConditions") final String reply = response.body().getText();

                        Log.e("RObot", reply);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<ItemModel> temp = new ArrayList<>();
                                ChatModel robotModel = new ChatModel();
                                robotModel.setContent(reply);
                                temp.add(new ItemModel(ItemModel.CHAT_A, robotModel));
                                adapter.addAll(temp);
                                data.addAll(temp);
                                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                recyclerView.invalidate();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ChatRobot> call, Throwable t) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<ItemModel> temp = new ArrayList<>();
                                ChatModel robotModel = new ChatModel();
                                robotModel.setContent("网络已断开！");
                                temp.add(new ItemModel(ItemModel.CHAT_A, robotModel));
                                adapter.addAll(temp);
                                data.addAll(temp);
                                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                recyclerView.invalidate();
                            }
                        });
                    }
                });

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putSerializable("data", data);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //noinspection unchecked
        data = (ArrayList<ItemModel>) savedInstanceState.getSerializable("data");
        super.onRestoreInstanceState(savedInstanceState);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

