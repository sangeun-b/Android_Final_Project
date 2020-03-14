package com.example.final_project;

public class BBCItem{
    private String title, description, link, date;
    private long id;

    public BBCItem(){}

    public BBCItem(String title, String description, String link, String date, long id) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.id = id;
    }

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

    public long getId() { return id; }
}
