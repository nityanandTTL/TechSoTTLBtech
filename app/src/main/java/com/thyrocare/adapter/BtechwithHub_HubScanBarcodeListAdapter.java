package com.thyrocare.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.models.data.BtechwithHub_BarcodeDataModel;
import com.thyrocare.utils.api.Logger;

import java.util.ArrayList;

public class BtechwithHub_HubScanBarcodeListAdapter extends RecyclerView.Adapter<BtechwithHub_HubScanBarcodeListAdapter.MyViewHolder> {

    private ArrayList<BtechwithHub_BarcodeDataModel> barcodeModels;
    private HomeScreenActivity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView btech_name;
        TextView barcodetype;
        TextView editBarcode;
        ImageView imgGreenTick;
        ImageView imgRedCross;
        View itemView;

        private MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            btech_name = (TextView) view.findViewById(R.id.btech_name);
            barcodetype = (TextView) view.findViewById(R.id.barcodetype);

            editBarcode = (TextView) view.findViewById(R.id.editbarcode);
            imgGreenTick = (ImageView) view.findViewById(R.id.scanned_status_green);
            imgRedCross = (ImageView) view.findViewById(R.id.scanned_status_red);
        }
    }


    public BtechwithHub_HubScanBarcodeListAdapter(ArrayList<BtechwithHub_BarcodeDataModel> barcodeModels, HomeScreenActivity activity) {
        this.barcodeModels = barcodeModels;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.btechwithhub_item_barcode_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BtechwithHub_BarcodeDataModel barcodeModel = barcodeModels.get(position);
        if (barcodeModel != null) {
            holder.btech_name.setText(barcodeModel.getBtechName());
            holder.btech_name.setSelected(true);

            holder.barcodetype.setText(barcodeModel.getBarcodeType());
            holder.editBarcode.setText(barcodeModel.getBarcode());

            System.out.println("##############\nbarcode:" + barcodeModel.getBarcode() + "\nisScanned:" + barcodeModel.isReceived());
            if (barcodeModel.isReceived()) {
                holder.imgRedCross.setVisibility(View.GONE);
                holder.imgGreenTick.setVisibility(View.VISIBLE);
            } else {
                holder.imgRedCross.setVisibility(View.VISIBLE);
                holder.imgGreenTick.setVisibility(View.GONE);
            }
        } else {
            Logger.error("hubDetailsResponseModel is null ");
        }
    }

    @Override
    public int getItemCount() {
        return barcodeModels.size();
    }
}
