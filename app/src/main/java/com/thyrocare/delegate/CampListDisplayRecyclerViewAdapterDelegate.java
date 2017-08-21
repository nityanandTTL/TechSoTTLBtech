package com.thyrocare.delegate;

import com.thyrocare.models.data.CampDetailModel;

/**
 * Created by Orion on 4/27/2017.
 */
public interface CampListDisplayRecyclerViewAdapterDelegate {
    void onItemClick(CampDetailModel campDetailModel,int status,int pos);
    void onNavigationStart(CampDetailModel campListDisplayResponseModel);
}
