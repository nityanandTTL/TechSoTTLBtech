package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.NewCampScanBarcodeAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Interfaces.GoToCampMisScreen;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampPatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWisePatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitB2BWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampPatientSearchDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampWisePatientDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.B2BWoeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CheckbarcodeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.FinalMainCampWisePatentDetailsModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.SearchableSpinner;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel1;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.FailedToVaildateBarcode;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.utils.app.BundleConstants.Apikey_WOE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewEntryCampWoeFragment extends Fragment {


    private Activity mActivity;
    private Global globalclass;
    private ConnectionDetector cd;
    private AppPreferenceManager appPreferenceManager;
    private ImageView img_search,img_CapturePhoto,img_deletephoto;
    private EditText  edt_name, edt_Age,edt_search;
    private RadioGroup rg_gender;
    private RadioButton rb_male, rb_female;
    private RecyclerView recycle_ScanBarcode;
    private Button  btn_submit;
    private TextView tv_viewVailPhoto,tv_uploadVailPhoto,tv_existing,tv_new;
    private String strGender = "";
    private Camera camera;
    private NewCampScanBarcodeAdapter newCampScanBarcodeAdapter;
    private File VialPhotoFile;
    private boolean isVialPhotoCaptured = false;

    private Spinner spn_Camp;
    private SearchableSpinner spn_Search;

    private CampPatientSearchDetailResponseModel.Camp  SelectedCampDetailMainModel;
    private FinalMainCampWisePatentDetailsModel  SelectedPatientDetailMainModel;
    ArrayList<SubmitB2BWoeRequestModel.Barcodelist> SelectedPatientBarcodeArrayList = new ArrayList<>();
    private IntentIntegrator intentIntegrator;
    private String SampleTypeToScan = "";
    private int BarcodepositionToScan;
    private String patientID  = "";
    private int currentPaitientPositionInMainList = 0;
    private RelativeLayout rel_CampSpinner,rel_serach,rel_form;
    ArrayList<CampPatientSearchDetailResponseModel.Camp> MainCampArraylist;
    private int SelectedCampPosition;
    private LinearLayout lin_new_existingEntry;
    private boolean isNewEntry = false;
    private ColorStateList defaultColor;

    public NewEntryCampWoeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_entry_camp_woe, container, false);

        initView(v);
        initData();
        initListener();


        return v;
    }

    private void initView(View v) {


        rel_CampSpinner = (RelativeLayout) v.findViewById(R.id.rel_CampSpinner);
        rel_serach = (RelativeLayout) v.findViewById(R.id.rel_serach);
        img_search = (ImageView) v.findViewById(R.id.img_search);
        rel_form = (RelativeLayout) v.findViewById(R.id.rel_form);
        spn_Camp = (Spinner) v.findViewById(R.id.spn_Camp);
        spn_Search = (SearchableSpinner) v.findViewById(R.id.spn_Search);
        edt_search = (EditText) v.findViewById(R.id.edt_search);
        edt_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_Age = (EditText) v.findViewById(R.id.edt_Age);
        rg_gender = (RadioGroup) v.findViewById(R.id.rg_gender);
        rb_male = (RadioButton) v.findViewById(R.id.rb_male);
        rb_female = (RadioButton) v.findViewById(R.id.rb_female);
        recycle_ScanBarcode = (RecyclerView) v.findViewById(R.id.recycle_ScanBarcode);
        tv_uploadVailPhoto = (TextView) v.findViewById(R.id.tv_uploadVailPhoto);
        tv_viewVailPhoto = (TextView) v.findViewById(R.id.tv_viewVailPhoto);
        img_CapturePhoto = (ImageView) v.findViewById(R.id.img_CapturePhoto);
        img_deletephoto = (ImageView) v.findViewById(R.id.img_deletephoto);
        btn_submit = (Button) v.findViewById(R.id.btn_submit);

        lin_new_existingEntry = (LinearLayout) v.findViewById(R.id.lin_new_existingEntry);
        tv_existing = (TextView) v.findViewById(R.id.tv_existing);
        tv_new = (TextView) v.findViewById(R.id.tv_new);

        defaultColor =  tv_new.getTextColors();

    }

    private void initData() {

        if (cd.isConnectingToInternet()) {
            CallGetCampDetailAPI();
        } else {
            globalclass.showCustomToast(mActivity, ConstantsMessages.CHECK_INTERNET_CONN);
        }
    }

    private void initListener() {

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (edt_search.getText().toString().trim().length() > 1){
                        CallGetPatientDetailAPI(edt_search.getText().toString().trim());
                    }else{
                        globalclass.showalert_OK("Please enter minimum 2 characters to search ", mActivity);
                    }
                    return true;
                }
                return false;
            }
        });


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_search.getText().toString().trim().length() > 1){
                        CallGetPatientDetailAPI(edt_search.getText().toString().trim());
                }else{
                    globalclass.showalert_OK("Please enter minimum 2 characters to search ", mActivity);
                }
            }
        });

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        strGender = "M";
                        break;
                    case R.id.rb_female:
                        strGender = "F";
                        break;
                }
            }
        });

        img_CapturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskPermissionAndOpenCamera();
            }
        });

        img_deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete Vial Photo ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                isVialPhotoCaptured = false;
                                VialPhotoFile = null;
                                if (camera != null){
                                    camera.deleteImage();
                                }
                                tv_viewVailPhoto.setText("Upload");
                                img_deletephoto.setVisibility(View.GONE);
