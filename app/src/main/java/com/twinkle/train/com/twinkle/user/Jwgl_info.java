package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/10/30.
 */

@Table(name = "jwgl_info")
public class Jwgl_info {

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "type")           //类别
    private String type;

    @Column(name = "xuehao")
    private String xuehao;

    @Column(name = "xingming")
    private String xingming;

    @Column(name = "xingbie")
    private String xingbie;

    @Column(name = "ruxue")
    private String ruxue;

    @Column(name = "chusheng")
    private String chusheng;

    @Column(name = "mingzu")
    private String mingzu;

    @Column(name = "zhengzhi")
    private String zhengzhi;

    @Column(name = "dianhua")
    private String dianhua;

    @Column(name = "youbian")
    private String youbian;

    @Column(name = "zhunkao")
    private String zhunkao;

    @Column(name = "shenfenzheng")
    private String shenfenzheng;

    @Column(name = "xueli")
    private String xueli;

    @Column(name = "xueyuan")
    private String xueyuan;

    @Column(name = "jiating")
    private String jiating;

    @Column(name = "zhuanye")
    private String zhuanye;

    @Column(name = "banji")
    private String banji;

    @Column(name = "yinyu")
    private String yinyu;

    @Column(name = "xuezhi")
    private String xuezhi;

    @Column(name = "xueji")
    private String xueji;

    @Column(name = "nianji")
    private String nianji;

    @Column(name = "kaoshenghao")
    private String kaoshenghao;

    public Jwgl_info(String type, String xuehao, String xingming, String xingbie,String ruxue,String chusheng,String mingzu,String zhengzhi,String dianhua,String youbian,String zhunkao,String shenfenzheng,String xueli,String xueyuan,String jiating,String zhuanye,String banji,String yinyu,String xuezhi,String nianji,String kaoshenghao,String xueji) {
        this.type = type;
        this.xuehao = xuehao;
        this.xingming = xingming;
        this.xingbie = xingbie;
        this.ruxue = ruxue;
        this.chusheng = chusheng;
        this.mingzu = mingzu;
        this.zhengzhi = zhengzhi;
        this.dianhua = dianhua;
        this.youbian = youbian;
        this.zhunkao = zhunkao;
        this.shenfenzheng = shenfenzheng;
        this.xueli = xueli;
        this.xueyuan = xueyuan;
        this.jiating = jiating;
        this.zhuanye = zhuanye;
        this.banji = banji;
        this.yinyu = yinyu;
        this.xuezhi = xuezhi;
        this.nianji = nianji;
        this.kaoshenghao = kaoshenghao;
        this.xueji = xueji;
    }

    public Jwgl_info() {

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

    public String getXuehao() {
        return xuehao;
    }

    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }

    public String getXingming() {
        return xingming;
    }

    public void setXingming(String xingming) {
        this.xingming = xingming;
    }

    public String getXingbie() {
        return xingbie;
    }

    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }

    public String getRuxue() {
        return ruxue;
    }

    public void setRuxue(String ruxue) {
        this.ruxue = ruxue;
    }

    public String getChusheng() {
        return chusheng;
    }

    public void setChusheng(String chusheng) {
        this.chusheng = chusheng;
    }

    public String getMingzu() {
        return mingzu;
    }

    public void setMingzu(String mingzu) {
        this.mingzu = mingzu;
    }

    public String getZhengzhi() {
        return zhengzhi;
    }

    public void setZhengzhi(String zhengzhi) {
        this.zhengzhi = zhengzhi;
    }

    public String getDianhua() {
        return dianhua;
    }

    public void setDianhua(String dianhua) {
        this.dianhua = dianhua;
    }

    public String getYoubian() {
        return youbian;
    }

    public void setYoubian(String youbian) {
        this.youbian = youbian;
    }

    public String getZhunkao() {
        return zhunkao;
    }

    public void setZhunkao(String zhunkao) {
        this.zhunkao = zhunkao;
    }

    public String getShenfenzheng() {
        return shenfenzheng;
    }

    public void setShenfenzheng(String shenfenzheng) {
        this.shenfenzheng = shenfenzheng;
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getXueyuan() {
        return xueyuan;
    }

    public void setXueyuan(String xueyuan) {
        this.xueyuan = xueyuan;
    }

    public String getJiating() {
        return jiating;
    }

    public void setJiating(String jiating) {
        this.jiating = jiating;
    }

    public String getZhuanye() {
        return zhuanye;
    }

    public void setZhuanye(String zhuanye) {
        this.zhuanye = zhuanye;
    }

    public String getBanji() {
        return banji;
    }

    public void setBanji(String banji) {
        this.banji = banji;
    }

    public String getYinyu() {
        return yinyu;
    }

    public void setYinyu(String yinyu) {
        this.yinyu = yinyu;
    }

    public String getXuezhi() {
        return xuezhi;
    }

    public void setXuezhi(String xuezhi) {
        this.xuezhi = xuezhi;
    }

    public String getXueji() {
        return xueji;
    }

    public void setXueji(String xueji) {
        this.xueji = xueji;
    }

    public String getNianji() {
        return nianji;
    }

    public void setNianji(String nianji) {
        this.nianji = nianji;
    }

    public String getKaoshenghao() {
        return kaoshenghao;
    }

    public void setKaoshenghao(String kaoshenghao) {
        this.kaoshenghao = kaoshenghao;
    }
}
