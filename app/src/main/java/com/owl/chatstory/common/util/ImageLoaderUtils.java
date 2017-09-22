package com.owl.chatstory.common.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lebron on 2017/9/1.
 */

public class ImageLoaderUtils {
    private static ImageLoaderUtils mInstance = null;

    private ImageLoaderUtils() {
    }

    public static ImageLoaderUtils getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtils();
                }
            }
        }
        return mInstance;
    }

    public void loadImage(Context context, Object object, ImageView view) {
        Glide.with(context).load(object).into(view);
    }

    public void loadImage(Context context, Object object, ImageView view, int placeHolderImg) {
        Glide.with(context).load(object).placeholder(placeHolderImg).into(view);
    }

    public void loadCircleImage(Context context, Object object, ImageView view) {
        Glide.with(context).load(object).bitmapTransform(new CropCircleTransformation(context)).into(view);
    }
}
