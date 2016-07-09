package com.flippey.mychat;

import android.test.AndroidTestCase;
import android.util.Log;

import com.flippey.mychat.utils.StringUtil;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/9 10:19
 */
public class MyTest extends AndroidTestCase{

    private static final String TAG = "test";

    public void testInitial() {
        String initial = StringUtil.getInitial("张三");
        Log.d(TAG, "testInitial: "+initial);
    }
}
