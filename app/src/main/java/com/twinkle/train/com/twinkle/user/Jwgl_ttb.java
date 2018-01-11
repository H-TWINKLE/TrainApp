package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/10/30.
 */

@Table(name = "jwgl_ttb")
public class Jwgl_ttb {

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "type")           //类别
    private String type;

    @Column(name = "zhouyi")
    private String zhouyi;

    @Column(name = "zhouer")
    private String zhouer;

    @Column(name = "zhousan")
    private String zhousan;

    @Column(name = "zhousi")
    private String zhousi;

    @Column(name = "zhouwu")
    private String zhouwu;


    public Jwgl_ttb(String type, String zhouyi, String zhouer, String zhousan, String zhousi, String zhouwu) {

        this.type = type;
        this.zhouyi = zhouyi;
        this.zhouer = zhouer;
        this.zhousan = zhousan;
        this.zhousi = zhousi;
        this.zhouwu = zhouwu;

    }


    public Jwgl_ttb() {
    }

    @Override
    public String toString() {
        return zhouyi +" "+zhouer +" "+zhousan+" "+zhousi+" "+zhouwu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZhouyi() {
        return zhouyi;
    }

    public void setZhouyi(String zhouyi) {
        this.zhouyi = zhouyi;
    }

    public String getZhouer() {
        return zhouer;
    }

    public void setZhouer(String zhouer) {
        this.zhouer = zhouer;
    }

    public String getZhousan() {
        return zhousan;
    }

    public void setZhousan(String zhousan) {
        this.zhousan = zhousan;
    }

    public String getZhousi() {
        return zhousi;
    }

    public void setZhousi(String zhousi) {
        this.zhousi = zhousi;
    }

    public String getZhouwu() {
        return zhouwu;
    }

    public void setZhouwu(String zhouwu) {
        this.zhouwu = zhouwu;
    }
}
