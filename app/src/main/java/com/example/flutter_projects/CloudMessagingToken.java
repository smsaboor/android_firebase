package com.example.flutter_projects;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CloudMessagingToken extends FirebaseMessagingService {
    private static final String TAG = "cloud message";

    // Override onNewToken to get new token
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            pushNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    public void pushNotification(String title, String message) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.gfg, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(bitmapDrawable.getBitmap())
                .bigLargeIcon(largeIcon)
                .setBigContentTitle("Image Sent by Raman")
                .setSummaryText("Image Message");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        final String CHANNEL_ID = "notification_channel";
        final int NOTIFICATION_ID = 100;
        Intent iNotify = new Intent(getApplicationContext(), MainActivityEmail.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 100, iNotify, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Custom Channel";
            String description = "Channel for push notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.gfg)
                    .setContentIntent(pi)
                    .setContentTitle(title)
                    .setSubText(message)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
//            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Cannel", NotificationManager.IMPORTANCE_HIGH));
        } else {

            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.gfg)
                    .setContentIntent(pi)
                    .setContentTitle(title)
                    .setSubText(message)
                    .setAutoCancel(true)
                    .build();
        }

        if(notificationManager!=null){
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}
