package com.james.coolweather.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by James on 2017/3/6.
 */

public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
//        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
