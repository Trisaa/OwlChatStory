package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lebron on 2017/7/24.
 */

public class ShareUtils {
    //浏览器应用市场跳转data
    public static final String WEB_APP_MARKET_INTENT_DATA = "https://play.google.com/store/apps/details?id=";
    public static final String SHARE_CHAPTER_BASE_URL = "http://52.15.164.29:8080/share/index.html?";
    //public static final String SHARE_CHAPTER_BASE_URL = "http://47.94.243.139:9090/share/index.html?";


    public static final String getShareChapterUrl(String type, String chapterId) {
        return SHARE_CHAPTER_BASE_URL + "type=" + type + "&id=" + chapterId;
    }

    public static final String getShareAppUrl(Context context) {
        return WEB_APP_MARKET_INTENT_DATA + context.getPackageName();
    }

    public static void shareToFacebook(Activity activity, CallbackManager callbackManager, ShareModel shareModel, final OnShareListener listener) {
        ShareDialog shareDialog = new ShareDialog(activity);
        if (callbackManager != null) {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    if (listener != null) {
                        listener.onShare(true);
                    }
                }

                @Override
                public void onCancel() {
                    if (listener != null) {
                        listener.onShare(false);
                    }
                }

                @Override
                public void onError(FacebookException error) {
                    Log.i("ShareUtils", " FacebookException " + error.toString());
                    if (listener != null) {
                        listener.onShare(false);
                    }
                }
            });
        }
        ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
        if (shareModel != null && shareModel.getUrl() != null) {
            builder.setContentUrl(Uri.parse(shareModel.getUrl()));
        } else {
            builder.setContentUrl(Uri.parse(getShareAppUrl(activity)));
        }
        if (shareModel != null && shareModel.getImage() != null) {
            Log.i("ShareUtils", " image " + shareModel.getImage());
            builder.setImageUrl(Uri.parse(shareModel.getImage()));
        }
        if (shareModel != null && shareModel.getContent() != null) {
            builder.setContentTitle(shareModel.getContent());
        }

        if (builder != null) {
            ShareLinkContent content = builder.build();
            shareDialog.show(content);
        }
    }

    public static void shareToTwitter(Context context, ShareModel model) {
        TweetComposer.Builder builder = new TweetComposer.Builder(context);
        try {
            if (model != null && model.getUrl() != null) {
                builder.url(new URL(model.getUrl()));
            } else {
                builder.url(new URL(getShareAppUrl(context)));
            }
            if (model != null && model.getImage() != null) {
                builder.image(Uri.parse(model.getImage()));
            }
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

    */

    /**
     * 系统默认分享
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

    public interface OnShareListener {
        void onShare(boolean success);
    }

}
