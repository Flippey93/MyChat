package com.flippey.mychat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/7 10:14
 */
public abstract class BaseActivity  extends Activity implements View.OnClickListener{
    protected static String SP_KEY_USERNAME = "username";
    protected static String SP_KEY_PWD = "password";
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
}
