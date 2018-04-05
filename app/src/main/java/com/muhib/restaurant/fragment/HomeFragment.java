package com.muhib.restaurant.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muhib.restaurant.R;
import com.muhib.restaurant.adapter.HomepageAdapter;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.PaginationAdapterCallback;
import com.muhib.restaurant.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.CategoryModel;
import okhttp3.Headers;
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

    private static final int PAGE_START = 15;
    private static final int PAGE_START_OFFSET = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM = 60;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;
    private int SELECTED;
    ArrayList<CategoryModel> results = new ArrayList<>();
    ArrayList<String> strList = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
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


        adapter = new HomepageAdapter(getContext(), this, strList);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage = 15;
                //currentOffst += 15;

                //loadNextPage();
                //callNewsApiNext();
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
       // callNewsApiFirst();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFirstPage();
                //callNewsApiFirst();
            }
        });


        //initView(view);
        return view;
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

    }

    @Override
    public void retryPageLoad() {

    }



    public void callNewsApiFirst( int selected) {
        //hideErrorView();

        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> value) {
                        if(value.code()==200){
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            List<CategoryModel> singleList = value.body();
                            //singleList.size();
                            progressBar.setVisibility(View.GONE);
                            //adapter.addAll(singleList);

                            if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
                            else isLastPage = true;
//                            results.addAll(singleList);
//                            results.add(singleList.get(4));
//                            si++;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //showErrorView(e);
                        //adapter.showRetry(true, fetchErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
