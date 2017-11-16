package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by lebron on 2017/11/3.
 */

public class CameraUtils {
    public static final int REQUEST_CODE_CAMERA = 101;
    public static final int REQUEST_CODE_GALLERY = 102;

    public static void chooseFromGallery(Activity context) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(pickIntent, REQUEST_CODE_GALLERY);
    }

    public static void chooseFromCamera(Activity context) {
        File file = FileUtils.getFilePath("temp.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imgUri = FileUtils.getFileUri(context, file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } catch (ActivityNotFoundException anf) {
            Toast.makeText(context, "摄像头打开失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void cropPhoto(Activity activity, Uri uri,int scaleX,int scaleY) {
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        // 修改状态栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        // 隐藏底部工具
        options.setHideBottomControls(true);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(50);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);
        // 设置源uri及目标uri
        UCrop.of(uri, Uri.fromFile(FileUtils.getFilePath(System.currentTimeMillis() + ".jpg")))
                // 长宽比
                .withAspectRatio(scaleX, scaleY)
                // 配置参数
                .withOptions(options)
                .start(activity);
    }

}
