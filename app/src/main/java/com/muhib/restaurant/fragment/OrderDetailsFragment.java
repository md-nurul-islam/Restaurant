package com.muhib.restaurant.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muhib.restaurant.R;
import com.muhib.restaurant.retrofit.RetrofitApiClient;
import com.muhib.restaurant.utils.MySheardPreference;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import model.Products;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {


    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    String id = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        callNewsApiFirst(id);
    }


    public void callNewsApiFirst(String id) {

        showProgress();
        RetrofitApiClient.getLoginApiInterface(MySheardPreference.getUserId(), MySheardPreference.getUserPassword()).getOrderDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> value) {
                        hideProgress();
                        if (value.code() == 200) {

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
                        hideProgress();
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
