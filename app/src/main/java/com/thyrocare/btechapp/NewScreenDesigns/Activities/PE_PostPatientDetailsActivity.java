package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.thyrocare.btechapp.BtechInterfaces.AppInterfaces;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.PE_PostPatientDetailsAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.SelectPeBenificiaryAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Controllers.PE_PostPatientDetailsController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_PEPostCheckoutOrderResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.OrderDetailsModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.request.GetPE_PostCheckOutPatientModel;
import com.thyrocare.btechapp.utils.app.InputUtils;

public class PE_PostPatientDetailsActivity extends AppCompatActivity {
    RecyclerView rcl_pePostCheckOutOrder;
    TextView tv_addbenDetails, tv_order_release, tv_amount;
    Button btn_arrive_proceed;
    Activity activity;
    BottomSheetDialog editPatientBottomsheet, addPatientBottomsheet;
    GetPE_PostCheckOutPatientModel selectedPatientModel = new GetPE_PostCheckOutPatientModel();
    OrderDetailsModel orderDetailsModel =  new OrderDetailsModel();

    //TODO select patient bottomsheet views--------------------------------------------
    ImageView iv_dismiss, iv_addnewben;
    TextView tv_testname, tv_nodatafound, tv_male, tv_female, tv_other;
    RelativeLayout rl_mainAddPatient, rl_addpatient;
    RecyclerView rcl_ben_list;
    LinearLayout ll_addPatient;
    TextInputEditText edt_patientname, edt_patientage;
    Button btn_addPatient, btn_submit;
    //TODO select patient bottomsheet views--------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pe_post_patient_details);
        initViews();
        initListners();
        orderDetailsModel= getIntent().getParcelableExtra(Constants.ORDER_DETAILS_MODEL);
        //callPostCheckoutPEOrderDetails_API();

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
                if (InputUtils.CheckEqualIgnoreCase(btn_arrive_proceed.getText().toString(), Constants.ARRIVED)){
                    callOTPGenerationAPI();

                }
            }

            private void callOTPGenerationAPI() {

            }
        });
    }

    private void callPostCheckoutPEOrderDetails_API() {
        PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this, new AppInterfaces.getPostPatientDetailsResponse() {
            @Override
            public void getPostPatientResponse(Get_PEPostCheckoutOrderResponseModel get_pePostCheckoutOrderResponseModel) {
                setupPostDetailsList(get_pePostCheckoutOrderResponseModel);

            }
        });
        controller.callPostcheckoutDetails();
    }

    private void setupPostDetailsList(Get_PEPostCheckoutOrderResponseModel get_pePostCheckoutOrderResponseModel) {
        PE_PostPatientDetailsAdapter pe_postPatientDetailsAdapter = new PE_PostPatientDetailsAdapter(activity, new AppInterfaces.PE_postPatientDetailsAdapterClick() {
            @Override
            public void selectPatientDetailsClick() {
                //TODO add conditions first
                callPostCheckoutPatientList(/*orderNo*/);
                //showSelectPatientDetailsLayout(/*need some data*/);
            }

            @Override
            public void editPatientDetailsClick() {
                //TODO add conditions first
                showEditPatientDetailsLayout(/*needs some data*/);
            }
        });
        rcl_pePostCheckOutOrder.setAdapter(pe_postPatientDetailsAdapter);
    }

    private void callPostCheckoutPatientList() {
        PE_PostPatientDetailsController controller = new PE_PostPatientDetailsController(PE_PostPatientDetailsActivity.this, new AppInterfaces.getBenList() {
            @Override
            public void getBeneficiaryList(GetPE_PostCheckOutPatientModel responseModel) {
                showSelectPatientDetailsLayout(responseModel);
            }
        });
    }

    private void showEditPatientDetailsLayout() {
        editPatientBottomsheet = new BottomSheetDialog(activity);
    }

    private void showSelectPatientDetailsLayout(GetPE_PostCheckOutPatientModel responseModel) {
        addPatientBottomsheet = new BottomSheetDialog(activity);
        addPatientBottomsheet.setCancelable(true);
        addPatientBottomsheet.setContentView(R.layout.select_patient_details_btms);
        initSelectpatientBottomsheet();
        initselectPatientListeners();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcl_ben_list.setLayoutManager(layoutManager);

        SelectPeBenificiaryAdapter adapter=  new SelectPeBenificiaryAdapter(responseModel, activity, new AppInterfaces.PatientSelector() {
            @Override
            public void addPatient(GetPE_PostCheckOutPatientModel addpatientModel) {
                selectedPatientModel=addpatientModel;
            }

        });
        //override <= global class
        rcl_ben_list.setAdapter(adapter);




        if (responseModel.getPatientDetailsList().isEmpty()) {

        }


        addPatientBottomsheet.show();
    }

    private void initselectPatientListeners() {
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
                if (validateNewBen()) ;
            }

            private boolean validateNewBen() {
                if (InputUtils.isNull(edt_patientname.getText().toString().trim())) {
                    return false;
                }
                if (Integer.parseInt(edt_patientage.getText().toString()) > 150 ||Integer.parseInt(edt_patientage.getText().toString()) ==0){
                    return false;
                }
                    return true;
            }
        });

    }

    private void initSelectpatientBottomsheet() {
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
    }


    private void initViews() {
        activity = PE_PostPatientDetailsActivity.this;
        rcl_pePostCheckOutOrder = findViewById(R.id.rcl_pePostCheckOutOrder);
        tv_addbenDetails = findViewById(R.id.tv_addbenDetails);
        tv_order_release = findViewById(R.id.tv_order_release);
        tv_amount = findViewById(R.id.tv_amount);
        btn_arrive_proceed = findViewById(R.id.btn_arrive_proceed);
    }
}