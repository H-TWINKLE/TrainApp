package com.gy.ticket;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.ticket.adapter.NewsAdapter;
import com.gy.ticket.java.InitString;
import com.gy.ticket.java.JsonParse;
import com.gy.ticket.user.Film;
import com.gy.ticket.user.Play;
import com.gy.ticket.user.Sing;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsActivity extends Fragment {

    private NewsAdapter adapter;
    private ListView lvnews;
    private List<Film> list = new ArrayList<>();
    private List<Sing> list2 = new ArrayList<>();
    private List<Play> list3 = new ArrayList<>();

    public NewsActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news, container, false);
        lvnews = (ListView) view.findViewById(R.id.lv_news);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geturl();


    }

    private void geturl() {
        RequestParams requestParams = new RequestParams(InitString.news_url);

        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    String text = new String(result, "utf-8");
                    list = JSON.parseArray(text, Film.class);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getSing();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void getSing() {
        RequestParams requestParams = new RequestParams(InitString.Sing_url);
        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    String text = new String(result, "utf-8");
                    text = "[ " + text + " ]";
                    list2 = JSON.parseArray(text, Sing.class);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getPlay();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void getPlay() {
        RequestParams requestParams = new RequestParams(InitString.Play_url);
        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    String text = new String(result, "utf-8");
                    text = "[ " + text + " ]";
                    list3 = JSON.parseArray(text, Play.class);
                    adapter = new NewsAdapter(getActivity(), list, list2, list3);
                    lvnews.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
