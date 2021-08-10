package com.abhi41.newsapi.room_db.dao.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.room_db.dao.NewsDao;

//@Database(entities = Article.class, version = 1, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {

    private static ArticleDatabase db_Asset;

    public static synchronized ArticleDatabase getDb_Asset(Context context) {

        if (db_Asset == null) {
            db_Asset = Room.databaseBuilder(context, ArticleDatabase.class, "article_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return db_Asset;

    }
    public abstract NewsDao assetDao();

}
