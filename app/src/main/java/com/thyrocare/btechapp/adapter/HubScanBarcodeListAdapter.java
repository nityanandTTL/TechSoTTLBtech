package com.thyrocare.btechapp.adapter;

/**
 * Created by Orion on 4/21/2017.
 */

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.models.data.HubBarcodeModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.List;

public class HubScanBarcodeListAdapter extends RecyclerView.Adapter<HubScanBarcodeListAdapter.MyViewHolder> {

    private List<HubBarcodeModel> barcodeModels;
    private Activity activity;

    public HubScanBarcodeListAdapter(List<HubBarcodeModel> barcodeModels, Activity activity) {
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

            if (!InputUtils.isNull(barcodeModel.getLocation())) {
                if (barcodeModel.getLocation().equalsIgnoreCase(ConstantsMessages.RPL)) {
                    holder.editBarcode.setTextColor(activity.getResources().getColor(R.color.blue_shade));
                } else if (barcodeModel.getLocation().equalsIgnoreCase(ConstantsMessages.CPL)) {
                    holder.editBarcode.setTextColor(activity.getResources().getColor(R.color.highlight_color));
                } else if (barcodeModel.getLocation().equalsIgnoreCase(ConstantsMessages.ZPL)) {
                    holder.editBarcode.setTextColor(activity.getResources().getColor(R.color.sample_type_serum));
                }
            } else {
                holder.editBarcode.setTextColor(activity.getResources().getColor(R.color.black));
            }

            holder.sample_type_name.setText(barcodeModel.getSampleType());
            if (barcodeModel.getSampleType().equals("SERUM")) {
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
            } else if (barcodeModel.getSampleType().equals("EDTA")) {
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
            } else if (barcodeModel.getSampleType().equals("FLUORIDE")) {
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
            } else if (barcodeModel.getSampleType().equals("HEPARIN")) {
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
            } else if (barcodeModel.getSampleType().equals("URINE")) {
                holder.sample_type_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
            }
            holder.editBarcode.setText(barcodeModel.getBarcode());

            MessageLogger.PrintMsg("##############\nbarcode:" + barcodeModel.getBarcode() + "\nisScanned:" + barcodeModel.isScanned());
            if (barcodeModel.isScanned()) {
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
}
