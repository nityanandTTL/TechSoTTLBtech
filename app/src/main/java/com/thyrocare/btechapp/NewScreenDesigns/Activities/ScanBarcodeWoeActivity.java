package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.Controller.VenupunctureAPIController;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ScanBarcodeViewPagerAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.VenupunctureUploadRequestModel;
import com.thyrocare.btechapp.models.api.response.GetTestListResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryBarcodeDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.BeneficiarySampleTypeDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;
import com.thyrocare.btechapp.utils.fileutils.FilePath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.NO_DATA_FOUND;

public class ScanBarcodeWoeActivity extends AppCompatActivity {

    private static String TAG = ScanBarcodeWoeActivity.class.getSimpleName();
    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    ArrayList<BeneficiaryDetailsModel> beneficaryWiseArylst;
    Runnable runnable;
    TextView btn_Next, btn_Previous;
    boolean isRescan = false;
    TextView tv_toolbar;
    ImageView iv_back, iv_home;
    File BenBarcodePicimagefile;
    File Affidavitfile;
    String fileName = "", filepath = "";
    int ImageFlag = 0;
    int DelFlag;
    boolean isRemovedUrine = false;
    private ViewPager BarcodeScanviewpager;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private IntentIntegrator intentIntegrator;
    private int page = 0;
    private int dotsCount;
    private ImageView[] dots;
    private CardView viewPagerIndicator;
    private LinearLayout viewPagerCountDots;
    private ScanBarcodeViewPagerAdapter mAdapter;
    private String SampleTypeToScan = "";
    private int BenIDToScan = 0, BarcodepositionToScan = 0;
    private int i = 0, BenPositionForDelete = 0;
    private Camera camera;
    private int BenIDToCaptureVenuPhoto = 0, PositionToStoreVenuPhoto = 0, BenIDToDeleteVenuPhoto = 0, PositionToDeleteVenuPhoto = 0, BenIDToUploadAffidavit = 0, BenIDToDeleteAffidavit = 0, PositionToDeleteAffidavit = 0;
    private TextView btn_Proceed;
    private Spinner spn_ben;
    private int pick = 1123;
    private int Select_PDFFILE = 2121;
    private String EnteredbenCode = "";

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity).create();
        alertDialog.setMessage("All changes made will be reset.\nAre you sure, you want to go back ?");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BundleConstants.setRefreshStartArriveActivity = 1;
                        dialog.dismiss();
                        //onbackpress();
                        finish();

                    }
                });
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan_woe_main);
//        setTitle("Barcode Scanning");
        mActivity = ScanBarcodeWoeActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);

        initView();
