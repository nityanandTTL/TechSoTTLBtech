package com.dhb.adapter;

/**
 * Created by vendor1 on 4/21/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.ScanBarcodeAdapterBarcodeButtonOnItemClickedDelegate;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.utils.api.Logger;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

public class ScanBarcodeAdapter extends RecyclerView.Adapter<ScanBarcodeAdapter.MyViewHolder> {

    private List<BeneficiarySampleTypeDetailsModel> beneficiarySampleTypeDetailsModelList;
    private HomeScreenActivity activity;
    private ScanBarcodeAdapterBarcodeButtonOnItemClickedDelegate mcallback;
    public static String scanned_result="";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sample__type_name;
        public EditText editbarcode;
        ImageView scan_barcode_button;

        public MyViewHolder(View view) {
            super(view);
            //itemView =  view;
            initComp(view);
        }

        private void initComp(View view) {
            sample__type_name = (TextView) view.findViewById(R.id.sample__type_name);
            editbarcode = (EditText) view.findViewById(R.id.editbarcode);
            scan_barcode_button = (ImageView) view.findViewById(R.id.scan_barcode_button);

        }
    }


    public ScanBarcodeAdapter(List<BeneficiarySampleTypeDetailsModel> beneficiarySampleTypeDetailsModel, HomeScreenActivity activity, ScanBarcodeAdapterBarcodeButtonOnItemClickedDelegate mCallback) {
        this.mcallback = mCallback;
        this.beneficiarySampleTypeDetailsModelList = beneficiarySampleTypeDetailsModel;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FoldingCell itemView = (FoldingCell) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barcode_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BeneficiarySampleTypeDetailsModel beneficiarySampleTypeDetailsModel = beneficiarySampleTypeDetailsModelList.get(position);
        final int pos = position;
        if (beneficiarySampleTypeDetailsModel != null) {
            holder.sample__type_name.setText("" + beneficiarySampleTypeDetailsModel.getSampleType());
            holder.scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onItemClicked(pos);
                }
            });
            holder.editbarcode.setText(scanned_result);

        } else {
            Logger.error("beneficiarySampleTypeDetailsModelList is null ");
        }
    }

    @Override
    public int getItemCount() {
        return beneficiarySampleTypeDetailsModelList.size();
    }


}
