package com.bbk.open;

import android.app.Application;

/**
 * Created by Administrator on 2016/7/22.
 */
public class MyApplication extends Application {
    String APPID = "578890d3";
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.initial(this);
    }
}
