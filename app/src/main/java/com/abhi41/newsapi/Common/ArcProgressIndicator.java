package com.abhi41.newsapi.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


public class ArcProgressIndicator {


   static int[] colors = {Color.parseColor("#757575"),
            Color.parseColor("#757575")};

    static SimpleArcDialog mDialog;
  /*  private Context context;

    public ArcProgressIndicator(Context context) {
        this.context = context;
    }*/

    public static void show_progressBar(Context context) {
        ArcConfiguration configuration = new ArcConfiguration(context);
        configuration.setColors(colors);
        mDialog = new SimpleArcDialog(context);
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);
        mDialog.show();

    }


    public static void dissmiss_progressbar() {

        mDialog.dismiss();

    }
}


