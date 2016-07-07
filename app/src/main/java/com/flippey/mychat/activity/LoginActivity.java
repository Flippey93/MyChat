package com.flippey.mychat.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.utils.SpUtil;
import com.flippey.mychat.utils.StringUtil;
import com.flippey.mychat.utils.ToastUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView
        .OnEditorActionListener {

    private EditText mUserName;
    private EditText mPassWord;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUserName = (EditText) findViewById(R.id.login_username);
        mPassWord = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.login_newUser).setOnClickListener(this);
        mUserName.setOnEditorActionListener(this);
        mPassWord.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
        String username = (String) SpUtil.get(this, SP_KEY_USERNAME, "");
        String passeword = (String) SpUtil.get(this, SP_KEY_PWD, "");
        mUserName.setText(username);
        mPassWord.setText(passeword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.login_newUser:
                //跳转到注册页面
                startActivity(RegistActivity.class,false);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.login_username:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //获取输入的用户名,进行校验
                    String username =  mUserName.getText().toString().trim();
                    if (StringUtil.validateUsername(username)) {
                        mPassWord.requestFocus(View.FOCUS_RIGHT);
                    } else {
                        ToastUtil.showToast("用户名不合法", this);
                    }
                }
                break;
            case R.id.login_password:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String password = mPassWord.getText().toString().trim();
                    if (StringUtil.validatePwd(password)) {
                        login();
                    } else {
                        ToastUtil.showToast("请重新设置密码", this);
                    }
                }
                break;
        }
        return true;
    }
    //跳转到主界面
    private void login() {
    }
}
