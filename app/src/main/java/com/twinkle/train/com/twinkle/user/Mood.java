package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/12/1.
 */

@Table(name = "Mood")
public class Mood {


    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "article")
    private String article;

    @Column(name = "date")
    private String date;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    public Mood() {

    }

    public Mood(String article, String date, String name, String title) {
        this.article = article;
        this.date = date;
        this.name = name;
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
