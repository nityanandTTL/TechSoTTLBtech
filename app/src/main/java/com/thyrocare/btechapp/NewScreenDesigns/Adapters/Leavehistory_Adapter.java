package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_Leave_Applied_history_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.R;

import java.util.ArrayList;

public class Leavehistory_Adapter extends RecyclerView.Adapter<Leavehistory_Adapter.ViewHolder> {
    Activity nactivity;
    Context ncontext;
    ArrayList<Get_Leave_Applied_history_Model> getLeave_applied_history_model;

    public Leavehistory_Adapter(Activity activity, ArrayList<Get_Leave_Applied_history_Model> getLeave_applied_history_model_) {
        this.nactivity = activity;
        this.getLeave_applied_history_model = getLeave_applied_history_model_;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_sr_no_id.setText(""+(position+1));

        String Date  = getLeave_applied_history_model.get(position).getLeaveDate();

        try {
            Date = DateUtil.Req_Date_Req(Date,"yyyy-MM-dd'T'HH:mm:ss","dd MMM yyyy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tv_date_id.setText(Date);
        holder.tv_remark_id.setText(getLeave_applied_history_model.get(position).getRemarks());

    }

    @Override
    public int getItemCount() {
        return getLeave_applied_history_model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sr_no_id,tv_date_id,tv_remark_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sr_no_id = itemView.findViewById(R.id.tv_sr_no_id);
            tv_date_id = itemView.findViewById(R.id.tv_date_id);
            tv_remark_id = itemView.findViewById(R.id.tv_remark_id);
        }
    }
}
