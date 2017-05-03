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
import com.dhb.activity.OrderBookingActivity;
import com.dhb.adapter.DisplayScanBarcodeItemListAdapter;
import com.dhb.delegate.ScanBarcodeIconClickedDelegate;
import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.BeneficiarySampleTypeDetailsModel;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.DeviceUtils;

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
    private ArrayList<BeneficiarySampleTypeDetailsModel> scannedSampleTypes;
    private OrderBookingActivity activity;
    private String userChoosenTask, encodedVanipunctureImg;
    private static final int REQUEST_CAMERA = 100;
    private Bitmap thumbnail;
    private View rootview;

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
        scannedSampleTypes = new ArrayList<>();

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
        initScanBarcodeView();
    }

    private void setListeners() {
        imgVenipuncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPhoto();
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
    }

    private void initScanBarcodeView() {

        View scanBarcodeView = activity.getLayoutInflater().inflate(R.layout.item_list_view,null);
        ListView lv = (ListView) scanBarcodeView.findViewById(R.id.lv_barcodes);
        DisplayScanBarcodeItemListAdapter displayScanBarcodeItemListAdapter = new DisplayScanBarcodeItemListAdapter(activity,beneficiaryDetailsModel.getSampleType(),scannedSampleTypes,beneficiaryDetailsModel.getBarcodedtl(),new ScanBarcodeIconClickedDelegateResult());
        lv.setAdapter(displayScanBarcodeItemListAdapter);
        llBarcodes.addView(scanBarcodeView);
        llBarcodes.setVisibility(View.VISIBLE);
    }

    private void clickPhoto() {
        final CharSequence[] items = {"Take Photo",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
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
        public void onClicked(ArrayList<BarcodeDetailsModel> scannedBarcodes, ArrayList<BeneficiarySampleTypeDetailsModel> scannedSampleTypes) {
            beneficiaryDetailsModel.setBarcodedtl(scannedBarcodes);
        }
    }
}
