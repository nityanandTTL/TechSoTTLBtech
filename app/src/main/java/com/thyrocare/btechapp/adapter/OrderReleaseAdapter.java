package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.PE_PostPatientDetailsActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;

import java.util.ArrayList;

public class
OrderReleaseAdapter extends RecyclerView.Adapter<OrderReleaseAdapter.MyViewHolder> {
    ArrayList<String> orderArray;
    StartAndArriveActivity startAndArriveActivity;
    PE_PostPatientDetailsActivity pe_postPatientDetailsActivity;
    Context mContext;
    int flag;
    ArrayList<GetPECancelRemarksResponseModel> arrayList;
    ArrayList<GetRemarksResponseModel> remarksArray;
    VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new;
    Boolean pePartner;


    /*public OrderReleaseAdapter(StartAndArriveActivity activity, ArrayList<GetPECancelRemarksResponseModel> arrayList, int i) {
        this.startAndArriveActivity = activity;
        this.mContext = activity;
        this.arrayList = arrayList;
        this.flag = i;
    }*/

    public OrderReleaseAdapter(StartAndArriveActivity activity, ArrayList<GetRemarksResponseModel> arrayList, boolean pePartner) {
        this.startAndArriveActivity = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
        flag = 1;
        this.pePartner = pePartner;
    }

    public OrderReleaseAdapter(VisitOrdersDisplayFragment_new activity, ArrayList<GetRemarksResponseModel> arrayList, boolean pePartner) {
        this.visitOrdersDisplayFragment_new = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
        flag = 2;
        this.pePartner = pePartner;
    }

    public OrderReleaseAdapter(PE_PostPatientDetailsActivity activity, ArrayList<GetRemarksResponseModel> arrayList, boolean pePartner) {
        this.pe_postPatientDetailsActivity = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
        flag = 3;
        this.pePartner = pePartner;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_release_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tv_text.setText(remarksArray.get(position).getRemarks());

        holder.iv_regularreshcedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    startAndArriveActivity.onRemarksClick(remarksArray.get(position), position);
                } else if (flag == 2) {
                    visitOrdersDisplayFragment_new.onRemarksClick(remarksArray.get(position), position);
                } else if (flag == 3) {
                    pe_postPatientDetailsActivity.onRemarksClick(remarksArray.get(position), position);
                }

            }
        });

        holder.ll_tripartyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    startAndArriveActivity.onCustomerSupportCallClicked();
                } else if (flag == 2) {
                    visitOrdersDisplayFragment_new.onCustomerSupportCallClicked();
                } else if (flag == 3) {
                    pe_postPatientDetailsActivity.onCustomerSupportCallClicked();
                }
            }
        });

        if (remarksArray.get(position).getRemarks().contains("reschedule") || remarksArray.get(position).getRemarks().contains("cancel")) {
            holder.ll_tripartyCall.setVisibility(View.VISIBLE);
            holder.iv_regularreshcedule.setVisibility(View.GONE);
        } else {
            holder.ll_tripartyCall.setVisibility(View.GONE);
            holder.iv_regularreshcedule.setVisibility(View.VISIBLE);
        }


    }
//    }

    @Override
    public int getItemCount() {
        /*if (flag == 1) {
            return arrayList.size();
        }*/
        return remarksArray.size();


    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_text;
        RelativeLayout ll_main;
        LinearLayout ll_tripartyCall;
        ImageView ic_triparty_call, iv_regularreshcedule;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            ll_main = itemView.findViewById(R.id.ll_main);
            ll_tripartyCall = itemView.findViewById(R.id.ll_tripartyCall);
            ic_triparty_call = itemView.findViewById(R.id.ic_triparty_call);
            iv_regularreshcedule = itemView.findViewById(R.id.iv_regularreshcedule);

        }
    }
}
