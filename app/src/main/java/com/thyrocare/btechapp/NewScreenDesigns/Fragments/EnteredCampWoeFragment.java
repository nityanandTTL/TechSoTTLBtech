package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.CampWOE_MIS_Adpter;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWoeMISReuestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampModuleMISResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampWoeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.DateUtil;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.SelectDatePickerDialogFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnteredCampWoeFragment extends Fragment {

    private TextView tv_date;
    private ImageView img_dateIcon,img_search;
    private EditText edt_Search;
    private RecyclerView recycle_CampWOEMIS;
    private Activity mActivity;
    private Global globalclass;
    private ConnectionDetector cd;
    private AppPreferenceManager appPreferenceManager;
    private String strDate;
    private TextView tv_noDatafound;
    private CampWOE_MIS_Adpter campWOE_mis_adpter;
    private Camera camera;
    private File VialPhotoFile;
    private CampModuleMISResponseModel.Output SelectedPatientModelforVialImageUpload;
    private Dialog openDialog;

    public EnteredCampWoeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_entered_camp_woe, container, false);

        strDate = DateUtil.getDateFromLong(System.currentTimeMillis(),"yyyy-MM-dd");
        initView(v);
        initData();
        initListener();


        return v;
    }

    private void initView(View v) {

        tv_date = (TextView) v.findViewById(R.id.tv_date);
        img_dateIcon = (ImageView) v.findViewById(R.id.img_dateIcon);
        img_search = (ImageView) v.findViewById(R.id.img_search);
        edt_Search = (EditText) v.findViewById(R.id.edt_Search);
        recycle_CampWOEMIS = (RecyclerView) v.findViewById(R.id.recycle_CampWOEMIS);
        tv_noDatafound = (TextView) v.findViewById(R.id.tv_noDatafound);

    }

    private void initData() {
        if (cd.isConnectingToInternet()){
            tv_date.setText(DateUtil.getDateFromLong(System.currentTimeMillis(),"dd-MM-yyyy"));
            CallCampWOEMisAPI();
        }else{
            globalclass.showCustomToast(mActivity, ConstantsMessages.CHECK_INTERNET_CONN);
        }
    }

    private void initListener() {

        edt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (campWOE_mis_adpter != null){
                    campWOE_mis_adpter.filterData(s.toString().trim());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNull(edt_Search.getText().toString().trim())){
                    if (campWOE_mis_adpter != null){
                        campWOE_mis_adpter.filterData(edt_Search.getText().toString().trim());
                    }
                }else{
                    globalclass.showCustomToast(mActivity,"Please enter something in search box to start searching");
                }

            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDAtePicker();
            }
        });
        img_dateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDAtePicker();
            }
        });

    }

    private void OpenDAtePicker() {
        Calendar mindate = Calendar.getInstance();
                /*mindate.add(Calendar.DAY_OF_MONTH, 1);
                mindate.add(Calendar.MONTH, -3);*/
        mindate.add(Calendar.YEAR, -1);
        Calendar maxdate = Calendar.getInstance();
        SelectDatePickerDialogFragment datePickerDialogFragment = new SelectDatePickerDialogFragment(mActivity, "Select Date", mindate.getTimeInMillis(), maxdate.getTimeInMillis(), "dd-MM-yyyy");
        datePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String strSelectedDate, Date SelectedDate) {

                strDate = DateUtil.getDateFromLong(SelectedDate.getTime(),"yyyy-MM-dd");
                tv_date.setText(strSelectedDate);
                if (cd.isConnectingToInternet()){
                    CallCampWOEMisAPI();
                }else{
                    globalclass.showCustomToast(mActivity,ConstantsMessages.CHECK_INTERNET_CONN);
                }
            }
        });
        datePickerDialogFragment.show(getFragmentManager(), "DatePicker");
    }

    private void CallCampWOEMisAPI() {

        CampWoeMISReuestModel campWoeMISReuestModel = new CampWoeMISReuestModel();
        campWoeMISReuestModel.setTechid(appPreferenceManager.getLoginResponseModel().getUserID());
//        campWoeMISReuestModel.setTechid("F768027001");
        campWoeMISReuestModel.setSdate(strDate);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.DecodeString64(mActivity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<CampModuleMISResponseModel> responseCall = apiInterface.CallGetCampWOEMISAPI(campWoeMISReuestModel);
        globalclass.showProgressDialog(mActivity,"Please wait..");
        responseCall.enqueue(new Callback<CampModuleMISResponseModel>() {
            @Override
            public void onResponse(Call<CampModuleMISResponseModel> call, Response<CampModuleMISResponseModel> response) {
                globalclass.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null){
                    onCampWOEMisAPIresponseReceived(response.body());
                }else{
                    Toast.makeText(mActivity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CampModuleMISResponseModel> call, Throwable t) {
                globalclass.hideProgressDialog();
            }
        });
    }

    private void onCampWOEMisAPIresponseReceived(CampModuleMISResponseModel responseModel) {
        if (responseModel.getResponseID() != null && responseModel.getResponseID().equalsIgnoreCase("RES0000")){
            if (responseModel.getOutput() != null && responseModel.getOutput().size() > 0){

                DisplayData(responseModel.getOutput());
            }else{
                tv_noDatafound.setVisibility(View.VISIBLE);
                recycle_CampWOEMIS.setVisibility(View.GONE);
            }
        }else{
            tv_noDatafound.setVisibility(View.VISIBLE);
            recycle_CampWOEMIS.setVisibility(View.GONE);
        }
    }

    private void DisplayData(ArrayList<CampModuleMISResponseModel.Output> CampMisAryList) {

        campWOE_mis_adpter = new CampWOE_MIS_Adpter(mActivity,CampMisAryList);
        campWOE_mis_adpter.setOnItemClickListener(new CampWOE_MIS_Adpter.OnClickListeners() {
            @Override
            public void onFilter(boolean isDataavailable) {
                if (!isDataavailable){
                    tv_noDatafound.setVisibility(View.VISIBLE);
                }else{
                    tv_noDatafound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onUploadVialImageClicked(CampModuleMISResponseModel.Output output) {
                SelectedPatientModelforVialImageUpload = output;

                if (!StringUtils.isNull(SelectedPatientModelforVialImageUpload.getPatientID())){
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage("Do you want to uploads vial Image for "+SelectedPatientModelforVialImageUpload.getName()+" ?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    AskPermissionAndOpenCamera();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    globalclass.showCustomToast(mActivity, "You cannot upload Vail Image for selected Patient.");
                }


            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recycle_CampWOEMIS.setLayoutManager(mLayoutManager);
        /*recycle_CampWOEMIS.setItemAnimator(new DefaultItemAnimator());
        recycle_CampWOEMIS.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        recycle_CampWOEMIS.setHasFixedSize(true);*/
        recycle_CampWOEMIS.setAdapter(campWOE_mis_adpter);
        recycle_CampWOEMIS.setVisibility(View.VISIBLE);
        tv_noDatafound.setVisibility(View.GONE);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String imageurl = "";
            try {
                imageurl = camera.getCameraBitmapPath();
                VialPhotoFile = new File(imageurl);
                if (VialPhotoFile.exists()) {
                    ShowImageInDialog();
                } else {
                    Toast.makeText(mActivity, "Failed to capture photo", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ShowImageInDialog() {
        try {
           openDialog = new Dialog(mActivity);
            openDialog.setContentView(R.layout.vial_image_display_dialog);
            int width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.99);
            int height = (int) (mActivity.getResources().getDisplayMetrics().heightPixels * 0.90);
            openDialog.getWindow().setLayout(width, height);
            openDialog.setTitle("");

            ImageView img_close = (ImageView) openDialog.findViewById(R.id.img_close);
            Button btn_Upload = (Button) openDialog.findViewById(R.id.btn_Upload);
            Button btn_Capture = (Button) openDialog.findViewById(R.id.btn_Capture);
            ImageView imageview = (ImageView) openDialog.findViewById(R.id.imageview);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog.dismiss();
                    SelectedPatientModelforVialImageUpload = null;
                }
            });

            btn_Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cd.isConnectingToInternet()){
                        CallSubmitBenVailPhotoAPI(SelectedPatientModelforVialImageUpload.getUniqueId(),SelectedPatientModelforVialImageUpload.getPatientID(),SelectedPatientModelforVialImageUpload.getCampID());
                    }else{
                        globalclass.showCustomToast(mActivity,ConstantsMessages.CHECK_INTERNET_CONN);
                    }
                }
            });

            btn_Capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AskPermissionAndOpenCamera();
                }
            });

            Glide.with(mActivity)
                    .load(VialPhotoFile.getAbsolutePath())
                    .placeholder(R.drawable.app_logo).dontAnimate()
                    .error(R.drawable.app_logo)
                    .into(imageview);

            if (!mActivity.isFinishing() && openDialog != null ){
                openDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            public void onResponse(final Call<CommonResponseModel1> call, Response<CommonResponseModel1> response) {
                globalclass.hideProgressDialog();

                if (response.isSuccessful() && response.body() != null) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage("Vial Image Uploaded Successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    try {
                                        if (!mActivity.isFinishing() && openDialog != null && openDialog.isShowing()){
                                            openDialog.dismiss();
                                        }
                                        CallCampWOEMisAPI();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel1> call, Throwable t) {
                globalclass.hideProgressDialog();
                globalclass.showcenterCustomToast(mActivity, "Failed to Upload Beneficiary Vial Photo. Please try again.",Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (camera != null){
                camera.deleteImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
