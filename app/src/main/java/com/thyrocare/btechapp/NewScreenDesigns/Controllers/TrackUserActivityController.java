package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import android.app.Activity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.TrackUserActivityRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.network.AbstractApiModel;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class TrackUserActivityController {
    private static final String TAG = TrackUserActivityController.class.getSimpleName();
    Activity activity;
    static RequestQueue requestQueue;
    Gson gson;

    public TrackUserActivityController(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public void trackUserActivity(TrackUserActivityRequestModel trackUserActivityRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.CallUserTrackingAPI(trackUserActivityRequestModel);
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, retrofit2.Response<CommonPOSTResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel model1 = response.body();
                    if (model1.getRES_ID() != null && model1.getRES_ID().equalsIgnoreCase("RES0000")) {
                    } else {
                    }
                } else {
                }
            }
            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }
}