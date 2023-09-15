package com.ljb.downloadx.util;

import android.util.Log;


public class DownloadLog {

    private static boolean isDebug = true;


    private static String TAG = "tag";


    public static void setDebug(boolean flag) {
        isDebug = flag;
    }


    public static void i(String tag, String msg) {
        if (!isDebug)
            return;
        Log.i(TAG, tag + " -- " + msg);
    }
}
