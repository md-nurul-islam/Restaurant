package com.muhib.restaurant.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.muhib.restaurant.MyApplication;
import com.muhib.restaurant.NetUtils;
import com.muhib.restaurant.R;
import com.muhib.restaurant.fcm.AlarmReceiver;
import com.muhib.restaurant.fragment.HomeFragment;
import com.muhib.restaurant.fragment.OrderDetailsFragment;
import com.muhib.restaurant.fragment.SearchResultFragment;
import com.muhib.restaurant.utils.AppConstant;
import com.muhib.restaurant.utils.MySheardPreference;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    // https://github.com/rameshvoltella/WoocommerceAndroidOAuth1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MySheardPreference.setUsingFirstTime(false);
        //setAlarm();
        Bundle extras = getIntent().getExtras();

        String orderId = "";
        if (extras != null && !extras.isEmpty()) {
            orderId = extras.getString("order_id");
            MyApplication.cancelAlarm();
//        }
//        if (!orderId.isEmpty() ) {
            MyApplication.cancelAlarm();
            goesToOrderDetails(orderId);
        } else
            goesToHomeFragment();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
    }

    private void goesToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        paginationSingleFragment.setArguments(bundle);
        transaction.replace(R.id.container, homeFragment, "homeFragment");
        transaction.commit();
    }
    private void goesToOrderDetails(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("order_id", id);
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        orderDetailsFragment.setArguments(bundle);
        transaction.replace(R.id.container, orderDetailsFragment, "orderDetailsFragment").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            SearchResultFragment searchResultFragment = (SearchResultFragment)getSupportFragmentManager().findFragmentByTag("searchResultFragment");
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
            OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag("orderDetailsFragment");
            if (homeFragment != null && homeFragment.isVisible()) {
                getSupportFragmentManager().popBackStack();
                goesToHomeFragment();
            }
            if ((orderDetailsFragment != null && orderDetailsFragment.isVisible()) || (searchResultFragment != null && searchResultFragment.isVisible())) {
                getSupportFragmentManager().popBackStack();
                goesToHomeFragment();
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else
            super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                    getSupportFragmentManager().popBackStack();
                break;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //getSupportFragmentManager().popBackStack();
                    SearchResultFragment searchResultFragment = (SearchResultFragment)getSupportFragmentManager().findFragmentByTag("searchResultFragment");

                    OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag("orderDetailsFragment");
                    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
//                    if (homeFragment.isVisible()) {
//                        getSupportFragmentManager().popBackStack();
//                        goesToHomeFragment();
//                    }
                    if ((orderDetailsFragment != null && orderDetailsFragment.isVisible()) || (searchResultFragment != null && searchResultFragment.isVisible())) {
                        getSupportFragmentManager().popBackStack();
                        goesToHomeFragment();
                    }
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                return true;
            case R.id.logout:
                //Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                MySheardPreference.setUsingFirstTime(true);
                MySheardPreference.clearSp();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void clearSaveData() {
//        MySheardPreference.setUserIdAndPassword("","");
//        MySheardPreference.setUserSiteUrl("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // Associate searchable configuration with the SearchView
//        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.colorAccent));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

//        this.menu = menu;  // this will copy menu values to upper defined menu so that we can change icon later akash
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //start.......................................
        final  MenuItem miSearch = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchResultFragment fragment = (SearchResultFragment)getSupportFragmentManager().findFragmentByTag("searchResultFragment");
                if(fragment!=null && fragment.isVisible())
                    getSupportFragmentManager().popBackStack();
                miSearch.collapseActionView();
                Bundle bundle = new Bundle();
                //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                bundle.putString(AppConstant.SEARCH_TEXT, query);
                gotoSearchResultFragment(bundle);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //end..............................


        return true;
    }

    SearchView searchView;

    private void gotoSearchResultFragment(Bundle bundle) {

        if(!NetUtils.isNetworkAvailable(this)) {
//            NetUtils.noInternetWarning(rv, getActivity());
            Toast.makeText(this, " No connectivity", Toast.LENGTH_SHORT).show();

            return;
        }
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        searchResultFragment.setArguments(bundle);
        transaction.add(R.id.container, searchResultFragment, "searchResultFragment").addToBackStack(null);
        transaction.commit();
    }

    private void setAlarm(){


//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 13);
//        calendar.set(Calendar.MINUTE, 38);
//        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 20000, pendingIntent);





////        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//        //PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        int interval = 1000 * 60 * 20;
//
//        Notification.Builder builder;
//        AlarmManager mgrAlarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//        builder = new Notification.Builder(this);
//        builder.setContentTitle("sdaas");
//        builder.setContentText("sss");
//        builder.setSmallIcon(R.drawable.logo);
//        builder.setAutoCancel(true);
//        builder.setLargeIcon(((BitmapDrawable) this.getResources().getDrawable(R.drawable.logo)).getBitmap());
//        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
////            long futureInMillis = OSharedPreference.getPeriodNotificationDateTime();
//        long futureInMillis = System.currentTimeMillis() ;
//        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//        alarmIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
//        alarmIntent.putExtra(AlarmReceiver.NOTIFICATION, builder.build());
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 110, alarmIntent, 0);
//
//        /* Set the alarm to start at 10:30 AM */
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
////        calendar.set(Calendar.HOUR_OF_DAY, 10);
////        calendar.set(Calendar.MINUTE, 30);
////        Calendar calNow = Calendar.getInstance();
////        long current_time = calNow.getTimeInMillis();
//
//        /* Repeating on every 20 minutes interval */
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                futureInMillis ,60000, pendingIntent1);
    }
}
