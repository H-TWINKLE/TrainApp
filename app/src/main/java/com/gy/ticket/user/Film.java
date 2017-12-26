package com.gy.ticket.user;

/**
 * Created by TWINKLE on 2017/12/25.
 */

public class Film {

    /**
     * actor : 黄轩,苗苗,钟楚曦,杨采钰,李晓峰,王天辰,王可如,隋源,苏岩,赵立新,张仁博,周放,薛祺,杨烁,陶海,李卓航,魏之皓,郭沛志,王艺铼
     * date : 2017-12-25
     * director : 冯小刚
     * id : 22
     * img : https://p0.meituan.net/148.208//movie/fe0d4da87d70ba2b91e10ac98e0bf5ef1365131.png
     * info : 少年文工团，青春很茫然
     * info_more : 本片讲述了上世纪七十到八十年代充满理想和激情的军队文工团，一群正值芳华的青春少年，经历着成长中的爱情萌发与充斥变数的人生命运。乐于助人、质朴善良的刘峰（黄轩 饰），和从农村来，屡遭文工团女兵歧视与排斥的何小萍（苗苗 饰），“意外”离开了浪漫安逸的文工团，卷入了残酷的战争，在战场上继续绽放着血染的芳华。他们感受
     * length : 136分钟
     * local : 中国大陆
     * name : 芳华
     * score : 9.1
     * type : 1
     * url_date : 2017-12-15上映
     * url_id : /movie/933943
     */

    private String actor;
    private String date;
    private String director;
    private int id;
    private String img;
    private String info;
    private String info_more;
    private String length;
    private String local;
    private String name;
    private String score;
    private int type;
    private String url_date;
    private String url_id;


    public Film() {
    }

    public Film(String actor, String date, String director, int id, String img, String info, String info_more,
                String length, String local, String name, String score, int type, String url_date, String url_id) {
        this.actor = actor;
        this.date = date;
        this.director = director;
        this.id = id;
        this.img = img;
        this.info = info;
        this.info_more = info_more;
        this.length = length;
        this.local = local;
        this.name = name;
        this.score = score;
        this.type = type;
        this.url_date = url_date;
        this.url_id = url_id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo_more() {
        return info_more;
    }

    public void setInfo_more(String info_more) {
        this.info_more = info_more;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl_date() {
        return url_date;
    }

    public void setUrl_date(String url_date) {
        this.url_date = url_date;
    }

    public String getUrl_id() {
        return url_id;
    }

    public void setUrl_id(String url_id) {
        this.url_id = url_id;
    }
}
