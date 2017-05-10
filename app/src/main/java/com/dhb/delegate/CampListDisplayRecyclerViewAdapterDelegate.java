package com.dhb.delegate;

import com.dhb.models.data.CampListDisplayResponseModel;
import com.dhb.models.data.OrderVisitDetailsModel;

/**
 * Created by ISRO on 4/27/2017.
 */
public interface CampListDisplayRecyclerViewAdapterDelegate {
    void onItemClick(CampListDisplayResponseModel campListDisplayResponseModel);
    void onNavigationStart(CampListDisplayResponseModel campListDisplayResponseModel);
}
