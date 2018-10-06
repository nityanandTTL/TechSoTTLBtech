package com.thyrocare.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.R;
import com.thyrocare.activity.OLCPickupActivity;
import com.thyrocare.models.api.request.OlcScanPickUpRequestModel;
import com.thyrocare.models.data.BtechClientsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

import org.json.JSONException;


public class OLCMasterBarcodeScanFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = OLCMasterBarcodeScanFragment.class.getSimpleName();
    private TextView txt_name, tv_distance;
    private OLCPickupActivity activity;
    String scanned_barcode;
    BtechClientsModel btechClientsModel;
    IntentIntegrator integrator;
    ImageView btnScanBarcode, txt_call;
    EditText edtScannedBarcode;
    public OLCMasterBarcodeScanFragment() {
        // Required empty public constructor
    }

    public static OLCMasterBarcodeScanFragment newInstance(BtechClientsModel btechClientsModel) {
        OLCMasterBarcodeScanFragment fragment = new OLCMasterBarcodeScanFragment();
        Bundle args = new Bundle();
        args.putParcelable("btechClientsModel",btechClientsModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_olc_master_barcode, container, false);
        activity = (OLCPickupActivity) getActivity();
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
        txt_call = (ImageView) rootView.findViewById(R.id.txt_call);
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
                        OLCMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
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
        final IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null && scanningResult.getContents() != null) {
            scanned_barcode = scanningResult.getContents();



            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setTitle("Check the Barcode ")
                    .setMessage("Do you want to Proceed with this barcode entry "+scanned_barcode+"?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(scanned_barcode.startsWith("0")|| scanned_barcode.startsWith("$")|| scanned_barcode.startsWith("1")|| scanned_barcode.startsWith(" ")){
                        Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                    }else {
                        Logger.error("scanned_barcode "+scanningResult.getContents());
                        if(scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$")|| scanned_barcode.startsWith("1")|| scanned_barcode.startsWith(" ")){
                            Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                        }
                        if(edtScannedBarcode.getText().toString().startsWith("0") || edtScannedBarcode.getText().toString().startsWith("$")|| scanned_barcode.startsWith("1")|| scanned_barcode.startsWith(" ")){
                            Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                        }else {
                            edtScannedBarcode.setText(scanned_barcode);
                        }


                        OlcScanPickUpRequestModel olcScanPickUpRequestModel = new OlcScanPickUpRequestModel();
                        if (scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$")|| scanned_barcode.startsWith("1")|| scanned_barcode.startsWith(" ")){
                            Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                        }else {
                            olcScanPickUpRequestModel.setBarcode(scanned_barcode);
                        }

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
                    }
                }
            }).show();





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
