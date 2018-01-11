package com.twinkle.train.com.twinkle.java;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.twinkle.train.ToolActivity;
import com.twinkle.train.VideoActivity;
import com.twinkle.train.Video_InfoActivity;
import com.twinkle.train.com.twinkle.user.Film;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TWINKLE on 2017/11/15.
 */

public class Spider {
    private String url_tv = "http://119.29.98.60:8080/Hmovie/SpiderTvServlet";
    private String url_film = "http://119.29.98.60:8080/Hmovie/SpiderFilmServlet";
    private String url_xpath = "http://www.61818988.com/index.php?url=";
    private String iface2 = "http://iface2.iqiyi.com/video/3.0/v_play?app_k=20168006319fc9e201facfbd7c2278b7&app_v=8.9.5&platform_id=10" +
            "&dev_os=8.0.1&dev_ua=Android&net_sts=1&qyid=9900077801666C&secure_p=GPhone&secure_v=1" +
            "&dev_hw=%7B%22cpu%22:0,%22gpu%22:%22%22,%22mem%22:%22%22%7D&app_t=0" +
            "&h5_url=";
    private String mixer = "http://mixer.video.iqiyi.com/mixin/videos/";
    MyApp myApp;
    DbManager db;
    String urls, name, tag, episode, img, info;
    List<HashMap<String, String>> list2, list1;
    Context context;

    public Spider(Context context) {
        myApp = new MyApp();
        this.context = context;
    }

    public void get_film() {

        final ProgressDialog progressDialog =
                new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.show();


        try {
            RequestParams parms = new RequestParams(url_film);//请求的url
            x.http().post(parms, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        result = "[" + result + "]";
                        List<Film> list = JSON.parseArray(result, Film.class);
                        ArrayList<Film> listes = new ArrayList<>();
                        db = x.getDb(myApp.daoConfig);
                        db.dropTable(Film.class);
                        for (Film lists : list) {
                            listes.add(new Film(lists.getTitle(), lists.getEpisode(), lists.getHref_img(), lists.getHref_url(), lists.getDate()));
                            db.saveOrUpdate(listes);
                        }
                        Log.i("film", "right");
                        db.close();
                        progressDialog.cancel();
                        context.startActivity(new Intent(context, VideoActivity.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            db.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void iface2(final Context context, String url) {
        final String urls = iface2 + url;
        RequestParams parms = new RequestParams(urls);//请求的url
        parms.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/53.0.2785.143 Safari/537.36");
        parms.addHeader("qyid", "0_a80059e64bc3a81f_F4Z91Z1EZ07ZFAZ42");
        parms.addHeader("t", "466931948");
        parms.addHeader("sign", "ffe061391e40c6a4ec7d1368c0333032");
        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String result = new String(bytes, "UTF-8");
                    String id = JSON.parseObject(result).get("play_aid").toString();
                    name = ((Map) JSON.parseObject(result).get("album")).get("_t").toString();
                    episode = ((Map) JSON.parseObject(result).get("album")).get("tvfcs").toString();
                    tag = ((Map) JSON.parseObject(result).get("album")).get("tag").toString();
                    img = ((Map) JSON.parseObject(result).get("album")).get("img_180x236").toString();
                    info = ((Map) JSON.parseObject(result).get("album")).get("desc").toString();
                    mixer(id, context);
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

    private void mixer(String url, final Context context) {

        final String urls = mixer + url;
        RequestParams parms = new RequestParams(urls);//请求的url
        //  parms.addHeader("User-Agent","Dalvik/2.1.0 (Linux; U; Android 7.1.2; M6 Note Build/N2G47H)");
        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String result = new String(bytes, "UTF-8");

                    String tvid = JSON.parseObject(result).get("tvId").toString();
                    String vid = JSON.parseObject(result).get("vid").toString();
                    String t = "" + System.currentTimeMillis();
                    String url = "/vps?" +
                            "tvid=" + tvid + "&vid=" + vid + "&v=0&qypid=" + tvid + "_12" +
                            "&src=01012001010000000000&t=" + t + "&k_tag=1&k_uid=9900077801666C" +
                            "&rs=1";
                    String vf = Util.vf((String) t + "d5fb4bd9d50c4be6948c97edd7254b0e" + tvid);
                    tmts(tvid, vid, t, vf, context);
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

    public void tmts(String tvid, String vid, String t, String vf, final Context context) {

        final String urls = "http://cache.m.iqiyi.com/tmts/" + tvid + "/" + vid + "/?" +
                "src=76f90cbd92f94a2e925d83e8ccd22cb7" +
                "&sc=" + vf + "&t=" + t;
        RequestParams parms = new RequestParams(urls);//请求的url
        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String result = new String(bytes, "UTF-8");

                    result = ((Map) JSON.parseObject(result).get("data")).get("vidl").toString();
                    JSONArray jsonArray = JSONArray.parseArray(result);

                    list2 = new ArrayList<>();
                    HashMap<String, String> hashMap;
                    for (int y = 0; y < jsonArray.size(); y++) {
                        hashMap = new HashMap<>();
                        hashMap.put("screenSize", JSON.parseObject(jsonArray.get(y).toString()).get("screenSize").toString());
                        hashMap.put("m3utx", JSON.parseObject(jsonArray.get(y).toString()).get("m3utx").toString());
                        list2.add(hashMap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, Video_InfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("episode", episode);
                intent.putExtra("tag", tag);
                intent.putExtra("img", img);
                intent.putExtra("info", info);
                intent.putExtra("list2", (Serializable) list2);
                context.startActivity(intent);
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
