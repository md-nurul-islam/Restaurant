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
        int num = 0;
        String id = "", title = "", body = "";
        if(intent.getExtras().get("order_id")!= null)
            id = intent.getExtras().getString("order_id");
        if(intent.getExtras().get("title")!= null)
            title = intent.getExtras().getString("title");
        if(intent.getExtras().get("body")!= null)
            body = intent.getExtras().getString("body");

        if(intent.getExtras().get("num")!= null)
            num = intent.getExtras().getInt("num");

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("order_id", id);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int num = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = Uri.parse("android.resource://com.muhib.restaurant/" + R.raw.sound);

//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.qbit)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(sound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{5000, 5000, 5000, 5000, 5000});
        notificationManager.notify(c, mNotifyBuilder.build());
        c++;

    }

}
