package com.twinkle.train;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.Util;
import com.twinkle.train.com.twinkle.service.InfoService;
import com.twinkle.train.com.twinkle.user.Mood;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;
import com.yalantis.phoenix.PullToRefreshView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.twinkle.train.R.anim.anim_alpha_1;

public class MainNActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    private JellyToolbar jbrmain;
    private FrameLayout lltmain;
    private AppCompatEditText editText;
    private SmartImageView ivw_main_logo, ivw_main_logo_2;
    private ImageView ivw_menu_level;
    private TextView tvw_main_title, tvw_menu_name, tvw_menu_say, tvw_menu_tool, tvw_menu_map, tvw_menu_live, tvw_menu_learn, tvw_menu_setting;
    private long mExitTime;
    private GuillotineAnimation guillotineAnimation;
    private boolean flag = false;
    private View layou_Menu;
    private SharedPreferences preferences;
    private String level, admin;
    private MainNActivity.MsgReceiver msgReceiver;
    private AlertDialog dialog_level;
    private String img_url = "http://119.29.98.60:8080/trainAndroid/pic/";
    private TextView main_n_article, main_n_name, main_n_title;
    private String artcle_url = "http://119.29.98.60:8080/Hmovie/MoodServlet";
    private MyApp myApp;
    private DbManager db;
    private PullToRefreshView pfv_main_n;
    private int number = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createReceiver();
        setContentView(R.layout.activity_main_n);
        try {
            init();
            apply();
            get_info_service();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        myApp = new MyApp();
        ivw_main_logo = (SmartImageView) findViewById(R.id.ivw_main_logo);
        tvw_main_title = (TextView) findViewById(R.id.tvw_main_title);
        lltmain = (FrameLayout) findViewById(R.id.activity_main_n);
        jbrmain = (JellyToolbar) findViewById(R.id.jbr_main);

        layou_Menu = LayoutInflater.from(MainNActivity.this).inflate(R.layout.layout_menu, null);
        ivw_main_logo_2 = (SmartImageView) layou_Menu.findViewById(R.id.ivw_menu_logo);
        tvw_menu_name = (TextView) layou_Menu.findViewById(R.id.tvw_menu_name);
        tvw_menu_name.setOnClickListener(this);
        tvw_menu_say = (TextView) layou_Menu.findViewById(R.id.tvw_menu_say);
        tvw_menu_say.setOnClickListener(this);
        tvw_menu_map = (TextView) layou_Menu.findViewById(R.id.tvw_menu_map);
        tvw_menu_map.setOnClickListener(this);
        tvw_menu_learn = (TextView) layou_Menu.findViewById(R.id.tvw_menu_learn);
        tvw_menu_learn.setOnClickListener(this);
        tvw_menu_live = (TextView) layou_Menu.findViewById(R.id.tvw_menu_live);
        tvw_menu_live.setOnClickListener(this);
        tvw_menu_tool = (TextView) layou_Menu.findViewById(R.id.tvw_menu_tool);
        tvw_menu_tool.setOnClickListener(this);
        tvw_menu_setting = (TextView) layou_Menu.findViewById(R.id.tvw_menu_setting);
        tvw_menu_setting.setOnClickListener(this);
        ivw_menu_level = (ImageView) layou_Menu.findViewById(R.id.ivw_menu_level);
        ivw_menu_level.setOnClickListener(this);

        main_n_article = (TextView) findViewById(R.id.main_n_article);
        main_n_name = (TextView) findViewById(R.id.main_n_name);
        main_n_title = (TextView) findViewById(R.id.main_n_title);

        pfv_main_n = (PullToRefreshView) findViewById(R.id.pfv_main_n);


    }

    private void apply() throws Exception {

        pfv_main_n.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pfv_main_n.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (number > 5) {
                            xpath_article();
                        } else {
                            get_article();
                            number =0;
                        }
                        number++;
                        pfv_main_n.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        String name = preferences.getString("name", "请登录");
        String say = preferences.getString("say", "");
        level = preferences.getString("level", "0");
        set_toolbar();

        set_level_img();
        tvw_menu_name.setText(name);
        tvw_menu_say.setText(say);
        set_logo_img();
        get_article();
    }


    public void get_info_service() {

        Intent intent = new Intent(MainNActivity.this, InfoService.class);
        startService(intent);

    }


    private void set_toolbar() {
        editText = (AppCompatEditText) LayoutInflater.from(this).inflate(R.layout.content_main_edit, null);
        editText.setBackgroundResource(R.color.colorTransparent);
        editText.setOnKeyListener(this);

        jbrmain.setContentView(editText);
        jbrmain.setJellyListener(jellyListener);

        lltmain.addView(layou_Menu);


        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(layou_Menu, layou_Menu.findViewById(R.id.ivw_menu_logo), ivw_main_logo)
                .setStartDelay(250)
                .setActionBarViewForAnimation(jbrmain)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        flag = true;
                    }

                    @Override
                    public void onGuillotineClosed() {
                        flag = false;
                    }
                })
                .build();


    }


    private JellyListener jellyListener = new JellyListener() {

        @Override
        public void onToolbarExpandingStarted() {
            super.onToolbarExpandingStarted();
            ivw_main_logo.setVisibility(View.GONE);
            tvw_main_title.setVisibility(View.GONE);
        }

        @Override
        public void onToolbarCollapsingStarted() {
            super.onToolbarCollapsingStarted();
            ivw_main_logo.setVisibility(View.VISIBLE);
            tvw_main_title.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editText.getText())) {
                jbrmain.collapse();
            } else {
                editText.getText().clear();
            }
        }

    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvw_menu_name:
                checkadmin();
                break;
            case R.id.tvw_menu_say:
                checkadmin();
                break;
            case R.id.tvw_menu_map:
                break;
            case R.id.tvw_menu_learn:
                startActivity(new Intent(MainNActivity.this, StudyActivity.class));
                break;
            case R.id.tvw_menu_live:
                startActivity(new Intent(MainNActivity.this, TreeActivity.class));
                break;
            case R.id.tvw_menu_tool:
                startActivity(new Intent(MainNActivity.this, ToolActivity.class));
                break;
            case R.id.tvw_menu_setting:
                startActivity(new Intent(MainNActivity.this, SettingsActivity.class));
                break;
            case R.id.ivw_menu_level:
                showDialog();
                break;
            default:
                break;
        }
    }

    public void checkadmin() {
        Intent intent;
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String admin = preferences.getString("admin", "admin");
        if (admin.equals("admin")) {
            intent = new Intent(MainNActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(MainNActivity.this, AdminInfoActivity.class);
            startActivity(intent);
        }
    }

    public void showDialog() {

        String message;
        String title;

        if (admin.equals("admin")) {
            title = "";
            message = "登录后可以看见您的经验值喔！";

        } else {
            title = admin;
            message = "您现在的经验值为：" + level + "L,每天启动易用、发布信息、更改信息都可以提高经验值喔！";
        }

        dialog_level = new AlertDialog.Builder(MainNActivity.this)
                .setMessage(message)
                .setTitle(title)
                .create();
        dialog_level.setCancelable(true);
        dialog_level.show();

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            String text = editText.getText().toString();
            if (TextUtils.isEmpty(text)) {
                Snackbar.make(lltmain, "输入内容为空", Snackbar.LENGTH_SHORT).show();
                return true;
            }
            Uri uri = Uri.parse("https://m.baidu.com/s?from=1086k&word="+text);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (flag) {
            guillotineAnimation.close();
            flag = false;
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainNActivity.this, getString(R.string.confirm_to_quit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
    }

    public void createReceiver() {                   //动态注册广播接收器

        msgReceiver = new MainNActivity.MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.twinkle.service.Receiver");
        registerReceiver(msgReceiver, intentFilter);
    }


    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI

            int flag = intent.getIntExtra("FLAG", -1);

            System.out.println("FLAG注册：" + flag);

            if (flag == 0) {
                ivw_menu_level.setImageResource(R.drawable.level0);
                ivw_main_logo.setImageResource(R.drawable.pic);
                ivw_main_logo_2.setImageResource(R.drawable.pic);
                tvw_menu_name.setText("请登录");
                tvw_menu_say.setText("");
                removepreferences();

            }
            if (flag == 1) {
                preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                String name = preferences.getString("name", "请登录");
                String admin = preferences.getString("admin", "admin");
                String say = preferences.getString("say", "");
                String level = preferences.getString("level", "0");

                tvw_menu_name.setText(name);
                tvw_menu_say.setText(say);
                set_level_img();
                set_logo_img();
            }
        }

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
        editor.apply();

    }

    private void set_level_img() {
        switch (level) {
            case "0":
                ivw_menu_level.setImageResource(R.drawable.level0);
                break;
            case "1":
                ivw_menu_level.setImageResource(R.drawable.level1);
                break;
            case "2":
                ivw_menu_level.setImageResource(R.drawable.level2);
                break;
            case "3":
                ivw_menu_level.setImageResource(R.drawable.level3);
                break;
            case "4":
                ivw_menu_level.setImageResource(R.drawable.level4);
                break;
            case "5":
                ivw_menu_level.setImageResource(R.drawable.level5);
                break;
            case "6":
                ivw_menu_level.setImageResource(R.drawable.level6);
                break;
            case "null":
                ivw_menu_level.setImageResource(R.drawable.level0);
                break;
            default:
                ivw_menu_level.setImageResource(R.drawable.level6);
                break;
        }
        Animation animation = AnimationUtils.loadAnimation(MainNActivity.this, anim_alpha_1);
        ivw_menu_level.startAnimation(animation);
    }

    private void set_logo_img() {
        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
        if(bm!=null){
            x.image().bind(ivw_main_logo,
                    Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                            .setCircular(true)
                            .setUseMemCache(false)
                            .build());
            x.image().bind(ivw_main_logo_2,
                    Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",new ImageOptions.Builder()
                            .setCircular(true)
                            .setUseMemCache(false)
                            .build());
        }else {
            ivw_main_logo.setImageResource(R.drawable.pic);
            ivw_main_logo_2.setImageResource(R.drawable.pic);
        }

    }


    private void get_article() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(System.currentTimeMillis()));
        db = x.getDb(myApp.daoConfig);
        List<DbModel> info = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int size = Integer.parseInt(preferences.getString("text_size", "15"));
        int flag = 0;
        try {
            info = db.findDbModelAll(new SqlInfo("select * from Mood where date = '" + date + "'"));
            flag = info.size();
        } catch (Exception e) {
            xpath_article();
        }
        if (flag > 0) {
            int random = new Random().nextInt(flag);
            main_n_title.setText(info.get(random).getString("title"));
            main_n_name.setText(info.get(random).getString("name"));
            main_n_article.setText(info.get(random).getString("article"));
            main_n_article.setTextSize(size);
        }else{
            xpath_article();
        }


    }

    private void xpath_article() {
        RequestParams params = new RequestParams(artcle_url);
        params.setReadTimeout(10000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    result = "[" + result + "]";
                    List<Mood> list = JSON.parseArray(result, Mood.class);
                    db.saveOrUpdate(list);
                    db.close();
                    get_article();
                } catch (Exception e) {
                    Snackbar.make(lltmain, "网络异常", Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Snackbar.make(lltmain, "网络异常", Snackbar.LENGTH_SHORT).show();
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

