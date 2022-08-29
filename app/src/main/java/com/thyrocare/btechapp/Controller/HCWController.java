package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HCW_Activity;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class HCWController {

    Activity activity;
    HCW_Activity hcw_activity;
    Global globalClass;

    public HCWController(HCW_Activity hcw_activity) {
        this.activity = hcw_activity;
        this.hcw_activity = hcw_activity;
        globalClass = new Global(activity);
    }

    public void postHCW(HCWRequestModel hcwRequestModel) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.Thyrocare))).create(PostAPIInterface.class);
            Call<HCWResponseModel> vehicleEntryResponseModelCall = postAPIInteface.postHCW(hcwRequestModel);
            vehicleEntryResponseModelCall.enqueue(new Callback<HCWResponseModel>() {
                @Override
                public void onResponse(Call<HCWResponseModel> call, retrofit2.Response<HCWResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            HCWResponseModel hcwResponseModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(hcwResponseModel.getStatus(), Constants.RESP0000)) {
                                hcw_activity.getSubmitDataResponse(hcwResponseModel);
                            } else {
                                globalClass.showCustomToast(activity, "" + hcwResponseModel.getMsg());
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<HCWResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
