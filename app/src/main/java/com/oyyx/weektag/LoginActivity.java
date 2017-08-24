package com.oyyx.weektag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN ="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    private Pattern mPattern = Pattern.compile(EMAIL_PATTERN);




    @BindView(R.id.username_til)
    TextInputLayout userName_til;

    @BindView(R.id.email_address_til)
    TextInputLayout emailAddress_til;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        actionBar.setTitle("");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done:
                hideKeyBoard();

                String username = userName_til.getEditText().getText().toString();
                String emailaddress = emailAddress_til.getEditText().getText().toString();

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
