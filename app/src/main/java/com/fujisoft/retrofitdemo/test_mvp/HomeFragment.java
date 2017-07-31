package com.fujisoft.retrofitdemo.test_mvp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fujisoft.retrofitdemo.R;
import com.fujisoft.retrofitdemo.databinding.FragmentHomeBinding;

/**
 * Created by 860617010 on 2017/7/28.
 */

public class HomeFragment extends Fragment implements ArticalContractor.View {

    private ArticalBean articalBean;
    private ArticalPresenter articalPresenter;
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articalPresenter = new ArticalPresenter(getContext(),this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initLoadData();
        return binding.getRoot();
    }

    /**
     * 加载数据
     */
    private void initLoadData() {
        articalPresenter.loadArticalBeanData();
    }

    /**
     * 这里是网络加载后获取的数据bean
     * @param bean
     */
    @Override
    public void bindData(ArticalBean bean) {
        articalBean= bean;
        binding.setBean(articalBean);//数据数据和视图绑定
    }
}
