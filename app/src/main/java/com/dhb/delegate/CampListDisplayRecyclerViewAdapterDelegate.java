package com.dhb.delegate;

import com.dhb.models.data.CampBtechModel;
import com.dhb.models.data.CampDetailModel;

/**
 * Created by Orion on 4/27/2017.
 */
public interface CampListDisplayRecyclerViewAdapterDelegate {
    void onItemClick(CampDetailModel campDetailModel,int status,int pos);
    void onNavigationStart(CampDetailModel campListDisplayResponseModel);
}
