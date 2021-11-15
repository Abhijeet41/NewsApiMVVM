package com.abhi41.newsapi.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.abhi41.newsapi.di.DIComponent;

import com.abhi41.newsapi.di.DaggerDIComponent;

import com.abhi41.newsapi.retrofit.RetrofitClient;

public class Myapplication extends Application {

    private DIComponent diComponents;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponents = DaggerDIComponent.builder()
                .retrofitClient(new RetrofitClient())
                /*.progressModule(new ProgressModule(getApplicationContext()))*/
                .build();


    }


    public DIComponent getComponents() {
        return diComponents;
    }
}
