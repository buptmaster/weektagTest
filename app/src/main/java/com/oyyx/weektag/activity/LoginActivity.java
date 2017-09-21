package com.oyyx.weektag.activity;


import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;



import com.oyyx.weektag.R;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 填写用户信息的Activity
 *
 */

public class LoginActivity extends AppCompatActivity {

    //匹配邮箱的正则表达式
    private static final String EMAIL_PATTERN ="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    private Pattern mPattern = Pattern.compile(EMAIL_PATTERN);


    //用户名
    @BindView(R.id.username_til)
    TextInputLayout userName_til;

    //邮箱地址
    @BindView(R.id.email_address_til)
    TextInputLayout emailAddress_til;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getApplication().getSharedPreferences("theme",MODE_PRIVATE).getInt("theme",R.style.myTheme));
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //设置返回
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置空标题
        actionBar.setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            //完成
            case R.id.action_done:
                hideKeyBoard();

                String username = userName_til.getEditText().getText().toString();
                String emailaddress = emailAddress_til.getEditText().getText().toString();

                //验证逻辑
                if(!validateEmail(emailaddress)){
                    emailAddress_til.setError("不是有效的邮箱地址");
                    return true;
                }
                if(username.equals("")){
                    userName_til.setError("用户名不能为空");
                    return true;                }
                else {
                    emailAddress_til.setErrorEnabled(false);
                    userName_til.setErrorEnabled(false);
                    Intent data = new Intent();
                    data.putExtra("username", username);
                    data.putExtra("emailaddress", emailaddress);
                    setResult(RESULT_OK,data);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_transaction_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //在用户点击完成隐藏键盘
    private void hideKeyBoard(){
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean validateEmail(String str){
        return mPattern.matcher(str).matches();
    }


}
