package com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class VideosModel {

    String RESPONSE,RES_ID;
    ArrayList<Videos> videolist;

    public VideosModel() {
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public ArrayList<Videos> getVideolist() {
        return videolist;
    }

    public void setVideolist(ArrayList<Videos> videolist) {
        this.videolist = videolist;
    }

    @Parcel
    public static class Videos {
        String videoTitle, videourl,thumbnailurl;


        public Videos() {
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public String getThumbnailurl() {
            return thumbnailurl;
        }

        public void setThumbnailurl(String thumbnailurl) {
            this.thumbnailurl = thumbnailurl;
        }
    }
}
