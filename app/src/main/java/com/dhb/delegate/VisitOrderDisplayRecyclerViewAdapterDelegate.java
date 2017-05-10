package com.dhb.delegate;

import com.dhb.models.data.OrderVisitDetailsModel;

/**
 * Created by ISRO on 4/27/2017.
 */
public interface VisitOrderDisplayRecyclerViewAdapterDelegate {
    void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel);
    void onNavigationStart(OrderVisitDetailsModel orderVisitDetailsModel);
    void onOrderAccepted(OrderVisitDetailsModel orderVisitDetailsModel);
}
