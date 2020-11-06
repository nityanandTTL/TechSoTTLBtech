package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.StockAvailabilityActivity;
import com.thyrocare.btechapp.models.api.request.StockAvailabilityRequestModel;
import com.thyrocare.btechapp.models.api.response.MainMaterialModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;

public class GetAvailableStockController {
    private static final String TAG = GetAvailableStockController.class.getSimpleName();
    private int flag;
    private Activity mActivity;
    private RequestQueue requestQueue;
    private StockAvailabilityActivity stockAvailabilityActivity;
    private Global globalClass;

    public GetAvailableStockController(StockAvailabilityActivity stockAvailabilityActivity, Activity activity) {
        this.stockAvailabilityActivity = stockAvailabilityActivity;
        this.mActivity = activity;
        globalClass = new Global(mActivity);
        flag = 0;
    }

    public void getAvailableStock(StockAvailabilityRequestModel model) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<MainMaterialModel> responseCall = apiInterface.CallStackAvailabilityAPI(model);
        globalClass.showProgressDialog(mActivity, PLEASE_WAIT);
        responseCall.enqueue(new Callback<MainMaterialModel>() {
            @Override
            public void onResponse(Call<MainMaterialModel> call, retrofit2.Response<MainMaterialModel> res) {
                globalClass.hideProgressDialog(mActivity);
                if (res.isSuccessful() && res.body() != null) {
                    if (flag == 0)
                        stockAvailabilityActivity.getStockResponse(res.body());
                }else{
                    Toast.makeText(mActivity,SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<MainMaterialModel> call, Throwable t) {
                globalClass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity,SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
            }
        });
    }
}