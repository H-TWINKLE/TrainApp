package com.gy.ticket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.image.SmartImageView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener {
    private Button dummy_button;
    private CountDownTimer countDownTimer;
    private SmartImageView ivw_first;
    private String pic_url = "http://119.29.98.60:8080/Cs/PicServlet";
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        init();
        apply();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();

    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.postDelayed(mHidePart2Runnable, 200);

    }


    public void init() {
        dummy_button = (Button) findViewById(R.id.dummy_button);
        dummy_button.setOnClickListener(this);

        ivw_first = (SmartImageView) findViewById(R.id.ivw_first);
        ivw_first.destroyDrawingCache();
        ivw_first.setImageUrl(pic_url);


    }

    private void apply() {
        //  get_pic();
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

                dummy_button.setText(String.format(getResources().getString(R.string.times), l / 1000));
            }

            @Override
            public void onFinish() {

                startActivity(new Intent(FullscreenActivity.this, MainActivity.class));
                finish();
            }
        };
        countDownTimer.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dummy_button:
                startActivity(new Intent(FullscreenActivity.this, MainActivity.class));
                countDownTimer.cancel();
                finish();
                break;
            default:
                break;
        }
    }


   /* public void get_pic() {
        String urls = pic_url;
        RequestParams parms = new RequestParams(urls);//请求的url

        x.http().get(parms, new Callback.CommonCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String result = new String(bytes, "UTF-8");
                    ivw_first.setImageUrl(result);

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
        });*/


}
