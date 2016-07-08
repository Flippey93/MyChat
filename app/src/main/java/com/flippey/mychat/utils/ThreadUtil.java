package com.flippey.mychat.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/8 10:32
 */
public class ThreadUtil {
    private static Executor sSingleThreadPool = Executors.newSingleThreadExecutor();
    public static void runOnSubThread(Runnable runnable) {
        sSingleThreadPool.execute(runnable);
    }
}
