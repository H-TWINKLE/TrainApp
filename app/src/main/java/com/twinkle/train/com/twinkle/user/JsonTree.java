package com.twinkle.train.com.twinkle.user;

import java.io.Serializable;


public class JsonTree implements Serializable {


    private String admin;
    private String time;
    private String information;


    public JsonTree() {
    }

    public JsonTree(String admin, String information, String time) {
        this.admin = admin;
        this.information = information;
        this.time = time;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
