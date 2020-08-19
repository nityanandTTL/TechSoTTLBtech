package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitCampWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

public class NewCampScanBarcodeAdapter extends RecyclerView.Adapter<NewCampScanBarcodeAdapter.MyViewHolder> {


    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private final AppPreferenceManager appPreferenceManager;
    private final Global globalClass;
    ArrayList<SubmitCampWoeRequestModel.Barcodelist> barcodelistArrayList;
    private OnClickListeners onClickListeners;

    public NewCampScanBarcodeAdapter(Activity activity, ArrayList<SubmitCampWoeRequestModel.Barcodelist> barcodelistArrayList) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        globalClass = new Global(activity);
        this.barcodelistArrayList = barcodelistArrayList;
    }

    public void UpdateBarcodeList(ArrayList<SubmitCampWoeRequestModel.Barcodelist> barcodelistArrayList){
         this.barcodelistArrayList = barcodelistArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_barcodeType, tv_enteredBarcode;
        ImageView img_scan, img_deleteBarcode;
        LinearLayout lin_main;

        public MyViewHolder(View view) {
            super(view);

            tv_barcodeType = (TextView) view.findViewById(R.id.tv_barcodeType);
            tv_enteredBarcode = (TextView) view.findViewById(R.id.tv_enteredBarcode);
            img_scan = (ImageView) view.findViewById(R.id.img_scan);
            img_deleteBarcode = (ImageView) view.findViewById(R.id.img_deleteBarcode);
            lin_main = (LinearLayout) view.findViewById(R.id.lin_main);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.camp_module_scanbarcode_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.img_scan.setVisibility(View.GONE);
        holder.tv_barcodeType.setText(StringUtils.toSentenceCase(barcodelistArrayList.get(position).getSAMPLE_TYPE()));
        if (!StringUtils.isNull(barcodelistArrayList.get(position).getBARCODE())){
            holder.tv_enteredBarcode.setText(barcodelistArrayList.get(position).getBARCODE());
            holder.img_deleteBarcode.setVisibility(View.VISIBLE);
        }else{
            holder.tv_enteredBarcode.setText("");
            holder.img_deleteBarcode.setVisibility(View.GONE);
        }

        holder.tv_enteredBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNull(holder.tv_enteredBarcode.getText().toString())){
                    if (onClickListeners != null){
                        onClickListeners.onScanBarcode(barcodelistArrayList.get(position).getSAMPLE_TYPE(),position);
                    }
                }else{
                    globalClass.showCustomToast(activity,"Please delete existing barcode to scan again.");
                }
            }
        });

        holder.img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null){
                    onClickListeners.onScanBarcode(barcodelistArrayList.get(position).getSAMPLE_TYPE(),position);
                }
            }
        });

        holder.img_deleteBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null){
                    onClickListeners.onDeleteBarcode(barcodelistArrayList.get(position).getBARCODE());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return barcodelistArrayList.size();
    }

    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {

        void onScanBarcode(String sample_type, int position);
        void onDeleteBarcode(String barcode);

    }
}
