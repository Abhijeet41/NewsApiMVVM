package com.abhi41.newsapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.abhi41.newsapi.Adapters.NewsDataAdapter;
import com.abhi41.newsapi.databinding.ActivityMainBinding;
import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.room_db.dao.database.ArticleDatabase;
import com.abhi41.newsapi.viewmodel.NetworkInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    NetworkInfoViewModel viewModel;
    private List<Article> articleArrayList = new ArrayList<>();
    private NewsDataAdapter newsDataAdapter;

    ArticleDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new NetworkInfoViewModel(getApplication());
        setObserver();

        doIntialization();

    }

    private void doIntialization() {
        viewModel = new ViewModelProvider(this).get(NetworkInfoViewModel.class);

        database = ArticleDatabase.getDb_article(getApplication());

        newsDataAdapter = new NewsDataAdapter(articleArrayList, this);
        binding.rvNewsList.setAdapter(newsDataAdapter);
        binding.rvNewsList.setHasFixedSize(true);
        loadAllArticle();

    }

    private void setObserver() {

        viewModel.getNewsData().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null)
                {
                    articleArrayList.clear();
                    articleArrayList.addAll(articles);
                }


              //  newsDataAdapter.notifyDataSetChanged();
            }
        });


    }

    private void loadAllArticle() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadAllArticle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())

                .doOnError(throwable -> {
                    Log.d("doOnError", ""+throwable.getMessage());
                }).subscribe(articles -> {
                    articleArrayList.clear();
                    articleArrayList.addAll(articles);
                    newsDataAdapter.notifyDataSetChanged();
                }));
    }


}