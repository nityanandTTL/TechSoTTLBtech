package com.thyrocare.btechapp.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

        if (!InputUtils.isNull(beneficaryWiseScanbarcodeModel.getTestsCode())) {
            if (beneficaryWiseScanbarcodeModel.getTestsCode().equalsIgnoreCase("RBS,PPBS") || beneficaryWiseScanbarcodeModel.getTestsCode().equalsIgnoreCase("PPBS,RBS")) {
                if (barcodedetailArylist.get(position).getSamplType().equalsIgnoreCase("FLUORIDE-R")) {
                    holder.txtSampleType.setText(Global.toCamelCase("FLUORIDE RBS"));
                }
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
        holder.edt_ben_code.setTag(position);
        holder.edtBarcode.setText(!InputUtils.isNull(barcodedetailArylist.get(position).getBarcode()) ? "  " + barcodedetailArylist.get(position).getBarcode() + "  " : "");
        holder.edt_barcode_confirm.setText(!InputUtils.isNull(barcodedetailArylist.get(position).getRescanBarcode()) ? "  " + barcodedetailArylist.get(position).getRescanBarcode() + "  " : "");
        holder.edt_ben_code.setText(!InputUtils.isNull(beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getBenCode()) ? beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getBenCode() : "");

        if (!InputUtils.isNull(beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getIsBenCodeCorrect()) && !InputUtils.isNull(beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getBarcode()))
            if (!beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getIsBenCodeCorrect()) {
                holder.edt_ben_code.setError("Please enter proper beneficiary code");
                holder.edt_ben_code.requestFocus();
            } else {
                holder.edt_ben_code.setError(null);
            }

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
                    String strbenCode = holder.edt_ben_code.getText().toString();
                    onClickListeners.onBarcodeScanClickedConfirm(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition, strbenCode);
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
//                    String strbenCode = holder.edt_ben_code.getText().toString();
                    if (!InputUtils.isNull(beneficaryWiseScanbarcodeModel.getBarcodedtl().get(position).getBenCode())) {
                        onClickListeners.onBarcodeScanClickedConfirm(barcodedetailArylist.get(position).getSamplType(), beneficaryWiseScanbarcodeModel.getBenId(), finalI, benposition, "");
                    } else {
                        Toast.makeText(mActivity, "Please enter bencode to scan barcode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.edt_ben_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("sample postion>>>>>>>>>>"+ holder.edt_ben_code.getTag());
                onClickListeners.getBenCode((Integer) holder.edt_ben_code.getTag(), benposition, editable.toString());
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

        void onBarcodeScanClickedConfirm(String SampleType, int BenID, int barcodePosition, int BenPosition, String strbenCode);

        void onBarcodeDelete(String barcode, int BenPosition, String isflag);

        void getBenCode(int samplePos, int benposition, String editable);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_serumtype, txtSampleType, txtSampleTypeRBS, edtBarcode, edt_barcode_confirm;
        LinearLayout lin_sampleType, ll_sample_type;
        ImageView imgScan, imgDelete, imgScanConfirm, imgDeleteconfirm;
        TextInputEditText edt_ben_code;
        TextInputLayout txtinput_edtbencode;
        CardView cv_edt_barcode;

        public MyViewHolder(View view) {
            super(view);
            super.setIsRecyclable(false);
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

            edt_ben_code = view.findViewById(R.id.edt_ben_code);

            cv_edt_barcode = view.findViewById(R.id.cv_edt_barcode);
            txtinput_edtbencode = view.findViewById(R.id.txtinput_edtbencode);
            edt_ben_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4), new InputFilter.AllCaps()});

            txtSampleType.setSelected(true);
            txtSampleTypeRBS.setSelected(true);


        }
    }
}