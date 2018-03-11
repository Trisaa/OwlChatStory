package com.tap.chatstory.common.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tap.chatstory.BuildConfig;

import java.io.File;

/**
 * Created by lebron on 2017/11/15.
 */

public class FirebaseUtil {
    public static void upLoadFile(String path, final OnUploadListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://owlchat-55f2c.appspot.com");
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(path));
        String folder = BuildConfig.DEBUG ? "test/" : "images/";
        StorageReference riversRef = storageRef.child(folder + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                if (listener != null) {
                    listener.onFailure();
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (listener != null) {
                    Log.i("Lebron", " upload success " + downloadUrl.toString());
                    listener.onSuccess(downloadUrl.toString());
                }
            }
        });
    }

    public interface OnUploadListener {
        void onFailure();

        void onSuccess(String url);
    }
}
