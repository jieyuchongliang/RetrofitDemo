package com.fujisoft.retrofitdemo.mvp.presenter;

import com.fujisoft.retrofitdemo.api.OnDataCallBack;
import com.fujisoft.retrofitdemo.bean.HomeFragmentBean;
import com.fujisoft.retrofitdemo.mvp.controller.HomeFragmentContraller;
import com.fujisoft.retrofitdemo.mvp.module.HomeFragmentModule;

/**
 * Created by 860617010 on 2017/7/28.
 */

public class HomeFragmentPresenter implements HomeFragmentContraller.Presenter {
    private HomeFragmentModule homeFragmentModule;

    public HomeFragmentPresenter(HomeFragmentContraller.View view) {
        this.view = view;
    }

    private HomeFragmentContraller.View view;
    @Override
    public void loadData() {
        homeFragmentModule = new HomeFragmentModule();
        homeFragmentModule.loadData(new OnDataCallBack<HomeFragmentBean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(HomeFragmentBean data) {
                view.bindData(data);
            }
        });
    }
}
