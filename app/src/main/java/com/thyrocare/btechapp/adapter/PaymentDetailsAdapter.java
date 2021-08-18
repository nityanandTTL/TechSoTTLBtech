package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by e5233@thyrocare.com on 30/8/18.
 */

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.MyViewHolder> {
    private ArrayList<PaymentProcessAPIResponseModel> mList;
    Context mContext;
    PaymentsActivity mPaymentsActivity;


    public PaymentDetailsAdapter(PaymentsActivity activity, ArrayList<PaymentProcessAPIResponseModel> paymentModesArr) {
        this.mPaymentsActivity = activity;
        this.mContext = activity;
        this.mList = paymentModesArr;
    }

    @Override
    public PaymentDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentsgateway_row_items, parent, false);
        return new PaymentDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaymentDetailsAdapter.MyViewHolder holder, final int position) {
        try {
            for (int i = 0; i < mList.get(position).getNameValueCollection().size(); i++) {
                if(mList.get(position).getNameValueCollection().get(i).getKey().equals("ModeName")){
                    holder.txt_paymentModename.setText(mList.get(position).getNameValueCollection().get(i).getValue());
                    if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("AIRTEL")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_airtel));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("MobiKwik")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bobikwik));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("PayU")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_payu));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("Paytm")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_paytm));
                    } else {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bank));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.img_clcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentsActivity.fetchPaymentClickDetails(mList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {

        return mList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_paymentModename;
        ImageView img_ident, img_clcik /*, edt_mbl_num_vald*/;
       /* EditText edt_mbl_num;*/


        MyViewHolder(View itemView) {
            super(itemView);

            this.txt_paymentModename = (TextView) itemView.findViewById(R.id.txt_paymentModename);
            this.img_ident = (ImageView) itemView.findViewById(R.id.img_ident);
            this.img_clcik = (ImageView) itemView.findViewById(R.id.img_clcik);
          /*  this.edt_mbl_num_vald = (ImageView) itemView.findViewById(R.id.edt_mbl_num_vald);
            this.edt_mbl_num = (EditText) itemView.findViewById(R.id.edt_mbl_num);*/

        }
    }
}
