package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Leave_intimation_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.DSAProductsResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class GetDSAProductsController {
    Activity activity;
    AddEditBenificaryActivity addEditBenificaryActivity;
    Global globalClass;
    AppPreferenceManager appPreferenceManager;

    public GetDSAProductsController(AddEditBenificaryActivity addEditBenificaryActivity) {
        this.activity = addEditBenificaryActivity;
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        globalClass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public void getDSAProducts(String orderNo) {
        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<DSAProductsResponseModel> responseCall = getAPIInterface.getDSAProducts(orderNo);
            responseCall.enqueue(new Callback<DSAProductsResponseModel>() {
                @Override
                public void onResponse(Call<DSAProductsResponseModel> call, Response<DSAProductsResponseModel> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        globalClass.hideProgressDialog(activity);
                        DSAProductsResponseModel dsaProductsResponseModel = response.body();
                        appPreferenceManager.setDSAProductResponseModel(dsaProductsResponseModel);
                        addEditBenificaryActivity.getDSAProducts(dsaProductsResponseModel);
                    } else {
                        globalClass.hideProgressDialog(activity);
                        globalClass.showCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<DSAProductsResponseModel> call, Throwable t) {
                    MessageLogger.LogDebug("Errror", t.getMessage());
                    globalClass.hideProgressDialog(activity);
                    globalClass.showCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}