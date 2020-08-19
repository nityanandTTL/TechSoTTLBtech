package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

public class ScanBarcodeViewPagerAdapter extends PagerAdapter {

    private boolean showProduct;
    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst;
    private static boolean Is_RBS_PPBS;
    private OnClickListeners onClickListeners;

    public ScanBarcodeViewPagerAdapter(Activity mActivity, ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst, boolean showProduct) {
        this.mActivity = mActivity;
        this.beneficaryWiseScanbarcodeArylst = beneficaryWiseScanbarcodeArylst;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        this.showProduct = showProduct;
    }


    public void updateScanData(ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst){
        this.beneficaryWiseScanbarcodeArylst = beneficaryWiseScanbarcodeArylst;
    }


    @Override
    public int getCount() {
        return beneficaryWiseScanbarcodeArylst.size();
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.barcode_scan_viewpager_item, container, false);

        TextView tv_benName = (TextView) itemView.findViewById(R.id.tv_benName);
        TextView tv_OrderNo = (TextView) itemView.findViewById(R.id.tv_OrderNo);
        TextView tv_products = (TextView) itemView.findViewById(R.id.tv_products);
        LinearLayout lin_benProduct = (LinearLayout) itemView.findViewById(R.id.lin_benProduct);
        LinearLayout ll_barcodes = (LinearLayout) itemView.findViewById(R.id.ll_barcodes);
        TableLayout tl_barcodes = (TableLayout) itemView.findViewById(R.id.tl_barcodes);
//        Button btn_captureBenBarcodePic = (Button) itemView.findViewById(R.id.btn_captureBenBarcodePic);
        final TextView txt_captureBenBarcodePic = (TextView) itemView.findViewById(R.id.txt_captureBenBarcodePic);
        /*ImageView img_benImage_tick = (ImageView) itemView.findViewById(R.id.img_benImage_tick);
        TextView tv_viewImage = (TextView) itemView.findViewById(R.id.tv_viewImage);*/
        final ImageView imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        final ImageView img_uploadBenVail = (ImageView) itemView.findViewById(R.id.img_uploadBenVail);


        tv_benName.setText(beneficaryWiseScanbarcodeArylst.get(position).getName() + " ("+beneficaryWiseScanbarcodeArylst.get(position).getAge() + "/"+beneficaryWiseScanbarcodeArylst.get(position).getGender()+")");
        tv_OrderNo.setText(beneficaryWiseScanbarcodeArylst.get(position).getOrderNo());

        if (showProduct){
            tv_products.setText(Html.fromHtml("<u>"+beneficaryWiseScanbarcodeArylst.get(position).getTestsCode()+ "</u>"));
            lin_benProduct.setVisibility(View.VISIBLE);
        }else{
            lin_benProduct.setVisibility(View.GONE);
        }



