package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.BTechSelfieRequestModel;
import com.thyrocare.btechapp.utils.app.Global;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSelfieWOEController {
    Activity mActivity;
    CheckoutWoeActivity checkoutWoeActivity;
    Global globalClass;

    public UploadSelfieWOEController(CheckoutWoeActivity checkoutWoeActivity, Activity activity) {
        this.mActivity = activity;
        this.checkoutWoeActivity = checkoutWoeActivity;
        globalClass = new Global(activity);
    }

    public void CallAPI(String RequestToken, BTechSelfieRequestModel bTechSelfieRequestModel) {

        globalClass = new Global(mActivity);
        globalClass.showProgressDialog(mActivity, "Please wait...", false);

        try {
            RequestBody BtechID = RequestBody.create(MediaType.parse("multipart/form-data"), bTechSelfieRequestModel.getBtechid());
            RequestBody OrderNO = RequestBody.create(MediaType.parse("multipart/form-data"), bTechSelfieRequestModel.getORDERNO());

            MultipartBody.Part file = null;
            if (bTechSelfieRequestModel.getFile() != null && bTechSelfieRequestModel.getFile().exists()) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bTechSelfieRequestModel.getFile());
                file = MultipartBody.Part.createFormData("file", bTechSelfieRequestModel.getFile().getName(), requestFile);
            }

            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<String> call = apiInterface.CallUploadSelfieAPI("Bearer " + RequestToken, BtechID, OrderNO, file);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    globalClass.hideProgressDialog(mActivity);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(mActivity, "Selfie uploaded successfully", Toast.LENGTH_SHORT).show();
                        checkoutWoeActivity.getSelfieResponse();
                    } else {
                        Toast.makeText(mActivity, "Failed to upload selfie, Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    globalClass.hideProgressDialog(mActivity);
                    Toast.makeText(mActivity, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            globalClass.hideProgressDialog(mActivity);
        }
    }
}
