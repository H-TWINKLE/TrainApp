package com.twinkle.train.com.twinkle.java;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.twinkle.train.R;
import com.twinkle.train.com.twinkle.user.Jwgl_exam;
import com.twinkle.train.com.twinkle.user.Jwgl_info;
import com.twinkle.train.com.twinkle.user.Jwgl_score;
import com.twinkle.train.com.twinkle.user.Jwgl_ttb;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.io.File;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TWINKLE on 2017/10/29.
 */

public class Jwgl_Dialog extends Dialog implements View.OnClickListener {

    EditText ett_jwgl_login_stu_id, ett_jwgl_login_stu_pass, ett_jwgl_login_stu_code;
    ImageView ivw_code;
    Button jwgl_login_ok, jwgl_login_quit;
    String jwgl_id, jwgl_pass, code, Cookie_name, Cookie_value, name;
    String url_code = "http://jwgl.cdnu.edu.cn/CheckCode.aspx";
    String url_login = "http://jwgl.cdnu.edu.cn/Default2.aspx";
    String url_info = "http://jwgl.cdnu.edu.cn/xsgrxx.aspx?xh=";
    String refer = "http://jwgl.cdnu.edu.cn/xs_main.aspx?xh=";
    String url_score = "http://jwgl.cdnu.edu.cn/xscj_gc.aspx?xh=";
    String url_ttb = "http://jwgl.cdnu.edu.cn/xskbcx.aspx?xh=";
    String url_exam = "http://jwgl.cdnu.edu.cn/xskscx.aspx?xh=";
    String UploadDateServletAndroid = "http://119.29.98.60:8080/trainAndroid/UploadDateServletAndroid";
    Bitmap bp_code;
    NumberProgressBar npb_jwgl_login;
    int flags = 0;
    MyApp myApp;
    DbManager db;
    OnDismissListener onDismissListener;
    SharedPreferences preferences;
    String admin = "";

    public Jwgl_Dialog(Context context, String jwgl_id, String jwgl_pass,OnDismissListener onDismissListener) {
        super(context);
        this.jwgl_id = jwgl_id;
        this.jwgl_pass = jwgl_pass;
        this.onDismissListener = onDismissListener;
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        admin = preferences.getString("admin", "admin");





    }

    public interface  OnDismissListener{
        void onDismissListener();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jwgl_login);


