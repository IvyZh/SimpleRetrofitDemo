package com.ivy.simpleretrofitdemo.base;

import android.app.Application;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.ivy.simpleretrofitdemo.net.OkHttp3Utils;
import com.orhanobut.logger.Logger;

import okhttp3.OkHttpClient;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public class MyApplication extends Application {
    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("way", "MyApplication onCreate");
        mContext = this;

        Logger.init("way");//初始化Logger
        initOkHttp();//初始化OkHttp
        initGlide();//初始化Glide

    }

    /**
     * 初始化单例Glide对象
     */
    private void initGlide() {

//        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkHttpClientUtils.getOkHttpSingletonInstance(mContext)));
    }

    /**
     * 初始化单例OkHttpClient对象
     */
    private void initOkHttp() {
        OkHttpClient client = OkHttp3Utils.getOkHttpClient();
        Log.v("way", client.toString());
    }

    public static MyApplication getContext() {
        return mContext;
    }
}
