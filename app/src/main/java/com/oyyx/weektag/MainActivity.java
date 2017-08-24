package com.oyyx.weektag;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private HistoryAdapter mHistoryAdapter;

    private TextView username;
    private TextView emailaddress;


    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LitePal.initialize(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        username = (TextView) view.findViewById(R.id.username);
        emailaddress = (TextView) view.findViewById(R.id.email_address);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,TransactionActivity.class));
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,LoginActivity.class),0);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        UpdateUI();
        sp = getApplicationContext().getSharedPreferences("userInfo", MODE_PRIVATE);
        setUserInfo(sp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateUI();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == 0) {
                String userName = data.getStringExtra("username");
                String emailAddress = data.getStringExtra("emailaddress");


                if (userName == null || emailAddress == null) {
                    username.setText("未知用户");
                    emailaddress.setText("未知邮箱");
                }else {

                    username.setText(userName);
                    emailaddress.setText(emailAddress);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", userName);
                    editor.putString("emailaddress", emailAddress);

                    editor.apply();
                    Snackbar.make(getWindow().getDecorView(),"欢迎，"+ userName,Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setUserInfo(SharedPreferences sp){
        username.setText(sp.getString("username", "未知用户"));
        emailaddress.setText(sp.getString("emailaddress", "未知邮箱"));

    }

    private void UpdateUI(){
        TransactionLab transactionLab = TransactionLab.get();
        List<Transactionn> transactionns = transactionLab.getTransactionns();

        Log.e("___________", transactionns.size() + "");

        if (mHistoryAdapter == null) {
            mHistoryAdapter = new HistoryAdapter(transactionns);
            mRecyclerView.setAdapter(mHistoryAdapter);
        }else {
            mHistoryAdapter.setTransactionns(transactionns);
            mHistoryAdapter.notifyDataSetChanged();
        }

    }

}
