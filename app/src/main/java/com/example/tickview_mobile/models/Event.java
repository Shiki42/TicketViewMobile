package com.example.tickview_mobile.models;
import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable{
    private String name;
    private String imageUrl;
    private String venueName;
    private String date;
    private String time;
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
        return date;
    }

    public void setDate(String dates) {
        this.date = dates;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String classifications) {
        this.segmentName = classifications;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        venueName = in.readString();
        date = in.readString();
        time = in.readString();
        segmentName = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(venueName);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(segmentName);
    }
}