package com.muhib.restaurant.fcm;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.muhib.restaurant.MyApplication;
import com.muhib.restaurant.R;
import com.muhib.restaurant.activity.MainActivity;

import java.util.Calendar;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage message) {


        Log.d("TAG", "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + message.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }
//        sendMyNotification(message.getNotification().getBody());
        message.getData().get("order_id");

        //setAlarm();
        MyApplication.sendMyNotification(message.getData().get("order_id"), message.getData().get("title"), message.getData().get("body"));
    }



    private void sendMyNotification(String id, String title, String body) {

        Intent intent1 = new Intent(this, AlarmReceiver.class);
        intent1.putExtra("order_id", id);
        intent1.putExtra("title", title);
        intent1.putExtra("body", body);
        int num = (int) System.currentTimeMillis();
        intent1.putExtra("num", num);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, num,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 20000, pendingIntent);

//
//        //On click of notification it redirect to this Activity
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("order_id", id);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        int num = (int) System.currentTimeMillis();
//
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,num,intent,PendingIntent.FLAG_ONE_SHOT);
//
//
//        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
////        notificationManager.notify(0, notificationBuilder.build());
//        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
//
//
//
////        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////        Calendar calendar=Calendar.getInstance();
////// Calendar.set(int year, int month, int day, int hourOfDay, int minute, int second)
////        calendar.set(2013, Calendar.OCTOBER, 10, 12, 10, 20);
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10*1000, pendingIntent);
    }

//    private void setAlarm(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 13);
//        calendar.set(Calendar.MINUTE, 38);
//        calendar.set(Calendar.SECOND, 0);
//        Intent intent1 = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 20000, pendingIntent);
////        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
////        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
////        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////        int interval = 1000 * 60 * 20;
////
////        /* Set the alarm to start at 10:30 AM */
////        Calendar calendar = Calendar.getInstance();
////        calendar.setTimeInMillis(System.currentTimeMillis());
////        calendar.set(Calendar.HOUR_OF_DAY, 10);
////        calendar.set(Calendar.MINUTE, 30);
////
////        /* Repeating on every 20 minutes interval */
////        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, System.currentTimeMillis(),
////                6000 , pendingIntent1);
//    }

}
