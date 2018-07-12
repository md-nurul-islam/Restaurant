package com.muhib.restaurant;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    public static void sendMyNotification(String id, String title, String body) {

        if(am !=null)
        {
            cancelAlarm();
            Log.v("alarm", "alarm cancelled");
        }

        Intent intent1 = new Intent(mInstance, AlarmReceiver.class);
        intent1.putExtra("order_id", id);
        intent1.putExtra("title", title);
        intent1.putExtra("body", body);
        int num = (int) System.currentTimeMillis();
        intent1.putExtra("num", num);
        pendingIntent = PendingIntent.getBroadcast(mInstance, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        am = (AlarmManager) mInstance.getSystemService(mInstance.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent);

        Log.v("alarm", "alarm set");
    }

    public static void cancelAlarm() {
        if (am!= null) {
            am.cancel(pendingIntent);
        }
    }
}
