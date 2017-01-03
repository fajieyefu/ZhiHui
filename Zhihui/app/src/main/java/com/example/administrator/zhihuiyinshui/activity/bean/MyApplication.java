package com.example.administrator.zhihuiyinshui.activity.bean;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Fajieyefu on 2016/8/31.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
    /**
     * 获取全局上下文
     */
    public static Context getContext(){
        return context;
    }
}
