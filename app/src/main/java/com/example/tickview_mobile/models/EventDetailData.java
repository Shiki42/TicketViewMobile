package com.example.tickview_mobile.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailData implements Parcelable {
    private String localDate;
    private String localTime;
    private String attractions;
    private String venueName;
    private String genres;
    private String priceRanges;
    private String ticketStatus;
    private String ticketUrl;
    private String seatMapUrl;

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        if (!localTime.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                Date parsedTime = inputFormat.parse(localTime);
                if (parsedTime != null) {
                    this.localTime = outputFormat.format(parsedTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                this.localTime = localTime;
            }
        } else {
            this.localTime = localTime;
        }
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(String priceRanges) {
        this.priceRanges = priceRanges;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getSeatMapUrl() {
        return seatMapUrl;
    }

    public void setSeatMapUrl(String seatMapUrl) {
        this.seatMapUrl = seatMapUrl;
    }

    public EventDetailData() {
    }

    protected EventDetailData(Parcel in) {
        localDate = in.readString();
        localTime = in.readString();
        attractions = in.readString();
        venueName = in.readString();
        genres = in.readString();
        priceRanges = in.readString();
        ticketStatus = in.readString();
        ticketUrl = in.readString();
        seatMapUrl = in.readString();
    }

    public static final Creator<EventDetailData> CREATOR = new Creator<EventDetailData>() {
        @Override
        public EventDetailData createFromParcel(Parcel in) {
            return new EventDetailData(in);
        }

        @Override
        public EventDetailData[] newArray(int size) {
            return new EventDetailData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(localDate);
        dest.writeString(localTime);
        dest.writeString(attractions);
        dest.writeString(venueName);
        dest.writeString(genres);
        dest.writeString(priceRanges);
        dest.writeString(ticketStatus);
        dest.writeString(ticketUrl);
        dest.writeString(seatMapUrl);
    }
}