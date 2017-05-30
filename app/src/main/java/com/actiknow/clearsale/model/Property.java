package com.actiknow.clearsale.model;

import java.util.ArrayList;

/**
 * Created by l on 22/03/2017.
 */

public class Property {
    // status => 0 = Sold, 1 = Available, 2 = Pending
    int id, status;
    ArrayList<String> imageList = new ArrayList<String> ();
    String price, bedroom, bathroom, area, year_built, address1, address2;
    boolean is_offer, is_favourite;
    
    public Property (int id, int status, ArrayList<String> imageList, String price, String bedroom, String bathroom, String area, String year_built, String address1, String address2, boolean is_offer, boolean is_favourite) {
        this.id = id;
        this.status = status;
        this.imageList = imageList;
        this.price = price;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.area = area;
        this.year_built = year_built;
        this.address1 = address1;
        this.address2 = address2;
        this.is_offer = is_offer;
        this.is_favourite = is_favourite;
    }
    
    public Property (int id, int status, String price, String bedroom, String bathroom, String area, String year_built, String address1, String address2, boolean is_offer) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.area = area;
        this.year_built = year_built;
        this.address1 = address1;
        this.address2 = address2;
        this.is_offer = is_offer;
    }
    
    public int getId () {
        return id;
    }
    
    public void setId (int id) {
        this.id = id;
    }
    
    public int getStatus () {
        return status;
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
    public ArrayList<String> getImageList () {
        return imageList;
    }
    
    public void setImageList (ArrayList<String> imageList) {
        this.imageList = imageList;
    }
    
    public String getPrice () {
        return price;
    }
    
    public void setPrice (String price) {
        this.price = price;
    }
    
    public String getBedroom () {
        return bedroom;
    }
    
    public void setBedroom (String bedroom) {
        this.bedroom = bedroom;
    }
    
    public String getBathroom () {
        return bathroom;
    }
    
    public void setBathroom (String bathroom) {
        this.bathroom = bathroom;
    }
    
    public String getArea () {
        return area;
    }
    
    public void setArea (String area) {
        this.area = area;
    }
    
    public String getYear_built () {
        return year_built;
    }
    
    public void setYear_built (String year_built) {
        this.year_built = year_built;
    }
    
    public String getAddress1 () {
        return address1;
    }
    
    public void setAddress1 (String address1) {
        this.address1 = address1;
    }
    
    public String getAddress2 () {
        return address2;
    }
    
    public void setAddress2 (String address2) {
        this.address2 = address2;
    }
    
    public boolean is_offer () {
        return is_offer;
    }
    
    public void setIs_offer (boolean is_offer) {
        this.is_offer = is_offer;
    }
    
    public boolean is_favourite () {
        return is_favourite;
    }
    
    public void setIs_favourite (boolean is_favourite) {
        this.is_favourite = is_favourite;
    }
}
