package com.abhi41.newsapi.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.newsapi.MainActivity;
import com.abhi41.newsapi.R;
import com.abhi41.newsapi.databinding.SingleNewsLayoutBinding;
import com.abhi41.newsapi.response.Article;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {

    private List<Article> articleArrayList;
    private LayoutInflater inflater;

    public static final int MAX_LINES = 2;
    public static final String TWO_SPACES = " ";
    private Context context;

    public NewsDataAdapter(List<Article> articleArrayList, MainActivity mainActivity) {
        this.articleArrayList = articleArrayList;
        this.context = mainActivity;
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

            //if (holder.layoutBinding.txtNewsDescription.getLayout().getLineCount() > 2) {

     /*       if (1 > 2) {

                holder.layoutBinding.textReadMore.setVisibility(View.VISIBLE);

            } else {
                holder.layoutBinding.textReadMore.setVisibility(View.GONE);
            }
*/


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
/*
        private TextView txt_news_title,txtDate,txt_news_description;
*/

        public ViewHolder(@NonNull @NotNull SingleNewsLayoutBinding itemView) {
            super(itemView.getRoot());
            this.layoutBinding = itemView;

        }

    }
}
