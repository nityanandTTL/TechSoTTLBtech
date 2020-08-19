package com.thyrocare.btechapp.models.api.response;

import java.util.ArrayList;

public class VideosResponseModel {

    ArrayList<Outputlang> Output;
    String resId;
    String response;

    public ArrayList<Outputlang> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<Outputlang> output) {
        Output = output;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static class Outputlang {

        String Active;
        String App;
        String Description;
        String Imageurl;
        String Language;
        String Path;
        String Thumbtime;
        String Title;
        String id;
        boolean isVideoPlaying = false;
        boolean isVideoPaused = false;

        public String getActive() {
            return Active;
        }

        public void setActive(String active) {
            Active = active;
        }

        public String getApp() {
            return App;
        }

        public void setApp(String app) {
            App = app;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getImageurl() {
            return Imageurl;
        }

        public void setImageurl(String imageurl) {
            Imageurl = imageurl;
        }

        public String getLanguage() {
            return Language;
        }

        public void setLanguage(String language) {
            Language = language;
        }

        public String getPath() {
            return Path;
        }

        public void setPath(String path) {
            Path = path;
        }

        public String getThumbtime() {
            return Thumbtime;
        }

        public void setThumbtime(String thumbtime) {
            Thumbtime = thumbtime;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isVideoPlaying() {
            return isVideoPlaying;
        }

        public void setVideoPlaying(boolean videoPlaying) {
            isVideoPlaying = videoPlaying;
        }

        public boolean isVideoPaused() {
            return isVideoPaused;
        }

        public void setVideoPaused(boolean videoPaused) {
            isVideoPaused = videoPaused;
        }
    }
}

