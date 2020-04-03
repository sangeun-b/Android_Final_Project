package com.example.final_project;
/**
 * This class is used to instantiated BBC news.
 * @author Xin Guo
 * @version 1.0
 */
public class BBCItem{
    /**
     * Represents BBC news id in long.
     */
    private long id;
    /**
     * Represents BBC news title/description/link/date/isFavourite in String.
     */
    private String title, description, link, date, isFavourite;
    /**
     * No-arg BBCItem constructor.
     */
    public BBCItem(){}
    /**
     * 6 arguments BBCItem constructor which instantiates BBCItem object with id, title, description, link, date, and isFavourite.
     * @param id BBC news id in long
     * @param title BBC news title in String
     * @param description BBC news description in String
     * @param link BBC news link in String
     * @param date BBC news date in String
     * @param isFavourite BBC news isFavourite in String
     */
    public BBCItem(long id, String title, String description, String link, String date, String isFavourite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.isFavourite = isFavourite;
    }
    /**
     * Getter for BBC news id. Return BBC news id in long.
     * @return BBC news id in long
     */
    public long getId() { return id; }
    /**
     * Setter for BBC news id. Set BBC news id with passed-in parameter.
     * @param id BBC news id in long
     */
    public void setId(long id) { this.id = id; }
    /**
     * Getter for BBC news title. Return BBC news title in String.
     * @return BBC news title in String
     */
    public String getTitle() { return title; }
    /**
     * Setter for BBC news title. Set BBC news title with passed-in parameter.
     * @param title BBC news title in String
     */
    public void setTitle(String title) { this.title = title; }
    /**
     * Getter for BBC news description. Return BBC news description in String.
     * @return BBC news description in String
     */
    public String getDescription() { return description; }
    /**
     * Setter for BBC news description. Set BBC news description with passed-in parameter.
     * @param description BBC news description in String
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Getter for BBC news link. Return BBC news link in String.
     * @return BBC news link in String
     */
    public String getLink() { return link; }
    /**
     * Setter for BBC news link. Set BBC news link with passed-in parameter.
     * @param link BBC news link in String
     */
    public void setLink(String link) { this.link = link; }
    /**
     * Getter for BBC news date. Return BBC news date in String.
     * @return BBC news date in String
     */
    public String getDate() { return date; }
    /**
     * Setter for BBC news date. Set BBC news date with passed-in parameter.
     * @param date BBC news date in String
     */
    public void setDate(String date) { this.date = date; }
    /**
     * Getter for BBC news isFavourite. Return BBC news isFavourite in String.
     * @return BBC news isFavourite in String
     */
    public String getIsFavourite() { return isFavourite; }
    /**
     * Setter for BBC news isFavourite. Set BBC news isFavourite with passed-in parameter.
     * @param isFavourite BBC news isFavourite in String
     */
    public void setIsFavourite(String isFavourite) { this.isFavourite = isFavourite; }
}
