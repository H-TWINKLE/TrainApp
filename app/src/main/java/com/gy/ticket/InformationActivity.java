package com.gy.ticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gy.ticket.java.InitString;
import com.gy.ticket.java.SharePerfence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_information_ok, iv_information_account;
    private ListView lv_info;
    private Toolbar tb_info;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Map<String, Object>> date = new ArrayList<>();
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        init();
        apply();
    }


    private void init() {

        iv_information_ok = (ImageView) findViewById(R.id.iv_information_ok);
        lv_info = (ListView) findViewById(R.id.lv_info);
        iv_information_account = (ImageView) findViewById(R.id.iv_information_account);
        tb_info = (Toolbar) findViewById(R.id.tb_info);
        logout = (TextView) findViewById(R.id.tv_info_logout);
    }

    private void apply() {

        iv_information_ok.setOnClickListener(this);
        iv_information_account.setOnClickListener(this);
        logout.setOnClickListener(this);

        tb_info.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_data();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_information_ok:
                break;
            case R.id.iv_information_account:
                break;
            case R.id.tv_info_logout:
                new SharePerfence("account", InformationActivity.this).remove_all();
                startActivity(new Intent(InformationActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
    }


    private void add_data() {
        ArrayList<String> list = new SharePerfence("account", InformationActivity.this).get_info();
        Map<String, Object> map;
        for (int x = 0; x < list.size(); x++) {
            map = new HashMap<>();
            map.put("title", list.get(x));
            map.put("info", InitString.lv_info[x]);
            date.add(map);
        }

        simpleAdapter = new SimpleAdapter(InformationActivity.this, date, android.R.layout.simple_expandable_list_item_2,
                new String[]{"info", "title"}, new int[]{android.R.id.text1, android.R.id.text2});
        lv_info.setAdapter(simpleAdapter);
    }


}
