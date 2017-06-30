package com.dhb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.adapter.BtechwithHub_HubScanBarcodeListAdapter;
import com.dhb.models.api.request.BtechwithHub_MasterBarcodeMappingRequestModel;
import com.dhb.models.api.response.BtechwithHubResponseModel;
import com.dhb.models.data.BtechwithHub_BarcodeDataModel;
import com.dhb.models.data.HUBBTechModel;
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


public class BtechwithHub_HubMasterBarcodeScanFragment extends AbstractFragment implements View.OnClickListener {

    public static final String TAG_FRAGMENT = BtechwithHub_HubMasterBarcodeScanFragment.class.getSimpleName();
    private LinearLayout ll_hub_display_footer, ll_scan_master_barcode, ll_scan_vial_barcode;
    private HomeScreenActivity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<BtechwithHub_BarcodeDataModel> barcodeModels = new ArrayList<>();
    private BtechwithHub_HubScanBarcodeListAdapter hubScanBarcodeListAdapter;

    private String master_scanned_barcode = "";
    HUBBTechModel hubbTechModel;
    private boolean isMasterBarcode;
    private IntentIntegrator intentIntegrator;
    private Button btn_receive;
    private boolean isCentrifuged = false;

    public BtechwithHub_HubMasterBarcodeScanFragment() {
    }

    //btech_hub
    public static BtechwithHub_HubMasterBarcodeScanFragment newInstance() {
        return new BtechwithHub_HubMasterBarcodeScanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.btechwithhub_fragment_btech_collections_list, container, false);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Hub Scan");
        appPreferenceManager = new AppPreferenceManager(activity);

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
        btn_receive.setOnClickListener(this);
    }

    private void fetchData() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchBtechwithHubBarcodeApiAsyncTask = asyncTaskForRequest.getfetchBtechwithHubBarcodeApiAsyncTaskRequestAsyncTask();
        fetchBtechwithHubBarcodeApiAsyncTask.setApiCallAsyncTaskDelegate(new BtechwithHubBarcodeApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchBtechwithHubBarcodeApiAsyncTask.execute(fetchBtechwithHubBarcodeApiAsyncTask);
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
        btn_receive = (Button) view.findViewById(R.id.btn_receive);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_scan_master_barcode) {
            isMasterBarcode = true;
            scanFromFragment();
        } else if (v.getId() == R.id.ll_scan_vial_barcode) {
            isMasterBarcode = false;
            scanFromFragment();
        } else if (v.getId() == R.id.btn_receive) {
            if (validate()) {
                callMasterBarcodeMapApi();
            }
        }
    }

    private void callMasterBarcodeMapApi() {
        BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel = new BtechwithHub_MasterBarcodeMappingRequestModel();

        masterBarcodeMappingRequestModel.setHubId(appPreferenceManager.getBtechID());
        masterBarcodeMappingRequestModel.setBtechId("");
        ArrayList<BtechwithHub_BarcodeDataModel> scannedBarcodesArr = new ArrayList<>();
        for (BtechwithHub_BarcodeDataModel hbm :
                barcodeModels) {
            if (hbm.isReceived()) {
                scannedBarcodesArr.add(hbm);
            }
        }

        masterBarcodeMappingRequestModel.setBarcodes(scannedBarcodesArr);
        masterBarcodeMappingRequestModel.setMasterBarcode(master_scanned_barcode);

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask bTECHWITHhUB_postMasterBarcodeMapDetailApiAsyncTask = asyncTaskForRequest.getbTECHWITHhUB_MasterBarcodeMapRequestAsyncTask(masterBarcodeMappingRequestModel);
        bTECHWITHhUB_postMasterBarcodeMapDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new bTECHWITHhUB_MasterBarcodeMappingApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            bTECHWITHhUB_postMasterBarcodeMapDetailApiAsyncTask.execute(bTECHWITHhUB_postMasterBarcodeMapDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if (master_scanned_barcode.equals("")) {
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            if (!isMasterBarcode) {
                String scanned_barcode = scanningResult.getContents();

                for (int i = 0; i < barcodeModels.size(); i++) {
                    if (barcodeModels.get(i).getBarcode().equals(scanned_barcode)) {
                        Logger.debug("inside loop" + "1");

                        if (barcodeModels.get(i).isReceived()) {
                            Logger.debug("inside loop" + "true");
                            Toast.makeText(activity, "Same Barcode is Already Scanned", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            barcodeModels.get(i).setReceived(true);
                            break;
                        }

                    }
                }

                hubScanBarcodeListAdapter.notifyDataSetChanged();
            } else {
                master_scanned_barcode = scanningResult.getContents();
            }
        } else {
            Logger.error("Cancelled from fragment");
        }
    }


    public void scanFromFragment() {
        intentIntegrator = new IntentIntegrator(activity) {
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                BtechwithHub_HubMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
            }
        };
        intentIntegrator.initiateScan();
    }

    private void prepareRecyclerView() {
        Logger.debug("onsode prepareRecyclerView" + "true");

        hubScanBarcodeListAdapter = new BtechwithHub_HubScanBarcodeListAdapter(barcodeModels, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hubScanBarcodeListAdapter);

       /* if (!isCentrifuged) {
            int serumCount = 0;
            for (BtechwithHub_BarcodeDataModel hbm :
                    barcodeModels) {
                if (hbm.getBarcodeType().equalsIgnoreCase("serum")) {
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
        }*/
    }

    private class BtechwithHubBarcodeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            System.out.println("@---->\n" + json);
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);

                BtechwithHubResponseModel btechwithHubResponseModel;
                btechwithHubResponseModel = responseParser.getBtechwithHubBarcodeResponseModel(json, statusCode);

                if (btechwithHubResponseModel != null && btechwithHubResponseModel.getReceivedHub().get(0).getBarcode() != null && btechwithHubResponseModel.getReceivedHub().size() > 0) {
                    barcodeModels = btechwithHubResponseModel.getReceivedHub();
                    isCentrifuged = false;
                    prepareRecyclerView();
                } else {
                    Toast.makeText(activity, "No records found", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (IS_DEBUG)
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "api cancelled ", Toast.LENGTH_SHORT).show();
        }
    }

    private class bTECHWITHhUB_MasterBarcodeMappingApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                    }
                }, 3000);
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
