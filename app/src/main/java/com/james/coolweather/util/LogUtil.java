package com.james.coolweather.util;

import android.util.Log;

/**
 * Created by James on 2017/3/6.
 */

public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 1;
    public static final int INFO = 1;
    public static final int WARN = 1;
    public static final int ERROR = 1;
    public static final int NOTING = 1;

    public static int level = VERBOSE;

    public static void v(String tag, String msg){
        if (level<=VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (level<=DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if (level<=INFO) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (level<=WARN) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (level<=ERROR) {
            Log.v(tag, msg);
        }
    }

}
