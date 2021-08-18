package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HCW_Activity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.models.api.request.PayTMRequestModel;
import com.thyrocare.btechapp.models.api.response.PayTMResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class PayTMController {
    Activity activity;
    PaymentsActivity paymentsActivity;
    Global globalClass;

    public PayTMController(PaymentsActivity activity) {
        this.activity = activity;
        this.paymentsActivity = activity;
        globalClass = new Global(activity);
    }

    public void payTM(final PayTMRequestModel payTMRequestModel) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PayTMResponseModel> payTMResponseModelCall = postAPIInteface.payTM(payTMRequestModel);
            payTMResponseModelCall.enqueue(new Callback<PayTMResponseModel>() {
                @Override
                public void onResponse(Call<PayTMResponseModel> call, retrofit2.Response<PayTMResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PayTMResponseModel payTMResponseModel = response.body();
                            if (!InputUtils.isNull(payTMResponseModel.getStatusCode()) && InputUtils.CheckEqualIgnoreCase(payTMResponseModel.getStatusCode(), Constants.RES200)) {
                                paymentsActivity.getSubmitDataResponse(payTMResponseModel, payTMRequestModel.getOrderno().toString());
                            } else {
                                globalClass.showCustomToast(activity, "" + payTMResponseModel.getResponse());
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PayTMResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
