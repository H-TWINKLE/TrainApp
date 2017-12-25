package com.gy.ticket.user;

/**
 * Created by TWINKLE on 2017/12/19.
 */

public class User {


    /**
     * date : 2017-12-12
     * email : zhanghjqqqq@163.com          1
     * emailcheck : 1
     * id : 1
     * idcard : 511123199505060650                   4
     * name : 张俊                              3
     * pass : 123456
     * tel : 13111856908                     2
     */

    private String date;
    private String email;
    private int emailcheck;
    private String id;
    private String idcard;
    private String name;
    private String pass;
    private String tel;


    public User(String date, String email, int emailcheck, String id, String idcard, String name, String pass, String tel) {
        this.date = date;
        this.email = email;
        this.emailcheck = emailcheck;
        this.id = id;
        this.idcard = idcard;
        this.name = name;
        this.pass = pass;
        this.tel = tel;
    }

    public User() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailcheck() {
        return emailcheck;
    }

    public void setEmailcheck(int emailcheck) {
        this.emailcheck = emailcheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
