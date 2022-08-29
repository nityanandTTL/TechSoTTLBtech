package com.thyrocare.btechapp.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.BtechwithHub_HubScanBarcodeListAdapter;
import com.thyrocare.btechapp.models.api.request.BtechwithHub_MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.response.BtechwithHubResponseModel;
import com.thyrocare.btechapp.models.data.BtechwithHub_BarcodeDataModel;
import com.thyrocare.btechapp.models.data.HUBBTechModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * 　APi Used 　　i)/SpecimenTrack/ReceiveHubBarcode/Userid<br/>
 * ii)/SpecimenTrack/ReceiveHubBarcodes
 * <p>
 * Created by Orion on 7/4/2017.
 */


public class TSP_HubMasterBarcodeScanFragment extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_FRAGMENT = TSP_HubMasterBarcodeScanFragment.class.getSimpleName();
    TextView tv_collection_sample;
    HUBBTechModel hubbTechModel;
    AppPreferenceManager appPreferenceManager;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    private LinearLayout ll_hub_display_footer, ll_scan_master_barcode, ll_scan_vial_barcode;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<BtechwithHub_BarcodeDataModel> barcodeModels = new ArrayList<>();
    private BtechwithHub_HubScanBarcodeListAdapter hubScanBarcodeListAdapter;
    private String master_scanned_barcode = "";
    private boolean isMasterBarcode;
    private IntentIntegrator intentIntegrator;
    private Button btn_receive;
    private GPSTracker gpsTracker;
    private boolean isCentrifuged = false;
    private Global global;

    public TSP_HubMasterBarcodeScanFragment() {
    }

    //btech_hub
    public static TSP_HubMasterBarcodeScanFragment newInstance() {
        return new TSP_HubMasterBarcodeScanFragment();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tsp_fragment_btech_collections_list);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        initUI();
        setListeners();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchData();
            }
        });
        fetchData();
    }


    private void setListeners() {
        ll_scan_master_barcode.setOnClickListener(this);
        ll_scan_vial_barcode.setOnClickListener(this);
        btn_receive.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void fetchData() {
        if (isNetworkAvailable(activity)) {
            CallgetTspBarcodeApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        ll_hub_display_footer = (LinearLayout) findViewById(R.id.ll_hub_display_footer);
        tv_collection_sample = findViewById(R.id.tv_collection_sample);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ll_scan_master_barcode = (LinearLayout) findViewById(R.id.ll_scan_master_barcode);
        ll_scan_vial_barcode = (LinearLayout) findViewById(R.id.ll_scan_vial_barcode);
        ll_hub_display_footer.setVisibility(VISIBLE);
        tv_collection_sample.setVisibility(VISIBLE);
        btn_receive = (Button) findViewById(R.id.btn_receive);

        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("TSP Scan");

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
        } else if (v.getId() == R.id.iv_home) {
            startActivity(new Intent(activity, HomeScreenActivity.class));

        } else if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    private void callMasterBarcodeMapApi() {
        gpsTracker = new GPSTracker(activity);
        BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel = new BtechwithHub_MasterBarcodeMappingRequestModel();

        masterBarcodeMappingRequestModel.setHubId(appPreferenceManager.getBtechID());
        masterBarcodeMappingRequestModel.setBtechId("");
        ArrayList<BtechwithHub_BarcodeDataModel> scannedBarcodesArr = new ArrayList<>();

        for (BtechwithHub_BarcodeDataModel hbm :
                barcodeModels) {
            if (hbm.isReceived()) {
                if (gpsTracker != null) {
                    hbm.setLatitude("" + String.valueOf(gpsTracker.getLatitude()));
                    hbm.setLongitude("" + String.valueOf(gpsTracker.getLongitude()));
                } else {
                    hbm.setLatitude("");
                    hbm.setLongitude("");
                }
                scannedBarcodesArr.add(hbm);
            }
        }

        masterBarcodeMappingRequestModel.setBarcodes(scannedBarcodesArr);
        masterBarcodeMappingRequestModel.setMasterBarcode(master_scanned_barcode);

        if (isNetworkAvailable(activity)) {
            CallgetTSP_MasterBarcodeMapRequestAPI(masterBarcodeMappingRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if (master_scanned_barcode.equals("")) {
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (master_scanned_barcode.toString().trim().length() < 8) {
            Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {

            if (scanningResult.getContents().startsWith("0") || scanningResult.getContents().startsWith("$")) {
                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            } else {

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
                    try {
                        hubScanBarcodeListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    master_scanned_barcode = scanningResult.getContents();
                    if (master_scanned_barcode.toString().trim().length() < 8) {
                        Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Master barcode scanned successfully", Toast.LENGTH_SHORT).show();
                    }
                    Logger.debug("result***" + master_scanned_barcode);
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
                TSP_HubMasterBarcodeScanFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
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

    }

    private void CallgetTspBarcodeApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<BtechwithHubResponseModel> responseCall = apiInterface.CallgetTspBarcodeApi(appPreferenceManager.getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<BtechwithHubResponseModel>() {
            @Override
            public void onResponse(Call<BtechwithHubResponseModel> call, retrofit2.Response<BtechwithHubResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    BtechwithHubResponseModel btechwithHubResponseModel = response.body();
                    if (btechwithHubResponseModel != null && btechwithHubResponseModel.getReceivedHub() != null && btechwithHubResponseModel.getReceivedHub().size() > 0 && btechwithHubResponseModel.getReceivedHub().get(0).getBarcode() != null) {
                        barcodeModels = btechwithHubResponseModel.getReceivedHub();
                        isCentrifuged = false;
                        prepareRecyclerView();
                    } else {
                        Toast.makeText(activity, "No records found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<BtechwithHubResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallgetTSP_MasterBarcodeMapRequestAPI(BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetTSP_MasterBarcodeMapRequestAPI(masterBarcodeMappingRequestModel);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setMessage("Received Successfully.");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(activity, HomeScreenActivity.class));
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
            }
        });
    }
}
