package com.ivy.simpleretrofitdemo.net;

import com.ivy.simpleretrofitdemo.factory.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description: http://blog.csdn.net/wlt111111/article/details/51455524
 */

public abstract class Retrofit2Utils {
    private static Retrofit mRetrofit = null;

    private static String BASE_URL = "https://api.douban.com";

    private static OkHttpClient mOkHttpClient;

    protected static Retrofit getRetrofit() {

        if (mRetrofit == null) {
            synchronized (Retrofit2Utils.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
                }
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(StringConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(mOkHttpClient)
                            .build();
                }
            }
        }

        return mRetrofit;

    }
}
