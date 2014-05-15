package com.ponyvillelive.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tyr on 14/05/2014.
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
