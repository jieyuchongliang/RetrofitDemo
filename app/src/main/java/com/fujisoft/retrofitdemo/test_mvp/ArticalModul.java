package com.fujisoft.retrofitdemo.test_mvp;

import android.app.ProgressDialog;
import android.content.Context;

import com.fujisoft.retrofitdemo.api.OnDataCallBack;
import com.fujisoft.retrofitdemo.api.RetrofitManager;

import rx.Subscriber;

/**
 * Created by 860617010 on 2017/7/28.
 */

public class ArticalModul {

    private ProgressDialog progressDialog;

    public ArticalModul(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public void getArticalBeanData(final OnDataCallBack<ArticalBean> callBack) {
        RetrofitManager.toSubscribe(RetrofitManager.getApiService().getArticalBean(), new Subscriber<ArticalBean>() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArticalBean articalBean) {
                callBack.onSuccess(articalBean);
                progressDialog.hide();
            }
        });
    }
}
