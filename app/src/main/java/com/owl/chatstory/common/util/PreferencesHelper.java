package com.owl.chatstory.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.owl.chatstory.MainApplication;

/**
 * Created by lebron on 2017/8/31.
 */

public class PreferencesHelper {
    public static final String SETTING = "owlchat";
    //登录过的用户ID
    public static final String KEY_USER_ID = "KEY_USER_ID";
    //登录过的用户
    public static final String KEY_USER = "KEY_USER";
    //Token值
    public static final String KEY_TOKEN = "KEY_TOKEN";
    //首页分类数据
    public static final String KEY_CATEGORY_LIST = "KEY_CATEGORY_LIST";

    private final static SharedPreferences mPref = MainApplication.getAppContext().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    private static PreferencesHelper mInstance = null;

    private PreferencesHelper() {
    }

    public static PreferencesHelper getInstance() {
        if (mInstance == null) {
            synchronized (PreferencesHelper.class) {
                if (mInstance == null) {
                    mInstance = new PreferencesHelper();
                }
            }
        }
        return mInstance;
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public void setLong(String key, long value) {
        mPref.edit().putLong(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        mPref.edit().putBoolean(key, value).apply();
    }

    public void setString(String key, String value) {
        mPref.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        mPref.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public boolean isLogined() {
        return !TextUtils.isEmpty(mPref.getString(KEY_USER_ID, ""));
    }
}
