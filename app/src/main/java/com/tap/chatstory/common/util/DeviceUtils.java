package com.tap.chatstory.common.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.tap.chatstory.MainApplication;

/**
 * Created by lebron on 2017/7/17.
 */

public class DeviceUtils {

    /**
     * 通过包名判断是否安装
     *
     * @param pkg 包名
     * @return
     */
    public static boolean isApkInstalled(String pkg) {
        if (TextUtils.isEmpty(pkg)) {
            return false;
        } else {
            try {
                MainApplication.getAppContext().getPackageManager().getPackageInfo(pkg, 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static String getVersionName(Context context) {
        ComponentName cn = new ComponentName(context, context.getClass());
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(cn.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return info.versionName;
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return 100;
        }
        return pi.versionCode;
    }

    public static boolean isAboveLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isAboveN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static String getUri(int res) {
        return Uri.parse("android.resource://" + MainApplication.getAppContext().getPackageName() + "/" + res).toString();
    }
}
