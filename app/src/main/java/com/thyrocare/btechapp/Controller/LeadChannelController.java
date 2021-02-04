package com.thyrocare.btechapp.Controller;

import android.app.ProgressDialog;
import android.content.Context;


import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.fragment.LeadGenerationFragment;
import com.thyrocare.btechapp.models.api.response.LeadChannelRespModel;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;

public class LeadChannelController {

    LeadGenerationFragment leadGenerationFragment;

    private Context context;
    private Global globalClass;

    public LeadChannelController( LeadGenerationFragment leadGenerationFragment) {

        this.leadGenerationFragment = leadGenerationFragment;
        this.context = leadGenerationFragment.getContext();
        globalClass = new Global(context);


    }

    public void GetLeadChannel() {
        try {
            globalClass.showProgressDialog(context, PLEASE_WAIT);
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(context, EncryptionUtils.Dcrp_Hex(context.getString(R.string.VELSO_URL))).create(PostAPIInterface.class);
            Call<LeadChannelRespModel> responseCall = apiInterface.getLeadChannel();

            responseCall.enqueue(new Callback<LeadChannelRespModel>() {
                @Override
                public void onResponse(Call<LeadChannelRespModel> call, Response<LeadChannelRespModel> response) {
                    globalClass.hideProgressDialog(leadGenerationFragment.getActivity());

                    if (response.isSuccessful() && response.body() != null) {
                        LeadChannelRespModel leadChannelRespModel = response.body();
                        leadGenerationFragment.getLeadChannel(leadChannelRespModel);
                    } else {
                        leadGenerationFragment.getLeadChannel(null);

                    }
                }

                @Override
                public void onFailure(Call<LeadChannelRespModel> call, Throwable t) {
                    globalClass.hideProgressDialog(leadGenerationFragment.getActivity());
                    leadGenerationFragment.getLeadChannel(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
