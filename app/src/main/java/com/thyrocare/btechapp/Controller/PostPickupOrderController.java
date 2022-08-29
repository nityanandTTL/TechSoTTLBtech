package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.OrderPickUpActivity;
import com.thyrocare.btechapp.models.api.request.PostPickupOrderRequestClass;
import com.thyrocare.btechapp.models.api.response.PostPickupOrderResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;

public class PostPickupOrderController {

    Activity activity;
    OrderPickUpActivity orderPickUpActivity;
    int flag;
    private Global globalClass;

    public PostPickupOrderController(OrderPickUpActivity orderPickUpActivity) {
        this.activity = orderPickUpActivity;
        globalClass = new Global(activity);
        this.orderPickUpActivity = orderPickUpActivity;
        flag = 1;
    }

    public void PostPickupOrder(PostPickupOrderRequestClass pickupOrderRequestClass) {
        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<PostPickupOrderResponseModel> responseModelCall = apiInterface.postPickupOrder(pickupOrderRequestClass);
            globalClass.showProgressDialog(activity, "Please wait..");

            responseModelCall.enqueue(new Callback<PostPickupOrderResponseModel>() {
                @Override
                public void onResponse(Call<PostPickupOrderResponseModel> call, retrofit2.Response<PostPickupOrderResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    if (response.isSuccessful() && response.body() != null) {
                        if (flag == 1) {
                            orderPickUpActivity.getPostResponse(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostPickupOrderResponseModel> call, Throwable t) {
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
