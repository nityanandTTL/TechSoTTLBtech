package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.PEQrCodeResponse;
import com.thyrocare.btechapp.models.api.response.PEVerifyQRResponseModel;
import com.thyrocare.btechapp.models.api.response.UpdatePaymentResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PEQrCodeController {
    private Activity activity;
    private Global global;

    public PEQrCodeController(Activity activity) {
        this.activity = activity;
        this.global = new Global(activity);
    }


    public void callPEQrcodeApi(String orderID, String accessToken, AppInterfaces.getPEQRApiResponse getPEQRApiResponseInterface) {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
        Call<PEQrCodeResponse> responseCall = getAPIInterface.getPEQRCode(orderID, accessToken);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<PEQrCodeResponse>() {
            @Override
            public void onResponse(Call<PEQrCodeResponse> call, Response<PEQrCodeResponse> response) {
                global.hideProgressDialog(activity);
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        throw new Exception("response is unsuccessful or response body is null");
                    }
                    if (response.body().getData() == null || InputUtils.isNull(response.body().getData().image)) {
                        throw new Exception("Qr code is null or empty");
                    }
                    getPEQRApiResponseInterface.onResponse(response.body());
                } catch (Exception e) {
                    Toast.makeText(activity, "Failed to load QR Code", Toast.LENGTH_SHORT).show();
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<PEQrCodeResponse> call, Throwable t) {
                global.hideProgressDialog(activity);
                Log.e("Exception", t.toString());
                Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void callPEverifyQR(String orderID, AppInterfaces.getPEQRVerifyApiResponse peVerifyQRInterface) {
        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.PE_API))).create(GetAPIInterface.class);
        Call<PEVerifyQRResponseModel> responseCall = getAPIInterface.peVerifyQR(orderID);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<PEVerifyQRResponseModel>() {
            @Override
            public void onResponse(Call<PEVerifyQRResponseModel> call, Response<PEVerifyQRResponseModel> response) {
                global.hideProgressDialog(activity);
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        throw new Exception("response is unsuccessful or response body is null");
                    }
                    if (InputUtils.isNull(response.body().getData())) {
                        throw new Exception("response data is null");
                    }
                    peVerifyQRInterface.onResponse(response.body());
                } catch (Exception e) {
                    Toast.makeText(activity, "Failed to verify Payment, Please try again", Toast.LENGTH_SHORT).show();
                    Log.e("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<PEVerifyQRResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                Log.e("Exception", t.toString());
                Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePayment(String orderID, AppInterfaces.getUpdatePaymentResponse getUpdatePaymentResponse) {
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<UpdatePaymentResponseModel> responseCall = postAPIInterface.updatePayment(orderID);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<UpdatePaymentResponseModel>() {
            @Override
            public void onResponse(Call<UpdatePaymentResponseModel> call, Response<UpdatePaymentResponseModel> response) {
                global.hideProgressDialog(activity);
                try {
                    if (!response.isSuccessful() || response.body() == null) {
                        throw new Exception("response is unsuccessful or response body is null");
                    }
                    if (InputUtils.isNull(response.body().getResponse())) {
                        throw new Exception("response data is null");
                    }
                    getUpdatePaymentResponse.onResponse(response.body());
                } catch (Exception e) {
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<UpdatePaymentResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                Log.e("Exception", t.toString());
                Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
