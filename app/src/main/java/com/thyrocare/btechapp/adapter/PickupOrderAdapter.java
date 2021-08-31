package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Btech_VisitDisplayAdapter;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.OrderPickUpActivity;
import com.thyrocare.btechapp.models.api.response.PickupOrderResponseModel;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.List;

public class PickupOrderAdapter extends RecyclerView.Adapter<PickupOrderAdapter.PickupList> {

    Activity activity;
    OrderPickUpActivity orderPickUpActivity;
    List<PickupOrderResponseModel.PickupordersDTO> pickupordersDTOS;
    private OnItemClickListener onItemClickListener;


    public PickupOrderAdapter(OrderPickUpActivity orderPickUpActivity, List<PickupOrderResponseModel.PickupordersDTO> pickupordersDTOS, OnItemClickListener orderPickUpActivity1) {
        this.activity = orderPickUpActivity;
        this.orderPickUpActivity = orderPickUpActivity;
        this.onItemClickListener = orderPickUpActivity1;
        this.pickupordersDTOS = pickupordersDTOS;

    }

    @NonNull
    @Override
    public PickupList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickup_order_list, parent, false);
        return new PickupList(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PickupList holder, final int position) {


        InputUtils.setTextToTextView(holder.txtCustomerName, pickupordersDTOS.get(position).getName());
        InputUtils.setTextToTextView(holder.txtOrderNo, pickupordersDTOS.get(position).getOrderNo());
        InputUtils.setTextToTextView(holder.txtDate, pickupordersDTOS.get(position).getAppointment());
        DisplayDayWiselayoutColor(position, holder);

        holder.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = pickupordersDTOS.get(position).getAddress() + "\n" + pickupordersDTOS.get(position).getPincode();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Address")
                        .setMessage(address)
                        .setCancelable(false)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        holder.img_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClientNameClicked(pickupordersDTOS.get(position));
                }
            }
        });


        holder.rel_imgRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClientNameClicked(pickupordersDTOS.get(position));
                }
            }
        });
    }

    private void DisplayDayWiselayoutColor(int position, PickupList holder) {
        if (pickupordersDTOS.get(position).getApptDay() == 0) {
            holder.LL_swipe.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_green_2));
            holder.layoutMain.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_green));
//            holder.txt_visit_day.setText("Today");
        } else if (pickupordersDTOS.get(position).getApptDay() == 1) {
            holder.LL_swipe.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_yellow_2));
            holder.layoutMain.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_yellow));

        } else {
            holder.LL_swipe.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_red_2));
            holder.layoutMain.setBackground(activity.getResources().getDrawable(R.drawable.rounded_background_red));
        }
    }

    public interface OnItemClickListener {
        void onClientNameClicked(PickupOrderResponseModel.PickupordersDTO pickupordersDTO);
    }

    @Override
    public int getItemCount() {
        return pickupordersDTOS.size();
    }

    public class PickupList extends RecyclerView.ViewHolder {
        TextView txtCustomerName, txtOrderNo, txtDate, txtAddress, txt_visit_day;
        ImageView img_pickup;
        LinearLayout LL_swipe;
        LinearLayout layoutMain;
        RelativeLayout rel_imgRelease;


        public PickupList(@NonNull View itemView) {
            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtOrderNo = itemView.findViewById(R.id.txtOrderNo);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txt_visit_day = itemView.findViewById(R.id.txt_visit_day);
            img_pickup = itemView.findViewById(R.id.img_pickup);
            LL_swipe = itemView.findViewById(R.id.LL_swipe);
            layoutMain = itemView.findViewById(R.id.layoutMain);
            rel_imgRelease = itemView.findViewById(R.id.rel_imgRelease);
        }
    }
}
