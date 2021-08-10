package com.abhi41.newsapi.room_db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.abhi41.newsapi.response.Article;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM Article")
    Flowable<List<Article>> getAllArticleList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Article asset);




}
