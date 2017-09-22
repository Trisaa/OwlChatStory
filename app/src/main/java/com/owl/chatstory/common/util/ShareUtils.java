package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lebron on 2017/7/24.
 */

public class ShareUtils {
    //浏览器应用市场跳转data
    public static final String WEB_APP_MARKET_INTENT_DATA = "https://play.google.com/store/apps/details?id=";

    public static void shareToFacebook(Activity activity, CallbackManager callbackManager, ShareModel shareModel) {
        ShareDialog shareDialog = new ShareDialog(activity);
        if (callbackManager != null) {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Log.i("ShareUtils", " FacebookException " + error.toString());
                }
            });
        }
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(WEB_APP_MARKET_INTENT_DATA + activity.getPackageName()))
                .build();
        shareDialog.show(content);
    }

    public static void shareToTwitter(Context context, ShareModel model) {
        TweetComposer.Builder builder = new TweetComposer.Builder(context);
        try {
            builder.url(new URL(WEB_APP_MARKET_INTENT_DATA + context.getPackageName()));
        } catch (MalformedURLException e) {

        }
        builder.text(model.getContent());
        builder.show();
    }


    /*public static void shareMp4(Context context, String packageName, ShareModel model) {
        try {
            Uri videoUri = null;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("video/mp4");
            share.putExtra(Intent.EXTRA_STREAM, videoUri);
            if (TextUtils.isEmpty(packageName)) {
                //context.startActivity(Intent.createChooser(share, context.getString(R.string.shareutil_share_title)));
            } else {
                share.setPackage(packageName);
                context.startActivity(share);
            }
        } catch (Exception e) {
            //Toast.makeText(context, context.getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareToMessenger(Activity activity, ShareModel shareModel) {
        if (!TextUtils.isEmpty(shareModel.path)) {
            Uri videoUri = null;
            if (!TextUtils.isEmpty(shareModel.path)) {
                File file = new File(shareModel.path);
                videoUri = FileProvider.getUriForFile(
                        activity,
                        activity.getPackageName() + ".provider",
                        file);
            }
            String mimeType = "video/mp4";

            // contentUri points to the content being shared to Messenger
            ShareToMessengerParams shareToMessengerParams =
                    ShareToMessengerParams.newBuilder(videoUri, mimeType)
                            .build();

            // Sharing from an Activity
            MessengerUtils.shareToMessenger(activity, 1, shareToMessengerParams);
        }
    }

    *//**
     * 系统默认分享
     *
     * @param pkgName 需要分享的应用包名
     *//*
    public static void shareByDefault(Context context, String pkgName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);//设置分享行为
        intent.setType("text/plain");//设置分享内容的类型
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));//添加分享内容标题
        intent.putExtra(Intent.EXTRA_TEXT, WEB_APP_MARKET_INTENT_DATA + context.getPackageName());
        if (!TextUtils.isEmpty(pkgName)) {
            intent.setPackage(pkgName);
        }
        intent = Intent.createChooser(intent, context.getString(R.string.app_name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }*/

}
