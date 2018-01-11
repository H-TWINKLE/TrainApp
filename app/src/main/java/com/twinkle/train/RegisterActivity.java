package com.twinkle.train;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView ivwretrue, ivwrefsh;
    LinearLayout view;
    EditText ettrepass2, ettrepass1, ettrename, ettreemal;
    String url = "http://119.29.98.60:8080/trainAndroid/RegisterServlet";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }


    private void init() {

        ivwretrue = (ImageView) findViewById(R.id.ivw_re_true);
        ivwretrue.setOnClickListener(this);
        ivwrefsh = (ImageView) findViewById(R.id.ivw_re_fsh);
        ivwrefsh.setOnClickListener(this);
        ettrepass2 = (EditText) findViewById(R.id.ett_re_pass2);
        ettrepass1 = (EditText) findViewById(R.id.ett_re_pass1);
        ettrename = (EditText) findViewById(R.id.ett_re_name);
        ettreemal = (EditText) findViewById(R.id.ett_re_emal);
        view = (LinearLayout) findViewById(R.id.view_register);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivw_re_fsh:
                finish();
                break;
            case R.id.ivw_re_true:
                check();
                break;
            default:
                break;

        }

    }

    private void check() {

        ettreemal.setError(null);
        ettrename.setError(null);
        ettrepass1.setError(null);
        ettrepass2.setError(null);

        String admin = ettreemal.getText().toString();
        String name = ettrename.getText().toString();
        String pass1 = ettrepass1.getText().toString();
        String pass2 = ettrepass2.getText().toString();


        if (TextUtils.isEmpty(admin)) {
            ettreemal.setError(getString(R.string.error_field_required));
            ettreemal.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            ettrename.setError(getString(R.string.error_field_required));
            ettrename.requestFocus();
        } else if (TextUtils.isEmpty(pass1)) {
            ettrepass1.setError(getString(R.string.error_field_required));
            ettrepass1.requestFocus();
        } else if (TextUtils.isEmpty(pass2)) {
            ettrepass2.setError(getString(R.string.error_field_required));
            ettrepass2.requestFocus();
        } else if (!pass1.equals(pass2)) {
            ettrepass2.setError(getString(R.string.renew_new));
            ettrepass2.requestFocus();
        } else if (!isPasswordValid(pass1) || !isPasswordValid(pass2)) {
            ettrepass2.setError(getString(R.string.error_invalid_password));
            ettrepass2.requestFocus();
        } else if (!isEmailValid(admin)) {
            ettreemal.setError(getString(R.string.error_invalid_email));
            ettreemal.requestFocus();
        } else {
            register(admin, pass2, name);
        }

    }

    private void register(final String admin, final String pass, final String name) {

        RequestParams parms = new RequestParams(url);//请求的url
        parms.addBodyParameter("admin", admin);
        parms.addBodyParameter("password", pass);
        parms.addBodyParameter("name", name);
        x.http().post(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {


                try {
                    String result = new String(bytes, "utf-8").replaceAll("\\s*", "");
                    if ("1".equals(result)) {
                        setpreferences_new(admin, pass, name);
                        startActivity(new Intent(RegisterActivity.this, MainNActivity.class));
                        finish();
                    } else if ("2".equals(result)) {
                        Snackbar.make(view, getString(R.string.register_2), Snackbar.LENGTH_SHORT).show();
                    } else if ("0".equals(result)) {
                        Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Snackbar.make(view, getString(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Snackbar.make(view, getString(R.string.register_2), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    public void setpreferences_new(String admin, String pass, String name) {

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("admin", admin);
        editor.putString("password", pass);
        editor.putString("name", name);
        editor.apply();
    }


}
