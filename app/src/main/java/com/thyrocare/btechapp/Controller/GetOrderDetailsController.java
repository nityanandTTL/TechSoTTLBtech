package com.thyrocare.btechapp.Controller;

import android.app.Activity;

import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class GetOrderDetailsController {

    Activity activity;
    VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new;
    Global globalClass;
    FetchOrderDetailsResponseModel getfetchOrderDetailsResponseModel;


    public GetOrderDetailsController(VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new) {
        this.activity = visitOrdersDisplayFragment_new;
        this.visitOrdersDisplayFragment_new = visitOrdersDisplayFragment_new;
        globalClass = new Global(activity);
    }

    public void getOrderDetails(final FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel, String btechID) {

        try {
            globalClass.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<GetOrderDetailsResponseModel> getOrderDetailsResponseModelCall = getAPIInterface.getpendingOrderDetails(btechID);
            getOrderDetailsResponseModelCall.enqueue(new Callback<GetOrderDetailsResponseModel>() {
                @Override
                public void onResponse(Call<GetOrderDetailsResponseModel> call, retrofit2.Response<GetOrderDetailsResponseModel> response) {
                    globalClass.hideProgressDialog(activity);
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            GetOrderDetailsResponseModel getOrderDetailsResponseModel = response.body();
                            if (InputUtils.CheckEqualIgnoreCase(getOrderDetailsResponseModel.getResponse(), Constants.Success)) {
                                if (getOrderDetailsResponseModel!=null&& getOrderDetailsResponseModel.getGetVisitcount().size()>0){
                                    visitOrdersDisplayFragment_new.checkOrderDetails(fetchOrderDetailsResponseModel,getOrderDetailsResponseModel);
                                }
                            } else {
                                globalClass.showCustomToast(activity, "" + getOrderDetailsResponseModel.getResponse());
                            }
                        }
                        else {
                            globalClass.showCustomToast(activity, response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetOrderDetailsResponseModel> call, Throwable t) {
                    globalClass.showCustomToast(activity, "Something went wrong. Try after sometime");
                    globalClass.hideProgressDialog(activity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
