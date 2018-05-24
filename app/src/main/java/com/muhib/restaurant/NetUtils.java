package com.muhib.restaurant;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;


/**
 * Created by TularJahaj on 12/22/2016.
 */
public class NetUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void noInternetWarning(View view, final Context context) {
        if (!isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar.make(view,"No internet Connection", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor((int) InputDeviceCompat.SOURCE_ANY);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(-1);
            snackbar.setAction("connect", new View.OnClickListener() {
                public void onClick(View v) {
//                    Intent intent = new Intent("android.settings.SETTINGS");
//                    intent.addFlags(268435456);
//                    context.startActivity(intent);
                }
            });
            snackbar.show();
        }
    }
}
