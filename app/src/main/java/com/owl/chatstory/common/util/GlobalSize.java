package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class GlobalSize {

    private static final Object sLock = new Object();
    private static final int DEFAULT_SCREEN_STATUSBAR = 75;
    private static final int DEFAULT_SCREEN_HEIGHT = 1920;
    private static final int DEFAULT_SCREEN_WIDTH = 1080;
    private static GlobalSize mInstance;
    private int sScreenWidth;
    private int sScreenHeight;

    private GlobalSize() {
        sScreenWidth = DEFAULT_SCREEN_WIDTH;
        sScreenHeight = DEFAULT_SCREEN_HEIGHT;
    }

    public static GlobalSize getInstance() {
        synchronized (sLock) {
            if (mInstance == null) {
                mInstance = new GlobalSize();
            }
            return mInstance;
        }
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = DEFAULT_SCREEN_STATUSBAR;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        } catch (NoSuchFieldException e) {
        } catch (ClassNotFoundException e) {
        }
        return statusHeight;
    }

    /**
     * 获得导航栏的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获得屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = null;
        int height = DEFAULT_SCREEN_HEIGHT;
        if (context instanceof Activity) {
            wm = ((Activity) context).getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(metric);
            height = metric.heightPixels;

        }
        return height;
    }

    /**
     * 获得屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = null;
        int width = DEFAULT_SCREEN_WIDTH;
        if (context instanceof Activity) {
            wm = ((Activity) context).getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;

        }
        return width;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

}
