package com.example.tickview_mobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class VenueData implements Parcelable {
    private String name;
    private String address;
    private String cityState;
    private String contactInfo;
    private String openHours;
    private String generalRule;
    private String childRule;
    private Double latitude;
    private Double longitude;

    public VenueData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    protected VenueData(Parcel in) {
        name = in.readString();
        address = in.readString();
        cityState = in.readString();
        contactInfo = in.readString();
        openHours = in.readString();
        generalRule = in.readString();
        childRule = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<VenueData> CREATOR = new Creator<VenueData>() {
        @Override
        public VenueData createFromParcel(Parcel in) {
            return new VenueData(in);
        }

        @Override
        public VenueData[] newArray(int size) {
            return new VenueData[size];
        }
    };

    // Add getter and setter methods for each field

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(cityState);
        dest.writeString(contactInfo);
        dest.writeString(openHours);
        dest.writeString(generalRule);
        dest.writeString(childRule);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}