package com.thyrocare.models.api.response;

import com.thyrocare.models.data.OrderVisitDetailsModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/21/2017.
 */

    public class FetchOrderDetailsResponseModel {
    private ArrayList<OrderVisitDetailsModel> orderVisitDetails;

    public ArrayList<OrderVisitDetailsModel> getOrderVisitDetails() {
        return orderVisitDetails;
    }

    public void setOrderVisitDetails(ArrayList<OrderVisitDetailsModel> orderVisitDetails) {
        this.orderVisitDetails = orderVisitDetails;
    }
}
