
package com.abhi41.newsapi.response;


import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
@Entity(tableName = "Article")
public class Article implements Serializable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    @ColumnInfo(name = "assetName")
    @Expose
    private String author;

    @ColumnInfo(name = "description")
    @Expose
    private String description;

    @ColumnInfo(name = "publishedAt")
    @Expose
    private String publishedAt;

    @ColumnInfo(name = "title")
    @Expose
    private String title;

    @ColumnInfo(name = "isLike")
    private boolean isLike = false;

    @ColumnInfo(name = "isDislike")
    private boolean isDislike = false;


    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    @Expose
    private String urlToImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }


    @BindingAdapter("android:loadImage")
    public static void loadImage(ImageView imageView,String url){
        Glide.with(imageView)
                .load(url)
                .into(imageView);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return getId() == article.getId() && isLike() == article.isLike() && isDislike() == article.isDislike() && Objects.equals(getAuthor(), article.getAuthor()) && Objects.equals(getDescription(), article.getDescription()) && Objects.equals(getPublishedAt(), article.getPublishedAt()) && Objects.equals(getTitle(), article.getTitle()) && Objects.equals(getUrlToImage(), article.getUrlToImage());
    }



    public static DiffUtil.ItemCallback<Article> itemCallback = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return String.valueOf(oldItem.getId()).equals(String.valueOf(newItem.getId()));
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.equals(newItem);
        }
    };

}
