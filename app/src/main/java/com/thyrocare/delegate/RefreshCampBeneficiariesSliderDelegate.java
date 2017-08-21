package com.thyrocare.delegate;

import com.thyrocare.models.api.response.CampListDisplayResponseModel;

/**
 * Created by Orion on 5/12/2017.
 */
public interface RefreshCampBeneficiariesSliderDelegate {
    void onRefreshActionCallbackReceived(CampListDisplayResponseModel campListDisplayResponseModel);
}
