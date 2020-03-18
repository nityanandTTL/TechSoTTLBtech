package com.thyrocare.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.delegate.LeaveAppliedDisplayDetailsAdapterClickedDelegate;
import com.thyrocare.models.api.response.LeaveAppliedResponseModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.DateUtils;

import java.util.List;

public class AppliedLeaveDisplayDetailsAdapter extends RecyclerView.Adapter<AppliedLeaveDisplayDetailsAdapter.MyViewHolder> {

    private List<LeaveAppliedResponseModel> leaveAppliedResponseModels;

    private LeaveAppliedDisplayDetailsAdapterClickedDelegate leaveAppliedDisplayDetailsAdapterClickedDelegate;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_remark,tv_sr;
        LinearLayout ll_leave;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            tv_sr = (TextView) view.findViewById(R.id.tv_sr);
            ll_leave=(LinearLayout)view.findViewById(R.id.ll_leave);

        }
    }


    public AppliedLeaveDisplayDetailsAdapter(List<LeaveAppliedResponseModel> leaveAppliedResponseModels, LeaveAppliedDisplayDetailsAdapterClickedDelegate leaveAppliedDisplayDetailsAdapterClickedDelegate) {
        this.leaveAppliedResponseModels = leaveAppliedResponseModels;

        this.leaveAppliedDisplayDetailsAdapterClickedDelegate = leaveAppliedDisplayDetailsAdapterClickedDelegate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_applied, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LeaveAppliedResponseModel btechOLeaveAppliedResponseModelrderModel = leaveAppliedResponseModels.get(position);
        if (btechOLeaveAppliedResponseModelrderModel!= null) {

            try {
                holder.tv_date.setText("" + DateUtils.Req_Date_Req("" + btechOLeaveAppliedResponseModelrderModel.getLeaveDate().substring(0,10), "yyyy-MM-dd", "dd-MM-yyyy"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.tv_remark.setText(""+btechOLeaveAppliedResponseModelrderModel.getRemarks());

            int sr_no= position +1;
            holder.tv_sr.setText(" "+sr_no);
            holder.ll_leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        
                    leaveAppliedDisplayDetailsAdapterClickedDelegate.onClickLeave(""+btechOLeaveAppliedResponseModelrderModel.getLeaveDate());
                }
            });

        } else {
            Logger.error("btechOrderModels is null ");
        }

    }
    @Override
    public int getItemCount() {
        return leaveAppliedResponseModels.size();
    }
  
}
