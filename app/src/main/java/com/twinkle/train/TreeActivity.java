package com.twinkle.train;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;
import com.twinkle.train.com.twinkle.java.Util;
import com.twinkle.train.com.twinkle.user.JsonTree;
import com.yalantis.phoenix.PullToRefreshView;
import org.apache.http.Header;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TreeActivity extends AppCompatActivity {
    Adapter adapter;
    EditText ett_tree;
    String admin, info;
    String time = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(System.currentTimeMillis()));
    Button btn_tree;
    ImageView ivw_tree_fsh, ivw_tree_login;
    ListView lv;
    LinearLayout view;
    SharedPreferences preferences;
    String result;
    String TreelookServlet = "http://119.29.98.60:8080/trainAndroid/TreelookServlet";
    String AddtreeServletAndroid = "http://119.29.98.60:8080/trainAndroid/AddtreeServletAndroid";
    String pic_url = "http://119.29.98.60:8080/trainAndroid/pic/";
    PullToRefreshView ptr_tree;
    List<JsonTree> list = new ArrayList<>();
    int pic[] = new int[]{
            R.drawable.p0,
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p9,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tree);
        init();
        get_data();
        setLogin_pic();

    }

    public void init() {
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        view = (LinearLayout) findViewById(R.id.view_tree);
        lv = (ListView) findViewById(R.id.lvw_tree);
        btn_tree = (Button) findViewById(R.id.btn_tree);
        btn_tree.setOnClickListener(new ButtonListener());
        ett_tree = (EditText) findViewById(R.id.ett_tree);
        ivw_tree_fsh = (ImageView) findViewById(R.id.ivw_tree_fsh);
        ivw_tree_fsh.setOnClickListener(new ButtonListener());
        ivw_tree_login = (ImageView) findViewById(R.id.ivw_tree_login);
        ivw_tree_login.setOnClickListener(new ButtonListener());
        ptr_tree = (PullToRefreshView) findViewById(R.id.ptr_tree);
        ptr_tree.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptr_tree.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr_tree.setRefreshing(false);
                        list.clear();
                        get_data();
                        set_lv();
                    }
                }, 3000);
            }
        });
    }

    public void AttemptToSend(final String admin, final String info, final String time) {
        try {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("name", admin);
            params.put("information", info);
            params.put("time", time);
            asyncHttpClient.post(AddtreeServletAndroid, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    if (i == 200) {
                        try {
                            String result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                            if (result.equals("1")) {
                                Snackbar.make(view, getString(R.string.send_info_ok), Snackbar.LENGTH_SHORT).show();
                                ett_tree.getText().clear();
                                get_data();
                            } else {
                                Snackbar.make(view, getString(R.string.send_info_false), Snackbar.LENGTH_SHORT).show();
                                ett_tree.getText().clear();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ett_tree.getText().clear();
                        Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                    ett_tree.getText().clear();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void send_message() {
        info = ett_tree.getText().toString();
        if (TextUtils.isEmpty(info)) {
            Toast.makeText(TreeActivity.this, getText(R.string.please_to_write_info), Toast.LENGTH_SHORT).show();
        } else {
            if (admin.equals("admin")) {
                admin = android.os.Build.MODEL;
            } else {
                AttemptToSend(admin, info, time);
            }
        }
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_tree:
                    send_message();
                    break;
                case R.id.ivw_tree_login:
                    dologin();
                    break;
                case R.id.ivw_tree_fsh:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    public void set_lv() {
        adapter = new Adapter();
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void get_data() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(10000);
        //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
        asyncHttpClient.post(TreelookServlet, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //获取结果
                if (statusCode == 200) {
                    try {
                        result = new String(responseBody, "utf-8").replaceAll("\\s*", "");
                        result = "[" + result + "]";
                        list = JSON.parseArray(result, JsonTree.class);
                        set_lv();
                    } catch (Exception e) {
                        Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setLogin_pic() {
        if ("admin".equals(admin)) {
            ivw_tree_login.setImageResource(R.drawable.login);
        } else {


            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
            if(bm!=null){
                ViewGroup.LayoutParams lp = ivw_tree_login.getLayoutParams();
                lp.width = 60;
                lp.height = 60;
                ivw_tree_login.setLayoutParams(lp);
                x.image().bind(ivw_tree_login,Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                        .setCircular(true)
                        .setUseMemCache(false)
                        .build());
            }
            else {
                ivw_tree_login.setImageResource(R.drawable.pic);
            }
             }
    }


    public void dologin() {
        Intent intent;
        if ("admin".equals(admin)) {
            intent = new Intent(TreeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(TreeActivity.this, TreeAdminActivity.class);
            startActivity(intent);
        }
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
            return list.size();
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

            String admin = list.get(position).getAdmin();
            viewHolder.tvw_name.setText(admin);
            viewHolder.tvw_info.setText(list.get(position).getInformation());
            viewHolder.tvw_date.setText(list.get(position).getTime());

            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
            if(bm!=null){
                x.image().bind(viewHolder.pic, Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder().
                        setCircular(true).setUseMemCache(false).setFailureDrawableId(pic[new Random().nextInt(9)]).build());
            }
            else {
                viewHolder.pic.setImageResource(pic[new Random().nextInt(9)]);
            }

            return convertView;
        }
    }
}

