package com.thyrocare.delegate;

import com.thyrocare.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/27/2017.
 */
public interface OrderPassRecyclerViewAdapterDelegate {
    void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel);
    void onItemReleaseto (String  Pincode ,OrderVisitDetailsModel orderVisitDetailsModel);

}
