package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import android.app.Activity;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.AddONRequestModel;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.VerifyCouponResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditBenificaryController {
    AddEditBenificaryActivity addEditBenificaryActivity;
    Boolean pePartner;
    Activity mActivity;
    String orderNo;
    Global global;
    AddONRequestModel addONRequestModel;


    public AddEditBenificaryController(AddEditBenificaryActivity addEditBenificaryActivity, boolean pePartner, String orderNo) {
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        this.pePartner = pePartner;
        this.mActivity = addEditBenificaryActivity;
        this.orderNo = (InputUtils.isNull(orderNo) ? "" : orderNo);
        global = new Global(mActivity);
    }

    public AddEditBenificaryController(AddEditBenificaryActivity addEditBenificaryActivity, boolean pePartner, AddONRequestModel addONRequestModel, String orderNo) {
        this.addEditBenificaryActivity = addEditBenificaryActivity;
        this.pePartner = pePartner;
        this.mActivity = addEditBenificaryActivity;
        this.addONRequestModel = addONRequestModel;
        this.orderNo = orderNo;
        global = new Global(mActivity);
    }

    public void callCouponCodeAPI() {
        global.showProgressDialog(mActivity, "Please wait...");
        if (pePartner) {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<CouponCodeResponseModel> call = apiInterface.getCouponCodes(orderNo);
            call.enqueue(new Callback<CouponCodeResponseModel>() {
                @Override
                public void onResponse(Call<CouponCodeResponseModel> call, Response<CouponCodeResponseModel> response) {
                    global.hideProgressDialog(mActivity);
                    if (response.body() != null && response.isSuccessful()) {
                        addEditBenificaryActivity.getCouponsResponse(response.body());
                    } else {
                        addEditBenificaryActivity.getCouponsResponse(null);
                        Global.showCustomStaticToast(mActivity, ConstantsMessages.SomethingWentwrngMsg);
                    }

                }

                @Override
                public void onFailure(Call<CouponCodeResponseModel> call, Throwable t) {
                    global.hideProgressDialog(mActivity);
                    addEditBenificaryActivity.getCouponsResponse(null);
                }
            });

        } else {
            //TODO in case of thyrocare Btech, call of other api for coupons
        }
    }

    public void callValidateCouponCodeApi() {
        try {
            global.showProgressDialog(mActivity, "Please wait...");
            if (pePartner) {
                PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.PE_API))).create(PostAPIInterface.class);
                Call<VerifyCouponResponseModel> call = apiInterface.verifySelectedCoupon(orderNo/*"VL258CDA"*/, addONRequestModel);
                call.enqueue(new Callback<VerifyCouponResponseModel>() {
                    @Override
                    public void onResponse(Call<VerifyCouponResponseModel> call, Response<VerifyCouponResponseModel> response) {
                        global.hideProgressDialog(mActivity);
                        if (response.isSuccessful() && response.body() != null) {
                            addEditBenificaryActivity.getCouponValidateResponse(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyCouponResponseModel> call, Throwable t) {
                        global.hideProgressDialog(mActivity);

                        Toast.makeText(mActivity, ConstantsMessages.SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mActivity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //TODO in case of thyrocare Btech, call of other api for coupons
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            global.hideProgressDialog(mActivity);
        }

    }
}
