package com.example.tickview_mobile.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ArtistData implements Parcelable {
    private String image;
    private String name;
    private int popularity;
    private int followers;
    private String spotifyUrl;
    private List<String> albumCoverImages;

    public ArtistData() {
        albumCoverImages = new ArrayList<>();
    }

    protected ArtistData(Parcel in) {
        image = in.readString();
        name = in.readString();
        popularity = in.readInt();
        followers = in.readInt();
        spotifyUrl = in.readString();
        albumCoverImages = in.createStringArrayList();
    }

    public static final Creator<ArtistData> CREATOR = new Creator<ArtistData>() {
        @Override
        public ArtistData createFromParcel(Parcel in) {
            return new ArtistData(in);
        }

        @Override
        public ArtistData[] newArray(int size) {
            return new ArtistData[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public List<String> getAlbumCoverImages() {
        return albumCoverImages;
    }

    public void setAlbumCoverImages(List<String> albumCoverImages) {
        this.albumCoverImages = albumCoverImages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeInt(popularity);
        dest.writeInt(followers);
        dest.writeString(spotifyUrl);
        dest.writeStringList(albumCoverImages);
    }
}