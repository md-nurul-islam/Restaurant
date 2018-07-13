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


public class MyFirebaseMessagingServiceOld extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage message) {


        Log.d("TAG", "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + message.getData());



        }
        message.getData().get("order_id");

        sendMyNotification(message.getData().get("order_id"), message.getData().get("title"), message.getData().get("body"));
    }



    private void sendMyNotification(String id, String title, String body) {


        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("order_id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        int num = (int) System.currentTimeMillis();

        PendingIntent pendingIntent=PendingIntent.getActivity(this,num,intent,PendingIntent.FLAG_ONE_SHOT);


        Uri sound = Uri.parse("android.resource://com.muhib.restaurant/" + R.raw.sound);
//        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
        //MyApplication.start();



    }


}
