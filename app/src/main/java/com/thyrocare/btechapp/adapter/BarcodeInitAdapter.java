package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class BarcodeInitAdapter extends RecyclerView.Adapter<BarcodeInitAdapter.MyViewHolder> {


    public int Serumcount = 0, benposition = 0;
    Activity mActivity;
    ArrayList<BeneficiaryBarcodeDetailsModel> barcodedetailArylist;
    BeneficiaryDetailsModel beneficaryWiseScanbarcodeModel;
    private boolean PrimarySerumAdded = false;
    private OnItemClickListener onClickListeners;

    public BarcodeInitAdapter(Activity mActivity, ArrayList<BeneficiaryBarcodeDetailsModel> barcodedetailArylist, int Serumcount, BeneficiaryDetailsModel beneficaryWiseScanbarcodeModel, int benposition) {
        this.mActivity = mActivity;
        this.barcodedetailArylist = barcodedetailArylist;
        this.beneficaryWiseScanbarcodeModel = beneficaryWiseScanbarcodeModel;
        this.Serumcount = Serumcount;
        this.benposition = benposition;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_serumtype, txtSampleType, txtSampleTypeRBS, edtBarcode, edt_barcode_confirm;
        LinearLayout lin_sampleType,ll_sample_type;
        ImageView imgScan, imgDelete, imgScanConfirm, imgDeleteconfirm;

        public MyViewHolder(View view) {
            super(view);
            tv_serumtype = (TextView) view.findViewById(R.id.tv_serumtype);
            txtSampleType = (TextView) view.findViewById(R.id.txt_sample_type);
            lin_sampleType = (LinearLayout) view.findViewById(R.id.lin_sampleType);
            ll_sample_type = (LinearLayout) view.findViewById(R.id.ll_sample_type);
            txtSampleTypeRBS = (TextView) view.findViewById(R.id.txt_sample_type_rb);
            edtBarcode = (TextView) view.findViewById(R.id.edt_barcode);
            imgScan = (ImageView) view.findViewById(R.id.scan_barcode_button);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            imgDeleteconfirm = (ImageView) view.findViewById(R.id.imgDeleteconfirm);

            edt_barcode_confirm = view.findViewById(R.id.edt_barcode_confirm);
            imgScanConfirm = view.findViewById(R.id.scan_barcode_button_confirm);

            txtSampleType.setSelected(true);
            txtSampleTypeRBS.setSelected(true);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barcode_scan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        // TODO code to show Primary and secondary serum
        if (barcodedetailArylist.get(position).getSamplType().equals("SERUM")) {
            if (Serumcount > 1) {
                if (PrimarySerumAdded) {
                    holder.tv_serumtype.setText("(SECONDARY)");
                } else {
                    PrimarySerumAdded = true;
                    holder.tv_serumtype.setText("(PRIMARY)");
                }
                holder.tv_serumtype.setVisibility(View.VISIBLE);
            } else {
                holder.tv_serumtype.setVisibility(View.GONE);
            }
        } else {
            holder.tv_serumtype.setVisibility(View.GONE);
        }
        // TODO code to show Primary and secondary serum

        holder.txtSampleType.setText(Global.toCamelCase(barcodedetailArylist.get(position).getSamplType()));
        if (beneficaryWiseScanbarcodeModel.getTestsCode().equalsIgnoreCase("RBS,PPBS") || beneficaryWiseScanbarcodeModel.getTestsCode().equalsIgnoreCase("PPBS,RBS")) {
            if (barcodedetailArylist.get(position).getSamplType().equalsIgnoreCase("FLUORIDE-R")) {
                holder.txtSampleType.setText(Global.toCamelCase("FLUORIDE RBS"));
            }
        }
        if (barcodedetailArylist.get(position).getSamplType().equals("SERUM")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("EDTA")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("FLUORIDE")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("HEPARIN")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("URINE")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("FLUORIDE-F")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("FLUORIDE-R")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSamplType().equals("FLUORIDE-PP")) {
            holder.ll_sample_type.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        }

        Logger.error("beneficiaryBarcodeDetailsModel.getBarcode() " + barcodedetailArylist.get(position).getBarcode());
        Logger.error("barcode value: " + barcodedetailArylist.get(position).getBarcode());


        if (!TextUtils.isEmpty(barcodedetailArylist.get(position).getBarcode())) {
            holder.edtBarcode.setText("  " + barcodedetailArylist.get(position).getBarcode() + "  ");
            holder.imgScan.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgScan.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(barcodedetailArylist.get(position).getRescanBarcode())) {
            holder.edt_barcode_confirm.setText("  " + barcodedetailArylist.get(position).getRescanBarcode() + "  ");
            holder.imgScanConfirm.setVisibility(View.GONE);
            holder.imgDeleteconfirm.setVisibility(View.VISIBLE);
        } else {
            holder.imgScanConfirm.setVisibility(View.VISIBLE);
            holder.imgDeleteconfirm.setVisibility(View.GONE);
        }
        holder.edtBarcode.setText(!InputUtils.isNull(barcodedetailArylist.get(position).getBarcode()) ? "  " + barcodedetailArylist.get(position).getBarcode() + "  " : "");
        holder.edt_barcode_confirm.setText(!InputUtils.isNull(barcodedetailArylist.get(position).getRescanBarcode()) ? "  " + barcodedetailArylist.get(position).getRescanBarcode() + "  " : "");

        final int finalI = position;
        initListeners(holder, position, finalI);

    }

    private void initListeners(@NonNull MyViewHolder holder, final int position, final int finalI) {
        holder.imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeScanClicked(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition);
                }
            }
        });

        holder.imgScanConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeScanClickedConfirm(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition);
                }
            }
        });

        holder.edtBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeScanClicked(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition);
                }
            }
        });

        holder.edt_barcode_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeScanClickedConfirm(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeDelete(barcodedetailArylist.get(position).getBarcode(), benposition, "yes");
                }
            }
        });
        holder.imgDeleteconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeDelete(barcodedetailArylist.get(position).getRescanBarcode(), benposition, "no");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcodedetailArylist.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onClickListeners = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition);

        void onBarcodeScanClickedConfirm(String SampleType, int BenID, int barcodePosition, int BenPosition);

        void onBarcodeDelete(String barcode, int BenPosition, String isflag);

    }

}