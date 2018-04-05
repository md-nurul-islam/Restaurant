package com.muhib.restaurant.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muhib.restaurant.R;
import com.muhib.restaurant.fragment.HomeFragment;
import com.muhib.restaurant.utils.MySheardPreference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySheardPreference.setUsingFirstTime(false);
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
}
