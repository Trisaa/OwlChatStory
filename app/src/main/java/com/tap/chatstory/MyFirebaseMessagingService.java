package com.tap.chatstory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tap.chatstory.chat.DirectoryActivity;
import com.tap.chatstory.common.util.Constants;
import com.tap.chatstory.user.message.MessageActivity;

import java.util.Locale;
import java.util.Map;

/**
 * Created by lebron on 2018/3/1.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Lebron", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Lebron", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Lebron", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, Map<String, String> dataMap) {
        Intent intent = new Intent();
        String type = dataMap.get("type");
        String language = dataMap.get("language");
        boolean isLanguageSame = true;
        if (type == null) {
            return;
        }
        try {
            String country = Locale.getDefault().getCountry().toLowerCase();
            String notificationLang;
            if (Constants.LANGUAGE_CHINESE.equals(language)) {
                notificationLang = "cn";
            } else {
                notificationLang = "tw";
            }
            isLanguageSame = country.equals(notificationLang);
        } catch (Exception e) {

        }
        if (type.equals(String.valueOf(Constants.MESSAGE_FICTION)) || type.equals(String.valueOf(Constants.MESSAGE_STAR))) {
            //推送小说或者推送收藏更新
            if (isLanguageSame) {
                intent.setClass(this, DirectoryActivity.class);
                intent.putExtra(DirectoryActivity.EXTRA_FICTION_ID, dataMap.get("ifiction_id"));
            } else {
                intent.setClass(this, MainActivity.class);
            }
        } else {
            intent.setClass(this, MessageActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "firebase";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_notify)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
