package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.R;

public class PE_PostPatientDetailsAdapter extends RecyclerView.Adapter<PE_PostPatientDetailsAdapter.PostCheckoutView> {
    Activity activity;
    AppInterfaces.PE_postPatientDetailsAdapterClick pe_postPatientDetailsAdapterClick;


    public PE_PostPatientDetailsAdapter(Activity activity, AppInterfaces.PE_postPatientDetailsAdapterClick pe_postPatientDetailsAdapterClick) {
        this.activity=activity;
        this.pe_postPatientDetailsAdapterClick=pe_postPatientDetailsAdapterClick;
    }

    @NonNull
    @Override
    public PostCheckoutView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_checkout_singleview,viewGroup,false);
        return new PostCheckoutView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCheckoutView postCheckoutView, int i) {
        postCheckoutView.iv_addBendetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pe_postPatientDetailsAdapterClick.selectPatientDetailsClick();
            }
        });

        postCheckoutView.iv_editBenDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pe_postPatientDetailsAdapterClick.editPatientDetailsClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class PostCheckoutView extends RecyclerView.ViewHolder {
        TextView testname,addedBenDetails;
        RelativeLayout rl_ben_addView,rl_ben_editView;
        ImageView iv_addBendetails,iv_editBenDetails ;
        public PostCheckoutView(@NonNull View itemView) {
            super(itemView);
            testname = itemView.findViewById(R.id.testname);
            addedBenDetails = itemView.findViewById(R.id.addedBenDetails);
            rl_ben_addView = itemView.findViewById(R.id.rl_ben_addView);
            iv_addBendetails = itemView.findViewById(R.id.iv_addBendetails);
            iv_editBenDetails = itemView.findViewById(R.id.iv_editBenDetails);
            rl_ben_editView = itemView.findViewById(R.id.rl_ben_editView);
        }
    }
}
