package com.example.androidcalc;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.WindowManager;


 class DisplayMode {

     static void setFullscreen(Activity activity){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    static void setViewByOrintation(Activity activity, Configuration config, Integer portrait, Integer landscape){
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setContentView(landscape);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setContentView(portrait);
        }
    }
}
