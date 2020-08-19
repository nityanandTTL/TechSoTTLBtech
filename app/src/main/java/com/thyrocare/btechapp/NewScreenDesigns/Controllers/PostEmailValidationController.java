package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import android.app.Activity;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.EmailVaildationPostModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.EmailValidationResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;



public class PostEmailValidationController {


    Activity mContext;
    Global globalClass;
    OnResponseListener onResponseListener;

    public PostEmailValidationController(Activity mContext) {
        this.mContext = mContext;
        globalClass = new Global(mContext);

    }

    public void CallEmailValdationAPI(String EmailID) {

        EmailVaildationPostModel model = new EmailVaildationPostModel();
        model.setAppID("6");   // This App Id is for Leggy T
        model.setEmailID(EmailID);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, EncryptionUtils.DecodeString64(mContext.getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
        Call<EmailValidationResponseModel> responseCall = apiInterface.PostEmailValidationAPI(model);

        globalClass.showProgressDialog();

        responseCall.enqueue(new Callback<EmailValidationResponseModel>() {
            @Override
            public void onResponse(Call<EmailValidationResponseModel> call, Response<EmailValidationResponseModel> response) {
                globalClass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {

                    EmailValidationResponseModel emailValidationResponseModel = response.body();
                    boolean isEmailValid = false;
                    if (emailValidationResponseModel.getSucess() != null && emailValidationResponseModel.getSucess().equalsIgnoreCase("TRUE")) {
                        isEmailValid = true;
                    }
                    if (onResponseListener != null) {
                        onResponseListener.onSuccess(isEmailValid);
                    }

                } else {
                    globalClass.showcenterCustomToast(mContext, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<EmailValidationResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog();
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

        void onSuccess(boolean isEmailValid);

        void onfailure(String msg);
    }

}
