package com.twinkle.train;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.twinkle.train.com.twinkle.java.Jwgl_Dialog;
import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.MyDialog;
import com.twinkle.train.com.twinkle.java.Util;
import com.yalantis.phoenix.PullToRefreshView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.image.ImageOptions;
import org.xutils.x;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.twinkle.train.ForgetPassActivity.result;

public class Jwgl_Stu_idActivity extends AppCompatActivity implements View.OnClickListener {

    PullToRefreshView rtf_study_stu_info;
    SimpleAdapter simpleAdapter;
    ArrayList<Map<String, Object>> mData = new ArrayList<>();
    ListView lvw_study_stu_info;
    ImageView ivw_stu_fsh, ivw_stu_login;
    String admin, stu_id, jwgl_pass;
    SharedPreferences preferences;
    Jwgl_Dialog jwgl_dialog;
    MyApp myApp;
    DbManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwgl__stu_id);
        init();


    }

    public void init() {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        stu_id = preferences.getString("stu_id", "学号");
        jwgl_pass = preferences.getString("jwgl_pass", "教务管理系统密码");

        ivw_stu_fsh = (ImageView) findViewById(R.id.ivw_study_stu_info_fsh);
        ivw_stu_fsh.setOnClickListener(this);
        ivw_stu_login = (ImageView) findViewById(R.id.ivw_study_stu_info_login);
        ivw_stu_login.setOnClickListener(this);

        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
        if(bm!=null){
            ViewGroup.LayoutParams lp = ivw_stu_login.getLayoutParams();
            lp.width = 60;
            lp.height = 60;
            ivw_stu_login.setLayoutParams(lp);
            x.image().bind(ivw_stu_login,Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build());
        }
        else {
            ivw_stu_login.setImageResource(R.drawable.pic);
        }

        lvw_study_stu_info = (ListView) findViewById(R.id.lvw_study_stu_info);
        load_data();
        rtf_study_stu_info = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        rtf_study_stu_info.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rtf_study_stu_info.setRefreshing(false);

                jwgl_login_Dialog();

            }
        });
    }


    public void load_data() {
        try {

            myApp = new MyApp();
            db = x.getDb(myApp.daoConfig);
            //   List<DbModel> tip = db.findDbModelAll(new SqlInfo("select * from jwgl_info where type = 'tip'"));
            //  List<DbModel> info = db.findDbModelAll(new SqlInfo("select * from jwgl_info where type = 'info'"));
            DbModel tip = db.findDbModelFirst(new SqlInfo("select * from jwgl_info where type = 'tip'"));
            DbModel info = db.findDbModelFirst(new SqlInfo("select * from jwgl_info where type = 'info'"));


            Map<String, Object> item = new HashMap<>();
            item.put("tip", tip.getString("xuehao"));
            item.put("info", info.getString("xuehao"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xingming"));
            item.put("info", info.getString("xingming"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xingbie"));
            item.put("info", info.getString("xingbie"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("ruxue"));
            item.put("info", info.getString("ruxue"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("chusheng"));
            item.put("info", info.getString("chusheng"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("mingzu"));
            item.put("info", info.getString("mingzu"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("zhengzhi"));
            item.put("info", info.getString("zhengzhi"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("dianhua"));
            item.put("info", info.getString("dianhua"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("youbian"));
            item.put("info", info.getString("youbian"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("zhunkao"));
            item.put("info", info.getString("zhunkao"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("shenfenzheng"));
            item.put("info", info.getString("shenfenzheng"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xueli"));
            item.put("info", info.getString("xueli"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xueyuan"));
            item.put("info", info.getString("xueyuan"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("jiating"));
            item.put("info", info.getString("jiating"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("zhuanye"));
            item.put("info", info.getString("zhuanye"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("banji"));
            item.put("info", info.getString("banji"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("yinyu"));
            item.put("info", info.getString("yinyu"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xuezhi"));
            item.put("info", info.getString("xuezhi"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("nianji"));
            item.put("info", info.getString("nianji"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("kaoshenghao"));
            item.put("info", info.getString("kaoshenghao"));
            mData.add(item);
            item = new HashMap<>();
            item.put("tip", tip.getString("xueji"));
            item.put("info", info.getString("xueji"));
            mData.add(item);



            simpleAdapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2,
                    new String[]{"tip", "info"}, new int[]{android.R.id.text1, android.R.id.text2});

            lvw_study_stu_info.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivw_study_stu_info_fsh:
                finish();
                break;
            case R.id.ivw_study_stu_info_login:
                Intent intent = new Intent(this, AdminInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    public void jwgl_login_Dialog() {


        jwgl_dialog = new Jwgl_Dialog(Jwgl_Stu_idActivity.this, stu_id, jwgl_pass, new Jwgl_Dialog.OnDismissListener() {
            @Override
            public void onDismissListener() {
                finish();
            }
        });
        jwgl_dialog.setCancelable(false);
        jwgl_dialog.show();


    }


}
