package com.thyrocare.models.api.response;

/**
 * Created by Orion on 5/29/2017.
 */

public class BtechImageResponseModel {
    private int BtechId;
    private String ImgUrl;
    private String Response;
    private String SubscriptionKey;
    private String EndpointKey;

    private int flag;

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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSubscriptionKey() {
        return SubscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        SubscriptionKey = subscriptionKey;
    }

    public String getEndpointKey() {
        return EndpointKey;
    }

    public void setEndpointKey(String endpointKey) {
        EndpointKey = endpointKey;
    }
}
