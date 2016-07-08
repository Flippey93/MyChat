package com.flippey.mychat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flippey.mychat.utils.DialogUtil;
import com.flippey.mychat.utils.ToastUtil;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/7 10:14
 */
public abstract class BaseActivity  extends AppCompatActivity implements View.OnClickListener{
    protected static String SP_KEY_USERNAME = "username";
    protected static String SP_KEY_PWD = "password";
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView(savedInstanceState);
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();
    //跳转activity
    protected void startActivity(Class clazz, boolean flag) {
        startActivity(new Intent(this, clazz));
        if (flag) {
            finish();
        }
    }

    protected ProgressDialog makeDialog(String msg) {
        return DialogUtil.makeDialog(this, msg);
    }
    public void showToast(final String msg) {
        if (Looper.myLooper() == mHandler.getLooper()) {
            ToastUtil.showToast(msg, this);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(msg, getApplicationContext());
                }
            });
        }
    }
}
