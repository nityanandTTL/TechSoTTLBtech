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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.BtechCollectionsAdapterOnscanBarcodeClickedDelegate;
import com.dhb.models.data.BarcodeModel;
import com.dhb.utils.api.Logger;

import java.util.List;

public class BtechCollectionsDetailsAdapter extends RecyclerView.Adapter<BtechCollectionsDetailsAdapter.MyViewHolder> {

    private List<BarcodeModel> barcodeModels;
    HomeScreenActivity activity;
    BtechCollectionsAdapterOnscanBarcodeClickedDelegate mcallback;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sample__type_name;
        public ImageView scan_barcode_button;
        EditText editbarcode;
        View itemView;
        LinearLayout ll_edt_barcode;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            initComp(view);
        }

        private void initComp(View view) {
            sample__type_name = (TextView) view.findViewById(R.id.sample__type_name);
            editbarcode = (EditText) view.findViewById(R.id.editbarcode);
            scan_barcode_button = (ImageView) view.findViewById(R.id.scan_barcode_button);
            ll_edt_barcode = (LinearLayout) view.findViewById(R.id.ll_edt_barcode);

        }
    }


    public BtechCollectionsDetailsAdapter(List<BarcodeModel> barcodeModels, HomeScreenActivity activity, BtechCollectionsAdapterOnscanBarcodeClickedDelegate mCallback) {
        this.mcallback = mCallback;
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
        final BarcodeModel barcodeModel = barcodeModels.get(position);

        final int pos = position;
        if (barcodeModel != null) {
            holder.sample__type_name.setText("" + barcodeModel.getSampleType());
            holder.editbarcode.setText("" + barcodeModel.getBarcode());
            holder.scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcallback.onItemClicked(barcodeModel, position, holder.ll_edt_barcode);

                    //    activity.pushFragments(Bte.newInstance(hubbTechModels.get(pos)), false, false, HubDetailMapDisplayFragmentActivity.TAG_FRAGMENT, R.id.cell_content_view, DispatchToHubFragment.TAG_FRAGMENT);
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
