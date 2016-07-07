package com.flippey.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.flippey.mychat.R;

public class SplashActivity extends AppCompatActivity {

    private boolean isLogged = false;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //检查是否已经登录,如果已经登录直接进入主界面,否则在闪屏页停留2s,进入登录界面

        if (isLogged) {
            //直接进入主界面
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        }

    }
}
