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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.CampOrderBookingActivity;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.LabAlertMasterDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.delegate.SelectLabAlertsCheckboxDelegate;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.dhb.models.api.response.OrderBookingResponseOrderModel;
import com.dhb.models.api.response.OrderBookingResponseVisitModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.CampAllOrderDetailsModel;

import com.dhb.models.data.CampDetailModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestClinicalHistoryModel;
import com.dhb.models.data.TestRateMasterModel;
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
    RelativeLayout rl_2;
    IntentIntegrator integrator;
    private String selectedGender="";
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
    private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
    private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
    private LabAlertMasterDao labAlertMasterDao;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private TextView edtCH,edtLA;
    private boolean isGenderSelected=false;
    private TextView txt_name,tv_location;
    private boolean isMale = false;
    private boolean isFemale = false;
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
        View view = inflater.inflate(R.layout.camp_new_cell_content_layout, container, false);
        campDetailModel = getArguments().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        activity = (CampOrderBookingActivity) getActivity();
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        orderNO = DeviceUtils.randomString(8);
        benId = DeviceUtils.randomInt(1, 10);
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        brandMasterModel = brandMasterDao.getModelFromId(campDetailModel.getBrandId());
        labAlertMasterDao = new LabAlertMasterDao(dhbDao.getDb());
        labAlertsArr = labAlertMasterDao.getAllModels();
        benLAArr = new ArrayList<>();
        benCHArr = new ArrayList<>();
        initUI(view);
        initData();
        btn_enter_manually.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg_deselected));
        btn_scan_qr.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));
        setListeners();
        return view;
    }

    private void initData() {
        String testCodes = campDetailModel.getProduct();
        TestRateMasterDao testRateMasterDao = new TestRateMasterDao(dhbDao.getDb());
        testRateMasterModels = testRateMasterDao.getModelsFromTestCodes(testCodes);
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
        if (testRateMasterModels != null) {
            for (TestRateMasterModel testRateMasterModel :
                    testRateMasterModels) {
                if (testRateMasterModel.getTstClinicalHistory() != null && testRateMasterModel.getTstClinicalHistory().size() > 0) {
                    testRateMasterModels.add(testRateMasterModel);
                }
            }
        }
        String chS = "";
        if(benCHArr!=null && benCHArr.size()>0) {
            for (BeneficiaryTestWiseClinicalHistoryModel chm :
                    benCHArr) {
                if (InputUtils.isNull(chS))
                    chS = "" + chm.getClinicalHistoryId();
                else
                    chS = chS + ", " + chm.getClinicalHistoryId();
            }
        }
        Logger.error("chS "+chS);
        String laS = "";
        if(benLAArr!=null && benLAArr.size()>0) {
            for (BeneficiaryLabAlertsModel lam :
                    benLAArr) {
                LabAlertMasterModel labAlertMasterModel = labAlertMasterDao.getModelFromId(lam.getLabAlertId()+"");
                if(labAlertMasterModel!=null) {
                    if (InputUtils.isNull(laS))
                        laS = "" + labAlertMasterModel.getLabAlert();
                    else
                        laS = laS + ", " + labAlertMasterModel.getLabAlert();
                }
            }
        }
        Logger.error("laS "+laS);
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
                if(barcodeDetailsArr.get(i).getSamplType().equals("SERUM")){
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_serum));
                }
                else if(barcodeDetailsArr.get(i).getSamplType().equals("EDTA")){
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_edta));
                }
                else if(barcodeDetailsArr.get(i).getSamplType().equals("FLUORIDE")){
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                }
                else if(barcodeDetailsArr.get(i).getSamplType().equals("HEPARIN")){
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                }
                else if(barcodeDetailsArr.get(i).getSamplType().equals("URINE")){
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_urine));
                }
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
        rl_2=(RelativeLayout)view.findViewById(R.id.rl_2);
        rl_2.requestFocus();
        rl_2.setFocusable(true);
        rl_2.requestFocusFromTouch();
        tv_location=(TextView)view.findViewById(R.id.tv_location);
        txt_name=(TextView)view.findViewById(R.id.txt_name);
        String upperString = campDetailModel.getLocation();
        tv_location.setText(upperString);
        txt_name.setText(campDetailModel.getCampName());
        ll_test_scan = (LinearLayout) view.findViewById(R.id.ll_test_scan);
        edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_mobile = (EditText) view.findViewById(R.id.edt_mobile);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        edt_test = (TextView) view.findViewById(R.id.edt_test);
        scan_barcode_button = (ImageView) view.findViewById(R.id.scan_barcode_button);
        btn_enter_manually = (Button) view.findViewById(R.id.btn_enter_manually);
        btn_scan_qr = (Button) view.findViewById(R.id.btn_scan_qr);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        view_female = (View) view.findViewById(R.id.view_female);
        view_male = (View) view.findViewById(R.id.view_male);
        edt_age = (EditText) view.findViewById(R.id.edt_age);
        img_male = (ImageView) view.findViewById(R.id.img_male);
        img_female = (ImageView) view.findViewById(R.id.img_female);
        btn_scan_qr.setVisibility(View.VISIBLE);
        edt_test_alerts = (TextView) view.findViewById(R.id.edt_test_alerts);
        String tests = campDetailModel.getProduct();
        testsList = tests.split(",");
        edt_test_alerts.setText("" + testsList[0]);
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
            btn_scan_qr.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg_deselected));
            btn_enter_manually.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));
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
            isGenderSelected=true;
            isFemale = true;
            isMale = false;
           // view_male.setVisibility(View.GONE);
          //  view_female.setVisibility(View.VISIBLE);
            img_male.setImageDrawable(getResources().getDrawable(R.drawable.male));
            img_female.setImageDrawable(getResources().getDrawable(R.drawable.f_selected));
        } else if (v.getId() == R.id.img_male) {
            isGenderSelected=true;
            isMale = true;
            isFemale = false;
            img_male.setImageDrawable(getResources().getDrawable(R.drawable.m_selected));
            img_female.setImageDrawable(getResources().getDrawable(R.drawable.female));
            //view_male.setVisibility(View.VISIBLE);
           // view_female.setVisibility(View.GONE);
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
        campAllOrderDetailsModel.setAddress(campDetailModel.getLocation());
        campAllOrderDetailsModel.setPincode(String.valueOf(campDetailModel.getPincode())/*.getText().toString()*/);
        campAllOrderDetailsModel.setMobile(edt_mobile.getText().toString());
        campAllOrderDetailsModel.setEmail("");
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
        if (isFemale) {
            beneficiaryDetailsModel.setGender("F");
        } else if (isMale) {
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
        orderBookingRequestModel.setClHistory(benCHArr);
        orderBookingRequestModel.setLabAlert(benLAArr);
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
        } else if (!isGenderSelected) {
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
        }
        for (BeneficiaryBarcodeDetailsModel benBarcode:barcodeDetailsArr){
            if(InputUtils.isNull(benBarcode.getBarcode())){
                Toast.makeText(activity,"Please scan barcode for sample type "+benBarcode.getSamplType(),Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            String scanned_barcode = scanningResult.getContents();
            Logger.error("scanned_barcode " + scanningResult.getContents());
            if (issampleTypeScan) {
                if(!InputUtils.isNull(scanned_barcode)&& scanned_barcode.length()==8) {
                    for (int i = 0; i < barcodeDetailsArr.size(); i++) {
                        if (barcodeDetailsArr.get(i).getSamplType().equals(currentSampleType)) {
                            for (BeneficiaryBarcodeDetailsModel benBarcode :
                                    barcodeDetailsArr) {
                                if (!InputUtils.isNull(benBarcode.getBarcode()) && benBarcode.getBarcode().equals(scanned_barcode)) {
                                    if (benBarcode.getSamplType().equals(currentSampleType)) {

                                    } else {
                                        Toast.makeText(activity, "Barcode Already Scanned for Sample Type " + currentSampleType, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            barcodeDetailsArr.get(i).setBarcode(scanningResult.getContents());
                            break;
                        }
                    }
                    initScanBarcodeView();
                }

            } else {
                callsendQRCodeApi(scanningResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callsendQRCodeApi("jhjhj");
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
        }
    }

    private class SendQRCodeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
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
                }catch (NumberFormatException e){
                   e.printStackTrace();

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

    private OrderBookingRequestModel fixForAddBeneficiary(OrderBookingRequestModel orderBookingRequestModel) {
        //Update Visit ID in OrdBooking Model
        if (orderBookingRequestModel.getOrdbooking().getVisitId().equals(orderBookingResponseVisitModel.getOldVisitId())) {
            orderBookingRequestModel.getOrdbooking().setVisitId(orderBookingResponseVisitModel.getNewVisitId());
        }
        //Update Order No and Visit ID in Order Dtl Arr
        if (orderBookingRequestModel.getOrddtl() != null) {
            for (int i = 0; i < orderBookingRequestModel.getOrddtl().size(); i++) {
                if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getVisitId()) && orderBookingRequestModel.getOrddtl().get(i).getVisitId().equals(orderBookingResponseVisitModel.getOldVisitId())) {
                    orderBookingRequestModel.getOrddtl().get(i).setVisitId(orderBookingResponseVisitModel.getNewVisitId());
                }
                if (orderBookingResponseVisitModel.getOrderids() != null) {
                    for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                        if (!InputUtils.isNull(orderBookingRequestModel.getOrddtl().get(i).getOrderNo()) && orderBookingRequestModel.getOrddtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                            orderBookingRequestModel.getOrddtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                        }
                    }
                }

            }
        }

        //Update Order No and BenId in Bendtl Arr
        if (orderBookingRequestModel.getBendtl() != null) {
            for (int i = 0; i < orderBookingRequestModel.getBendtl().size(); i++) {
                if (orderBookingResponseVisitModel.getOrderids() != null) {
                    for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                        if (!InputUtils.isNull(orderBookingRequestModel.getBendtl().get(i).getOrderNo()) && orderBookingRequestModel.getBendtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                            orderBookingRequestModel.getBendtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                        }
                    }
                }

                if (orderBookingResponseBeneficiaryModelArr != null) {
                    for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                        if (!InputUtils.isNull((orderBookingRequestModel.getBendtl().get(i).getBenId() + "")) && (orderBookingRequestModel.getBendtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                            orderBookingRequestModel.getBendtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                        }
                    }
                }

            }
        }

        //Update orderNo and BenId in BarcodeDtl Arr
        if (orderBookingRequestModel.getBarcodedtl() != null) {
            for (int i = 0; i < orderBookingRequestModel.getBarcodedtl().size(); i++) {
                if (orderBookingResponseVisitModel.getOrderids() != null) {
                    for (int j = 0; j < orderBookingResponseVisitModel.getOrderids().size(); j++) {
                        if (!InputUtils.isNull(orderBookingRequestModel.getBarcodedtl().get(i).getOrderNo()) && orderBookingRequestModel.getBarcodedtl().get(i).getOrderNo().equals(orderBookingResponseVisitModel.getOrderids().get(j).getOldOrderId())) {
                            orderBookingRequestModel.getBarcodedtl().get(i).setOrderNo(orderBookingResponseVisitModel.getOrderids().get(j).getNewOrderId());
                        }
                    }
                }
                if (orderBookingResponseBeneficiaryModelArr != null) {
                    for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                        if (!InputUtils.isNull((orderBookingRequestModel.getBarcodedtl().get(i).getBenId() + "")) && (orderBookingRequestModel.getBarcodedtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                            orderBookingRequestModel.getBarcodedtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                        }
                    }
                }

            }
        }

        //Update BenId in SmplDtl Arr
        if (orderBookingRequestModel.getSmpldtl() != null) {
            for (int i = 0; i < orderBookingRequestModel.getSmpldtl().size(); i++) {
                if (orderBookingResponseBeneficiaryModelArr != null) {
                    for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                        if ((orderBookingRequestModel.getSmpldtl().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                            orderBookingRequestModel.getSmpldtl().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                        }
                    }
                }

            }
        }

        //Update BenId in ClHistory Arr
        if (orderBookingRequestModel.getClHistory() != null) {
            for (int i = 0; i < orderBookingRequestModel.getClHistory().size(); i++) {
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getClHistory().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getClHistory().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        }

        //Update BenId in LabAlert Arr
        if (orderBookingRequestModel.getLabAlert() != null) {
            for (int i = 0; i < orderBookingRequestModel.getLabAlert().size(); i++) {
                for (int j = 0; j < orderBookingResponseBeneficiaryModelArr.size(); j++) {
                    if ((orderBookingRequestModel.getLabAlert().get(i).getBenId() + "").equals(orderBookingResponseBeneficiaryModelArr.get(j).getOldBenIds())) {
                        orderBookingRequestModel.getLabAlert().get(i).setBenId(Integer.parseInt(orderBookingResponseBeneficiaryModelArr.get(j).getNewBenIds()));
                    }
                }
            }
        }

        return orderBookingRequestModel;
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                orderBookingResponseVisitModel = new ResponseParser(activity).getOrderBookingAPIResponse(json, statusCode);
                for (OrderBookingResponseOrderModel obrom :
                        orderBookingResponseVisitModel.getOrderids()) {
                    orderBookingResponseBeneficiaryModelArr.addAll(obrom.getBenfids());
                }

                callWoeApi();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void callWoeApi() {
        OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
        orderBookingRequestModel = fixForAddBeneficiary(orderBookingRequestModel);
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
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
