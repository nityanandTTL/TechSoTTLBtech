package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.RescheduleSlotActivity;
import com.thyrocare.btechapp.models.api.response.GetPEBtechSlotResponseModel;

import java.util.ArrayList;

public class SlotAppointmentTimeAdapter extends RecyclerView.Adapter<SlotAppointmentTimeAdapter.MyViewHolder> {
    ArrayList<GetPEBtechSlotResponseModel> slotResponseModels;
    RescheduleSlotActivity rescheduleSlotActivity;
    Context mContext;
    int selectedPosition = -1;
    int flag;


    public SlotAppointmentTimeAdapter(RescheduleSlotActivity activity, ArrayList<GetPEBtechSlotResponseModel> arrayList, int i) {
        this.rescheduleSlotActivity = activity;
        this.mContext = activity;
        this.slotResponseModels = arrayList;
        flag = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_new_order_release_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (flag==1){
            holder.tv_textNew.setText(slotResponseModels.get(position).getNewSlot());
            holder.rd_txt_order.setChecked(position == selectedPosition);
            holder.rd_txt_order.setTag(position);
            holder.rd_txt_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = (Integer)v.getTag();
                    rescheduleSlotActivity.appointmentslotChecked(slotResponseModels.get(position));
                    notifyDataSetChanged();
                }
            });
        }else{
            holder.tv_textNew.setText(slotResponseModels.get(position).getSlot());
            holder.rd_txt_order.setChecked(position == selectedPosition);
            holder.rd_txt_order.setTag(position);
            holder.rd_txt_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = (Integer)v.getTag();
                    rescheduleSlotActivity.appointmentslotChecked(slotResponseModels.get(position));
                    notifyDataSetChanged();
                }
            });
        }

//        holder.tv_textNew.setText(slotResponseModels.get(position).getSlot());

    }

    @Override
    public int getItemCount() {
        return slotResponseModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RadioButton rd_txt_order;
        TextView tv_textNew;

        MyViewHolder(View itemView) {
            super(itemView);
            this.rd_txt_order = itemView.findViewById(R.id.rd_txt_order);
            this.tv_textNew = itemView.findViewById(R.id.tv_textNew);
        }
    }
}