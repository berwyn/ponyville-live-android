package com.ponyvillelive.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Radio station entity as presented by the API
 */
public class Station extends Entity implements Parcelable {

    public static final String STATION_TYPE_AUDIO = "audio";
    public static final String STATION_TYPE_VIDEO = "video";

    public String name;
    public String shortcode;
    public String genre;
    public String category;
    public String type;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("web_url")
    public String webUrl;
    @SerializedName("stream_url")
    public String streamUrl;
    @SerializedName("twitter_url")
    public String twitterUrl;
    public String irc;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.shortcode);
        dest.writeString(this.genre);
        dest.writeString(this.category);
        dest.writeString(this.type);
        dest.writeString(this.imageUrl);
        dest.writeString(this.webUrl);
        dest.writeString(this.streamUrl);
        dest.writeString(this.twitterUrl);
        dest.writeString(this.irc);
        dest.writeInt(this.id);
    }

    public Station() {
    }

    private Station(Parcel in) {
        this.name = in.readString();
        this.shortcode = in.readString();
        this.genre = in.readString();
        this.category = in.readString();
        this.type = in.readString();
        this.imageUrl = in.readString();
        this.webUrl = in.readString();
        this.streamUrl = in.readString();
        this.twitterUrl = in.readString();
        this.irc = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
