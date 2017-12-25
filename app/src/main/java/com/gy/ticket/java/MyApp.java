package com.gy.ticket.java;

import android.app.Application;

import com.mob.MobSDK;

import org.xutils.x;

/**
 * Created by TWINKLE on 2017/12/19.
 */

public class MyApp  extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
    }
}
