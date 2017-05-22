package com.dhb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.CampOrderBookingActivity;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.data.CampAllOrderDetailsModel;

import com.dhb.models.data.CampDetailModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.ArrayList;

public class CampManualWOEFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = CampManualWOEFragment.class.getSimpleName();
    private static final String TAG_ACTIVITY = CampManualWOEFragment.class.getSimpleName();
    private EditText edt_name, edt_mobile, edt_email, edt_scan_result;
    private TextView tv_age, tv_gender, edt_test,edt_test_alerts;
    private ImageView scan_barcode_button;
    private LinearLayout ll_age_gender;
    private Button btn_enter_manually, btn_scan_qr, btn_next;
    IntentIntegrator integrator;
    private CampListDisplayResponseModel campListDisplayResponseModel;
    private CampOrderBookingActivity activity;
    private String[] test_code_arr;
    ArrayList<CampAllOrderDetailsModel> campAllOrderDetailsModelslist = new ArrayList<>();
    CampScanQRResponseModel campScanQRResponseModel =new CampScanQRResponseModel();
    CampDetailModel campDetailModel;
    public CampManualWOEFragment() {
        // Required empty public constructor
    }

    public static CampManualWOEFragment newInstance(CampScanQRResponseModel campScanQRResponseModel,CampListDisplayResponseModel campListDisplayResponseModel,CampDetailModel campDetailModel) {
        CampManualWOEFragment fragment = new CampManualWOEFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL, campScanQRResponseModel);
        args.putParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL,campListDisplayResponseModel);
        args.putParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL,campDetailModel);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camp_manual_woe_scan_barcode, container, false);
        initUi(view);
        activity = (CampOrderBookingActivity) getActivity();
        setListeners();
        return view;
    }

    private void setListeners() {
        btn_enter_manually.setOnClickListener(this);
        btn_scan_qr.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    private void initUi(View view) {
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
        btn_next = (Button) view.findViewById(R.id.btn_enter_manually);
        btn_enter_manually.setVisibility(View.GONE);
        btn_scan_qr.setVisibility(View.VISIBLE);
        ll_age_gender=(LinearLayout)view.findViewById(R.id.ll_age_gender);
        ll_age_gender.setVisibility(View.GONE);
        edt_test_alerts=(TextView)view.findViewById(R.id.edt_test_alerts);

        test_code_arr = CampListDisplayFragment.products.split(",");
        edt_test_alerts.setText(test_code_arr[0]);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_next) {

        }
        if (v.getId() == R.id.btn_scan_qr) {
            integrator = new IntentIntegrator(getActivity()) {
                @Override
                protected void startActivityForResult(Intent intent, int code) {
                    CampManualWOEFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override

                }
            };
            integrator.initiateScan();
        }
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
            callsendQRCodeApi(scanningResult.getContents());

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

                    for (int i = 0; i < campScanQRResponseModel.getAllOrderdetails().size() ; i++) {
                        for (int j = 0; j<
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
        pushFragments(CampBeneficiariesDisplayFragment.newInstance(campScanQRResponseModel),false,false,CampBeneficiariesDisplayFragment.TAG_FRAGMENT,R.id.fl_camp_order_booking,TAG_ACTIVITY);
    }


}
