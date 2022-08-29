package com.thyrocare.btechapp.Controller;

import android.app.Activity;


import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.GetCollectionReqModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionRespModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class GetCollectionCenterController {


    Activity activity;
    CheckoutWoeActivity checkoutWoeActivity;
    int flag;
    private Global globalClass;

    public GetCollectionCenterController(CheckoutWoeActivity checkoutWoeActivity) {
        this.activity = checkoutWoeActivity;
        globalClass = new Global(activity);
        this.checkoutWoeActivity = checkoutWoeActivity;
        flag = 1;
    }


    public void CallAPI(GetCollectionReqModel getCollectionReqModel) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.VELSO_URL))).create(PostAPIInterface.class);
            Call<GetCollectionRespModel> responseModelCall = apiInterface.GetCollectionCenter(getCollectionReqModel);
            globalClass.showProgressDialog(activity, "Please wait..");

            responseModelCall.enqueue(new Callback<GetCollectionRespModel>() {
                @Override
                public void onResponse(Call<GetCollectionRespModel> call, retrofit2.Response<GetCollectionRespModel> response) {
                    globalClass.hideProgressDialog(activity);
                    if (response.isSuccessful() && response.body() != null) {
                        if (flag == 1) {
                            checkoutWoeActivity.getCollectioncenters(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetCollectionRespModel> call, Throwable t) {
                    globalClass.hideProgressDialog(activity);

                }
            });

        } catch (Exception e) {
            globalClass.hideProgressDialog(activity);
            e.printStackTrace();
        }
    }

}
