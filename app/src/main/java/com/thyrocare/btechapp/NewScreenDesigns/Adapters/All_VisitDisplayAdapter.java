package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class All_VisitDisplayAdapter extends RecyclerView.Adapter<All_VisitDisplayAdapter.MyViewHolder> {
    private static final String TAG = All_VisitDisplayAdapter.class.getSimpleName();
    private Activity activity;
    private final String current_date;
    GetOrderDetailsResponseModel orderDetailsResponseModel;
    private ArrayList<GetOrderDetailsResponseModel.GetVisitcountDTO> orderVisitDetailsModelsArr;
    private AppPreferenceManager appPreferenceManager;
    B2BVisitOrdersDisplayFragment mB2BVisitOrdersDisplayFragment;

    public All_VisitDisplayAdapter(B2BVisitOrdersDisplayFragment visitOrdersDisplayFragment_new, GetOrderDetailsResponseModel orderDetailsResponseModels, Activity activity) {
        this.activity = activity;
        this.orderVisitDetailsModelsArr = orderDetailsResponseModels.getGetVisitcount();
        this.orderDetailsResponseModel = orderDetailsResponseModels;
        appPreferenceManager = new AppPreferenceManager(this.activity);
        mB2BVisitOrdersDisplayFragment = visitOrdersDisplayFragment_new;
        current_date = DateUtil.getDateFromLong(System.currentTimeMillis(), "dd-MM-yyyy");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_visit_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int pos) {
        holder.txtOrderNo.setText(orderVisitDetailsModelsArr.get(pos).getVisitId());

        holder.txtDate.setText(orderVisitDetailsModelsArr.get(pos).getAppointmentDate());
        holder.txtCustomerName.setText(Global.toCamelCase(orderVisitDetailsModelsArr.get(pos).getName()));
        if (InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), BundleConstants.ACCEPTED) ||
                !InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), "fix appointment") ||
                !InputUtils.CheckEqualIgnoreCase(orderVisitDetailsModelsArr.get(pos).getStatus(), "ASSIGNED")) {
            holder.txtStatus.setText("Status : " + orderVisitDetailsModelsArr.get(pos).getStatus());
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }

        holder.txtDate.setText(DateUtils.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy") + " "+ (!InputUtils.isNull(orderDetailsResponseModel.getGetVisitcount().get(pos).getTimeSlot())?orderDetailsResponseModel.getGetVisitcount().get(pos).getTimeSlot():DateUtils.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), "dd-MM-yyyy hh:mm a", "HH:mm")) );

        try {
            if (!InputUtils.isNull(orderVisitDetailsModelsArr.get(pos).getAppointmentDate()) && !InputUtils.isNull(orderDetailsResponseModel.getCurrentDatetime()) && DateUtils.Req_Date_Req(orderVisitDetailsModelsArr.get(pos).getAppointmentDate(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy").equals(DateUtils.Req_Date_Req(orderDetailsResponseModel.getCurrentDatetime(), "dd-MM-yyyy hh:mm a", "dd-MM-yyyy"))) {
                holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_green);
            } else {
                holder.layoutMain.setBackgroundResource(R.drawable.rounded_background_yellow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BundleConstants.setSelectedOrder = orderVisitDetailsModelsArr.get(pos).getVisitId();
                mB2BVisitOrdersDisplayFragment.orderSelected(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderVisitDetailsModelsArr.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        TextView txtCustomerName, txtOrderNo, txtDate, txtSlot, txtStatus;

        public MyViewHolder(View view) {
            super(view);

            txtOrderNo = (TextView) view.findViewById(R.id.txtOrderNo);
            layoutMain = (LinearLayout) view.findViewById(R.id.layoutMain);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtSlot = (TextView) view.findViewById(R.id.txtSlot);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        }
    }
}
