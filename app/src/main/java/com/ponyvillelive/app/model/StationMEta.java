package com.ponyvillelive.app.model;

/**
 * Created by tyr on 12/05/2014.
 */
public class StationMeta {

    public Station station;
    public ListenerMeta listeners;

    public static class ListenerMeta {
        public int current;
        public int unique;
        public int total;
    }

    public static class TrackMeta {
        public String id;
        public String text;
        public String artist;
        public String title;
        public Rating rating;
    }

    public static class Rating {
        public int likes;
        public int dislikes;
        public int score;
    }



}
