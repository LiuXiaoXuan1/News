package com.xiaoxuan.news.application;

import android.app.Application;

import cn.smssdk.SMSSDK;

public class MyApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(getApplicationContext(), "d54b977817f3", "8f304c84024d5b73de4bd5366a0ec417");//初始化短信验证
    }
}
