package com.actiknow.clearsale.model;

public class Testimonial {
    int id,image2;
    String  description , name,image;

    public Testimonial(int id,int image2, String description, String name,String image) {
        this.id = id;
        this.image2 = image2;
        this.description = description;
        this.name = name;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage2() {
        return image2;
    }

    public void setImage2(int image2) {
        this.image2 = image2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
