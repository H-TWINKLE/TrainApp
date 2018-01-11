package com.twinkle.train;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.loopj.android.image.SmartImageView;
import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.Spider;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {


    private MyApp myApp;
    private DbManager db;
    private RecyclerView mRecyclerView;
    private FastScroller fastScroller;
    private List<DbModel> list;
    private CommonAdapter mAdapter;
    private Toolbar tbr_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        try {
            init();
            value();
            set_recyclerview();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void init() {

        myApp = new MyApp();
        mRecyclerView = (RecyclerView) findViewById(R.id.rvw_main);
        fastScroller = (FastScroller) findViewById(R.id.fastscroll);

        tbr_video = (Toolbar) findViewById(R.id.tbr_video);
        setSupportActionBar(tbr_video);
        tbr_video.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void set_recyclerview() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new CommonAdapter<DbModel>(VideoActivity.this, R.layout.recyclerview_content, list) {
            @Override
            protected void convert(ViewHolder holder, DbModel s, int position) {
                SmartImageView svw = holder.getView(R.id.svw_rvw);
                holder.setText(R.id.tvw_title_rvw, list.get(position).getString("title"));
                holder.setText(R.id.tvw_info_rvw, list.get(position).getString("episode"));
                svw.setImageUrl(list.get(position).getString("href_img"));
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        fastScroller.setRecyclerView(mRecyclerView);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    new Spider(VideoActivity.this).iface2(VideoActivity.this, list.get(position).getString("href_url"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    public void value() {
        try {
            db = x.getDb(myApp.daoConfig);
            list = db.findDbModelAll(new SqlInfo("select * from film"));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
