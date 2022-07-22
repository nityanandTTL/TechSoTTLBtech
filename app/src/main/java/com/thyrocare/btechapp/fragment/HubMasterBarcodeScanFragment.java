package com.thyrocare.btechapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.VenupunctureAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.HubScanBarcodeListAdapter;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.models.api.request.MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.request.VenupunctureUploadRequestModel;
import com.thyrocare.btechapp.models.api.response.BtechCollectionsResponseModel;
import com.thyrocare.btechapp.models.data.HUBBTechModel;
import com.thyrocare.btechapp.models.data.HubBarcodeModel;


import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

/**
 * 　APi Used 　　BtechCollections/userid<br/>
 * 　　* /MasterBarcodeMapping/
 * Created by Orion on 7/4/2017.
 */

public class HubMasterBarcodeScanFragment extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_FRAGMENT = HubMasterBarcodeScanFragment.class.getSimpleName();
    private LinearLayout ll_hub_display_footer, ll_scan_master_barcode, ll_scan_vial_barcode;
    private TextView tv_collection_sample;
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HubBarcodeModel> barcodeModels = new ArrayList<>();
    private HubScanBarcodeListAdapter hubScanBarcodeListAdapter;
    private String master_scanned_barcode = "";
    HUBBTechModel hubbTechModel;
    private boolean isMasterBarcode;
    private IntentIntegrator intentIntegrator;
    private Button btnDispatch;
    private boolean isCentrifuged = false;
    private TextView tv_centrifuge;
    private Global global;
    AppPreferenceManager appPreferenceManager;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    TextView txt_master_barcode;
    private File selfie_photo;
    private Uri selfieUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_btech_collections_list);
        activity = this;
        global = new Global(activity);

        appPreferenceManager = new AppPreferenceManager(activity);
        hubbTechModel = getIntent().getExtras().getParcelable(BundleConstants.HUB_BTECH_MODEL);
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
        btnDispatch.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void fetchData() {
        if (isNetworkAvailable(activity)) {
            CallGetBtechCollectionListApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void initUI() {

        if (HubListDisplayFragment.flowDecider == 1) {          //for btech_hub flow
            tv_centrifuge = (TextView) findViewById(R.id.tv_centrifuge);
            tv_centrifuge.setText("Scan Barcode");
        }

        ll_hub_display_footer = (LinearLayout) findViewById(R.id.ll_hub_display_footer);
        tv_collection_sample = findViewById(R.id.tv_collection_sample);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        ll_scan_master_barcode = (LinearLayout) findViewById(R.id.ll_scan_master_barcode);
        ll_scan_vial_barcode = (LinearLayout) findViewById(R.id.ll_scan_vial_barcode);
        ll_hub_display_footer.setVisibility(VISIBLE);
        tv_collection_sample.setVisibility(VISIBLE);
        btnDispatch = (Button) findViewById(R.id.btn_dispatch);
        txt_master_barcode = findViewById(R.id.txt_master_barcode);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_home = findViewById(R.id.iv_home);
        iv_back = findViewById(R.id.iv_back);
        tv_toolbar.setText("Hub Scan");
        iv_home.setVisibility(View.GONE);
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        btnDispatch.setText(activity.getString(R.string.upload_selfie));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_scan_master_barcode) {
            if (barcodeModels != null && barcodeModels.size() > 0) {
                isMasterBarcode = true;
                scanFromFragment();
            } else {
                Toast.makeText(activity, "No barcode to scan.", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.ll_scan_vial_barcode) {
            if (barcodeModels != null && barcodeModels.size() > 0) {
                isMasterBarcode = false;
                scanFromFragment();
            } else {
                Toast.makeText(activity, "No barcode to scan.", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.btn_dispatch) {
            onClickDispatchBtn();
        } else if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.iv_home) {
            startActivity(new Intent(activity, HomeScreenActivity.class));
        }
    }

    private void onClickDispatchBtn() {

        if (InputUtils.CheckEqualIgnoreCase(btnDispatch.getText().toString(), activity.getString(R.string.dispatch))) {
            if (validate()) {
                callMasterBarcodeMapApi();
            }
        } else if (InputUtils.CheckEqualIgnoreCase(btnDispatch.getText().toString(), activity.getString(R.string.upload_selfie))) {
            if (validate()) {
                uploadSelfie();
            }
        }
    }

    private void uploadSelfie() {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.CAMERA)
                .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Global.cropImageFullScreenActivity(activity, 3);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(activity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void callMasterBarcodeMapApi() {
        MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel = new MasterBarcodeMappingRequestModel();
        masterBarcodeMappingRequestModel.setHubId(hubbTechModel.getHubId());
        masterBarcodeMappingRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        ArrayList<HubBarcodeModel> scannedBarcodesArr = new ArrayList<>();
        for (HubBarcodeModel hbm :
                barcodeModels) {
            if (hbm.isScanned()) {
                scannedBarcodesArr.add(hbm);
            }
        }
        GPSTracker gpsTracker = new GPSTracker(activity);
        for (int i = 0; i < scannedBarcodesArr.size(); i++) {
            scannedBarcodesArr.get(i).setLatitude(String.valueOf(gpsTracker.getLatitude()));
            scannedBarcodesArr.get(i).setLongitude(String.valueOf(gpsTracker.getLongitude()));
        }
        masterBarcodeMappingRequestModel.setBarcodes(scannedBarcodesArr);
        masterBarcodeMappingRequestModel.setMasterBarcode(master_scanned_barcode);
        if (isNetworkAvailable(activity)) {
            CallGetMasterBarcodeMapApi(masterBarcodeMappingRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {

        if (barcodeModels == null) {
            Toast.makeText(activity, "you cannot dispatch", Toast.LENGTH_SHORT).show();
            return false;
        } else if (barcodeModels.size() <= 0) {
            Toast.makeText(activity, "you cannot dispatch", Toast.LENGTH_SHORT).show();
            return false;
        } else if (master_scanned_barcode.equals("")) {
            Toast.makeText(activity, "scan for master barcode first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (master_scanned_barcode.toString().trim().length() != 8) {
            Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void CallGetBtechCollectionListApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<BtechCollectionsResponseModel> responseCall = apiInterface.CallGetBtechCollectionListApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<BtechCollectionsResponseModel>() {
            @Override
            public void onResponse(Call<BtechCollectionsResponseModel> call, retrofit2.Response<BtechCollectionsResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    BtechCollectionsResponseModel btechCollectionsResponseModel = response.body();
                    if (btechCollectionsResponseModel != null && btechCollectionsResponseModel.getBarcode() != null && btechCollectionsResponseModel.getBarcode().size() > 0) {
                        barcodeModels = btechCollectionsResponseModel.getBarcode();
                        isCentrifuged = false;
                        prepareRecyclerView();
                    } else {
                        Toast.makeText(activity, "No records found", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    CallLogOutFromComDevice();
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BtechCollectionsResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    public void CallLogOutFromComDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            try {
                new LogUserActivityTagging(activity, LOGOUT, "");
                appPreferenceManager.clearAllPreferences();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                DhbDao dhbDao;
                dhbDao = new DhbDao(activity);
                dhbDao.deleteTablesonLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(activity, LoginActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(n);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {


          /*  AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
            builder1.setTitle("Check the Barcode ")
                    .setMessage("Do you want to Proceed with this barcode entry " + scanningResult + "?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
*/
            if (scanningResult.getContents().startsWith("0") || scanningResult.getContents().startsWith("$") || scanningResult.getContents().startsWith("1") || scanningResult.getContents().startsWith(" ")) {
                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            } else {
                if (!isMasterBarcode) {
                    String scanned_barcode = scanningResult.getContents();
                    if (scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$") || scanned_barcode.startsWith("1") || scanned_barcode.startsWith(" ")) {
                        Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < barcodeModels.size(); i++) {
                            if (barcodeModels.get(i).getBarcode().equals(scanned_barcode) || barcodeModels.get(i).getBarcode().toString().trim().toLowerCase().equals(scanned_barcode.toString().trim().toLowerCase())) {
                                if (barcodeModels.get(i).isScanned()) {
                                    Toast.makeText(activity, "Same Barcode is Already Scanned", Toast.LENGTH_SHORT).show();
                                    break;
                                } else {
                                    barcodeModels.get(i).setScanned(true);
                                    break;
                                }
                            }
                        }
                        try {
                            hubScanBarcodeListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    master_scanned_barcode = scanningResult.getContents();
                    if (master_scanned_barcode.toString().trim().length() != 8) {
                        Toast.makeText(activity, "Invalid master barcode", Toast.LENGTH_SHORT).show();
                    } else {
                        txt_master_barcode.setVisibility(VISIBLE);
                        txt_master_barcode.setText("Master Barcode:" + master_scanned_barcode);
                        Toast.makeText(activity, "Master barcode scanned successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // }
            // });


        }
        //Handle Upload selfie image picked
        else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                String imageurl = null;
                Uri selectedImageUri = data.getData();
                imageurl = selectedImageUri.getPath();

                selfie_photo = new File(imageurl);
                selfieUri = Uri.fromFile(selfie_photo);
                if (selfieUri == null) {
                    Toast.makeText(activity, "Failed to capture selfie", LENGTH_SHORT).show();
                    return;
                }
                CallUploadSelfieAPI();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, "Something went wrong", LENGTH_SHORT).show();
            }
        } else {
            Logger.error("Cancelled from fragment");
        }
    }

    private void CallUploadSelfieAPI() {
        VenupunctureUploadRequestModel vrm = new VenupunctureUploadRequestModel();
        vrm.setBENID(appPreferenceManager.getLoginResponseModel().getUserID());
        vrm.setFile(selfie_photo);
        vrm.setTYPE("HUB_DROP");
        vrm.setAPPID("");
        vrm.setTEST("");
        vrm.setORDERNO("");
        VenupunctureAPIController vc = new VenupunctureAPIController(activity, HubMasterBarcodeScanFragment.this, vrm);
        vc.CallAPI(vrm);
    }

    public void UpdateDispatchBtn() {
        btnDispatch.setText(R.string.dispatch);
    }


    public void scanFromFragment() {
        intentIntegrator = new IntentIntegrator(activity) {
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
        if (!isCentrifuged) {
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

    private void CallGetMasterBarcodeMapApi(MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallGetMasterBarcodeMapApi(masterBarcodeMappingRequestModel);
        global.showProgressDialog(activity, "Submitting data. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setMessage("Dispatched Successfully.");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(activity, HomeScreenActivity.class);
                                    startActivity(intent);
//                                    pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


}
