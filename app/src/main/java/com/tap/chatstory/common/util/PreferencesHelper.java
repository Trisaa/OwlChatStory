package com.tap.chatstory.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tap.chatstory.MainApplication;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.ProgressModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    //是否显示过阅读提示
    public static final String KEY_CLICK_TIPS_SHOWED = "KEY_CLICK_TIPS_SHOWED";
    //是否有订阅过任何一种会员套餐
    public static final String KEY_PAID_FOR_VIP = "KEY_PAID_FOR_VIP";
    //是否已经上传过DeviceToken
    public static final String KEY_DEVICE_TOKEN_UPLOADED = "KEY_DEVICE_TOKEN_UPLOADED";
    //是否展示过了创作引导
    public static final String KEY_CREATE_GUIDE_SHOWED = "KEY_CREATE_GUIDE_SHOWED";
    //章节过关后弹出广告间隔章节数
    public static final String KEY_CHAPTER_ADS_INTERVAL = "KEY_CHAPTER_ADS_INTERVAL";
    //是否是免广告的VIP
    public static final String KEY_NO_ADS_VIP = "KEY_NO_ADS_VIP";

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

    //设置小说阅读进度
    public void setFictionProgressList(String key, List<ProgressModel> list) {
        Type listType = new TypeToken<List<ProgressModel>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(list, listType);
        mPref.edit().putString(key + "progress", json).apply();
    }

    /**
     * 获取小说章节的阅读进度
     *
     * @param fictionId
     * @return
     */
    public List<ProgressModel> getFictionProgressList(String fictionId) {
        String temp = mPref.getString(fictionId + "progress", "");
        Type listType = new TypeToken<List<ProgressModel>>() {
        }.getType();
        Gson gson = new Gson();
        List<ProgressModel> target = gson.fromJson(temp, listType);
        if (target == null) {
            target = new ArrayList<>();
        }
        return target;
    }

    public List<FictionModel> getLocalChapterList(String key) {
        Gson gson = new Gson();
        String chapters = mPref.getString(key, "");
        List<FictionModel> list = gson.fromJson(chapters, new TypeToken<List<FictionModel>>() {
        }.getType());
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void removeLocalChapter(FictionModel model) {
        String key = model.getIfiction_id() + model.getLanguage() + "chapter";
        List<FictionModel> list = getLocalChapterList(key);
        for (FictionModel fictionModel : list) {
            if (fictionModel.getNum() == model.getNum()) {
                list.remove(fictionModel);
                break;
            }
        }
        setString(key, new Gson().toJson(list));
    }
}
