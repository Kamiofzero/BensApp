package com.ljb.base.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class AppUtil {

    public static String getPackageName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
