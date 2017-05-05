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
import com.dhb.utils.app.InputUtils;

import java.util.ArrayList;

/**
 * Created by ISRO on 5/2/2017.
 */
public class DisplayScanBarcodeItemListAdapter extends BaseAdapter{
    private OrderBookingActivity activity;
    private ArrayList<BarcodeDetailsModel> barcodeDetailsArr;
    private ScanBarcodeIconClickedDelegate scanBarcodeIconClickedDelegate;
    private LayoutInflater layoutInflater;

    public DisplayScanBarcodeItemListAdapter(OrderBookingActivity activity, ArrayList<BarcodeDetailsModel> barcodeDetailsArr, ScanBarcodeIconClickedDelegate scanBarcodeIconClickedDelegate) {
        this.activity = activity;
        this.barcodeDetailsArr = barcodeDetailsArr;
        this.scanBarcodeIconClickedDelegate = scanBarcodeIconClickedDelegate;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return barcodeDetailsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return barcodeDetailsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.txtSampleType.setText(barcodeDetailsArr.get(position).getSamplType());

        if(!InputUtils.isNull(barcodeDetailsArr.get(position).getBarcode())){
            holder.edtBarcodeValue.setText(barcodeDetailsArr.get(position).getBarcode());
            holder.edtBarcodeValue.setBackground(activity.getResources().getDrawable(R.drawable.custom_border_green));
        }
        holder.imgBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcodeIconClickedDelegate.onClicked(barcodeDetailsArr.get(position).getSamplType());
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
