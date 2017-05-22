package com.actiknow.clearsale.model;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by l on 22/03/2017.
 */

public class AllProperty {
    int id;
    String image,number_bedroom,number_bathroom,size,year_build,address1,address2;
    boolean is_offer,is_available;

    public  AllProperty(int id,String number_bedroom,String number_bathroom,String size,String year_build
            ,String address1,String address2, boolean is_offer, boolean is_available,String image){

        this.id=id;
        this.image=image;
        this.number_bedroom=number_bedroom;
        this.number_bathroom=number_bathroom;
        this.size=size;
        this.year_build=year_build;
        this.address1=address1;
        this.address2=address2;
        this.is_offer=is_offer;
        this.is_available=is_available;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber_bedroom() {
        return number_bedroom;
    }

    public void setNumber_bedroom(String number_bedroom) {
        this.number_bedroom = number_bedroom;
    }

    public String getNumber_bathroom() {
        return number_bathroom;
    }

    public void setNumber_bathroom(String number_bathroom) {
        this.number_bathroom = number_bathroom;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getYear_build() {
        return year_build;
    }

    public void setYear_build(String year_build) {
        this.year_build = year_build;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public boolean is_offer() {
        return is_offer;
    }

    public void setIs_offer(boolean is_offer) {
        this.is_offer = is_offer;
    }

    public boolean is_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }
}
