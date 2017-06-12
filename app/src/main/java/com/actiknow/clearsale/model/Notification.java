package com.actiknow.clearsale.model;

import org.json.JSONObject;

/**
 * Created by Admin on 11-01-2017.
 */

public class Notification {
    boolean isBackground;
    int notification_type; // 0=> default, 1=> request_for_blood, 2=> requester_notification, promotional_notification
    int notification_style; // 0=> default, 1=> simple_notification, 2=> inbox_style, 3=> big_text_style, 4=> big_picture_style
    int notification_priority; // -2=> PRIORITY_MIN, -1=> PRIORITY_LOW, 0=> PRIORITY_DEFAULT, 1=> PRIORITY_HIGH, 2=> PRIORITY_MAX
    String title, message, image_url, timestamp;
    JSONObject payload;


    public Notification(boolean isBackground, int notification_type, int notification_style, int notification_priority, String title, String message, String image_url, String timestamp, JSONObject payload) {
        this.isBackground = isBackground;
        this.notification_type = notification_type;
        this.notification_style = notification_style;
        this.notification_priority = notification_priority;
        this.title = title;
        this.message = message;
        this.image_url = image_url;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public Notification() {
    }

    public boolean isBackground () {
        return isBackground;
    }

    public void setBackground (boolean background) {
        isBackground = background;
    }

    public int getNotification_type () {
        return notification_type;
    }

    public void setNotification_type (int notification_type) {
        this.notification_type = notification_type;
    }

    public int getNotification_style () {
        return notification_style;
    }

    public void setNotification_style (int notification_style) {
        this.notification_style = notification_style;
    }

    public int getNotification_priority () {
        return notification_priority;
    }

    public void setNotification_priority (int notification_priority) {
        this.notification_priority = notification_priority;
    }
    
    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public String getImage_url () {
        return image_url;
    }

    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }

    public String getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (String timestamp) {
        this.timestamp = timestamp;
    }

    public JSONObject getPayload () {
        return payload;
    }

    public void setPayload (JSONObject payload) {
        this.payload = payload;
    }
}
