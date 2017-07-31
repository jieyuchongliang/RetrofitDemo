package com.fujisoft.retrofitdemo.api;

/**
 * Created by 860617010 on 2017/6/8.
 */

public interface OnDataCallBack<T> {

    void onError(Throwable e);

    void onSuccess(T data);
}
