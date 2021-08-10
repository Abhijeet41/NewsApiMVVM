package com.abhi41.newsapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.abhi41.newsapi.Adapters.NewsDataAdapter;
import com.abhi41.newsapi.databinding.ActivityMainBinding;
import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.viewmodel.NetworkInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NetworkInfoViewModel viewModel;
    private List<Article> articleArrayList = new ArrayList<>();
    private NewsDataAdapter newsDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = new NetworkInfoViewModel(getApplication());
        setObserver();

        doIntialization();

    }

    private void doIntialization() {
        viewModel = new ViewModelProvider(this).get(NetworkInfoViewModel.class);

        newsDataAdapter = new NewsDataAdapter(articleArrayList,this);
        binding.rvNewsList.setAdapter(newsDataAdapter);
        binding.rvNewsList.setHasFixedSize(true);

    }

    private void setObserver() {

        viewModel.getNewsData().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                articleArrayList.clear();
                articleArrayList.addAll(articles);
                newsDataAdapter.notifyDataSetChanged();
            }
        });

    }
}