package com.twinkle.train.com.twinkle.java;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.config.DbConfigs;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;


/**
 * Created by TWINKLE on 2017/10/27.
 */

public class MyApp extends Application {



    public DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath()+"/How/"))
            .setDbName("train.db")
            //设置数据库路径，默认存储在app的私有目录
            .setDbVersion(1)
            .setAllowTransaction(true)    //设置是否允许事务，默认true
            //设置数据库打开的监听
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            //设置数据库更新的监听
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                }
            })
            //设置表创建的监听
            .setTableCreateListener(new DbManager.TableCreateListener() {
                @Override
                public void onTableCreated(DbManager db, TableEntity<?> table){
                    Log.i("train", "onTableCreated：" + table.getName());
                }
            });




    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能

    }
}
