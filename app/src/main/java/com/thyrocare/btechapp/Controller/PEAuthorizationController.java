package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HCW_Activity;
import com.thyrocare.btechapp.models.api.response.PEAuthResponseModel;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class PEAuthorizationController {

    Activity activity;
    HCW_Activity hcw_activity;
    Global globalClass;
    AppPreferenceManager appPreferenceManager;

    public PEAuthorizationController(HCW_Activity hcw_activity) {
        this.activity = hcw_activity;
        this.hcw_activity = hcw_activity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public void getAuthorizationToken(HCWRequestModel hcwRequestModel) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
            Call<PEAuthResponseModel> peAuthResponseModelCall = getAPIInterface.callPEAuthorization(Constants.XSource, Constants.clientid);
            peAuthResponseModelCall.enqueue(new Callback<PEAuthResponseModel>() {
                @Override
                public void onResponse(Call<PEAuthResponseModel> call, retrofit2.Response<PEAuthResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PEAuthResponseModel peAuthResponseModel = response.body();
                            if (peAuthResponseModel.isStatus() == true) {
                                appPreferenceManager.setAuthToken(peAuthResponseModel.getData().getAuthtoken());
                            } else {

                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PEAuthResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
