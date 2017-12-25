package com.gy.ticket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gy.ticket.R;
import com.gy.ticket.java.InitString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TWINKLE on 2017/12/20.
 */

public class FunctionAdapter extends BaseAdapter {

    private Context context;

    public FunctionAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        return InitString.pic.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.content_lv_fun, null);
        ImageView iv_content_fun = (ImageView) view.findViewById(R.id.iv_content_pic);
        TextView tv_content_title = (TextView) view.findViewById(R.id.tv_content_title);
        iv_content_fun.setImageResource(InitString.pic[position]);
        tv_content_title.setText(InitString.title[position]);

        return view;

    }


}
