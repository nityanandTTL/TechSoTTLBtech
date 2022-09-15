package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.PE_PostPatientDetailsActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.GetPE_PostCheckOutPatientModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PE_PostPatientDetailsController {
    PE_PostPatientDetailsActivity pe_postPatientDetailsActivity;
    AppInterfaces.getPostPatientDetailsResponse getPostPatientDetailsResponse;
    AppInterfaces.getBenList getBenList;

    public PE_PostPatientDetailsController(PE_PostPatientDetailsActivity pe_postPatientDetailsActivity, AppInterfaces.getPostPatientDetailsResponse getPostPatientDetailsResponse) {
        this.pe_postPatientDetailsActivity = pe_postPatientDetailsActivity;
       this.getPostPatientDetailsResponse = getPostPatientDetailsResponse;
    }

    public PE_PostPatientDetailsController(PE_PostPatientDetailsActivity pe_postPatientDetailsActivity, AppInterfaces.getBenList getBenList) {
        this.pe_postPatientDetailsActivity = pe_postPatientDetailsActivity;
        this.getBenList = getBenList;
    }

    public void callPostcheckoutDetails() {
        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(pe_postPatientDetailsActivity, EncryptionUtils.Dcrp_Hex(pe_postPatientDetailsActivity.getString(R.string.PE_API))).create(GetAPIInterface.class);
        Call<GetPE_PostCheckOutPatientModel> call = getAPIInterface.getPostCheckoutPatientList("","")  ;
        call.enqueue(new Callback<GetPE_PostCheckOutPatientModel>() {
            @Override
            public void onResponse(Call<GetPE_PostCheckOutPatientModel> call, Response<GetPE_PostCheckOutPatientModel> response) {
                getBenList.getBeneficiaryList(response.body());
            }

            @Override
            public void onFailure(Call<GetPE_PostCheckOutPatientModel> call, Throwable t) {

            }
        });
    }
}
