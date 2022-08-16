package com.thyrocare.btechapp.NewScreenDesigns.Controllers;

import android.app.Activity;
import android.widget.Toast;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.AddEditBenificaryActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.AddONRequestModel;
import com.thyrocare.btechapp.models.api.request.TCVerifyCouponRequestModel;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.TCVerifyCouponResponseModel;
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
    TCVerifyCouponRequestModel requestModel;
    AppInterfaces.getVerifyCoupon couponInterface;


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


    public void callValidatePECouponCodeApi() {
        try {
            global.showProgressDialog(mActivity, "Verifying coupon...");
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.PE_API))).create(PostAPIInterface.class);
            Call<VerifyCouponResponseModel> call = apiInterface.verifySelectedPECoupon(orderNo/*"VL258CDA"*/, addONRequestModel);
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
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            global.hideProgressDialog(mActivity);
        }

    }

    public void callVerifyTCCouponAPI() {
        try {
            global.showProgressDialog(mActivity, "Verifying coupon...");
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<TCVerifyCouponResponseModel> call = apiInterface.verifySelectedTCCoupon(requestModel);
            call.enqueue(new Callback<TCVerifyCouponResponseModel>() {
                @Override
                public void onResponse(Call<TCVerifyCouponResponseModel> call, Response<TCVerifyCouponResponseModel> response) {
                    global.hideProgressDialog(mActivity);
                    if (response.isSuccessful() && response.body() != null) {
                        couponInterface.OnTCCouponResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<TCVerifyCouponResponseModel> call, Throwable t) {
                    global.hideProgressDialog(mActivity);
                    Global.showCustomStaticToast(mActivity, ConstantsMessages.SomethingWentwrngMsg);
                }
            });
        } catch (Exception e) {
            Global.showCustomStaticToast(mActivity, ConstantsMessages.SomethingWentwrngMsg);
            e.printStackTrace();
        }

    }
}
