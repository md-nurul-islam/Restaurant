package com.muhib.restaurant.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.muhib.restaurant.R;
import com.muhib.restaurant.activity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
//    public static String NOTIFICATION_ID = "notification_id";
//    public static String NOTIFICATION = "notification";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification notification = intent.getParcelableExtra(NOTIFICATION);
//        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
//        notificationManager.notify(notificationId, notification);
//    }
    int c =0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.logo)
                .setContentTitle("Alarm Fired")
                .setContentText("Events to be Performed").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(c, mNotifyBuilder.build());
        c++;

    }

}
