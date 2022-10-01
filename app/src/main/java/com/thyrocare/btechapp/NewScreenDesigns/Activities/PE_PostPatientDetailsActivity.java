package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.PE_PostPatientDetailsAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.SelectPeBenificiaryAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PE_PostPatientDetailsController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_PEPostCheckoutOrderResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.AddPatientResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryTestDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PE_PostPatientDetailsActivity extends AppCompatActivity {
    RecyclerView rcl_pePostCheckOutOrder;
    TextView tv_addbenDetails, tv_order_release, tv_amount;
    Button btn_arrive_proceed;
    Activity activity;
    BottomSheetDialog editPatientBottomsheet, addPatientBottomsheet;
    AppPreferenceManager appPreferenceManager;
    GetPatientListResponseModel selectedPatientModel = new GetPatientListResponseModel();
    OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
    Global globalclass;
    ConnectionDetector cd;
    Dialog CustomDialogforOTPValidation;
    ArrayList<Get_PEPostCheckoutOrderResponseModel> Pe_PatientBaseResponseModel = new ArrayList<>();
    Boolean isArrived = false;

    //TODO select patient bottomsheet views--------------------------------------------
    ImageView iv_dismiss, iv_addnewben;
    TextView tv_testname, tv_nodatafound, tv_male, tv_female;
    RelativeLayout rl_mainAddPatient, rl_addpatient;
    RecyclerView rcl_ben_list;
    LinearLayout ll_addPatient;
    TextInputEditText edt_patientname, edt_patientage;
    Button btn_addPatient, btn_submit;
    Boolean isMale =null;
    //TODO select patient bottomsheet views--------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pe_post_patient_details);
        initViews();
        initListners();
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        orderVisitDetailsModel = bundle.getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        appPreferenceManager = new AppPreferenceManager(activity);
        globalclass = new Global(activity);
        cd = new ConnectionDetector(activity);
        //callPostCheckoutPEOrderDetails_API();

        Pe_PatientBaseResponseModel = setupInitialList();
        setupPostDetailsList();

    }

    private void initListners() {
        tv_order_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_arrive_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    //TODO skipped the OTP part for debug app
                    callOrderStatusChangeApi(3, "Arrive", "", "");
                } else if (InputUtils.CheckEqualIgnoreCase(btn_arrive_proceed.getText().toString(), Constants.ARRIVED)) {
                    callOTPGenerationAPI();
                }
            }

            private void callOTPGenerationAPI() {
                SendOTPRequestModel otpRequestModel = new SendOTPRequestModel();
                otpRequestModel.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                otpRequestModel.setOrderno(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
                PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this);
                controller.callSendOTPAPI(otpRequestModel, orderVisitDetailsModel);
            }
        });
    }

    private void setupPostDetailsList() {


        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(RecyclerView.VERTICAL);
        rcl_pePostCheckOutOrder.setLayoutManager(manager);


        PE_PostPatientDetailsAdapter pe_postPatientDetailsAdapter = new PE_PostPatientDetailsAdapter(activity, Pe_PatientBaseResponseModel, new AppInterfaces.PE_postPatientDetailsAdapterClick() {
            @Override
            public void selectPatientDetailsClick() {
                if (isArrived) {
                    Toast.makeText(activity, "select patient clicked", Toast.LENGTH_SHORT).show();
                    callPostCheckoutPatientList();

                } else {
                    Toast.makeText(activity, "please mark order arrived", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void editPatientDetailsClick() {
                openEditPatientLayout(true);

            }
        });

        rcl_pePostCheckOutOrder.setAdapter(pe_postPatientDetailsAdapter);
    }

    private void openEditPatientLayout(boolean isEdit) {
        editPatientBottomsheet = new BottomSheetDialog(activity);
        addPatientBottomsheet = new BottomSheetDialog(activity);
        addPatientBottomsheet.setCancelable(true);
        addPatientBottomsheet.setContentView(R.layout.select_patient_details_btms);
        initSelectpatientBottomsheet(isEdit);
        initselectPatientListeners(isEdit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

    }

    @NonNull
    private ArrayList<Get_PEPostCheckoutOrderResponseModel> setupInitialList() {
        HashMap<String, Integer> Test_PatientMap = new HashMap<>();
        for (int i = 0; i < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
            ArrayList<BeneficiaryTestDetailsModel> benSampleType = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType();
            for (int j = 0; j < benSampleType.size(); j++) {
                String Testname = benSampleType.get(j).getTests();
                if (Test_PatientMap.containsKey(Testname)) {
                    Test_PatientMap.put(Testname, Test_PatientMap.get(Testname) + 1);
                } else {
                    Test_PatientMap.put(Testname, 1);
                }
            }
        }
        ArrayList<Get_PEPostCheckoutOrderResponseModel> responseModel = new ArrayList<>();
        Get_PEPostCheckoutOrderResponseModel model;

        for (Map.Entry<String, Integer> entry : Test_PatientMap.entrySet()) {
            model = new Get_PEPostCheckoutOrderResponseModel();
            model.setTestName(entry.getKey());
            model.setPatientCount(entry.getValue());
            responseModel.add(model);
        }
        return responseModel;
    }

    private void callPostCheckoutPatientList() {
        PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this, new AppInterfaces.getBenList() {
            @Override
            public void getBeneficiaryList(GetPatientListResponseModel responseModel) {
                showSelectPatientDetailsLayout(responseModel);
            }
        });
        controller.callPostcheckoutDetails(orderVisitDetailsModel.getVisitId());
    }


    private void showSelectPatientDetailsLayout(GetPatientListResponseModel responseModel) {
        addPatientBottomsheet = new BottomSheetDialog(activity);
        addPatientBottomsheet.setCancelable(true);
        addPatientBottomsheet.setContentView(R.layout.select_patient_details_btms);
        initSelectpatientBottomsheet(false);
        initselectPatientListeners(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcl_ben_list.setLayoutManager(layoutManager);

        SelectPeBenificiaryAdapter adapter = new SelectPeBenificiaryAdapter(responseModel, activity, new AppInterfaces.PatientSelector() {
            @Override
            public void addPatient(GetPatientListResponseModel addpatientModel) {
                selectedPatientModel = addpatientModel;
            }

            @Override
            public void editPatient( GetPatientListResponseModel.patients editingPatientData) {
                showEditPatientDetailsLayout(editingPatientData);
            }

        });
        rcl_ben_list.setAdapter(adapter);


        addPatientBottomsheet.show();
    }

    private void showEditPatientDetailsLayout(GetPatientListResponseModel.patients editingPatientData) {
        if (addPatientBottomsheet.isShowing()){
           initSelectpatientBottomsheet(true);
        }
    }

    private void initselectPatientListeners(boolean isEdit) {
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatientBottomsheet.dismiss();
            }
        });

        iv_addnewben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcl_ben_list.setVisibility(View.GONE);
                ll_addPatient.setVisibility(View.VISIBLE);
                iv_addnewben.setVisibility(View.GONE);
            }
        });
        btn_addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit && validateNewBen()) {
                    //TODO add new patient
                    callAddNewPEPatientAPI();

                } else if (isEdit && validateNewBen()) {
                    //TODO edit patient

                }

            }

            private boolean validateNewBen() {
                if (InputUtils.isNull(edt_patientname.getText().toString().trim())) {
                    return false;
                }
                if (Integer.parseInt(edt_patientage.getText().toString()) > 150 || Integer.parseInt(edt_patientage.getText().toString()) == 0) {
                    return false;
                }
                return true;
            }
        });

        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_male.setBackground(getResources().getDrawable(R.drawable.pe_selectedtext_bg));
                tv_male.setTextColor(getResources().getColor(R.color.white));

                tv_female.setBackground(getResources().getDrawable(R.drawable.pe_text_background));
                tv_female.setTextColor(getResources().getColor(R.color.black));
                isMale = true;
            }
        });

        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_female.setBackground(getResources().getDrawable(R.drawable.pe_selectedtext_bg));
                tv_female.setTextColor(getResources().getColor(R.color.white));
                tv_male.setBackground(getResources().getDrawable(R.drawable.pe_text_background));
                tv_male.setTextColor(getResources().getColor(R.color.black));
                isMale = false;
            }
        });
    }

    private void callAddNewPEPatientAPI() {
        try {
            if (cd.isConnectingToInternet()) {
                JSONObject jsonObject = new JSONObject();
                int patientAge = Integer.valueOf(edt_patientage.getText().toString());

                jsonObject.put("name", edt_patientname.getText());
                jsonObject.put("age", Integer.valueOf(String.valueOf(edt_patientage.getText())));
                jsonObject.put("gender", isMale ? "MALE" : "FEMALE");

                PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this);
                controller.callAddPatient(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initSelectpatientBottomsheet(boolean isEdit) {
        iv_dismiss = addPatientBottomsheet.findViewById(R.id.iv_dismiss);
        iv_addnewben = addPatientBottomsheet.findViewById(R.id.iv_addnewben);
        tv_testname = addPatientBottomsheet.findViewById(R.id.tv_testname);
        tv_nodatafound = addPatientBottomsheet.findViewById(R.id.tv_nodatafound);
        tv_male = addPatientBottomsheet.findViewById(R.id.tv_male);
        tv_female = addPatientBottomsheet.findViewById(R.id.tv_female);
        rl_mainAddPatient = addPatientBottomsheet.findViewById(R.id.rl_mainAddPatient);
        rl_addpatient = addPatientBottomsheet.findViewById(R.id.rl_addpatient);
        rcl_ben_list = addPatientBottomsheet.findViewById(R.id.rcl_ben_list);
        ll_addPatient = addPatientBottomsheet.findViewById(R.id.ll_addPatient);
        edt_patientname = addPatientBottomsheet.findViewById(R.id.edt_patientname);
        edt_patientage = addPatientBottomsheet.findViewById(R.id.edt_patientage);
        btn_addPatient = addPatientBottomsheet.findViewById(R.id.btn_addPatient);
        btn_submit = addPatientBottomsheet.findViewById(R.id.btn_submit);
        if (isEdit) {
            rl_addpatient.setVisibility(View.GONE);
            ll_addPatient.setVisibility(View.VISIBLE);
        }


    }


    private void initViews() {
        activity = PE_PostPatientDetailsActivity.this;
        rcl_pePostCheckOutOrder = findViewById(R.id.rcl_pePostCheckOutOrder);
        tv_addbenDetails = findViewById(R.id.tv_addbenDetails);
        tv_order_release = findViewById(R.id.tv_order_release);
        tv_amount = findViewById(R.id.tv_amount);
        btn_arrive_proceed = findViewById(R.id.btn_arrive_proceed);
        rcl_pePostCheckOutOrder.setClickable(false);
        tv_addbenDetails.setVisibility(View.GONE);
    }

    public void ShowDialogToVerifyOTP(OrderVisitDetailsModel orderVisitDetailsModel) {
        {

            CustomDialogforOTPValidation = new Dialog(activity);
            CustomDialogforOTPValidation.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            CustomDialogforOTPValidation.requestWindowFeature(Window.FEATURE_NO_TITLE);
            CustomDialogforOTPValidation.setContentView(R.layout.validate_otp_dialog);
            CustomDialogforOTPValidation.setCancelable(false);
            CustomDialogforOTPValidation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            RelativeLayout rel_main = (RelativeLayout) CustomDialogforOTPValidation.findViewById(R.id.rel_main);
            TextView tv_header = (TextView) CustomDialogforOTPValidation.findViewById(R.id.tv_header);
            ImageView img_btn_validateOTP = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_btn_validateOTP);
            ImageView img_close = (ImageView) CustomDialogforOTPValidation.findViewById(R.id.img_close);
            final EditText edt_OTP = (EditText) CustomDialogforOTPValidation.findViewById(R.id.edt_OTP);

            tv_header.setText("Enter OTP send to mobile number linked with this order, to proceed with WOE.");

            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


            int height = 0, width = 0;
            if (displayMetrics != null) {
                try {
                    height = displayMetrics.heightPixels;
                    width = displayMetrics.widthPixels;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
            rel_main.setLayoutParams(lp);

            CustomDialogforOTPValidation.show();

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialogforOTPValidation.dismiss();
                }
            });


            img_btn_validateOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String strOTP = edt_OTP.getText().toString().trim();
                    if (!InputUtils.isNull(strOTP) && strOTP.length() != 4) {
                        globalclass.showalert_OK("Please enter valid OTP. Length required : 4", activity);
                        edt_OTP.requestFocus();
                    } else {
                        OrderPassRequestModel model = new OrderPassRequestModel();
                        model.setMobile(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                        model.setOTP(strOTP);
                        model.setVisitId(orderVisitDetailsModel.getAllOrderdetails().get(0).getVisitId());
//                    if (BundleConstants.isPEPartner || BundleConstants.PEDSAOrder) {
                        if (appPreferenceManager.isPEPartner() || appPreferenceManager.PEDSAOrder()) {
                            model.setOTPStatus(BundleConstants.PE_OTP_ARRIVED);
                        }
                        if (cd.isConnectingToInternet()) {
                            CallValidateOTPAPI(model);
                        } else {
                            globalclass.showCustomToast(activity, activity.getResources().getString(R.string.plz_chk_internet));
                        }
                    }

                }
            });

        }
    }

    private void CallValidateOTPAPI(OrderPassRequestModel model) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallValidateOTPAPI(model);
        globalclass.showProgressDialog(activity, "Validating OTP. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    String strresponse = response.body();
                    if (!TextUtils.isEmpty(strresponse) && strresponse.toUpperCase().contains("SUCCESS")) {
                        CustomDialogforOTPValidation.dismiss();
                        Toast.makeText(activity, "OTP Validated Successfully.", Toast.LENGTH_SHORT).show();
//                        globalclass.showCustomToast(mActivity, "OTP Validated Successfully.");
                        callOrderStatusChangeApi(3, "Arrive", "", "");
                        BundleConstants.callOTPFlag = 1;

                    } else {
                        globalclass.showCustomToast(activity, "Invalid OTP.");
                    }
                } else if (response.code() == 401) {
                    globalclass.showCustomToast(activity, "Invalid OTP.");
                } else {
                    globalclass.showCustomToast(activity, MSG_SERVER_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(activity);
                globalclass.showCustomToast(activity, MSG_SERVER_EXCEPTION);
            }
        });

    }

    private void callOrderStatusChangeApi(int status, final String strButton, String remarks, String date) {
        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
        orderStatusChangeRequestModel.setStatus(status);
        if (!InputUtils.isNull(remarks)) {
            orderStatusChangeRequestModel.setRemarks(remarks);
        }
        if (!InputUtils.isNull(date)) {
            orderStatusChangeRequestModel.setAppointmentDate(date);
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        globalclass.showProgressDialog(activity, getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalclass.hideProgressDialog(activity);
                if (response.code() == 200 || response.code() == 204 && response.body() != null) {
                    onOrderStatusChangedResponseReceived(response.body());
                } else {
                    try {
                        Toast.makeText(activity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onOrderStatusChangedResponseReceived(String msg) {

        tv_addbenDetails.setVisibility(View.VISIBLE);
        btn_arrive_proceed.setText(Constants.CONFIRM_ORDER);
        rcl_pePostCheckOutOrder.setClickable(true);
        isArrived = true;
    }

    public void onAddPatientResponseReceived(AddPatientResponseModel addPatientResponseModel) {
        if (addPatientBottomsheet.isShowing()){
            addPatientBottomsheet.dismiss();
        }
        callPostCheckoutPatientList();
    }
}