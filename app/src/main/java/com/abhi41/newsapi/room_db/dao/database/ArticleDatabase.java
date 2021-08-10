package com.abhi41.newsapi.room_db.dao.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.room_db.dao.NewsDao;

@Database(entities = Article.class, version = 1, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {

    private static ArticleDatabase db_article;

    public static synchronized ArticleDatabase getDb_article(Context context) {

        if (db_article == null) {
            db_article = Room.databaseBuilder(context, ArticleDatabase.class, "article_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return db_article;

    }
    public abstract NewsDao articleDao();

}
