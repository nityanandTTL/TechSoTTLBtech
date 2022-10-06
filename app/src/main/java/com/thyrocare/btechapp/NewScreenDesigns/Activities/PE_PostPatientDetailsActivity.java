package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.AppConstants.MSG_SERVER_EXCEPTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.PE_PostPatientDetailsAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.SelectPeBenificiaryAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PE_PostPatientDetailsController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.AddEditPatientModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.ConfirmOrderRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_PEPostCheckoutOrderResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.response.AddPatientResponseModel;
import com.thyrocare.btechapp.models.api.response.ConfirmOrderResponseModel;
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
    GetPatientListResponseModel.DataDTO editingPatientModel = new GetPatientListResponseModel.DataDTO();
    GetPatientListResponseModel patientListResponse = new GetPatientListResponseModel();
    OrderVisitDetailsModel orderVisitDetailsModel = new OrderVisitDetailsModel();
    Global globalclass;
    ConnectionDetector cd;
    Dialog CustomDialogforOTPValidation;
    ArrayList<Get_PEPostCheckoutOrderResponseModel> Pe_PatientBaseResponseModel = new ArrayList<>();
    Boolean isArrived = false;
    SelectPeBenificiaryAdapter selectPeBenificiaryAdapter;
    PE_PostPatientDetailsAdapter pe_postPatientDetailsAdapter;
    ConfirmOrderRequestModel confirmOrderRequestModel = new ConfirmOrderRequestModel();
    ArrayList<intialListModelClass> patientMapModel = new ArrayList<>();
    boolean patientListEdit = false;
    int postionToAddTest = 0, positionOfTest = 0;

    //TODO select patient bottomsheet views--------------------------------------------
    ImageView iv_dismiss, iv_addnewben;
    TextView tv_testname, tv_nodatafound, tv_male, tv_female, tv_select_editPatient;
    RelativeLayout rl_mainAddPatient, rl_addpatient;
    RecyclerView rcl_ben_list;
    LinearLayout ll_addPatient;
    TextInputEditText edt_patientname, edt_patientage;
    Button btn_addPatient, btn_submit;
    Boolean isMale = null;
    private int editListPosition;
    private int baseModelPosition;
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
                if (!isArrived) {
                    callOTPGenerationAPI();
                } else {
                    callConfirmOrderAPI();
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

    private void callConfirmOrderAPI() {
        if (validateBaseModel(Pe_PatientBaseResponseModel)) {


            for (int i = 0; i < Pe_PatientBaseResponseModel.size(); i++) {
                ConfirmOrderRequestModel.PatientsDTO.ProductsDTO singleTestData = new ConfirmOrderRequestModel.PatientsDTO.ProductsDTO();
                singleTestData.setDos_code(Pe_PatientBaseResponseModel.get(i).getTestName());
                singleTestData.setLab_dos_name(Pe_PatientBaseResponseModel.get(i).getTestName());
                singleTestData.setType(Pe_PatientBaseResponseModel.get(i).getTesttype());
                singleTestData.setPackageId(Pe_PatientBaseResponseModel.get(i).getProjectID());

                for (int j = 0; j < Pe_PatientBaseResponseModel.get(i).getAddedPatients().size(); j++) {
                    if (patientExist(Pe_PatientBaseResponseModel.get(i).getAddedPatients().get(j).getId())) {
                        confirmOrderRequestModel.getPatients().get(postionToAddTest).addProducts(singleTestData);
                    } else {
                        ConfirmOrderRequestModel.PatientsDTO singlePatientData = new ConfirmOrderRequestModel.PatientsDTO();
                        singlePatientData.setAge(String.valueOf(Pe_PatientBaseResponseModel.get(i).getAddedPatients().get(j).getAge()));
                        singlePatientData.setName(Pe_PatientBaseResponseModel.get(i).getAddedPatients().get(j).getName());
                        singlePatientData.setGender(Pe_PatientBaseResponseModel.get(i).getAddedPatients().get(j).getGender() == 1 ? "M" : "F");
                        singlePatientData.setLeadId(String.valueOf(Pe_PatientBaseResponseModel.get(i).getAddedPatients().get(j).getId()));
                        singlePatientData.addProducts(singleTestData);
                        confirmOrderRequestModel.addPatient(singlePatientData);
                    }

                }
            }
            confirmOrderRequestModel.setOrderId(orderVisitDetailsModel.getAllOrderdetails().get(0).getOrderNo());
            confirmOrderRequestModel.setPhleboId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
            Global.sout("confirm order request model>>>>>\n", new Gson().toJson(confirmOrderRequestModel));

            PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this);
            controller.callConfirmOrderAPI(confirmOrderRequestModel);

        }

    }

    private boolean patientExist(int singlePatientID) {
        for (int i = 0; i < confirmOrderRequestModel.getPatients().size(); i++) {
            if (confirmOrderRequestModel.getPatients().get(i).getLeadId().equals(String.valueOf(singlePatientID))) {
                postionToAddTest = i;
                return true;
            }
        }
        return false;
    }

    private boolean validateBaseModel(ArrayList<Get_PEPostCheckoutOrderResponseModel> pe_patientBaseResponseModel) {
        for (int i = 0; i < pe_patientBaseResponseModel.size(); i++) {
            if (pe_patientBaseResponseModel.get(i).getAddedPatients() == null) {
                Toast.makeText(activity, "Please fill all beneficiary data", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void setupPostDetailsList() {
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(RecyclerView.VERTICAL);
        rcl_pePostCheckOutOrder.setLayoutManager(manager);
        pe_postPatientDetailsAdapter = new PE_PostPatientDetailsAdapter(activity, Pe_PatientBaseResponseModel, new AppInterfaces.PE_postPatientDetailsAdapterClick() {
            @Override
            public void selectPatientDetailsClick(int baseModelPostion) {// TODO the position was taken here to set base model data in case of edit selected patient List.
                baseModelPosition = baseModelPostion;
                if (isArrived) {
                    Toast.makeText(activity, "select patient clicked", Toast.LENGTH_SHORT).show();
                    callPostCheckoutPatientList();

                } else {
                    Toast.makeText(activity, "please mark order arrived", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void editPatientDetailsClick(int position) {
                baseModelPosition = position;
                callPostCheckoutPatientList();
                //TODO open the select patient list with added patient having the checkbox clicked
            }
        });

        rcl_pePostCheckOutOrder.setAdapter(pe_postPatientDetailsAdapter);
    }

    @NonNull
    private ArrayList<Get_PEPostCheckoutOrderResponseModel> setupInitialList() {
        HashMap<String, Integer> Test_PatientMap = new HashMap<>();

        for (int i = 0; i < orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().size(); i++) {
            ArrayList<BeneficiaryTestDetailsModel> benSampleType = orderVisitDetailsModel.getAllOrderdetails().get(0).getBenMaster().get(i).getTestSampleType();
            for (int j = 0; j < benSampleType.size(); j++) {
                String Testname = benSampleType.get(j).getTests();
                if (testExist(benSampleType.get(j).getTests())) {
                    patientMapModel.get(positionOfTest).setCount(patientMapModel.get(positionOfTest).getCount() + 1);

                } else {
                    intialListModelClass modelClass = new intialListModelClass();
                    modelClass.setCount(1);
                    modelClass.setProjID(benSampleType.get(j).getProjId());
                    modelClass.setTestname(benSampleType.get(j).getTests());
                    modelClass.setTestType(benSampleType.get(j).getTestType());
                    patientMapModel.add(modelClass);
                }
            }
        }
        ArrayList<Get_PEPostCheckoutOrderResponseModel> responseModel = new ArrayList<>();
        Get_PEPostCheckoutOrderResponseModel model;

        for (int i = 0; i < patientMapModel.size(); i++) {
            model = new Get_PEPostCheckoutOrderResponseModel();
            model.setTestName(patientMapModel.get(i).getTestname());
            model.setTesttype(patientMapModel.get(i).getTestType());
            model.setProjectID(patientMapModel.get(i).getProjID());
            model.setPatientCount(patientMapModel.get(i).getCount());
            responseModel.add(model);
        }
        return responseModel;
    }

    private boolean testExist(String tests) {
        for (int i = 0; i < patientMapModel.size(); i++) {
            if (patientMapModel.get(i).getTestname().contains(tests)) {
                positionOfTest = i;
                return true;
            }
        }

        return false;
    }

    private void callPostCheckoutPatientList() {
        PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this, new AppInterfaces.getBenList() {
            @Override
            public void getBeneficiaryList(GetPatientListResponseModel responseModel) {
                patientListResponse = responseModel;
                if (Pe_PatientBaseResponseModel.get(baseModelPosition).getPatientDetails() != null) {
                    for (int i = 0; i < Pe_PatientBaseResponseModel.get(baseModelPosition).getAddedPatients().size(); i++) {
                        for (int j = 0; j < patientListResponse.getData().size(); j++) {
                            if (patientListResponse.getData().get(j).getId() == Pe_PatientBaseResponseModel.get(baseModelPosition).getAddedPatients().get(i).getId()) {
                                patientListResponse.getData().get(j).setSelected(true);
                            }
                        }
                    }
                    patientListEdit = true;
                }
                showSelectPatientDetailsLayout(patientListResponse);
            }
        });
        controller.callPostcheckoutDetails(orderVisitDetailsModel.getVisitId());
    }


    private void showSelectPatientDetailsLayout(GetPatientListResponseModel patientListResponseModel) {
        addPatientBottomsheet = new BottomSheetDialog(activity);
        addPatientBottomsheet.setCancelable(true);
        addPatientBottomsheet.setContentView(R.layout.select_patient_details_btms);
        initSelectpatientBottomsheet(false, baseModelPosition);
        initselectPatientListeners(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcl_ben_list.setLayoutManager(layoutManager);
        rcl_ben_list.setVisibility(View.VISIBLE);

        selectPeBenificiaryAdapter = new SelectPeBenificiaryAdapter(patientListEdit, Pe_PatientBaseResponseModel.get(baseModelPosition).getPatientCount(), patientListResponseModel, activity, new AppInterfaces.PatientSelector() {
            @Override
            public void addPatient(int addPatientPosition) {
                patientListResponse.getData().get(addPatientPosition).setSelected(true);
            }

            @Override
            public void removePatient(int removePatientPostion) {
                patientListResponse.getData().get(removePatientPostion).setSelected(false);
            }

            @Override
            public void editPatient(GetPatientListResponseModel.DataDTO singlePatient) {
                editingPatientModel = singlePatient;
                showEditPatientDetailsLayout();
            }

        });
        rcl_ben_list.setAdapter(selectPeBenificiaryAdapter);
        addPatientBottomsheet.show();
    }

    private void showEditPatientDetailsLayout() {
        if (addPatientBottomsheet.isShowing()) {
            initSelectpatientBottomsheet(true, baseModelPosition);
            initselectPatientListeners(true);
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
                    calleditPatientAPI();
                }

            }

            private boolean validateNewBen() {
                if (InputUtils.isNull(edt_patientname.getText().toString().trim())) {
                    Toast.makeText(activity, "Please enter name", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Integer.parseInt(edt_patientage.getText().toString()) > 150 || Integer.parseInt(edt_patientage.getText().toString()) == 0 || edt_patientage.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please enter age", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (isMale == null) {
                    Toast.makeText(activity, "Please select gender", Toast.LENGTH_SHORT).show();
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /*selectedPatientModel = patientListResponse;*/
                    ArrayList<GetPatientListResponseModel.DataDTO> testArray = new ArrayList<>();
                    for (int i = 0; i < patientListResponse.getData().size(); i++) {
                        if (patientListResponse.getData().get(i).isSelected()) {
                            testArray.add(patientListResponse.getData().get(i));
                        }
                    }
                    selectedPatientModel.setData(testArray);
                    if (Pe_PatientBaseResponseModel.get(baseModelPosition).getPatientCount() >= selectedPatientModel.getData().size()) {
                        addPatientBottomsheet.dismiss();
                        Global.sout("selected Patient mode>>>>>>>>>>>>>>>", selectedPatientModel);
                        Pe_PatientBaseResponseModel.get(baseModelPosition).setAddedPatients(selectedPatientModel.getData());
                        Global.sout("selected Patient mode>>>>>>>>>>>>>>>", Pe_PatientBaseResponseModel);
                        pe_postPatientDetailsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity, "Please select " + Pe_PatientBaseResponseModel.get(baseModelPosition).getPatientCount() + " or less patients", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Global.sout("btn_submit_exception", e.getLocalizedMessage());
                }

            }
        });
    }

    private void calleditPatientAPI() {
        try {
            if (cd.isConnectingToInternet()) {
                AddEditPatientModel model = new AddEditPatientModel();
                model.setName(edt_patientname.getText().toString());
                model.setAge(Integer.valueOf(edt_patientage.getText().toString()));
                model.setGender(isMale ? 1 : 2);
                model.setId(editingPatientModel.getId());

                PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this);
                controller.callEditPatient(model, orderVisitDetailsModel.getVisitId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAddNewPEPatientAPI() {
        try {
            if (cd.isConnectingToInternet()) {
                AddEditPatientModel model = new AddEditPatientModel();

                model.setName(edt_patientname.getText().toString());
                model.setAge(Integer.valueOf(edt_patientage.getText().toString()));
                model.setGender(isMale ? 1 : 2);

                PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this);
                controller.callAddPatient(model, orderVisitDetailsModel.getVisitId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initSelectpatientBottomsheet(boolean isEdit, int baseModelPosition) {
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
        tv_select_editPatient = addPatientBottomsheet.findViewById(R.id.tv_select_editPatient);

        tv_testname.setText(Pe_PatientBaseResponseModel.get(baseModelPosition).getTestName());

        if (isEdit) {
            rcl_ben_list.setVisibility(View.GONE);
            rl_addpatient.setVisibility(View.GONE);
            ll_addPatient.setVisibility(View.VISIBLE);
            edt_patientname.setText(editingPatientModel.getName());
            edt_patientage.setText(String.valueOf(editingPatientModel.getAge()));
            isMale = editingPatientModel.getGender() == 1;
            tv_select_editPatient.setText("Edit Patients");

            if (isMale) {
                tv_male.setBackground(getResources().getDrawable(R.drawable.pe_selectedtext_bg));
                tv_male.setTextColor(getResources().getColor(R.color.white));
                tv_female.setBackground(getResources().getDrawable(R.drawable.pe_text_background));
                tv_female.setTextColor(getResources().getColor(R.color.black));
            } else {
                tv_female.setBackground(getResources().getDrawable(R.drawable.pe_selectedtext_bg));
                tv_female.setTextColor(getResources().getColor(R.color.white));
                tv_male.setBackground(getResources().getDrawable(R.drawable.pe_text_background));
                tv_male.setTextColor(getResources().getColor(R.color.black));
            }
        } else {
            btn_submit.setVisibility(View.VISIBLE);
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
        if (addPatientBottomsheet.isShowing()) {
            addPatientBottomsheet.dismiss();
        }
        if (!addPatientResponseModel.getStatus()) {
            callPostCheckoutPatientList();
        } else {
            TastyToast.makeText(activity, SomethingWentwrngMsg, TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
        }

    }

    public void onEditPatientResponse(AddPatientResponseModel addPatientResponseModel) {
        if (addPatientBottomsheet.isShowing())
            addPatientBottomsheet.dismiss();
        if (addPatientResponseModel.getData() != null) {
            callPostCheckoutPatientList();
        }
    }

    public void onConfirmOrderResponseReceived(ConfirmOrderResponseModel confirmOrderResponseModel) {
        if (confirmOrderResponseModel.isStatus()){
            startActivity(new Intent(PE_PostPatientDetailsActivity.this, StartAndArriveActivity.class));
        }else{
            TastyToast.makeText(activity, SomethingWentwrngMsg, TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
        }
    }

    public class intialListModelClass {
        String testname, projID, testType;
        int count;

        public String getTestname() {
            return testname;
        }

        public void setTestname(String testname) {
            this.testname = testname;
        }

        public String getProjID() {
            return projID;
        }

        public void setProjID(String projID) {
            this.projID = projID;
        }

        public String getTestType() {
            return testType;
        }

        public void setTestType(String testType) {
            this.testType = testType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}