package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.CheckoutWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.OrderPickUpActivity;
import com.thyrocare.btechapp.models.api.request.PickupOrderRequestModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionReqModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionRespModel;
import com.thyrocare.btechapp.models.api.response.PickupOrderResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class GetPickupOrderController {

    Activity activity;
    OrderPickUpActivity orderPickUpActivity;
    int flag;
    private Global globalClass;

    public GetPickupOrderController(OrderPickUpActivity orderPickUpActivity) {
        this.activity = orderPickUpActivity;
        globalClass = new Global(activity);
        this.orderPickUpActivity = orderPickUpActivity;
        flag = 1;
    }


    public void PickupOrderAPI(PickupOrderRequestModel pickupOrderRequestModel) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PickupOrderResponseModel> responseModelCall = apiInterface.getPickupOrder(pickupOrderRequestModel);
            globalClass.showProgressDialog(activity, "Please wait..");
            responseModelCall.enqueue(new Callback<PickupOrderResponseModel>() {
                @Override
                public void onResponse(Call<PickupOrderResponseModel> call, retrofit2.Response<PickupOrderResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    if (response.isSuccessful() && response.body() != null) {
                        if (flag == 1) {
                            orderPickUpActivity.getPickupOrderList(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PickupOrderResponseModel> call, Throwable t) {
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
