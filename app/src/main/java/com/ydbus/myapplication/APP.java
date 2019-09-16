package com.ydbus.myapplication;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * 说明：
 * Created by jjs on 2019/9/17.
 */
public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
