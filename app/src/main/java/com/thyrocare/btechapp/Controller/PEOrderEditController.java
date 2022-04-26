package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.PEOrderEditRequestModel;
import com.thyrocare.btechapp.models.api.request.PEUpdatePatientRequestModel;
import com.thyrocare.btechapp.models.api.response.PEOrderEditResponseModel;
import com.thyrocare.btechapp.models.api.response.PEUpdatePatientResponseModel;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class PEOrderEditController {
    Activity activity;
    AddEditBenificaryActivity addEditBenificaryActivity;
    StartAndArriveActivity startAndArriveActivity;
    Global globalClass;

    public PEOrderEditController(AddEditBenificaryActivity addEditBenificaryActivity) {
        this.activity = addEditBenificaryActivity;
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        globalClass = new Global(activity);
    }

    public PEOrderEditController(StartAndArriveActivity startAndArriveActivity) {
        this.activity = startAndArriveActivity;
        this.startAndArriveActivity = startAndArriveActivity;
        globalClass = new Global(activity);
    }

    public void postPEUpdatePatient(PEUpdatePatientRequestModel peUpdatePatientRequestModel, final int i) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PEUpdatePatientResponseModel> peOrderEditResponseModelCall = postAPIInteface.postPEUpdatePatient(peUpdatePatientRequestModel);
            peOrderEditResponseModelCall.enqueue(new Callback<PEUpdatePatientResponseModel>() {
                @Override
                public void onResponse(Call<PEUpdatePatientResponseModel> call, retrofit2.Response<PEUpdatePatientResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PEUpdatePatientResponseModel peUpdatePatientResponseModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(AppConstants.SUCCESS_MSG, peUpdatePatientResponseModel.getResponse())) {
                                if (i==1){
                                    TastyToast.makeText(activity, peUpdatePatientResponseModel.getMessage().toString() != null ? peUpdatePatientResponseModel.getMessage().toString() : "Beneficiary edited successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    addEditBenificaryActivity.pePatientDetailsUpdated();
                                }
                            } else {
                                TastyToast.makeText(activity, peUpdatePatientResponseModel.getResponse().toString().trim(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PEUpdatePatientResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void postPEOrderEdit(final String action, PEOrderEditRequestModel peOrderEditRequestModel, final int i) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PEOrderEditResponseModel> peOrderEditResponseModelCall = postAPIInteface.postPEOrderEdit(peOrderEditRequestModel);
            peOrderEditResponseModelCall.enqueue(new Callback<PEOrderEditResponseModel>() {
                @Override
                public void onResponse(Call<PEOrderEditResponseModel> call, retrofit2.Response<PEOrderEditResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            PEOrderEditResponseModel peOrderEditResponseModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(peOrderEditResponseModel.getResponse(), AppConstants.SUCCESS_MSG)){
                                BundleConstants.setPEOrderEdit = true;
                                TastyToast.makeText(activity,peOrderEditResponseModel.getMessage().toString()!=null?peOrderEditResponseModel.getMessage().toString().trim():"Beneficiary edited successfully",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                               if (action.equalsIgnoreCase("DELETE")){
                                   startAndArriveActivity.pePatientDetailsUpdated();
                               }else{
//                                   if (i==1){
                                       addEditBenificaryActivity.pePatientDetailsUpdated();
//                                   }
                               }

                            }else{
                                TastyToast.makeText(activity,peOrderEditResponseModel.getResponse().toString()!=null?peOrderEditResponseModel.getResponse().toString().trim():SomethingWentwrngMsg,TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            }
                        } else {
                            globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PEOrderEditResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
