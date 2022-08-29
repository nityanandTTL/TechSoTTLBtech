package com.thyrocare.btechapp.Controller;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.fragment.HomeScreenFragment;
import com.thyrocare.btechapp.models.api.request.SignINSummaryRequestModel;
import com.thyrocare.btechapp.models.api.request.SignInRequestModel;
import com.thyrocare.btechapp.models.api.response.SignInResponseModel;
import com.thyrocare.btechapp.models.api.response.SignSummaryResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class SignINOUTController {
    Global globalClass;
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    HomeScreenActivity homeScreenActivity;

    public SignINOUTController(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
        this.activity = homeScreenActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public void signINOUT(SignInRequestModel requestModel) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<SignInResponseModel> vehicleEntryResponseModelCall = postAPIInteface.signINOUT(requestModel);
            vehicleEntryResponseModelCall.enqueue(new Callback<SignInResponseModel>() {
                @Override
                public void onResponse(Call<SignInResponseModel> call, retrofit2.Response<SignInResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            SignInResponseModel responseModel = response.body();
                            if (!InputUtils.isNull(responseModel.getResponseId()) /*&& InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), ConstantsMessages.RES0000)*/) {
                                homeScreenActivity.getSignINdata(responseModel);
                            } else {
                                globalClass.showCustomToast(activity, "" + responseModel.getResponse());
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SignInResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

