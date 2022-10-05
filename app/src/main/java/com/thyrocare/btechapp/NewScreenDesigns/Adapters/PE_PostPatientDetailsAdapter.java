package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_PEPostCheckoutOrderResponseModel;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PE_PostPatientDetailsAdapter extends RecyclerView.Adapter<PE_PostPatientDetailsAdapter.PostCheckoutView> {
    Activity activity;
    AppInterfaces.PE_postPatientDetailsAdapterClick pe_postPatientDetailsAdapterClick;
    ArrayList<Get_PEPostCheckoutOrderResponseModel> responseModels = new ArrayList<>();

    public PE_PostPatientDetailsAdapter(Activity activity, ArrayList<Get_PEPostCheckoutOrderResponseModel> responseModels, AppInterfaces.PE_postPatientDetailsAdapterClick pe_postPatientDetailsAdapterClick) {
        this.activity = activity;
        this.pe_postPatientDetailsAdapterClick = pe_postPatientDetailsAdapterClick;
        this.responseModels = responseModels;
    }

    @Override
    public PostCheckoutView onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_checkout_singleview, parent, false);
        return new PostCheckoutView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCheckoutView holder, int position) {
        Get_PEPostCheckoutOrderResponseModel singlePatientDataPostion = responseModels.get(position);
        holder.tv_benCount.setText("Please add " + singlePatientDataPostion.getPatientCount() + " beneficiaries");
        holder.testname.setText(singlePatientDataPostion.getTestName());

        holder.iv_addBendetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pe_postPatientDetailsAdapterClick.selectPatientDetailsClick(position);
            }
        });

        holder.iv_editBenDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pe_postPatientDetailsAdapterClick.editPatientDetailsClick(position);
            }
        });

        if (singlePatientDataPostion.getPatientDetails() == null) {
            holder.rl_ben_addView.setVisibility(View.VISIBLE);
            holder.rl_ben_editView.setVisibility(View.GONE);
        } else {
            holder.rl_ben_addView.setVisibility(View.GONE);
            holder.rl_ben_editView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return responseModels.size();
    }

    protected static class PostCheckoutView extends RecyclerView.ViewHolder {
        TextView testname, addedBenDetails, tv_benCount;
        RelativeLayout rl_ben_addView, rl_ben_editView;
        ImageView iv_addBendetails, iv_editBenDetails;

        public PostCheckoutView(@NonNull View itemView) {
            super(itemView);
            testname = itemView.findViewById(R.id.testname);
            tv_benCount = itemView.findViewById(R.id.tv_benCount);
            addedBenDetails = itemView.findViewById(R.id.addedBenDetails);
            rl_ben_addView = itemView.findViewById(R.id.rl_ben_addView);
            iv_addBendetails = itemView.findViewById(R.id.iv_addBendetails);
            iv_editBenDetails = itemView.findViewById(R.id.iv_editBenDetails);
            rl_ben_editView = itemView.findViewById(R.id.rl_ben_editView);
        }
    }
}
