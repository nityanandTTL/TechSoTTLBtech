package com.thyrocare.btechapp.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.OrderServedDisplayDetailsAdapterClickedDelegate;
import com.thyrocare.btechapp.models.data.BtechOrderModel;


import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

public class OrderServedDisplayDetailsAdapter extends RecyclerView.Adapter<OrderServedDisplayDetailsAdapter.MyViewHolder> {

    private List<BtechOrderModel> btechOrderModels;
    private HomeScreenActivity activity;
    private OrderServedDisplayDetailsAdapterClickedDelegate orderServedDisplayDetailsAdapterClickedDelegate;
    private Global global;
    private AppPreferenceManager appPreferenceManager;

    public OrderServedDisplayDetailsAdapter(List<BtechOrderModel> btechOrderModels, HomeScreenActivity activity, OrderServedDisplayDetailsAdapterClickedDelegate orderServedDisplayDetailsAdapterClickedDelegate) {
        this.btechOrderModels = btechOrderModels;
        this.activity = activity;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        this.orderServedDisplayDetailsAdapterClickedDelegate = orderServedDisplayDetailsAdapterClickedDelegate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_served, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BtechOrderModel btechOrderModel = btechOrderModels.get(position);
        if (btechOrderModel != null) {
            holder.txt_name.setText("" + btechOrderModel.getOrderBy());
            holder.tv_orderno.setText("" + btechOrderModel.getOrderNo());
            holder.tv_status.setText("" + btechOrderModel.getStatus());
            holder.txt_fasting.setText("" + btechOrderModel.getFasting());
            int sr_no = position + 1;
            holder.txt_sr_no.setText("" + sr_no);
            holder.txt_benCount.setText("   " + btechOrderModel.getBenCount());


            holder.ereciept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btechOrderModel.getOrderNo() != null) {
                        if (isNetworkAvailable(activity)) {
                            CallgetErieceptApi(btechOrderModel.getOrderNo());
                        } else {
                            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


            ArrayList<String> mylist = new ArrayList<String>();
            for (int i = 0; i < btechOrderModel.getBtchBracodeDtl().size(); i++) {
                mylist.add(btechOrderModel.getBtchBracodeDtl().get(i).getBarcode());
                Logger.error("barcode : " + btechOrderModel.getBtchBracodeDtl().get(i).getBarcode());
            }
            String listString = "";
            for (String s : mylist) {
                if (InputUtils.isNull(listString)) {
                    listString = s;
                } else {
                    listString = listString + ",\t" + s;
                }
            }
            holder.tv_barcode.setText("" + listString);
            if (btechOrderModel.getAmountCollected() == 0) {
                holder.tv_amount.setVisibility(View.GONE);
                holder.amount_title.setVisibility(View.GONE);
            } else {
                holder.tv_amount.setVisibility(View.VISIBLE);
                holder.amount_title.setVisibility(View.VISIBLE);
                holder.tv_amount.setText("" + btechOrderModel.getAmountCollected());
            }

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderServedDisplayDetailsAdapterClickedDelegate.onCallCustomer(btechOrderModel.getMobile(), btechOrderModel.getOrderNo());
                }
            });

        } else {
            Logger.error("btechOrderModels is null ");
        }
    }

    @Override
    public int getItemCount() {
        return btechOrderModels.size();
    }

    private void CallgetErieceptApi(String orderNo) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetErieceptApi(orderNo);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().equalsIgnoreCase("1") || response.body().equalsIgnoreCase("1\n")) {
                        Toast.makeText(activity, "E-Reciept sent Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Failed to send E-Reciept. Please try after sometime.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Failed to send E-Reciept. Please try after sometime.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView call;
        TextView tv_orderno, tv_barcode, tv_status, txt_amount, txt_name, txt_title, txt_sr_no, tv_amount, amount_title, txt_benCount, txt_fasting;
        Button ereciept;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_title = (TextView) view.findViewById(R.id.oderno_title);
            call = (ImageView) view.findViewById(R.id.call);
            tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);
            tv_barcode = (TextView) view.findViewById(R.id.tv_barcode);
            tv_barcode.setSelected(true);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            txt_amount = (TextView) view.findViewById(R.id.txt_amount);
            txt_sr_no = (TextView) view.findViewById(R.id.txt_sr_no);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            amount_title = (TextView) view.findViewById(R.id.amount_title);
            txt_benCount = (TextView) view.findViewById(R.id.tv_beneficiary_count);
            txt_fasting = (TextView) view.findViewById(R.id.tv_fasting);
            ereciept = (Button) view.findViewById(R.id.ereciept);
        }
    }


}
