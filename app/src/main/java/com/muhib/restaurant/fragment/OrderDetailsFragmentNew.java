///*
//package com.muhib.restaurant.fragment;
//
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.reflect.TypeToken;
//import com.muhib.restaurant.NetUtils;
//import com.muhib.restaurant.R;
//import com.muhib.restaurant.myinterface.OrderActionListener;
//import com.muhib.restaurant.retrofit.RetrofitApiClient;
//import com.muhib.restaurant.utils.MySheardPreference;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import model.BillingAddressaModel;
//import model.Products;
//import model.ShippingAddressaModel;
//import model.UpdateModel;
//
//*/
///**
// * A simple {@link Fragment} subclass.
// *//*
//
//public class OrderDetailsFragmentNew extends Fragment implements View.OnClickListener {
//    private Products products;
//
//    View myView;
//    LayoutInflater layoutInflater;
//    LinearLayout layout;
//    private LinearLayout itemNameLayout;
//    TextView totalPay, shippingUserName;
//    TextView addressOne, addressOneText, addressTwo, addressTwoText;
//    TextView acceptBtn, rejectBtn;
//
//    private LinearLayout selectLay;
//    private TextView select;
//    private TextView OrderStatus;
//    private TextView addressHead, userPhone;
//
//    String shippingAddressOne = "";
//    String shippingAddressTwo = "";
//    String billingAddressOne = "";
//    String billingAddressTwo = "";
//    String phoneString = "";
//    OrderActionListener orderActionListener;
//
//
//    public OrderDetailsFragmentNew() {
//    }
//
//    String id = "";
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_order_details_new, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        itemNameLayout = (LinearLayout) view.findViewById(R.id.itemNameLayout);
//        totalPay = (TextView) view.findViewById(R.id.totalPay);
//
//        OrderStatus = (TextView) view.findViewById(R.id.status);
//        addressHead = (TextView) view.findViewById(R.id.addressHead);
//        userPhone = (TextView)view.findViewById(R.id.phoneNo);
//
//        addressOne = (TextView) view.findViewById(R.id.addressOne);
//        addressOneText = (TextView) view.findViewById(R.id.addressOneText);
//        addressTwo = (TextView) view.findViewById(R.id.addressTwo);
//        addressTwoText = (TextView) view.findViewById(R.id.addressTwoText);
//
//        acceptBtn = (TextView) view.findViewById(R.id.accept);
//        rejectBtn = (TextView) view.findViewById(R.id.reject);
//        acceptBtn.setOnClickListener(this);
//        rejectBtn.setOnClickListener(this);
//
//        shippingUserName = (TextView) view.findViewById(R.id.shippingUserName);
//        String FullNameShipping = " ";
//        String FullNameBilling = " ";
//
////        if(products.getShippingTo().get(0).get("first_name")!= null)
////            addressSt = addressSt + products.getShippingTo().get(0).get("first_name");
//        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Bundle bundle = this.getArguments();
////        if (bundle != null) {
////            id = bundle.getString("id");
////        }
//
//
//        if (getArguments().containsKey("products")) {
//            String str = getArguments().getString("products");
//            if (str != null)
//                products = parseNewsList(str);
//            //callNewsApiFirst(id);
//        }
//
//        ShippingAddressaModel shippingAddressaModel;
//        JsonElement jsonElement = products.getShippingTo();
//        if (products.getShippingTo() != null) {
//            Gson gson = new GsonBuilder().create();
//            shippingAddressaModel = gson.fromJson(products.getShippingTo(), ShippingAddressaModel.class);
//            FullNameShipping = shippingAddressaModel.getFirstName() + " " + shippingAddressaModel.getLastName();
//
//            if (!shippingAddressaModel.getAddressOne().isEmpty()) {
//                addressOne.setVisibility(View.VISIBLE);
//                addressOneText.setVisibility(View.VISIBLE);
//                shippingAddressOne = shippingAddressaModel.getAddressOne() + "\n"
//                        + shippingAddressaModel.getState()+ "\n" + shippingAddressaModel.getCity() +
//                        " " + shippingAddressaModel.getPostcode() + "\n" + shippingAddressaModel.getCountry();
//                //addressOne.setText(shippingAddressOne);
//            } else {
//                addressOne.setVisibility(View.GONE);
//                addressOneText.setVisibility(View.GONE);
//            }
//            if (!shippingAddressaModel.getAddressTwo().isEmpty()) {
//                addressTwo.setVisibility(View.VISIBLE);
//                addressTwoText.setVisibility(View.VISIBLE);
//                shippingAddressTwo = shippingAddressaModel.getAddressTwo() + "\n"
//                        + shippingAddressaModel.getState()+ "\n" + shippingAddressaModel.getCity() +
//                        " " + shippingAddressaModel.getPostcode() + "\n" + shippingAddressaModel.getCountry();
//                //addressTwo.setText(shippingAddressTwo);
//            } else {
//                addressTwo.setVisibility(View.GONE);
//                addressTwoText.setVisibility(View.GONE);
//            }
//        }
//
//        BillingAddressaModel billingAddressaModel;
//        JsonElement jsonElementBilling = products.getBilling();
//        if (products.getShippingTo() != null) {
//            Gson gson = new GsonBuilder().create();
//            billingAddressaModel= gson.fromJson(products.getBilling(), BillingAddressaModel.class);
//            FullNameBilling = billingAddressaModel.getFirstName() + " " + billingAddressaModel.getLastName();
//
//            phoneString = billingAddressaModel.getPhone();
//            if (!billingAddressaModel.getAddressOne().isEmpty()) {
//                addressOne.setVisibility(View.VISIBLE);
//                addressOneText.setVisibility(View.VISIBLE);
//                billingAddressOne = billingAddressaModel.getAddressOne() + "\n"
//                        + billingAddressaModel.getState()+ "\n" + billingAddressaModel.getCity() +
//                        " " + billingAddressaModel.getPostcode() + "\n" + billingAddressaModel.getCountry();
//                //addressOne.setText(billingAddressOne);
//            } else {
//                addressOne.setVisibility(View.GONE);
//                addressOneText.setVisibility(View.GONE);
//            }
//            if (!billingAddressaModel.getAddressTwo().isEmpty()) {
//                addressTwo.setVisibility(View.VISIBLE);
//                addressTwoText.setVisibility(View.VISIBLE);
//                billingAddressTwo = billingAddressaModel.getAddressOne() + "\n"
//                        + billingAddressaModel.getState()+ "\n" + billingAddressaModel.getCity() +
//                        " " + billingAddressaModel.getPostcode() + "\n" + billingAddressaModel.getCountry();
//                //addressTwo.setText(billingAddressTwo);
//            } else {
//                addressTwo.setVisibility(View.GONE);
//                addressTwoText.setVisibility(View.GONE);
//            }
//        }
//
//        if (products.getStatus().equalsIgnoreCase("pending")) {
//            acceptBtn.setVisibility(View.VISIBLE);
//            rejectBtn.setVisibility(View.VISIBLE);
//
//        } else {
//            acceptBtn.setVisibility(View.GONE);
//            rejectBtn.setVisibility(View.GONE);
//        }
//
//        if(shippingAddressOne.isEmpty() && shippingAddressTwo.isEmpty())
//        {
//            addressOne.setText(billingAddressOne);
//            addressTwo.setText(billingAddressTwo);
//            addressHead.setText("Billing Address");
//            shippingUserName.setText(FullNameBilling);
//            userPhone.setText(phoneString);
//        }
//        else {
//            addressOne.setText(shippingAddressOne);
//            addressTwo.setText(shippingAddressTwo);
//            addressHead.setText("Shipping Address");
//            shippingUserName.setText(FullNameShipping);
//            userPhone.setText(phoneString);
//        }
//
//        //shippingUserName.setText(FullName);
//        if (!products.getTotal().isEmpty() && products.getTotal() != null)
//            totalPay.setText(products.getTotal());
//        OrderStatus.setText(products.getStatus());
//
//
//        itemNameLayout.removeAllViews();
//        int total = products.getItemList().size();
//        for (int i = 0; i < total; i++) {
//            myView = layoutInflater.inflate(R.layout.item_row_view_details, itemNameLayout, false);
//            LinearLayout ll = (LinearLayout) myView.findViewById(R.id.ll);
//            TextView name = (TextView) myView.findViewById(R.id.itemName);
//            if (!products.getItemList().get(i).getName().isEmpty())
//                name.setText(products.getItemList().get(i).getName());
//
//            TextView itemNo = (TextView) myView.findViewById(R.id.itemNo);
//            if (products.getItemList().get(i).getQuantity() > 0)
//                itemNo.setText("" + products.getItemList().get(i).getQuantity());
//
//            TextView priceText = (TextView) myView.findViewById(R.id.price);
//            if (!products.getItemList().get(i).getPrice().isEmpty())
//                priceText.setText(products.getItemList().get(i).getPrice());
//            itemNameLayout.addView(ll);
//
//        }
//
//    }
//
//    private Products parseNewsList(String object) {
//
//        Products products = new Products();
//        Type listType = new TypeToken<Products>() {
//        }.getType();
//        products = new Gson().fromJson(object, listType);
//        return products;
//    }
//
//
//    public void callNewsApiFirst(String id) {
//
//        showProgress();
//        RetrofitApiClient.getApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
////        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<JsonElement>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(JsonElement value) {
//                        hideProgress();
//                        Gson gson = new GsonBuilder().create();
//                        Products r = gson.fromJson(value, Products.class);
//                        String st = r.getId();
////                        if (value.code() == 200) {
////
////                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //showErrorView(e);
//                        //adapter.showRetry(true, fetchErrorMessage(e));
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//
//
//    public void showProgress() {
//        dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("please wait.");
//        dialog.show();
//    }
//
//    public void hideProgress() {
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//
//    private ProgressDialog dialog;
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.accept:
//                ordeProcess(products.getId(), true);
//                break;
//            case R.id.reject:
//                ordeProcess(products.getId(), false);
//                break;
//        }
//    }
//
//    private void ordeProcess(String id, boolean b) {
//        String status = "";
//        if (b)
//            status = "Accepted";
//        else
//            status = "Rejected";
//        //Toast.makeText(getActivity(), "Order " + status , Toast.LENGTH_SHORT).show();
//        if (b)
//            processOrder(id);
//        else
//            callUpdateApi(products.getId(), "");
//
//    }
//
//    String processTime = "";
//
//    public void processOrder(final String id) {
//        LayoutInflater factory = LayoutInflater.from(getActivity());
//        final View dateDialogView = factory.inflate(R.layout.accept_dialog, null);
//        final AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
//        selectLay = (LinearLayout) dateDialogView.findViewById(R.id.selectLay);
//        select = (TextView) dateDialogView.findViewById(R.id.selectText);
//        selectLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Creating the instance of PopupMenu
//                PopupMenu popup = new PopupMenu(getActivity(), selectLay);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        select.setText(item.getTitle());
//                        //Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
//                        processTime = item.getTitle().toString();
//                        return true;
//                    }
//                });
//
//                popup.show();//showing popup menu
//                //initiatePopupWindow();
//            }
//        });
//        myDialog.setView(dateDialogView);
//
//        dateDialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String description = "";
//
//                myDialog.dismiss();
//                callUpdateApi(id, processTime);
//            }
//        });
//        dateDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//
//
//        myDialog.show();
//    }
//
//    String statusSt =" ";
//    private void callUpdateApi(String id, String timeToProcess) {
//        if(!NetUtils.isNetworkAvailable(getActivity())) {
//            //NetUtils.noInternetWarning(OrderStatus, getActivity());
//            Toast.makeText(getActivity(), " No connectivity", Toast.LENGTH_SHORT).show();
//            hideProgress();
//            return;
//        }
//
//        List<HashMap> mapList = new ArrayList<>();
//        showProgress();
//        UpdateModel updateModel = new UpdateModel();
//        if (timeToProcess.isEmpty()) {
//            updateModel.setStatus("cancelled");
//            statusSt = "cancelled";
//        }
//        else {
//            //updateModel.setStatus("pending");
//            updateModel.setStatus("processing");
//            statusSt = "accepted";
//        }
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("key", "time_to_deliver");
//        params.put("value", timeToProcess);
//
////        MetaDatum metaDatum = new MetaDatum();
////        metaDatum.setKey("time_to_deliver");
////        metaDatum.setValue("120 Min");
////        metaDataList.add(metaDatum);
//        mapList.add(params);
//        updateModel.setMetaData(mapList);
//
//        RetrofitApiClient.getApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).updateOrder(id, updateModel)
////        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).updateOrder(id, updateModel)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<JsonElement>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(JsonElement value) {
//                        hideProgress();
//                        Gson gson = new GsonBuilder().create();
//                        Products r = gson.fromJson(value, Products.class);
//                        String st = r.getId();
//                        Toast.makeText(getActivity(), "Order successfully " + statusSt, Toast.LENGTH_SHORT).show();
//                        if(statusSt.equals("accepted"))
//                            OrderStatus.setText("processing");
//                        else
//                            OrderStatus.setText("cancelled");
//                        acceptBtn.setVisibility(View.GONE);
//                        rejectBtn.setVisibility(View.GONE);
//
////                        if (value.code() == 200) {
////
////                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //showErrorView(e);
//                        //adapter.showRetry(true, fetchErrorMessage(e));
//                        hideProgress();
//                        Toast.makeText(getActivity(), "Something wrong, Please try again later", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
//}
//
//////    Gson gson = new GsonBuilder().create();
//////    Products r = gson.fromJson(value, Products.class);
////showProgress();
////        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Observer<JsonElement>() {
////@Override
////public void onSubscribe(Disposable d) {
////
////        }
////
////@Override
////public void onNext(JsonElement value) {
////        hideProgress();
////        Gson gson = new GsonBuilder().create();
////        Products r = gson.fromJson(value, Products.class);
////        String st = r.getId();
//////                        if (value.code() == 200) {
//////
//////                        }
////
////        }
////
////@Override
////public void onError(Throwable e) {
////        //showErrorView(e);
////        //adapter.showRetry(true, fetchErrorMessage(e));
////        hideProgress();
////        }
////
////@Override
////public void onComplete() {
////
////        }
////        });*/
