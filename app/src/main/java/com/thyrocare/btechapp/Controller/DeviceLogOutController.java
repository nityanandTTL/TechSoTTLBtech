package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.Post_DeviceID;
import com.thyrocare.btechapp.models.api.response.LoginDeviceResponseModel;


import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.INVALID_LOG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by E5233 on 4/30/2018.
 */

public class DeviceLogOutController {

    Context mContext;
    private Activity activity;
    private Global global;

    public DeviceLogOutController(Context activity) {
        this.mContext = activity;
    }

    public DeviceLogOutController(Activity activity) {
        this.activity = activity;
        this.mContext = activity;
        global = new Global(activity);
    }

    public void CallLogOutDevice(String UserId, String device_id) {

        LogoutRequestModel model = new LogoutRequestModel();
        model.setUserId("" + UserId);
        model.setDeviceId("" + device_id);
        if (isNetworkAvailable(mContext)) {
            CallLogoutRequestApi(model);
        } else {
//            Toast.makeText(mContext, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
        }
    }

    public void CallLogoutRequestApi(LogoutRequestModel model) {

        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel> commonResponeModelCall = postAPIInterface.CallLogoutAPI(model);
        commonResponeModelCall.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                CommonResponseModel commonResponseModel = response.body();
            }

            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {

            }
        });
    }

    public void CallLogInDevice(String userID, String device_id) {
        Post_DeviceID n = new Post_DeviceID();
        n.setUserId("" + userID);
        n.setDeviceId("" + device_id);

        if (isNetworkAvailable(mContext)) {
            CallDeviceIDLoginAPI(n);
        } else {
//            Toast.makeText(mContext, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
        }
    }

    private void CallDeviceIDLoginAPI(Post_DeviceID post_deviceID) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<LoginDeviceResponseModel> responseCall = apiInterface.PostLoginUserDeviceAPI(post_deviceID);
        responseCall.enqueue(new Callback<LoginDeviceResponseModel>() {
            @Override
            public void onResponse(Call<LoginDeviceResponseModel> call, Response<LoginDeviceResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginDeviceResponseModel model = response.body();
                    if (model.getRespId() == 1) {
                        //                        mLoginScreenActivity.setLoginDeviceResponse();
                    } else {
                        TastyToast.makeText(activity, !InputUtils.isNull(model.getRespMessage()) ? model.getRespMessage() : SomethingWentwrngMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }


                } else {
                    global.showCustomToast(activity, INVALID_LOG, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<LoginDeviceResponseModel> call, Throwable t) {
                global.showCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });

    }
}