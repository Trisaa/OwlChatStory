package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

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

}
