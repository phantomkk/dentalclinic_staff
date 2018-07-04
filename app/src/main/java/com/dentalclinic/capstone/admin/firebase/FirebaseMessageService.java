package com.dentalclinic.capstone.admin.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dentalclinic.capstone.admin.api.responseobject.FeedbackResponse;
import com.dentalclinic.capstone.admin.utils.AppConst;
import com.dentalclinic.capstone.admin.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

public class FirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
        String responseType = message.getData().get("type");
        if (responseType.equals(AppConst.RESPONSE_FEEDBACK)) {
            try {
                FeedbackResponse response =
                        Utils.parseJson(message.getData().get("body"), FeedbackResponse.class);
                Map<String, String> map = message.getData();
                String title = map.get("title");
                String msg = map.get("message");
//                showNotifications(title, msg, response);
            } catch(JsonSyntaxException ex) {
                ex.printStackTrace();
                showToast(ex.getMessage());
            }
        }
    }///End oncreated

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
//    private void sendNotificationFinal(String messageBody) {
//        Intent intent = new Intent(this, FeedbackActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = "XXX";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("FCM Message")
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    private void showToast(String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

//    private void showNotifications(String title, String msg, FeedbackResponse response) {
//        String channelId = AppConst.CHANNEL_FEEDBACK;
//        Intent i = new Intent(this, FeedbackActivity.class);
//        i.putExtra(AppConst.TREATMENT_DETAIL_BUNDLE, response);
//        Log.d(AppConst.DEBUG_TAG, "FirebaseMessageService:RUN");
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManager manager =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new NotificationCompat.Builder(this, channelId)
//                .setContentText(msg)
//                .setContentTitle(title)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .build();
//// Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//        manager.notify(0, notification);
//    }


}
