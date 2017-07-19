package com.dhb.delegate;

import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.data.OrderVisitDetailsModel;

/**
 * Created by Orion on 5/12/2017.
 */
public interface RefreshCampBeneficiariesSliderDelegate {
    void onRefreshActionCallbackReceived(CampListDisplayResponseModel campListDisplayResponseModel);
}
