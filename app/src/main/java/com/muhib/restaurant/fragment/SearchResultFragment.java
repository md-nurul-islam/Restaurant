package com.muhib.restaurant.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.muhib.restaurant.adapter.SearchAdapterNew;
import com.muhib.restaurant.myinterface.OrderProcess;
import com.muhib.restaurant.retrofit.OAuthInterceptor;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.AppConstant;
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
public class SearchResultFragment extends Fragment implements PaginationAdapterCallback {
    SearchAdapterNew adapter;
    LinearLayoutManager linearLayoutManager;


    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    //    private int SELECTED;
    ArrayList<CategoryModel> results = new ArrayList<>();
    ArrayList<String> strList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    OrderProcess orderProcessCallback;
    private TextView select;
    private LinearLayout selectLay;

    private static final int PAGE_START = 10;
    private static final int PAGE_START_OFFSET = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;

    OAuthInterceptor oAuthInterceptor;
    private String SELECTED;

    public SearchResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_search_result, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments().containsKey(AppConstant.SEARCH_TEXT)) {
            SELECTED = getArguments().getString(AppConstant.SEARCH_TEXT);
        }


        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        strList = getStrList();


        adapter = new SearchAdapterNew(getContext(), this, SearchResultFragment.this);


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage = 15;
                currentOffst += 10;

                //loadNextPage();
                callNewsApiNext(SELECTED);
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
        callNewsApiFirst(SELECTED);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFirstPage();
                callNewsApiNext(SELECTED);
            }
        });


        //initView(view);
        return view;
    }

    private void callNewsApiNext(String selected) {
        RetrofitApiClient.getApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getSearachTopics(selected, currentPage, currentOffst)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Products>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<Products>> value) {
                        //hideProgress();
                        if (value.code() == 200) {
                            adapter.removeLoadingFooter();
                            isLoading = false;
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            List<Products> singleList = value.body();
                            singleList.size();
                            //progressBar.setVisibility(View.GONE);

                            adapter.addAllData(singleList);
                            if ((currentOffst + singleList.size()) <TOTAL_ITEM)
                                adapter.addLoadingFooter();
                            else
                                isLastPage = true;

//                            if (currentOffst < TOTAL_ITEM)
//                                adapter.addLoadingFooter();
//                            else
//                            isLastPage = true;
//                            results.addAll(singleList);
//                            results.add(singleList.get(4));
//                            si++;
                            //mSwipeRefreshLayout.setRefreshing(false);

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

    List<Products> singleList = new ArrayList<>();

    public void callNewsApiFirst(String selected) {
//        final String nonce = new TimestampServiceImpl().getNonce();
//        final String timestamp = new TimestampServiceImpl().getTimestampInSeconds();
//        String firstBaseString = original.method() + "&" + urlEncoded(dynamicStructureUrl);
//        String baseString = firstBaseString + secoundBaseString;
//        String signature = new HMACSha1SignatureService().getSignature(baseString, consumerSecret, "");
        //hideErrorView();

        showProgress();

        RetrofitApiClient.getApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getSearachTopics(selected, currentPage, currentOffst)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Products>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<Products>> value) {
                        hideProgress();
                        if (value.code() == 200) {
                            adapter.clear();
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            singleList.clear();
                            singleList = value.body();
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


                        }
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
        } else {
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
                        Toast.makeText(getActivity(), "Order successfully" + statusSt, Toast.LENGTH_SHORT).show();
                        //callNewsApiFirst(false);
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
                    FrameLayout.LayoutParams.WRAP_CONTENT, true);
            Drawable background = getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
            mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(selectLay, 5, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }

}
