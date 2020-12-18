package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.adapter.BarcodeInitAdapter;
import com.thyrocare.btechapp.adapter.DisplayVideoListAdapter;
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
    private BarcodeInitAdapter barcodeinitAdapter;

    public ScanBarcodeViewPagerAdapter(Activity mActivity, ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst, boolean showProduct) {
        this.mActivity = mActivity;
        this.beneficaryWiseScanbarcodeArylst = beneficaryWiseScanbarcodeArylst;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        this.showProduct = showProduct;
    }


    public void updateScanData(ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst) {
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
//        Button btn_captureBenBarcodePic = (Button) itemView.findViewById(R.id.btn_captureBenBarcodePic);
        final TextView txt_captureBenBarcodePic = (TextView) itemView.findViewById(R.id.txt_captureBenBarcodePic);
        /*ImageView img_benImage_tick = (ImageView) itemView.findViewById(R.id.img_benImage_tick);
        TextView tv_viewImage = (TextView) itemView.findViewById(R.id.tv_viewImage);*/
        final ImageView imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        final ImageView img_uploadBenVail = (ImageView) itemView.findViewById(R.id.img_uploadBenVail);
        RecyclerView recyle_barcode = (RecyclerView) itemView.findViewById(R.id.recyle_barcode);

        final EditText edt_srf = (EditText) itemView.findViewById(R.id.edt_srf);
        ImageView img_resetSRF = (ImageView) itemView.findViewById(R.id.img_resetSRF);
        TextView tv_saveSRF = (TextView) itemView.findViewById(R.id.tv_saveSRF);


        tv_benName.setText(beneficaryWiseScanbarcodeArylst.get(position).getName() + " (" + beneficaryWiseScanbarcodeArylst.get(position).getAge() + "/" + beneficaryWiseScanbarcodeArylst.get(position).getGender() + ")");
        tv_OrderNo.setText(beneficaryWiseScanbarcodeArylst.get(position).getOrderNo());

        if (showProduct) {
            tv_products.setText(Html.fromHtml("<u>" + beneficaryWiseScanbarcodeArylst.get(position).getTestsCode() + "</u>"));
            lin_benProduct.setVisibility(View.VISIBLE);
        } else {
            lin_benProduct.setVisibility(View.GONE);
        }


        if (!StringUtils.isNull(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture())) {
            txt_captureBenBarcodePic.setText("View Image");
            txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            imgDelete.setVisibility(View.VISIBLE);
            img_uploadBenVail.setVisibility(View.GONE);
        } else {
            txt_captureBenBarcodePic.setText("Upload Image");
            txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags() | 0);
            imgDelete.setVisibility(View.GONE);
            img_uploadBenVail.setVisibility(View.VISIBLE);
        }

        if (!StringUtils.isNull(beneficaryWiseScanbarcodeArylst.get(position).getSRFID())) {
            edt_srf.setText(beneficaryWiseScanbarcodeArylst.get(position).getSRFID());
            edt_srf.setEnabled(false);
            tv_saveSRF.setVisibility(View.GONE);
            img_resetSRF.setVisibility(View.VISIBLE);
        } else {
            edt_srf.setText("");
            edt_srf.setEnabled(true);
            tv_saveSRF.setVisibility(View.VISIBLE);
            img_resetSRF.setVisibility(View.GONE);
        }


        initScanBarcodeView(position, recyle_barcode);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onVialImageDelete(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                }
            }
        });

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
                    globalclass.OpenImageDialog(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture(), mActivity, false);
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

        tv_saveSRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SrfID = edt_srf.getText().toString().trim();
                if (!InputUtils.isNull(SrfID) && SrfID.length() > 4) {
                    if (onClickListeners != null) {
                        onClickListeners.onSRFSaved(SrfID, position, beneficaryWiseScanbarcodeArylst.get(position).getBenId());
                    }
                } else {
                    Global.showCustomStaticToast(mActivity, ConstantsMessages.InvalidSRFIDMsg);
                }

            }
        });

        img_resetSRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onSRFDeleted(beneficaryWiseScanbarcodeArylst.get(position).getSRFID(), position);
                }
            }
        });

        container.addView(itemView);
        return itemView;
    }

    private void initScanBarcodeView(final int position, RecyclerView recyle_barcode) {
        if (beneficaryWiseScanbarcodeArylst != null && beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl() != null && beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size() > 0) {
            // TODO code to show Primary and secondary serum
            int serumCount = 0;
            for (int i = 0; i < beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size(); i++) {
                if (beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().get(i).getSamplType().equalsIgnoreCase("SERUM")) {
                    serumCount++;
                }
            }

            recyle_barcode.setLayoutManager(new LinearLayoutManager(mActivity));
            barcodeinitAdapter = new BarcodeInitAdapter(mActivity, beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl(), serumCount, beneficaryWiseScanbarcodeArylst.get(position), position);
            barcodeinitAdapter.setOnItemClickListener(new BarcodeInitAdapter.OnItemClickListener() {
                @Override
                public void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition) {
                    if (onClickListeners != null) {
                        onClickListeners.onBarcodeScanClicked(SampleType, BenID, barcodePosition, BenPosition);
                    }
                }

                @Override
                public void onBarcodeDelete(String barcode, int BenPosition) {
                    if (onClickListeners != null) {
                        onClickListeners.onBarcodeDelete(barcode, BenPosition);
                    }
                }
            });
            recyle_barcode.setAdapter(barcodeinitAdapter);
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

        void onSRFSaved(String srfId, int BenPosition, int BenID);

        void onSRFDeleted(String SRFID, int BenPosition);

    }
}
