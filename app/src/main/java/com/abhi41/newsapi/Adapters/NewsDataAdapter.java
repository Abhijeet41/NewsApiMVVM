package com.abhi41.newsapi.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.newsapi.MainActivity;
import com.abhi41.newsapi.R;
import com.abhi41.newsapi.databinding.SingleNewsLayoutBinding;
import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.room_db.dao.database.ArticleDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class NewsDataAdapter extends ListAdapter<Article,NewsDataAdapter.ViewHolder> {

 //   private List<Article> articleArrayList;
    private LayoutInflater inflater;
    private Context context;
    ArticleDatabase database;
    private static final String TAG = "NewsDataAdapter";


    public NewsDataAdapter(MainActivity mainActivity) {
        super(Article.itemCallback);
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

        final Article article = getItem(position);
        holder.layoutBinding.setArticle(article);
        holder.layoutBinding.executePendingBindings();

        try {
            clickEvent(article,position,holder);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(article.getPublishedAt());
            holder.layoutBinding.txtDate.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private SingleNewsLayoutBinding layoutBinding;


        public ViewHolder(@NonNull @NotNull SingleNewsLayoutBinding itemView) {
            super(itemView.getRoot());
            this.layoutBinding = itemView;

        }

    }
    private void clickEvent(Article article, int position, @NotNull ViewHolder holder) {
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

        if (getCurrentList().get(position).isLike()) {
            holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like));
        } else {
            holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like_outlined));
        }

        if (getCurrentList().get(position).isDislike()) {
            holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike));
        } else {
            holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike_outline));
        }


        holder.layoutBinding.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentList().get(position).isLike()) {
                    getCurrentList().get(position).setLike(false);
                    holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like));
                    insertLikeInDb(article);
                } else {
                    getCurrentList().get(position).setLike(true);
                    insertLikeInDb(article);
                    holder.layoutBinding.imgLike.setBackground(context.getDrawable(R.drawable.ic_like_outlined));
                }
            }
        });

        holder.layoutBinding.imageDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentList().get(position).isDislike()) {
                    getCurrentList().get(position).setDislike(false);
                    insertLikeInDb(article);
                    holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike));
                } else {
                    getCurrentList().get(position).setDislike(true);
                    insertLikeInDb(article);
                    holder.layoutBinding.imageDislike.setBackground(context.getDrawable(R.drawable.ic_dislike_outline));
                }
            }
        });

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
