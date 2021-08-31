package com.thyrocare.btechapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentsgateway_row_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            for (int i = 0; i < mList.get(position).getNameValueCollection().size(); i++) {
                if (mList.get(position).getNameValueCollection().get(i).getKey().equals("ModeName")) {
                    holder.txt_paymentModename.setText(mList.get(position).getNameValueCollection().get(i).getValue());
                    if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("AIRTEL")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_airtel));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("MobiKwik")) {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bobikwik));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("PayU")) {
                        holder.txt_paymentModename.setText("Send link on SMS/Email");
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_pay_n));
                    } else if (mList.get(position).getNameValueCollection().get(i).getValue().equalsIgnoreCase("Paytm")) {
                        holder.txt_paymentModename.setText("QR Code");
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_baseline_qr_code_scanner_24));
                    } else {
                        holder.img_ident.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bank));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.ll_rowmainelmnt.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout ll_rowmainelmnt;
        ImageView img_ident;

        MyViewHolder(View itemView) {
            super(itemView);

            this.txt_paymentModename = (TextView) itemView.findViewById(R.id.txt_paymentModename);
            this.img_ident = (ImageView) itemView.findViewById(R.id.img_ident);
            this.ll_rowmainelmnt = (LinearLayout) itemView.findViewById(R.id.ll_rowmainelmnt);
        }
    }
}
