package com.ljb.base.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    public static final String PERMISSION_BLUETOOTH_CONNECT = Manifest.permission.BLUETOOTH_CONNECT;// api 32
//    public static final String PERMISSION_BLUETOOTH_SCAN = Manifest.permission.BLUETOOTH_SCAN;//  api 32

    public static boolean checkAndRequestPermission(Context context, String[] permissions, int requestCode) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            if (permissionList.size() != 0) {
                String[] strings = new String[permissionList.size()];
                String[] realPermissions = permissionList.toArray(strings);
                ActivityCompat.requestPermissions(((Activity) context), realPermissions, requestCode);
                return false;
            } else
                return true;

        } else {
            return true;
        }
    }


    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, RequestPermissionCallback callback) {

        if (callback != null) {
            ArrayList<String> permissionsGrantList = new ArrayList<>();
            ArrayList<String> permissionDeniedList = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGrantList.add(permissions[i]);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionDeniedList.add(permissions[i]);
                }
            }
            callback.onRequestResult(requestCode, permissionsGrantList, permissionDeniedList);
        }
    }

    public interface RequestPermissionCallback {
        void onRequestResult(int requestCode, List<String> permissionGrant, List<String> permissionDenied);

    }


}
