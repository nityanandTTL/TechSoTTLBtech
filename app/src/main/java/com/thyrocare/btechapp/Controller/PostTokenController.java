package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.NotificationTokenRequestModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionReqModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionRespModel;
import com.thyrocare.btechapp.models.api.response.NewCommonResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class PostTokenController {

    Activity activity;

    public PostTokenController(Activity activity) {
        this.activity = activity;
    }


    public void CallAPI(NotificationTokenRequestModel notificationTokenRequestModel) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.VELSO_URL))).create(PostAPIInterface.class);
            Call<NewCommonResponseModel> responseModelCall = apiInterface.PostToken(notificationTokenRequestModel);

            responseModelCall.enqueue(new Callback<NewCommonResponseModel>() {
                @Override
                public void onResponse(Call<NewCommonResponseModel> call, retrofit2.Response<NewCommonResponseModel> response) {


                }

                @Override
                public void onFailure(Call<NewCommonResponseModel> call, Throwable t) {


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
