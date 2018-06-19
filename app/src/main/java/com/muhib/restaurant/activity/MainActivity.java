package com.muhib.restaurant.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.muhib.restaurant.R;
import com.muhib.restaurant.fragment.HomeFragment;
import com.muhib.restaurant.fragment.OrderDetailsFragment;
import com.muhib.restaurant.fragment.SearchResultFragment;
import com.muhib.restaurant.utils.AppConstant;
import com.muhib.restaurant.utils.MySheardPreference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    // https://github.com/rameshvoltella/WoocommerceAndroidOAuth1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MySheardPreference.setUsingFirstTime(false);
        Bundle extras = getIntent().getExtras();

        String orderId = "";
        if (extras != null) {
            orderId = extras.getString("order_id");
        }
        if (!orderId.isEmpty()) {
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
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
            OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag("orderDetailsFragment");
            if (homeFragment != null && homeFragment.isVisible()) {
                getSupportFragmentManager().popBackStack();
                goesToHomeFragment();
            }
            else if (orderDetailsFragment != null && orderDetailsFragment.isVisible()) {
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
                    OrderDetailsFragment orderDetailsFragment = (OrderDetailsFragment) getSupportFragmentManager().findFragmentByTag("orderDetailsFragment");
                    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
//                    if (homeFragment.isVisible()) {
//                        getSupportFragmentManager().popBackStack();
//                        goesToHomeFragment();
//                    }
                    if (orderDetailsFragment != null && orderDetailsFragment.isVisible()) {
                        getSupportFragmentManager().popBackStack();
                        goesToHomeFragment();
                    }
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
//        final  MenuItem miSearch = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
//
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(Color.BLACK);
//        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                SearchResultFragment fragment = (SearchResultFragment)getSupportFragmentManager().findFragmentByTag("searchResultFragment");
//                if(fragment!=null && fragment.isVisible())
//                    getSupportFragmentManager().popBackStack();
//                miSearch.collapseActionView();
//                Bundle bundle = new Bundle();
//                //Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
//                bundle.putString(AppConstant.SEARCH_TEXT, query);
//                gotoSearchResultFragment(bundle);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        //end..............................


        return true;
    }

    SearchView searchView;

    private void gotoSearchResultFragment(Bundle bundle) {
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        searchResultFragment.setArguments(bundle);
        transaction.add(R.id.container, searchResultFragment, "searchResultFragment").addToBackStack(null);
        transaction.commit();
    }
}
