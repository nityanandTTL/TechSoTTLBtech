package com.thyrocare.btechapp.fragment.LME;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import application.ApplicationController;
import com.thyrocare.btechapp.fragment.HomeScreenFragment;
import com.thyrocare.btechapp.models.data.HubBarcodeModel;
import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.models.data.ScannedMasterBarcodebyLMEPOSTDATAModel;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;

import java.util.ArrayList;

/**
 * 　APi Used 　　BtechCollections/userid<br/>
 * 　　* /MasterBarcodeMapping/
 * Created by Orion on 7/4/2017.
 */

public class LMEMasterBarcodeScanFragment extends AbstractFragment implements View.OnClickListener {

    public static final String TAG_FRAGMENT = LMEMasterBarcodeScanFragment.class.getSimpleName();
    private LinearLayout ll_hub_display_footer, ll_scan_master_barcode;
    TextView tv_collection_sample;
    private HomeScreenActivity activity;
    private ArrayList<HubBarcodeModel> barcodeModels = new ArrayList<>();
    private String master_scanned_barcode = "";
    SampleDropDetailsbyTSPLMEDetailsModel mSampleDropDetailsbyTSPLMEDetailsModel;
    private IntentIntegrator intentIntegrator;
    private Button btnDispatch;
    TextView scanned_barcode;
    static LMEMasterBarcodeScanFragment fragment;
    TextView txt_code, txt_cnt, txt_name, txt_address;

    public LMEMasterBarcodeScanFragment() {
    }

    public static LMEMasterBarcodeScanFragment newInstance(int i) {

        return new LMEMasterBarcodeScanFragment();
    }

    public static LMEMasterBarcodeScanFragment newInstance(SampleDropDetailsbyTSPLMEDetailsModel hubbTechModel) {
        fragment = new LMEMasterBarcodeScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.LME_ORDER_MODEL, hubbTechModel);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lme_collections_list, container, false);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Barcode Scan");
        appPreferenceManager = new AppPreferenceManager(activity);
        mSampleDropDetailsbyTSPLMEDetailsModel = getArguments().getParcelable(BundleConstants.LME_ORDER_MODEL);
        initUI(view);
        setListeners();

        return view;
    }

    private void setListeners() {
        ll_scan_master_barcode.setOnClickListener(this);
        btnDispatch.setOnClickListener(this);
    }

    private void initUI(View view) {

        ll_hub_display_footer = (LinearLayout) view.findViewById(R.id.ll_hub_display_footer);
        tv_collection_sample = view.findViewById(R.id.tv_collection_sample);
        tv_collection_sample.setVisibility(View.VISIBLE);
        ll_scan_master_barcode = (LinearLayout) view.findViewById(R.id.ll_scan_master_barcode);
        ll_hub_display_footer.setVisibility(View.VISIBLE);
        btnDispatch = (Button) view.findViewById(R.id.btn_dispatch);
        scanned_barcode = (TextView) view.findViewById(R.id.scanned_barcode);

        txt_name = (TextView) view.findViewById(R.id.txt_name);
        txt_address = (TextView) view.findViewById(R.id.txt_address);
        txt_code = (TextView) view.findViewById(R.id.txt_code);
        txt_cnt = (TextView) view.findViewById(R.id.txt_cnt);

        txt_code.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getSourceCode());
//        txt_cnt.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getSampleCount());
        txt_name.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getName());
        txt_address.setText("" + mSampleDropDetailsbyTSPLMEDetailsModel.getAddress() +"-"+mSampleDropDetailsbyTSPLMEDetailsModel.getPincode());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_scan_master_barcode) {
            scanFromFragment();
        } else if (v.getId() == R.id.btn_dispatch) {
            if (validate()) {
                //callMasterBarcodeMapApi();
                StartPostScannedMasterBarcodebyLME(mSampleDropDetailsbyTSPLMEDetailsModel);
            }
        }
    }

    private void StartPostScannedMasterBarcodebyLME(SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsbyTSPLMEDetailsModel) {
        ScannedMasterBarcodebyLMEPOSTDATAModel n = null;
        try {
            GPSTracker gpsTracker = new GPSTracker(activity);
            n = new ScannedMasterBarcodebyLMEPOSTDATAModel();
            n.setMasterBarcode(""+master_scanned_barcode.toString().trim());
//            n.setSampleDropIds("" + sampleDropDetailsbyTSPLMEDetailsModel.getSampleDropId());
            n.setStatus("3");
            n.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            n.setLongitude(String.valueOf(gpsTracker.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }
    }

    private boolean validate() {
        if (master_scanned_barcode.equals("")) {
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (master_scanned_barcode.toString().trim().length() != 8) {
            Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            if (scanningResult.getContents().startsWith("0") || scanningResult.getContents().startsWith("$")) {
                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            } else {
                master_scanned_barcode = scanningResult.getContents();
                if (master_scanned_barcode.toString().trim().length() != 8) {
                    Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
                } else {
                    scanned_barcode.setText(""+master_scanned_barcode.toString().trim());
                    Toast.makeText(activity, "Master barcode scanned successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Logger.error("Cancelled from fragment");
        }
    }


    public void scanFromFragment() {
        intentIntegrator = new IntentIntegrator(activity) {
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                LMEMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
            }
        };
        intentIntegrator.initiateScan();
    }

    public void EndButtonClickedSuccess() {
        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
    }
}
