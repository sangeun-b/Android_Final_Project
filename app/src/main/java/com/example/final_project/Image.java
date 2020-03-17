package com.example.final_project;

public class Image {

    private String date;
    private String title;
    private String url;
    private String hdurl;
    private long id;

    public Image(String date, String title, String url, String hdurl, long id){
        this.date=date;
        this.title=title;
        this.url= url;
        this.hdurl= hdurl;
        this.id= id;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



}
