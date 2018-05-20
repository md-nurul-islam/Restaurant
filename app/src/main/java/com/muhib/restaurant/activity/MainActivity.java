package com.muhib.restaurant.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.muhib.restaurant.R;
import com.muhib.restaurant.fragment.HomeFragment;
import com.muhib.restaurant.utils.MySheardPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;

   // https://github.com/rameshvoltella/WoocommerceAndroidOAuth1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

    @Override
    public void onBackPressed() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
                return true;
            case R.id.logout:
                //Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
                MySheardPreference.setUsingFirstTime(true);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
