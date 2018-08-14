package com.thyrocare.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.LMEMapDisplayFragmentActivity;
import com.thyrocare.models.data.HubBarcodeModel;
import com.thyrocare.models.data.SampleDropBarcodeList;
import com.thyrocare.utils.api.Logger;

import java.util.ArrayList;
import java.util.List;

public class LMEBarcodescanListListAdapter extends RecyclerView.Adapter<LMEBarcodescanListListAdapter.MyViewHolder> {

    private ArrayList<SampleDropBarcodeList> barcodeModels;
    private LMEMapDisplayFragmentActivity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView editBarcode, txt_samplecnt;
        ImageView imgGreenTick;
        ImageView imgRedCross;
        View itemView;
        LinearLayout ll_edt_barcode;

        private MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            editBarcode = (TextView) view.findViewById(R.id.editbarcode);
            txt_samplecnt = (TextView) view.findViewById(R.id.txt_samplecnt);
            ll_edt_barcode = (LinearLayout) view.findViewById(R.id.ll_edt_barcode);
            imgGreenTick = (ImageView) view.findViewById(R.id.scanned_status_green);
            imgRedCross = (ImageView) view.findViewById(R.id.scanned_status_red);
        }
    }


    public LMEBarcodescanListListAdapter(ArrayList<SampleDropBarcodeList> barcodeModels, LMEMapDisplayFragmentActivity activity) {
        this.barcodeModels = barcodeModels;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barcode_list_lme, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SampleDropBarcodeList barcodeModel = barcodeModels.get(position);
        if (barcodeModel != null) {
            holder.editBarcode.setText(barcodeModel.getMasterBarcode());
            holder.txt_samplecnt.setText("" + barcodeModel.getSampleCount());


            if (barcodeModel.isScanned()) {
                holder.imgRedCross.setVisibility(View.GONE);
                holder.imgGreenTick.setVisibility(View.VISIBLE);
            } else {
                holder.imgRedCross.setVisibility(View.VISIBLE);
                holder.imgGreenTick.setVisibility(View.GONE);
            }

        } else {
            Logger.error("DetailsResponseModel is null ");
        }
    }

    @Override
    public int getItemCount() {
        return barcodeModels.size();
    }
}
