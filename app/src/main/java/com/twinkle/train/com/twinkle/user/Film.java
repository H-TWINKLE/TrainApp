package com.twinkle.train.com.twinkle.user;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by TWINKLE on 2017/11/15.
 */


@Table(name = "film")
public class Film {

    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "episode")
    private String episode;

    @Column(name = "href_img")
    private String href_img;

    @Column(name = "href_url")
    private String href_url;

    @Column(name = "date")
    private String date;


    public Film(String title, String episode, String href_img, String href_url, String date) {
        super();
        this.title = title;
        this.episode = episode;
        this.href_img = href_img;
        this.href_url = href_url;
        this.date = date;
    }

    public Film() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getHref_img() {
        return href_img;
    }

    public void setHref_img(String href_img) {
        this.href_img = href_img;
    }

    public String getHref_url() {
        return href_url;
    }

    public void setHref_url(String href_url) {
        this.href_url = href_url;
    }


}
