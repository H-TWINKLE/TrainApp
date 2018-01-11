package com.twinkle.train.com.twinkle.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twinkle.train.com.twinkle.user.JsonAdmin;
import com.twinkle.train.com.twinkle.java.Util;

import org.apache.http.Consts;
import org.apache.http.Header;


public class InfoService extends Service {

    static String LoginServletAndroid = "http://119.29.98.60:8080/trainAndroid/LoginServletAndroid";
    SharedPreferences preferences;
    String admin, password, result;


    public InfoService() {

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

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        password = preferences.getString("password", "password");

        Log.i("InfoService输出admin", admin);

        if (!"admin".equals(admin)) {

            Log.i("跑InfoService", "yes");
            AttemptToLogin(admin, password);
            if (!"admin".equals(admin)) {
                set_Alarm();
            }

        }


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void get_info(String result) {

        try {

            JSONObject object = JSON.parseObject(result);
            JsonAdmin jsonAdmin = new JsonAdmin();                 //处理信息
            jsonAdmin.setAdmin(object.getString("admin"));
            jsonAdmin.setPassword(object.getString("password"));
            jsonAdmin.setName(object.getString("name"));
            jsonAdmin.setSex(object.getString("sex"));
            jsonAdmin.setAge(object.getString("age"));
            jsonAdmin.setSay(object.getString("say"));
            jsonAdmin.setLocal(object.getString("local"));
            jsonAdmin.setLevel(object.getString("level"));
            jsonAdmin.setStu_id(object.getString("stu_id"));
            jsonAdmin.setEol_pass(object.getString("eol_pass"));
            jsonAdmin.setJwgl_pass(object.getString("jwgl_pass"));

            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("admin", jsonAdmin.getAdmin());
            editor.putString("password", jsonAdmin.getPassword());
            editor.putString("name", jsonAdmin.getName());
            editor.putString("say", jsonAdmin.getSay());
            editor.putString("level", jsonAdmin.getLevel());
            editor.putString("sex", jsonAdmin.getSex());
            editor.putString("local", jsonAdmin.getLocal());
            editor.putString("age", jsonAdmin.getAge());
            editor.putString("stu_id", jsonAdmin.getStu_id());
            editor.putString("eol_pass", jsonAdmin.getEol_pass());
            editor.putString("jwgl_pass", jsonAdmin.getJwgl_pass());
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
            result = "网络异常";
            System.out.println("Service输出结果：\n" + result);
        }

    }

    public void AttemptToLogin(final String admin, final String pass) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        params.put("password", pass);
        asyncHttpClient.setTimeout(10000);
        //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
        asyncHttpClient.post(LoginServletAndroid, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //获取结果
                if (statusCode == 200) {
                    try {
                        result = new String(responseBody, "utf-8").replaceAll("\\s*", "");
                        // System.out.println("输出登录结果：\n" + result);
                        if ("0".equals(result)) {
                            Toast.makeText(InfoService.this, "密码失效，请重新登录", Toast.LENGTH_SHORT).show();
                            removepreferences();
                            Intent intent = new Intent("com.twinkle.service.Receiver");
                            intent.putExtra("FLAG", 0);
                            sendBroadcast(intent);


                        } else {
                            get_info(result);
                            new Util().downloadFile(admin, 0, InfoService.this);
                            Intent intent = new Intent("com.twinkle.service.Receiver");
                            intent.putExtra("FLAG", 1);
                            sendBroadcast(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void removepreferences() {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("admin");
        editor.remove("password");
        editor.remove("name");
        editor.remove("say");
        editor.remove("level");
        editor.remove("sex");
        editor.remove("local");
        editor.remove("age");
        editor.remove("stu_id");
        editor.remove("eol_pass");
        editor.remove("jwgl_pass");
        editor.apply();
    }

    public void set_Alarm() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000 * Integer.parseInt(preferences.getString("sync_frequency", "10")); // 这是一小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }


}