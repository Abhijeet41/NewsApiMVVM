package com.abhi41.newsapi.retrofit;

import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.response.NewsInfo;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {



    @GET("top-headlines")
    Single<String> news_list(@Query("country")String country, @Query("category")String category);

}
