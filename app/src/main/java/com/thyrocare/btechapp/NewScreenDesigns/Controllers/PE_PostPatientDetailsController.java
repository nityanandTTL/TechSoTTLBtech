package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;

import android.text.TextUtils;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.PE_PostPatientDetailsActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.AddPatientResponseModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PE_PostPatientDetailsController {
    PE_PostPatientDetailsActivity pe_postPatientDetailsActivity;
    AppInterfaces.getPostPatientDetailsResponse getPostPatientDetailsResponse;
    AppInterfaces.getBenList getBenList;
    Global globalclass;
    AppPreferenceManager appPreferenceManager;
    AddPatientResponseModel addPatientResponseModel = new AddPatientResponseModel();

    public PE_PostPatientDetailsController(PE_PostPatientDetailsActivity pe_postPatientDetailsActivity, AppInterfaces.getPostPatientDetailsResponse getPostPatientDetailsResponse) {
        this.pe_postPatientDetailsActivity = pe_postPatientDetailsActivity;
        this.getPostPatientDetailsResponse = getPostPatientDetailsResponse;
    }

    public PE_PostPatientDetailsController(PE_PostPatientDetailsActivity pe_postPatientDetailsActivity, AppInterfaces.getBenList getBenList) {
        this.pe_postPatientDetailsActivity = pe_postPatientDetailsActivity;
        this.getBenList = getBenList;
        appPreferenceManager = new AppPreferenceManager(pe_postPatientDetailsActivity);
    }

    public PE_PostPatientDetailsController(PE_PostPatientDetailsActivity pe_postPatientDetailsActivity) {
        this.pe_postPatientDetailsActivity = pe_postPatientDetailsActivity;
        globalclass = new Global(pe_postPatientDetailsActivity);
        appPreferenceManager = new AppPreferenceManager(pe_postPatientDetailsActivity);
    }

    public void callPostcheckoutDetails(String visitId) {
        globalclass.showProgressDialog(pe_postPatientDetailsActivity, "Fetching patient's list, please wait...");
        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(pe_postPatientDetailsActivity, EncryptionUtils.Dcrp_Hex(pe_postPatientDetailsActivity.getString(R.string.PE_API))).create(GetAPIInterface.class);
        Call<GetPatientListResponseModel> call = getAPIInterface.getPostCheckoutPatientList(appPreferenceManager.getAccess_Token(), visitId);
        call.enqueue(new Callback<GetPatientListResponseModel>() {
            @Override
            public void onResponse(Call<GetPatientListResponseModel> call, Response<GetPatientListResponseModel> response) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                if (response.isSuccessful() && response.body().getData() != null) {
                    GetPatientListResponseModel responseModel = response.body();
                    getBenList.getBeneficiaryList(responseModel);
                } else {
                    TastyToast.makeText(pe_postPatientDetailsActivity, "Failed to get the patient's list", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }

            }

            @Override
            public void onFailure(Call<GetPatientListResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                TastyToast.makeText(pe_postPatientDetailsActivity, ConstantsMessages.SomethingWentwrngMsg, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }

    public void callSendOTPAPI(SendOTPRequestModel otpRequestModel, OrderVisitDetailsModel orderVisitDetailsModel) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(pe_postPatientDetailsActivity, EncryptionUtils.Dcrp_Hex(pe_postPatientDetailsActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(otpRequestModel);
        globalclass.showProgressDialog(pe_postPatientDetailsActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
//
                        pe_postPatientDetailsActivity.ShowDialogToVerifyOTP(orderVisitDetailsModel);
                    } else {
                        globalclass.showCustomToast(pe_postPatientDetailsActivity, "OTP Generation Failed.");
                    }
                } else {
                    globalclass.showCustomToast(pe_postPatientDetailsActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel2> call, Throwable t) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                globalclass.showCustomToast(pe_postPatientDetailsActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    public void callAddPatient(JSONObject jsonObject, String visitId) {

        globalclass.showProgressDialog(pe_postPatientDetailsActivity, "Please wait while processing your request...");
        PostAPIInterface appInterfaces = RetroFit_APIClient.getInstance().getClient(pe_postPatientDetailsActivity, EncryptionUtils.Dcrp_Hex(pe_postPatientDetailsActivity.getString(R.string.PE_API))).create(PostAPIInterface.class);
        Call<AddPatientResponseModel> call = appInterfaces.addNewPatient(jsonObject, appPreferenceManager.getAuthToken(), visitId);
        call.enqueue(new Callback<AddPatientResponseModel>() {
            @Override
            public void onResponse(Call<AddPatientResponseModel> call, Response<AddPatientResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                    addPatientResponseModel = response.body();
                    pe_postPatientDetailsActivity.onAddPatientResponseReceived(addPatientResponseModel);
                } else {
                    globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                    TastyToast.makeText(pe_postPatientDetailsActivity, addPatientResponseModel.error, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<AddPatientResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                TastyToast.makeText(pe_postPatientDetailsActivity, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });
    }

    public void callEditPatient(JSONObject jsonObject, String visitId) {
        globalclass.showProgressDialog(pe_postPatientDetailsActivity, "Please wait while processing your request...");
        PostAPIInterface appInterfaces = RetroFit_APIClient.getInstance().getClient(pe_postPatientDetailsActivity, EncryptionUtils.Dcrp_Hex(pe_postPatientDetailsActivity.getString(R.string.PE_API))).create(PostAPIInterface.class);
        Call<AddPatientResponseModel> call = appInterfaces.editPatient(jsonObject, appPreferenceManager.getAuthToken(), visitId);
        call.enqueue(new Callback<AddPatientResponseModel>() {
            @Override
            public void onResponse(Call<AddPatientResponseModel> call, Response<AddPatientResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                    addPatientResponseModel = response.body();
                    pe_postPatientDetailsActivity.onEditPatientResponse(addPatientResponseModel);
                } else {
                    globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                    TastyToast.makeText(pe_postPatientDetailsActivity, addPatientResponseModel.error, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<AddPatientResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(pe_postPatientDetailsActivity);
                TastyToast.makeText(pe_postPatientDetailsActivity, "Something went wrong", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
            }
        });

    }
}
