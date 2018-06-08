package com.thyrocare.models.api.response;

/**
 * Created by Orion on 5/29/2017.
 */

public class BtechImageResponseModel {
    private int BtechId;
    private String ImgUrl;
    private String Response;

    public int getBtechId() {
        return BtechId;
    }

    public void setBtechId(int btechId) {
        BtechId = btechId;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
