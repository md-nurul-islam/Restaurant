package com.muhib.restaurant;

import android.app.Application;

/**
 * Created by RR on 05-Apr-18.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

}
