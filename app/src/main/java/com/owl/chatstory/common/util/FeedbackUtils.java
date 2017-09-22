package com.owl.chatstory.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.owl.chatstory.R;


/**
 * Created by lebron on 2017/7/24.
 */

public class FeedbackUtils {
    private static final String MAILTO = "mailto:" + "";
    private static final String EMAIL = "hypotato3@gmail.com";

    public static void startFeedbackActivity(Context context) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse(MAILTO));
            String feedbackContent = context.getString(R.string.feedback_message, android.os.Build.MODEL,
                    android.os.Build.VERSION.RELEASE);
            String versionName = DeviceUtils.getVersionName(context);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, TextUtils.isEmpty(versionName) ? "" : context.getString(R.string.feedback_app_version, versionName));
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedbackContent);
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
            if (!(context instanceof Activity)) {
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.feedback_no_email_installed_txt), Toast.LENGTH_SHORT).show();
        }

    }
}
