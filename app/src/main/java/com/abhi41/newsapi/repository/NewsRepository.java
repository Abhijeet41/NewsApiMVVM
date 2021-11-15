package com.abhi41.newsapi.repository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.newsapi.AppExecutors;
import com.abhi41.newsapi.Common.ArcProgressIndicator;

import com.abhi41.newsapi.di.DIComponent;

import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.retrofit.ApiService;

import com.abhi41.newsapi.room_db.dao.database.ArticleDatabase;
import com.abhi41.newsapi.util.Myapplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private static final String TAG = "NewsRepository";

    @Inject
    ApiService apiService;

    //@Inject
    ArcProgressIndicator progressIndicator;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ArticleDatabase database;
    List<Article> articleList = new ArrayList<>();
    MutableLiveData<List<Article>> data = new MutableLiveData<>();
    boolean cancleRequest;


    public NewsRepository(Application application) {
        //apiService = RetrofitClient.getRetrofitCLient();
        DIComponent diComponents = ((Myapplication) application).getComponents();

        //progressIndicator = diComponents.arcProgressIndicator();
        diComponents.inject(NewsRepository.this);


        database = ArticleDatabase.getDb_article(application);

        cancleRequest = false;
    }


    @SuppressLint("CheckResult")
    public LiveData<List<Article>> apiSendData(Activity context) {

        if (cancleRequest) {
            compositeDisposable.dispose();
        }

        ArcProgressIndicator.show_progressBar(context);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("country", "us");
        hashMap.put("category", "business");


        compositeDisposable.add(apiService.news_list("us", "business")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {

                    @Override
                    public void onSuccess(@NotNull String response) {

                        ArcProgressIndicator.dissmiss_progressbar();
                        deleteAllArticles();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("articles");

                            Gson gson = new Gson();
                            String jsonOutput = String.valueOf(jsonArray);
                            Type listType = new TypeToken<List<Article>>() {
                            }.getType();
                            articleList = gson.fromJson(jsonOutput, listType);
                            data.setValue(articleList);

                            InsertArticleInDB(articleList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            data.setValue(null);
                            ArcProgressIndicator.dissmiss_progressbar();
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        data.setValue(null);
                        ArcProgressIndicator.dissmiss_progressbar();
                        e.printStackTrace();
                    }

                }));


        return data;
    }

    public void cancleRequest() {
        cancleRequest = true;
        compositeDisposable.clear();
        Log.d(TAG, "cancleRequest: ");
    }

    private void InsertArticleInDB(List<Article> articleList) {


        for (Article article : articleList) {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(database.articleDao().insertNote(article)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Log.d(TAG, "doOnError: " + throwable.getMessage());
                    }).subscribe(
                            () -> {
                                Log.d(TAG, "InsertArticleInDB: ");
                                compositeDisposable.dispose();
                            }
                    ));
        }

    }

    private void deleteAllArticles() {

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(database.articleDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "deleteAllArticles: ");
                }));

    }


}
