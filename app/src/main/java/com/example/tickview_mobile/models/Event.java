package com.example.tickview_mobile.models;

import java.util.List;

public class Event {
    private String name;
    private String imageUrl;
    private String venueName;
    private String localDate;
    private String localTime;
    private String segmentName;
    // Create getters and setters for each field

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return localDate;
    }

    public void setDate(String dates) {
        this.localDate = dates;
    }

    public String getTime() {
        return localTime;
    }

    public void setTime(String time) {
        this.localTime = time;
    }


    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String classifications) {
        this.segmentName = classifications;
    }
}