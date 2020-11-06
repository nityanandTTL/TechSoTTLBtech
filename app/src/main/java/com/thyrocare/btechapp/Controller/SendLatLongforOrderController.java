package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.google.gson.Gson;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class SendLatLongforOrderController {

    Activity activity;
    Gson gson;
    private OnResponseListener onResponseListener;
    private Global globalClass;
    private AppPreferenceManager appPreferenceManager;

    public SendLatLongforOrderController(Activity activity) {
        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        gson = new Gson();
    }

    public void SendLatlongToToServer(String Orderno ,int Status) {

        OrderAllocationTrackLocationRequestModel model = new OrderAllocationTrackLocationRequestModel();

        model.setVisitId(Orderno);
        model.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        model.setStatus(Status);

//Latlong added
        try {
            GPSTracker gpsTracker = new GPSTracker(activity);
            if (gpsTracker.canGetLocation()){
                model.setLatitude(String.valueOf(gpsTracker.getLatitude()));
                model.setLongitude(String.valueOf(gpsTracker.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.SendBtechLatlongToServer(model);
        globalClass.showProgressDialog(activity,activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog(activity);
                String strresponse = response.body();
                if (onResponseListener != null) {
                    onResponseListener.onSuccess(strresponse);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalClass.hideProgressDialog(activity);
                if (onResponseListener != null) {
                    onResponseListener.onfailure(t.getMessage());
                }
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }
    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public interface OnResponseListener {

        void onSuccess(String response);

        void onfailure(String msg);
    }

}
