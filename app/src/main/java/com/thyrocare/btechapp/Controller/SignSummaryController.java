package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HCW_Activity;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.fragment.HomeScreenFragment;
import com.thyrocare.btechapp.models.api.request.SignINSummaryRequestModel;
import com.thyrocare.btechapp.models.api.response.SignSummaryResponseModel;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignSummaryController {
    Global globalClass;
    Activity activity;
    HomeScreenActivity homeScreenActivity;
    AppPreferenceManager appPreferenceManager;

    /*public SignSummaryController(HomeScreenFragment homeScreenFragment) {
        this.homeScreenFragment = homeScreenFragment;
        globalClass = new Global(homeScreenFragment.getActivity());
        appPreferenceManager = new AppPreferenceManager(homeScreenFragment.getActivity());
    }*/

    public SignSummaryController(HomeScreenActivity homeScreenActivity) {
        this.homeScreenActivity = homeScreenActivity;
        this.activity = homeScreenActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public void signINOUTSummary(SignINSummaryRequestModel requestModel) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<SignSummaryResponseModel> summaryModelCall = postAPIInteface.signInSummary(requestModel);
            summaryModelCall.enqueue(new Callback<SignSummaryResponseModel>() {
                @Override
                public void onResponse(Call<SignSummaryResponseModel> call, Response<SignSummaryResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            SignSummaryResponseModel responseModel = response.body();
                            if (!InputUtils.isNull(responseModel.getResponseId()) && InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), ConstantsMessages.RES0000)) {
                                homeScreenActivity.getSubmitDataResponse(responseModel);
                            } else if (!InputUtils.isNull(responseModel.getResponseId()) && InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), Constants.RES0001)) {
                                homeScreenActivity.getSubmitDataResponse(responseModel);
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
                public void onFailure(Call<SignSummaryResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