        init();
        load_code();
        load_code();


    }

    public void init() {


        ett_jwgl_login_stu_id = (EditText) findViewById(R.id.ett_jwgl_login_id);
        ett_jwgl_login_stu_id.setText(jwgl_id);

        ett_jwgl_login_stu_pass = (EditText) findViewById(R.id.ett_jwgl_login_pass);
        ett_jwgl_login_stu_pass.setText(jwgl_pass);

        ett_jwgl_login_stu_code = (EditText) findViewById(R.id.ett_jwgl_login_code);


        ivw_code = (ImageView) findViewById(R.id.ivw_jwgl_login_code);
        ivw_code.setOnClickListener(this);

        jwgl_login_ok = (Button) findViewById(R.id.btn_jwgl_login_ok);
        jwgl_login_ok.setOnClickListener(this);

        jwgl_login_quit = (Button) findViewById(R.id.btn_jwgl_login_quit);
        jwgl_login_quit.setOnClickListener(this);

        npb_jwgl_login = (NumberProgressBar) findViewById(R.id.npb_jwgl);

        myApp = new MyApp();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_jwgl_login_ok:


                code = ett_jwgl_login_stu_code.getText().toString().trim();
                if (code.isEmpty()) {
                    ett_jwgl_login_stu_code.findFocus();
                    ett_jwgl_login_stu_code.setError("请填写验证码");
                } else {
                    to_login();
                }

                break;
            case R.id.btn_jwgl_login_quit:
                dismiss();
                break;
            case R.id.ivw_jwgl_login_code:
                load_code();
                break;
            default:
                break;

        }
    }


    public void get_cookie() {

        RequestParams parms = new RequestParams(url_login);//请求的url
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {

                DbCookieStore instance = DbCookieStore.INSTANCE;
                List<HttpCookie> cookies = instance.getCookies();
                for (HttpCookie cookie : cookies) {
                    Cookie_name = cookie.getName();
                    Cookie_value = cookie.getValue();
                }

                Log.i("cookie", Cookie_name + "=" + Cookie_value);
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


    public void load_code() {
        get_cookie();
        RequestParams parms = new RequestParams(url_code);//请求的url
        parms.addHeader(Cookie_name, Cookie_value);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                bp_code = BitmapFactory.decodeByteArray(result, 0, result.length);
                ivw_code.setImageBitmap(bp_code);
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

    public void to_login() {

        RequestParams parms = new RequestParams(url_login);//请求的url
        parms.addHeader(Cookie_name, Cookie_value);
        parms.addBodyParameter("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz5xN+4mSZJgNeB+dzCD2t+7WLoCAg==");
        parms.addBodyParameter("txtUserName", ett_jwgl_login_stu_id.getText().toString());
        parms.addBodyParameter("Textbox1", ett_jwgl_login_stu_id.getText().toString());
        parms.addBodyParameter("TextBox2", ett_jwgl_login_stu_pass.getText().toString());
        parms.addBodyParameter("txtSecretCode", ett_jwgl_login_stu_code.getText().toString());
        parms.addBodyParameter("RadioButtonList1", "学生");
        parms.addBodyParameter("Button1", "");
        parms.addBodyParameter("lbLanguage", "");
        parms.addBodyParameter("hidPdrs", "");
        parms.addBodyParameter("hidsc", "");

        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {

                    String stuName = Jsoup.parse(new String(result, "gb2312")).getElementById("xhxm").text();
                    name = stuName.substring(0, stuName.length() - 2);
                    Log.i("xhxm", name);
                    // dismiss();

                    npb_jwgl_login.setProgress(10);
                    set_enable(false);
                    get_jwgl_info();

                } catch (Exception e) {
                    if (flags == 0) {
                        Toast.makeText(getContext().getApplicationContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        ett_jwgl_login_stu_code.getText().clear();
                        load_code();
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "学号或者密码错误", Toast.LENGTH_SHORT).show();
                        load_code();
                    }


                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(getContext().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                load_code();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void get_jwgl_info() {
        try {
            name = java.net.URLEncoder.encode(name, "gb2312");
            Log.i("name", name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refer = refer + ett_jwgl_login_stu_id.getText().toString();
        RequestParams parms = new RequestParams(url_info + ett_jwgl_login_stu_id.getText().toString() + "&xm=" + name + "&gnmkdm=N121501");//请求的url
        parms.addHeader("Referer", refer);
        parms.addHeader(Cookie_name, Cookie_value);
        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {

                    npb_jwgl_login.setProgress(20);
                    String out = new String(result, "gb2312");
                    Document doc = Jsoup.parse(out);
                    Elements ele = doc.select("td");

                    db = x.getDb(myApp.daoConfig);
                    db.dropTable(Jwgl_info.class);
                    ArrayList<Jwgl_info> jwgl_info = new ArrayList<>();
                    jwgl_info.add(new Jwgl_info("tip", ele.get(0).text(), ele.get(7).text(), ele.get(19).text(), ele.get(21).text(),
                            ele.get(25).text(), ele.get(31).text(), ele.get(44).text(), ele.get(46).text(), ele.get(53).text(), ele.get(60).text(),
                            ele.get(67).text(), ele.get(73).text(), ele.get(77).text(), ele.get(87).text(), ele.get(89).text(), ele.get(99).text(),
                            ele.get(101).text(), ele.get(103).text(), ele.get(113).text(), ele.get(119).text(), ele.get(125).text()));
                    db.saveOrUpdate(jwgl_info);

                    ArrayList<Jwgl_info> jwgl_infos = new ArrayList<>();
                    jwgl_infos.add(new Jwgl_info("info", ele.get(1).text(), ele.get(8).text(), ele.get(20).text(), ele.get(22).text(),
                            ele.get(26).text(), ele.get(32).text(), ele.get(45).text(), ele.get(47).text(), ele.get(54).text(), ele.get(61).text(),
                            ele.get(68).text(), ele.get(74).text(), ele.get(78).text(), ele.get(88).text(), ele.get(90).text(), ele.get(100).text(),
                            ele.get(102).text(), ele.get(104).text(), ele.get(114).text(), ele.get(120).text(), ele.get(126).text()));
                    db.saveOrUpdate(jwgl_infos);
                    db.close();

                    npb_jwgl_login.setProgress(30);
                    get_jwgl_score();

                } catch (Exception e) {
                    set_enable(true);
                    Toast.makeText(getContext().getApplicationContext(), "解析异常", Toast.LENGTH_SHORT).show();
                    load_code();

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                set_enable(true);
                Toast.makeText(getContext().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                load_code();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void get_jwgl_score() {
        String refer = url_score + ett_jwgl_login_stu_id.getText().toString() + "&xm=" + name + "&gnmkdm=N121605";
        RequestParams parms = new RequestParams(url_score + ett_jwgl_login_stu_id.getText().toString() + "&xm=" + name + "&gnmkdm=N121605");//请求的url
        parms.addHeader("Referer", refer);
        parms.addBodyParameter("__VIEWSTATE", "dDwxODI2NTc3MzMwO3Q8cDxsPHhoOz47bDwxNTAwMTU1MTExMzE7Pj47bDxpPDE+Oz47bDx0PDtsPGk8MT47aTwzPjtpPDU+O2k8Nz47aTw5PjtpPDExPjtpPDEzPjtpPDE2PjtpPDI2PjtpPDI3PjtpPDI4PjtpPDM1PjtpPDM3PjtpPDM5PjtpPDQxPjtpPDQ1Pjs+O2w8dDxwPHA8bDxUZXh0Oz47bDzlrablj7fvvJoxNTAwMTU1MTExMzE7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWnk+WQje+8muW8oOS/ijs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85a2m6Zmi77ya6K6h566X5py656eR5a2m5a2m6ZmiOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzkuJPkuJrvvJo7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOiuoeeul+acuuenkeWtpuS4juaKgOacrzs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w86KGM5pS/54+t77ya6K6h56eR5oqAMTUwMjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8MjAxNTE1NTE7Pj47Pjs7Pjt0PHQ8cDxwPGw8RGF0YVRleHRGaWVsZDtEYXRhVmFsdWVGaWVsZDs+O2w8WE47WE47Pj47Pjt0PGk8Mz47QDxcZTsyMDE2LTIwMTc7MjAxNS0yMDE2Oz47QDxcZTsyMDE2LTIwMTc7MjAxNS0yMDE2Oz4+Oz47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LnByaW50KClcOzs+Pj47Oz47dDxwPDtwPGw8b25jbGljazs+O2w8d2luZG93LmNsb3NlKClcOzs+Pj47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPHQ+Oz4+Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDA+O2k8MT47aTwyPjtpPDQ+Oz47bDx0PDtsPGk8MD47aTwxPjs+O2w8dDw7bDxpPDA+O2k8MT47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PDtsPGk8MD47aTwxPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjtpPDE+Oz47bDx0PDtsPGk8MD47PjtsPHQ8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA+Oz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+O3Q8O2w8aTwwPjs+O2w8dDw7bDxpPDA+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPEpMVTs+Pjs+Ozs+Oz4+Oz4+Oz4+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47PjnrqsON67hfqvj5V9C9FRl3Q8S7");
        parms.addBodyParameter("ddlXN", "");
        parms.addBodyParameter("ddlXQ","");
        parms.addBodyParameter("__VIEWSTATEGENERATOR","DB0F94E3");
        parms.addBodyParameter("Button1", "按学期查询");
        parms.addHeader(Cookie_name, Cookie_value);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {

                try {

                    npb_jwgl_login.setProgress(40);
                   //保存网页

                    Util.delFile("score.html");
                    Util.writeFileSdcard("score.html",result);

                    File input = new File(Environment.getExternalStorageDirectory() + "/How", "score.html");
                    Document doc = Jsoup.parse(input, "gb2312");
                    Elements ele = doc.select("table.datelist").select("td");

                  //  Log.i("score",ele.toString());

                    db = x.getDb(myApp.daoConfig);
                    db.dropTable(Jwgl_score.class);
                    ArrayList<Jwgl_score> jwgl_score = new ArrayList<>();

                    jwgl_score.add(new Jwgl_score("tip", ele.get(0).text(), ele.get(1).text(), ele.get(3).text(), ele.get(4).text(), ele.get(6).text()
                            , ele.get(7).text(), ele.get(8).text(), ele.get(12).text(), ele.get(14).text()));

                    for (int x = 1; x < ele.size() / 16 - 1; x++) {
                        jwgl_score.add(new Jwgl_score("info", ele.get(16 * x + 0).text(), ele.get(16 * x + 1).text(), ele.get(16 * x + 3).text(),
                                ele.get(16 * x + 4).text(), ele.get(16 * x + 6).text(), ele.get(16 * x + 7).text(), ele.get(16 * x + 8).text(),
                                ele.get(16 * x + 12).text(), ele.get(16 * x + 14).text()));
                    }
                    db.saveOrUpdate(jwgl_score);
                    db.close();

                    npb_jwgl_login.setProgress(50);
                    get_jwgl_ttb();

                } catch (Exception e) {
                    set_enable(true);
                 //   Toast.makeText(getContext().getApplicationContext(), "学号或者密码错误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    load_code();

                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                set_enable(true);
                Toast.makeText(getContext().getApplicationContext(), "解析异常", Toast.LENGTH_SHORT).show();
                load_code();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void get_jwgl_ttb() {

        RequestParams parms = new RequestParams(url_ttb + ett_jwgl_login_stu_id.getText().toString() + "&xm=" + name + "&gnmkdm=N121603");//请求的url
        parms.addHeader("Referer", refer);
        parms.addHeader(Cookie_name, Cookie_value);

        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {

                    //保存网页
                    npb_jwgl_login.setProgress(60);

                    Util.delFile("ttb.html");
                    Util.writeFileSdcard("ttb.html",result);

                    File input = new File(Environment.getExternalStorageDirectory() + "/How", "ttb.html");
                    Document doc = Jsoup.parse(input, "gb2312");
                    Elements ele = doc.select("table.blacktab").select("[align=Center]");

                 //   Log.i("ttb",ele.toString());

                    db = x.getDb(myApp.daoConfig);
                    db.dropTable(Jwgl_ttb.class);
                    ArrayList<Jwgl_ttb> jwgl_ttb = new ArrayList<>();
                    jwgl_ttb.add(new Jwgl_ttb("tip",ele.get(0).text(),ele.get(1).text(),ele.get(2).text(),ele.get(3).text(),ele.get(4).text()));

                    for(int x=7;x<ele.size();x++){
                        if(math("周一",ele.get(x).text())){
                            jwgl_ttb.add(new Jwgl_ttb("info",ele.get(x).text(),"","","",""));
                        }
                        if(math("周二",ele.get(x).text())){
                            jwgl_ttb.add(new Jwgl_ttb("info","",ele.get(x).text(),"","",""));
                        }
                        if(math("周三",ele.get(x).text())){
                            jwgl_ttb.add(new Jwgl_ttb("info","","",ele.get(x).text(),"",""));
                        }
                        if(math("周四",ele.get(x).text())){
                            jwgl_ttb.add(new Jwgl_ttb("info","","","",ele.get(x).text(),""));
                        }
                        if(math("周五",ele.get(x).text())){
                            jwgl_ttb.add(new Jwgl_ttb("info","","","","",ele.get(x).text()));
                        }
                    }

                    db.saveOrUpdate(jwgl_ttb);
                    db.close();




                    npb_jwgl_login.setProgress(70);
                    get_jwgl_exam();
                } catch (Exception e) {
                    set_enable(true);
                    Toast.makeText(getContext().getApplicationContext(), "解析异常", Toast.LENGTH_SHORT).show();
                    load_code();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                set_enable(true);
                Toast.makeText(getContext().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void get_jwgl_exam() {
        RequestParams parms = new RequestParams(url_exam + ett_jwgl_login_stu_id.getText().toString() + "&xm=" + name + "&gnmkdm=N121604");//请求的url
        parms.addHeader("Referer", refer);
        parms.addHeader(Cookie_name, Cookie_value);
        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {

                    npb_jwgl_login.setProgress(80);

                    String out = new String(result, "gb2312");
                    Document doc = Jsoup.parse(out);
                    Elements ele = doc.select("td");

                    db = x.getDb(myApp.daoConfig);
                    db.dropTable(Jwgl_exam.class);
                    ArrayList<Jwgl_exam> jwgl_exam = new ArrayList<>();
                    jwgl_exam.add(new Jwgl_exam("tip", ele.get(1).text(), ele.get(3).text(), ele.get(4).text(),
                            ele.get(5).text(), ele.get(6).text()));


                    for (int x = 1; x < ele.size() / 8 - 1; x++) {
                        jwgl_exam.add(new Jwgl_exam("info", ele.get(8 * x + 1).text(), ele.get(8 * x + 3).text(), ele.get(8 * x + 4).text(),
                                ele.get(8 * x + 5).text(), ele.get(8 * x + 6).text()));
                    }
                    db.saveOrUpdate(jwgl_exam);
                    db.close();
                    npb_jwgl_login.setProgress(90);
                    uploadFile();


                } catch (Exception e) {
                    set_enable(true);
                    Toast.makeText(getContext().getApplicationContext(), "解析异常", Toast.LENGTH_SHORT).show();
                    load_code();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                set_enable(true);
                Toast.makeText(getContext().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    public void set_enable(boolean b) {

        ett_jwgl_login_stu_id.setEnabled(b);
        ett_jwgl_login_stu_pass.setEnabled(b);
        ett_jwgl_login_stu_code.setEnabled(b);
        jwgl_login_ok.setEnabled(b);
        ivw_code.setEnabled(b);
        jwgl_login_quit.setEnabled(b);
    }


    public boolean math(String week,String str){

        Pattern p= Pattern.compile(week);
        Matcher m=p.matcher(str);
        return m.find();

    }

    public void uploadFile() throws Exception {
        try {
            final String tempPath = Environment.getExternalStorageDirectory().getPath() + "/How/train.db";
            File file = new File(tempPath);
            if (file.exists() && file.length() > 0) {
                AsyncHttpClient client = new AsyncHttpClient();
                com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
                params.put("admin", admin);
                params.put("uploadfile", file);
                client.setTimeout(20000);
                // 上传文件
                client.post(UploadDateServletAndroid, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        npb_jwgl_login.setProgress(98);
                        dismiss();
                        onDismissListener.onDismissListener();
                        Toast.makeText(getContext().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // 上传失败后要做到工作
                    }
                    @Override
                    public void onRetry(int retryNo) {
                        super.onRetry(retryNo);
                        // 返回重试次数
                    }
                });
            } else {
            }
        } catch (Exception e) {
            set_enable(true);
            Toast.makeText(getContext().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
        }


    }




    @Override
    public void onBackPressed() {
        dismiss();
    }

}