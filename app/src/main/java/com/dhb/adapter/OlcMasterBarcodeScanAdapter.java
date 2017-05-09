package com.dhb.adapter;

/**
 * Created by vendor1 on 4/21/2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.OlcPickupActivity;
import com.dhb.delegate.OlcMasterBarcodeScanAdapterDelegate;
import com.dhb.utils.api.Logger;

import java.util.List;

public class OlcMasterBarcodeScanAdapter extends RecyclerView.Adapter<OlcMasterBarcodeScanAdapter.MyViewHolder> {

    private List<String> olcMasterBarcodes;
    OlcPickupActivity activity;
    OlcMasterBarcodeScanAdapterDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText edt_barcode;
        public ImageView scan_barcode_button, add_barcode_button;
        View itemView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            edt_barcode = (EditText) view.findViewById(R.id.edt_barcode);
            scan_barcode_button = (ImageView) view.findViewById(R.id.scan_barcode_button);
            add_barcode_button = (ImageView) view.findViewById(R.id.add_barcode_button);
        }
    }


    public OlcMasterBarcodeScanAdapter(List<String> olcMasterBarcodes, OlcPickupActivity activity, OlcMasterBarcodeScanAdapterDelegate mCallback) {
        this.mcallback = mCallback;
        this.olcMasterBarcodes = olcMasterBarcodes;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_olc_master_barcode, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //  final BtechClientsModel btechClientsModel = btechClientsModels.get(position);

        final int pos = position;
        if (olcMasterBarcodes != null && olcMasterBarcodes.size()>0) {
            holder.edt_barcode.setText("" + olcMasterBarcodes.get(pos));
            if (holder.itemView.getParent() != null)
                ((ViewGroup) holder.itemView.getParent()).removeView(holder.itemView);
            holder.scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onScanClick(position);
                }
            });
            holder.add_barcode_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onAddClick(position);
                }
            });
        } else {
            Logger.error("olcMasterBarcodes is null ");
        }
    }

    @Override
    public int getItemCount() {
        return olcMasterBarcodes.size();
    }
}
