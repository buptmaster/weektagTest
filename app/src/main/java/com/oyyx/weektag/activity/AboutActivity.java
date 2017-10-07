package com.oyyx.weektag.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.oyyx.weektag.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MY_GITHUB = "https://github.com/masteraux1/weektagTest";

    @BindView(R.id.ll_card_about_2_email)
    LinearLayout emailLayout;

    @BindView(R.id.ll_card_about_2_git_hub)
    LinearLayout githubLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        emailLayout.setOnClickListener(this);
        githubLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_card_about_2_email:
                //TODO
                break;
            case R.id.ll_card_about_2_git_hub:
                intent.setData(Uri.parse(MY_GITHUB));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