//        initToolBar();
        initData();
        initListener();
    }

  /*  private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStockAvailablity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu_screen, menu);
        return true;
    }
*/
    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    private void initView() {
        BarcodeScanviewpager = (ViewPager) findViewById(R.id.BarcodeScanviewpager);
        viewPagerIndicator = (CardView) findViewById(R.id.viewPagerIndicator);
        viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        spn_ben = (Spinner) findViewById(R.id.spn_ben);
        btn_Previous = (TextView) findViewById(R.id.btn_Previous);
        btn_Next = (TextView) findViewById(R.id.btn_Next);
        btn_Proceed = (TextView) findViewById(R.id.btn_Proceed);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("WOE");
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        iv_home.setVisibility(View.GONE);
    }

    private void initData() {
        if (orderVisitDetailsModel != null
                && orderVisitDetailsModel.getAllOrderdetails() != null
                && orderVisitDetailsModel.getAllOrderdetails().size() > 0
                && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null
                && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 0) {

            beneficaryWiseArylst = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster();

            ArrayAdapter<BeneficiaryDetailsModel> spinnerArrayAdapter = new ArrayAdapter<BeneficiaryDetailsModel>(mActivity, android.R.layout.simple_spinner_item, beneficaryWiseArylst); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_ben.setAdapter(spinnerArrayAdapter);


            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                boolean isBarcodeAndSampleListSame = false;
                if (beneficaryWiseArylst.get(i).getBarcodedtl() != null
                        && beneficaryWiseArylst.get(i).getSampleType() != null
                        && beneficaryWiseArylst.get(i).getBarcodedtl().size() == beneficaryWiseArylst.get(i).getSampleType().size()) {
                    int sameSampleTypeCnt = 0;
                    for (BeneficiaryBarcodeDetailsModel bbdm :
                            beneficaryWiseArylst.get(i).getBarcodedtl()) {
                        for (BeneficiarySampleTypeDetailsModel bstdm :
                                beneficaryWiseArylst.get(i).getSampleType()) {
                            if (bbdm.getSamplType().equals(bstdm.getSampleType())) {
                                sameSampleTypeCnt++;
                                break;
                            }
                        }
                    }
                    if (sameSampleTypeCnt == beneficaryWiseArylst.get(i).getBarcodedtl().size() && sameSampleTypeCnt == beneficaryWiseArylst.get(i).getSampleType().size()) {
                        isBarcodeAndSampleListSame = true;
                    }
                }
                if (!isBarcodeAndSampleListSame) {
                    if (beneficaryWiseArylst.get(i).getBarcodedtl() == null) {
                        beneficaryWiseArylst.get(i).setBarcodedtl(new ArrayList<BeneficiaryBarcodeDetailsModel>());
                    } else {
                        beneficaryWiseArylst.get(i).getBarcodedtl().clear();
                    }

                    if (beneficaryWiseArylst.get(i) != null && beneficaryWiseArylst.get(i).getSampleType() != null) {
                        for (BeneficiarySampleTypeDetailsModel sampleTypes :
                                beneficaryWiseArylst.get(i).getSampleType()) {
                            Logger.error("sample type: " + beneficaryWiseArylst.get(i).getSampleType());
                            BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = new BeneficiaryBarcodeDetailsModel();
                            beneficiaryBarcodeDetailsModel.setBenId(beneficaryWiseArylst.get(i).getBenId());
                            beneficiaryBarcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                            beneficiaryBarcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                            beneficiaryBarcodeDetailsModel.setOrderNo(beneficaryWiseArylst.get(i).getOrderNo());
                            beneficaryWiseArylst.get(i).getBarcodedtl().add(beneficiaryBarcodeDetailsModel);
                        }
                    }
                    if (!InputUtils.isNull(beneficaryWiseArylst.get(i).getTestsCode())) {
                        if (beneficaryWiseArylst.get(i).getTestsCode().equalsIgnoreCase("RBS,PPBS") ||
                                beneficaryWiseArylst.get(i).getTestsCode().equalsIgnoreCase("PPBS,RBS")) {
                            BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModelRBS = new BeneficiaryBarcodeDetailsModel();
                            beneficiaryBarcodeDetailsModelRBS.setBenId(beneficaryWiseArylst.get(i).getBenId());
                            beneficiaryBarcodeDetailsModelRBS.setId(DeviceUtils.getRandomUUID());
                            beneficiaryBarcodeDetailsModelRBS.setSamplType("FLUORIDE-R");
                            beneficiaryBarcodeDetailsModelRBS.setOrderNo(beneficaryWiseArylst.get(i).getOrderNo());
                            beneficiaryBarcodeDetailsModelRBS.setRBS_PPBS(true);
                            beneficaryWiseArylst.get(i).getBarcodedtl().add(beneficiaryBarcodeDetailsModelRBS);
                        } else if (beneficaryWiseArylst.get(i).getTestsCode().contains("RBS,PPBS") || beneficaryWiseArylst.get(i).getTestsCode().contains("PPBS,RBS")) {
                            if (beneficaryWiseArylst.get(i).getFasting().trim().equalsIgnoreCase("NON-FASTING")) {
                                BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModelRBS = new BeneficiaryBarcodeDetailsModel();
                                beneficiaryBarcodeDetailsModelRBS.setBenId(beneficaryWiseArylst.get(i).getBenId());
                                beneficiaryBarcodeDetailsModelRBS.setId(DeviceUtils.getRandomUUID());
                                beneficiaryBarcodeDetailsModelRBS.setSamplType("FLUORIDE-R");
                                beneficiaryBarcodeDetailsModelRBS.setOrderNo(beneficaryWiseArylst.get(i).getOrderNo());
                                beneficiaryBarcodeDetailsModelRBS.setRBS_PPBS(true);
                                beneficaryWiseArylst.get(i).getBarcodedtl().add(beneficiaryBarcodeDetailsModelRBS);
                            }
                        }
                    }
                }
            }

            for (int j = 0; j < beneficaryWiseArylst.size(); j++) {
                for (int i = 0; i < beneficaryWiseArylst.get(j).getBarcodedtl().size(); i++) {
                    if (beneficaryWiseArylst.get(j).getBarcodedtl().get(i).getSamplType().equalsIgnoreCase(BundleConstants.URINE)) {
                        beneficaryWiseArylst.get(j).getBarcodedtl().remove(i);
                        isRemovedUrine = true;
                    }
                }

                if (isRemovedUrine) {
                    isRemovedUrine = false;
                    BeneficiaryBarcodeDetailsModel beneficiaryBarcodeDetailsModel = new BeneficiaryBarcodeDetailsModel();
                    beneficiaryBarcodeDetailsModel.setSamplType(BundleConstants.URINE);
                    beneficaryWiseArylst.get(j).getBarcodedtl().add(beneficiaryBarcodeDetailsModel);
                }
            }

            InitViewpager(0);
        }
    }

    private void initListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity).create();
                alertDialog.setMessage("All changes made will be reset.\nAre you sure, you want to go back ?");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onbackpress();
                                //finish();
                            }
                        });
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeScanviewpager.setCurrentItem(page - 1);
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateBenCode())
                    BarcodeScanviewpager.setCurrentItem(page + 1);
            }
        });

        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateAllBarcodeScan() && validateAllBenCode()) {
                    new LogUserActivityTagging(mActivity, BundleConstants.WOE, "Scan Barcode");
                    Intent intent = new Intent(mActivity, CheckoutWoeActivity.class);
                    intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
                    intent.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficaryWiseArylst);
                    mActivity.startActivity(intent);
                }
            }
        });

        spn_ben.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BarcodeScanviewpager.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validateAllBenCode() {
        try {
            if (!beneficaryWiseArylst.isEmpty()) {
                for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                    for (int j = 0; j < beneficaryWiseArylst.get(i).getBarcodedtl().size(); j++) {
                        String currentBarcode = beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getBarcode();
                        String currentSampleType = beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getSamplType();
                        String currentBenCode = beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getBenCode();
                        String currentBenName = beneficaryWiseArylst.get(i).getName();
                        if (InputUtils.isNull(currentSampleType) || InputUtils.isNull(currentBenCode) || InputUtils.isNull(currentBarcode)) {
                            beneficaryWiseArylst.get(i).getBarcodedtl().get(j).setIsBenCodeCorrect(false);
                            InitViewpager(i);
                            return false;
                        } else if (currentBarcode.length() < 4) {
                            beneficaryWiseArylst.get(i).getBarcodedtl().get(j).setIsBenCodeCorrect(false);
                            InitViewpager(i);
                            return false;
                        } else if (!currentBenCode.startsWith(currentBenName.substring(0, 2))
                                || !currentBenCode.endsWith(currentBarcode.substring(currentBarcode.length() - 2))) {
                            beneficaryWiseArylst.get(i).getBarcodedtl().get(j).setIsBenCodeCorrect(false);
                            InitViewpager(i);
                            return false;
                        } else {
                            mAdapter.updateScanData(beneficaryWiseArylst, fileName, filepath);
                            mAdapter.notifyDataSetChanged();
                            beneficaryWiseArylst.get(i).getBarcodedtl().get(j).setIsBenCodeCorrect(true);
                        }
                    }
                }
                return true;
            }

        } catch (Exception e) {
            Global.sout("validate all bencode exception>>>>>>> ", e.getLocalizedMessage());
        }
        return false;
    }

    private boolean validateBenCode() {
        if (!beneficaryWiseArylst.isEmpty()) {

            if (!validateNullCheck()) {
                String currentBenName = beneficaryWiseArylst.get(BarcodepositionToScan).getName().replace(" ", "");
                for (int i = 0; i < beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().size(); i++) {
                    String currentBarcode = beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).getBarcode();
                    String currentSampleType = beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).getSamplType();
                    String currentBenCode = beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).getBenCode();
                    if (InputUtils.isNull(currentBenCode)) {
                        beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).setIsBenCodeCorrect(false);
                        Toast.makeText(mActivity, "Please enter bencode of sample type " + currentSampleType, Toast.LENGTH_SHORT).show();
                        InitViewpager(BarcodepositionToScan);
                        return false;

                    } else if ((InputUtils.isNull(currentBarcode))) {
                        Toast.makeText(mActivity, "Please scan barcode for " + currentBenName + "of sample type " + currentSampleType, Toast.LENGTH_SHORT).show();
                        InitViewpager(BarcodepositionToScan);
                        return false;
                    } else if (currentBarcode.length() < 4) {
                        beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).setIsBenCodeCorrect(false);
                        InitViewpager(BarcodepositionToScan);
                        return false;
                    } else if (!currentBenCode.startsWith(currentBenName.substring(0, 2))
                            || !currentBenCode.endsWith(currentBarcode.substring(currentBarcode.length() - 2))) {
                        beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(BarcodepositionToScan).setIsBenCodeCorrect(false);
                        InitViewpager(BarcodepositionToScan);
                        return false;
                    } else {
                        mAdapter.updateScanData(beneficaryWiseArylst, fileName, filepath);
                        mAdapter.notifyDataSetChanged();
                        beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).setIsBenCodeCorrect(true);
                    }
                }

            } else
                return true;
        }
        return false;
    }

    private boolean validateNullCheck() {
        for (int i = 0; i < beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().size(); i++) {
            String currentBarcode = beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).getBarcode();
            String currentBenCode = beneficaryWiseArylst.get(BarcodepositionToScan).getBarcodedtl().get(i).getBenCode();
            if (!InputUtils.isNull(currentBenCode) || !InputUtils.isNull(currentBarcode) ) {
                return false;
            }
        }
        return true;
    }

    private void onbackpress() {
        BundleConstants.setRefreshStartArriveActivity = 1;
        Intent intent = new Intent(mActivity, StartAndArriveActivity.class);
        intent.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean ValidateAllBarcodeScan() {
        boolean isVenupuntureCapturedForAllBen = true, isBarcodeScanforAllben = true;
        int VenupuntureScanRemainingPosition = 0, BarcodeScanRemainingbenPosition = 0;
        String barcode = "", confirmBarcode = "";
        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() > 0) {
            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (StringUtils.isNull(beneficaryWiseArylst.get(i).getVenepuncture())) {
                    isVenupuntureCapturedForAllBen = false;
                    VenupuntureScanRemainingPosition = i;
                    break;
                }
            }

            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (beneficaryWiseArylst.get(i).getBarcodedtl() != null && beneficaryWiseArylst.get(i).getBarcodedtl().size() > 0) {
                    for (int j = 0; j < beneficaryWiseArylst.get(i).getBarcodedtl().size(); j++) {
                        if (StringUtils.isNull(beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getBarcode())) {
                            isBarcodeScanforAllben = false;
                            BarcodeScanRemainingbenPosition = i;
                        } else {
                            barcode = "" + beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getBarcode();
                        }

                        if (StringUtils.isNull(beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getRescanBarcode())) {
                            isBarcodeScanforAllben = false;
                            BarcodeScanRemainingbenPosition = i;
                        } else {
                            confirmBarcode = "" + beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getRescanBarcode();
                        }

                        if (!barcode.equalsIgnoreCase(confirmBarcode)) {
                            Global.showCustomStaticToast(mActivity, "Barcode mismatched.");
                            return false;
                        }
                    }
                }
            }

            if (!isBarcodeScanforAllben && !isVenupuntureCapturedForAllBen && BarcodeScanRemainingbenPosition == VenupuntureScanRemainingPosition) {
                String name = beneficaryWiseArylst.get(BarcodeScanRemainingbenPosition).getName();
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                final int finalBarcodeScanbenPosition = BarcodeScanRemainingbenPosition;
                alertDialogBuilder
                        .setMessage("Please scan all barcode and also upload barcode Vial photo for " + name + " to proceed.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                BarcodeScanviewpager.setCurrentItem(finalBarcodeScanbenPosition);
                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            } else if (!isBarcodeScanforAllben) {
                String name = beneficaryWiseArylst.get(BarcodeScanRemainingbenPosition).getName();
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                final int finalBarcodeScanbenPosition = BarcodeScanRemainingbenPosition;
                alertDialogBuilder
                        .setMessage("Please scan all barcodes for " + name + " to proceed.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                BarcodeScanviewpager.setCurrentItem(finalBarcodeScanbenPosition);
                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            } else if (!isVenupuntureCapturedForAllBen) {
                String name = beneficaryWiseArylst.get(VenupuntureScanRemainingPosition).getName();
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                final int finalVenupuntureScanRemainingPosition = VenupuntureScanRemainingPosition;
                alertDialogBuilder
                        .setMessage("Please upload " + name + "'s Barcode vial photo to proceed.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                BarcodeScanviewpager.setCurrentItem(finalVenupuntureScanRemainingPosition);
                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            } else if (ValidateSRFID(beneficaryWiseArylst)) {  //NK added
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                alertDialogBuilder
                        .setMessage("Please enter and save SRF ID for Covid tests to proceed.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;  //NK added
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean ValidateSRFID(ArrayList<BeneficiaryDetailsModel> beneficaryWiseArylst) {
        if (beneficaryWiseArylst != null) {
            if (beneficaryWiseArylst.size() != 0) {
                for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                    if (beneficaryWiseArylst.get(i).getTestsCode() != null) {
//                        if (CommonUtils.ValidateCovidorders(beneficaryWiseArylst.get(i).getTestsCode())) {
                        if (beneficaryWiseArylst.get(i).isCovidOrder()) {
                            if (beneficaryWiseArylst.get(i).getSRFID() != null) {
                                if (beneficaryWiseArylst.get(i).getSRFID().toString().trim().length() > 4) {

                                } else {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void InitViewpager(int Currentposition) {


        if (mAdapter != null) {
            mAdapter.updateScanData(beneficaryWiseArylst, fileName, filepath);
            mAdapter.notifyDataSetChanged();
        } else {
            boolean showProduct = orderVisitDetailsModel.getAllOrderdetails().get(0).isDisplayProduct();
            boolean isHCL = orderVisitDetailsModel.getAllOrderdetails().get(0).isISHclOrder();
            boolean isOTP = orderVisitDetailsModel.getAllOrderdetails().get(0).isOTP();
            mAdapter = new ScanBarcodeViewPagerAdapter(mActivity, beneficaryWiseArylst, showProduct, orderVisitDetailsModel, orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile(), isHCL, fileName, isOTP);
            BarcodeScanviewpager.setAdapter(mAdapter);
            DisplayDotsBelowViewpager();
        }

        BarcodeScanviewpager.setCurrentItem(Currentposition, true);
        mAdapter.setOnItemClickListener(new ScanBarcodeViewPagerAdapter.OnClickListeners() {
            @Override
            public void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition) {
                SampleTypeToScan = SampleType;
                BenIDToScan = BenID;
                BarcodepositionToScan = barcodePosition;
                i = BenPosition;
                isRescan = false;
                if (BuildConfig.DEBUG) {
//                       OpenBarcodeConfirnationDialog(DeviceUtils.randomBarcodeString(8)); // Testing in simulator
                    EnterBarocodeManually();
//                   OpenScanBarcodeScreen();
                } else {
                    OpenScanBarcodeScreen();
                }
            }

            @Override
            public void onBarcodeScanClickedConfirm(String SampleType, int BenID, int barcodePosition, int BenPosition, String strbenCode) {
                SampleTypeToScan = SampleType;
                BenIDToScan = BenID;
                BarcodepositionToScan = barcodePosition;
                i = BenPosition;
                EnteredbenCode = strbenCode;

                for (int i = 0; i < beneficaryWiseArylst.get(Currentposition).getSampleType().size(); i++) {
                    String sampleTypeToCheck = beneficaryWiseArylst.get(Currentposition).getSampleType().get(i).getSampleType();
                    if (InputUtils.CheckEqualIgnoreCase(sampleTypeToCheck, SampleTypeToScan)) {
                        beneficaryWiseArylst.get(Currentposition).getBarcodedtl().get(i).setBenCode(EnteredbenCode);
                    }
                }
                isRescan = false; //TODO setting this to false due multipatient vial scan swap task


                if (BuildConfig.DEBUG) {
                    //  OpenBarcodeConfirnationDialog(DeviceUtils.randomBarcodeString(8)); // Testing in simulator
                    EnterBarocodeManually();
//                   OpenScanBarcodeScreen();
                } else {
                    OpenScanBarcodeScreen();
                }
            }

            @Override
            public void onVenupunturePhotoClicked(int BenID, int position) {
                BenIDToCaptureVenuPhoto = BenID;
                PositionToStoreVenuPhoto = position;
                CaptureBeneficicaryBarcodePic();
            }

            @Override
            public void onAffidavitClicked(int BenID, int position) {
                BenIDToUploadAffidavit = BenID;
                PositionToStoreVenuPhoto = position;
                CaptureAffidavitDetails();
            }

            @Override
            public void onRefresh() {

            }

            @Override
            public void onBarcodeDelete(String barcode, int BenPosition, String flag) {
                BenPositionForDelete = BenPosition;
                OpenBarcodeDeleteDiaog(barcode, flag);
            }

            @Override
            public void onVialImageDelete(int BenID, int position) {
                BenIDToDeleteVenuPhoto = BenID;
                PositionToDeleteVenuPhoto = position;
                deleteVialImage();
            }

            @Override
            public void onAffidavitDelete(int BenID, int position) {
                BenIDToDeleteVenuPhoto = BenID;
                PositionToDeleteVenuPhoto = position;
                deleteAffidavit();
            }

            @Override
            public void onViewTestDetailsClicked(String benId) {
                ViewTestData(benId);
            }

            @Override
            public void onSRFSaved(String srfId, int BenPosition, int BenID) {

                for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                    if (i == BenPosition && beneficaryWiseArylst.get(i).getBenId() == BenID) {
                        beneficaryWiseArylst.get(i).setSRFID(srfId);
                    }
                }
                InitViewpager(BenPosition);
            }

            @Override
            public void onSRFDeleted(String SRFID, int BenPosition) {

                for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                    if (i == BenPosition && InputUtils.CheckEqualIgnoreCase(beneficaryWiseArylst.get(i).getSRFID(), SRFID)) {
                        beneficaryWiseArylst.get(i).setSRFID("");
                    }
                }
                InitViewpager(BenPosition);
            }

            @Override
            public void onImageShow(String venepuncture, int benId, int position, int flag) {
                BottomSheetController bottomSheetController = new BottomSheetController(mActivity, ScanBarcodeWoeActivity.this, venepuncture, false, benId, position, flag);
                bottomSheetController.SetBottomSheet(mActivity);
            }

            @Override
            public void getBenCode(int samplePos, int benposition, String benCode) {
                beneficaryWiseArylst.get(benposition).getBarcodedtl().get(samplePos).setBenCode(benCode);
            }
        });


        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() == 1) {
            btn_Next.setVisibility(View.GONE);
            btn_Proceed.setVisibility(View.VISIBLE);
        }
        if (BarcodeScanviewpager.getCurrentItem() == 0) {
            btn_Previous.setVisibility(View.INVISIBLE);
        } else {
            btn_Previous.setVisibility(View.VISIBLE);
        }
    }

    private void deleteAffidavit() {
        String imageurl = "";
        if (filepath != null) {
            imageurl = filepath;
        } else if (fileName != null) {
            imageurl = fileName;
        }

//        imageurl = camera.getCameraBitmapPath();
        File BenBarcodePicimagefile = new File(imageurl);
        if (BenBarcodePicimagefile != null) {
            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (beneficaryWiseArylst.get(i).getBenId() == BenIDToDeleteVenuPhoto && i == PositionToDeleteVenuPhoto) {
                    fileName = "";
                    filepath = "";
                }
            }
            InitViewpager(PositionToStoreVenuPhoto);
        } else {
            Toast.makeText(mActivity, "Failed to delete Affidavit", Toast.LENGTH_SHORT).show();
        }

    }

    private void CaptureAffidavitDetails() {

        try {
            final CharSequence[] items = {"Take Photo", "Gallery", "Cancel"};
            final String[] userChoosenTask = {""};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
            builder.setTitle("Upload Affidavit!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Take Photo")) {
                        userChoosenTask[0] = "Take Photo";
                        TedPermission.with(mActivity)
                                .setPermissions(Manifest.permission.CAMERA)
                                .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        ImageFlag = 1;
                                        globalclass.cropImageFullScreenActivity(mActivity, 0);
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).check();

                    } else if (items[item].equals("Gallery")) {
                        userChoosenTask[0] = "Gallery";
                        TedPermission.with(mActivity)
                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        showGallery();
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).check();

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowImagepickerDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Select")
                .setCancelable(true)
                .setMessage("Choose any one ")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showGallery();
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        globalclass.cropImageFullScreenActivity(mActivity, 0);
                    }
                })
                .show();
    }

    private void showGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), Select_PDFFILE);
    }

    private void EnterBarocodeManually() {
        globalclass.EnterBarcodeManually(mActivity, new Global.OnBarcodeDialogSubmitClickListener() {
            @Override
            public void onSubmitButtonClicked(String barcode) {
                OpenBarcodeConfirnationDialog(barcode);
            }
        });
    }

    private void DisplayDotsBelowViewpager() {
        BarcodeScanviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (validateBenCode()) {
                    if (BarcodeScanviewpager.getAdapter().getCount() > 1) {
                        for (int i = 0; i < dotsCount; i++) {
                            dots[i].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                        dots[position].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.selecteditem_dot));
                        page = position;
                        BarcodepositionToScan = position;
                    }

                    spn_ben.setSelection(position);

                    i = position;

                    if (position == 0) {
                        btn_Previous.setVisibility(View.INVISIBLE);
                    } else {
                        btn_Previous.setVisibility(View.VISIBLE);
                    }
                    if (position == beneficaryWiseArylst.size() - 1) {
                        btn_Next.setVisibility(View.GONE);
                        btn_Proceed.setVisibility(View.VISIBLE);
                    } else {
                        btn_Next.setVisibility(View.VISIBLE);
                        btn_Proceed.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUiPageViewController(viewPagerCountDots);

    }

    private void setUiPageViewController(LinearLayout viewPagerCountDots) {

        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() > 0) {
            dotsCount = beneficaryWiseArylst.size();
        }
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(mActivity);
            dots[i].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.selecteditem_dot));

       /* if (beneficaryWiseArylst.size() > 1){
            viewPagerCountDots.setVisibility(View.VISIBLE);
        }else{
            viewPagerCountDots.setVisibility(View.INVISIBLE);
        }*/
        viewPagerCountDots.setVisibility(View.INVISIBLE);
    }

    private void CaptureBeneficicaryBarcodePic() {
        TedPermission.with(mActivity)
                .setPermissions(Manifest.permission.CAMERA)
                .setRationaleMessage("We need permission to capture photo from your camera to Upload TRF.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Camera")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        globalclass.cropImageFullScreenActivity(mActivity, 0);
//                        selectImage();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(mActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void selectImage() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("BtechApp/BenBarcodePics")
                .setName(orderVisitDetailsModel.getVisitId() + "_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(40)
                .setImageHeight(480)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void OpenScanBarcodeScreen() {
        intentIntegrator = null;
        intentIntegrator = new IntentIntegrator(mActivity) {
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                ScanBarcodeWoeActivity.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN);
            }
        };
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == BundleConstants.START_BARCODE_SCAN) {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                    final String scanned_barcode = scanningResult.getContents().trim();
                    if (!InputUtils.isNull(scanned_barcode)) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                OpenBarcodeConfirnationDialog(scanned_barcode);
                            }
                        }, 500);
                    } else {
                        Toast.makeText(mActivity, "Retry scanning barcode", Toast.LENGTH_SHORT).show();
                    }

                }
            } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
                String imageurl = "";
                VenupunctureUploadRequestModel vrm = null;
                try {
                    imageurl = ImagePicker.Companion.getFile(data).toString();
//                    imageurl = camera.getCameraBitmapPath();
                    BundleConstants.VenepunctureImage_path = imageurl;
                    BenBarcodePicimagefile = new File(BundleConstants.VenepunctureImage_path);
//                    BenBarcodePicimagefile = new File(imageurl);

                    if (BenBarcodePicimagefile.exists()) {
                        boolean ImageUpdated = false;
                        if (ImageFlag == 1) {
                            fileName = BenBarcodePicimagefile.getName().toString();
                            filepath = BenBarcodePicimagefile.getAbsoluteFile().toString();
                            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                                if (beneficaryWiseArylst.get(i).getBenId() == BenIDToUploadAffidavit && i == PositionToStoreVenuPhoto) {
//                                        beneficaryWiseArylst.get(PositionToStoreVenuPhoto).setVenepuncture(BenBarcodePicimagefile.getAbsolutePath());
                                    ImageUpdated = true;
                                }
                                //TODO Venupucture Image API
                                vrm = getVenupunctureUploadRequestModel(i, "AFFEDEBIT", BenBarcodePicimagefile);
                            }
                            ImageFlag = 0;
                        } else {
                            fileName = BenBarcodePicimagefile.getAbsoluteFile().toString();
                            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                                if (beneficaryWiseArylst.get(i).getBenId() == BenIDToCaptureVenuPhoto && i == PositionToStoreVenuPhoto) {
                                    beneficaryWiseArylst.get(PositionToStoreVenuPhoto).setVenepuncture(BundleConstants.VenepunctureImage_path);
//                                    beneficaryWiseArylst.get(PositionToStoreVenuPhoto).setVenepuncture(BenBarcodePicimagefile.getAbsolutePath());
                                    ImageUpdated = true;
                                    vrm = getVenupunctureUploadRequestModel(i, "VIAL", BenBarcodePicimagefile);
                                    break;
                                }
                                //TODO Venupucture Image API

                            }
                        }
                        callVenupuctureAPI(vrm);
                        if (ImageUpdated) {
                            InitViewpager(PositionToStoreVenuPhoto);
                        }
                    } else {
                        Toast.makeText(mActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Select_PDFFILE) {
                try {
                    Uri path;
                    if (data != null && data.getData() != null) {
                        path = data.getData();
                        if (path.toString().contains(".pdf")) {
                            String pt = FilePath.getPath(mActivity, path);
                            if (pt == null) {
                                Toast.makeText(mActivity, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_SHORT).show();
                            } else {
                                VenupunctureUploadRequestModel vrm = null;
                                Affidavitfile = new File(FilePath.getPath(mActivity, path));
                                fileName = Affidavitfile.getName().toString();
                                if (Affidavitfile.exists()) {
                                    boolean ImageUpdated = false;
                                    for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                                        if (beneficaryWiseArylst.get(i).getBenId() == BenIDToUploadAffidavit && i == PositionToStoreVenuPhoto) {
//                                        beneficaryWiseArylst.get(PositionToStoreVenuPhoto).setVenepuncture(Affidavitfile.getAbsolutePath());
                                            ImageUpdated = true;
                                        }
                                        //TODO Venupucture Image API
                                        vrm = getVenupunctureUploadRequestModel(i, "AFFEDEBIT", Affidavitfile);
                                    }
                                    callVenupuctureAPI(vrm);
                                    if (ImageUpdated) {
                                        InitViewpager(PositionToStoreVenuPhoto);
                                    }
                                } else {
                                    Toast.makeText(mActivity, "Failed to Upload data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(mActivity, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(mActivity, "Something wrong with file", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private VenupunctureUploadRequestModel getVenupunctureUploadRequestModel(int i, String affedebit, File benBarcodePicimagefile) {
        VenupunctureUploadRequestModel vrm;
        vrm = new VenupunctureUploadRequestModel();
        vrm.setBENID(String.valueOf(beneficaryWiseArylst.get(i).getBenId()));
        vrm.setORDERNO(beneficaryWiseArylst.get(i).getOrderNo());
        vrm.setTEST(beneficaryWiseArylst.get(i).getTests());
        vrm.setTYPE(affedebit);
        vrm.setAPPID(appPreferenceManager.getLoginResponseModel().getBrandId());
        vrm.setFile(benBarcodePicimagefile);
        return vrm;
    }

    private void callVenupuctureAPI(VenupunctureUploadRequestModel vrm) {
        VenupunctureAPIController vpc = new VenupunctureAPIController(this);
        vpc.CallAPI(vrm);
    }


    private void deleteVialImage() {
        String imageurl = "";
        String imgurl = "";

        for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
            if (beneficaryWiseArylst.get(i).getVenepuncture() != null) {
                if (beneficaryWiseArylst.get(i).getBenId() == BenIDToDeleteVenuPhoto && i == PositionToDeleteVenuPhoto) {
                    imgurl = beneficaryWiseArylst.get(PositionToDeleteVenuPhoto).getVenepuncture();
                    break;
                }
            } else {
                imgurl = "";
            }
        }

        imageurl = imgurl;
//        imageurl = camera.getCameraBitmapPath();
        File BenBarcodePicimagefile = new File(imageurl);
        if (BenBarcodePicimagefile.exists()) {
            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (beneficaryWiseArylst.get(i).getBenId() == BenIDToDeleteVenuPhoto && i == PositionToDeleteVenuPhoto) {
                    beneficaryWiseArylst.get(PositionToDeleteVenuPhoto).setVenepuncture("");
                }
            }
            InitViewpager(PositionToStoreVenuPhoto);
        } else {
            Toast.makeText(mActivity, "Failed to delete photo", Toast.LENGTH_SHORT).show();
        }


    }

    private void OpenBarcodeDeleteDiaog(final String scannedBarcode, final String flag) {
        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setTitle("Check the Barcode ")
                    .setMessage("Do you want to delete this barcode entry " + scannedBarcode + "?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MessageLogger.LogError(TAG, "onClick: " + scannedBarcode);
                            for (int i = 0; i < beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().size(); i++) {
                                if (flag.equalsIgnoreCase("yes")) {
                                    if (!InputUtils.isNull(beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getBarcode()) && beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getBarcode().equals(scannedBarcode)) {
                                        beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).setBarcode("");
                                        break;
                                    }
                                } else {
                                    if (!InputUtils.isNull(beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getRescanBarcode()) && beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getRescanBarcode().equals(scannedBarcode)) {
                                        beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).setRescanBarcode("");
                                        break;
                                    }
                                }

                            }
                            InitViewpager(i);
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenBarcodeConfirnationDialog(final String scanned_barcode) {
        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setTitle("Check the Barcode ")
                    .setMessage("Do you want to proceed with this barcode entry " + scanned_barcode + "?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MessageLogger.LogError(TAG, "onClick: " + scanned_barcode);

                            if (TextUtils.isEmpty(scanned_barcode) || scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$") || scanned_barcode.startsWith("1") || scanned_barcode.startsWith(" ") /*|| Character.isDigit(scanned_barcode.charAt(0))*/) {
                                Toast.makeText(mActivity, "Invalid barcode", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                                    if (beneficaryWiseArylst.get(i).getBarcodedtl() != null) {
                                        for (int i = 0; i < beneficaryWiseArylst.get(i).getBarcodedtl().size(); i++) {
                                            //size 4
                                            if (!InputUtils.isNull(beneficaryWiseArylst.get(i).getBarcodedtl().get(i).getSamplType())
                                                    && !InputUtils.isNull(SampleTypeToScan)
                                                    && SampleTypeToScan.equals(beneficaryWiseArylst.get(i).getBarcodedtl().get(i).getSamplType())
                                                    && BarcodepositionToScan == i) {

                                                for (BeneficiaryDetailsModel bdm : beneficaryWiseArylst) {
                                                    if (bdm.getBarcodedtl() != null && bdm.getBarcodedtl().size() > 0) {
                                                        for (BeneficiaryBarcodeDetailsModel bbdm : bdm.getBarcodedtl()) {
                                                            if (!isRescan) {
                                                                if (!InputUtils.isNull(bbdm.getBarcode()) && bbdm.getBarcode().equals(scanned_barcode)) {
                                                                    Toast.makeText(mActivity, "Same barcode already scanned for " + bdm.getName() + " - " + bbdm.getSamplType(), Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (isRescan) {
                                                    beneficaryWiseArylst.get(i).getBarcodedtl().get(i).setRescanBarcode(scanned_barcode);
                                                } else {
                                                    beneficaryWiseArylst.get(i).getBarcodedtl().get(i).setRescanBarcode(scanned_barcode);
                                                    beneficaryWiseArylst.get(i).getBarcodedtl().get(i).setBarcode(scanned_barcode);

                                                }
                                                beneficaryWiseArylst.get(i).getBarcodedtl().get(i).setBenId(BenIDToScan);

                                                break;
                                            }
                                        }
                                        InitViewpager(i);
                                    } else {
                                        Toast.makeText(mActivity, "Failed to update scanned barcode value", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mActivity, "Invalid barcode", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        if (Constants.Finish_barcodeScanAcitivty) {
            Constants.Finish_barcodeScanAcitivty = false;
            finish();
        }
        super.onResume();
    }

    private void ViewTestData(String benId) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<GetTestListResponseModel> responseCall = apiInterface.CallGetTestDetailsAPI(benId);
        globalclass.showProgressDialog(mActivity, "Please wait..", false);
        responseCall.enqueue(new Callback<GetTestListResponseModel>() {
            @Override
            public void onResponse(Call<GetTestListResponseModel> call, retrofit2.Response<GetTestListResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    GetTestListResponseModel TestListResponseModel = response.body();
                    if (TestListResponseModel.getTestGroupList() != null && TestListResponseModel.getTestGroupList().size() > 0) {
                        DisplayTestListDialog(TestListResponseModel);
                    } else {
                        Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTestListResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void DisplayTestListDialog(GetTestListResponseModel testListResponseModel) {
        /*CustomDialogClass cdd = new CustomDialogClass(mActivity, testListResponseModel);
        cdd.show();*/

        if (testListResponseModel != null) {
            BottomSheetController bottomSheetController = new BottomSheetController(mActivity, ScanBarcodeWoeActivity.this);
            bottomSheetController.setTestListBottomSheet(mActivity, testListResponseModel);
        }
    }

    public void getbottomsheetresponse(BottomSheetDialog bottomSheetDialog) {
        bottomSheetDialog.dismiss();
    }

    public void vialImageDelete(int benId, int position, BottomSheetDialog bottomSheetDialog, int imageFlag) {
        BenIDToDeleteVenuPhoto = benId;
        PositionToDeleteVenuPhoto = position;
        DelFlag = imageFlag;
        if (DelFlag == 0) {
            deleteVialImage();
        } else {
            deleteAffidavit();
        }
        bottomSheetDialog.dismiss();

    }

    public void iflateTestGroupName(LinearLayout ll_tests, GetTestListResponseModel testListResponseModel) {
        if (testListResponseModel.getTestGroupList().size() > 0) {
            // Logger.error("if ");
            ll_tests.removeAllViews();
            for (int i = 0; i < testListResponseModel.getTestGroupList().size(); i++) {
                LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.item_view_test, null);
                final TextView tv_test = (TextView) v.findViewById(R.id.tv_test);
                final LinearLayout ll_child = (LinearLayout) v.findViewById(R.id.ll_child);
                tv_test.setText("" + testListResponseModel.getTestGroupList().get(i).getGroupName() + " (" + testListResponseModel.getTestGroupList().get(i).getTestCount() + ")");
                tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);

                tv_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ll_child.getVisibility() == View.VISIBLE) {
                            ll_child.setVisibility(View.GONE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_collapse_15, 0);
                        } else {
                            ll_child.setVisibility(View.VISIBLE);
                            tv_test.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_group_expand_15, 0);

                        }
                    }
                });
                ll_child.removeAllViews();
                for (int j = 0; j < testListResponseModel.getTestGroupList().get(i).getTestDetails().size(); j++) {
                    LayoutInflater vj = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v1 = vj.inflate(R.layout.item_view_test1, null);
                    TextView tv_test1 = (TextView) v1.findViewById(R.id.tv_test);
                    tv_test1.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv_test1.setTextColor(Color.parseColor("#000000"));
                    tv_test1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    tv_test1.setText(">> Description:  " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getDescription() + " \n     Test Code: " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getTestCode() + "\n     Unit: " + testListResponseModel.getTestGroupList().get(i).getTestDetails().get(j).getUnit());

                    ll_child.addView(v1);
                    ll_child.invalidate();
                }

                ll_tests.addView(v);
                ll_tests.invalidate();
            }


        }
    }

    private class CustomDialogClass extends Dialog {

        private Activity c;
        private Dialog d;
        private Button yes, no;
        private TextView tv_test;
        private LinearLayout ll_tests;
        private GetTestListResponseModel testListResponseModel;

        public CustomDialogClass(Activity mActivity, GetTestListResponseModel testListResponseModel) {
            super(mActivity);
            this.c = mActivity;
            this.testListResponseModel = testListResponseModel;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.item_test_list_display);
            ll_tests = (LinearLayout) findViewById(R.id.ll_tests);
            iflateTestGroupName(ll_tests, testListResponseModel);
            ImageView img_close = (ImageView) findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }
}