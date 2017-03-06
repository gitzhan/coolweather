package com.james.coolweather.util;

import android.widget.Toast;

/**
 * Created by James on 2017/3/6.
 */

public class ToastUtil {

    public static void toastShort(String toastContent){
        Toast.makeText(MyApplication.getContext(),toastContent,Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String toastContent){
        Toast.makeText(MyApplication.getContext(),toastContent,Toast.LENGTH_LONG).show();
    }
}
