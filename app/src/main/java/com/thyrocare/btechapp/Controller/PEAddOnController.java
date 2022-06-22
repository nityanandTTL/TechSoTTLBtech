package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.AddONRequestModel;
import com.thyrocare.btechapp.models.api.response.AddOnErrorResponsemodel;
import com.thyrocare.btechapp.models.api.response.AddOnResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

public class PEAddOnController {
    Activity mactivity;
    Global globalClass;
    AddEditBenificaryActivity addEditBenificaryActivity;
    AppPreferenceManager appPreferenceManager;

    public PEAddOnController(AddEditBenificaryActivity activity) {
        this.mactivity = activity;
        this.addEditBenificaryActivity = activity;
        this.mactivity = addEditBenificaryActivity;
        globalClass = new Global(mactivity);
        appPreferenceManager = new AppPreferenceManager(mactivity);
    }

    public void getAddOnOrder(String orderID, final AddONRequestModel addONRequestModel, final int peAddben) {
        try {
            globalClass.showProgressDialog(mactivity, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(mactivity, EncryptionUtils.Dcrp_Hex(mactivity.getString(R.string.PE_API))).create(PostAPIInterface.class);
            Call<AddOnResponseModel> peAuthResponseModelCall = postAPIInterface.getAddOnOrder(orderID, addONRequestModel);
            peAuthResponseModelCall.enqueue(new Callback<AddOnResponseModel>() {
                @Override
                public void onResponse(Call<AddOnResponseModel> call, retrofit2.Response<AddOnResponseModel> response) {
                    try {
                        AddOnResponseModel add = response.body();
                        if (add!=null&&!InputUtils.isNull(add.isStatus()) && add.isStatus()) {
                            boolean peaddON  ;
                            if (peAddben == 1) {
                                peaddON = true;
//                                BundleConstants.isPEPartner =false;
                                appPreferenceManager.setPE_Partner(false);
                                BundleConstants.callOTPFlag = 0;
                                TastyToast.makeText(mactivity, ConstantsMessages.BenefiaryAdded, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                Intent intent = new Intent(mactivity, B2BVisitOrdersDisplayFragment.class);
                                intent.putExtra(BundleConstants.PEADDON,peaddON);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mactivity.startActivity(intent);
                            } else {
                                TastyToast.makeText(mactivity, ConstantsMessages.BenefiaryAdded, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                addEditBenificaryActivity.setResult(Activity.RESULT_OK);
                                addEditBenificaryActivity.finish();
                            }
                        } else {
                            Gson gson = new GsonBuilder().create();
                            AddOnErrorResponsemodel mError=new AddOnErrorResponsemodel();
                            try {
                                mError= gson.fromJson(response.errorBody().string(),AddOnErrorResponsemodel.class);
                                TastyToast.makeText(mactivity, mError.getError() != null ? mError.getError().trim() : ConstantsMessages.SomethingWentwrngMsg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                globalClass.hideProgressDialog(mactivity);
                            } catch (IOException e) {
                                e.printStackTrace();// handle failure to read error
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        globalClass.hideProgressDialog(mactivity);
                        TastyToast.makeText(mactivity,"Something went wrong",TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    }
                }
                @Override
                public void onFailure(Call<AddOnResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(mactivity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(mactivity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            globalClass.hideProgressDialog(mactivity);
        }
    }
}

