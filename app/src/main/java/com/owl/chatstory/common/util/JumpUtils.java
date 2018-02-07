package com.owl.chatstory.common.util;

import android.content.Intent;
import android.net.Uri;

import com.owl.chatstory.MainApplication;

/**
 * Created by lebron on 2017/7/24.
 */

public class JumpUtils {
    //浏览器应用市场跳转data
    public static final String WEB_APP_MARKET_INTENT_DATA = "https://play.google.com/store/apps/details?id=";
    //隐私政策链接
    public static final String PRIVACY_POLICY_URL = "http://52.15.164.29:8080/more/protocol";
    //Facebook主页
    public static final String FACEBOOK_PAGE_URL = "https://www.facebook.com/150735542201460/";
    private static final String APP_PKG_NAME_GP = "com.android.vending";
    private static final String APP_CLASSNAME_GP = "com.android.vending.AssetBrowserActivity";
    //应用市场跳转data
    private static final String LOCAL_APP_MARKET_INTENT_DATA = "market://details?id=";

    /**
     * 跳转到应用市场中定指定app
     *
     * @param packageName 指定应用包名
     */
    public static void jumpToMarket(String packageName) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if (DeviceUtils.isApkInstalled(APP_PKG_NAME_GP)) {
                marketIntent.setData(getUri(packageName, false));
                marketIntent.setClassName(APP_PKG_NAME_GP, APP_CLASSNAME_GP);
                MainApplication.getAppContext().startActivity(marketIntent);
            } else {
                marketIntent.setData(getUri(packageName, true));
                if (marketIntent.resolveActivity(MainApplication.getAppContext().getPackageManager()) != null) {
                    MainApplication.getAppContext().startActivity(marketIntent);
                }
            }
        } catch (android.content.ActivityNotFoundException e) {

        }
    }

    private static Uri getUri(String packageName, boolean isJumpBrowse) {
        Uri uri;
        if (isJumpBrowse) {
            uri = Uri.parse(WEB_APP_MARKET_INTENT_DATA + packageName);
        } else {
            uri = Uri.parse(LOCAL_APP_MARKET_INTENT_DATA + packageName);
        }
        return uri;
    }

    public static void jumpToBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        if (intent.resolveActivity(MainApplication.getAppContext().getPackageManager()) != null) {
            MainApplication.getAppContext().startActivity(intent);
        }
    }
}
