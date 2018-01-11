package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/10/30.
 */

@Table(name = "jwgl_exam")
public class Jwgl_exam {


    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "type")           //类别
    private String type;

    @Column(name = "kecheng")     //1
    private String kecheng;

    @Column(name = "shijian")      //3
    private String shijian;

    @Column(name = "didian")      //4
    private String didian;

    @Column(name = "xingshi")      //5
    private String xingshi;

    @Column(name = "zuoweihao")      //6
    private String zuoweihao;

    public Jwgl_exam(String type,String kecheng,String shijian,String didian,String xingshi,String zuoweihao) {

        this.type = type;
        this.kecheng = kecheng;
        this.shijian = shijian;
        this.didian = didian;
        this.xingshi = xingshi;
        this.zuoweihao = zuoweihao;


    }

    public Jwgl_exam() {

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

    public String getKecheng() {
        return kecheng;
    }

    public void setKecheng(String kecheng) {
        this.kecheng = kecheng;
    }

    public String getShijian() {
        return shijian;
    }

    public void setShijian(String shijian) {
        this.shijian = shijian;
    }

    public String getDidian() {
        return didian;
    }

    public void setDidian(String didian) {
        this.didian = didian;
    }

    public String getXingshi() {
        return xingshi;
    }

    public void setXingshi(String xingshi) {
        this.xingshi = xingshi;
    }

    public String getZuoweihao() {
        return zuoweihao;
    }

    public void setZuoweihao(String zuoweihao) {
        this.zuoweihao = zuoweihao;
    }
}
