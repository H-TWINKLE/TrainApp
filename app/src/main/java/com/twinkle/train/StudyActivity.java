package com.twinkle.train;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.twinkle.train.com.twinkle.java.BuilderManager;
import com.yalantis.phoenix.PullToRefreshView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;


public class StudyActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView ivw_study_fsh, ivw_study_login;
    SharedPreferences preferences, preferences_admin;
    TextView tvw_study_notice;
    String admin, stu_id, eol_pass, jwgl_pass, Cookie_name, Cookie_value, tip = "";
    boolean flag = false;
    final String eol_url = "http://eol.cdnu.edu.cn/eol/homepage/common/login.jsp";
    final String eol_url_notice = "http://eol.cdnu.edu.cn/eol/welcomepage/student/index.jsp";
    ListView lv_study;
    PullToRefreshView ptr_study;
    LinearLayout view;
    List<String> list = new ArrayList<>();
    BoomMenuButton boomMenuButton;
    NotificationManager myManager;
    Notification myNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        init();
        default_data();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivw_study_login:
                Intent intent = new Intent(StudyActivity.this, AdminInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ivw_study_fsh:
                finish();
                break;
            default:
                break;
        }
    }


    public void init() {

        view = (LinearLayout) findViewById(R.id.view_study);
        myManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        ivw_study_fsh = (ImageView) findViewById(R.id.ivw_study_fsh);
        ivw_study_fsh.setOnClickListener(this);

        ivw_study_login = (ImageView) findViewById(R.id.ivw_study_login);
        ivw_study_login.setAdjustViewBounds(true);
        ivw_study_login.setOnClickListener(this);

        tvw_study_notice = (TextView) findViewById(R.id.tvw_study_notice);

        lv_study = (ListView) findViewById(R.id.lvw_study_notice);

        ptr_study = (PullToRefreshView) findViewById(R.id.ptr_study);
        ptr_study.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptr_study.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr_study.setRefreshing(false);
                        set_simpleAdapter();
                    }
                }, 3000);
            }
        });


        boomMenuButton = (BoomMenuButton) findViewById(R.id.bmb);
        if (boomMenuButton != null) {
            boomMenuButton.setButtonEnum(ButtonEnum.Ham);
            boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.HAM_4);
            boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.HAM_4);
            for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
                boomMenuButton.addBuilder(BuilderManager.getHamButtonBuilder());
            }
            boomMenuButton.setOnBoomListener(new OnBoomListener() {
                @Override
                public void onClicked(int index, BoomButton boomButton) {
                    to_activity(index);
                }

                @Override
                public void onBackgroundClick() {

                }

                @Override
                public void onBoomWillHide() {

                }

                @Override
                public void onBoomDidHide() {

                }

                @Override
                public void onBoomWillShow() {

                }

                @Override
                public void onBoomDidShow() {

                }
            });

        }


    }


    public void default_data() {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        stu_id = preferences.getString("stu_id", "学号");
        jwgl_pass = preferences.getString("jwgl_pass", "教务管理系统密码");
        eol_pass = preferences.getString("eol_pass", "网络教学平台密码");


        preferences_admin = getSharedPreferences(admin, Context.MODE_PRIVATE);
        list.clear();
        for (int x = 0; x < 100; x++) {
            String cl = preferences_admin.getString("class" + Integer.toString(x), Integer.toString(x));
            if (!cl.equals(Integer.toString(x))) {
                list.add(cl);
            }
        }

        set_simpleAdapter();

        if ("admin".equals(admin)) {
            Intent intent = new Intent(StudyActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
            if (bm != null) {
                ViewGroup.LayoutParams lp = ivw_study_login.getLayoutParams();
                lp.width = 60;
                lp.height = 60;
                ivw_study_login.setLayoutParams(lp);
                x.image().bind(ivw_study_login, Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg", new ImageOptions.Builder()
                        .setCircular(true)
                        .setUseMemCache(false)
                        .build());
            } else {
                ivw_study_login.setImageResource(R.drawable.pic);
            }
        }
        if (stu_id.equals("学号") || stu_id.isEmpty() || eol_pass.isEmpty() || eol_pass.equals("网络教学平台密码")) {

            Snackbar.make(view, "请填写学号和网络教学密码", Snackbar.LENGTH_SHORT).show();

        } else {
            flag = true;
            ptr_study.setRefreshing(true);
            pre_eol();
        }
    }

    public void pre_eol() {
        RequestParams parms = new RequestParams(eol_url);//请求的url
//有参数的时候，添加参数
        parms.addBodyParameter("IPT_LOGINUSERNAME", stu_id);
        parms.addBodyParameter("IPT_LOGINPASSWORD", eol_pass);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                // 请求取消
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                tip = "网络异常";
                Snackbar.make(view, tip, Snackbar.LENGTH_SHORT).show();
                tvw_study_notice.setText("网络异常，以下为之前的作业");
            }

            @Override
            public void onFinished() {
                //请求完成的时候执行
                eol_scrapy();
            }

            @Override
            public void onSuccess(byte[] arg0) {
                //在请求成功之后，获取cookies信息
            }
        });

    }

    public void to_activity(int activity) {
        Intent intent;
        if (flag) {
            if (jwgl_pass.equals("教务管理系统密码") || jwgl_pass.isEmpty()) {
                intent = new Intent(StudyActivity.this, AdminInfoActivity.class);
                startActivity(intent);
                Toast.makeText(StudyActivity.this, "请填写学号和教务管理系统密码", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                switch (activity) {
                    case 0:
                        intent = new Intent(StudyActivity.this, Jwgl_Stu_idActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(StudyActivity.this, Jwgl_ScoreActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(StudyActivity.this, Jwgl_TtbActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(StudyActivity.this, Jwgl_ExamActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }

        } else {
            Toast.makeText(StudyActivity.this, "请填写学号和网络教学密码", Toast.LENGTH_SHORT).show();
            intent = new Intent(StudyActivity.this, AdminInfoActivity.class);
            startActivity(intent);
        }

    }

    public void eol_scrapy() {
        RequestParams parms = new RequestParams(eol_url);
        parms.addBodyParameter("IPT_LOGINUSERNAME", stu_id);
        parms.addBodyParameter("IPT_LOGINPASSWORD", eol_pass);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                tip = "网络异常";
                Snackbar.make(view, "网络异常", Snackbar.LENGTH_SHORT).show();
                tvw_study_notice.setText("网络异常，以下为之前的作业");
                ptr_study.setRefreshing(false);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(byte[] arg0) {
                try {
                    String notice = new String(arg0, "gbk");
                    Document doc = Jsoup.parse(notice);
                    Element links = doc.select("div.login-success").first();
                    Log.i("login-success", links.text());
                    DbCookieStore instance = DbCookieStore.INSTANCE;
                    List<HttpCookie> cookies = instance.getCookies();
                    for (HttpCookie cookie : cookies) {
                        Cookie_name = cookie.getName();
                        Cookie_value = cookie.getValue();
                    }
                    eol_scrapy_notice();
                } catch (Exception e) {

                    tip = "学号或者密码错误，请检查！";

                    Snackbar.make(view, tip, Snackbar.LENGTH_SHORT).show();
                    ptr_study.setRefreshing(false);
                    tvw_study_notice.setText(tip);
                    lv_study.setVisibility(View.GONE);
                    e.printStackTrace();


                }

            }
        });

    }

    public void eol_scrapy_notice() {
        RequestParams parms = new RequestParams(eol_url_notice);
        parms.addHeader(Cookie_name, Cookie_value);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    String notice = new String(result, "gbk");
                    Document doc = Jsoup.parse(notice);
                    Elements links = doc.getElementById("reminder").select("a[href]");
                    set_notice(links);
                } catch (Exception e) {
                    e.printStackTrace();
                    tip = "恭喜您，作业都做完了！";
                    notice();
                    ptr_study.setRefreshing(false);
                    tvw_study_notice.setText(tip);
                    lv_study.setVisibility(View.GONE);
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

    public void set_notice(Elements links) {

        preferences_admin = getSharedPreferences(admin, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences_admin.edit();
        editor.clear();
        editor.apply();
        tip = links.get(0).text();

        editor.putString("tip", tip);
        list.clear();
        for (int x = 0; x < links.size() - 1; x++) {
            String cl = links.get(x + 1).text();
            list.add(cl);
            editor.putString("class" + x, cl);
        }

        editor.apply();
        ptr_study.setRefreshing(false);
        tvw_study_notice.setText(tip);
        notice();
        set_simpleAdapter();
    }

    public void set_simpleAdapter() {
        lv_study.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, list));
    }

    private void notice() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notifications_new_message = preferences.getBoolean("notifications_new_message", true);
        boolean notifications_new_message_vibrate = preferences.getBoolean("notifications_new_message_vibrate", true);
        Notification.Builder myBuilder = new Notification.Builder(StudyActivity.this);
        myBuilder.setContentTitle("作业提示")
                .setContentText(tip)
                .setTicker("您收到新的消息")
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                //设置默认声音和震动
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setVisibility(Notification.VISIBILITY_PRIVATE);

        if (notifications_new_message_vibrate) {
            myBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

        }
        if (notifications_new_message) {
            myBuilder.setDefaults(Notification.DEFAULT_SOUND);
            myNotification = myBuilder.build();
            //4.通过通知管理器来发起通知，ID区分通知
            myManager.notify(1, myNotification);
        }


    }

}
