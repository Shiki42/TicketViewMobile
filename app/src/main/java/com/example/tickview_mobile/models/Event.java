package com.example.tickview_mobile.models;
import android.os.Parcel;
import android.os.Parcelable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event implements Parcelable{
    private String id;
    private String name;
    private String imageUrl;
    private String venueName;
    private String date;
    private String time;
    private String segmentName;
    // Create getters and setters for each field

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setTime(String localTime) {
        if (!localTime.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                Date parsedTime = inputFormat.parse(localTime);
                if (parsedTime != null) {
                    this.time = outputFormat.format(parsedTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                this.time = localTime;
            }
        } else {
            this.time = localTime;
        }
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
        id = in.readString();
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
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(venueName);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(segmentName);
    }
}