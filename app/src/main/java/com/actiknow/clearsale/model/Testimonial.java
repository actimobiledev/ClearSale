package com.actiknow.clearsale.model;

public class Testimonial {
    int id;
    String description, name, video_url;
    
    public Testimonial (int id, String description, String name, String video_url) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.video_url = video_url;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getVideo_url () {
        return video_url;
    }
    
    public void setVideo_url (String video_url) {
        this.video_url = video_url;
    }
}
