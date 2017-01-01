package com.iam.artists.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iam.artists.R;

/**
 * Util class for simplify design features
 */

public class Decorator {

    public static int screenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int screenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static void customizeActionBar(AppCompatActivity activity, CharSequence title) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.purple)));
        actionBar.setTitle(title);
    }

    public static int heightByWidthUsesAspectRatio(int width, int aspectRatioX, int aspectRatioY) {
        return width * aspectRatioY / aspectRatioX;
    }

    public static void setHeight(View view, int height) {
        view.getLayoutParams().height = height;
    }

    public static void setStatusBarColor(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.dark_purple));
        }
    }

}
