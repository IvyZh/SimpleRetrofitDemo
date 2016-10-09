package com.ivy.simpleretrofitdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ivy.simpleretrofitdemo.R;
import com.ivy.simpleretrofitdemo.adapter.MovieAdapter;
import com.ivy.simpleretrofitdemo.domain.MoviesBean;
import com.ivy.simpleretrofitdemo.net.NetUtils;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovies = (ListView) findViewById(R.id.lv_movies);
        loadData();
    }

    private void loadData() {
        Call<MoviesBean> call = NetUtils.getTop250Movies(0, 20);
//        MoviesBean moviesBean = call.execute().body();//同步请求
        call.enqueue(new Callback<MoviesBean>() {
            @Override
            public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
                if (response.isSuccessful()) {
                    MoviesBean body = response.body();
                    lvMovies.setAdapter(new MovieAdapter(MainActivity.this, body.getSubjects()));
                }

            }

            @Override
            public void onFailure(Call<MoviesBean> call, Throwable t) {

            }
        });
    }

}
