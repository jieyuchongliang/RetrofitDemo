package com.fujisoft.retrofitdemo.test_mvp;

import android.content.Context;

import com.fujisoft.retrofitdemo.api.OnDataCallBack;

/**
 * Created by 860617010 on 2017/7/28.
 */

public class ArticalPresenter implements ArticalContractor.Presenter {
    private HomeFragment homeFragment;
    private ArticalModul articalModul;
    private Context context;
    public ArticalPresenter(Context context,HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
        this.context = context;
    }
    @Override
    public void loadArticalBeanData() {
        articalModul = new ArticalModul(context);
        articalModul.getArticalBeanData(new OnDataCallBack<ArticalBean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(ArticalBean data) {
                homeFragment.bindData(data);
            }
        });
    }
}
