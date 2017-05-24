package com.dhb.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.CampOrderBookingActivity;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.CampAllOrderDetailsModel;

import com.dhb.models.data.CampDetailModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

public class CampManualWOEFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = CampManualWOEFragment.class.getSimpleName();
    private static final String TAG_ACTIVITY = CampManualWOEFragment.class.getSimpleName();
    private EditText edt_name, edt_mobile, edt_email, edt_scan_result, edt_amount, edt_pincode;
    private TextView edt_brand_name;
    private TextView tv_age, tv_gender, edt_test, edt_test_alerts, edt_age, edt_address;
    private ImageView scan_barcode_button, img_female, img_male;
    private LinearLayout ll_age_gender, ll_test_scan;
    private Button btn_enter_manually, btn_scan_qr, btn_next;
    IntentIntegrator integrator;
    private AppPreferenceManager appPreferenceManager;
    private boolean issampleTypeScan;
    View view_male, view_female;
    private CampOrderBookingActivity activity;
    String[] testsList;
    public static ArrayList<BrandMasterModel> brandMastersArr;
    ArrayList<CampAllOrderDetailsModel> campAllOrderDetailsModelslist = new ArrayList<>();
    CampScanQRResponseModel campScanQRResponseModel = new CampScanQRResponseModel();
    CampDetailModel campDetailModel = new CampDetailModel();
    int benId;
    private String orderNO;
    private ArrayList<BeneficiaryBarcodeDetailsModel> barcodeDetailsArr;
    private String currentSampleType;
    private DhbDao dhbDao;
    private BrandMasterModel brandMasterModel;

    public CampManualWOEFragment() {
        // Required empty public constructor
    }

    public static CampManualWOEFragment newInstance(CampDetailModel campDetailModel) {
        CampManualWOEFragment fragment = new CampManualWOEFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camp_manual_woe_scan_barcode, container, false);
        campDetailModel = getArguments().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        activity = (CampOrderBookingActivity) getActivity();
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        orderNO = DeviceUtils.randomString(8);
        Random r = new Random();

        benId = DeviceUtils.randomInt(1, 10);
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        brandMasterModel = brandMasterDao.getModelFromId(campDetailModel.getBrandId());
        initUI(view);
        initData();
        setListeners();
        return view;
    }

    private void initData() {
        String testCodes = campDetailModel.getProduct();
        Logger.error("testCodes " + testCodes);
        if (campDetailModel.getSampleType().size() > 0) {
            barcodeDetailsArr = new ArrayList<>();
            for (int i = 0; i < campDetailModel.getSampleType().size(); i++) {
                BeneficiaryBarcodeDetailsModel bbdm = new BeneficiaryBarcodeDetailsModel();
                campDetailModel.getSampleType().get(i).setBenId(benId);
                bbdm.setBenId(benId);
                bbdm.setSamplType(campDetailModel.getSampleType().get(i).getSampleType());
                bbdm.setOrderNo(orderNO);
                bbdm.setBarcode("");
                barcodeDetailsArr.add(bbdm);
            }
        }
        initScanBarcodeView();
    }

    private void initScanBarcodeView() {
        if (barcodeDetailsArr.size() > 0) {
            ll_test_scan.removeAllViews();
            for (int i = 0; i < barcodeDetailsArr.size(); i++) {
                final int pos = i;
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_scan_barcode, null);
                TextView txt_sample_type = (TextView) v.findViewById(R.id.txt_sample_type);
                TextView edt_barcode = (TextView) v.findViewById(R.id.edt_barcode);
                ImageView scan_barcode_button = (ImageView) v.findViewById(R.id.scan_barcode_button);
                txt_sample_type.setText(barcodeDetailsArr.get(i).getSamplType());
                edt_barcode.setText(barcodeDetailsArr.get(i).getBarcode());
                scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        issampleTypeScan = true;
                        currentSampleType = campDetailModel.getSampleType().get(pos).getSampleType();
                        integrator = new IntentIntegrator(getActivity()) {
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                CampManualWOEFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
                            }
                        };
                        integrator.initiateScan();
                    }
                });
                ll_test_scan.addView(v, i, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    private void setListeners() {
        btn_enter_manually.setOnClickListener(this);
        btn_scan_qr.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        edt_test_alerts.setOnClickListener(this);
        img_male.setOnClickListener(this);
        img_female.setOnClickListener(this);
    }

    private void initUI(View view) {
        ll_test_scan = (LinearLayout) view.findViewById(R.id.ll_test_scan);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_mobile = (EditText) view.findViewById(R.id.edt_mobile);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_scan_result = (EditText) view.findViewById(R.id.edt_scan_result);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        edt_test = (TextView) view.findViewById(R.id.edt_test);
        scan_barcode_button = (ImageView) view.findViewById(R.id.scan_barcode_button);
        btn_enter_manually = (Button) view.findViewById(R.id.btn_enter_manually);
        btn_scan_qr = (Button) view.findViewById(R.id.btn_scan_qr);
        edt_address = (EditText) view.findViewById(R.id.edt_address);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        view_female = (View) view.findViewById(R.id.view_female);
        view_male = (View) view.findViewById(R.id.view_male);
        edt_age = (EditText) view.findViewById(R.id.edt_age);
        img_male = (ImageView) view.findViewById(R.id.img_male);
        edt_pincode = (EditText) view.findViewById(R.id.edt_pincode);
        img_female = (ImageView) view.findViewById(R.id.img_female);
        btn_enter_manually.setVisibility(View.GONE);
        btn_scan_qr.setVisibility(View.VISIBLE);
        ll_age_gender = (LinearLayout) view.findViewById(R.id.ll_age_gender);
        ll_age_gender.setVisibility(View.GONE);
        edt_test_alerts = (TextView) view.findViewById(R.id.edt_test_alerts);
        String tests = campDetailModel.getProduct();
        edt_amount = (EditText) view.findViewById(R.id.edt_amount);
        edt_amount.setText("" + campDetailModel.getAmount());
        testsList = tests.split(",");
        edt_test_alerts.setText("" + testsList[0]);
        edt_brand_name = (TextView) view.findViewById(R.id.edt_brand_name);
        if (brandMasterModel != null) {
            if (InputUtils.isNull(brandMasterModel.getBrandName())) {
                edt_brand_name.setText("" + brandMasterModel.getBrandId());
            } else {
                edt_brand_name.setText(brandMasterModel.getBrandName());
            }

        }

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_next) {
            if (validate()) {
                OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
                ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getOrderBookingRequestAsyncTask(orderBookingRequestModel);
                orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new OrderBookingAPIAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (v.getId() == R.id.btn_scan_qr) {
            issampleTypeScan = false;
            integrator = new IntentIntegrator(getActivity()) {
                @Override
                protected void startActivityForResult(Intent intent, int code) {
                    CampManualWOEFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override

                }
            };
            integrator.initiateScan();
        }
        if (v.getId() == R.id.img_female) {
            view_male.setVisibility(View.GONE);
            view_female.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.img_male) {
            view_male.setVisibility(View.VISIBLE);
            view_female.setVisibility(View.GONE);
        }
        if (v.getId() == R.id.edt_test_alerts) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_test_edit, null);
            builder.setView(dialogView);
            ListView lv_test_codes = (ListView) dialogView.findViewById(R.id.lv_test_codes);
            Button btn_edit = (Button) dialogView.findViewById(R.id.btn_edit);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, testsList);
            lv_test_codes.setAdapter(adapter);
            btn_edit.setVisibility(View.GONE);
            builder.show();
        }
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel() {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();

        OrderDetailsModel campAllOrderDetailsModel = new OrderDetailsModel();
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setPaymentMode(1);
        orderBookingDetailsModel.setVisitId("");
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        campAllOrderDetailsModel.setOrderNo(orderNO);
        campAllOrderDetailsModel.setBrandId(campDetailModel.getBrandId());
        campAllOrderDetailsModel.setAddress(edt_address.getText().toString());
        campAllOrderDetailsModel.setPincode(edt_pincode.getText().toString());
        campAllOrderDetailsModel.setMobile(edt_mobile.getText().toString());
        campAllOrderDetailsModel.setEmail(edt_email.getText().toString());
        campAllOrderDetailsModel.setPayType(campDetailModel.getPayType());
        campAllOrderDetailsModel.setAmountDue(campDetailModel.getAmount());
        campAllOrderDetailsModel.setMargin(0);
        campAllOrderDetailsModel.setDiscount(0);
        campAllOrderDetailsModel.setRefcode(edt_mobile.getText().toString());
        campAllOrderDetailsModel.setReportHC(0);
        campAllOrderDetailsModel.setTestEdit(false);
        campAllOrderDetailsModel.setServicetype("2");
        campAllOrderDetailsModel.setCampId(String.valueOf(campDetailModel.getId()));
        ArrayList<OrderDetailsModel> ordrDtl = new ArrayList<>();
        ordrDtl.add(campAllOrderDetailsModel);
        orderBookingDetailsModel.setOrddtl(ordrDtl);
        orderBookingRequestModel.setOrddtl(ordrDtl);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        BeneficiaryDetailsModel beneficiaryDetailsModel = new BeneficiaryDetailsModel();
        beneficiaryDetailsModel.setBenId(benId);
        beneficiaryDetailsModel.setOrderNo(orderNO);
        beneficiaryDetailsModel.setName(edt_name.getText().toString());
        beneficiaryDetailsModel.setAge(Integer.parseInt(edt_age.getText().toString()));
        if (view_female.getVisibility() == View.VISIBLE) {
            beneficiaryDetailsModel.setGender("F");
        } else if (view_male.getVisibility() == View.VISIBLE) {
            beneficiaryDetailsModel.setGender("M");
        }
        beneficiaryDetailsModel.setTests(campDetailModel.getProduct());
        if (campDetailModel.isFasting()) {
            beneficiaryDetailsModel.setFasting("true");
        } else {
            beneficiaryDetailsModel.setFasting("false");
        }
        beneficiaryDetailsModel.setProjId("");
        ArrayList<BeneficiaryDetailsModel> bendtl = new ArrayList<>();
        bendtl.add(beneficiaryDetailsModel);
        orderBookingRequestModel.setBendtl(bendtl);
        orderBookingRequestModel.setBarcodedtl(barcodeDetailsArr);
        orderBookingRequestModel.setSmpldtl(campDetailModel.getSampleType());
        return orderBookingRequestModel;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validate() {
        if (InputUtils.isNull(edt_name.getText().toString())) {
            edt_name.setError("Enter Name");
            edt_name.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_age.getText().toString())) {
            edt_age.setError("Enter Age");
            edt_age.requestFocus();
            return false;
        } else if (Integer.parseInt(edt_age.getText().toString()) > 135 || Integer.parseInt(edt_age.getText().toString()) < 1) {
            edt_age.setError("Age Should be minimum 1 year and maximum 135 year");
            edt_age.requestFocus();
            return false;
        } else if (view_female.getVisibility() == View.GONE && view_male.getVisibility() == View.GONE) {
            Toast.makeText(activity, "Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else if (InputUtils.isNull(edt_mobile.getText().toString())) {
            edt_mobile.setError("Enter Mobile");
            edt_mobile.requestFocus();
            return false;
        } else if (edt_mobile.getText().toString().length() < 10) {
            edt_mobile.setError("Enter Valid Mobile Number");
            edt_mobile.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_address.getText().toString())) {
            edt_address.setError("Enter Address");
            edt_address.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_pincode.getText().toString())) {
            edt_pincode.setError("Enter Pincode");
            edt_pincode.requestFocus();
            return false;
        } else if (edt_pincode.getText().toString().length() < 6) {
            edt_pincode.setError("Enter Valid Pincode");
            edt_pincode.requestFocus();
            return false;
        } else if (InputUtils.isNull(edt_email.getText().toString())) {
            edt_email.setError("Enter Email");
            edt_email.requestFocus();
            return false;
        }/* else if (isValidEmail(edt_email.getText().toString())) {
            edt_email.setError("Enter Valid Email");
            edt_email.requestFocus();
            return false;
        }*/
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(activity, "scanned res " + scanningResult, Toast.LENGTH_SHORT).show();
        if (scanningResult != null && scanningResult.getContents() != null) {
            Logger.error("" + scanningResult);
            Logger.error("scanned_barcode " + scanningResult.getContents());
            Toast.makeText(activity, "" + scanningResult, Toast.LENGTH_SHORT).show();
            if (issampleTypeScan) {
                for (int i = 0; i < barcodeDetailsArr.size(); i++) {
                    if (barcodeDetailsArr.get(i).getSamplType().equals(currentSampleType)) {
                        barcodeDetailsArr.get(i).setBarcode(scanningResult.getContents());
                        break;
                    }
                }
                initScanBarcodeView();
            } else {
                callsendQRCodeApi(scanningResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callsendQRCodeApi("jhjhj");
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callsendQRCodeApi(String contents) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getSendQRCodeRequestAsyncTask(contents);
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new SendQRCodeApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            // initData();
        }
    }

    private class SendQRCodeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                ResponseParser responseParser = new ResponseParser(activity);
                campScanQRResponseModel = new CampScanQRResponseModel();
                campScanQRResponseModel = responseParser.getcampOrderDetailsResponseModel(json, statusCode);
                if (campScanQRResponseModel != null && campScanQRResponseModel.getAllOrderdetails().size() > 0) {

                    for (int i = 0; i < campScanQRResponseModel.getAllOrderdetails().size(); i++) {
                        for (int j = 0; j <
                                campScanQRResponseModel.getAllOrderdetails().get(i).getBenMaster().size(); j++) {
                            campScanQRResponseModel.getAllOrderdetails().get(i).getBenMaster().get(j).setOrderNo(campScanQRResponseModel.getAllOrderdetails().get(i).getOrderNo());
                        }
                    }

                    campAllOrderDetailsModelslist = campScanQRResponseModel.getAllOrderdetails();
                    Logger.error("camp detail size " + campScanQRResponseModel.getAllOrderdetails().size());
                    prepareData();

                }

            } else {
                Toast.makeText(activity, "error : " + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareData() {
        pushFragments(CampBeneficiariesDisplayFragment.newInstance(campScanQRResponseModel, campDetailModel), false, false, CampBeneficiariesDisplayFragment.TAG_FRAGMENT, R.id.fl_camp_order_booking, TAG_ACTIVITY);
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                callWoeApi();
            } else {
                Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void callWoeApi() {
        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
        ApiCallAsyncTask workOrderEntryRequestAsyncTask = new AsyncTaskForRequest(activity).getWorkOrderEntryRequestAsyncTask(orderBookingRequestModel);
        workOrderEntryRequestAsyncTask.setApiCallAsyncTaskDelegate(new WorkOrderEntryAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            workOrderEntryRequestAsyncTask.execute(workOrderEntryRequestAsyncTask);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private class WorkOrderEntryAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode==200){
                Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
