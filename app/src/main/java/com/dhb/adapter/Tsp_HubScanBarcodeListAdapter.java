package com.dhb.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.fragment.TSP_HubMasterBarcodeScanFragment;
import com.dhb.fragment.TSP_SendFragment;
import com.dhb.models.data.BtechwithHub_BarcodeDataModel;
import com.dhb.models.data.Tsp_ScanBarcodeDataModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class Tsp_HubScanBarcodeListAdapter extends RecyclerView.Adapter<Tsp_HubScanBarcodeListAdapter.MyViewHolder> {


    private ArrayList<Tsp_ScanBarcodeDataModel> barcodeModels;
    private HomeScreenActivity activity;
    private IntentIntegrator intentIntegrator;
    private CallbackInterface mCallback;

    public void setOnShareClickedListener(TSP_SendFragment tsp_sendFragment) {
        mCallback = tsp_sendFragment;
        Logger.debug("result***123" + String.valueOf(mCallback));
    }


    public interface CallbackInterface {

        /**
         * Callback invoked when clicked
         *
         * @param position - the position
         */
        void onHandleSelection(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView editBarcode;
        ImageView imgGreenTick;
        ImageView imgRedCross;
        View itemView;
        ImageView btnScanBarcode;

        private MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            editBarcode = (TextView) view.findViewById(R.id.editbarcode);
            imgGreenTick = (ImageView) view.findViewById(R.id.scanned_status_green);
            imgRedCross = (ImageView) view.findViewById(R.id.scanned_status_red);
            btnScanBarcode = (ImageView) view.findViewById(R.id.btnScanBarcode);
        }
    }


    public Tsp_HubScanBarcodeListAdapter(ArrayList<Tsp_ScanBarcodeDataModel> barcodeModels, HomeScreenActivity activity) {
        this.barcodeModels = barcodeModels;
        this.activity = activity;

        try {
            Logger.debug("result***" + "try");
        } catch (ClassCastException ex) {
            //.. should log the error or throw and exception
            Log.e("MyAdapter", "Must implement the CallbackInterface in the Activity", ex);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tsp_item_barcode_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Tsp_ScanBarcodeDataModel barcodeModel = barcodeModels.get(position);
        if (barcodeModel != null) {

            holder.editBarcode.setText(barcodeModel.getBarcode());
            System.out.println("##############\nbarcode:" + barcodeModel.getBarcode() + "\nisScanned:" + barcodeModel.isReceived());

            if (barcodeModel.isReceived()) {
                holder.imgRedCross.setVisibility(View.GONE);
                holder.imgGreenTick.setVisibility(View.VISIBLE);
            } else {
                holder.imgRedCross.setVisibility(View.VISIBLE);
                holder.imgGreenTick.setVisibility(View.GONE);
            }
            holder.btnScanBarcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.debug("result***" + "clicked");
                    if (mCallback != null) {
                        Logger.debug("result***" + "callback");
                        mCallback.onHandleSelection(position);
                    }
                }
            });
        } else {
            Logger.error("hubDetailsResponseModel is null ");
        }
    }


    @Override
    public int getItemCount() {
        return barcodeModels.size();
    }
}
