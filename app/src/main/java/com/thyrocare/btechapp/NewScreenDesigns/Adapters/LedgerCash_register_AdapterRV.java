package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_cash_register_details_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;


public class LedgerCash_register_AdapterRV extends RecyclerView.Adapter<LedgerCash_register_AdapterRV.MyViewholder> {

    Activity activity;
    Context context;
    Get_cash_register_details_Model get_cash_register_details_model;

    public LedgerCash_register_AdapterRV(Activity activity, Context context, Get_cash_register_details_Model get_cash_register_details_model) {
        this.activity = activity;
        this.context = context;
        this.get_cash_register_details_model = get_cash_register_details_model;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_titlesub_ledger_module_new, parent, false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        holder.tv_date_id.setText(!StringUtils.isNull(get_cash_register_details_model.getLedgerDetails().get(position).getDate()) ? get_cash_register_details_model.getLedgerDetails().get(position).getDate() : "" );
        holder.tv_openingbalance_id.setText("" + get_cash_register_details_model.getLedgerDetails().get(position).getOpeningBal());
        holder.tv_closingBalance_id.setText("" + get_cash_register_details_model.getLedgerDetails().get(position).getClosingBal());
        holder.tv_credit_id.setText("" + get_cash_register_details_model.getLedgerDetails().get(position).getCredit());
        holder.tv_debit_id.setText("" + get_cash_register_details_model.getLedgerDetails().get(position).getDebit());


    }

    @Override
    public int getItemCount() {
        return get_cash_register_details_model.getLedgerDetails().size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView tv_closingBalance_id, tv_debit_id, tv_credit_id, tv_openingbalance_id, tv_date_id;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tv_closingBalance_id = itemView.findViewById(R.id.tv_closingBalance_id);
            tv_debit_id = itemView.findViewById(R.id.tv_debit_id);
            tv_credit_id = itemView.findViewById(R.id.tv_credit_id);
            tv_openingbalance_id = itemView.findViewById(R.id.tv_openingbalance_id);
            tv_date_id = itemView.findViewById(R.id.tv_date_id);

        }
    }
}
