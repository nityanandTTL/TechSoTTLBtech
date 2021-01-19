package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;


import retrofit2.Call;
import retrofit2.Callback;

public class NotificationMappingController {
    Activity activity;
    Gson gson;

    public NotificationMappingController(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public void getNotificationMapping(final NotificationMappingRequestModel notificationMappingModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<NotificationMappingResponseModel> responseCall = apiInterface.NotificationTokenMappingAPI(notificationMappingModel);
        responseCall.enqueue(new Callback<NotificationMappingResponseModel>() {
            @Override
            public void onResponse(Call<NotificationMappingResponseModel> call, retrofit2.Response<NotificationMappingResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NotificationMappingResponseModel model1 = response.body();
                    if (model1.getResponseId() != null && model1.getResponseId().equalsIgnoreCase("RES0000")) {
                    } else {
                    }
                } else {
                }
            }
            @Override
            public void onFailure(Call<NotificationMappingResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }
}