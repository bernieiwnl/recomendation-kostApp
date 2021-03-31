package com.example.rekomendasikostapp.NOTIFICATIONS;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class OreoNotification extends ContextWrapper {
    private static final String CHANNEL_ID ="com.example.rekomendasikostapp";
    private static final String CHANNEL_NAME ="rekomendasikostapp";
    private NotificationManager notificationManager;
    public OreoNotification(Context base)
    {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.Q)
    private void createChannel()
    {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setShowBadge(true);
        channel.setAllowBubbles(true);
        channel.setLockscreenVisibility(Notification.BADGE_ICON_SMALL);
        getManager().createNotificationChannel(channel);

    }
    public NotificationManager getManager()
    {
        if(notificationManager == null)
        {
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    @TargetApi(Build.VERSION_CODES.Q)
    public Notification.Builder getOreNotification (String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon)
    {
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(Integer.parseInt(icon))
                .setSound(soundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);
    }


}
