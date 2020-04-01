package com.example.final_project;

public class BBCItem{
    private long id;
    private String title, description, link, date, isFavourite;

    public BBCItem(){}

    public BBCItem(long id, String title, String description, String link, String date, String isFavourite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.isFavourite = isFavourite;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getLink() {
        return link;
    }

    public void setLink(String link) { this.link = link; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public String getIsFavourite() { return isFavourite; }

    public void setIsFavourite(String s) { this.isFavourite = s; }
}
