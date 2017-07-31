package com.fujisoft.retrofitdemo.mvp.module;

import com.fujisoft.retrofitdemo.api.OnDataCallBack;
import com.fujisoft.retrofitdemo.api.RetrofitManager;
import com.fujisoft.retrofitdemo.bean.HomeFragmentBean;

import rx.Subscriber;

/**
 * Created by 860617010 on 2017/7/28.
 */

public class HomeFragmentModule {
    public void loadData(final OnDataCallBack<HomeFragmentBean> callBack){
        RetrofitManager.toSubscribe(RetrofitManager.getApiService().getHomeInstitutionList(), new Subscriber<HomeFragmentBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HomeFragmentBean homeFragmentBean) {
                callBack.onSuccess(homeFragmentBean);
            }
        });
    }
}
