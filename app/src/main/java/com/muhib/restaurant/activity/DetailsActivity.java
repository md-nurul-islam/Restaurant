package com.muhib.restaurant.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhib.restaurant.R;

import model.Products;

public class DetailsActivity extends AppCompatActivity {
    private Products products;

    View myView;
    LayoutInflater layoutInflater;
    LinearLayout layout;
    private LinearLayout itemNameLayout;
    TextView totalPay, shippingUserName;
    TextView addressOne, addressOneText, addressTwo, addressTwoText;
    TextView acceptBtn, rejectBtn;

    private LinearLayout selectLay;
    private TextView select;
    private TextView OrderStatus;
    private TextView addressHead, userPhone;

    String shippingAddressOne = "";
    String shippingAddressTwo = "";
    String billingAddressOne = "";
    String billingAddressTwo = "";
    String phoneString = "";


    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_details_new);


    }

}
