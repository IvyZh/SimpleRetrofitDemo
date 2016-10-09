package com.ivy.simpleretrofitdemo.net;

import com.ivy.simpleretrofitdemo.domain.MoviesBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public class NetUtils extends Retrofit2Utils {

    private static final MyService mService = getRetrofit().create(MyService.class);

    public static Call<MoviesBean> getTop250Movies(int start, int count) {
        return mService.getTop250Movies(start, count);
    }

    private interface MyService {
        @GET("/v2/movie/top250")
        Call<MoviesBean> getTop250Movies(@Query("start") int start, @Query("count") int count);

        @GET("/v2/movie/in_theaters")
        Call<MoviesBean> getTheatersMovies(@Query("city") String city);

        @GET("/v2/movie/search")
        Call<MoviesBean> getSearchMovies(@QueryMap Map<String, String> params);

    }

}
