package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetAccessTokenForOTPRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.RequestOTPModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.utils.app.BundleConstants.API_FOR_OTP;


public class AccessTokenAndOTPAPIController {

    private final SharedPreferences pref;
    Activity mActivity;
    Global globalClass;
    private OnResponseListener onResponseListener;

    public AccessTokenAndOTPAPIController(Activity mActivity) {
        this.mActivity = mActivity;
        globalClass = new Global(mActivity);
        pref = mActivity.getSharedPreferences("domain", 0);
    }

    public void CallGetTokenAPIForOTP(final String MobileNumber, final String purpose, final String OTP) {

        GetAccessTokenForOTPRequestModel accessTokenForOTPRequestModel = new GetAccessTokenForOTPRequestModel();
        accessTokenForOTPRequestModel.setAppId(Constants.BtechAppID_FOROTP);
        accessTokenForOTPRequestModel.setPurpose("OTP");
        int appLevelVersionCode = 0;
        try {
            appLevelVersionCode = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        accessTokenForOTPRequestModel.setVersion("" + appLevelVersionCode);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.RequestForOTPTokenAPI(accessTokenForOTPRequestModel);
        globalClass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalClass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel model1 = response.body();
                    if (!InputUtils.isNull(model1.getRespId()) && model1.getRespId().equalsIgnoreCase("RES0000")){
                        RequestOTPModel model = new RequestOTPModel();
                        model.setApi_key(API_FOR_OTP);
                        model.setMobile(MobileNumber);
                        model.setType(purpose);
                        if (!InputUtils.isNull(OTP)) {
                            model.setOtpno(OTP);
                        }

                        model.setAccessToken(model1.getToken());
                        model.setReqId(model1.getRequestId());
                        CallGetOTPAPI(model);
                    }else{
                        onFailureResponseReceived();
                    }
                } else {
                    globalClass.showCustomToast(mActivity, "Unable to connect to the server. Please try after sometime.");
                    onFailureResponseReceived();
                }
            }
            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog(mActivity);
                globalClass.showCustomToast(mActivity, "Something went wrong. Please try after sometime.");
                onFailureResponseReceived();
            }
        });

    }

    private void CallGetOTPAPI(RequestOTPModel model) {
        globalClass.showCustomToast(mActivity, "Sending OTP please wait..");


        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.CallGenerateOTPAPI(model);
        globalClass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalClass.hideProgressDialog(mActivity);

                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel responseModel = response.body();
                    if (onResponseListener != null) {
                        onResponseListener.onSuccess(responseModel);
                    } else {
                        onFailureResponseReceived();
                    }
                } else {
                    onFailureResponseReceived();
                }
            }

            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog(mActivity);
                onFailureResponseReceived();
            }
        });
    }

    private void onFailureResponseReceived() {

        Global.showCustomStaticToast(mActivity, ConstantsMessages.OTP_GENERATION_FAILED);
        if (onResponseListener != null) {
            CommonPOSTResponseModel failureModel = new CommonPOSTResponseModel();
            failureModel.setRES_ID("RESXXXX");
            failureModel.setRESID1("RESXXXX");
            failureModel.setRespId("RESXXXX");
            onResponseListener.onfailure(failureModel);
        }
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }


    public interface OnResponseListener {

        void onSuccess(CommonPOSTResponseModel commonPOSTResponseModel);

        void onfailure(CommonPOSTResponseModel commonPOSTResponseModel);
    }
}
