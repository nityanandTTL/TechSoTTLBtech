package com.dhb.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.OlcPickupActivity;
import com.dhb.models.api.request.OlcScanPickUpRequestModel;
import com.dhb.models.data.BtechClientsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;


public class OlcMasterBarcodeScanFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = OlcMasterBarcodeScanFragment.class.getSimpleName();
    private TextView txt_name, tv_distance, txt_call;
    private OlcPickupActivity activity;
    String scanned_barcode;
    BtechClientsModel btechClientsModel;
    IntentIntegrator integrator;
    ImageView btnScanBarcode;
    EditText edtScannedBarcode;
    public OlcMasterBarcodeScanFragment() {
        // Required empty public constructor
    }

    public static OlcMasterBarcodeScanFragment newInstance(BtechClientsModel btechClientsModel) {
        OlcMasterBarcodeScanFragment fragment = new OlcMasterBarcodeScanFragment();
        Bundle args = new Bundle();
        args.putParcelable("btechClientsModel",btechClientsModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_olc_master_barcode, container, false);
        activity = (OlcPickupActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        integrator = new IntentIntegrator(activity);
        initUI(rootView);
        initData();
        setListeners();
        return rootView;
    }

    public void initUI(View rootView) {
        super.initUI();
        txt_name = (TextView) rootView.findViewById(R.id.txt_name);
        txt_call = (TextView) rootView.findViewById(R.id.txt_call);
        tv_distance = (TextView) rootView.findViewById(R.id.tv_distance);
        btnScanBarcode = (ImageView) rootView.findViewById(R.id.scan_barcode_button);
        edtScannedBarcode = (EditText) rootView.findViewById(R.id.edt_barcode);
    }

    private void setListeners() {
        txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +btechClientsModel.getMobile()));
                startActivity(intent);
            }
        });
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator = new IntentIntegrator(getActivity()) {
                    @Override
                    protected void startActivityForResult(Intent intent, int code) {
                        OlcMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
                    }
                };
                integrator.initiateScan();
            }
        });
    }

    private void initData() {
        btechClientsModel = getArguments().getParcelable("btechClientsModel");
        tv_distance.setText(""+btechClientsModel.getDistance());
        txt_name.setText(""+btechClientsModel.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            scanned_barcode = scanningResult.getContents();
            Logger.error("scanned_barcode "+scanningResult.getContents());
            edtScannedBarcode.setText(scanned_barcode);

            OlcScanPickUpRequestModel olcScanPickUpRequestModel = new OlcScanPickUpRequestModel();
            olcScanPickUpRequestModel.setBarcode(scanned_barcode);
            olcScanPickUpRequestModel.setBarcodeType("master barcode");
            olcScanPickUpRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
            olcScanPickUpRequestModel.setClientId(btechClientsModel.getClientId());

            ApiCallAsyncTask apiCallAsyncTask = new AsyncTaskForRequest(activity).getScanPickupRequestAsyncTask(olcScanPickUpRequestModel);
            apiCallAsyncTask.setApiCallAsyncTaskDelegate(new OLCScanPickUpAPIResponseDelegateResult());
            if(isNetworkAvailable(activity)) {
                apiCallAsyncTask.execute(apiCallAsyncTask);
            }
            else{
                Toast.makeText(activity,activity.getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    private class OLCScanPickUpAPIResponseDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200||statusCode==204){
                Toast.makeText(activity,"Master Barcode Successfully Scanned and Submitted",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
