package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.models.api.request.PayTMRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMVerifyRequestModel;
import com.thyrocare.btechapp.models.api.response.PayTMResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMVerifyResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class PayTMVerifyController {

    Activity activity;
    PaymentsActivity paymentsActivity;
    Global globalClass;

    public PayTMVerifyController(PaymentsActivity activity) {
        this.activity = activity;
        this.paymentsActivity = activity;
        globalClass = new Global(activity);
    }

    public void payTMVerify(PayTMVerifyRequestModel payTMVerifyRequestModel) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PayTMVerifyResponseModel> payTMResponseModelCall = postAPIInteface.payTMVerify(payTMVerifyRequestModel);
            payTMResponseModelCall.enqueue(new Callback<PayTMVerifyResponseModel>() {
                @Override
                public void onResponse(Call<PayTMVerifyResponseModel> call, retrofit2.Response<PayTMVerifyResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PayTMVerifyResponseModel payTMVerifyResponseModel = response.body();
                            if (!InputUtils.isNull(payTMVerifyResponseModel.getStatus()) && InputUtils.CheckEqualIgnoreCase(payTMVerifyResponseModel.getStatus(), ConstantsMessages.RES0000)) {
                               Global.showCustomStaticToast(activity,payTMVerifyResponseModel.getResponseMessage());
                                paymentsActivity.getSubmitVerifyResponse();
                            } else {
                                globalClass.showCustomToast(activity, "" + payTMVerifyResponseModel.getResponseMessage());
                            }
                        }
                        else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PayTMVerifyResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