        if (!StringUtils.isNull(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture())){
            txt_captureBenBarcodePic.setText("View Image");
            txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            imgDelete.setVisibility(View.VISIBLE);
            img_uploadBenVail.setVisibility(View.GONE);
        } else {
            txt_captureBenBarcodePic.setText("Upload Image");
            txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags()| 0);
            imgDelete.setVisibility(View.GONE);
            img_uploadBenVail.setVisibility(View.VISIBLE);
        }

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onVialImageDelete(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                }
            }
        });

        initScanBarcodeView(ll_barcodes,tl_barcodes,position);

        img_uploadBenVail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onVenupunturePhotoClicked(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                }
            }
        });

        txt_captureBenBarcodePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_captureBenBarcodePic.getText().toString().equalsIgnoreCase("View Image")) {
                    globalclass.OpenImageDialog(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture(), mActivity);
                } else if (txt_captureBenBarcodePic.getText().toString().equalsIgnoreCase("Upload Image")) {
                    if (onClickListeners != null) {
                        onClickListeners.onVenupunturePhotoClicked(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                    }
                }
            }
        });

        tv_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onViewTestDetailsClicked(beneficaryWiseScanbarcodeArylst.get(position).getLeadId());
                }
            }
        });

        container.addView(itemView);
        return itemView;
    }

    private void initScanBarcodeView(LinearLayout llBarcodes, TableLayout tlBarcodes, final int position) {
        if (beneficaryWiseScanbarcodeArylst != null && beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl() != null && beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size() > 0) {
            tlBarcodes.removeAllViews();

            // TODO code to show Primary and secondary serum
            boolean PrimarySerumAdded = false;
            int serumCount = 0;
            for (int i = 0; i < beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size(); i++) {
                if (beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().get(i).getSamplType().equalsIgnoreCase("SERUM")) {
                    serumCount++;
                }
            }
            // TODO code to show Primary and secondary serum


            for (int i = 0; i < beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size(); i++) {
                final BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().get(i);


                TableRow tr = (TableRow) mActivity.getLayoutInflater().inflate(R.layout.item_scan_barcode, null);
                TextView tv_serumtype = (TextView) tr.findViewById(R.id.tv_serumtype);
                TextView txtSampleType = (TextView) tr.findViewById(R.id.txt_sample_type);
                LinearLayout lin_sampleType = (LinearLayout) tr.findViewById(R.id.lin_sampleType);
                TextView txtSampleTypeRBS = (TextView) tr.findViewById(R.id.txt_sample_type_rb);
                final TextView edtBarcode = (TextView) tr.findViewById(R.id.edt_barcode);
                final ImageView imgScan = (ImageView) tr.findViewById(R.id.scan_barcode_button);
                final ImageView imgDelete = (ImageView) tr.findViewById(R.id.imgDelete);

                txtSampleType.setSelected(true);
                txtSampleTypeRBS.setSelected(true);

                // TODO code to show Primary and secondary serum
                if (beneficiaryBarcodeDetailsModel.getSamplType().equals("SERUM")) {
                    if (serumCount > 1) {
                        if (PrimarySerumAdded) {
                            tv_serumtype.setText("(SECONDARY)");
                        } else {
                            PrimarySerumAdded = true;
                            tv_serumtype.setText("(PRIMARY)");
                        }
                        tv_serumtype.setVisibility(View.VISIBLE);
                    } else {
                        tv_serumtype.setVisibility(View.GONE);
                    }
                } else {
                    tv_serumtype.setVisibility(View.GONE);
                }
                // TODO code to show Primary and secondary serum

                txtSampleType.setText(beneficiaryBarcodeDetailsModel.getSamplType());

                if (beneficaryWiseScanbarcodeArylst.get(position).getTestsCode().equalsIgnoreCase("RBS,PPBS") || beneficaryWiseScanbarcodeArylst.get(position).getTestsCode().equalsIgnoreCase("PPBS,RBS")) {
                    if (beneficiaryBarcodeDetailsModel.getSamplType().equalsIgnoreCase("FLUORIDE-R")){
                        txtSampleType.setText("FLUORIDE RBS");
                    }
                }

                if (beneficiaryBarcodeDetailsModel.getSamplType().equals("SERUM")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("EDTA")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("HEPARIN")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("URINE")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-F")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-R")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryBarcodeDetailsModel.getSamplType().equals("FLUORIDE-PP")) {
                    lin_sampleType.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                Logger.error("beneficiaryBarcodeDetailsModel.getBarcode() " + beneficiaryBarcodeDetailsModel.getBarcode());
                Logger.error("barcode value: " + beneficiaryBarcodeDetailsModel.getBarcode());


                if (!TextUtils.isEmpty(beneficiaryBarcodeDetailsModel.getBarcode())) {
                    edtBarcode.setText("  "+beneficiaryBarcodeDetailsModel.getBarcode()+"  ");
                    imgScan.setVisibility(View.GONE);
                    imgDelete.setVisibility(View.VISIBLE);
                } else {
                    imgScan.setVisibility(View.VISIBLE);
                    imgDelete.setVisibility(View.GONE);
                }
                edtBarcode.setText(!InputUtils.isNull(beneficiaryBarcodeDetailsModel.getBarcode()) ? "  "+beneficiaryBarcodeDetailsModel.getBarcode()+"  " : "");

                final int finalI = i;
                imgScan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListeners != null){
                            onClickListeners.onBarcodeScanClicked(beneficiaryBarcodeDetailsModel.getSamplType(),beneficiaryBarcodeDetailsModel.getBenId(), finalI, position);
                        }
                    }
                });

                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListeners != null) {
                            onClickListeners.onBarcodeDelete(beneficiaryBarcodeDetailsModel.getBarcode(), position);
                        }
                    }
                });

                tlBarcodes.addView(tr);
            }
            llBarcodes.setVisibility(View.VISIBLE);
        }
    }


    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public interface OnClickListeners {
        void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition);

        void onVenupunturePhotoClicked(int BenID, int position);

        void onRefresh();

        void onBarcodeDelete(String barcode, int BenPosition);

        void onVialImageDelete(int BenID, int position);

        void onViewTestDetailsClicked(String benId);
    }
}
