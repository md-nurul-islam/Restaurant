package com.muhib.restaurant.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.muhib.restaurant.R;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.MySheardPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.Products;
import model.ShippingAddressaModel;
import okhttp3.Headers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {
    private Products products;

    View myView;
    LayoutInflater layoutInflater;
    LinearLayout layout;
    private LinearLayout itemNameLayout;
    TextView totalPay, shippingUserName;
    TextView addressOne, addressOneText, addressTwo, addressTwoText;


    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    String id = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemNameLayout = (LinearLayout) view.findViewById(R.id.itemNameLayout);
        totalPay = (TextView)view.findViewById(R.id.totalPay);

        addressOne = (TextView)view.findViewById(R.id.addressOne);
        addressOneText = (TextView)view.findViewById(R.id.addressOneText);
        addressTwo = (TextView)view.findViewById(R.id.addressTwo);
        addressTwoText = (TextView)view.findViewById(R.id.addressTwoText);

        shippingUserName = (TextView)view.findViewById(R.id.shippingUserName);
        String FullName  = " ";

//        if(products.getShippingTo().get(0).get("first_name")!= null)
//            addressSt = addressSt + products.getShippingTo().get(0).get("first_name");
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            id = bundle.getString("id");
//        }


        if (getArguments().containsKey("products")) {
            String str = getArguments().getString("products");
            if (str != null)
                products = parseNewsList(str);
            //callNewsApiFirst(id);
        }

        JsonElement jsonElement = products.getShippingTo();
        if(products.getShippingTo()!=null){
            Gson gson = new GsonBuilder().create();
            ShippingAddressaModel shippingAddressaModel = gson.fromJson(products.getShippingTo(), ShippingAddressaModel.class);
            FullName = shippingAddressaModel.getFirstName() + " " + shippingAddressaModel.getLastName();

            if(!shippingAddressaModel.getAddressOne().isEmpty())
            {
                addressOne.setVisibility(View.VISIBLE);
                addressOneText.setVisibility(View.VISIBLE);
                addressOne.setText(shippingAddressaModel.getAddressOne());
            }
            else {
                addressOne.setVisibility(View.GONE);
                addressOneText.setVisibility(View.GONE);
            }
            if(!shippingAddressaModel.getAddressTwo().isEmpty())
            {
                addressTwo.setVisibility(View.VISIBLE);
                addressTwoText.setVisibility(View.VISIBLE);
                addressTwo.setText(shippingAddressaModel.getAddressTwo());
            }
            else {
                addressTwo.setVisibility(View.GONE);
                addressTwoText.setVisibility(View.GONE);
            }
        }
        shippingUserName.setText(FullName);
        if(!products.getTotal().isEmpty()&& products.getTotal()!=null)
            totalPay.setText(products.getTotal());



        itemNameLayout.removeAllViews();
        int total = products.getItemList().size();
        for (int i = 0; i < total; i++) {
            myView = layoutInflater.inflate(R.layout.item_row_view_details, itemNameLayout, false);
            LinearLayout ll = (LinearLayout) myView.findViewById(R.id.ll);
            TextView name = (TextView) myView.findViewById(R.id.itemName);
            if (!products.getItemList().get(i).getName().isEmpty())
                name.setText(products.getItemList().get(i).getName());

            TextView itemNo = (TextView) myView.findViewById(R.id.itemNo);
            if (products.getItemList().get(i).getQuantity() > 0)
                itemNo.setText("" + products.getItemList().get(i).getQuantity());

            TextView priceText = (TextView) myView.findViewById(R.id.price);
            if (!products.getItemList().get(i).getPrice().isEmpty())
                priceText.setText(products.getItemList().get(i).getPrice());
            itemNameLayout.addView(ll);

        }

    }

    private Products parseNewsList(String object) {

        Products products = new Products();
        Type listType = new TypeToken<Products>() {
        }.getType();
        products = new Gson().fromJson(object, listType);
        return products;
    }


    public void callNewsApiFirst(String id) {

        showProgress();

        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonElement value) {
                        hideProgress();
                        Gson gson = new GsonBuilder().create();
                        Products r = gson.fromJson(value, Products.class);
                        String st = r.getId();
//                        if (value.code() == 200) {
//
//                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //showErrorView(e);
                        //adapter.showRetry(true, fetchErrorMessage(e));
                        hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showProgress() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait.");
        dialog.show();
    }

    public void hideProgress() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private ProgressDialog dialog;
}

////    Gson gson = new GsonBuilder().create();
////    Products r = gson.fromJson(value, Products.class);
//showProgress();
//        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<JsonElement>() {
//@Override
//public void onSubscribe(Disposable d) {
//
//        }
//
//@Override
//public void onNext(JsonElement value) {
//        hideProgress();
//        Gson gson = new GsonBuilder().create();
//        Products r = gson.fromJson(value, Products.class);
//        String st = r.getId();
////                        if (value.code() == 200) {
////
////                        }
//
//        }
//
//@Override
//public void onError(Throwable e) {
//        //showErrorView(e);
//        //adapter.showRetry(true, fetchErrorMessage(e));
//        hideProgress();
//        }
//
//@Override
//public void onComplete() {
//
//        }
//        });