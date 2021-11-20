package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.PaytypeRequestModel;
import com.thyrocare.btechapp.models.api.response.PaytypeResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class PayTypeController {

        Activity activity;
        CheckoutWoeActivity checkoutWoeActivity;
        Global globalClass;

        public PayTypeController(CheckoutWoeActivity activity) {
            this.activity = activity;
            this.checkoutWoeActivity = activity;
            globalClass = new Global(activity);
        }

        public void getPayType(final PaytypeRequestModel paytypeRequestModel) {
            try {
                globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
                PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
                Call<PaytypeResponseModel> paytypeResponseModelCall = postAPIInteface.getOrderPaytype(paytypeRequestModel);
                paytypeResponseModelCall.enqueue(new Callback<PaytypeResponseModel>() {
                    @Override
                    public void onResponse(Call<PaytypeResponseModel> call, retrofit2.Response<PaytypeResponseModel> response) {
                        globalClass.hideProgressDialog(activity);
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                PaytypeResponseModel responseModel = response.body();
                                if (!InputUtils.isNull(responseModel.getRespId()) && InputUtils.CheckEqualIgnoreCase(responseModel.getRespId(), Constants.RES000)) {
                                    checkoutWoeActivity.getPayTypeResponse(responseModel);
//                                    TastyToast.makeText(activity,""+responseModel.getPaytype(),TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
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
                    public void onFailure(Call<PaytypeResponseModel> call, Throwable t) {
                        globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        globalClass.hideProgressDialog(activity);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}
