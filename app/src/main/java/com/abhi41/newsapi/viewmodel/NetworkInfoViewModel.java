package com.abhi41.newsapi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.newsapi.repository.NewsRepository;
import com.abhi41.newsapi.response.Article;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetworkInfoViewModel extends AndroidViewModel {

    public NewsRepository newsRepository;



    public NetworkInfoViewModel(@NonNull @NotNull Application application) {
        super(application);
        newsRepository = new NewsRepository(application);
    }

    public LiveData<List<Article>> getNewsData()
    {
        return newsRepository.apiSendData();
    }
}
