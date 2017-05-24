package com.dhb.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.AddEditBeneficiaryDetailsActivity;
import com.dhb.activity.CampOrderBookingActivity;
import com.dhb.activity.EditTestListActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.dao.AbstractDao;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.LabAlertMasterDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.AddSampleBarcodeDialogDelegate;
import com.dhb.delegate.OrderCancelDialogButtonClickedDelegate;
import com.dhb.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.dhb.delegate.RefreshBeneficiariesSliderDelegate;
import com.dhb.delegate.RefreshCampBeneficiariesSliderDelegate;
import com.dhb.delegate.SelectClinicalHistoryCheckboxDelegate;
import com.dhb.delegate.SelectLabAlertsCheckboxDelegate;
import com.dhb.dialog.AddSampleBarcodeDialog;
import com.dhb.dialog.CancelOrderDialog;
import com.dhb.dialog.ClinicalHistorySelectorDialog;
import com.dhb.dialog.LabAlertSelectorDialog;
import com.dhb.dialog.RescheduleOrderDialog;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiaryLabAlertsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.BeneficiaryTestWiseClinicalHistoryModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.CampAllOrderDetailsModel;
import com.dhb.models.data.CampDetailsBenMasterModel;
import com.dhb.models.data.LabAlertMasterModel;
import com.dhb.models.data.OrderBookingDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.models.data.TestRateMasterModel;
import com.dhb.models.data.TestSampleTypeModel;
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
import com.google.android.gms.vision.text.Text;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.dhb.utils.app.CommonUtils.decodedImageBytes;
import static com.dhb.utils.app.CommonUtils.encodeImage;


