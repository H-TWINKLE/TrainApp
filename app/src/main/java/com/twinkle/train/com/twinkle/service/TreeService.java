package com.twinkle.train.com.twinkle.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.twinkle.train.R;
import com.twinkle.train.com.twinkle.user.JsonTree;

import org.apache.http.Header;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class TreeService extends Service {
    String result;
    final  String TreelookServlet = "http://119.29.98.60:8080/trainAndroid/TreelookServlet";
    List<JsonTree> list;


    public TreeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("开始TreeService");

                get_data();
                set_Alarm();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void get_data() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(10000);
        //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
        RequestHandle post = asyncHttpClient.post(TreelookServlet, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //获取结果
                if (statusCode == 200) {
                    try {
                        result = new String(responseBody, "utf-8").replaceAll("\\s*", "");
                        result = "[" + result + "]";
                        list = JSON.parseArray(result,JsonTree.class);

                        Intent intent = new Intent("com.twinkle.service.TreeReceiver");
                        intent.putExtra("list",(Serializable) list);
                        sendBroadcast(intent);

                    } catch (Exception e) {
                        Toast.makeText(TreeService.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TreeService.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public  void set_Alarm(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000 * Integer.parseInt(preferences.getString("sync_frequency", "10")); // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmTreeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }
}