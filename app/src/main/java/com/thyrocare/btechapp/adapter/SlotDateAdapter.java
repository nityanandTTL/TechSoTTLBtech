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
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;

import java.util.ArrayList;

public class SlotDateAdapter extends RecyclerView.Adapter<SlotDateAdapter.MyViewHolder> {
    ArrayList<GetRemarksResponseModel> remarksArray;
    StartAndArriveActivity startAndArriveActivity;
    Context mContext;

    public SlotDateAdapter(StartAndArriveActivity activity, ArrayList<GetRemarksResponseModel> arrayList) {
        this.startAndArriveActivity = activity;
        this.mContext = activity;
        this.remarksArray = arrayList;
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

        holder.tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAndArriveActivity.onClickposition(remarksArray.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
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