//                                img_CapturePhoto.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        tv_viewVailPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVialPhotoCaptured){
                    globalclass.OpenImageDialog(VialPhotoFile.getAbsolutePath(),mActivity,false);
                }else{
                    AskPermissionAndOpenCamera();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (isValidToSubmit()) {
                        if (btn_submit.getText().toString().trim().equals("Upload Vail Photo")){
                            if (!StringUtils.isNull(patientID)){
                                if (isNewEntry){
                                    CallSubmitBenVailPhotoAPI("",patientID,SelectedCampDetailMainModel.getCampID());
                                }else{
                                    CallSubmitBenVailPhotoAPI(SelectedPatientDetailMainModel.getPatientDetails().getUniqueId(),patientID,SelectedCampDetailMainModel.getCampID());
                                }

                            }else{
                                globalclass.showCustomToast(mActivity,"Patient ID not found to upload vial Photo.");
                            }
                        }else{
                            CallSubmitWOEAPI();
                        }
                    }
                } else {
                    globalclass.showCustomToast(mActivity, ConstantsMessages.CHECK_INTERNET_CONN);
                }
            }
        });

        tv_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_existing.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_existing.setBackground(getResources().getDrawable(R.drawable.selectedtab_white));
                tv_new.setTextColor(defaultColor);
                tv_new.setBackground(getResources().getDrawable(R.drawable.unselected_tab_grey));
                isNewEntry = false;
                ResetData();

            }
        });

        tv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_new.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_new.setBackground(getResources().getDrawable(R.drawable.selectedtab_white));
                tv_existing.setTextColor(defaultColor);
                tv_existing.setBackground(getResources().getDrawable(R.drawable.unselected_tab_grey));
                isNewEntry = true;
                ResetData();
            }
        });
    }

    private void CallGetCampDetailAPI() {

        CampPatientDetailRequestModel campPatientDetailRequestModel = new CampPatientDetailRequestModel();
        campPatientDetailRequestModel.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
//        campPatientDetailRequestModel.setBtechId("F768027001");

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<CampPatientSearchDetailResponseModel> responseCall = apiInterface.CallGetCampDetailAPI(campPatientDetailRequestModel);
        globalclass.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<CampPatientSearchDetailResponseModel>() {
            @Override
            public void onResponse(Call<CampPatientSearchDetailResponseModel> call, Response<CampPatientSearchDetailResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null){
                    onCampDetailsReceived(response.body());
                }else{
                    ShowNoCampAssignedAlertBox();
                }
            }
            @Override
            public void onFailure(Call<CampPatientSearchDetailResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                ShowNoCampAssignedAlertBox();
            }
        });
    }

    private void onCampDetailsReceived(CampPatientSearchDetailResponseModel responseModel) {
        if (responseModel.getResponseID() != null && responseModel.getResponseID().equalsIgnoreCase("RES0000") && responseModel.getCamp() != null && responseModel.getCamp().size() > 0){
            MainCampArraylist = new ArrayList<>();
            CampPatientSearchDetailResponseModel.Camp mainCampWisePatentDetailsModel = new CampPatientSearchDetailResponseModel.Camp();
            mainCampWisePatentDetailsModel.setCampName(" - Select Camp - ");
            MainCampArraylist.add(mainCampWisePatentDetailsModel);
            MainCampArraylist.addAll(responseModel.getCamp());

                ArrayAdapter<CampPatientSearchDetailResponseModel.Camp> spinnerCampArrayAdapter = new ArrayAdapter<CampPatientSearchDetailResponseModel.Camp>(mActivity, android.R.layout.simple_spinner_item, MainCampArraylist);
                spinnerCampArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Camp.setAdapter(spinnerCampArrayAdapter);
                spn_Camp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ResetData();
                        if (position > 0) {
                            SelectedCampPosition = position;
                            SelectedCampDetailMainModel = MainCampArraylist.get(position);
                            rel_serach.setVisibility(View.VISIBLE);
                            lin_new_existingEntry.setVisibility(View.VISIBLE);
//                            DisplayPaitentSpinner(SelectedCampDetailMainModel.getPatientDetails());
                        }else{
                            rel_serach.setVisibility(View.GONE);
                            rel_form.setVisibility(View.GONE);
                            lin_new_existingEntry.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            if (spn_Camp.getAdapter().getCount() == 2){
                spn_Camp.setSelection(1);
            }

        }else{
            ShowNoCampAssignedAlertBox();
        }
    }

    private void CallGetPatientDetailAPI(final String StrSearch) {
        CampWisePatientDetailRequestModel campWisePatientDetailRequestModel = new CampWisePatientDetailRequestModel();
        campWisePatientDetailRequestModel.setCampId(SelectedCampDetailMainModel.getCampID());
        campWisePatientDetailRequestModel.setFiltervalue(StrSearch);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<CampWisePatientDetailResponseModel> responseCall = apiInterface.CallGetPatientDetailAPI(campWisePatientDetailRequestModel);
        globalclass.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<CampWisePatientDetailResponseModel>() {
            @Override
            public void onResponse(Call<CampWisePatientDetailResponseModel> call, Response<CampWisePatientDetailResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null){
                    CampWisePatientDetailResponseModel campWisePatientDetailResponseModel = response.body();
                    if (campWisePatientDetailResponseModel.getPatient() != null ){
                        DisplayPaitentSpinner(campWisePatientDetailResponseModel.getPatient(),true);
                    }else{
                        globalclass.showalert_OK("No Data found for '"+StrSearch+"'" ,mActivity);
                        ResetData();
                    }

                }else{
                    globalclass.showalert_OK("No Data found for '"+StrSearch+"'" ,mActivity);
                    ResetData();
                }
            }
            @Override
            public void onFailure(Call<CampWisePatientDetailResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                globalclass.showalert_OK("No Data found for '"+StrSearch+"'" ,mActivity);
            }
        });
    }

    private void DisplayPaitentSpinner(ArrayList<CampPatientSearchDetailResponseModel.PatientDetails> patientDetailsAryList, boolean performClick) {

        rel_serach.setVisibility(View.VISIBLE);

        if (patientDetailsAryList.size() > 0){
            final ArrayList<FinalMainCampWisePatentDetailsModel>  finalMainCampWisePatentDetailsAryList = new ArrayList<>();
            rel_form.setVisibility(View.VISIBLE);
            if (patientDetailsAryList.size() == 1){
                for (int j = 0; j < patientDetailsAryList.size(); j++) {
                    FinalMainCampWisePatentDetailsModel model = new FinalMainCampWisePatentDetailsModel();
                    model.setCamp(SelectedCampDetailMainModel);
                    model.setPatientDetails(patientDetailsAryList.get(j));
                    model.setBarcodelistArrayList(SelectedCampDetailMainModel.getBarcodelist());
                    finalMainCampWisePatentDetailsAryList.add(model);
                }

                if (finalMainCampWisePatentDetailsAryList.size() > 0){
                    SelectedPatientDetailMainModel = finalMainCampWisePatentDetailsAryList.get(0);
                    DisplayPatientData(SelectedPatientDetailMainModel);
                }

            }else{
                /*FinalMainCampWisePatentDetailsModel Defaultmodel = new FinalMainCampWisePatentDetailsModel();
                Defaultmodel.setCamp(new CampPatientSearchDetailResponseModel.Camp());
                CampPatientSearchDetailResponseModel.PatientDetails patientDetails = new CampPatientSearchDetailResponseModel.PatientDetails();
                patientDetails.setName("- Search Patient -");
                Defaultmodel.setPatientDetails(patientDetails);
                Defaultmodel.setBarcodelistArrayList(new ArrayList<SubmitB2BWoeRequestModel.Barcodelist>());
                finalMainCampWisePatentDetailsAryList.add(Defaultmodel);*/
                if (patientDetailsAryList != null &&patientDetailsAryList.size() > 0){
                    for (int j = 0; j < patientDetailsAryList.size(); j++) {
                        FinalMainCampWisePatentDetailsModel model = new FinalMainCampWisePatentDetailsModel();
                        model.setCamp(SelectedCampDetailMainModel);
                        model.setPatientDetails(patientDetailsAryList.get(j));
                        model.setBarcodelistArrayList(SelectedCampDetailMainModel.getBarcodelist());
                        finalMainCampWisePatentDetailsAryList.add(model);
                    }
                }

                ArrayAdapter<FinalMainCampWisePatentDetailsModel> spinnerArrayAdapter = new ArrayAdapter<FinalMainCampWisePatentDetailsModel>(mActivity, android.R.layout.simple_spinner_item, finalMainCampWisePatentDetailsAryList); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Search.setAdapter(spinnerArrayAdapter);
                spn_Search.setTitle("Search Patient");
                spn_Search.setPositiveButton("OK");
                spn_Search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ResetData();
//                        if (position > 0) {
                            rel_form.setVisibility(View.VISIBLE);
                            currentPaitientPositionInMainList = position;
                            SelectedPatientDetailMainModel = finalMainCampWisePatentDetailsAryList.get(position);
                            DisplayPatientData(SelectedPatientDetailMainModel);
//                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (performClick){
                    spn_Search.OpenSpinnerDialog();
                }
            }
        }else{
            globalclass.showCustomToast(mActivity,"No Data Found");
        }



    }


    private void DisplayPatientData(FinalMainCampWisePatentDetailsModel selectedPatientDetailMainModel) {
        edt_name.setText(selectedPatientDetailMainModel.getPatientDetails().getName());
            edt_Age.setText(selectedPatientDetailMainModel.getPatientDetails().getAge());
            if (selectedPatientDetailMainModel.getPatientDetails().getGender().equalsIgnoreCase("M")){
                strGender = "M";
                rb_male.setChecked(true);
            }else{
                strGender = "F";
                rb_female.setChecked(true);
            }
            if (SelectedCampDetailMainModel != null && SelectedCampDetailMainModel.getBarcodelist() != null &&  SelectedCampDetailMainModel.getBarcodelist().size() > 0){
                SelectedPatientBarcodeArrayList = null;
                SelectedPatientBarcodeArrayList = new ArrayList<>();
                SelectedPatientBarcodeArrayList = SelectedCampDetailMainModel.getBarcodelist();
                DisplayBarcodesforScanning();
            }

    }

    private void DisplayBarcodesforScanning() {
        newCampScanBarcodeAdapter = new NewCampScanBarcodeAdapter(mActivity, SelectedPatientBarcodeArrayList);
        newCampScanBarcodeAdapter.setOnItemClickListener(new NewCampScanBarcodeAdapter.OnClickListeners() {
            @Override
            public void onScanBarcode(String sample_type, int position) {
                SampleTypeToScan = sample_type;
                BarcodepositionToScan = position;
                OpenScanBarcodeScreen();
//                OpenBarcodeConfirmationDialog(DeviceUtils.randomBarcodeString(8)); // Testing in simulator
            }

            @Override
            public void onDeleteBarcode(String barcode) {

                if (!StringUtils.isNull(barcode)){
                    for (int i = 0; i < SelectedPatientBarcodeArrayList.size(); i++) {
                        if (SelectedPatientBarcodeArrayList.get(i).getBARCODE() != null && SelectedPatientBarcodeArrayList.get(i).getBARCODE().equals(barcode)){
                            SelectedPatientBarcodeArrayList.get(i).setBARCODE("");
                        }
                    }
                }else{
                    globalclass.showCustomToast(mActivity,"No barcode to delete");
                }
                newCampScanBarcodeAdapter.UpdateBarcodeList(SelectedPatientBarcodeArrayList);
                newCampScanBarcodeAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        recycle_ScanBarcode.setLayoutManager(linearLayoutManager);
        recycle_ScanBarcode.setAdapter(newCampScanBarcodeAdapter);
        recycle_ScanBarcode.setVisibility(View.VISIBLE);
    }

    private void AskPermissionAndOpenCamera() {

        TedPermission.with(mActivity)
                .setPermissions(Manifest.permission.CAMERA)
                .setRationaleMessage("We need permission to capture photo from your camera to Upload Vail Photo.")
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
                .setDirectory("BtechApp/CampVailsPhotos")
//                .setName(orderVisitDetailsModel.getVisitId() + "_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(60)
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
                mActivity.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN);
            }
        };
        intentIntegrator.setPrompt("Scan a barcode");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.forSupportFragment(this).initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BundleConstants.START_BARCODE_SCAN) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if ((scanningResult != null) && (scanningResult.getContents() != null)) {
                final String scanned_barcode = scanningResult.getContents().trim();
                if (!InputUtils.isNull(scanned_barcode)) {
                    OpenBarcodeConfirmationDialog(scanned_barcode);
                } else {
                    Toast.makeText(mActivity, "Retry scanning barcode", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imageurl = "";
            try {
                imageurl = camera.getCameraBitmapPath();
                VialPhotoFile = new File(imageurl);
                if (VialPhotoFile.exists()) {
                    isVialPhotoCaptured = true;
                    tv_viewVailPhoto.setText(Html.fromHtml("<u>"+" View "+"</u>"));
                    img_deletephoto.setVisibility(View.VISIBLE);
                    img_CapturePhoto.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void OpenBarcodeConfirmationDialog(final String scanned_barcode) {
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

                if (TextUtils.isEmpty(scanned_barcode) || scanned_barcode.startsWith("0") || scanned_barcode.startsWith("$") || scanned_barcode.startsWith("1") || scanned_barcode.startsWith(" ") /*|| Character.isDigit(scanned_barcode.charAt(0))*/) {
                    Toast.makeText(mActivity, "Invalid barcode", Toast.LENGTH_SHORT).show();
                } else {
                    if (!InputUtils.isNull(scanned_barcode) && scanned_barcode.length() == 8) {
                        CallCheckbarcodeAPI(scanned_barcode);
                    } else {
                        Toast.makeText(mActivity, "Barcode should be of 8 digits", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).show();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    private void CallCheckbarcodeAPI(final String scanned_barcode) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(GetAPIInterface.class);
        Call<CheckbarcodeResponseModel> responseCall = apiInterface.CallCheckBarcodeAPI(Apikey_WOE,scanned_barcode);
        globalclass.showProgressDialog(mActivity,"Validating barcode. Please wait..",false);
        responseCall.enqueue(new Callback<CheckbarcodeResponseModel>() {
            @Override
            public void onResponse(Call<CheckbarcodeResponseModel> call, retrofit2.Response<CheckbarcodeResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    if (!StringUtils.isNull(response.body().getRES_ID()) && response.body().getRES_ID().equalsIgnoreCase("RES0000")){
                        if (SelectedPatientBarcodeArrayList != null) {
                            for (int i = 0; i < SelectedPatientBarcodeArrayList.size(); i++) {
                                //size 4
                                if (!InputUtils.isNull(SelectedPatientBarcodeArrayList.get(i).getSAMPLE_TYPE())
                                        && !InputUtils.isNull(SampleTypeToScan)
                                        && SampleTypeToScan.equals(SelectedPatientBarcodeArrayList.get(i).getSAMPLE_TYPE())
                                        && BarcodepositionToScan == i) {

                                    for (int j = 0; j < SelectedPatientBarcodeArrayList.size(); j++) {
                                        if (!InputUtils.isNull(SelectedPatientBarcodeArrayList.get(j).getBARCODE()) && SelectedPatientBarcodeArrayList.get(j).getBARCODE().equals(scanned_barcode)) {
                                            Toast.makeText(mActivity, "Same barcode already scanned for "  + SelectedPatientBarcodeArrayList.get(j).getSAMPLE_TYPE(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    SelectedPatientBarcodeArrayList.get(i).setBARCODE(scanned_barcode);
                                    break;
                                }
                            }
                            if (newCampScanBarcodeAdapter != null){
                                newCampScanBarcodeAdapter.UpdateBarcodeList(SelectedPatientBarcodeArrayList);
                                newCampScanBarcodeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mActivity, "Failed to update scanned barcode value", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mActivity,!StringUtils.isNull(response.body().getResponse()) ?  "'"+scanned_barcode +"' "+response.body().getResponse() :  FailedToVaildateBarcode, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mActivity, FailedToVaildateBarcode, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CheckbarcodeResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                Toast.makeText(mActivity, FailedToVaildateBarcode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidToSubmit() {
        String ErrorMsg = "";
        boolean showbarcodeError = false;
        String PatientName = edt_name.getText().toString().trim();
        String PatientAge = edt_Age.getText().toString().trim();
        int Age = 0;
        try {
            Age = Integer.parseInt(PatientAge);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (SelectedPatientBarcodeArrayList != null && SelectedPatientBarcodeArrayList.size() > 0){
            for (int i = 0; i < SelectedPatientBarcodeArrayList.size(); i++) {
                if (StringUtils.isNull(SelectedPatientBarcodeArrayList.get(i).getBARCODE())){
                    ErrorMsg = "Please Scan Barcode for Sample type : " + SelectedPatientBarcodeArrayList.get(i).getSAMPLE_TYPE() ;
                    showbarcodeError = true;
                    break;
                }
            }
        }else{
            showbarcodeError = true;
            ErrorMsg = "Barocdes are Missing. Cannot Submit WOE.";
        }

        if (StringUtils.isNull(PatientName)) {
            ErrorMsg = ConstantsMessages.EnterPatientName;
        } else if (PatientName.length() < 2) {
            ErrorMsg = ConstantsMessages.EnterVAlidPatientName;
        } else if (StringUtils.isNull(PatientAge)) {
            ErrorMsg = ConstantsMessages.EnterPatientAge;
        } else if (Age < 1 || Age > 120) {
            ErrorMsg = ConstantsMessages.EnterValidPatientAge;
        } else if (StringUtils.isNull(strGender)) {
            ErrorMsg = ConstantsMessages.Select_PatientGender;
        }else if (showbarcodeError){

        } else{
            try {
                if (VialPhotoFile == null || !VialPhotoFile.exists()) {
                    ErrorMsg = ConstantsMessages.UploadPatientVailPhoto;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }





        if (!StringUtils.isNull(ErrorMsg)) {
            globalclass.showalert_OK(ErrorMsg, mActivity);
            return false;
        } else {
            return true;
        }

    }

    private void CallSubmitWOEAPI() {

        String name = edt_name.getText().toString().trim();
        int Age = Integer.parseInt(edt_Age.getText().toString().trim());
        String mobile  = "";
        String UID = "";
        String address = "";
        if (SelectedPatientDetailMainModel != null && SelectedPatientDetailMainModel.getPatientDetails() != null && !StringUtils.isNull(SelectedPatientDetailMainModel.getPatientDetails().getContactNo())){
            mobile = SelectedPatientDetailMainModel.getPatientDetails().getContactNo();
        }
        if (SelectedPatientDetailMainModel != null && SelectedPatientDetailMainModel.getPatientDetails() != null && !StringUtils.isNull(SelectedPatientDetailMainModel.getPatientDetails().getUID())){
            UID = SelectedPatientDetailMainModel.getPatientDetails().getUID();
        }
        if (SelectedPatientDetailMainModel != null && SelectedPatientDetailMainModel.getPatientDetails() != null && !StringUtils.isNull(SelectedPatientDetailMainModel.getPatientDetails().getAddress())){
            address = SelectedPatientDetailMainModel.getPatientDetails().getAddress();
        }

        SubmitB2BWoeRequestModel submitB2BWoeRequestModel = new SubmitB2BWoeRequestModel();
        SubmitB2BWoeRequestModel.Woe woe = new SubmitB2BWoeRequestModel.Woe();
        woe.setAADHAR_NO("");
        woe.setADDRESS(address.toUpperCase());
        woe.setAGE(Age);
        woe.setAGE_TYPE("Y");
        woe.setALERT_MESSAGE("");
        woe.setAMOUNT_COLLECTED(SelectedCampDetailMainModel.getAmountCollected());
        woe.setAMOUNT_DUE(0);
        woe.setAPP_ID("2.0");
        woe.setBCT_ID(appPreferenceManager.getLoginResponseModel().getUserID());
//        woe.setBCT_ID("F768027001");
        woe.setBRAND("TTL");
        woe.setCAMP_ID(SelectedCampDetailMainModel.getCampID());
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(mobile);
        woe.setCUSTOMER_ID(UID);
        woe.setDELIVERY_MODE(2);
        woe.setEMAIL_ID("");
        woe.setENTERED_BY(appPreferenceManager.getLoginResponseModel().getUserID());
        woe.setGENDER(strGender);
        woe.setLAB_ADDRESS(SelectedCampDetailMainModel.getCampAddress().toUpperCase());
        woe.setLAB_ID("");
        woe.setLAB_NAME("");
        woe.setLEAD_ID("");
        woe.setMAIN_SOURCE(SelectedCampDetailMainModel.getSourceCode());
        woe.setORDER_NO("");
        woe.setOS("");
        woe.setPATIENT_NAME(name.toUpperCase());
        woe.setPINCODE("");
        String Product = "";
        for (int i = 0; i < SelectedCampDetailMainModel.getBarcodelist().size(); i++) {
            Product = Product + "," + SelectedCampDetailMainModel.getBarcodelist().get(i).getTESTS();
        }
        woe.setPRODUCT(StringUtils.removeFirstCharacter(Product.replace(",,",",")));
        woe.setPurpose("");
        woe.setREF_DR_ID("");
        woe.setREF_DR_NAME(SelectedCampDetailMainModel.getRefDrname());
        woe.setREMARKS("MOBILE");
        woe.setSPECIMEN_COLLECTION_TIME(DateUtil.getDateFromLong(System.currentTimeMillis(),"yyy-MM-dd HH:mm"));
        woe.setSPECIMEN_SOURCE("");
        woe.setSR_NO(1);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(SelectedCampDetailMainModel.getSourceCode());
        woe.setTOTAL_AMOUNT(Integer.parseInt(SelectedCampDetailMainModel.getAmountCollected()));
        woe.setTYPE("CAMP");
        woe.setWATER_SOURCE("");
        woe.setWO_MODE("BTECHAPP");
        woe.setWO_STAGE(3);

        submitB2BWoeRequestModel.setWoe(woe);
        submitB2BWoeRequestModel.setBarcodelist(SelectedPatientBarcodeArrayList);
        submitB2BWoeRequestModel.setWoe_type("WOE");
        submitB2BWoeRequestModel.setApi_key(Apikey_WOE);//api_key

        CallSubmitWoeDetailsAPI(submitB2BWoeRequestModel);
    }

    private void CallSubmitWoeDetailsAPI(final SubmitB2BWoeRequestModel submitB2BWoeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<B2BWoeResponseModel> responseCall = apiInterface.CallSubmitCampWOEAPI(submitB2BWoeRequestModel);
        globalclass.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<B2BWoeResponseModel>() {
            @Override
            public void onResponse(Call<B2BWoeResponseModel> call, Response<B2BWoeResponseModel> response) {
                globalclass.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null){
                    onSubmitWoeResponseReceived(response.body());
                }else{
                    Toast.makeText(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<B2BWoeResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
            }
        });

    }

    private void onSubmitWoeResponseReceived(B2BWoeResponseModel b2BWoeResponseModel) {

        if (b2BWoeResponseModel.getRES_ID() != null && b2BWoeResponseModel.getRES_ID().equalsIgnoreCase("RES0000")){
            if (isNewEntry){
                if (!StringUtils.isNull(b2BWoeResponseModel.getBarcode_patient_id())
                        && !StringUtils.isNull(SelectedCampDetailMainModel.getCampID())){
                    CallSubmitBenVailPhotoAPI("", b2BWoeResponseModel.getBarcode_patient_id(),SelectedCampDetailMainModel.getCampID());
                }else{
                    patientID = !StringUtils.isNull(b2BWoeResponseModel.getBarcode_patient_id()) ? b2BWoeResponseModel.getBarcode_patient_id() : "";
                    btn_submit.setText("Upload Vail Photo");
                    globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
                }
            }else{
                if (SelectedPatientDetailMainModel.getPatientDetails() != null
                        && !StringUtils.isNull(SelectedPatientDetailMainModel.getPatientDetails().getUniqueId())
                        && !StringUtils.isNull(b2BWoeResponseModel.getBarcode_patient_id())
                        && !StringUtils.isNull(SelectedCampDetailMainModel.getCampID())){

                    CallSubmitBenVailPhotoAPI(SelectedPatientDetailMainModel.getPatientDetails().getUniqueId(), b2BWoeResponseModel.getBarcode_patient_id(),SelectedCampDetailMainModel.getCampID());
                }else{
                    patientID = !StringUtils.isNull(b2BWoeResponseModel.getBarcode_patient_id()) ? b2BWoeResponseModel.getBarcode_patient_id() : "";
                    btn_submit.setText("Upload Vail Photo");
                    globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
                }
            }
        }else{
            globalclass.showCustomToast(mActivity,!StringUtils.isNull(b2BWoeResponseModel.getMessage()) ? b2BWoeResponseModel.getMessage() : "Failed to submit WOE. Please try Again.");
        }

    }

    private void CallSubmitBenVailPhotoAPI(String uniqueID, final String woepatientID, String campID) {

        // add another part within the multipart request
        RequestBody requestuniqueID = RequestBody.create(MediaType.parse("multipart/form-data"), uniqueID);
        RequestBody requestwoepatientID = RequestBody.create(MediaType.parse("multipart/form-data"), woepatientID);
        RequestBody requestcampID = RequestBody.create(MediaType.parse("multipart/form-data"), campID);

        MultipartBody.Part ImageFileMultiBody = null;
        if(VialPhotoFile != null &&VialPhotoFile.exists()){
            MessageLogger.info(mActivity,"FileName "+ VialPhotoFile.getName());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), VialPhotoFile);
            ImageFileMultiBody = MultipartBody.Part.createFormData("vailImage", VialPhotoFile.getName(), requestFile);
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<CommonResponseModel1> responseCall = apiInterface.CalluploadCAmpWOEPatientVailPhotoAPI(ImageFileMultiBody,requestuniqueID,requestwoepatientID,requestcampID);
        globalclass.showProgressDialog(mActivity,"Please wait",false);

        responseCall.enqueue(new Callback<CommonResponseModel1>() {
            @Override
            public void onResponse(Call<CommonResponseModel1> call, Response<CommonResponseModel1> response) {
                globalclass.hideProgressDialog(mActivity);

                if (response.isSuccessful() && response.body() != null) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage("WOE done Successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    try {
                                        ResetData();
//                                        RemoveWOEDonePaitentDetailsforMainList();
//                                        getActivity().getSupportFragmentManager().beginTransaction().detach(NewEntryCampWoeFragment.this).attach(NewEntryCampWoeFragment.this).commit();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    patientID = woepatientID;
                    btn_submit.setText("Upload Vail Photo");
                    globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel1> call, Throwable t) {
                globalclass.hideProgressDialog(mActivity);
                patientID = woepatientID;
                btn_submit.setText("Upload Vail Photo");
                globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
            }
        });
    }

    private void RemoveWOEDonePaitentDetailsforMainList() {

        int patientposition = currentPaitientPositionInMainList - 1;
        if (MainCampArraylist != null &&
                MainCampArraylist.size() > SelectedCampPosition &&
                MainCampArraylist.get(SelectedCampPosition).getPatientDetails() != null &&
                MainCampArraylist.get(SelectedCampPosition).getPatientDetails().size() > patientposition){

            MainCampArraylist.get(SelectedCampPosition).getPatientDetails().remove(patientposition);
        }
        /*if (finalMainCampWisePatentDetailsAryList != null && finalMainCampWisePatentDetailsAryList.size() > currentPaitientPositionInMainList ){
            finalMainCampWisePatentDetailsAryList.remove(currentPaitientPositionInMainList);
        }*/

        if (MainCampArraylist != null && MainCampArraylist.size() > 0){
                if (MainCampArraylist.size() == 1 ){
                    rel_CampSpinner.setVisibility(View.GONE);
                    SelectedCampDetailMainModel = MainCampArraylist.get(0);
                }else{
                    rel_CampSpinner.setVisibility(View.VISIBLE);
                    if (spn_Camp != null && spn_Camp.getAdapter().getCount() > SelectedCampPosition) {
                        SelectedCampDetailMainModel = null;
                        SelectedCampDetailMainModel = new CampPatientSearchDetailResponseModel.Camp();
                        spn_Camp.setSelection(SelectedCampPosition);
                    }
                }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isVialPhotoCaptured = false;
        VialPhotoFile = null;
        if (camera != null){
            camera.deleteImage();
        }
        tv_viewVailPhoto.setText("Upload");
        img_deletephoto.setVisibility(View.GONE);
    }

    private void ResetData() {
        edt_search.setText("");
        edt_name.setText("");
        edt_Age.setText("");
        btn_submit.setText("Submit");
        rb_male.setChecked(true);
        strGender = "M";
        patientID = "";
        SampleTypeToScan = "";
        isVialPhotoCaptured = false;
        VialPhotoFile = null;
        if (camera != null){
            camera.deleteImage();
        }
        tv_viewVailPhoto.setText("Upload");
        img_deletephoto.setVisibility(View.GONE);
//        img_CapturePhoto.setVisibility(View.VISIBLE);

        SelectedPatientDetailMainModel = null;

        if (SelectedCampDetailMainModel != null && SelectedCampDetailMainModel.getBarcodelist() != null){
            for (int i = 0; i < SelectedCampDetailMainModel.getBarcodelist().size(); i++) {
                SelectedCampDetailMainModel.getBarcodelist().get(i).setBARCODE("");
            }
        }
        if (isNewEntry){
            rel_serach.setVisibility(View.GONE);
            rel_form.setVisibility(View.VISIBLE);
            if (SelectedCampDetailMainModel != null && SelectedCampDetailMainModel.getBarcodelist() != null &&  SelectedCampDetailMainModel.getBarcodelist().size() > 0){
                SelectedPatientBarcodeArrayList = null;
                SelectedPatientBarcodeArrayList = new ArrayList<>();
                SelectedPatientBarcodeArrayList = SelectedCampDetailMainModel.getBarcodelist();
                DisplayBarcodesforScanning();
            }

        }else{
            rel_serach.setVisibility(View.VISIBLE);
            rel_form.setVisibility(View.GONE);
            SelectedPatientBarcodeArrayList = null;
            SelectedPatientBarcodeArrayList = new ArrayList<>();
            if (newCampScanBarcodeAdapter != null){
                newCampScanBarcodeAdapter.UpdateBarcodeList(SelectedPatientBarcodeArrayList);
                newCampScanBarcodeAdapter.notifyDataSetChanged();
            }
            recycle_ScanBarcode.setVisibility(View.GONE);
        }

//        currentPaitientPositionInMainList = 0;
    }

    private void ShowNoCampAssignedAlertBox() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
        alertDialogBuilder
                .setMessage("No camp is assigned for today.")
                .setCancelable(false)
                .setPositiveButton("Go To MIS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        GoToCampMisScreen listener = (GoToCampMisScreen)getActivity();
                        if (listener != null){
                            listener.GoToMisScreen();
                        }
                    }
                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mActivity.finish();
            }
        });
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
