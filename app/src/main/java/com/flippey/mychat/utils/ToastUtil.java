package com.flippey.mychat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/7 20:16
 */
public class ToastUtil {
    protected static Toast mToast = null;
    public static void showToast(String msg, Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
