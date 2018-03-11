package com.tap.chatstory.common.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by lebron on 2017/11/3.
 */

public class FileUtils {
    public static File getFilePath(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Owl", fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static Uri getFileUri(Context context, File file) {
        Uri uri;
        if (DeviceUtils.isAboveN()) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static boolean deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists())
                return file.delete();
        }
        return false;
    }
}
