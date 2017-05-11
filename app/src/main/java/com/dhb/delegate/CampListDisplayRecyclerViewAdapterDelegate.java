package com.dhb.delegate;

import com.dhb.models.data.CampBtechModel;
import com.dhb.models.data.CampDetailModel;

/**
 * Created by ISRO on 4/27/2017.
 */
public interface CampListDisplayRecyclerViewAdapterDelegate {
    void onItemClick(CampDetailModel campDetailModel);
    void onNavigationStart(CampDetailModel campListDisplayResponseModel);
}
