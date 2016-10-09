package com.ivy.simpleretrofitdemo.interfaces;

import com.ivy.simpleretrofitdemo.domain.MoviesBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public interface MyService {
    @GET("/v2/movie/top250")
    Call<MoviesBean> getTop250Movies(@Query("start") int start, @Query("count") int count);

    @GET("/v2/movie/in_theaters")
    Call<MoviesBean> getTheatersMovies(@Query("city") String city);

    @GET("/v2/movie/search")
    Call<MoviesBean> getSearchMovies(@QueryMap Map<String, String> params);

    @GET("/v2/movie/new_movies")
    Call<MoviesBean> getNewMovies();// 需要权限


    //---------------------------------------------------------------------------------------------
    @FormUrlEncoded
    @POST("/valproWebN-4.0/app/login/doLogin")
    Call<String> login(@Field("userName") String userName, @Field("password") String password, @Field("validateCode") String validateCode);

    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviews(@FieldMap Map<String, String> fields);

}