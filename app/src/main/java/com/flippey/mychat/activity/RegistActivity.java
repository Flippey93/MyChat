package com.flippey.mychat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flippey.mychat.R;
import com.flippey.mychat.entity.User;
import com.flippey.mychat.utils.SpUtil;
import com.flippey.mychat.utils.StringUtil;
import com.flippey.mychat.utils.ThreadUtil;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.listener.SaveListener;

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
            case R.id.regist_btn:
                regist();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.regist_username:
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //获取输入的用户名,进行校验
                    String username =  mUserName.getText().toString().trim();
                    System.out.println(username);
                    if (StringUtil.validateUsername(username)) {
                        mPassWord.requestFocus(View.FOCUS_RIGHT);
                    } else {
                        showToast("用户名不合法");
                    }
                }
                break;
            case R.id.regist_password:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String password = mPassWord.getText().toString().trim();
                    System.out.println(password);
                    if (StringUtil.validatePwd(password)) {
                        regist();
                    } else {
                        showToast("请重新设置密码");
                    }
                }
                break;
        }
        return false;
    }
    //注册
    private void regist() {
        final String username = mUserName.getText().toString().trim();
        final String password = mPassWord.getText().toString().trim();
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
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //显示进度条对话框
        final ProgressDialog dialog = makeDialog("正在注册..........");
        dialog.show();
        //将user对象注册到bmob
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //将用户注册到环信
                ThreadUtil.runOnSubThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //注册用户名密码到环信
                            EMClient.getInstance().createAccount(username, password);
                            //保存用户名和密码到sp
                            SpUtil.put(RegistActivity.this,SP_KEY_USERNAME,username);
                            SpUtil.put(RegistActivity.this, SP_KEY_PWD, password);
                            //跳转到登陆界面
                            startActivity(LoginActivity.class, true);
                            showToast("注册成功....");
                        } catch (Exception e) {
                            System.out.println(e+"...........");
                            e.printStackTrace();
                            //环信注册失败
                            showToast("注册失败：e=" + e);
                            user.delete(RegistActivity.this);
                        } finally {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                showToast("注册失败：s" + s);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }
}
