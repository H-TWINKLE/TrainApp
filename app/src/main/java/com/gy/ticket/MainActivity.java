package com.gy.ticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gy.ticket.java.InitString;
import com.gy.ticket.java.SharePerfence;
import com.gy.ticket.user.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_main_account, et_main_password;
    private Button bt_main_login;
    private TextView tv_main_register,tv_main_getpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        apply();
        get_admin();

    }

    private void init() {
        et_main_account = (EditText) findViewById(R.id.et_main_account);
        et_main_password = (EditText) findViewById(R.id.et_main_password);
        bt_main_login = (Button) findViewById(R.id.bt_main_login);
        tv_main_register = (TextView) findViewById(R.id.tv_main_register);
        tv_main_getpass = (TextView) findViewById(R.id.tv_main_getpass);
    }

    private void apply() {
        bt_main_login.setOnClickListener(this);
        tv_main_register.setOnClickListener(this);

    }

    private void get_admin() {
        if (new SharePerfence("account", MainActivity.this).getShareperfence_login()) {
            startActivity(new Intent(MainActivity.this, WelActivity.class));
            finish();
        }
    }


    private void check() {

        String account = et_main_account.getText().toString().trim();
        String pass = et_main_password.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            et_main_account.setError(getString(R.string.account_tip));
            et_main_account.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            et_main_password.setError(getString(R.string.password_tip));
            et_main_password.requestFocus();
        } else {
            login(account, pass);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_main_login:
                check();
                break;
            case R.id.tv_main_register:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_main_getpass:
                startActivity(new Intent(MainActivity.this, PassActivity.class));
                break;
            default:
                break;
        }
    }


    private void login(String account, String pass) {

        RequestParams requestParams = new RequestParams(InitString.login_url);
        requestParams.addBodyParameter("admin", account);
        requestParams.addBodyParameter("pass", pass);
        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {

                try {
                    String text = new String(result, "utf-8").trim().replaceAll("\\s*", "");
                    Log.i("result", text);

                    if (text.equals("2")) {
                        Toast.makeText(MainActivity.this, getString(R.string.account_error_tip), Toast.LENGTH_SHORT).show();
                    } else if (text.equals("3")) {

                        Toast.makeText(MainActivity.this, getString(R.string.account_inside_error), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.account_right), Toast.LENGTH_SHORT).show();

                        List<User> list = new ArrayList<>(JSON.parseArray(text, User.class));
                        new SharePerfence("account", MainActivity.this).setShareperfence(list.get(0));
                        startActivity(new Intent(MainActivity.this, WelActivity.class));
                        finish();


                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
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
