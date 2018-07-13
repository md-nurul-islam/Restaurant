package com.muhib.restaurant;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.muhib.restaurant.fcm.AlarmReceiver;

/**
 * Created by RR on 05-Apr-18.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static AlarmManager am;
    private static PendingIntent pendingIntent;
    private static Intent intent1;

    private static Handler h = new Handler();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public static void sendMyNotification(final String id, final String title, final String body) {

        if(am !=null)
        {
            cancelAlarm();
            Log.v("alarm", "alarm cancelled");
        }


//        final int delay = 5000; //milliseconds

//        h.postDelayed(new Runnable(){
//            public void run(){
//                //do something
//
//                //Intent alarmIntent = new Intent(mInstance, AlarmReceiver.class);
                intent1 = new Intent(mInstance, AlarmReceiver.class);

//                //sendBroadcast(alarmIntent);
//
//                h.postDelayed(this, delay);
//            }
//        }, delay);

        intent1.putExtra("order_id", id);
        intent1.putExtra("title", title);
        intent1.putExtra("body", body);
        int num = (int) System.currentTimeMillis();
        intent1.putExtra("num", num);
        pendingIntent = PendingIntent.getBroadcast(mInstance, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

        am = (AlarmManager) mInstance.getSystemService(mInstance.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pendingIntent);

        Log.v("alarm", "alarm set");

    }

    public static void cancelAlarm() {
        pendingIntent = PendingIntent.getBroadcast(mInstance, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (am!= null) {
            am.cancel(pendingIntent);
        }
    }
}
