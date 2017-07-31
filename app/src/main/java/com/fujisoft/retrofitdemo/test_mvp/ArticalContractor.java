package com.fujisoft.retrofitdemo.test_mvp;

/**
 * Created by 860617010 on 2017/7/28.
 */

public interface ArticalContractor {
    interface View{
        void bindData(ArticalBean bean);
    }
    interface Presenter{
        void loadArticalBeanData();
    }
}
