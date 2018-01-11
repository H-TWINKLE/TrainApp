package com.twinkle.train;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twinkle.train.com.twinkle.user.JsonAdmin;
import com.twinkle.train.com.twinkle.java.Util;

import org.apache.http.Header;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button mEmailSignInButton;
    TextView tvw_login_fsh, tvw_login_fps, tvw_login_err, tvw_login_re;
    LinearLayout view;


    String LoginServletAndroid = "http://119.29.98.60:8080/trainAndroid/LoginServletAndroid";
    String result = "", true_admin = "";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        init();
        mEmailSignInButton.setOnClickListener(new ButtonListener());
        tvw_login_fps.setOnClickListener(new ButtonListener());
        tvw_login_fsh.setOnClickListener(new ButtonListener());
        tvw_login_re.setOnClickListener(new ButtonListener());

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;

        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            setTitle(R.string.attempt_to_login);
            AttemptToLogin(email, password);

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email_sign_in_button:
                    attemptLogin();
                    break;
                case R.id.tvw_login_fsh:
                    finish();
                    break;
                case R.id.tvw_login_fps:
                    Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tvw_login_re:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    break;
                default:
                    break;
            }
        }
    }

    public void init() {
        view = (LinearLayout) findViewById(R.id.view_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        tvw_login_fps = (TextView) findViewById(R.id.tvw_login_fps);
        tvw_login_fsh = (TextView) findViewById(R.id.tvw_login_fsh);
        tvw_login_err = (TextView) findViewById(R.id.tvw_login_err);
        tvw_login_re = (TextView) findViewById(R.id.tvw_login_re);
    }


    public void AttemptToLogin(final String admin, final String pass) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("admin", admin);
        params.put("password", pass);
        asyncHttpClient.setTimeout(10000);
        //url:   parmas：请求时携带的参数信息   responseHandler：是一个匿名内部类接受成功过失败
        asyncHttpClient.post(LoginServletAndroid, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //statusCode:状态码    headers：头信息  responseBody：返回的内容，返回的实体
                //获取结果
                if (statusCode == 200) {
                    try {
                        result = new String(responseBody, "utf-8").replaceAll("\\s*", "");
                        Log.i("输出登录结果：", "" + result);
                        if ("0".equals(result)) {
                            showProgress(false);
                            Snackbar.make(view, "邮箱或者密码错误", Snackbar.LENGTH_SHORT).show();
                            tvw_login_err.setText("邮箱或者密码错误");
                        } else if ("2".equals(result)) {
                            showProgress(false);
                            Snackbar.make(view, "该用户尚未注册", Snackbar.LENGTH_SHORT).show();

                        } else {
                            if (setpreferences_old(result)) {
                                Toast.makeText(LoginActivity.this, "欢迎用户:" + admin, Toast.LENGTH_SHORT).show();
                                true_admin = admin;
                                new Util().downloadFile(true_admin, 1, LoginActivity.this);
                            } else {
                                network_warn();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showProgress(false);
                    Snackbar.make(view, getText(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                network_warn();
            }
        });
    }

    public boolean setpreferences_old(String result) {
        boolean flag;
        try {
            JSONObject object = JSON.parseObject(result);

            JsonAdmin jsonAdmin = new JsonAdmin();                 //处理信息
            jsonAdmin.setAdmin(object.getString("admin"));
            jsonAdmin.setPassword(object.getString("password"));
            jsonAdmin.setName(object.getString("name"));
            jsonAdmin.setSex(object.getString("sex"));
            jsonAdmin.setAge(object.getString("age"));
            jsonAdmin.setSay(object.getString("say"));
            jsonAdmin.setLocal(object.getString("local"));
            jsonAdmin.setLevel(object.getString("level"));

            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("admin", jsonAdmin.getAdmin());
            editor.putString("password", jsonAdmin.getPassword());
            editor.putString("name", jsonAdmin.getName());
            editor.putString("say", jsonAdmin.getSay());
            editor.putString("level", jsonAdmin.getLevel());
            editor.putString("sex", jsonAdmin.getSex());
            editor.putString("local", jsonAdmin.getLocal());
            editor.putString("age", jsonAdmin.getAge());
            editor.apply();
            flag = true;

        } catch (Exception e) {
            network_warn();
            flag = false;
        }
        return flag;
    }

    private void network_warn() {
        try {
            showProgress(false);
            Snackbar.make(view, getText(R.string.permission_rationale), Snackbar.LENGTH_SHORT).show();
            result = "网络异常";
            Log.i("输出登录结果：", "" + result);
            tvw_login_err.setText(getText(R.string.permission_rationale));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}