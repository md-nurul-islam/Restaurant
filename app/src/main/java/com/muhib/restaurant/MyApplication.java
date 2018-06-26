package com.muhib.restaurant;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import com.muhib.restaurant.fcm.AlarmReceiver;

/**
 * Created by RR on 05-Apr-18.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static AlarmManager am;
    private static PendingIntent pendingIntent;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public static void sendMyNotification(String id, String title, String body) {

        Intent intent1 = new Intent(mInstance, AlarmReceiver.class);
        intent1.putExtra("order_id", id);
        intent1.putExtra("title", title);
        intent1.putExtra("body", body);
        int num = (int) System.currentTimeMillis();
        intent1.putExtra("num", num);
        pendingIntent = PendingIntent.getBroadcast(mInstance, num, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) mInstance.getSystemService(mInstance.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 20000, pendingIntent);
    }

    public static void cancelAlarm() {
        if (am!= null) {
            am.cancel(pendingIntent);
        }
    }
}
