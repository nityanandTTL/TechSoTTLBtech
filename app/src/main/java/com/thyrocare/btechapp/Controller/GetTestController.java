package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.GetTestCodeRequestModel;
import com.thyrocare.btechapp.models.api.response.GetTestResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class GetTestController {
    Activity activity;
    Global globalClass;
    CheckoutWoeActivity checkoutWoeActivity;

    public GetTestController(CheckoutWoeActivity checkoutWoeActivity) {
        this.activity = checkoutWoeActivity;
        this.checkoutWoeActivity = checkoutWoeActivity;
        globalClass = new Global(activity);
    }

    public void getTest(GetTestCodeRequestModel getTestCodeRequestModel) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.Cliso))).create(PostAPIInterface.class);
            Call<GetTestResponseModel> getTestResponseModelCall = postAPIInteface.postTest(getTestCodeRequestModel);
            getTestResponseModelCall.enqueue(new Callback<GetTestResponseModel>() {
                @Override
                public void onResponse(Call<GetTestResponseModel> call, retrofit2.Response<GetTestResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            GetTestResponseModel responseModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(responseModel.getResID(), ConstantsMessages.RES0000)) {
                                checkoutWoeActivity.getSubmitDataResponse(responseModel);
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
                public void onFailure(Call<GetTestResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
