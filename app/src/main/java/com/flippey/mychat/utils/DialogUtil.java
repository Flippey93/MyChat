package com.flippey.mychat.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 10:22
 */
public class DialogUtil {

    public static ProgressDialog makeDialog(Context context, String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        return dialog;
    }
}
