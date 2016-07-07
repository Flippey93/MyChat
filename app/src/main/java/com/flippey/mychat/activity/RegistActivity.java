package com.flippey.mychat.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.utils.StringUtil;
import com.flippey.mychat.utils.ToastUtil;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/7 20:44
 */
public class RegistActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }

    private EditText mUserName;
    private EditText mPassWord;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUserName = (EditText) findViewById(R.id.regist_username);
        mPassWord = (EditText) findViewById(R.id.regist_password);
        findViewById(R.id.regist_btn).setOnClickListener(this);
        mUserName.setOnEditorActionListener(this);
        mPassWord.setOnEditorActionListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                regist();
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
                    System.out.println(username);
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
                    System.out.println(password);
                    if (StringUtil.validatePwd(password)) {
                        regist();
                    } else {
                        ToastUtil.showToast("请重新设置密码", this);
                    }
                }
                break;
        }
        return false;
    }
    //注册
    private void regist() {
    }
}
