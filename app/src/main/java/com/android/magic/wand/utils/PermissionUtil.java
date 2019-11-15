package com.android.magic.wand.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final int PERMISSIONS_CODE_APK_INFO = 1;

    public static boolean checkPermissions(Activity activity) {
        for (String permission : PERMISSIONS) {
            UpdateLog.d("PermissionUtil", "checkPermissions:" + permission);
            if (checkSelfPermission(activity, permission)) {
                UpdateLog.d("PermissionUtil", " checkPermissions true");
                return true;
            }
        }
        UpdateLog.d("PermissionUtil", " checkPermissions false");
        return false;
    }

    @SuppressLint("NewApi")
    public static boolean checkSelfPermission(Activity activity,
                                              String permission) {
        if (activity != null) {
            return activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @SuppressLint("NewApi")
    public static boolean requirePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        List<String> permissions = new ArrayList<>();
        String[] permissionlist = PERMISSIONS;
        UpdateLog.d("PermissionUtil", "requirePermission...");
        for (String permission : permissionlist) {
            if (checkSelfPermission(activity, permission)) {
                UpdateLog.d("PermissionUtil", "requirePermissions:" + permission);
                permissions.add(permission);
            }
        }
        if (permissions.isEmpty()) {
            return false;
        }
        UpdateLog.d("PermissionUtil", "requirePermissions permissions:" + permissions.size());
        activity.requestPermissions(permissions.toArray(new String[permissions.size()]),
                PERMISSIONS_CODE_APK_INFO);
        return true;
    }

}
