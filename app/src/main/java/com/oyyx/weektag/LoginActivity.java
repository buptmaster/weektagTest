package com.oyyx.weektag;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "/^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$/";
    private Pattern mPattern = Pattern.compile(EMAIL_PATTERN);


    @BindView(R.id.username_til)
    TextInputLayout userName_til;

    @BindView(R.id.email_address_til)
    TextInputLayout emailAddress_til;

    @BindView(R.id.information_confirm_button)
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            confirmButton.performClick();
            return false;
        }
        return true;
    }

    @OnClick(R.id.information_confirm_button)
    public void confirmYourInformation(){
        hideKeyBoard();

        String username = userName_til.getEditText().getText().toString();
        String emailaddress = emailAddress_til.getEditText().getText().toString();

        if(validateEmail(emailaddress)&&username.equals("")){
            setResult(RESULT_OK);
            finish();
            return;
        }

        if(!validateEmail(emailaddress)){
            emailAddress_til.setError("不是有效的邮箱地址");
        }
        if(!username.equals("")){
            userName_til.setError("用户名不能为空");
        }
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
