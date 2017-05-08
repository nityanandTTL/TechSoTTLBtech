package com.dhb.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.adapter.BtechCollectionsDetailsAdapter;
import com.dhb.delegate.BtechCollectionsAdapterOnscanBarcodeClickedDelegate;
import com.dhb.models.api.request.MasterBarcodeMappingRequestModel;
import com.dhb.models.api.response.BtechCollectionsResponseModel;
import com.dhb.models.data.BarcodeModel;
import com.dhb.models.data.HUBBTechModel;
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


public class BtechCollectionsListFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = BtechCollectionsListFragment.class.getSimpleName();
    LinearLayout ll_hub_display_footer, ll_fetch_order_footer, ll_olc_picup_footer, ll_scan_master_barcode;
    HomeScreenActivity activity;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<BarcodeModel> barcodeModels = new ArrayList<>();
    BtechCollectionsDetailsAdapter btechCollectionsDetailsAdapter;
    String scanned_barcode = "",master_scanned_barcode="";
    int current_position;
    View view;
    boolean isAllScanned=false;
    HUBBTechModel hubbTechModel;

    public BtechCollectionsListFragment() {
    }


    public static BtechCollectionsListFragment newInstance(HUBBTechModel hubbTechModel) {
        BtechCollectionsListFragment fragment = new BtechCollectionsListFragment();
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
        hubbTechModel = getArguments().getParcelable(BundleConstants.HUB_BTECH_MODEL);
        initUI(view);
        setListeners();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                prepareRecyclerView();
            }
        });
        fetchData();
        return view;
    }

    private void setListeners() {
        ll_scan_master_barcode.setOnClickListener(this);
    }

    private void fetchData() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
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
        ll_fetch_order_footer = (LinearLayout) view.findViewById(R.id.ll_fetch_order_footer);
        ll_olc_picup_footer = (LinearLayout) view.findViewById(R.id.ll_olc_picup_footer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        ll_scan_master_barcode = (LinearLayout) view.findViewById(R.id.ll_scan_master_barcode);
        ll_hub_display_footer.setVisibility(View.VISIBLE);
        ll_fetch_order_footer.setVisibility(View.GONE);
        ll_olc_picup_footer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_scan_master_barcode) {
            if(!isAllScanned){
                Toast.makeText(activity, "Verify all barcode", Toast.LENGTH_SHORT).show();
            }else if (validate()) {
                callMasterBarcodeMapApi();
            }
             else {

            }
        }
    }

    private void callMasterBarcodeMapApi() {
        Bundle args = new Bundle();
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel = new MasterBarcodeMappingRequestModel();
        hubbTechModel = args.getParcelable(BundleConstants.HUB_BTECH_MODEL);
        masterBarcodeMappingRequestModel.setHubId(hubbTechModel.getHubId());
        masterBarcodeMappingRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        masterBarcodeMappingRequestModel.setBarcodeModels(barcodeModels);
        masterBarcodeMappingRequestModel.setMasterBarcode("");

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
        if(/*!isAllScanned &&*/ master_scanned_barcode.equals("")){
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            scanFromFragment();

        }
        return true;
    }


    private class BtechCollectionsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
//                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                ResponseParser responseParser = new ResponseParser(activity);
                BtechCollectionsResponseModel btechCollectionsResponseModel = new BtechCollectionsResponseModel();
                btechCollectionsResponseModel = responseParser.getBtechCollectionsDetailsResponseModel(json, statusCode);
                if (btechCollectionsResponseModel != null && btechCollectionsResponseModel.getBarcode().size() > 0) {
//                    Toast.makeText(activity, "dispatchHubDisplayDetailsResponseModel not null", Toast.LENGTH_SHORT).show();
                    barcodeModels = btechCollectionsResponseModel.getBarcode();
                    Logger.error("hubbTechModels size " + btechCollectionsResponseModel.getBarcode().size());
                    prepareRecyclerView();
                } else {
                    Logger.error("else " + json);
                }
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
            scanned_barcode = scanningResult.getContents();
            master_scanned_barcode=scanningResult.getContents();
            if (!scanned_barcode.equals("" + barcodeModels.get(current_position).getBarcode())) {
                Toast.makeText(activity, "no match! Try again", Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.RED);
                isAllScanned = false;
            } else {
                Toast.makeText(activity, "barcode match! ", Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.GREEN);
                isAllScanned = true;
            }
        }
        else{
            Logger.error("Cancelled from fragment");
            isAllScanned=false;
        }
    }


    public void scanFromFragment() {
        new IntentIntegrator(activity).initiateScan();
    }

    private void prepareRecyclerView() {
        btechCollectionsDetailsAdapter = new BtechCollectionsDetailsAdapter(barcodeModels, activity, new BtechCollectionsAdapterOnscanBarcodeClickedDelegate() {
            @Override
            public void onItemClicked(BarcodeModel barcodeModel, int position, View v) {
                current_position = position;
                scanFromFragment();
                view = v;
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(btechCollectionsDetailsAdapter);
    }

    private class MasterBarcodeMappingApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
