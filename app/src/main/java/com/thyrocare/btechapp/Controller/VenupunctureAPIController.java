package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.fragment.HubMasterBarcodeScanFragment;
import com.thyrocare.btechapp.models.api.request.VenupunctureUploadRequestModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenupunctureAPIController {
    Activity mActivity;
    ScanBarcodeWoeActivity scanBarcodeWoeActivity;
    HubMasterBarcodeScanFragment hubMasterBarcodeScanFragment;
    Global globalClass;


    public VenupunctureAPIController(ScanBarcodeWoeActivity scanBarcodeWoeActivity) {
        this.mActivity = scanBarcodeWoeActivity;
        this.scanBarcodeWoeActivity = scanBarcodeWoeActivity;
        globalClass = new Global(mActivity);
    }

    public VenupunctureAPIController(Activity activity, HubMasterBarcodeScanFragment hubMasterBarcodeScanFragment, VenupunctureUploadRequestModel vrm) {
        this.mActivity = activity;
        this.hubMasterBarcodeScanFragment = hubMasterBarcodeScanFragment;
        globalClass = new Global(activity);
    }

    public void CallAPI(final VenupunctureUploadRequestModel vrm) {

        globalClass = new Global(mActivity);
        globalClass.showProgressDialog(mActivity, "Please wait...", false);
        try {
            RequestBody OrderNO = null;
            RequestBody appID = null;
            RequestBody test = null;
            if (scanBarcodeWoeActivity != null) {
                OrderNO = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getORDERNO());
                appID = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getAPPID());
                test = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getTEST());
            }
            RequestBody benID = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getBENID());
            RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getTYPE());

            MultipartBody.Part file = null;
            if (vrm.getFile() != null && vrm.getFile().exists()) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), vrm.getFile());
                file = MultipartBody.Part.createFormData("file", vrm.getFile().getName(), requestFile);
            }
            Log.i("ORDERNO", vrm.getORDERNO());
            Log.i("BENID", vrm.getBENID());
            Log.i("TEST", vrm.getTEST());
            Log.i("TYPE", vrm.getTYPE());
            Log.i("APPID", vrm.getAPPID());
            Log.i("FILE", vrm.getFile().getPath());

            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<String> call = (scanBarcodeWoeActivity != null) ? apiInterface.CallUploadVialAPI(OrderNO, benID, test, type, appID, file) : apiInterface.CallUploadSelfieHubAPI(benID, type, file);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    globalClass.hideProgressDialog(mActivity);
                    showToastBasedOnType(response, vrm);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    globalClass.hideProgressDialog(mActivity);
                    Toast.makeText(mActivity, "Something went wrong, Please try again!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            globalClass.hideProgressDialog(mActivity);
        }
    }

    private void showToastBasedOnType(Response<String> response, VenupunctureUploadRequestModel vrm) {
        if (vrm.getTYPE().equalsIgnoreCase("HUB_DROP")) {
            showToastForHubDrop(response, vrm);
        } else {
            showToastForVial(response, vrm);
        }
    }

    private void showToastForHubDrop(Response<String> response, VenupunctureUploadRequestModel vrm) {
        // if response is successful
        if (response.isSuccessful() && response.body() != null && InputUtils.CheckEqualIgnoreCase(response.body(), "\"BLOB UPLOADED\"")) {
            hubMasterBarcodeScanFragment.UpdateDispatchBtn();
            Toast.makeText(mActivity, "Selfie Uploaded Successfully", Toast.LENGTH_SHORT).show();
            return;
        }
        //if response is unsuccessful
        Toast.makeText(mActivity, "Failed to Upload Selfie, Please try again", Toast.LENGTH_SHORT).show();
    }

    private void showToastForVial(Response<String> response, VenupunctureUploadRequestModel vrm) {
        if (response.isSuccessful() && response.body() != null) {
            if (vrm.getTYPE().equalsIgnoreCase("VIAL")) {
                Toast.makeText(mActivity, "Vial image uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "Affidavit uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (vrm.getTYPE().equalsIgnoreCase("VIAL")) {
                Toast.makeText(mActivity, "Failed to upload vial image, Please try again!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "Failed to upload affidavit, Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
