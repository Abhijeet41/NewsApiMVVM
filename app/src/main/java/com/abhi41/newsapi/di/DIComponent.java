package com.abhi41.newsapi.di;


import com.abhi41.newsapi.repository.NewsRepository;
import com.abhi41.newsapi.retrofit.RetrofitClient;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitClient.class})
public interface DaggerComponent {

    public void inject(NewsRepository newsRepository);

}
