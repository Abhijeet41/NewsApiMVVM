package com.abhi41.newsapi.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.newsapi.MainActivity;
import com.abhi41.newsapi.R;
import com.abhi41.newsapi.databinding.SingleNewsLayoutBinding;
import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.room_db.dao.database.ArticleDatabase;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {

    private List<Article> articleArrayList;
    private LayoutInflater inflater;
    private Context context;
    private boolean isLiked = false;
    private boolean isDisLiked = false;
    ArticleDatabase database;
    private static final String TAG = "NewsDataAdapter";

    public NewsDataAdapter(List<Article> articleArrayList, MainActivity mainActivity) {
        this.articleArrayList = articleArrayList;
        this.context = mainActivity;
        database = ArticleDatabase.getDb_article(context);
    }

    @NonNull
    @NotNull
    @Override
    public NewsDataAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        SingleNewsLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.single_news_layout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsDataAdapter.ViewHolder holder, int position) {

        final Article article = articleArrayList.get(position);
        holder.layoutBinding.txtNewsTitle.setText(article.getAuthor());
        try {
            holder.layoutBinding.txtNewsDescription.setText(article.getDescription());


            holder.layoutBinding.txtNewsDescription.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("numChars", String.valueOf(holder.layoutBinding.txtNewsDescription.getLineCount()));

                    if (holder.layoutBinding.txtNewsDescription.getLineCount() < 2 &&
                            holder.layoutBinding.txtNewsDescription.getEllipsize() == TextUtils.TruncateAt.END) {
                        holder.layoutBinding.textReadMore.setVisibility(View.GONE);
                    } else {
                        holder.layoutBinding.textReadMore.setVisibility(View.VISIBLE);
                    }
                }
            });

            if (articleArrayList.get(position).isLike()) {
                holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like));

            } else {

                holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like_outlined));
            }

            if (articleArrayList.get(position).isDislike()) {

                holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike));
            } else {

                holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike_outline));
            }


            holder.layoutBinding.imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (articleArrayList.get(position).isLike()) {
                        articleArrayList.get(position).setLike(false);
                        holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like));
                        insertLikeInDb(article);
                    } else {
                        articleArrayList.get(position).setLike(true);
                        insertLikeInDb(article);
                        holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like_outlined));
                    }
                }
            });

            holder.layoutBinding.imageDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (articleArrayList.get(position).isDislike()) {
                        articleArrayList.get(position).setDislike(false);
                        insertLikeInDb(article);
                        holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike));
                    } else {
                        articleArrayList.get(position).setDislike(true);
                        insertLikeInDb(article);
                        holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike_outline));
                    }
                }
            });


            holder.layoutBinding.textReadMore.setVisibility(View.VISIBLE);

            holder.layoutBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.layoutBinding.textReadMore.getText().toString().equals(context.getResources().getString(R.string.read_more))) {
                        holder.layoutBinding.txtNewsDescription.setMaxLines(Integer.MAX_VALUE);
                        holder.layoutBinding.txtNewsDescription.setEllipsize(null);
                        holder.layoutBinding.textReadMore.setText(context.getResources().getString(R.string.read_less));

                    } else {
                        holder.layoutBinding.txtNewsDescription.setMaxLines(2);
                        holder.layoutBinding.txtNewsDescription.setEllipsize(TextUtils.TruncateAt.END);
                        holder.layoutBinding.textReadMore.setText(context.getResources().getString(R.string.read_more));
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(context).load(article.getUrlToImage()).into(holder.layoutBinding.imgNews);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        simpleDateFormat.format(today);
        holder.layoutBinding.txtDate.setText(simpleDateFormat.format(today));
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SingleNewsLayoutBinding layoutBinding;


        public ViewHolder(@NonNull @NotNull SingleNewsLayoutBinding itemView) {
            super(itemView.getRoot());
            this.layoutBinding = itemView;

        }

    }

    private void insertLikeInDb(Article article){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(database.articleDao().insertNote(article)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.d(TAG, "doOnError: "+throwable.getMessage());
                }).subscribe(
                        () -> {
                            Log.d(TAG, "InsertArticleInDB: ");
                            compositeDisposable.dispose();
                        }
                ));
    }
}
