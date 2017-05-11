package com.dhb.models.api.response;

import com.dhb.models.data.CampDetailModel;

import java.util.ArrayList;

/**
 * Created by vendor1 on 5/11/2017.
 */

public class CampListDisplayResponseModel {
    String Response;
    ArrayList<CampDetailModel> campDetail;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<CampDetailModel> getCampDetail() {
        return campDetail;
    }

    public void setCampDetail(ArrayList<CampDetailModel> campDetail) {
        this.campDetail = campDetail;
    }
}
