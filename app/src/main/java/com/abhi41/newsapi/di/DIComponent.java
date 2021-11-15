package com.abhi41.newsapi.di;


import android.app.Activity;

import com.abhi41.newsapi.Common.ArcProgressIndicator;
import com.abhi41.newsapi.repository.NewsRepository;
import com.abhi41.newsapi.retrofit.RetrofitClient;
import com.abhi41.newsapi.util.Myapplication;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {RetrofitClient.class/*, ProgressModule.class*/})
public interface DIComponent {

   // ArcProgressIndicator arcProgressIndicator();
    void inject(Myapplication myApplication);
    void inject(NewsRepository newsRepository);



}
