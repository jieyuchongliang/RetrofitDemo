package com.fujisoft.retrofitdemo.api;

import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.fujisoft.retrofitdemo.MyApplication;
import com.fujisoft.retrofitdemo.utils.used_to_format_log.ParamNames;
import com.fujisoft.retrofitdemo.utils.NetworkUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RetrofitManager {
    private volatile static RetrofitManager sRetrofitManager;
    private Gson gson;
    private RetrofitManager() {

    }

    /**
     * 单例模式构建RetrofitManager
     * @return
     */
    public static RetrofitManager getInstance() {
        if (sRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (sRetrofitManager == null) {
                    sRetrofitManager = new RetrofitManager();
                }
            }
        }
        return sRetrofitManager;
    }

    /**
     * 获取ApiService的实例
     * @return
     */
    public static ApiService getApiService(){
        return getInstance().getRetrofit().create(ApiService.class);
    }

    /**
     * 拿到Retrofit实例
     * @return
     */
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))//通常情况下我们都是直接用，GsonConverterFactory.create()。这里我们加入了参数getGson();主要用来格式化log的。
                .client(getOkHttpClient())
                .build();
    }

    /**
     * 手动设置一个网络连接超时的OkHttpClient给Retrofit
     * @return
     */
    public OkHttpClient getOkHttpClient(){
        //retrofit底层用的okHttp,所以设置超时还需要okHttp
        //然后设置5秒超时
        //TimeUnit为java.util.concurrent包下的时间单位
        //TimeUnit.SECONDS这里为秒的单位
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())//设置一个插值器，用来打印格式化log的.
                .build();
    }

    /**
     * 将Rxjava的Observable和Subscriber绑定在一起
     * @param observable
     * @param subscriber
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        if (MyApplication.getContext() != null){
            if (!NetworkUtils.isConnected(MyApplication.getContext())){
                Toast.makeText(MyApplication.getContext(), "手机未连接网络，请连接后重试", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 以下三个方法是打印log的。以规范化格式，打印全部服务器返回的json数据
     * 需添加依赖     compile 'com.apkfuns.logutils:library:1.5.1.1'
     * @return
     */
    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            builder.excludeFieldsWithModifiers(Modifier.TRANSIENT);
            gson = builder.create();
        }
        return gson;
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }
    private class LogInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(chain.request());
            try {
                String parmas = "";
                if (request.method().equals("GET")) {
                    parmas = request.url().encodedQuery();
                } else {
                    if (request.body() instanceof FormBody) {
                        FormBody body = (FormBody) request.body();
                        for (int i = 0; i < body.size(); i++) {
                            parmas += (body.encodedName(i) + " = " + body.encodedValue(i));
                            if (i + 1 < body.size()) {
                                parmas += " & ";
                            }
                        }
                    }
                }
                LogUtils.v("request:" + request.toString() + "\n参数 = " + parmas);
                long t1 = System.nanoTime();

                long t2 = System.nanoTime();
                LogUtils.v(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                okhttp3.MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                LogUtils.json(content);
                return response.newBuilder()
                        .body(okhttp3.ResponseBody.create(mediaType, content))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.newBuilder()
                    .build();
        }
    }
}
