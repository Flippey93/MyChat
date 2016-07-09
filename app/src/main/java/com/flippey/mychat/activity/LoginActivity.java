package com.flippey.mychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.utils.SpUtil;
import com.flippey.mychat.utils.StringUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
                        showToast("用户名不合法");
                    }
                }
                break;
            case R.id.login_password:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String password = mPassWord.getText().toString().trim();
                    if (StringUtil.validatePwd(password)) {
                        login();
                    } else {
                        showToast("请重新设置密码");
                    }
                }
                break;
        }
        return true;
    }
    //跳转到主界面
    private void login() {
        //检验数据
        String username = mUserName.getText().toString().trim();
        String password = mPassWord.getText().toString().trim();
        if (!StringUtil.validateUsername(username)) {
            showToast("用户名不合法");
            mUserName.requestFocus(View.FOCUS_RIGHT);
            return;
        }
        if (!StringUtil.validatePwd(password)) {
            showToast("密码不合法");
            mPassWord.requestFocus(View.FOCUS_RIGHT);
            return;
        }
        //保存用户名和密码
        SpUtil.put(this, SP_KEY_USERNAME, username);
        SpUtil.put(this, SP_KEY_PWD, password);
        final ProgressDialog dialog = makeDialog("正在登陆...");
        dialog.show();
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //跳转到主界面
                startActivity(MainActivity.class, true);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(int i, String s) {
                showToast("登陆失败...");
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }
}
