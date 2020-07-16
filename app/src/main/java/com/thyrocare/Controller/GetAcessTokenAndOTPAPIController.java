package com.thyrocare.Controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.thyrocare.Retrofit.PostAPIInteface;
import com.thyrocare.Retrofit.RetroFit_APIClient;
import com.thyrocare.models.api.request.GetAccessTokenForOTPRequestModel;
import com.thyrocare.models.api.request.RequestOTPModel;
import com.thyrocare.models.api.response.CommonPOSTResponseModel;
import com.thyrocare.utils.app.Global;
import com.thyrocare.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.network.AbstractApiModel.B2C_API_VERSION;
import static com.thyrocare.network.AbstractApiModel.MAINURL;
import static com.thyrocare.network.AbstractApiModel.SERVER_BASE_API_URL;
import static com.thyrocare.utils.app.BundleConstants.API_FOR_OTP;


public class GetAcessTokenAndOTPAPIController {

    Activity mActivity;
    Global globalClass;
    private OnResponseListener onResponseListener;

    public GetAcessTokenAndOTPAPIController(Activity mActivity) {
        this.mActivity = mActivity;
        globalClass = new Global(mActivity);
        globalClass.setLoadingGIF(mActivity);

    }

    public void CallGetTokenAPIForOTP(final String MobileNumber, final String purpose, final String OTPto, final String OrderNo) {

        GetAccessTokenForOTPRequestModel accessTokenForOTPRequestModel = new GetAccessTokenForOTPRequestModel();
        accessTokenForOTPRequestModel.setAppId("5"); // 5 is for Btech
        accessTokenForOTPRequestModel.setPurpose("OTP");
        int appLevelVersionCode = 0;
        try {
            appLevelVersionCode = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        accessTokenForOTPRequestModel.setVersion(""+appLevelVersionCode);
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, B2C_API_VERSION).create(PostAPIInteface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.RequestForOTPTokenAPI(accessTokenForOTPRequestModel);
        globalClass.showProgressDialog();
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalClass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel model1 = response.body();
                    if (model1 != null && !InputUtils.isNull(model1.getRespId()) && model1.getRespId().equalsIgnoreCase("RES0000")){
                        RequestOTPModel model = new RequestOTPModel();
                        model.setApi_key(API_FOR_OTP);
                        model.setMobile(MobileNumber);
                        model.setType(purpose);
                        model.setOtpTo(OTPto);
                        model.setOrderno(OrderNo);
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
                globalClass.hideProgressDialog();
                globalClass.showCustomToast(mActivity, "Something went wrong. Please try after sometime.");
                onFailureResponseReceived();
            }
        });

    }

    private void CallGetOTPAPI(RequestOTPModel model) {
        globalClass.showCustomToast(mActivity, "Generating OTP. Please wait..");
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, SERVER_BASE_API_URL).create(PostAPIInteface.class);
        Call<CommonPOSTResponseModel> responseCall = apiInterface.RequestForOTPAPI(model);
        globalClass.showProgressDialog();
        responseCall.enqueue(new Callback<CommonPOSTResponseModel>() {
            @Override
            public void onResponse(Call<CommonPOSTResponseModel> call, Response<CommonPOSTResponseModel> response) {
                globalClass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    CommonPOSTResponseModel model1 = response.body();
                    if (onResponseListener != null) {
                        onResponseListener.onSuccess(model1);
                    }
                } else {
                    globalClass.showCustomToast(mActivity, "Unable to connect to the server. Please try after sometime.");
                    onFailureResponseReceived();
                }
            }

            @Override
            public void onFailure(Call<CommonPOSTResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog();
                globalClass.showCustomToast(mActivity, "Something went wrong. Please try after sometime.");

                onFailureResponseReceived();
            }
        });

    }

    private void onFailureResponseReceived() {
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
