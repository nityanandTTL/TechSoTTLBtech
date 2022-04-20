package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;

import java.util.ArrayList;

public class OrderReleaseAdapter extends RecyclerView.Adapter<OrderReleaseAdapter.MyViewHolder> {
    ArrayList<String> orderArray;
    StartAndArriveActivity startAndArriveActivity;
    Context mContext;
    int flag;
    ArrayList<GetPECancelRemarksResponseModel> arrayList;
    ArrayList<GetRemarksResponseModel> remarksArray;
    VisitOrdersDisplayFragment_new visitOrdersDisplayFragment_new;


    /*public OrderReleaseAdapter(StartAndArriveActivity activity, ArrayList<GetPECancelRemarksResponseModel> arrayList, int i) {
        this.startAndArriveActivity = activity;
        this.mContext = activity;
        this.arrayList = arrayList;
        this.flag = i;
    }*/

    public OrderReleaseAdapter(StartAndArriveActivity activity, ArrayList<GetRemarksResponseModel> arrayList) {
        this.startAndArriveActivity = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
        flag = 1;
    }
    public OrderReleaseAdapter(VisitOrdersDisplayFragment_new activity, ArrayList<GetRemarksResponseModel> arrayList) {
        this.visitOrdersDisplayFragment_new = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
        flag = 2;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_release_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        /*if (flag == 1) {
            holder.tv_text.setText(arrayList.get(position).getReason());

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAndArriveActivity.onClick(arrayList.get(position).getId(), arrayList.get(position).getReason());
                }
            });
        } else if (flag == 2) {*/
            holder.tv_text.setText(remarksArray.get(position).getRemarks());

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == 1){
                        startAndArriveActivity.onRemarksClick(remarksArray.get(position),position);
                    }else{
                        visitOrdersDisplayFragment_new.onRemarksClick(remarksArray.get(position),position);
                    }

                }
            });
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
        LinearLayout ll_main;

        MyViewHolder(View itemView) {
            super(itemView);
            this.tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            this.ll_main = (LinearLayout) itemView.findViewById(R.id.ll_main);
        }
    }
}
