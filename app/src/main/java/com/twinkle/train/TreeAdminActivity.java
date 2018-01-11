package com.twinkle.train;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.image.SmartImageView;
import com.twinkle.train.com.twinkle.java.Util;
import com.twinkle.train.com.twinkle.user.JsonTree;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.Header;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by TWINKLE on 2017/9/24.
 */

public class TreeAdminActivity extends AppCompatActivity {

    Adapter adapter;
    String admin;
    ImageView ivw_treeadmin_fsh;
    ListView lv_admin;
    SharedPreferences preferences;
    String result;
    final String TreelookServlet = "http://119.29.98.60:8080/trainAndroid/TreelookServlet";
    String pic_url = "http://119.29.98.60:8080/trainAndroid/pic/";
    List<JsonTree> list,list1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admintree);
        init();
        get_data_admin();

    }

    public void init() {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");

        lv_admin = (ListView) findViewById(R.id.lvw_tree_admin);
        ivw_treeadmin_fsh = (ImageView) findViewById(R.id.ivw_tree_fsh);
        ivw_treeadmin_fsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void set_lvtree() {
        adapter = new Adapter();
        lv_admin.setAdapter(adapter);
    }

    public void get_data_admin() {
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
                        list = JSON.parseArray(result, JsonTree.class);
                        for(JsonTree jsonTree :list){
                            if(jsonTree.getAdmin().equals(admin)){
                                list1.add(jsonTree);
                            }
                        }
                        set_lvtree();

                    } catch (Exception e) {
                        Toast.makeText(TreeAdminActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TreeAdminActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private class Adapter extends BaseAdapter {
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            class ViewHolder {
                private   TextView tvw_name;
                private   TextView tvw_info;
                private   TextView tvw_date;
                SmartImageView pic;
            }
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.listview_tree, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvw_name = (TextView) convertView.findViewById(R.id.tvw_ls_say);
                viewHolder.tvw_info = (TextView) convertView.findViewById(R.id.tvw_ls_info);
                viewHolder.tvw_date = (TextView) convertView.findViewById(R.id.tvw_ls_time);
                viewHolder.pic = (SmartImageView) convertView.findViewById(R.id.ivw_ls_pic);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String admin = list1.get(position).getAdmin();
            viewHolder.tvw_name.setText(admin);
            viewHolder.tvw_info.setText(list1.get(position).getInformation());
            viewHolder.tvw_date.setText(list1.get(position).getTime());
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
            if(bm!=null){
                x.image().bind(viewHolder.pic, Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder().
                        setCircular(true).setUseMemCache(false).setFailureDrawableId(R.drawable.pic).build());
            }
            else {
                viewHolder.pic.setImageResource(R.drawable.pic);
            }
            return convertView;
        }
    }


}
