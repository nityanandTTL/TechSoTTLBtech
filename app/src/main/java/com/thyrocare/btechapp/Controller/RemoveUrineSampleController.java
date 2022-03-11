package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ScanBarcodeViewPagerAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.OrderPickUpActivity;
import com.thyrocare.btechapp.models.api.request.PostPickupOrderRequestClass;
import com.thyrocare.btechapp.models.api.request.RemoveUrineReqModel;
import com.thyrocare.btechapp.models.api.response.PostPickupOrderResponseModel;
import com.thyrocare.btechapp.models.api.response.RemoveUrineSampleRespModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class RemoveUrineSampleController {

    Activity activity;
    ScanBarcodeViewPagerAdapter scanBarcodeViewPagerAdapter;
    int flag;
    private Global globalClass;
    AppPreferenceManager appPreferenceManager;

    public RemoveUrineSampleController(ScanBarcodeViewPagerAdapter scanBarcodeViewPagerAdapter, Activity activity) {
        this.activity = activity;
        globalClass=new Global(activity);
        this.scanBarcodeViewPagerAdapter = scanBarcodeViewPagerAdapter;
        flag = 1;
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public void CallAPI(RemoveUrineReqModel removeUrineReqModel, final BeneficiaryDetailsModel beneficiaryDetailsModel, final int i) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<RemoveUrineSampleRespModel> responseModelCall = apiInterface.removeUrineSample("Bearer " + appPreferenceManager.getLoginResponseModel().getAccess_token(),removeUrineReqModel);
            globalClass.showProgressDialog(activity, "Please wait..");
            responseModelCall.enqueue(new Callback<RemoveUrineSampleRespModel>() {
                @Override
                public void onResponse(Call<RemoveUrineSampleRespModel> call, retrofit2.Response<RemoveUrineSampleRespModel> response) {
                    try {
                        globalClass.hideProgressDialog(activity);
                        if (response.isSuccessful() && response.body()!=null){
                            if (flag==1){
                                if (i == 1){
                                    scanBarcodeViewPagerAdapter.getResponse(response.body(),beneficiaryDetailsModel);
                                }
                            }
                        }else{
                            TastyToast.makeText(activity,response.body().getResponse().toString().trim(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RemoveUrineSampleRespModel> call, Throwable t) {
                    globalClass.hideProgressDialog(activity);
                    Global.showCustomStaticToast(activity, ConstantsMessages.SOMETHING_WENT_WRONG);
                }
            });

        } catch (Exception e) {
            globalClass.hideProgressDialog(activity);
            Global.showCustomStaticToast(activity, ConstantsMessages.SOMETHING_WENT_WRONG);
            e.printStackTrace();
        }
    }

}
