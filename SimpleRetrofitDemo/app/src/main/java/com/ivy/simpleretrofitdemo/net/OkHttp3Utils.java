package com.ivy.simpleretrofitdemo.net;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description: http://www.open-open.com/lib/view/open1456904039046.
 * <p>
 * http://blog.csdn.net/wlt111111/article/details/51455524
 */

import android.widget.Toast;

import com.ivy.simpleretrofitdemo.UIUtils;
import com.ivy.simpleretrofitdemo.base.MyApplication;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttp3Utils {
    private static OkHttpClient mOkHttpClient = null;
    private static int cacheSize = 10 << 20; // 10 MiB
    private static Cache cache = new Cache(MyApplication.getContext().getCacheDir(), cacheSize);

    private OkHttp3Utils() {

    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClient == null) {
//                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


                    mOkHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new CookiesManager())//设置一个自动管理cookies的管理器
                            .cache(cache)
//                            .addInterceptor(new MyIntercepter())//添加拦截器
                            //添加网络连接器
//                            .addNetworkInterceptor(new CookiesInterceptor(MyApplication.getContext()))//让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数
                            .retryOnConnectionFailure(true)//方法为设置出现错误进行重新连接。
                            .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 拦截器
     */
    private static class MyIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!UIUtils.isNetworkConnected()) {
                Toast.makeText(MyApplication.getContext(), "暂无网络", Toast.LENGTH_SHORT).show();
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                        .build();
            }

            Response response = chain.proceed(request);
            if (UIUtils.isNetworkConnected()) {
                int maxAge = 60 * 60; // 有网络时 设置缓存超时时间1个小时
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }


    /**
     * 自动管理Cookies
     */
    private static class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getContext());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }


        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
