package com.ivy.simpleretrofitdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ivy.simpleretrofitdemo.base.MyApplication;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public class UIUtils {
    public static boolean isNetworkConnected() {
        Context context = MyApplication.getContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
