package com.dhb.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.adapter.HubScanBarcodeListAdapter;
import com.dhb.models.api.request.MasterBarcodeMappingRequestModel;
import com.dhb.models.api.response.BtechCollectionsResponseModel;
import com.dhb.models.data.HUBBTechModel;
import com.dhb.models.data.HubBarcodeModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.ArrayList;


public class HubMasterBarcodeScanFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = HubMasterBarcodeScanFragment.class.getSimpleName();
    private LinearLayout ll_hub_display_footer, ll_scan_master_barcode,ll_scan_vial_barcode;
    private HomeScreenActivity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HubBarcodeModel> barcodeModels = new ArrayList<>();
    private HubScanBarcodeListAdapter hubScanBarcodeListAdapter;
    private String master_scanned_barcode="";
    HUBBTechModel hubbTechModel;
    private boolean isMasterBarcode;
    private IntentIntegrator intentIntegrator;
    private Button btnDispatch;
    private boolean isCentrifuged = false;
    public HubMasterBarcodeScanFragment() {
    }


    public static HubMasterBarcodeScanFragment newInstance(HUBBTechModel hubbTechModel) {
        HubMasterBarcodeScanFragment fragment = new HubMasterBarcodeScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.HUB_BTECH_MODEL, hubbTechModel);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btech_collections_list, container, false);
        activity = (HomeScreenActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        hubbTechModel = getArguments().getParcelable(BundleConstants.HUB_BTECH_MODEL);
        initUI(view);
        setListeners();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchData();
            }
        });
        fetchData();
        return view;
    }

    private void setListeners() {
        ll_scan_master_barcode.setOnClickListener(this);
        ll_scan_vial_barcode.setOnClickListener(this);
        btnDispatch.setOnClickListener(this);
    }

    private void fetchData() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchBtechCollectionsListDetailApiAsyncTask = asyncTaskForRequest.getBtechCollectionListRequestAsyncTask();
        fetchBtechCollectionsListDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new BtechCollectionsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchBtechCollectionsListDetailApiAsyncTask.execute(fetchBtechCollectionsListDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI(View view) {
        ll_hub_display_footer = (LinearLayout) view.findViewById(R.id.ll_hub_display_footer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        ll_scan_master_barcode = (LinearLayout) view.findViewById(R.id.ll_scan_master_barcode);
        ll_scan_vial_barcode = (LinearLayout) view.findViewById(R.id.ll_scan_vial_barcode);
        ll_hub_display_footer.setVisibility(View.VISIBLE);
        btnDispatch = (Button) view.findViewById(R.id.btn_dispatch);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_scan_master_barcode) {
            isMasterBarcode = true;
            scanFromFragment();
        }
        else if (v.getId() == R.id.ll_scan_vial_barcode) {
            isMasterBarcode = false;
            scanFromFragment();
        }
        else if(v.getId()==R.id.btn_dispatch){
            if(validate()) {
                callMasterBarcodeMapApi();
            }
        }
    }
    private void callMasterBarcodeMapApi() {
        MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel = new MasterBarcodeMappingRequestModel();
        masterBarcodeMappingRequestModel.setHubId(hubbTechModel.getHubId());
        masterBarcodeMappingRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        ArrayList<HubBarcodeModel> scannedBarcodesArr = new ArrayList<>();
        for (HubBarcodeModel hbm:
             barcodeModels) {
            if(hbm.isScanned()){
                scannedBarcodesArr.add(hbm);
            }
        }
        masterBarcodeMappingRequestModel.setBarcodes(scannedBarcodesArr);
        masterBarcodeMappingRequestModel.setMasterBarcode(master_scanned_barcode);

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask postMasterBarcodeMapDetailApiAsyncTask = asyncTaskForRequest.getMasterBarcodeMapRequestAsyncTask(masterBarcodeMappingRequestModel);
        postMasterBarcodeMapDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new MasterBarcodeMappingApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            postMasterBarcodeMapDetailApiAsyncTask.execute(postMasterBarcodeMapDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if(master_scanned_barcode.equals("")){
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private class BtechCollectionsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                BtechCollectionsResponseModel btechCollectionsResponseModel = new BtechCollectionsResponseModel();
                btechCollectionsResponseModel = responseParser.getBtechCollectionsDetailsResponseModel(json, statusCode);
                if (btechCollectionsResponseModel != null && btechCollectionsResponseModel.getBarcode()!=null && btechCollectionsResponseModel.getBarcode().size() > 0) {
                    barcodeModels = btechCollectionsResponseModel.getBarcode();
                    isCentrifuged = false;
                    prepareRecyclerView();
                } else {
                    Toast.makeText(activity,"No records found",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "api cancelled ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            if(!isMasterBarcode) {
                String scanned_barcode = scanningResult.getContents();
                for (int i=0;i<barcodeModels.size();i++){
                    if(barcodeModels.get(i).getBarcode().equals(scanned_barcode)){
                        barcodeModels.get(i).setScanned(true);
                        break;
                    }
                }
                hubScanBarcodeListAdapter.notifyDataSetChanged();
            }
            else{
                master_scanned_barcode = scanningResult.getContents();
            }
        } else {
            Logger.error("Cancelled from fragment");
        }
    }


    public void scanFromFragment() {
        intentIntegrator = new IntentIntegrator(activity){
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                HubMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
            }
        };
        intentIntegrator.initiateScan();
    }

    private void prepareRecyclerView() {
        hubScanBarcodeListAdapter = new HubScanBarcodeListAdapter(barcodeModels, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hubScanBarcodeListAdapter);
        if(!isCentrifuged) {
            int serumCount = 0;
            for (HubBarcodeModel hbm :
                    barcodeModels) {
                if (hbm.getSampleType().equalsIgnoreCase("serum")) {
                    serumCount++;
                }
            }
            if (serumCount > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Centrifuge")
                        .setMessage("Please Centrifuge " + serumCount + " Serum Vials")
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    private class MasterBarcodeMappingApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
                    }
                },3000);
            }
            else{
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
