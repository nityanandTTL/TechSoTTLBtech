package com.thyrocare.btechapp.NewScreenDesigns.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thyrocare.btechapp.Controller.RemoveUrineSampleController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.ScanBarcodeWoeActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.BarcodeInitAdapter;
import com.thyrocare.btechapp.adapter.DisplayVideoListAdapter;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.RemoveUrineReqModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.RemoveUrineSampleRespModel;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;

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
    private String mobile = "";
    private boolean flagremove = false;
    boolean isHCL, isOTP;
    String filename, filepath;

    public ScanBarcodeViewPagerAdapter(Activity mActivity, ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst, boolean showProduct, String mobile, boolean isHCL, String filename, boolean isOTP) {
        this.mActivity = mActivity;
        this.beneficaryWiseScanbarcodeArylst = beneficaryWiseScanbarcodeArylst;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        this.showProduct = showProduct;
        this.isHCL = isHCL;
        this.isOTP = isOTP;
        this.mobile = mobile;
        this.filename = filename;
    }


    public void updateScanData(ArrayList<BeneficiaryDetailsModel> beneficaryWiseScanbarcodeArylst, String filename, String filepath) {
        this.beneficaryWiseScanbarcodeArylst = beneficaryWiseScanbarcodeArylst;
        this.filename = filename;
        this.filepath = filepath;
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
        TextView tv_srf_mob = (TextView) itemView.findViewById(R.id.tv_srf_mob);
        TextView tv_OrderNo = (TextView) itemView.findViewById(R.id.tv_OrderNo);
        TextView tv_products = (TextView) itemView.findViewById(R.id.tv_products);
        RelativeLayout ll_srfID = (RelativeLayout) itemView.findViewById(R.id.ll_srfID);
        CardView cv_rel = itemView.findViewById(R.id.cv_rel);
        LinearLayout lin_benProduct = (LinearLayout) itemView.findViewById(R.id.lin_benProduct);
        LinearLayout lin_BenAffidavitPic = (LinearLayout) itemView.findViewById(R.id.lin_BenAffidavitPic);
//        Button btn_captureBenBarcodePic = (Button) itemView.findViewById(R.id.btn_captureBenBarcodePic);
        final TextView txt_captureBenBarcodePic = (TextView) itemView.findViewById(R.id.txt_captureBenBarcodePic);
        final TextView txt_captureAffidavitPic = (TextView) itemView.findViewById(R.id.txt_captureAffidavitPic);
        /*ImageView img_benImage_tick = (ImageView) itemView.findViewById(R.id.img_benImage_tick);
        TextView tv_viewImage = (TextView) itemView.findViewById(R.id.tv_viewImage);*/
        final ImageView imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        final ImageView imgDeleteA = (ImageView) itemView.findViewById(R.id.imgDeleteA);
        final ImageView img_uploadBenVail = (ImageView) itemView.findViewById(R.id.img_uploadBenVail);
        ImageView img_uploadAffidavit = (ImageView) itemView.findViewById(R.id.img_uploadAffidavit);
        RecyclerView recyle_barcode = (RecyclerView) itemView.findViewById(R.id.recyle_barcode);

        final EditText edt_srf = (EditText) itemView.findViewById(R.id.edt_srf);
        ImageView img_resetSRF = (ImageView) itemView.findViewById(R.id.img_resetSRF);

        TextView tv_saveSRF = (TextView) itemView.findViewById(R.id.tv_saveSRF);
        ImageView btn_remove = (ImageView) itemView.findViewById(R.id.btn_remove);
        LinearLayout ll_urine = itemView.findViewById(R.id.ll_urine);
        LinearLayout ll_grey = itemView.findViewById(R.id.ll_grey);
        LinearLayout ll_view = itemView.findViewById(R.id.ll_view);
        final TextView btn_sendopt = itemView.findViewById(R.id.btn_sendopt);
        TextView btn_verify_otp = itemView.findViewById(R.id.btn_verify_otp);
        final TextView tv_number = (TextView) itemView.findViewById(R.id.tv_number);
        final EditText edt_verify = (EditText) itemView.findViewById(R.id.edt_verify);
        final LinearLayout ll_otpvalidate = (LinearLayout) itemView.findViewById(R.id.ll_otpvalidate);

        try {
            if (beneficaryWiseScanbarcodeArylst.get(position).isCovidOrder()) {
                edt_srf.setHint("SRF ID*");
                cv_rel.setVisibility(View.VISIBLE);
                ll_srfID.setVisibility(View.VISIBLE);
            } else {
                edt_srf.setHint("SRF ID");
                cv_rel.setVisibility(View.GONE);
                ll_srfID.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ll_srfID.getVisibility() == View.VISIBLE) {
            tv_srf_mob.setVisibility(View.VISIBLE);
            tv_srf_mob.setText("Mobile - " + mobile);
        } else {
            tv_srf_mob.setVisibility(View.GONE);
        }

        if (isHCL) {
            lin_BenAffidavitPic.setVisibility(View.VISIBLE);
            ll_view.setVisibility(View.VISIBLE);

            if (!StringUtils.isNull(filename)) {

                if (filename.contains(".pdf")) {
                    txt_captureAffidavitPic.setText(filename);
                    txt_captureAffidavitPic.setPaintFlags(txt_captureAffidavitPic.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_captureAffidavitPic.setVisibility(View.VISIBLE);
                    txt_captureAffidavitPic.setSelected(true);
                    imgDeleteA.setVisibility(View.VISIBLE);
                    img_uploadBenVail.setVisibility(View.VISIBLE);
                } else {
                    txt_captureAffidavitPic.setText("View Image");
                    txt_captureAffidavitPic.setPaintFlags(txt_captureAffidavitPic.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_captureAffidavitPic.setVisibility(View.VISIBLE);
                    txt_captureAffidavitPic.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    imgDelete.setVisibility(View.VISIBLE);
                    img_uploadBenVail.setVisibility(View.VISIBLE);
                }
            } else {
                txt_captureAffidavitPic.setText("Upload");
                txt_captureAffidavitPic.setPaintFlags(txt_captureAffidavitPic.getPaintFlags() | 0);
                imgDeleteA.setVisibility(View.GONE);
                txt_captureAffidavitPic.setVisibility(View.GONE);
                img_uploadBenVail.setVisibility(View.VISIBLE);
            }
        }


        tv_benName.setText(Global.toCamelCase(beneficaryWiseScanbarcodeArylst.get(position).getName()) + " (" + beneficaryWiseScanbarcodeArylst.get(position).getGender() + " | " + beneficaryWiseScanbarcodeArylst.get(position).getAge() + " years" + ")");
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
            txt_captureBenBarcodePic.setVisibility(View.VISIBLE);
            imgDelete.setVisibility(View.VISIBLE);
            img_uploadBenVail.setVisibility(View.VISIBLE);
        } else {
            txt_captureBenBarcodePic.setText("Upload Image");
            txt_captureBenBarcodePic.setPaintFlags(txt_captureBenBarcodePic.getPaintFlags() | 0);
            imgDelete.setVisibility(View.GONE);
            txt_captureBenBarcodePic.setVisibility(View.GONE);
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

        setUrineLogic(ll_grey, ll_urine, position);

        initScanBarcodeView(position, recyle_barcode);


        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
                    if (!isOTP) {
                        CallRemoveSample(beneficaryWiseScanbarcodeArylst.get(position));
                    } else {
                        ll_otpvalidate.setVisibility(View.VISIBLE);
                        tv_number.setText("OTP will be sent to " + mobile);
                    }
                } else {
                    ll_otpvalidate.setVisibility(View.VISIBLE);
                    tv_number.setText("OTP will be sent to " + mobile);
                }
            }
        });

        btn_sendopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallsendOTPAPIforOrderEdit(beneficaryWiseScanbarcodeArylst.get(position).getOrderNo(), btn_sendopt);
            }
        });

        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ValidateOTP(edt_verify.getText().toString())) {
                    OrderPassRequestModel model = new OrderPassRequestModel();
                    model.setMobile(mobile);
                    model.setOTP(edt_verify.getText().toString());
                    model.setVisitId(beneficaryWiseScanbarcodeArylst.get(position).getOrderNo());
                    if (cd.isConnectingToInternet()) {
                        CallValidateOTPAPI(model, beneficaryWiseScanbarcodeArylst.get(position));
                    } else {
                        globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
                    }
                }
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onVialImageDelete(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                }
            }
        });

        imgDeleteA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                removePDF(txt_captureAffidavitPic.getText().toString(),txt_captureAffidavitPic,imgDeleteA);

                if (onClickListeners != null) {
                    onClickListeners.onAffidavitDelete(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
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

        img_uploadAffidavit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListeners != null) {
                    onClickListeners.onAffidavitClicked(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                }
            }
        });

        txt_captureBenBarcodePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_captureBenBarcodePic.getText().toString().equalsIgnoreCase("View Image")) {
                    if (onClickListeners != null) {
                        onClickListeners.onImageShow(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture(), beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position, 0);
                    }
//                   globalclass.OpenImageDialog(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture(), mActivity, false);
                } else if (txt_captureBenBarcodePic.getText().toString().equalsIgnoreCase("Upload Image")) {
                    if (onClickListeners != null) {
                        onClickListeners.onVenupunturePhotoClicked(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
                    }
                }
            }
        });

        txt_captureAffidavitPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_captureAffidavitPic.getText().toString().equalsIgnoreCase("View Image")) {
                    if (onClickListeners != null) {
                        onClickListeners.onImageShow(filepath, beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position, 1);
                    }
//                   globalclass.OpenImageDialog(beneficaryWiseScanbarcodeArylst.get(position).getVenepuncture(), mActivity, false);
                } else if (txt_captureAffidavitPic.getText().toString().equalsIgnoreCase("Upload")) {
                    if (onClickListeners != null) {
                        onClickListeners.onAffidavitClicked(beneficaryWiseScanbarcodeArylst.get(position).getBenId(), position);
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

    private void removePDF(String strPDF, TextView textView, ImageView iv) {
        if (strPDF.length() != 0) {
            textView.setText("");
            iv.setVisibility(View.GONE);
        }
    }

    private void setUrineLogic(LinearLayout ll_grey, LinearLayout ll_urine, int position) {

        if (!InputUtils.isNull(beneficaryWiseScanbarcodeArylst.get(position))) {
            for (int i = 0; i < beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size(); i++) {
                if (beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().get(i).getSamplType().equalsIgnoreCase("URINE")) {
                    if (beneficaryWiseScanbarcodeArylst.get(position).getBarcodedtl().size() > 1) {
                        ll_grey.setVisibility(View.VISIBLE);
                        ll_urine.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
    }

    private boolean ValidateOTP(String s) {
        if (InputUtils.isNull(s)) {
            globalclass.showCustomToast(mActivity, "Enter OTP");
            return false;
        }
        if (s.length() > 4) {
            globalclass.showCustomToast(mActivity, "Enter Valid OTP");
            return false;
        }
        return true;
    }


    private void CallsendOTPAPIforOrderEdit(String order, final TextView btn_sendopt) {

        SendOTPRequestModel model = new SendOTPRequestModel();
        model.setMobile(mobile);
        model.setOrderno(order);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<CommonResponseModel2> responseCall = apiInterface.CallSendOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<CommonResponseModel2>() {
            @Override
            public void onResponse(Call<CommonResponseModel2> call, Response<CommonResponseModel2> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    CommonResponseModel2 responseModel = response.body();
                    if (!TextUtils.isEmpty(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
                        globalclass.showCustomToast(mActivity, "OTP send successfully to mobile number mapped to this order.");
                        btn_sendopt.setEnabled(false);
//                      btn_sendopt.setText("Re-send OTP");
                    } else {
                        globalclass.showCustomToast(mActivity, "OTP Generation Failed.");
                    }
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel2> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });
    }

    private void CallValidateOTPAPI(OrderPassRequestModel model, final BeneficiaryDetailsModel beneficiaryDetailsModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(mActivity, "Requesting for OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    String strresponse = response.body();
                    if (!TextUtils.isEmpty(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                        globalclass.showCustomToast(mActivity, "OTP Validated Successfully.");
                        CallRemoveSample(beneficiaryDetailsModel);
                    } else {
                        globalclass.showCustomToast(mActivity, "Invalid OTP.");
                    }
                } else if (response.code() == 401) {
                    globalclass.showCustomToast(mActivity, "Invalid OTP.");
                } else {
                    globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showCustomToast(mActivity, MSG_SERVER_EXCEPTION);
            }
        });

    }

    private void CallRemoveSample(BeneficiaryDetailsModel beneficiaryDetailsModel) {

        if (cd.isConnectingToInternet()) {
            RemoveUrineReqModel removeUrineReqModel = new RemoveUrineReqModel();

            removeUrineReqModel.setRemarks("");
            removeUrineReqModel.setSampleType("URINE");
            removeUrineReqModel.setOrderNo("" + beneficiaryDetailsModel.getOrderNo());
            removeUrineReqModel.setBenID(beneficiaryDetailsModel.getBenId());
            removeUrineReqModel.setBtechID(appPreferenceManager.getLoginResponseModel().getUserID());

            RemoveUrineSampleController removeUrineSampleController = new RemoveUrineSampleController(this, mActivity);
            removeUrineSampleController.CallAPI(removeUrineReqModel, beneficiaryDetailsModel);
        } else {
            globalclass.showCustomToast(mActivity, mActivity.getResources().getString(R.string.plz_chk_internet));
        }

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
                public void onBarcodeScanClickedConfirm(String SampleType, int BenID, int barcodePosition, int BenPosition) {
                    if (onClickListeners != null) {
                        onClickListeners.onBarcodeScanClickedConfirm(SampleType, BenID, barcodePosition, BenPosition);
                    }
                }

                @Override
                public void onBarcodeDelete(String barcode, int BenPosition, String flag) {
                    if (onClickListeners != null) {
                        onClickListeners.onBarcodeDelete(barcode, BenPosition, flag);
                    }
                }
            });

            recyle_barcode.setAdapter(barcodeinitAdapter);
        }
    }


    public void setOnItemClickListener(OnClickListeners onClickListeners) {
        this.onClickListeners = onClickListeners;
    }

    public void getResponse(RemoveUrineSampleRespModel body, BeneficiaryDetailsModel beneficiaryDetailsModel) {
        if (!InputUtils.isNull(body.getResID()) && body.getResID().equalsIgnoreCase("RES0000")) {
            globalclass.showCustomToast(mActivity, "" + body.getResponse());
            if (!InputUtils.isNull(beneficiaryDetailsModel)) {
                for (int i = 0; i < beneficiaryDetailsModel.getBarcodedtl().size(); i++) {
                    if (beneficiaryDetailsModel.getBarcodedtl().get(i).getSamplType().equalsIgnoreCase("URINE")) {
                        beneficiaryDetailsModel.getBarcodedtl().remove(i);
                        notifyDataSetChanged();
                    }
                }
            }
        } else {
            globalclass.showCustomToast(mActivity, "" + body.getResponse());
        }

    }

    public interface OnClickListeners {
        void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition);

        void onBarcodeScanClickedConfirm(String SampleType, int BenID, int barcodePosition, int BenPosition);

        void onVenupunturePhotoClicked(int BenID, int position);

        void onAffidavitClicked(int BenID, int position);

        void onRefresh();

        void onBarcodeDelete(String barcode, int BenPosition, String flag);

        void onVialImageDelete(int BenID, int position);

        void onAffidavitDelete(int BenID, int position);

        void onViewTestDetailsClicked(String benId);

        void onSRFSaved(String srfId, int BenPosition, int BenID);

        void onSRFDeleted(String SRFID, int BenPosition);

        void onImageShow(String venepuncture, int benId, int position, int flag);

    }
}