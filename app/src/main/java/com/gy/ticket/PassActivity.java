package com.gy.ticket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PassActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar tl_pass;
    Button bt_pass;
    EditText et_account, et_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        init();
        apply();
    }

    private void init() {

        tl_pass = (Toolbar) findViewById(R.id.tl_pass);
        bt_pass = (Button) findViewById(R.id.bt_pass);
        et_account = (EditText) findViewById(R.id.et_pass_account);
        et_code = (EditText) findViewById(R.id.et_pass_code);

    }

    private void apply() {
        tl_pass.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_pass.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_pass:
                check();
                break;
            default:
                break;
        }
    }

    private void check() {

    }
}
