package com.gy.ticket.java;

import android.content.Context;
import android.content.SharedPreferences;

import com.gy.ticket.user.User;

import java.util.ArrayList;


public class SharePerfence {

    private Context context;
    private String xml_name;
    private SharedPreferences sp;

    public SharePerfence(String xml_name, Context context) {
        this.xml_name = xml_name;
        this.context = context;
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);

    }

    public void setShareperfence(User user) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("date", user.getDate());
        editor.putString("email", user.getEmail());
        editor.putInt("emailcheck", user.getEmailcheck());
        editor.putString("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("pass", user.getPass());
        editor.putString("tel", user.getTel());
        editor.putString("idcard", user.getIdcard());
        editor.apply();
    }

    public boolean getShareperfence_login() {
        boolean flag = true;
        if ("-1".equals(sp.getString("id", "-1"))) {
            flag = false;
        }
        return flag;

    }

    public void remove_all() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

    }

    public ArrayList<String> get_info() {
        ArrayList<String> list = new ArrayList<>();
        list.add(sp.getString("email", ""));
        list.add(sp.getString("tel", ""));
        list.add(sp.getString("name", ""));
        list.add(sp.getString("idcard", ""));
        return list;
    }

}
