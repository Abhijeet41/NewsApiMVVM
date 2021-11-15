package com.abhi41.newsapi.di;

import android.app.Activity;
import android.content.Context;

import com.abhi41.newsapi.Common.ArcProgressIndicator;
import com.abhi41.newsapi.util.Myapplication;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/*@Module
public class ProgressModule {


    private Context context;

    public ProgressModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public ArcProgressIndicator getProgress(Context context) {
        return new ArcProgressIndicator(context);
    }



}*/
