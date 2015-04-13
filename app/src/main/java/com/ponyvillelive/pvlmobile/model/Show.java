package com.ponyvillelive.pvlmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Presents all the properties of API show objects
 */
public class Show extends Entity {

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

}
