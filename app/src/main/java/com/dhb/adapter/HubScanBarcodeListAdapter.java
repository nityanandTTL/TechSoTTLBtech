package com.dhb.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.models.data.HubBarcodeModel;
import com.dhb.utils.api.Logger;

import java.util.List;

public class HubScanBarcodeListAdapter extends RecyclerView.Adapter<HubScanBarcodeListAdapter.MyViewHolder> {

    private List<HubBarcodeModel> barcodeModels;
    private HomeScreenActivity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sample_type_name;
        TextView editBarcode;
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
            sample_type_name = (TextView) view.findViewById(R.id.sample_type_name);
            editBarcode = (TextView) view.findViewById(R.id.editbarcode);
            ll_edt_barcode = (LinearLayout) view.findViewById(R.id.ll_edt_barcode);
            imgGreenTick = (ImageView) view.findViewById(R.id.scanned_status_green);
            imgRedCross = (ImageView) view.findViewById(R.id.scanned_status_red);
        }
    }


    public HubScanBarcodeListAdapter(List<HubBarcodeModel> barcodeModels, HomeScreenActivity activity) {
        this.barcodeModels = barcodeModels;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barcode_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HubBarcodeModel barcodeModel = barcodeModels.get(position);
        if (barcodeModel != null) {
            holder.sample_type_name.setText(barcodeModel.getSampleType());
            if(barcodeModel.getSampleType().equals("SERUM")){
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
            }
            else if(barcodeModel.getSampleType().equals("EDTA")){
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
            }
            else if(barcodeModel.getSampleType().equals("FLUORIDE")){
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
            }
            else if(barcodeModel.getSampleType().equals("HEPARIN")){
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
            }
            else if(barcodeModel.getSampleType().equals("URINE")){
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
            }
            holder.editBarcode.setText(barcodeModel.getBarcode());


            System.out.println("##############\nbarcode:" + barcodeModel.getBarcode()+"\nisScanned:"+barcodeModel.isScanned());
            if(barcodeModel.isScanned()){
                holder.imgRedCross.setVisibility(View.GONE);
                holder.imgGreenTick.setVisibility(View.VISIBLE);
            }
            else{
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
