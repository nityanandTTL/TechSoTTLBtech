package com.thyrocare.fragment.LME;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.TSPLMESampleDropController;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.LMEMapDisplayFragmentActivity;
import com.thyrocare.adapter.LME.LMEVisitsListAdapter;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.models.data.ScannedMasterBarcodebyLMEPOSTDATAModel;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.GPSTracker;

import java.util.ArrayList;

/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class LME_OrdersDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LME_OrdersDisplayFragment";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    static LME_OrdersDisplayFragment fragment;
    private RecyclerView recy_Orderlist;
    TextView txt_nodata;
    private SampleDropDetailsbyTSPLMEDetailsModel msampleDropDetailsbyTSPLMEDetailsModel;

    public LME_OrdersDisplayFragment() {
        // Required empty public constructor
    }

    public static LME_OrdersDisplayFragment newInstance() {
        fragment = new LME_OrdersDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Visit Orders");
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lme_orders_display, container, false);
        initUI();
        fetchData();
        return rootView;
    }

    @Override
    public void initUI() {
        recy_Orderlist = (RecyclerView) rootView.findViewById(R.id.recy_Orderlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recy_Orderlist.setLayoutManager(mLayoutManager);
        recy_Orderlist.setItemAnimator(new DefaultItemAnimator());
        txt_nodata = (TextView) rootView.findViewById(R.id.txt_nodata);
    }

    private void fetchData() {
        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }

        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallGetSampleDropDetailsbyTSPLME(appPreferenceManager.getLoginResponseModel().getUserID(), BundleConstants.batch_code);
    }

    public void SetOrdersList(ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> materialDetailsModels) {
        System.out.println("");

        if (materialDetailsModels.size() != 0) {
            LMEVisitsListAdapter adaptor = new LMEVisitsListAdapter(materialDetailsModels, fragment);
            recy_Orderlist.setAdapter(adaptor);
            recy_Orderlist.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
        }else {
            NodataFound();
        }
    }

    public void NodataFound() {
        recy_Orderlist.setVisibility(View.GONE);
        txt_nodata.setVisibility(View.VISIBLE);
    }

    public void StartEndButtonClicked(final SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsbyTSPLMEDetailsModel) {
        msampleDropDetailsbyTSPLMEDetailsModel = sampleDropDetailsbyTSPLMEDetailsModel;
        Intent intentMapDisplay = new Intent(activity, LMEMapDisplayFragmentActivity.class);
        intentMapDisplay.putExtra(BundleConstants.LME_ORDER_MODEL, sampleDropDetailsbyTSPLMEDetailsModel);
        startActivityForResult(intentMapDisplay, BundleConstants.LME_START);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleConstants.LME_START && resultCode == BundleConstants.LME_ARRIVED) {
            pushFragments(LMEMasterBarcodeScanFragment.newInstance(msampleDropDetailsbyTSPLMEDetailsModel), false, false, LMEMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
        }
    }

    private void StartPostScannedMasterBarcodebyLME(SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsbyTSPLMEDetailsModel) {
        ScannedMasterBarcodebyLMEPOSTDATAModel n = null;
        try {
            GPSTracker gpsTracker = new GPSTracker(activity);
            n = new ScannedMasterBarcodebyLMEPOSTDATAModel();
            n.setMasterBarcode("");
            n.setSampleDropIds("" + sampleDropDetailsbyTSPLMEDetailsModel.getSampleDropId());
            n.setStatus("1");
            n.setLatitude(String.valueOf(gpsTracker.getLatitude()));
            n.setLongitude(String.valueOf(gpsTracker.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }

        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallPostScannedMasterBarcodebyLME(n);
    }

    public void StartButtonClickedSuccess() {

        //ToDo Map start arrived arrived

        pushFragments(LMEMasterBarcodeScanFragment.newInstance(msampleDropDetailsbyTSPLMEDetailsModel), false, false, LMEMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
    }
}
