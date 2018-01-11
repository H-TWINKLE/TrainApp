package com.twinkle.train;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class ForgetPassActivity extends AppCompatActivity {


    AutoCompleteTextView atv_fps_email, atv_fps_code;
    Button btm_fps_ok;
    TextView tvw_fps_returnmain;
    int flag = 0;
    static String UpgradePassServletAndroid = "http://119.29.98.60:8080/trainAndroid/UpgradePassServletAndroid";
    static String CheckCodeServletAndroid = "http://119.29.98.60:8080/trainAndroid/CheckCodeServletAndroid";
    static String UpgradeNEWpassServletAndroid = "http://119.29.98.60:8080/trainAndroid/UpgradeNEWpassServletAndroid";
    static String result;
    static String true_admin;
    View focusView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        init();
        btm_fps_ok.setOnClickListener(new ButtonListener());
        tvw_fps_returnmain.setOnClickListener(new ButtonListener());

    }

    public void init() {

        atv_fps_email = (AutoCompleteTextView) findViewById(R.id.acvw_fps_email);
        atv_fps_code = (AutoCompleteTextView) findViewById(R.id.acvw_fps_code);
        atv_fps_code.setVisibility(View.GONE);
        btm_fps_ok = (Button) findViewById(R.id.btn_fps_ok);
        tvw_fps_returnmain = (TextView) findViewById(R.id.tvw_fps_retunmain);


    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_fps_ok:
                    attempt();
                    break;
                case R.id.tvw_fps_retunmain:
                    Intent intent = new Intent(ForgetPassActivity.this, MainNActivity.class);
                    startActivity(intent);
                    finish();

            }
        }
    }


    private void attempt() {

        if (flag == 0) {
            String email = atv_fps_email.getText().toString();
            //  View focusView = null;
            boolean cancel = false;

            atv_fps_email.setError(null);
            atv_fps_code.setError(null);


            if (TextUtils.isEmpty(email)) {
                atv_fps_email.setError(getString(R.string.error_field_required));
                focusView = atv_fps_email;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            } else {
                Toast.makeText(ForgetPassActivity.this, getText(R.string.sending_code), Toast.LENGTH_SHORT).show();
                checkadmin(email);
            }
        } else if (flag == 1) {
            code();
        } else if (flag == 2) {
            src();
        }
    }


    public void code() {


        String new_src = atv_fps_email.getText().toString();
        String renew_src = atv_fps_code.getText().toString();

        View focusView = null;
        boolean cancel = false;

        atv_fps_email.setError(null);
        atv_fps_code.setError(null);

        if (TextUtils.isEmpty(new_src)) {
            atv_fps_email.setError(getString(R.string.error_field_required));
            focusView = atv_fps_email;
            cancel = true;
        }
        if (TextUtils.isEmpty(renew_src)) {
            atv_fps_code.setError(getString(R.string.error_field_required));
            focusView = atv_fps_code;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            checkcode(new_src, renew_src);
        }
    }

    public void src() {
        String new_src = atv_fps_email.getText().toString();
        String renew_src = atv_fps_code.getText().toString();

        boolean cancel = false;
        atv_fps_email.setError(null);
        atv_fps_code.setError(null);

        if (TextUtils.isEmpty(new_src)) {
            atv_fps_email.setError(getString(R.string.error_field_required));
            focusView = atv_fps_email;
            cancel = true;
        }
        if (TextUtils.isEmpty(renew_src)) {
            atv_fps_code.setError(getString(R.string.error_field_required));
            focusView = atv_fps_code;
            cancel = true;
        }

        if (!new_src.equals(renew_src)) {
            atv_fps_code.setError(getString(R.string.renew_new));
            focusView = atv_fps_code;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            Toast.makeText(ForgetPassActivity.this, getText(R.string.check_src), Toast.LENGTH_SHORT).show();
            creat_pass(true_admin, renew_src);
        }
    }

    public void checkadmin(final String admin) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.post(UpgradePassServletAndroid, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {
                    try {
                        result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                        System.out.println("输出验证邮箱结果：" + result);
                        if ("0".equals(result)) {
                            atv_fps_email.setError(getString(R.string.no_admin));
                            focusView = atv_fps_email;
                        } else if ("1".equals(result)) {
                            flag = 1;
                            true_admin = admin;
                            btm_fps_ok.setText(getText(R.string.ok_code));
                            atv_fps_code.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void checkcode(String admin, String code) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        params.put("code", code);
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.post(CheckCodeServletAndroid, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {

                    try {
                        result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                        System.out.println("输出验证验证码结果：" + result);
                        if ("0".equals(result)) {
                            atv_fps_code.setError(getString(R.string.code_isfalse));
                            focusView = atv_fps_code;
                        } else if ("1".equals(result)) {
                            atv_fps_email.setHint(getText(R.string.new_scr));
                            atv_fps_email.getText().clear();
                            atv_fps_code.setHint(getText(R.string.old_scr));
                            atv_fps_code.getText().clear();
                            Toast.makeText(ForgetPassActivity.this, getText(R.string.ok_src), Toast.LENGTH_SHORT).show();
                            btm_fps_ok.setText(getText(R.string.ok_src));
                            flag = 2;
                        } else {
                            Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void creat_pass(String admin, String code) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        params.put("pass", code);
        asyncHttpClient.setTimeout(10000);
        asyncHttpClient.post(UpgradeNEWpassServletAndroid, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) {

                    try {
                        result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                        System.out.println("输出修改结果：" + result);
                        if ("1".equals(result)) {
                            Toast.makeText(ForgetPassActivity.this, getText(R.string.new_pass_is_right), Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgetPassActivity.this, getText(R.string.new_pass_is_flase), Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(ForgetPassActivity.this, ForgetPassActivity.class);
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ForgetPassActivity.this, getText(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
