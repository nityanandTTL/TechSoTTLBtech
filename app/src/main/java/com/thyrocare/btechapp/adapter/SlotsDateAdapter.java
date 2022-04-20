package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.RescheduleSlotActivity;
import com.thyrocare.btechapp.models.api.request.SevenDaysModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SlotsDateAdapter extends RecyclerView.Adapter<SlotsDateAdapter.MyViewHolder> {
    ArrayList<SevenDaysModel> sevenDaysModels;
    RescheduleSlotActivity rescheduleSlotActivity;
    Context mContext;
    int mseletected = -1;
    private OnClickListeners onClickListeners;


    public SlotsDateAdapter(RescheduleSlotActivity activity, ArrayList<SevenDaysModel> arrayList) {
        this.rescheduleSlotActivity = activity;
        this.mContext = activity;
        this.sevenDaysModels = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_slot_date_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(c);
        if (date.equalsIgnoreCase(sevenDaysModels.get(position).getDate())) {
            holder.tv_today.setVisibility(View.VISIBLE);
        } else {
            holder.tv_today.setVisibility(View.GONE);
        }

        holder.tv_date.setText(sevenDaysModels.get(position).getDate() + " " + sevenDaysModels.get(position).getMonth());
        holder.tv_days.setText(sevenDaysModels.get(position).getDay());
        holder.ll_view.setTag(position);
        if (sevenDaysModels.get(position).getSelected()) {
            holder.cv_main.setBackgroundResource(R.drawable.rounded_backgroun_theme_color);
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.bg_new_color));
            holder.tv_days.setTextColor(mContext.getResources().getColor(R.color.bg_new_color));
        } else {
            holder.cv_main.setBackgroundResource(R.drawable.rounded_background_white);
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.tv_days.setTextColor(mContext.getResources().getColor(R.color.grey));
        }
        // holder.tv_today.setText();
        holder.ll_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < sevenDaysModels.size(); i++) {
                    sevenDaysModels.get(i).setSelected(false);
                }
                sevenDaysModels.get(position).setSelected(true);
                notifyDataSetChanged();
                rescheduleSlotActivity.dateforappointmentSlot(sevenDaysModels.get(position).getFulldate());
            }
        });

    }

    private void tochecktodaysDate(String fulldate, TextView tv_today) {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        if (fulldate.equals(todayString)) {
            tv_today.setVisibility(View.VISIBLE);
        } else {
            tv_today.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return sevenDaysModels.size();
    }

    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {
        void onDateSelected(String remarks, String s);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_view;
        TextView tv_today, tv_date, tv_days;
        CardView cv_main;

        MyViewHolder(View itemView) {
            super(itemView);
            this.ll_view = itemView.findViewById(R.id.ll_view);
            this.cv_main = itemView.findViewById(R.id.cv_main);
            this.tv_today = itemView.findViewById(R.id.tv_today);
            this.tv_date = itemView.findViewById(R.id.tv_date);
            this.tv_days = itemView.findViewById(R.id.tv_days);
        }
    }
}