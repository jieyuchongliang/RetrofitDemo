package com.fujisoft.retrofitdemo.mvp.controller;

import com.fujisoft.retrofitdemo.bean.HomeFragmentBean;

/**
 * Created by 860617010 on 2017/7/28.
 */

public interface HomeFragmentContraller {
    interface View{
        void bindData(HomeFragmentBean bean);
    }
    interface Presenter{
        void loadData();
    }
}
