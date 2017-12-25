package com.gy.ticket;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gy.ticket.java.InitString;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText te_name, te_pass, te_idcard, te_tel, te_email;
    Button bt_register_tel, bt_register_email;
    ImageView iv_register_ok;
    Toolbar tl_register;
    int flag_code = 0, flag_bt = 0;

    LinearLayout ll_register;
    EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        apply();
    }


    private void init() {

        tl_register = (Toolbar) findViewById(R.id.tl_register);
        te_name = (TextInputEditText) findViewById(R.id.te_name);
        te_pass = (TextInputEditText) findViewById(R.id.te_pass);
        te_idcard = (TextInputEditText) findViewById(R.id.te_idcard);
        te_tel = (TextInputEditText) findViewById(R.id.te_tel);
        te_email = (TextInputEditText) findViewById(R.id.te_email);
        bt_register_tel = (Button) findViewById(R.id.bt_register_tel);
        bt_register_email = (Button) findViewById(R.id.bt_register_email);
        iv_register_ok = (ImageView) findViewById(R.id.iv_register_ok);
        ll_register = (LinearLayout) findViewById(R.id.ll_register_tel);
        et_code = (EditText) findViewById(R.id.et_register_code);


    }

    public void apply() {
        tl_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        te_tel.addTextChangedListener(new TextChange(1));
        te_email.addTextChangedListener(new TextChange(2));
        iv_register_ok.setOnClickListener(this);
        bt_register_email.setOnClickListener(this);
        bt_register_tel.setOnClickListener(this);
        et_code.addTextChangedListener(new TextChange(3));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_email:
                email_active(te_email.getText().toString().trim());
                break;
            case R.id.bt_register_tel:
                bt_sms();
                break;
            case R.id.iv_register_ok:
                check();
                break;
            default:
                break;
        }
    }

    public class TextChange implements TextWatcher {
        int flag;

        private TextChange(int flag) {
            this.flag = flag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            switch (flag) {
                case 1:
                    if (te_tel.getText().toString().length() != 0) {
                        ll_register.setVisibility(View.VISIBLE);
                    } else {
                        ll_register.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if (te_email.getText().toString().length() != 0) {
                        bt_register_email.setVisibility(View.VISIBLE);
                    } else {
                        bt_register_email.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    if (et_code.getText().toString().length() != 0) {
                        bt_register_tel.setText(getString(R.string.check));
                        flag_code = 10;
                    } else {
                        flag_code = 11;
                    }
                default:
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void check() {
        String name = te_name.getText().toString().trim();
        String pass = te_pass.getText().toString().trim();
        String idcard = te_idcard.getText().toString().trim();
        String tel = te_tel.getText().toString().trim();
        String email = te_email.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            te_name.setError(getString(R.string.warn_input));
            te_name.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            te_pass.setError(getString(R.string.warn_input));
            te_pass.requestFocus();
        } else if (TextUtils.isEmpty(idcard)) {
            te_idcard.setError(getString(R.string.warn_input));
            te_idcard.requestFocus();
        } else if (TextUtils.isEmpty(tel)) {
            te_tel.setError(getString(R.string.warn_input));
            te_tel.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            te_email.setError(getString(R.string.warn_input));
            te_email.requestFocus();
        } else {
            if (flag_code == 1) {
                register(name, pass, idcard, tel, email);
            }else {
                toast(getString(R.string.plaese_check_tel));
            }

        }
    }

    private void register(String name, String pass, String idcard, String tel, String email) {

        RequestParams requestParams = new RequestParams(InitString.login_register);
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("pass", pass);
        requestParams.addBodyParameter("email", email);
        requestParams.addBodyParameter("tel", tel);
        requestParams.addBodyParameter("idcard", idcard);
        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                try {
                    String text = new String(result, "utf-8").trim().replaceAll("\\s*", "");
                    Log.i("result", text);

                    switch (text) {
                        case "2":
                            toast(getString(R.string.register_error));
                            break;
                        case "3":
                            toast(getString(R.string.account_inside_error));
                            ;
                            break;
                        case "1":
                            toast(getString(R.string.register_ok));
                            finish();
                            break;
                        default:
                            toast(getString(R.string.internet_error));
                            break;
                    }

                } catch (Exception e) {
                    toast(getString(R.string.internet_error));

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                toast(getString(R.string.internet_error));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void email_active(String email) {
        RequestParams requestParams = new RequestParams(InitString.login_emailcheck);
        requestParams.addBodyParameter("admin", email);
        requestParams.addBodyParameter("check", "2");
        x.http().post(requestParams, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                bt_register_email.setText(getString(R.string.email_check_send));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toast(getString(R.string.internet_error));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                Log.i("reslut", result + "   " + data);
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    ct.cancel();
                    toast(getString(R.string.register_ok));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bt_register_tel.setText(getString(R.string.register_ok));
                        }
                    });
                    flag_code = 1;
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功
                    toast(getString(R.string.code_get_ok));
                }
            } else {//错误等在这里（包括验证失败）
                ((Throwable) data).printStackTrace();
                try{
                   // data = "["+data.toString()+"]";
                    toast(JSONObject.parseObject(data.toString().replace("java.lang.Throwable: ","")).get("detail").toString());
                }catch (Exception e){
                    e.printStackTrace();
                    toast(getString(R.string.internet_error));
                }

            }
        }
    };


    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);// 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    }

    @Override
    protected void onStop() {
        super.onStop();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);// 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
    }

    @Override
    protected void onStart() {
        super.onStart();
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）

    }

    private CountDownTimer ct = new CountDownTimer(1000 * 60, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (flag_code != 10) {
                bt_register_tel.setText((int) millisUntilFinished / 1000 + getString(R.string.s));
                bt_register_tel.setEnabled(false);
            } else {
                bt_register_tel.setEnabled(true);
            }

        }

        @Override
        public void onFinish() {
            bt_register_tel.setText(getString(R.string.check));
            bt_register_tel.setEnabled(true);
            flag_bt = 0;
        }
    };


    private void bt_sms() {
        switch (flag_bt) {
            case 0:
                flag_bt = 1;
                SMSSDK.getVerificationCode("86", te_tel.getText().toString().trim());
                ct.start();
                break;
            case 1:
                SMSSDK.submitVerificationCode("86", te_tel.getText().toString().trim(), et_code.getText().toString().trim());
                Log.i("sdk", te_tel.getText().toString().trim() + "   " + et_code.getText().toString().trim());
                break;
            default:
                break;
        }
    }


}
