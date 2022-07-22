package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.VenupunctureUploadRequestModel;
import com.thyrocare.btechapp.utils.app.Global;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenupunctureAPIController {
    Activity mActivity;
    ScanBarcodeWoeActivity scanBarcodeWoeActivity;
    Global globalClass;

    public VenupunctureAPIController(ScanBarcodeWoeActivity scanBarcodeWoeActivity) {
        this.mActivity = scanBarcodeWoeActivity;
        this.scanBarcodeWoeActivity = scanBarcodeWoeActivity;
        globalClass = new Global(mActivity);
    }

    public void CallAPI(final VenupunctureUploadRequestModel vrm) {

        globalClass = new Global(mActivity);
        globalClass.showProgressDialog(mActivity, "Please wait...", false);
        try {
            RequestBody OrderNO = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getORDERNO());
            RequestBody benID = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getBENID());
            RequestBody test = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getTEST());
            RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getTYPE());
            RequestBody appID = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getAPPID());

            MultipartBody.Part file = null;
            if (vrm.getFile() != null && vrm.getFile().exists()) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getFile());
                file = MultipartBody.Part.createFormData("file", vrm.getFile().getName(), requestFile);
            }
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<String> call = apiInterface.CallUploadVialAPI(OrderNO,benID,test,type,appID,file);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    globalClass.hideProgressDialog(mActivity);
                    if (response.isSuccessful() && response.body() != null) {
                        if (vrm.getTYPE().equalsIgnoreCase("VIAL")){
                            Toast.makeText(mActivity, "Vial image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "Affidavit uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (vrm.getTYPE().equalsIgnoreCase("VIAL")){
                            Toast.makeText(mActivity, "Failed to upload vial image, Please try again!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, "Failed to upload affidavit, Please try again!", Toast.LENGTH_SHORT).show();
                        }

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
