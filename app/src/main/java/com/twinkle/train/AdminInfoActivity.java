package com.twinkle.train;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twinkle.train.com.twinkle.java.MyApp;
import com.twinkle.train.com.twinkle.java.MyDialog;
import com.twinkle.train.com.twinkle.java.Util;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.http.Header;
import org.xutils.DbManager;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminInfoActivity extends AppCompatActivity {


    ListView lvw_info;
    ImageView ivw_info_fsh, ivw_info_pic;
    TextView tvw_info_ok, tvw_info_quit;
    String[] names = {"账号", "昵称", "性别", "年龄", "个性签名", "故乡", "等级", "学号", "网络教学平台密码", "教务管理系统密码"};
    String[] data;
    String[] age_int;
    String admin, password, name, age, say, sex, level, local, stu_id, jwgl_pass, eol_pass;
    String imagePath;
    String result;
    SimpleAdapter simpleAdapter;
    ArrayList<Map<String, Object>> mData = new ArrayList<>();
    private static final int IMAGE = 1;
    SharedPreferences preferences;
    AlertDialog alertDialog_sex, alertDialog_age;
    ProgressDialog progressDialog;
    PullToRefreshView ptr_info;
    MyApp myApp;
    DbManager db;

    static String UploadPicServletAndroid = "http://119.29.98.60:8080/trainAndroid/UploadPicServletAndroid";
    static String UpgradeServletAndroid = "http://119.29.98.60:8080/trainAndroid/UpgradeServletAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);
        init();
        setLvw_info();

    }


    public void init() {

        lvw_info = (ListView) findViewById(R.id.lvw_info);

        ivw_info_fsh = (ImageView) findViewById(R.id.ivw_info_fsh);
        ivw_info_fsh.setOnClickListener(new ButtonListener());

        ivw_info_pic = (ImageView) findViewById(R.id.ivw_info_pic);
        ivw_info_pic.setOnClickListener(new ButtonListener());

        tvw_info_ok = (TextView) findViewById(R.id.tvw_info_ok);
        tvw_info_ok.setOnClickListener(new ButtonListener());

        tvw_info_quit = (TextView) findViewById(R.id.tvw_info_quit);
        tvw_info_quit.setOnClickListener(new ButtonListener());

        ptr_info = (PullToRefreshView) findViewById(R.id.ptr_info);
        ptr_info.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptr_info.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ptr_info.setRefreshing(false);
                        mData.clear();
                        setLvw_info();

                    }
                }, 3000);
            }
        });

        progressDialog = new ProgressDialog(AdminInfoActivity.this);
        set_info();
    }

    public void set_info() {
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");
        password = preferences.getString("password", "password");
        name = preferences.getString("name", "名字");
        say = preferences.getString("say", "个性签名");
        level = preferences.getString("level", "0");
        sex = preferences.getString("sex", "男");
        local = preferences.getString("local", "中国");
        age = preferences.getString("age", "0");
        stu_id = preferences.getString("stu_id", "学号");
        jwgl_pass = preferences.getString("jwgl_pass", "教务管理系统密码");
        eol_pass = preferences.getString("eol_pass", "网络教学平台密码");
        data = new String[]{admin, name, sex, age, say, local, level, stu_id, eol_pass, jwgl_pass};

        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg");
        if (bm != null) {
            x.image().bind(ivw_info_pic,Environment.getExternalStorageDirectory() + "/How/" + admin + ".jpg",
                    new ImageOptions.Builder().setUseMemCache(false).setCircular(true).build());
        }
        else {
            ivw_info_pic.setImageResource(R.drawable.pic);
        }
    }

    public void setLvw_info() {
        set_simpleAdapter();
        lvw_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                if (position == 0 || position == 6) {
                } else if (position == 2) {
                    alertDialog_sex = new AlertDialog.Builder(AdminInfoActivity.this)
                            .setTitle(getString(R.string.choose_sex))
                            .setSingleChoiceItems(new String[]{"男", "女"}, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            data[position] = "男";
                                            System.out.println(Arrays.toString(data));
                                            mData.clear();
                                            set_simpleAdapter();
                                            alertDialog_sex.dismiss();
                                            break;
                                        case 1:
                                            data[position] = "女";
                                            System.out.println(Arrays.toString(data));
                                            mData.clear();
                                            set_simpleAdapter();
                                            dialog.dismiss();
                                            break;
                                    }

                                }
                            }).create();
                    alertDialog_sex.show();
                } else if (position == 3) {
                    age_int = new String[150];
                    for (int x = 0; x < 150; x++) {
                        age_int[x] = Integer.toString(x);
                    }
                    alertDialog_age = new AlertDialog.Builder(AdminInfoActivity.this)
                            .setTitle(getString(R.string.choose_age))
                            .setSingleChoiceItems(age_int, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    data[position] = age_int[which];
                                    System.out.println(Arrays.toString(data));
                                    mData.clear();
                                    set_simpleAdapter();
                                    alertDialog_age.dismiss();
                                }
                            }).create();
                    alertDialog_age.show();
                } else {
                    MyDialog dialog = new MyDialog(AdminInfoActivity.this, data[position], new MyDialog.OnEditInputFinishedListener() {
                        @Override
                        public void editInputFinished(String info) {
                            data[position] = info;
                            System.out.println(Arrays.toString(data));
                            mData.clear();
                            set_simpleAdapter();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivw_info_fsh:
                    finish();
                    break;
                case R.id.ivw_info_pic:
                    getimages();
                    break;
                case R.id.tvw_info_ok:
                    try {
                        setProgressDialog();
                        //   String[] names = {"账号","昵称","性别","年龄","个性签名","故乡","等级"};
                        uploadInfo(admin, password, data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.tvw_info_quit:
                    removepreferences();
                    removedb();
                    finish();
                    Intent intent = new Intent(AdminInfoActivity.this, MainNActivity.class);
                    startActivity(intent);
                    break;
            }

        }
    }

    public void removedb() {
        try {
            myApp = new MyApp();
            db = x.getDb(myApp.daoConfig);
            db.dropDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getimages() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        try {
            if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imagePath = c.getString(columnIndex);
                showImage(imagePath);
                c.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        Bitmap handlebm = Util.makeRoundCorner(bm);
        ivw_info_pic.setAdjustViewBounds(true);
        ViewGroup.LayoutParams lp = ivw_info_pic.getLayoutParams();
        lp.width = 350;
        lp.height = 350;
        ivw_info_pic.setLayoutParams(lp);
        ivw_info_pic.setImageBitmap(handlebm);
    }

    public void removepreferences() {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void set_simpleAdapter() {
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("title", names[i]);
            item.put("text", data[i]);
            mData.add(item);
        }
        simpleAdapter = new SimpleAdapter(this, mData, android.R.layout.simple_list_item_2,
                new String[]{"title", "text"}, new int[]{android.R.id.text1, android.R.id.text2});
        lvw_info.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

    }

    public void uploadFile(final String admin) throws Exception {
        try {
            final String tempPath = Environment.getExternalStorageDirectory().getPath() + "/How/" + admin + ".jpg";
            Util.compressBmpToFile(imagePath, new File(tempPath));
            File file = new File(tempPath);
            if (file.exists() && file.length() > 0) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("admin", admin);
                params.put("uploadfile", file);
                client.setTimeout(20000);
                // 上传文件
                client.post(UploadPicServletAndroid, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        // 上传成功后要做的工作
                        Toast.makeText(AdminInfoActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();
                        to_activity();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // 上传失败后要做到工作
                        Toast.makeText(AdminInfoActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        super.onRetry(retryNo);
                        // 返回重试次数
                    }
                });
            } else {
                Toast.makeText(AdminInfoActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Intent intent = new Intent(AdminInfoActivity.this, MainNActivity.class);
            startActivity(intent);
            e.printStackTrace();
            progressDialog.dismiss();
        }


    }

    public void uploadInfo(final String admin, final String pass, final String name, final String sex, final String age, final String say, final String local, final String level, final String stu_id, final String eol_pass, final String jwgl_pass) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        params.put("password", pass);
        params.put("name", name);
        params.put("sex", sex);
        params.put("age", age);
        params.put("say", say);
        params.put("local", local);
        params.put("level", level);
        params.put("stu_id", stu_id);
        params.put("eol_pass", eol_pass);
        params.put("jwgl_pass", jwgl_pass);
        System.out.println("上传数据:" + admin + "  " + pass + "" + "  " + name + "  " + sex + "  " + "  " + age + "  " + say + "  " + local + "  " + level + "  " + stu_id + "  " + eol_pass + "  " + jwgl_pass);
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.post(UpgradeServletAndroid, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    try {
                        result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                        System.out.println("输出上传信息结果：" + result);
                        if ("0".equals(result)) {
                            Toast.makeText(AdminInfoActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        } else if ("1".equals(result)) {
                            setpreferences(admin, pass, name, sex, age, say, local, level, stu_id, eol_pass, jwgl_pass);
                            Toast.makeText(AdminInfoActivity.this, getText(R.string.info_is_right), Toast.LENGTH_SHORT).show();
                            uploadFile(admin);
                        } else {
                            Toast.makeText(AdminInfoActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AdminInfoActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(AdminInfoActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void setProgressDialog() {
        progressDialog.setTitle(getString(R.string.info_upgradeing));
        progressDialog.setMessage(getString(R.string.info_upgradeing));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    public void setpreferences(String admin, String pass, String name, String sex, String age, String say, String local, String level, String stu_id, String eol_pass, String jwgl_pass) {
        //处理信息
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("admin", admin);
        editor.putString("password", pass);
        editor.putString("name", name);
        editor.putString("say", say);
        editor.putString("level", level);
        editor.putString("sex", sex);
        editor.putString("local", local);
        editor.putString("age", age);
        editor.putString("stu_id", stu_id);
        editor.putString("eol_pass", eol_pass);
        editor.putString("jwgl_pass", jwgl_pass);
        editor.apply();
    }

    public void to_activity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(AdminInfoActivity.this, MainNActivity.class));
                finish();
            }
        }, 1500);
    }

}
