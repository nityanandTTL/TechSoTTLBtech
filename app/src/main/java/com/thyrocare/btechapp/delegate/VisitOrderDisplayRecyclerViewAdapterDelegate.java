package com.thyrocare.btechapp.delegate;

import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 4/27/2017.
 */
public interface VisitOrderDisplayRecyclerViewAdapterDelegate {
    void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel);

    void onItemReschedule(OrderVisitDetailsModel orderVisitDetailsModel);

    void onNavigationStart(OrderVisitDetailsModel orderVisitDetailsModel);

    void onOrderAccepted(OrderVisitDetailsModel orderVisitDetailsModel);

    void onCallCustomer(OrderVisitDetailsModel orderVisitDetailsModel);
}
