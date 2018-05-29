package com.muhib.restaurant.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.muhib.restaurant.R;
import com.muhib.restaurant.adapter.HomepageAdapter;
import com.muhib.restaurant.myinterface.OrderProcess;
import com.muhib.restaurant.retrofit.OAuthInterceptor;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.MySheardPreference;
import com.muhib.restaurant.utils.PaginationAdapterCallback;
import com.muhib.restaurant.utils.PaginationScrollListener;
import com.woocommerse.OAuth1.OauthConstants.ParameterList;
import com.woocommerse.OAuth1.services.HMACSha1SignatureService;
import com.woocommerse.OAuth1.services.TimestampServiceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.CategoryModel;
import model.MetaDatum;
import model.Products;
import model.UpdateModel;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PaginationAdapterCallback,SwipeRefreshLayout.OnRefreshListener{
    HomepageAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private int SELECTED;
    ArrayList<CategoryModel> results = new ArrayList<>();
    ArrayList<String> strList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    OrderProcess orderProcessCallback;
    private TextView select;
    private LinearLayout selectLay;

    private static final int PAGE_START = 5;
    private static final int PAGE_START_OFFSET = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;

    OAuthInterceptor oAuthInterceptor;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callNewsApiFirst(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);

                // Fetching data from server
                //loadRecyclerViewData();
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        strList = getStrList();


        adapter = new HomepageAdapter(getContext(), this, HomeFragment.this);



        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage = 15;
                currentOffst += 5;

                //loadNextPage();
                callNewsApiNext();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_ITEM;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        rv.setAdapter(adapter);
//        loadFirstPage();
        callNewsApiFirst(false);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFirstPage();
                callNewsApiNext();
            }
        });


        //initView(view);
        return view;
    }

    boolean allowRefresh;
    @Override
    public void onResume() {
        super.onResume();
        if (allowRefresh)
        {
            allowRefresh = false;
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            callNewsApiFirst(true);
        }
    }


    private void callNewsApiNext() {
        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderList(currentPage, currentOffst)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Products>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<Products>> value) {
                        hideProgress();
                        if(value.code()==200){
                            adapter.removeLoadingFooter();
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            List<Products> singleList = value.body();
                            singleList.size();
                            //progressBar.setVisibility(View.GONE);

                            adapter.addAllData(singleList);
                            if ((currentOffst + singleList.size()) < TOTAL_ITEM) adapter.addLoadingFooter();
                            else isLastPage = true;

//                            if (currentOffst < TOTAL_ITEM)
//                                adapter.addLoadingFooter();
//                            else
//                            isLastPage = true;
//                            results.addAll(singleList);
//                            results.add(singleList.get(4));
//                            si++;
                            mSwipeRefreshLayout.setRefreshing(false);

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //showErrorView(e);
                        //adapter.showRetry(true, fetchErrorMessage(e));
                        hideProgress();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private ArrayList<String> getStrList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        return list;
    }

//    private void initView(View view) {
//        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        adapter = new HomepageAdapter(getContext());
//
//        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rv.setLayoutManager(linearLayoutManager);
//        rv.setItemAnimator(new DefaultItemAnimator());
//
//    }

    @Override
    public void onRefresh() {
            callNewsApiFirst(true);

    }

    @Override
    public void retryPageLoad() {
        showProgress();

        UpdateModel updateModel = new UpdateModel();
        updateModel.setStatus("pending");

        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).updateOrder("62", updateModel)
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


    public void callNewsApiFirst(boolean isRefresh) {
//        final String nonce = new TimestampServiceImpl().getNonce();
//        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
//        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
//        String baseString = firstBaseString + secoundBaseString;
//        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, "");
        //hideErrorView();
        if(!isRefresh) {
            showProgress();
        }
        else {
            currentOffst= PAGE_START_OFFSET;
            isLastPage = false;
            isLoading = false;
        }



        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderList(currentPage, currentOffst)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Products>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<Products>> value) {
                        hideProgress();
                        if(value.code()==200){
                            adapter.clear();
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            List<Products> singleList = value.body();
                            singleList.size();
                            //progressBar.setVisibility(View.GONE);

                            adapter.addAllData(singleList);
                            if (currentOffst < TOTAL_ITEM)
                                adapter.addLoadingFooter();
                            else
                                isLastPage = true;

//                            results.addAll(singleList);
//                            results.add(singleList.get(4));
//                            si++;
                            mSwipeRefreshLayout.setRefreshing(false);

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //showErrorView(e);
                        //adapter.showRetry(true, fetchErrorMessage(e));
                        hideProgress();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        RetrofitApiClient.getApiInterface().getTopics()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<List<Products>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<List<Products>> value) {
//                        hideProgress();
//                        if(value.code()==200){
//                            Headers headers = value.headers();
//                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
//                            List<Products> singleList = value.body();
//                            singleList.size();
//                            //progressBar.setVisibility(View.GONE);
//                            adapter.addAllData(singleList);
//
////                            if (currentOffst < TOTAL_ITEM)
////                                adapter.addLoadingFooter();
////                            else
//                                isLastPage = true;
////                            results.addAll(singleList);
////                            results.add(singleList.get(4));
////                            si++;
//
//                        }
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

    }



//    private void callNewsApiNext( ) {
//
//        RetrofitApiClient.getApiInterface().getTopics(currentPage, currentOffst)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<List<CategoryModel>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<List<CategoryModel>> value) {
//                        if (value.code() == 200) {
//                            adapter.removeLoadingFooter();
//                            isLoading = false;
//                            List<CategoryModel> singleList = value.body();
//                            //singleList.size();
//                            adapter.addAllData(singleList);
//
//                            if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
//                            else isLastPage = true;
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //adapter.showRetry(true, fetchErrorMessage(e));
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

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



    String processTime = "";
    public void processOrder(final String id) {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dateDialogView = factory.inflate(R.layout.accept_dialog, null);
        final AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
        selectLay = (LinearLayout) dateDialogView.findViewById(R.id.selectLay);
        select = (TextView) dateDialogView.findViewById(R.id.selectText);
        selectLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), selectLay);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        select.setText(item.getTitle());
                        //Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        processTime = item.getTitle().toString();
                        return true;
                    }
                });

                popup.show();//showing popup menu
                //initiatePopupWindow();
            }
        });
        myDialog.setView(dateDialogView);

        dateDialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = "";

                myDialog.dismiss();
                callUpdateApi(id, processTime);
            }
        });
        dateDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });


        myDialog.show();
    }

    String statusSt = " ";
    public void callUpdateApi(String id, String timeToProcess) {
        List<HashMap> mapList = new ArrayList<>();
        showProgress();
        UpdateModel updateModel = new UpdateModel();
        if (timeToProcess.equals("-1")) {
            updateModel.setStatus("rejected");
            statusSt = "rejected";
        }
        else {
            //updateModel.setStatus("pending");
            updateModel.setStatus("processing");
            statusSt = "accepted";
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("key", "time_to_deliver");
        params.put("value", timeToProcess);

//        MetaDatum metaDatum = new MetaDatum();
//        metaDatum.setKey("time_to_deliver");
//        metaDatum.setValue("120 Min");
//        metaDataList.add(metaDatum);
        mapList.add(params);
        updateModel.setMetaData(mapList);


        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).updateOrder(id, updateModel)
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
                        Toast.makeText(getActivity(), "Order successfully " + statusSt, Toast.LENGTH_SHORT).show();
                        callNewsApiFirst(false);
//                        if (value.code() == 200) {
//
//                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //showErrorView(e);
                        //adapter.showRetry(true, fetchErrorMessage(e));
                        hideProgress();
                        Toast.makeText(getActivity(), "Something wrong, Please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;

    private PopupWindow initiatePopupWindow() {

        try {

            mInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.popup_window_layout, null);

            //If you want to add any listeners to your textviews, these are two //textviews.
            final TextView itema = (TextView) layout.findViewById(R.id.ItemA);


            final TextView itemb = (TextView) layout.findViewById(R.id.ItemB);



            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
            Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(selectLay, 5, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }


//    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
//    private static final String OAUTH_NONCE = "oauth_nonce";
//    private static final String OAUTH_SIGNATURE = "oauth_signature";
//    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
//    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
//    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
//    private static final String OAUTH_VERSION = "oauth_version";
//    private static final String OAUTH_VERSION_VALUE = "1.0";
//
//    private final String consumerKey="";
//    private final String consumerSecret="";
//
//    @Override
//    public okhttp3.Response intercept(Chain chain) throws IOException {
//        Request original = chain.request();
//        HttpUrl originalHttpUrl = original.url();
//
//        Log.d("URL", original.url().toString());
//        Log.d("URL", original.url().scheme());
//        Log.d("encodedpath", original.url().encodedPath());
//        Log.d("query", ""+original.url().query());
//        Log.d("path", ""+original.url().host());
//        Log.d("encodedQuery", ""+original.url().encodedQuery());
//        ;
//        Log.d("method", ""+original.method());
//
//        ////////////////////////////////////////////////////////////
//
//        final String nonce = new TimestampServiceImpl().getNonce();
//        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
//        Log.d("nonce", nonce);
//        Log.d("time", timestamp);
//
//        String dynamicStructureUrl = original.url().scheme() + "://" + original.url().host() + original.url().encodedPath();
//
//        Log.d("ENCODED PATH", ""+dynamicStructureUrl);
//        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
//        Log.d("firstBaseString", firstBaseString);
//        String generatedBaseString = "";
//
//
//        if(original.url().encodedQuery()!=null) {
//            generatedBaseString = original.url().encodedQuery() + "&oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
//        }
//        else
//        {
//            generatedBaseString = "oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + nonce + "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=" + timestamp + "&oauth_version=1.0";
//
//        }
//
//        ParameterList result = new ParameterList();
//        result.addQuerystring(generatedBaseString);
//        generatedBaseString=result.sort().asOauthBaseString();
//        Log.d("Sorted","00--"+result.sort().asOauthBaseString());
//
//        String secoundBaseString = "&" + generatedBaseString;
//
//        if (firstBaseString.contains("%3F")) {
//            Log.d("iff","yess iff");
//            secoundBaseString = "%26" + urlEncoded(generatedBaseString);
//        }
//
//        String baseString = firstBaseString + secoundBaseString;
//
//        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, "");
//        Log.d("Signature", signature);
//
//        HttpUrl url = originalHttpUrl.newBuilder()
//
//                .addQueryParameter(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE)
//                .addQueryParameter(OAUTH_CONSUMER_KEY, consumerKey)
//                .addQueryParameter(OAUTH_VERSION, OAUTH_VERSION_VALUE)
//                .addQueryParameter(OAUTH_TIMESTAMP, timestamp)
//                .addQueryParameter(OAUTH_NONCE, nonce)
//                .addQueryParameter(OAUTH_SIGNATURE, signature)
//
//
//                .build();
//
//        // Request customization: add request headers
//        Request.Builder requestBuilder = original.newBuilder()
//                .url(url);
//
//        Request request = requestBuilder.build();
//        return chain.proceed(request);
//    }
//    public String urlEncoded(String url) {
//        String encodedurl = "";
//        try {
//
//            encodedurl = URLEncoder.encode(url, "UTF-8");
//            Log.d("TEST", encodedurl);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        return encodedurl;
//    }
}
