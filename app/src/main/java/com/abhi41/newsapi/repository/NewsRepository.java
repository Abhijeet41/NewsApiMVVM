package com.abhi41.newsapi.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.retrofit.ApiService;
import com.abhi41.newsapi.retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {

    private static final String TAG = "NewsRepository";

    ApiService apiService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    List<Article> articleList = new ArrayList<>();


    public NewsRepository(Application application) {
        apiService = RetrofitClient.getRetrofitCLient();

    }



    public LiveData<List<Article>> apiSendData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("country", "us");
        hashMap.put("category", "business");
        /*hashMap.put("apiKey", "444ad0895ee94130972d070cead5fcb3");
*/
        MutableLiveData<List<Article>> data = new MutableLiveData<>();

        compositeDisposable.add(apiService.news_list("us","business")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                /*    @Override
                    public void onNext(@NotNull List<Article> articles) {
                        data.setValue(articles);
                    }*/

                    @Override
                    public void onSuccess(@NotNull String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("articles");

                            Gson gson = new Gson();
                            String jsonOutput = String.valueOf(jsonArray);
                            Type listType = new TypeToken<List<Article>>(){}.getType();
                            articleList = gson.fromJson(jsonOutput,listType);
                            data.setValue(articleList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        data.setValue(null);
                        e.printStackTrace();
                    }

                }));

        return data;

    }
}
