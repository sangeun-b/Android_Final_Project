package com.example.final_project;

public class GuardianNews {
    private String title;
    private String url;
    private String section;
    private long id;

    public GuardianNews(){};

    public GuardianNews(String title, String url, String section, long id){
        this.title=title;
        this.url=url;
        this.section=section;
        this.id=id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public String getSection(){
        return section;
    }

    public void setSection(String section){
        this.section=section;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id=id;
    }


}
