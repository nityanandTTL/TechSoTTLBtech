package com.thyrocare.btechapp.adapter;

import android.app.Activity;
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
import com.thyrocare.btechapp.models.api.response.QrcodeBasedPatientDetailsResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class QrCodeBasedBarcodeScanAdapter extends RecyclerView.Adapter<QrCodeBasedBarcodeScanAdapter.MyViewHolder> {


    public int Serumcount = 0;
    Activity mActivity;
    ArrayList<QrcodeBasedPatientDetailsResponseModel.PatientDataBean.TestDataBean> barcodedetailArylist;
    QrcodeBasedPatientDetailsResponseModel.PatientDataBean patientDetailsModel;

    private boolean PrimarySerumAdded = false;
    private OnItemClickListener onClickListeners;

    public QrCodeBasedBarcodeScanAdapter(Activity mActivity, int Serumcount, QrcodeBasedPatientDetailsResponseModel.PatientDataBean qrcodeBasedPatientDetailsResponseModel) {
        this.mActivity = mActivity;
        this.barcodedetailArylist = qrcodeBasedPatientDetailsResponseModel.getTestData();
        this.patientDetailsModel = qrcodeBasedPatientDetailsResponseModel;
        this.Serumcount = Serumcount;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_serumtype, txtSampleType,txtSampleTypeRBS,edtBarcode;
        LinearLayout lin_sampleType;
        ImageView imgScan,imgDelete;

        public MyViewHolder(View view) {
            super(view);
            tv_serumtype = (TextView) view.findViewById(R.id.tv_serumtype);
            txtSampleType = (TextView) view.findViewById(R.id.txt_sample_type);
            lin_sampleType = (LinearLayout) view.findViewById(R.id.lin_sampleType);
            txtSampleTypeRBS = (TextView) view.findViewById(R.id.txt_sample_type_rb);
            edtBarcode = (TextView) view.findViewById(R.id.edt_barcode);
            imgScan = (ImageView) view.findViewById(R.id.scan_barcode_button);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // TODO code to show Primary and secondary serum
        if (barcodedetailArylist.get(position).getSampleType().equals("SERUM")) {
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

        holder.txtSampleType.setText(barcodedetailArylist.get(position).getSampleType());

        if (patientDetailsModel.getProduct().equalsIgnoreCase("RBS,PPBS") || patientDetailsModel.getProduct().equalsIgnoreCase("PPBS,RBS")) {
            if (barcodedetailArylist.get(position).getSampleType().equalsIgnoreCase("FLUORIDE-R")){
                holder.txtSampleType.setText("FLUORIDE RBS");
            }
        }

        if (barcodedetailArylist.get(position).getSampleType().equals("SERUM")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("EDTA")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("FLUORIDE")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("HEPARIN")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("URINE")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("FLUORIDE-F")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("FLUORIDE-R")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        } else if (barcodedetailArylist.get(position).getSampleType().equals("FLUORIDE-PP")) {
            holder.lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
        }
        Logger.error("beneficiaryBarcodeDetailsModel.getBarcode() " + barcodedetailArylist.get(position).getBarcode());
        Logger.error("barcode value: " + barcodedetailArylist.get(position).getBarcode());


        if (!TextUtils.isEmpty(barcodedetailArylist.get(position).getBarcode())) {
            holder.edtBarcode.setText("  "+barcodedetailArylist.get(position).getBarcode()+"  ");
            holder.imgScan.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgScan.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.GONE);
        }
        holder.edtBarcode.setText(!InputUtils.isNull(barcodedetailArylist.get(position).getBarcode()) ? "  "+barcodedetailArylist.get(position).getBarcode()+"  " : "");

        final int finalI = position;
        initListeners(holder, position, finalI);
    }

    private void initListeners(@NonNull MyViewHolder holder, final int position, final int finalI) {
        holder.imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null){
                    onClickListeners.onBarcodeScanClicked(barcodedetailArylist.get(position).getSampleType(), finalI);
                }
            }
        });

        holder.edtBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null){
                    onClickListeners.onBarcodeScanClicked(barcodedetailArylist.get(position).getSampleType(),finalI);
                }
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onBarcodeDelete(barcodedetailArylist.get(position).getBarcode());
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
        void onBarcodeScanClicked(String SampleType, int barcodePosition);
        void onBarcodeDelete(String barcode);

    }
}
