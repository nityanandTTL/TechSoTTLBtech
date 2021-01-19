package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.UpdateStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.UpdateMaterialActivity;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;

public class UpdateStockController {
    private static final String TAG = UpdateStockController.class.getSimpleName();
    private Activity mActivity;
    private int flag;
    private RequestQueue requestQueue;
    private UpdateMaterialActivity bmc_updateMaterialActivity;
    Global globalClass;

    public UpdateStockController(UpdateMaterialActivity updateMaterialActivity, Activity activity) {
        this.bmc_updateMaterialActivity = updateMaterialActivity;
        this.mActivity = activity;
        globalClass = new Global(mActivity);
        flag = 0;
    }

    public void UpdateAvailableStock(UpdateStockModel updateStockModel) {

        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonResponseModel> commonResponseModelCall = postAPIInterface.updateStock(updateStockModel);
        globalClass = new Global(mActivity);
        globalClass.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.updatingStock), false);
        commonResponseModelCall.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, retrofit2.Response<CommonResponseModel> response) {
                globalClass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null){
                    CommonResponseModel commonResponseModel = response.body();
                    if (flag == 0){
                        bmc_updateMaterialActivity.getUpdatedResponse(commonResponseModel);
                    }
                }else{
                    globalClass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog(mActivity);
                globalClass.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                MessageLogger.LogDebug(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}