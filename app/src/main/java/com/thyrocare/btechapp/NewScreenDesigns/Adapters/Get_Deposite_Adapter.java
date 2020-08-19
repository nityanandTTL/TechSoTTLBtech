package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_deposite_details_model;
import com.thyrocare.btechapp.R;

import java.util.ArrayList;

public class Get_Deposite_Adapter extends RecyclerView.Adapter<Get_Deposite_Adapter.MyViewholder> {
    Activity activity;
    ArrayList<Get_deposite_details_model> get_deposite_details_models;

    public Get_Deposite_Adapter(Activity activity, Context context, ArrayList<Get_deposite_details_model> get_deposite_details_model) {
        this.activity = activity;
        this.get_deposite_details_models = get_deposite_details_model;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_ledgersub_earning_new,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.txt_Amount.setText(get_deposite_details_models.get(position).getAmount()+"");
        holder.txt_Registerdate.setText(get_deposite_details_models.get(position).getDate());
        holder.txt_Remarks.setText(get_deposite_details_models.get(position).getRemarks());
    }

    @Override
    public int getItemCount() {
        return get_deposite_details_models.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView txt_Remarks,txt_Amount,txt_Registerdate;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            txt_Amount = itemView.findViewById(R.id.txt_Amount);
            txt_Remarks = itemView.findViewById(R.id.txt_Remarks);
            txt_Registerdate = itemView.findViewById(R.id.txt_Registerdate);


        }
    }
}
