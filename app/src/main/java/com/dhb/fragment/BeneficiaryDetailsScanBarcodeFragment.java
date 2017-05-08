package com.dhb.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.EditTestListActivity;
import com.dhb.activity.AddEditBeneficiaryDetailsActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.adapter.DisplayScanBarcodeItemListAdapter;
import com.dhb.delegate.ScanBarcodeIconClickedDelegate;
import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class BeneficiaryDetailsScanBarcodeFragment extends AbstractFragment{
    public static final String TAG_FRAGMENT = BeneficiaryDetailsScanBarcodeFragment.class.getSimpleName();
    private ImageView imgVenipuncture, imgHC;
    private TextView txtSrNo;
    private TextView txtName;
    private TextView txtAge;
    private TextView txtAadharNo;
    private ImageView btnRelease,btnEdit;
    private EditText edtTests;
    private LinearLayout llBarcodes;
    private EditText edtCH;
    private EditText edtSign;
    private BeneficiaryDetailsModel beneficiaryDetailsModel;
    private OrderBookingActivity activity;
    private String userChoosenTask, encodedVanipunctureImg;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private View rootview;
    private boolean isHC = false;
    private String userChoosenReleaseTask;
    private OrderDetailsModel orderDetailsModel;
    private ArrayList<BarcodeDetailsModel> barcodeDetailsModelsArr;
    private String currentScanSampleType;
    private DisplayScanBarcodeItemListAdapter displayScanBarcodeItemListAdapter;

    public BeneficiaryDetailsScanBarcodeFragment() {
        // Required empty public constructor
    }

    public static BeneficiaryDetailsScanBarcodeFragment newInstance(Bundle bundle) {
        BeneficiaryDetailsScanBarcodeFragment fragment = new BeneficiaryDetailsScanBarcodeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_beneficiary_details_scan_barcode, container, false);
        activity = (OrderBookingActivity) getActivity();
        Bundle bundle = getArguments();
        beneficiaryDetailsModel = bundle.getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
        orderDetailsModel = bundle.getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
        initUI();
        initData();
        setListeners();
        return rootview;
    }

    private void initData() {
        txtName.setText(beneficiaryDetailsModel.getName());
        txtAge.setText(beneficiaryDetailsModel.getAge()+" | "+beneficiaryDetailsModel.getGender());
        txtAadharNo.setVisibility(View.GONE);
        edtTests.setText(beneficiaryDetailsModel.getTests());
        txtSrNo.setText(beneficiaryDetailsModel.getBenId()+"");
        if(orderDetailsModel!=null && orderDetailsModel.getReportHC()==0){
            imgHC.setImageDrawable(getResources().getDrawable(R.drawable.tick_icon));
        }
        else{
            imgHC.setImageDrawable(getResources().getDrawable(R.drawable.check_mark));
        }
        if(beneficiaryDetailsModel!=null
                && beneficiaryDetailsModel.getBarcodedtl()!=null
                && beneficiaryDetailsModel.getSampleType()!=null
                && beneficiaryDetailsModel.getBarcodedtl().size() == beneficiaryDetailsModel.getSampleType().size()){
            barcodeDetailsModelsArr = beneficiaryDetailsModel.getBarcodedtl();
        }
        else{
            barcodeDetailsModelsArr = new ArrayList<>();
            for (BeneficiarySampleTypeDetailsModel sampleTypes :
                    beneficiaryDetailsModel.getSampleType()) {
                BarcodeDetailsModel barcodeDetailsModel = new BarcodeDetailsModel();
                barcodeDetailsModel.setBenId(sampleTypes.getBenId());
                barcodeDetailsModel.setId(DeviceUtils.getRandomUUID());
                barcodeDetailsModel.setSamplType(sampleTypes.getSampleType());
                barcodeDetailsModel.setOrderNo(beneficiaryDetailsModel.getOrderNo());
                barcodeDetailsModelsArr.add(barcodeDetailsModel);
            }
        }
        initScanBarcodeView();
    }

    private void setListeners() {
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPhoto();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsModel);
                intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL,orderDetailsModel);
                startActivityForResult(intentEdit,BundleConstants.ADD_EDIT_START);
            }
        });
        imgHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHC){
                    isHC = false;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.tick_icon));
                    orderDetailsModel.setReportHC(0);
                }else{
                    isHC = true;
                    imgHC.setImageDrawable(activity.getResources().getDrawable(R.drawable.check_mark));
                    orderDetailsModel.setReportHC(1);
                }
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Order Reschedule",
                        "Order Cancellation"};
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Action");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Order Reschedule")) {
                            userChoosenReleaseTask = "Order Reschedule";
                        } else if (items[item].equals("Order Cancellation")) {
                            userChoosenReleaseTask = "Order Cancellation";
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        edtTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tests = beneficiaryDetailsModel.getTests();
                final String[] testsList = tests.split(",");
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Tests List");
                builder.setItems(testsList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentEdit = new Intent(activity, EditTestListActivity.class);
                        intentEdit.putExtra(BundleConstants.BENEFICIARY_TEST_LIST,beneficiaryDetailsModel.getTests());
                        startActivityForResult(intentEdit,BundleConstants.EDIT_TESTS_START);
                    }
                });
                builder.show();
            }
        });

    }




    @Override
    public void initUI() {
        imgVenipuncture = (ImageView) rootview.findViewById(R.id.img_venipuncture);
        imgHC = (ImageView) rootview.findViewById(R.id.hard_copy_check);
        txtName = (TextView) rootview.findViewById(R.id.txt_name);
        txtAge = (TextView) rootview.findViewById(R.id.txt_age);
        txtAadharNo = (TextView) rootview.findViewById(R.id.txt_aadhar_no);
        txtSrNo = (TextView) rootview.findViewById(R.id.txt_sr_no);
        btnEdit = (ImageView) rootview.findViewById(R.id.img_edit);
        btnRelease = (ImageView) rootview.findViewById(R.id.img_release);
        edtTests = (EditText) rootview.findViewById(R.id.edt_test);
        edtCH = (EditText) rootview.findViewById(R.id.clinical_history);
        edtSign = (EditText) rootview.findViewById(R.id.customer_sign);
        llBarcodes = (LinearLayout)rootview.findViewById(R.id.ll_barcodes);
        btnEdit.setVisibility(View.VISIBLE);
    }

    private void initScanBarcodeView() {
        View scanBarcodeView = activity.getLayoutInflater().inflate(R.layout.item_list_view,null);
        ListView lv = (ListView) scanBarcodeView.findViewById(R.id.lv_barcodes);
        displayScanBarcodeItemListAdapter = new DisplayScanBarcodeItemListAdapter(activity,barcodeDetailsModelsArr,new ScanBarcodeIconClickedDelegateResult());
        lv.setAdapter(displayScanBarcodeItemListAdapter);
        llBarcodes.addView(scanBarcodeView);
        llBarcodes.setVisibility(View.VISIBLE);
    }

    private void clickPhoto() {
        final CharSequence[] items = {"Take Photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = DeviceUtils.checkPermission(activity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((scanningResult != null) && (scanningResult.getContents() != null)) {
            String scanned_barcode = scanningResult.getContents();
            if(!InputUtils.isNull(scanned_barcode)&&scanned_barcode.length()==8) {
                for (BarcodeDetailsModel barcodeDetailsModel :
                        barcodeDetailsModelsArr) {
                    if (currentScanSampleType.equals(barcodeDetailsModel.getSamplType())) {
                        barcodeDetailsModel.setBarcode(scanned_barcode);
                        break;
                    }
                }
                initScanBarcodeView();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
        if(requestCode==BundleConstants.EDIT_TESTS_START&& resultCode==BundleConstants.EDIT_TESTS_FINISH){
            Bundle bundle = data.getExtras();

        }
        if(requestCode==BundleConstants.ADD_EDIT_START && resultCode==BundleConstants.ADD_EDIT_FINISH){
            beneficiaryDetailsModel = data.getExtras().getParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL);
            orderDetailsModel = data.getExtras().getParcelable(BundleConstants.ORDER_DETAILS_MODEL);
            initData();
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedVanipunctureImg = encodeImage(thumbnail);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private class ScanBarcodeIconClickedDelegateResult implements ScanBarcodeIconClickedDelegate {

        @Override
        public void onClicked(String sampleType) {
            currentScanSampleType = sampleType;
            new IntentIntegrator(activity).initiateScan();
        }
    }
}
