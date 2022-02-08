package com.thyrocare.btechapp.Controller;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Leave_intimation_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HCW_Activity;
import com.thyrocare.btechapp.activity.Tsp_ScheduleYourDayActivity;
import com.thyrocare.btechapp.models.api.request.GetNBTDetailRequestModel;
import com.thyrocare.btechapp.models.api.response.GetNBTDetailResponseModel;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
import com.thyrocare.btechapp.models.data.TSPNBT_AvilModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class GetNBTListController {
    Activity activity;
    Leave_intimation_fragment_new leave_intimation_fragment_new;
    Global globalClass;

    public GetNBTListController(Leave_intimation_fragment_new leave_intimation_fragment_new) {
        this.activity = leave_intimation_fragment_new;
        this.leave_intimation_fragment_new = leave_intimation_fragment_new;
        globalClass = new Global(activity);
    }

    public void getNBTList(GetNBTDetailRequestModel getNBTDetailRequestModel) {
        try {
            globalClass.showProgressDialog(leave_intimation_fragment_new, ConstantsMessages.PLEASE_WAIT);
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(leave_intimation_fragment_new, EncryptionUtils.Dcrp_Hex(leave_intimation_fragment_new.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
            Call<GetNBTDetailResponseModel> responseCall = postAPIInterface.getNBTDetails(getNBTDetailRequestModel);
            responseCall.enqueue(new Callback<GetNBTDetailResponseModel>() {
                @Override
                public void onResponse(Call<GetNBTDetailResponseModel> call, Response<GetNBTDetailResponseModel> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        globalClass.hideProgressDialog(leave_intimation_fragment_new);
                        GetNBTDetailResponseModel getNBTDetailResponseModel = response.body();
                        leave_intimation_fragment_new.NBTList(getNBTDetailResponseModel);
                    } else {
                        globalClass.hideProgressDialog(leave_intimation_fragment_new);
                        globalClass.showCustomToast(leave_intimation_fragment_new, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<GetNBTDetailResponseModel> call, Throwable t) {
                    MessageLogger.LogDebug("Errror", t.getMessage());
                    globalClass.hideProgressDialog(leave_intimation_fragment_new);
                    globalClass.showCustomToast(leave_intimation_fragment_new, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
