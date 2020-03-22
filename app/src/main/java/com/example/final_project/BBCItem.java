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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getIsFavourite() { return isFavourite; }

    public void setIsFavourite(String s) { this.isFavourite = s; }
}
