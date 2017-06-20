package com.dhb.adapter;

/**
 * Created by vendor1 on 4/21/2017.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.dhb.models.api.request.CallPatchRequestModel;
import com.dhb.models.data.BtechOrderModel;
import com.dhb.models.data.HUBBTechModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderServedDisplayDetailsAdapter extends RecyclerView.Adapter<OrderServedDisplayDetailsAdapter.MyViewHolder> {

    private List<BtechOrderModel> btechOrderModels;
    HomeScreenActivity activity;

    private String MaskedPhoneNumber = "";
    private AppPreferenceManager appPreferenceManager;
    //DispatchToHubAdapterOnItemClickedDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderno, tv_barcode, tv_status, txt_amount, txt_name, txt_title, txt_sr_no,tv_amount,amount_title;
        public ImageView call;
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
            tv_amount=(TextView)view.findViewById(R.id.tv_amount);
            amount_title=(TextView)view.findViewById(R.id.amount_title);
        }
    }


    public OrderServedDisplayDetailsAdapter(List<BtechOrderModel> btechOrderModels, HomeScreenActivity activity/*, DispatchToHubAdapterOnItemClickedDelegate mCallback*/) {
        // this.mcallback = mCallback;
        this.btechOrderModels = btechOrderModels;
        this.activity = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_served, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BtechOrderModel btechOrderModel = btechOrderModels.get(position);

        final int pos = position;
        if (btechOrderModels != null) {

            holder.txt_name.setText("" + btechOrderModel.getOrderBy());
            holder.tv_orderno.setText("" + btechOrderModel.getOrderNo());
            holder.tv_status.setText("" + btechOrderModel.getStatus());
            int sr_no=pos+1;
            holder.txt_sr_no.setText(""+sr_no);
            ArrayList<String> mylist = new ArrayList<String>();
            for (int i = 0; i < btechOrderModel.getBtchBracodeDtl().size(); i++) {
                mylist.add(btechOrderModel.getBtchBracodeDtl().get(i).getBarcode());
                Logger.error("barcode : "+btechOrderModel.getBtchBracodeDtl().get(i).getBarcode());
            }
            String listString="";
            for (String s : mylist){
                if(InputUtils.isNull(listString)) {
                    listString = s;
                }
                else{
                    listString = listString+",\t"+s;
                }
            }
            holder.tv_barcode.setText(""+ listString);
            if(btechOrderModel.getAmountCollected()==0){
                holder.tv_amount.setVisibility(View.GONE);
                holder.amount_title.setVisibility(View.GONE);
            }else {
                holder.tv_amount.setVisibility(View.VISIBLE);
                holder.amount_title.setVisibility(View.VISIBLE);
                holder.tv_amount.setText(""+btechOrderModel.getAmountCollected());
            }

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
                    callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
                    callPatchRequestModel.setDestNumber(btechOrderModel.getMobile());
                    callPatchRequestModel.setVisitID(btechOrderModel.getOrderNo());
                    ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
                    callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
                    callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);

                }
            });

        } else {
            Logger.error("btechOrderModels is null ");
        }
    }
    class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    MaskedPhoneNumber = json;
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                    Logger.error("MaskedPhoneNumber"+MaskedPhoneNumber);

                    appPreferenceManager.setMaskNumber(MaskedPhoneNumber);
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    } else {
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }
    @Override
    public int getItemCount() {
        return btechOrderModels.size();
    }
}
