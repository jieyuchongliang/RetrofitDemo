package com.fujisoft.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fujisoft.retrofitdemo.api.RetrofitManager;
import com.fujisoft.retrofitdemo.bean.HomeFragmentBean;
import com.fujisoft.retrofitdemo.mvp.controller.HomeFragmentContraller;
import com.fujisoft.retrofitdemo.mvp.presenter.HomeFragmentPresenter;
import com.fujisoft.retrofitdemo.test_mvp.HomeFragment;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements HomeFragmentContraller.View {
    private HomeFragmentContraller.Presenter homeFragmentPresenter;
    private HomeFragmentBean homeFragmentBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTarget();
        initLoadData();

        initFragment();
    }

    /**
     * 添加homefragment到主页
     */
    private void initFragment() {
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,homeFragment)
                .commit();
    }

    /**
     * 触发加载数据
     */
    private void initLoadData() {
        RetrofitManager.toSubscribe(RetrofitManager.getApiService().getHomeInstitutionList(), new Subscriber<HomeFragmentBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HomeFragmentBean homeFragmentBean) {

            }
        });
    }

    /**
     * 初始化所需对象（包括UI，控制器等）
     */
    private void initTarget() {
        homeFragmentPresenter = new HomeFragmentPresenter(this);
    }

    /**
     * 数据与视图绑定
     * @param bean
     */
    @Override
    public void bindData(HomeFragmentBean bean) {
        homeFragmentBean = bean;
    }
}
