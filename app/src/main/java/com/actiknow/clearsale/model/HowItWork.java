package com.actiknow.clearsale.model;

/**
 * Created by Admin on 24-05-2017.
 */

public class HowItWork {
    int id, image_drawable;
    String title, description, image;
    
    public HowItWork (int id, String title, String description, String image, int image_drawable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.image_drawable = image_drawable;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    public String getImage () {
        return image;
    }
    
    public void setImage (String image) {
        this.image = image;
    }
    
    public int getImage_drawable () {
        return image_drawable;
    }
    
    public void setImage_drawable (int image_drawable) {
        this.image_drawable = image_drawable;
    }
}
