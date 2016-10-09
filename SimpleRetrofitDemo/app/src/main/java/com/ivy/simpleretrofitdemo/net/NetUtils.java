package com.ivy.simpleretrofitdemo.net;

import com.ivy.simpleretrofitdemo.interfaces.MyService;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public class NetUtils extends Retrofit2Utils {

    private static final MyService mService = getRetrofit().create(MyService.class);

    public static MyService getService() {
        return mService;
    }


}
