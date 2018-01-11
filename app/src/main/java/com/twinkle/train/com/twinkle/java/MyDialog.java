package com.twinkle.train.com.twinkle.java;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.twinkle.train.R;

/**
 * Created by TWINKLE on 2017/9/18.
 */

public class MyDialog extends Dialog {

    EditText editText;
    Button layout_ok,layout_quit;
    String info;
    OnEditInputFinishedListener mListener;


    public MyDialog(Context context,String info,OnEditInputFinishedListener mListener){

        super(context);
        this.info = info;
        this.mListener = mListener;


    }

    public interface OnEditInputFinishedListener{
        void editInputFinished(String password);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_revise_info);
        setTitle("请输入信息");
        setCancelable(false);

        editText = (EditText) findViewById(R.id.lyt_revise_info_ptt);
        editText.setText(info);
        layout_ok = (Button)findViewById(R.id.btn_layout_ok);
        layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    info = editText.getText().toString();
                    mListener.editInputFinished(info);
                    dismiss();
            }
        });
        layout_quit = (Button)findViewById(R.id.btn_layout_quit);
        layout_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }
}
