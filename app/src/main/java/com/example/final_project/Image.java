package com.example.final_project;

/**
 * The image is a class that has no logic in it. It contains constructor, getter and setter for the five instance variables.
 * @author Hsing-I Wang
 * @version 1.0
 */
public class Image {

    private String date;
    private String title;
    private String url;
    private String hdurl;
    private long id;

    /**
     *five argument constructor
     */
    public Image(String date, String title, String url, String hdurl, long id){
        this.date=date;
        this.title=title;
        this.url= url;
        this.hdurl= hdurl;
        this.id= id;
    }

    /**
     * getter for the date
     */
    public String getDate() { return date; }

    /**
     * setter for the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * getter for the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter for the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getter for the hdurl
     */
    public String getHdurl() {
        return hdurl;
    }

    /**
     * setter for the hdurl
     */
    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    /**
     * getter for the id
     */
    public long getId() {
        return id;
    }

    /**
     * setter for the id
     */
    public void setId(long id) {
        this.id = id;
    }

}
