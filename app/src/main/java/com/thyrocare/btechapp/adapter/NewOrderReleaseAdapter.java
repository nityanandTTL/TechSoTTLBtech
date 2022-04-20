package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.NewOrderReleaseActivity;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;

import java.util.ArrayList;

public class NewOrderReleaseAdapter extends RecyclerView.Adapter<NewOrderReleaseAdapter.MyViewHolder> {
    ArrayList<GetPECancelRemarksResponseModel> reasonsDTOS;
    NewOrderReleaseActivity newOrderReleaseActivity;
    Context mContext;
    int selectedPosition = -1;

    public NewOrderReleaseAdapter(NewOrderReleaseActivity activity, ArrayList<GetPECancelRemarksResponseModel> arrayList) {
        this.newOrderReleaseActivity = activity;
        this.mContext = activity;
        this.reasonsDTOS = arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_new_order_release_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*if (selectedPosition >= 0) {
            if (selectedPosition == position) {
                if (InputUtils.CheckEqualIgnoreCase(reasonsDTOS.get(selectedPosition).getReason(), "Other")) {
                    holder.edt_other.setVisibility(View.VISIBLE);
                } else {
                    holder.edt_other.setVisibility(View.GONE);
                }
            }else {
                holder.edt_other.setVisibility(View.GONE);
            }
        }*/


        holder.tv_textNew.setText(reasonsDTOS.get(position).getReason());
        holder.rd_txt_order.setChecked(position == selectedPosition);
        holder.rd_txt_order.setTag(position);
        holder.rd_txt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (Integer) v.getTag();
                newOrderReleaseActivity.getSelection(reasonsDTOS.get(position));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasonsDTOS.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_textNew;
        RadioButton rd_txt_order;
        EditText edt_other;

        MyViewHolder(View itemView) {
            super(itemView);
            this.tv_textNew = (TextView) itemView.findViewById(R.id.tv_textNew);
            this.rd_txt_order = (RadioButton) itemView.findViewById(R.id.rd_txt_order);
            edt_other = itemView.findViewById(R.id.edt_other);
        }
    }
}