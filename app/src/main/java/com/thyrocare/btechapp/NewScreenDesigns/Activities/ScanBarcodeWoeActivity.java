package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.ScanBarcodeViewPagerAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.NO_DATA_FOUND;

public class ScanBarcodeWoeActivity extends AppCompatActivity {

    private static  String TAG = ScanBarcodeWoeActivity.class.getSimpleName();
    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    private ViewPager BarcodeScanviewpager;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    ArrayList<BeneficiaryDetailsModel> beneficaryWiseArylst;
    private IntentIntegrator intentIntegrator;
    private int page = 0;
    private int dotsCount;
    Runnable runnable;
    private ImageView[] dots;
    private RelativeLayout viewPagerIndicator;
    private LinearLayout viewPagerCountDots;
    private ScanBarcodeViewPagerAdapter mAdapter;
    private String SampleTypeToScan = "";
    private int BenIDToScan = 0, BarcodepositionToScan = 0;
    private int BenPositionForScan = 0, BenPositionForDelete = 0;
    private Camera camera;
    private int BenIDToCaptureVenuPhoto = 0 ,PositionToStoreVenuPhoto = 0, BenIDToDeleteVenuPhoto = 0, PositionToDeleteVenuPhoto = 0;
    private Button btn_Next,btn_Previous,btn_Proceed;
    private Spinner spn_ben;

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(mActivity).create();
        alertDialog.setMessage("All changes made will be reset.\nAre you sure, you want to go back ?");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
        setTitle("Barcode Scanning");
        mActivity = ScanBarcodeWoeActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);

        initView();
        initToolBar();
        initData();
        initListener();
    }

    private void initToolBar() {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initView() {
        BarcodeScanviewpager = (ViewPager) findViewById(R.id.BarcodeScanviewpager);
        viewPagerIndicator = (RelativeLayout) findViewById(R.id.viewPagerIndicator);
        viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        spn_ben = (Spinner) findViewById(R.id.spn_ben);
        btn_Previous = (Button) findViewById(R.id.btn_Previous);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        btn_Proceed = (Button) findViewById(R.id.btn_Proceed);
    }

    private void initData() {
        if (orderVisitDetailsModel != null
                && orderVisitDetailsModel.getAllOrderdetails() != null
                && orderVisitDetailsModel.getAllOrderdetails().size() > 0
                && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster() != null
                && orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size() > 0){

            beneficaryWiseArylst = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster();

            ArrayAdapter<BeneficiaryDetailsModel> spinnerArrayAdapter = new ArrayAdapter<BeneficiaryDetailsModel>(mActivity, android.R.layout.simple_spinner_item, beneficaryWiseArylst); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_ben.setAdapter(spinnerArrayAdapter);



            for (int i = 0; i <beneficaryWiseArylst.size() ; i++) {
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

                    if (beneficaryWiseArylst.get(i).getTestsCode().equalsIgnoreCase("RBS,PPBS") || beneficaryWiseArylst.get(i).getTestsCode().equalsIgnoreCase("PPBS,RBS") ) {
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
            InitViewpager(0);
        }
    }

    private void initListener() {
        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeScanviewpager.setCurrentItem(page-1);
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeScanviewpager.setCurrentItem(page+1);
            }
        });

        btn_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateAllBarcodeScan()){
                    Intent intent = new Intent(mActivity,CheckoutWoeActivity.class);
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

    private boolean ValidateAllBarcodeScan() {
        boolean isVenupuntureCapturedForAllBen = true, isBarcodeScanforAllben = true;
        int VenupuntureScanRemainingPosition = 0 ,BarcodeScanRemainingbenPosition = 0;

        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() > 0){
            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (StringUtils.isNull(beneficaryWiseArylst.get(i).getVenepuncture())){
                    isVenupuntureCapturedForAllBen = false;
                    VenupuntureScanRemainingPosition  = i;
                    break;
                }
            }

            for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                if (beneficaryWiseArylst.get(i).getBarcodedtl() != null && beneficaryWiseArylst.get(i).getBarcodedtl().size() > 0){
                    for (int j = 0; j < beneficaryWiseArylst.get(i).getBarcodedtl().size(); j++) {
                        if (StringUtils.isNull(beneficaryWiseArylst.get(i).getBarcodedtl().get(j).getBarcode())){
                            isBarcodeScanforAllben = false;
                            BarcodeScanRemainingbenPosition = i;
                        }
                    }
                }
            }

            if (!isBarcodeScanforAllben && !isVenupuntureCapturedForAllBen && BarcodeScanRemainingbenPosition == VenupuntureScanRemainingPosition){
                String name  = beneficaryWiseArylst.get(BarcodeScanRemainingbenPosition).getName();
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                final int finalBarcodeScanbenPosition = BarcodeScanRemainingbenPosition;
                alertDialogBuilder
                        .setMessage("Please scan all barcode and also upload barcode Vial photo for " +name + " to proceed.")
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
            }else if (!isBarcodeScanforAllben){
                String name  = beneficaryWiseArylst.get(BarcodeScanRemainingbenPosition).getName();
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
            }else if (!isVenupuntureCapturedForAllBen){
                String name  = beneficaryWiseArylst.get(VenupuntureScanRemainingPosition).getName();
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
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    private void InitViewpager(int Currentposition) {
        if (mAdapter != null){
            mAdapter.updateScanData(beneficaryWiseArylst);
            mAdapter.notifyDataSetChanged();
        }else{
            boolean showProduct = orderVisitDetailsModel.getAllOrderdetails().get(0).isDisplayProduct();
            mAdapter = new ScanBarcodeViewPagerAdapter(mActivity, beneficaryWiseArylst,showProduct);
            BarcodeScanviewpager.setAdapter(mAdapter);
            DisplayDotsBelowViewpager();
        }

        BarcodeScanviewpager.setCurrentItem(Currentposition);
        mAdapter.setOnItemClickListener(new ScanBarcodeViewPagerAdapter.OnClickListeners() {
           @Override
           public void onBarcodeScanClicked(String SampleType, int BenID, int barcodePosition, int BenPosition) {
               SampleTypeToScan = SampleType;
               BenIDToScan = BenID;
               BarcodepositionToScan = barcodePosition;
               BenPositionForScan = BenPosition;
               if (BuildConfig.DEBUG){
//                   OpenBarcodeConfirnationDialog(DeviceUtils.randomBarcodeString(8)); // Testing in simulator
//                   EnterBarocodeManually();
                   OpenScanBarcodeScreen();
               }else{
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
           public void onRefresh() {

           }

            @Override
            public void onBarcodeDelete(String barcode, int BenPosition) {
               BenPositionForDelete = BenPosition;
               OpenBarcodeDeleteDiaog(barcode);
            }

            @Override
            public void onVialImageDelete(int BenID, int position) {
                BenIDToDeleteVenuPhoto = BenID;
                PositionToDeleteVenuPhoto = position;
                deleteVialImage();
            }

            @Override
            public void onViewTestDetailsClicked(String benId) {
                ViewTestData(benId);
            }
        });


        if (beneficaryWiseArylst != null && beneficaryWiseArylst.size() == 1){
            btn_Next.setVisibility(View.GONE);
            btn_Proceed.setVisibility(View.VISIBLE);
        }
        if (BarcodeScanviewpager.getCurrentItem() == 0){
            btn_Previous.setVisibility(View.INVISIBLE);
        }else{
            btn_Previous.setVisibility(View.VISIBLE);
        }
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
                if (BarcodeScanviewpager.getAdapter().getCount() > 1){
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }
                    dots[position].setImageDrawable(mActivity.getResources().getDrawable(R.drawable.selecteditem_dot));
                    page = position;
                }

                spn_ben.setSelection(position);

                if (position == 0){
                    btn_Previous.setVisibility(View.INVISIBLE);
                }else{
                    btn_Previous.setVisibility(View.VISIBLE);
                }
                if (position == beneficaryWiseArylst.size() - 1){
                    btn_Next.setVisibility(View.GONE);
                    btn_Proceed.setVisibility(View.VISIBLE);
                }else{
                    btn_Next.setVisibility(View.VISIBLE);
                    btn_Proceed.setVisibility(View.GONE);
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
                        selectImage();
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
        }else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imageurl = "";
            try {
                imageurl = camera.getCameraBitmapPath();
                File BenBarcodePicimagefile = new File(imageurl);
                if (BenBarcodePicimagefile.exists()){
                    boolean ImageUpdated = false;
                    for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                        if (beneficaryWiseArylst.get(i).getBenId() == BenIDToCaptureVenuPhoto && i == PositionToStoreVenuPhoto ){
                            beneficaryWiseArylst.get(PositionToStoreVenuPhoto).setVenepuncture(BenBarcodePicimagefile.getAbsolutePath());
                            ImageUpdated  = true;
                        }
                    }
                    if (ImageUpdated){
                        InitViewpager(PositionToStoreVenuPhoto);
                    }
                }else{
                    Toast.makeText(mActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteVialImage() {
        String imageurl = "";
        try {
            imageurl = camera.getCameraBitmapPath();
            File BenBarcodePicimagefile = new File(imageurl);
            if (BenBarcodePicimagefile.exists()){
                for (int i = 0; i < beneficaryWiseArylst.size(); i++) {
                    if (beneficaryWiseArylst.get(i).getBenId() == BenIDToDeleteVenuPhoto && i == PositionToDeleteVenuPhoto ){
                        beneficaryWiseArylst.get(PositionToDeleteVenuPhoto).setVenepuncture("");
                    }
                }
                InitViewpager(PositionToStoreVenuPhoto);
            }else{
                Toast.makeText(mActivity, "Failed to delete photo", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenBarcodeDeleteDiaog(final String scannedBarcode) {
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
                        if (!InputUtils.isNull(beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getBarcode()) && beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).getBarcode().equals(scannedBarcode)) {
                            beneficaryWiseArylst.get(BenPositionForDelete).getBarcodedtl().get(i).setBarcode("");
                            break;
                        }
                    }
                    InitViewpager(BenPositionForScan);
                }
            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenBarcodeConfirnationDialog( final String scanned_barcode) {
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
                            if (beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl() != null) {
                                for (int i = 0; i < beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl().size(); i++) {
                                    //size 4
                                    if (!InputUtils.isNull(beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl().get(i).getSamplType())
                                            && !InputUtils.isNull(SampleTypeToScan)
                                            && SampleTypeToScan.equals(beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl().get(i).getSamplType())
                                            && BarcodepositionToScan == i) {

                                            for (BeneficiaryDetailsModel bdm : beneficaryWiseArylst) {
                                                if (bdm.getBarcodedtl() != null && bdm.getBarcodedtl().size() > 0) {
                                                    for (BeneficiaryBarcodeDetailsModel bbdm : bdm.getBarcodedtl()) {
                                                        if (!InputUtils.isNull(bbdm.getBarcode()) && bbdm.getBarcode().equals(scanned_barcode)) {
                                                            Toast.makeText(mActivity, "Same barcode already scanned for " + bdm.getName() + " - " + bbdm.getSamplType(), Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                            beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl().get(i).setBarcode(scanned_barcode);
                                            beneficaryWiseArylst.get(BenPositionForScan).getBarcodedtl().get(i).setBenId(BenIDToScan);
                                        break;
                                    }
                                }
                                InitViewpager(BenPositionForScan);
                            } else {
                                Toast.makeText(mActivity, "Failed to update scanned barcode value", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mActivity, "Failed to scan barcode", Toast.LENGTH_SHORT).show();
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
        if (Constants.Finish_barcodeScanAcitivty){
            Constants.Finish_barcodeScanAcitivty = false;
            finish();
        }
        super.onResume();
    }

    private void ViewTestData(String benId) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<GetTestListResponseModel> responseCall = apiInterface.CallGetTestDetailsAPI(benId);
        globalclass.showProgressDialog(mActivity,"Please wait..",false);
        responseCall.enqueue(new Callback<GetTestListResponseModel>() {
            @Override
            public void onResponse(Call<GetTestListResponseModel> call, retrofit2.Response<GetTestListResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    GetTestListResponseModel TestListResponseModel = response.body();
                    if (TestListResponseModel.getTestGroupList() != null && TestListResponseModel.getTestGroupList().size() > 0){
                        DisplayTestListDialog(TestListResponseModel);
                    }else{
                        Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetTestListResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
                Toast.makeText(mActivity, NO_DATA_FOUND, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void DisplayTestListDialog(GetTestListResponseModel testListResponseModel) {
        CustomDialogClass cdd = new CustomDialogClass(mActivity,testListResponseModel);
        cdd.show();
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
            iflateTestGroupName(ll_tests,testListResponseModel);
            ImageView img_close = (ImageView) findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }

    private void iflateTestGroupName(LinearLayout ll_tests, GetTestListResponseModel testListResponseModel) {
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
                    View v1 = vj.inflate(R.layout.item_view_test, null);
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
}
