package com.ivy.simpleretrofitdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ivy.simpleretrofitdemo.R;
import com.ivy.simpleretrofitdemo.adapter.MovieAdapter;
import com.ivy.simpleretrofitdemo.domain.MoviesBean;
import com.ivy.simpleretrofitdemo.net.NetUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView lvMovies;
    private MovieAdapter adapter;
    private List<MoviesBean.SubjectsBean> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMovies = (ListView) findViewById(R.id.lv_movies);
    }


    public void top250(View v) {

        Call<MoviesBean> call = NetUtils.getService().getTop250Movies(0, 20);
        call.enqueue(new Callback<MoviesBean>() {
            @Override
            public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
                if (response.isSuccessful()) {
                    setData(response);
                }

            }

            @Override
            public void onFailure(Call<MoviesBean> call, Throwable t) {

            }
        });
    }

    public void theaters(View v) {
        Call<MoviesBean> call = NetUtils.getService().getTheatersMovies("上海");
        call.enqueue(new Callback<MoviesBean>() {
            @Override
            public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
                if (response.isSuccessful()) {
                    setData(response);
                }

            }

            @Override
            public void onFailure(Call<MoviesBean> call, Throwable t) {

            }
        });
    }

    private void setData(Response<MoviesBean> response) {

//        Log.v("way", response);

        List<MoviesBean.SubjectsBean> subjects = response.body().getSubjects();
        if (adapter == null) {
            mDataList = subjects;
            adapter = new MovieAdapter(MainActivity.this, mDataList);
            lvMovies.setAdapter(adapter);
        } else {
            mDataList.clear();
            mDataList.addAll(subjects);
            adapter.notifyDataSetChanged();
        }

    }

}
