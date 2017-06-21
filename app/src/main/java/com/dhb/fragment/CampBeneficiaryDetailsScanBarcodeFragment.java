package com.dhb.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.CampOrderBookingActivity;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BrandMasterDao;
import com.dhb.dao.models.LabAlertMasterDao;
import com.dhb.delegate.RefreshCampBeneficiariesSliderDelegate;
import com.dhb.dialog.CancelOrderDialog;
import com.dhb.dialog.RescheduleOrderDialog;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.api.response.OrderBookingResponseBeneficiaryModel;
import com.dhb.models.api.response.OrderBookingResponseVisitModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.CampAllOrderDetailsModel;
import com.dhb.models.data.CampDetailModel;
import com.dhb.models.data.CampDetailsBenMasterModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
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

import static com.dhb.utils.app.CommonUtils.encodeImage;


public class CampBeneficiaryDetailsScanBarcodeFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = CampBeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();
    private OrderBookingResponseVisitModel orderBookingResponseVisitModel = new OrderBookingResponseVisitModel();
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private CampOrderBookingActivity activity;
    private ArrayList<OrderBookingResponseBeneficiaryModel> orderBookingResponseBeneficiaryModelArr = new ArrayList<>();
    private static final int REQUEST_CAMERA = 100;
    private View rootview;
    private ImageView img_vsg;
    private CampDetailsBenMasterModel beneficiaryDetailsArr;
    private OrderDetailsModel orderDetailsModel;
    private CampAllOrderDetailsModel campAllOrderDetailsModel;
    // private CampDetailsBenMasterModel
    private String currentScanSampleType;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted = false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private static RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegateResult;
    private EditText edt_name, edt_mobile, edt_email, edt_age, edt_amount, edt_address, edt_pincode;
    private Button btn_scan_qr, btn_enter_manually, btn_next;
    private TextView tv_age, tv_gender, edt_test_alerts, edt_brand_name;
    private LinearLayout ll_tests;
    private ArrayList<String> tests_iems = new ArrayList<>();
    private Spinner sp_test;
    private String test_codes;
    RelativeLayout rl_2;
    private String[] test_code_arr;
    private ArrayList<String> strings = new ArrayList<>();
    private LinearLayout ll_test_scan;
    private ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
    CampDetailModel campDetailModel = new CampDetailModel();
    private ArrayList<BeneficiaryBarcodeDetailsModel> barcodeDetailsArr;
    ArrayList<CampDetailsBenMasterModel> campDetailsBenMasterModelsArray = new ArrayList<>();
    private DhbDao dhbDao;
    private boolean issampleTypeScan;
    private String orderNO;
    private int clearText = 0;
    private String currentSampleType;
    IntentIntegrator integrator;
    int benId;
    ImageView img_male, img_female;
    private CampScanQRResponseModel campScanQRResponseModel;
    private BrandMasterModel brandMasterModel;
    private TextView txt_name, tv_location;
    private LabAlertMasterDao labAlertMasterDao;

    public CampBeneficiaryDetailsScanBarcodeFragment() {
        // Required empty public constructor
    }

    public static CampBeneficiaryDetailsScanBarcodeFragment newInstance(Bundle bundle, RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate) {
        CampBeneficiaryDetailsScanBarcodeFragment fragment = new CampBeneficiaryDetailsScanBarcodeFragment();
        fragment.setArguments(bundle);
        refreshBeneficiariesSliderDelegateResult = refreshBeneficiariesSliderDelegate;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.camp_new_cell_content_layout, container, false);
        activity = (CampOrderBookingActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        Bundle bundle = getArguments();
        beneficiaryDetailsArr = bundle.getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        campAllOrderDetailsModel = bundle.getParcelable(BundleConstants.CAMP_ALL_ORDER_DETAIL);
        campScanQRResponseModel = bundle.getParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL);
        campDetailModel = bundle.getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        BrandMasterDao brandMasterDao = new BrandMasterDao(dhbDao.getDb());
        brandMasterModel = brandMasterDao.getModelFromId(campAllOrderDetailsModel.getBrandId());
        orderNO = DeviceUtils.randomString(8);
        benId = DeviceUtils.randomInt(1, 10);
        labAlertMasterDao = new LabAlertMasterDao(dhbDao.getDb());
        labAlertsArr = labAlertMasterDao.getAllModels();
        benLAArr = new ArrayList<>();
        benCHArr = new ArrayList<>();
        initUI();
        initData();
        btn_scan_qr.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg_deselected));

        btn_enter_manually.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));
        //btn_enter_manually.setElevation(10);
        setListeners();
        //  initBrandMaster();
        return rootview;
    }

    private void initData() {

        String testCodes = campDetailModel.getProduct();

        //  Logger.error("testCodes " + testCodes);
      /*  if (campDetailModel.getSampleType().size() > 0) {
            barcodeDetailsArr = new ArrayList<>();
            for (int i = 0; i < campDetailModel.getSampleType().size(); i++) {
                campDetailModel.getSampleType().get(i).setBenId(benId);
                BeneficiaryBarcodeDetailsModel bbdm = new BeneficiaryBarcodeDetailsModel();
                bbdm.setBenId(benId);
              //  Logger.error("Camp sample type");
                bbdm.setSamplType(campDetailModel.getSampleType().get(i).getSampleType());
                bbdm.setOrderNo(beneficiaryDetailsArr.getOrderNo());
                bbdm.setBarcode("");
                barcodeDetailsArr.add(bbdm);
            }
        }*/
        if (beneficiaryDetailsArr.getSampleType().size() > 0) {
            Logger.error("beneficiaryDetailsArr sixe: " + beneficiaryDetailsArr.getSampleType().size());
            barcodeDetailsArr = new ArrayList<>();
            for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
                campDetailModel.getSampleType().get(i).setBenId(beneficiaryDetailsArr.getBenId());
                //campDetailModel.getSampleType().get(i).setBenId(benId);
                BeneficiaryBarcodeDetailsModel bbdm = new BeneficiaryBarcodeDetailsModel();
                bbdm.setBenId(beneficiaryDetailsArr.getBenId());
                //  Logger.error("Camp sample type");
                //  bbdm.setSamplType(campDetailModel.getSampleType().get(i).getSampleType());
                bbdm.setSamplType(beneficiaryDetailsArr.getSampleType().get(i).getSampleType());
                bbdm.setOrderNo(beneficiaryDetailsArr.getOrderNo());
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
        if (benCHArr != null && benCHArr.size() > 0) {
            for (BeneficiaryTestWiseClinicalHistoryModel chm :
                    benCHArr) {
                if (InputUtils.isNull(chS))
                    chS = "" + chm.getClinicalHistoryId();
                else
                    chS = chS + ", " + chm.getClinicalHistoryId();
            }
        }
        Logger.error("chS " + chS);
        String laS = "";
        if (benLAArr != null && benLAArr.size() > 0) {
            for (BeneficiaryLabAlertsModel lam :
                    benLAArr) {
                LabAlertMasterModel labAlertMasterModel = labAlertMasterDao.getModelFromId(lam.getLabAlertId() + "");
                if (labAlertMasterModel != null) {
                    if (InputUtils.isNull(laS))
                        laS = "" + labAlertMasterModel.getLabAlert();
                    else
                        laS = laS + ", " + labAlertMasterModel.getLabAlert();
                }
            }
        }
        Logger.error("laS " + laS);
        initScanBarcodeView();
    }

    private void initScanBarcodeView() {



       /* if (barcodeDetailsArr.size() > 0) {
            ll_test_scan.removeAllViews();
            for (int i = 0; i < barcodeDetailsArr.size(); i++) {
                final int pos = i;
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_scan_barcode, null);
                TextView txt_sample_type = (TextView) v.findViewById(R.id.txt_sample_type);
                TextView edt_barcode = (TextView) v.findViewById(R.id.edt_barcode);
                ImageView scan_barcode_button = (ImageView) v.findViewById(R.id.scan_barcode_button);
                txt_sample_type.setText(barcodeDetailsArr.get(i).getSamplType());
                if (barcodeDetailsArr.get(i).getSamplType().equals("SERUM")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_serum));
                } else if (barcodeDetailsArr.get(i).getSamplType().equals("EDTA")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_edta));
                } else if (barcodeDetailsArr.get(i).getSamplType().equals("FLUORIDE")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (barcodeDetailsArr.get(i).getSamplType().equals("HEPARIN")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                } else if (barcodeDetailsArr.get(i).getSamplType().equals("URINE")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_urine));
                }
                Logger.debug("debug####" + barcodeDetailsArr.get(i).getBarcode().toString());
              //  edt_barcode.setText(barcodeDetailsArr.get(i).getBarcode());

                scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        issampleTypeScan = true;
                        currentSampleType = beneficiaryDetailsArr.getSampleType().get(pos).getSampleType();
                       // currentSampleType = campDetailModel.getSampleType().get(pos).getSampleType();
                        integrator = new IntentIntegrator(getActivity()) {
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                CampBeneficiaryDetailsScanBarcodeFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
                            }
                        };
                        integrator.initiateScan();
                    }
                });
                ll_test_scan.addView(v, i, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }*/

        if (beneficiaryDetailsArr.getSampleType().size() > 0) {

            ll_test_scan.removeAllViews();
            Logger.debug("debug" + beneficiaryDetailsArr.getSampleType().size());
            Logger.debug("debug1" + beneficiaryDetailsArr.getSampleType().toString());

            for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
                final int pos = i;
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_scan_barcode, null);
                TextView txt_sample_type = (TextView) v.findViewById(R.id.txt_sample_type);
                TextView edt_barcode = (TextView) v.findViewById(R.id.edt_barcode);
                ImageView scan_barcode_button = (ImageView) v.findViewById(R.id.scan_barcode_button);
                txt_sample_type.setText(beneficiaryDetailsArr.getSampleType().get(i).getSampleType());

                if (beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equalsIgnoreCase("SERUM")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_serum));
                } else if (beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equalsIgnoreCase("EDTA")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_edta));
                } else if (beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equalsIgnoreCase("FLUORIDE")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
                } else if (beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equalsIgnoreCase("HEPARIN")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_heparin));
                } else if (beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equalsIgnoreCase("URINE")) {
                    txt_sample_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_sample_type_urine));
                }

                Logger.debug("debug####" + barcodeDetailsArr.get(i).getBarcode().toString());
                if (clearText == 0) {
                    edt_barcode.setText(barcodeDetailsArr.get(i).getBarcode());
                } else if (clearText == 1) {
                    edt_barcode.setText("");
                }


                scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        issampleTypeScan = true;
                        currentSampleType = beneficiaryDetailsArr.getSampleType().get(pos).getSampleType();
                        integrator = new IntentIntegrator(getActivity()) {
                            @Override
                            protected void startActivityForResult(Intent intent, int code) {
                                CampBeneficiaryDetailsScanBarcodeFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
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
        btn_next.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        rl_2 = (RelativeLayout) rootview.findViewById(R.id.rl_2);
        rl_2.requestFocus();
        tv_location = (TextView) rootview.findViewById(R.id.tv_location);
        txt_name = (TextView) rootview.findViewById(R.id.txt_name);
        String upperString = campDetailModel.getLocation();
        tv_location.setText(upperString);
        txt_name.setText(campDetailModel.getCampName());
        ll_test_scan = (LinearLayout) rootview.findViewById(R.id.ll_test_scan);
        edt_test_alerts = (TextView) rootview.findViewById(R.id.edt_test_alerts);
        edt_name = (EditText) rootview.findViewById(R.id.edt_name);
        edt_mobile = (EditText) rootview.findViewById(R.id.edt_mobile);
        edt_mobile.setText("" + campAllOrderDetailsModel.getMobile());
        edt_name.setText("" + beneficiaryDetailsArr.getName());
        btn_scan_qr = (Button) rootview.findViewById(R.id.btn_scan_qr);
        btn_enter_manually = (Button) rootview.findViewById(R.id.btn_enter_manually);
        btn_next = (Button) rootview.findViewById(R.id.btn_next);
        img_female = (ImageView) rootview.findViewById(R.id.img_female);
        img_male = (ImageView) rootview.findViewById(R.id.img_male);
        if (beneficiaryDetailsArr.getGender().equals("M")) {
            img_female.setVisibility(View.GONE);
            img_male.setVisibility(View.VISIBLE);
            img_male.setImageDrawable(getResources().getDrawable(R.drawable.m_selected));
        } else {
            img_female.setVisibility(View.VISIBLE);
            img_male.setVisibility(View.GONE);
            img_female.setImageDrawable(getResources().getDrawable(R.drawable.f_selected));
        }
        btn_enter_manually.setVisibility(View.VISIBLE);
        tv_age = (TextView) rootview.findViewById(R.id.tv_age);
        tv_gender = (TextView) rootview.findViewById(R.id.tv_gender);
        edt_age = (EditText) rootview.findViewById(R.id.edt_age);
        edt_age.setText("" + beneficiaryDetailsArr.getAge());
        for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
            tests_iems.add(beneficiaryDetailsArr.getSampleType().get(i).getSampleType());
        }
        test_codes = beneficiaryDetailsArr.getTestsCode();
        test_code_arr = test_codes.split(",");
        Logger.error("arr: " + test_code_arr.toString());
        Logger.error("test code string: " + test_codes);
        edt_test_alerts.setText(test_code_arr[0]);

        edt_test_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_scan_qr.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg_deselected));
                btn_enter_manually.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_test_edit, null);
                builder.setView(dialogView);
                ListView lv_test_codes = (ListView) dialogView.findViewById(R.id.lv_test_codes);
                Button btn_edit = (Button) dialogView.findViewById(R.id.btn_edit);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, test_code_arr);
                lv_test_codes.setAdapter(adapter);
                btn_edit.setVisibility(View.GONE);
                builder.show();
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            String scanned_barcode = scanningResult.getContents();
            Logger.error("" + scanningResult);
            Logger.error("scanned_barcode " + scanningResult.getContents());
           // Toast.makeText(activity, "" + scanningResult, Toast.LENGTH_SHORT).show();
            if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {

                Logger.error("barcodeDetailsArr size: " + barcodeDetailsArr.size());
                for (int i = 0; i < barcodeDetailsArr.size(); i++) {
                    Logger.error("barcodeDetailsArr tostring: " + barcodeDetailsArr.toArray().toString());
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
                        Logger.debug(barcodeDetailsArr.toString());
                        break;
                    }
                } /*for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
                    Logger.error("barcodeDetailsArr tostring: "+ barcodeDetailsArr.toArray().toString());

                    if ( beneficiaryDetailsArr.getSampleType().get(i).getSampleType().equals(currentSampleType)) {
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
                        barcodeDetailsArr.get(i).setBarcode(scanned_barcode);
                        break;
                    }
                }*/
                initScanBarcodeView();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(activity, "No result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        } else if (InputUtils.isNull(edt_mobile.getText().toString())) {
            edt_mobile.setError("Enter Mobile");
            edt_mobile.requestFocus();
            return false;
        } else if (edt_mobile.getText().toString().length() < 10) {
            edt_mobile.setError("Enter Valid Mobile Number");
            edt_mobile.requestFocus();
            return false;
        }
        for (BeneficiaryBarcodeDetailsModel benBarcode : barcodeDetailsArr) {
            if (InputUtils.isNull(benBarcode.getBarcode())) {
                Toast.makeText(activity, "Please scan barcode for sample type " + benBarcode.getSamplType(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter_manually) {
            btn_scan_qr.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg));
            btn_enter_manually.setBackgroundDrawable(getResources().getDrawable(R.drawable.footer_bg_deselected));
            Intent intentOrderBooking = new Intent(activity, CampOrderBookingActivity.class);
            intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
            startActivity(intentOrderBooking);
        }
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
       /* if (v.getId() == R.id.edt_brand_name) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_test_edit, null);
            builder.setView(dialogView);
            ListView lv_test_codes = (ListView) dialogView.findViewById(R.id.lv_test_codes);
            Button btn_edit = (Button) dialogView.findViewById(R.id.btn_edit);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, strings);
            lv_test_codes.setAdapter(adapter);
            btn_edit.setVisibility(View.GONE);
            builder.show();
        }*/
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel() {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        orderBookingDetailsModel.setVisitId("" + campScanQRResponseModel.getVisitId());
        orderBookingDetailsModel.setPaymentMode(1);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        orderDetailsModel.setOrderNo(beneficiaryDetailsArr.getOrderNo());
        orderDetailsModel.setBrandId(campDetailModel.getBrandId());

        orderDetailsModel.setAddress(campDetailModel.getLocation());
        orderDetailsModel.setPincode("" + campAllOrderDetailsModel.getPincode());
        orderDetailsModel.setMobile(edt_mobile.getText().toString());
        orderDetailsModel.setEmail("" + campAllOrderDetailsModel.getEmail());
        orderDetailsModel.setPayType(campDetailModel.getPayType());
        orderDetailsModel.setAmountDue(campDetailModel.getAmount());
        orderDetailsModel.setMargin(0);
        orderDetailsModel.setDiscount(0);
        orderDetailsModel.setRefcode(edt_mobile.getText().toString());
        orderDetailsModel.setReportHC(0);
        orderDetailsModel.setTestEdit(false);
        orderDetailsModel.setServicetype("2");
        orderDetailsModel.setCampId(String.valueOf(campDetailModel.getId()));
        ArrayList<OrderDetailsModel> ordrDtl = new ArrayList<>();
        ordrDtl.add(orderDetailsModel);
        orderBookingDetailsModel.setOrddtl(ordrDtl);
        orderBookingRequestModel.setOrddtl(ordrDtl);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);
        BeneficiaryDetailsModel beneficiaryDetailsModel = new BeneficiaryDetailsModel();
        Logger.error("benid: " + beneficiaryDetailsArr.getBenId());
        beneficiaryDetailsModel.setBenId(beneficiaryDetailsArr.getBenId());
        // beneficiaryDetailsModel.setBenId(campScanQRResponseModel.getAllOrderdetails().get(0).getBenMaster().get(0).getBenId());
        beneficiaryDetailsModel.setOrderNo(beneficiaryDetailsArr.getOrderNo());
        beneficiaryDetailsModel.setName(edt_name.getText().toString());
        beneficiaryDetailsModel.setAge(Integer.parseInt(edt_age.getText().toString()));
        if (img_female.getVisibility() == View.VISIBLE) {
            beneficiaryDetailsModel.setGender("F");
        } else if (img_female.getVisibility() == View.VISIBLE) {
            beneficiaryDetailsModel.setGender("M");
        }
        beneficiaryDetailsModel.setTests(campDetailModel.getProduct());
        if (campDetailModel.isFasting()) {
            beneficiaryDetailsModel.setFasting("true");
        } else {
            beneficiaryDetailsModel.setFasting("false");
        }
        beneficiaryDetailsModel.setProjId("" + orderDetailsModel.getProjId());
        ArrayList<BeneficiaryDetailsModel> bendtl = new ArrayList<>();
        bendtl.add(beneficiaryDetailsModel);
        orderBookingRequestModel.setBendtl(bendtl);
        orderBookingRequestModel.setBarcodedtl(barcodeDetailsArr);
        //orderBookingRequestModel.setBarcodedtl(beneficiaryDetailsModel.getBarcodedtl());
        orderBookingRequestModel.setSmpldtl(campDetailModel.getSampleType());
        orderBookingRequestModel.setClHistory(benCHArr);
        orderBookingRequestModel.setLabAlert(benLAArr);
        return orderBookingRequestModel;
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

    private void clearEntries() {
        edt_name.setText("");
        edt_age.setText("");
        edt_mobile.setText("");
        img_male.setImageDrawable(getResources().getDrawable(R.drawable.male));
        img_female.setImageDrawable(getResources().getDrawable(R.drawable.female));

    }

    private class WorkOrderEntryAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                clearText = 1;
                clearEntries();
                initScanBarcodeView();

            } else {
                //  if(IS_DEBUG)
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                // Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                callWoeApi();

            } else {
                if (IS_DEBUG)
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
