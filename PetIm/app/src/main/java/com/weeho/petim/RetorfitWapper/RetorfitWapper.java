package com.weeho.petim.RetorfitWapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangkui on 2017/5/16.
 */

public class RetorfitWapper {
    static RetorfitWapper mRetorfitWapper;
    private final Retrofit mRetorfit;

    public RetorfitWapper(String url){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = builder.addInterceptor(logging).connectTimeout(10,TimeUnit.SECONDS)
                .build();
        mRetorfit = new Retrofit.Builder().baseUrl(url).
                 addConverterFactory(GsonConverterFactory.create(gson)).
                 addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                 client(client).
                 build();
    }
    //获取Retorfit对象
    public static RetorfitWapper getInstance(String url){
        if(null == mRetorfitWapper){
            synchronized (RetorfitWapper.class){
                if(mRetorfitWapper == null) {
                    mRetorfitWapper = new RetorfitWapper(url);
                }
            }
        }
        return mRetorfitWapper;
    }
    public <T>T create(Class<T> service){
        return  mRetorfit.create(service);
    }
}
