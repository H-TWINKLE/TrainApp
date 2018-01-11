package com.twinkle.train;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;


import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.Util;
import com.twinkle.train.com.twinkle.java.MyAdapter;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Jwgl_TtbActivity extends AppCompatActivity implements View.OnClickListener {


    GridView detailCource;
    MyAdapter adapter;
    ImageView ivw_jwgl_ttb_fsh, ivw_jwgl_ttb_login;
    String admin, stu_id, jwgl_pass;
    SharedPreferences preferences;
    MyApp myApp;
    DbManager db;
    String[] text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwgl__ttb);

        init();

    }


    public void init() {


        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        stu_id = preferences.getString("stu_id", "学号");
        jwgl_pass = preferences.getString("jwgl_pass", "教务管理系统密码");

        ivw_jwgl_ttb_login = (ImageView) findViewById(R.id.ivw_jwgl_ttb_login);
        ivw_jwgl_ttb_login.setOnClickListener(this);

        ivw_jwgl_ttb_fsh = (ImageView) findViewById(R.id.ivw_jwgl_ttb_fsh);
        ivw_jwgl_ttb_fsh.setOnClickListener(this);

        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
        if(bm!=null){
            ViewGroup.LayoutParams lp = ivw_jwgl_ttb_login.getLayoutParams();
            lp.width = 60;
            lp.height = 60;
            ivw_jwgl_ttb_login.setLayoutParams(lp);
            x.image().bind(ivw_jwgl_ttb_login,Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build());
        }
        else {
            ivw_jwgl_ttb_login.setImageResource(R.drawable.pic);
        }

       detailCource = (GridView) findViewById(R.id.courceDetail);
        try {
            List<String> list = i();
            adapter = new MyAdapter(this, list);
            detailCource.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivw_jwgl_ttb_fsh:
                finish();
                break;
            case R.id.ivw_jwgl_ttb_login:
                Intent intent = new Intent(this, AdminInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    public List<String>
    i() throws Exception {


        myApp = new MyApp();
        db = x.getDb(myApp.daoConfig);

        List<DbModel> info = db.findDbModelAll(new SqlInfo("select * from jwgl_ttb where type = 'info'"));
        text = new String[info.size()];

        List<String> list = new ArrayList<>();
        for (int x = 0; x < info.size(); x++) {
            list.add(info.get(x).getString("zhouyi"));
        }
        for (int x = 0; x < info.size(); x++) {
            list.add(info.get(x).getString("zhouer"));
        }
        for (int x = 0; x < info.size(); x++) {
            list.add(info.get(x).getString("zhousan"));
        }
        for (int x = 0; x < info.size(); x++) {
            list.add(info.get(x).getString("zhousi"));
        }
        for (int x = 0; x < info.size(); x++) {
            list.add(info.get(x).getString("zhouwu"));
        }


        int y = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                y++;
            }
        }
        Log.i("y", Integer.toString(y));

        text = new String[y];
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals("")) {
                text[j] = list.get(i).replaceAll(".第[\\d]+-[\\d]+周", "").replaceAll("1{2}", "十一").replace("}","");
                j++;

            }
        }

        Log.i("arr", Arrays.toString(text));

        String[] newtext = new String[30];


        for (int x = 0; x < text.length; x++) {
            if (math(text[x], "周一")) {
                if (math(text[x], "第1")) {
                    newtext[0] = text[x];
                }
                if (math(text[x], "第3")) {
                    newtext[5] = text[x];
                }
                if (math(text[x], "第5")) {
                    newtext[10] = text[x];
                }
                if (math(text[x], "第7")) {
                    newtext[15] = text[x];
                }
                if (math(text[x], "第9")) {
                    newtext[20] = text[x];
                }
                if (math(text[x], "第十一")) {
                    newtext[25] = text[x];
                }

            }
            if (math(text[x], "周二")) {

                if (math(text[x], "第1")) {
                    newtext[1] = text[x];
                }
                if (math(text[x], "第3")) {
                    newtext[6] = text[x];
                }
                if (math(text[x], "第5")) {
                    newtext[11] = text[x];
                }
                if (math(text[x], "第7")) {
                    newtext[16] = text[x];
                }
                if (math(text[x], "第9")) {
                    newtext[21] = text[x];
                }
                if (math(text[x], "第十一")) {
                    newtext[26] = text[x];
                }


            }
            if (math(text[x], "周三")) {
                if (math(text[x], "第1")) {
                    newtext[2] = text[x];
                }
                if (math(text[x], "第3")) {
                    newtext[7] = text[x];
                }
                if (math(text[x], "第5")) {
                    newtext[12] = text[x];
                }
                if (math(text[x], "第7")) {
                    newtext[17] = text[x];
                }
                if (math(text[x], "第9")) {
                    newtext[22] = text[x];
                }
                if (math(text[x], "第十一")) {
                    newtext[27] = text[x];
                }


            }
            if (math(text[x], "周四")) {

                if (math(text[x], "第1")) {
                    newtext[3] = text[x];
                }
                if (math(text[x], "第3")) {
                    newtext[8] = text[x];
                }
                if (math(text[x], "第5")) {
                    newtext[13] = text[x];
                }
                if (math(text[x], "第7")) {
                    newtext[18] = text[x];
                }
                if (math(text[x], "第9")) {
                    newtext[23] = text[x];
                }
                if (math(text[x], "第十一")) {
                    newtext[28] = text[x];
                }


            }
            if (math(text[x], "周五")) {
                if (math(text[x], "第1")) {
                    newtext[4] = text[x];
                }
                if (math(text[x], "第3")) {
                    newtext[9] = text[x];
                }
                if (math(text[x], "第5")) {
                    newtext[14] = text[x];
                }
                if (math(text[x], "第7")) {
                    newtext[19] = text[x];
                }
                if (math(text[x], "第9")) {
                    newtext[24] = text[x];
                }
                if (math(text[x], "第十一")) {
                    newtext[29] = text[x];
                }

            }

        }


        String[] renew = new String[newtext.length];
        for (int z = 0; z < newtext.length; z++) {

            if (newtext[z] == null) {
                renew[z] = "";

            } else {
                renew[z] = newtext[z];
            }
        }

        Log.i("new", Arrays.toString(renew));
        List<String> lists = Arrays.asList(renew);

        return lists;

        //  return  list;

    }

    public boolean math(String week, String str) {

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(week);
        return m.find();

    }

}
