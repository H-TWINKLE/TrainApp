package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/10/28.
 */

@Table(name = "eol")
public class Eol_info {

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "admin")
    private String admin;

    @Column(name = "stu_id")
    private String stu_id;

    @Column(name = "eol_pass")
    private String eol_pass;

    @Column(name = "notice")
    private String notice;



    public Eol_info(String admin, String stu_id, String eol_pass, String notice) {
        this.admin = admin;
        this.stu_id = stu_id;
        this.eol_pass = eol_pass;
        this.notice = notice;
    }




    public Eol_info() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getEol_pass() {
        return eol_pass;
    }

    public void setEol_pass(String eol_pass) {
        this.eol_pass = eol_pass;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
