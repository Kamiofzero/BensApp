package com.ljb.base.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtil {


    public static NotificationObject createNotificationObject(Context context, String channel_id, int icon, int layoutId) {
        createNotificationChannel(context, channel_id);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layoutId);
        NotificationObject object = new NotificationObject(channel_id, remoteViews, icon);
        return object;
    }

    public static ProgressNotificationObject createProgressNotificationObject(Context context, String channel_id, String title, String text, int icon) {
        createNotificationChannel(context, channel_id);
        ProgressNotificationObject object = new ProgressNotificationObject(channel_id, title, text, icon);
        return object;
    }


    private static void createNotificationChannel(Context context, String channelID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static void startForeground(Service service, NotificationObject object) {
        Notification notification = buildNotification(service, object);
        service.startForeground(object.notificationId, notification);
    }

    public static void notify(Context context, NotificationObject object) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, object.channel_id);
        builder.setCustomContentView(object.remoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setSmallIcon(object.icon);
        notificationManager.notify(object.notificationId, builder.build());
    }


    public static void notifyProgress(Context context, ProgressNotificationObject object) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, object.channel_id);
        builder.setContentTitle(object.title)
                .setContentText(object.text)
                .setSmallIcon(object.icon)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = object.progress;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(object.notificationId, builder.build());
    }


    public static Notification showNotification(Context context, NotificationObject notificationObject) {
        return buildNotification(context, notificationObject);
    }

    public static Notification upDateNotification(Context context, NotificationObject notificationObject) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = buildNotification(context, notificationObject);
        manager.notify(1, notification);
        return notification;
    }

    private static Notification buildNotification(Context context, NotificationObject notificationObject) {
        Notification notification = new NotificationCompat.Builder(context, notificationObject.channel_id)
//                .setCustomContentView(notificationObject.remoteViews)
//                .setWhen(System.currentTimeMillis())//设置创建时间
                .setContentTitle("hello")
                .setContentText("how are you")
                .build();
        return notification;
    }


    public static class NotificationObject {


        int notificationId;
        String channel_id;
        RemoteViews remoteViews;
        int icon;



        public NotificationObject(String channel_id, RemoteViews remoteViews, int icon) {
            this.channel_id = channel_id;
            this.remoteViews = remoteViews;
            this.icon = icon;
        }

        public RemoteViews getRemoteViews() {
            return remoteViews;
        }

    }


    public static class ProgressNotificationObject {


        int notificationId;
        String channel_id;
        String title;
        String text;
        int icon;
        int progress;


        public ProgressNotificationObject(String channel_id, String title, String text, int icon) {
            this.channel_id = channel_id;
            this.title = title;
            this.text = text;
            this.icon = icon;
        }


        public void setProgress(int progress) {
            this.progress = progress;
        }
    }
}
