package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/10/30.
 */

@Table(name = "jwgl_score")
public class Jwgl_score {

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "type")           //类别
    private String type;

    @Column(name = "xuenian")    //0
    private String xuenian;

    @Column(name = "xueqi") //1
    private String xueqi;

    @Column(name = "mingcheng")  //3
    private String mingcheng;

    @Column(name = "xingzhi") //4
    private String xingzhi;

    @Column(name = "xuefen")//6
    private String xuefen;

    @Column(name = "jidian") //7
    private String jidian;

    @Column(name = "chengji") //8
    private String chengji;

    @Column(name = "xueyuan")//12
    private String xueyuan;

    @Column(name = "chongxiu")  //14
    private String chongxiu;

    public Jwgl_score(String type, String xuenian, String xueqi, String mingcheng, String xingzhi, String xuefen, String jidian, String chengji, String xueyuan, String chongxiu) {
        this.type = type;
        this.xuenian = xuenian;
        this.xueqi = xueqi;
        this.mingcheng = mingcheng;
        this.xingzhi = xingzhi;
        this.xuefen = xuefen;
        this.jidian = jidian;
        this.chengji = chengji;
        this.xueyuan = xueyuan;
        this.chongxiu = chongxiu;
    }

    public Jwgl_score() {
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

    public String getXuenian() {
        return xuenian;
    }

    public void setXuenian(String xuenian) {
        this.xuenian = xuenian;
    }

    public String getXueqi() {
        return xueqi;
    }

    public void setXueqi(String xueqi) {
        this.xueqi = xueqi;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getXingzhi() {
        return xingzhi;
    }

    public void setXingzhi(String xingzhi) {
        this.xingzhi = xingzhi;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getJidian() {
        return jidian;
    }

    public void setJidian(String jidian) {
        this.jidian = jidian;
    }

    public String getChengji() {
        return chengji;
    }

    public void setChengji(String chengji) {
        this.chengji = chengji;
    }

    public String getXueyuan() {
        return xueyuan;
    }

    public void setXueyuan(String xueyuan) {
        this.xueyuan = xueyuan;
    }

    public String getChongxiu() {
        return chongxiu;
    }

    public void setChongxiu(String chongxiu) {
        this.chongxiu = chongxiu;
    }
}
