package com.abhi41.newsapi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abhi41.newsapi.Adapters.NewsDataAdapter;
import com.abhi41.newsapi.databinding.ActivityMainBinding;
import com.abhi41.newsapi.di.DIComponent;


import com.abhi41.newsapi.response.Article;
import com.abhi41.newsapi.retrofit.RetrofitClient;
import com.abhi41.newsapi.room_db.dao.database.ArticleDatabase;
import com.abhi41.newsapi.viewmodel.NetworkInfoViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    NetworkInfoViewModel viewModel;
    private List<Article> articleArrayList = new ArrayList<>();
    private NewsDataAdapter newsDataAdapter;
    ArticleDatabase database;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //data binding
        //  binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new NetworkInfoViewModel(getApplication());

        doIntialization();
        setObserver();


    }


    private void doIntialization() {
        viewModel = new ViewModelProvider(this).get(NetworkInfoViewModel.class);

        database = ArticleDatabase.getDb_article(getApplication());

        activityLauncher();


        newsDataAdapter = new NewsDataAdapter(this);
        binding.rvNewsList.setAdapter(newsDataAdapter);
        binding.rvNewsList.setHasFixedSize(true);

        loadAllArticle();

    }

    private void activityLauncher() {
        resultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                Intent intent = result.getData();
                                if (intent != null) {
                                    try {
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap
                                                (getContentResolver(), intent.getData());


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
    }

    private void setObserver() {

        viewModel.getNewsData(this).observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles != null) {
                    articleArrayList.clear();
                    articleArrayList.addAll(articles);
                    newsDataAdapter.submitList(articles);
                }
            }
        });


    }

    private void loadAllArticle() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadAllArticle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())

                .doOnError(throwable -> {
                    Log.d("doOnError", "" + throwable.getMessage());
                }).subscribe(articles -> {
                    articleArrayList.clear();
                    articleArrayList.addAll(articles);

                }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.item_cam:
                getImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }
}