public class CampBeneficiaryDetailsScanBarcodeFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = CampBeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();

    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private CampOrderBookingActivity activity;
    private static final int REQUEST_CAMERA = 100;
    private View rootview;
    private CampDetailsBenMasterModel beneficiaryDetailsArr;
    private OrderDetailsModel orderDetailsModel;
    private CampAllOrderDetailsModel campAllOrderDetailsModel;
    private String currentScanSampleType;
    private RescheduleOrderDialog cdd;
    private CancelOrderDialog cod;
    private boolean isCancelRequesGenereted = false;
    private ArrayList<TestRateMasterModel> restOfTestsList;
    private ArrayList<BeneficiaryTestWiseClinicalHistoryModel> benCHArr;
    private ArrayList<BeneficiaryLabAlertsModel> benLAArr;
    private ArrayList<LabAlertMasterModel> labAlertsArr;
    private static RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegateResult;
    private EditText edt_name, edt_mobile, edt_email, edt_age,  edt_amount, edt_address;
    private Button btn_scan_qr, btn_enter_manually, btn_next;
    private TextView tv_age, tv_gender, edt_test_alerts,edt_brand_name;
    private LinearLayout ll_tests;
    private ArrayList<String> tests_iems = new ArrayList<>();
    private Spinner sp_test;
    private String test_codes;
    private String[] test_code_arr;
    private ArrayList<String> strings = new ArrayList<>();
    private LinearLayout ll_test_scan;
    private ArrayList<TestRateMasterModel> testRateMasterModels = new ArrayList<>();
    private DhbDao dhbDao;
    TextView edt_barcode;
    IntentIntegrator integrator;
    ImageView img_male, img_female;

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
        rootview = inflater.inflate(R.layout.fragment_camp_manual_woe_scan_barcode, container, false);
        activity = (CampOrderBookingActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        Bundle bundle = getArguments();
        beneficiaryDetailsArr = bundle.getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        campAllOrderDetailsModel = bundle.getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        initUI();
        setListeners();
        initBrandMaster();
        return rootview;
    }

    private void initBrandMaster() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchBrandMasterRequestAsyncTask();
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new BrandMasterApiCallResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();

        }
    }

    private class BrandMasterApiCallResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                //   Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                ResponseParser responseParser = new ResponseParser(activity);
                responseParser.setToShowErrorDailog(false);
                responseParser.setToShowToast(false);
                CampManualWOEFragment.brandMastersArr = new ArrayList<BrandMasterModel>();
                CampManualWOEFragment.brandMastersArr = responseParser.getBrandMaster(json, statusCode);
                edt_brand_name.setText("" + CampManualWOEFragment.brandMastersArr.get(0).getBrandName());
                for (int i = 0; i < CampManualWOEFragment.brandMastersArr.size(); i++) {
                    strings.add(CampManualWOEFragment.brandMastersArr.get(i).getBrandName());
                }

            } else {

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void setListeners() {
        btn_enter_manually.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        edt_brand_name.setOnClickListener(this);
    }

    @Override
    public void initUI() {
        ll_test_scan = (LinearLayout) rootview.findViewById(R.id.ll_test_scan);
        edt_test_alerts = (TextView) rootview.findViewById(R.id.edt_test_alerts);
        edt_name = (EditText) rootview.findViewById(R.id.edt_name);
        edt_mobile = (EditText) rootview.findViewById(R.id.edt_mobile);
        edt_email = (EditText) rootview.findViewById(R.id.edt_email);
        edt_email.setText("" + campAllOrderDetailsModel.getEmail());
        edt_mobile.setText("" + campAllOrderDetailsModel.getMobile());
        edt_name.setText("" + beneficiaryDetailsArr.getName());
        btn_scan_qr = (Button) rootview.findViewById(R.id.btn_scan_qr);
        btn_enter_manually = (Button) rootview.findViewById(R.id.btn_enter_manually);
        btn_next = (Button) rootview.findViewById(R.id.btn_next);
        btn_scan_qr.setVisibility(View.GONE);
        img_female = (ImageView) rootview.findViewById(R.id.img_female);
        img_male = (ImageView) rootview.findViewById(R.id.img_male);
        if (beneficiaryDetailsArr.getGender().equals("M")) {
            img_female.setVisibility(View.GONE);
            img_male.setVisibility(View.VISIBLE);
        } else {
            img_female.setVisibility(View.VISIBLE);
            img_male.setVisibility(View.GONE);
        }
        btn_enter_manually.setVisibility(View.VISIBLE);
        ll_tests = (LinearLayout) rootview.findViewById(R.id.ll_tests);
        tv_age = (TextView) rootview.findViewById(R.id.tv_age);
        tv_gender = (TextView) rootview.findViewById(R.id.tv_gender);
        sp_test = (Spinner) rootview.findViewById(R.id.sp_tests);
        edt_address = (EditText) rootview.findViewById(R.id.edt_address);
        tv_age.setText("Age: " + beneficiaryDetailsArr.getAge());
        edt_age = (EditText) rootview.findViewById(R.id.edt_age);
        edt_age.setText("" + beneficiaryDetailsArr.getAge());
        tv_gender.setText("| Gender: " + beneficiaryDetailsArr.getGender());
        edt_amount = (EditText) rootview.findViewById(R.id.edt_amount);
        edt_amount.setText("" + campAllOrderDetailsModel.getAmountDue());
        ll_tests.setVisibility(View.GONE);
        edt_address.setText("" + campAllOrderDetailsModel.getAddress());
        edt_brand_name = (TextView) rootview.findViewById(R.id.edt_brand_name);
        for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
            tests_iems.add(beneficiaryDetailsArr.getSampleType().get(i).getSampleType());
        }
        test_codes = beneficiaryDetailsArr.getTestsCode();
        test_code_arr = test_codes.split(",");
        Logger.error("arr: " + test_code_arr.toString());
        Logger.error("test code string: " + test_codes);
        edt_test_alerts.setText(test_code_arr[0]);
        if (beneficiaryDetailsArr.getSampleType().size() > 0) {
            for (int i = 0; i < beneficiaryDetailsArr.getSampleType().size(); i++) {
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_scan_barcode, null);
                TextView txt_sample_type = (TextView) v.findViewById(R.id.txt_sample_type);
                edt_barcode = (TextView) v.findViewById(R.id.edt_barcode);
                ImageView scan_barcode_button = (ImageView) v.findViewById(R.id.scan_barcode_button);
                txt_sample_type.setText("" + beneficiaryDetailsArr.getSampleType().get(i).getSampleType());
                scan_barcode_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

        edt_test_alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Toast.makeText(activity, "scanned res " + scanningResult, Toast.LENGTH_SHORT).show();
        if (scanningResult != null && scanningResult.getContents() != null) {
            //  String scanned_barcode = scanningResult.getContents();
            Logger.error("" + scanningResult);
            Logger.error("scanned_barcode " + scanningResult.getContents());
            Toast.makeText(activity, "" + scanningResult, Toast.LENGTH_SHORT).show();
            edt_barcode.setText("" + scanningResult.getContents());
            // callsendQRCodeApi(scanningResult.getContents());

        } else {
            super.onActivityResult(requestCode, resultCode, data);
            // callsendQRCodeApi("jhjhj");
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_enter_manually) {
            Intent intentOrderBooking = new Intent(activity, CampOrderBookingActivity.class);
            //intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModels);
            startActivity(intentOrderBooking);
        }
        if (v.getId() == R.id.btn_next) {
            OrderBookingRequestModel orderBookingRequestModel = generateOrderBookingRequestModel();
            ApiCallAsyncTask orderBookingAPIAsyncTask = new AsyncTaskForRequest(activity).getOrderBookingRequestAsyncTask(orderBookingRequestModel);
            orderBookingAPIAsyncTask.setApiCallAsyncTaskDelegate(new OrderBookingAPIAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                orderBookingAPIAsyncTask.execute(orderBookingAPIAsyncTask);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
            }

        }
        if (v.getId() == R.id.edt_brand_name) {
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
        }
    }

    private OrderBookingRequestModel generateOrderBookingRequestModel() {
        OrderBookingRequestModel orderBookingRequestModel = new OrderBookingRequestModel();
        OrderBookingDetailsModel orderBookingDetailsModel = new OrderBookingDetailsModel();
        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
        orderBookingDetailsModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        String orderNO = DeviceUtils.randomString(8);
        orderBookingDetailsModel.setVisitId("#" + orderNO);
        orderBookingRequestModel.setOrdbooking(orderBookingDetailsModel);

        return orderBookingRequestModel;
    }


    private class OrderBookingAPIAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
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
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
