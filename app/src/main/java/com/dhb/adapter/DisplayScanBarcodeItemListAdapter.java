package com.dhb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.delegate.ScanBarcodeIconClickedDelegate;
import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;

import java.util.ArrayList;

/**
 * Created by ISRO on 5/2/2017.
 */
public class DisplayScanBarcodeItemListAdapter extends BaseAdapter{
    private OrderBookingActivity activity;
    private ArrayList<BeneficiarySampleTypeDetailsModel> sampleTypesArr;
    private ArrayList<BeneficiarySampleTypeDetailsModel> scannedSampleTypesArr;
    private ArrayList<BarcodeDetailsModel> barcodeDetailsArr;
    private ScanBarcodeIconClickedDelegate scanBarcodeIconClickedDelegate;
    private LayoutInflater layoutInflater;

    public DisplayScanBarcodeItemListAdapter(OrderBookingActivity activity, ArrayList<BeneficiarySampleTypeDetailsModel> sampleTypesArr, ArrayList<BeneficiarySampleTypeDetailsModel> scannedSampleTypesArr, ArrayList<BarcodeDetailsModel> barcodeDetailsArr, ScanBarcodeIconClickedDelegate scanBarcodeIconClickedDelegate) {
        this.activity = activity;
        this.barcodeDetailsArr = barcodeDetailsArr;
        this.sampleTypesArr = sampleTypesArr;
        this.scannedSampleTypesArr = scannedSampleTypesArr;
        this.scanBarcodeIconClickedDelegate = scanBarcodeIconClickedDelegate;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return sampleTypesArr.size();
    }

    @Override
    public Object getItem(int position) {
        return sampleTypesArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.item_scan_barcode,parent,false);
            holder = new ViewHolder();
            holder.txtSampleType = (TextView) convertView.findViewById(R.id.txt_sample_type);
            holder.edtBarcodeValue = (EditText) convertView.findViewById(R.id.edt_barcode);
            holder.imgBtnScan = (ImageView) convertView.findViewById(R.id.scan_barcode_button);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtSampleType.setText(sampleTypesArr.get(position).getSampleType());
        holder.imgBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcodeIconClickedDelegate.onClicked(barcodeDetailsArr, scannedSampleTypesArr);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView txtSampleType;
        EditText edtBarcodeValue;
        ImageView imgBtnScan;
    }
}
