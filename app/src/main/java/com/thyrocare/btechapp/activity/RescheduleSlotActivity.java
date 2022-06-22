package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.OrderReleaseRemarksController;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.B2BVisitOrdersDisplayFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FixAppointmentDataModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.SlotAppointmentTimeAdapter;
import com.thyrocare.btechapp.adapter.SlotsDateAdapter;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.PEBenWiseApptSlotRequestModel;
import com.thyrocare.btechapp.models.api.request.SevenDaysModel;
import com.thyrocare.btechapp.models.api.response.GetPEBtechSlotResponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CheckInternetConnectionMsg;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

public class RescheduleSlotActivity extends AppCompatActivity {
    RecyclerView rec_dateList, rec_slotList;
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    ArrayList<SevenDaysModel> arrayList = new ArrayList<>();
    ImageView home, back;
    ConnectionDetector cd;
    Activity activity;
    AppPreferenceManager appPreferenceManager;
    String orderno, pincode, token, date, slot2, appoinmentdate;
    String slot1, response;
    int SlotID, remarks, slotID, remarksID,count;
    Button btn_submit, btn_back;
    CheckBox chk_slot;
    Global globalClass;
    LinearLayout ll_slotList;
    TextView tv_toolbar;
    ArrayList<OrderVisitDetailsModel> orderVisitDetailsModelArrayList = new ArrayList<>();
    OrderVisitDetailsModel orderDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_slot);
        initUI();
        initlisteners();
    }

    private void initlisteners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chk_slot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_slotList.setVisibility(View.GONE);
                } else {
                    ll_slotList.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDetails()) {
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage("Are you sure you want to reschedule order!")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (chk_slot.isChecked()) {
                                        toReleaseOrder();
                                    } else {
                                        toRescheduleOrder();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private boolean checkDetails() {
        if (chk_slot.isChecked()) {
            return true;
        }
        if (InputUtils.isNull(date)) {
            Toast.makeText(activity, "Kindly select appointment date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (InputUtils.isNull(slot1)) {
            Toast.makeText(activity, "Kindly select appointment slot", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toReleaseOrder() {
        if (cd.isConnectingToInternet()) {
            callOrderStatusChangeApi(27);
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void toRescheduleOrder() {
        FixAppointmentDataModel dataModel = new FixAppointmentDataModel();
        dataModel.setBTechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        dataModel.setOrderNo(orderno);
        dataModel.setAppointmentDate(slot2);
        dataModel.setAppointmentSlot(SlotID);
        dataModel.setStatus(11);
        dataModel.setRemarksId(Global.selectedRemarksID);
        dataModel.setReasonId(remarks);
        dataModel.setVisitId(orderno);
        dataModel.setOthers(response);
        dataModel.setUserId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        dataModel.setVipOrder(false);
        callSubmitReleaseAPI(dataModel);
    }

    private void callOrderStatusChangeApi(int i) {

        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(slotID + "");
        orderStatusChangeRequestModel.setStatus(i);
        orderStatusChangeRequestModel.setReasonId(remarks);
        orderStatusChangeRequestModel.setRemarksId(Global.selectedRemarksID);
        orderStatusChangeRequestModel.setRemarks(response);
        orderStatusChangeRequestModel.setAppointmentDate(DateUtils.Req_Date_Req(appoinmentdate, "dd-MM-yyyy", "yyyy-MM-dd"));

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        globalClass.showProgressDialog(activity, getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                globalClass.hideProgressDialog(activity);
                if (response.code() == 200 || response.code() == 204) {
                    onOrderStatusChangedResponseReceived();
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
                globalClass.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onOrderStatusChangedResponseReceived() {
        TastyToast.makeText(activity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        Intent intent = new Intent(this, B2BVisitOrdersDisplayFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void callSubmitReleaseAPI(FixAppointmentDataModel dataModel) {
        if (cd.isConnectingToInternet()) {
            OrderReleaseRemarksController ordc = new OrderReleaseRemarksController(this);
            ordc.updateOrderHistory(0, token, dataModel);
        } else {
            Toast.makeText(activity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        activity = this;
        appPreferenceManager = new AppPreferenceManager(this);
        globalClass = new Global(this);
        cd = new ConnectionDetector(this);
        home = findViewById(R.id.iv_home);
        back = findViewById(R.id.iv_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Select Slot");
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        home.setVisibility(View.GONE);
        rec_dateList = findViewById(R.id.rec_dateList);
        rec_slotList = findViewById(R.id.rec_slotList);
        btn_submit = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.btn_back);
        chk_slot = findViewById(R.id.chk_slot);
        ll_slotList = findViewById(R.id.ll_slotList);
        arrayList = CommonUtils.getSevenDays();
        orderno = getIntent().getStringExtra(BundleConstants.ORDER);
        pincode = getIntent().getStringExtra(BundleConstants.PINCODE);
        remarks = getIntent().getIntExtra(BundleConstants.RES_REMARKS, 0);
        remarksID = getIntent().getIntExtra(BundleConstants.REMARKS_ID, 0);
        response = getIntent().getStringExtra(BundleConstants.RES_RESPONSE);
        appoinmentdate = getIntent().getStringExtra(BundleConstants.APPOINTMENT_DATE);
        slotID = getIntent().getIntExtra(BundleConstants.ORDER_SLOTID, 0);
        orderVisitDetailsModelArrayList = getIntent().getExtras().getParcelableArrayList(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        orderDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        count = getIntent().getIntExtra("Bencount",0);
        SlotsDateAdapter sdadapter = new SlotsDateAdapter(this, arrayList);
        rec_dateList.setAdapter(sdadapter);
        chk_slot.setChecked(false);
        token = "Bearer " + appPreferenceManager.getAccess_Token();
    }

    public void dateforappointmentSlot(String fulldate) {
        if (cd.isConnectingToInternet()) {
            date = fulldate;

            //New PE Slot API Mith
            /*PEBenWiseApptSlotRequestModel peBenWiseApptSlotRequestModel = new PEBenWiseApptSlotRequestModel();
            int benCount = orderVisitDetailsModelArrayList.get(0).getAllOrderdetails().get(0).getBenMaster().size();
            String product = orderVisitDetailsModelArrayList.get(0).getAllOrderdetails().get(0).getBenMaster().get(0).getTestsCode();
            peBenWiseApptSlotRequestModel.setBenCount(benCount);
            peBenWiseApptSlotRequestModel.setDate(fulldate);
            peBenWiseApptSlotRequestModel.setPincode(pincode);
            peBenWiseApptSlotRequestModel.setProjectId("NA");
            peBenWiseApptSlotRequestModel.setApp("BTECH");
            peBenWiseApptSlotRequestModel.setProduct(product);

            OrderReleaseRemarksController or = new OrderReleaseRemarksController(this);
            or.getPEBenWiseSlot(peBenWiseApptSlotRequestModel);*/

//            int count = orderDetailsModel.getAllOrderdetails().get(0).getBenMaster().size();
//            int bencount = orderVisitDetailsModelArrayList.get(0).getAllOrderdetails().get(0).getBenMaster().size();

//            System.out.println(count);
            OrderReleaseRemarksController orc = new OrderReleaseRemarksController(this);
            orc.getPEbtechSlot(token, pincode, fulldate, count);


        } else {
            Toast.makeText(activity, CheckInternetConnectionMsg, Toast.LENGTH_SHORT).show();
        }
    }

    public void PEslotresponse(ArrayList<GetPEBtechSlotResponseModel> slotResponseModels) {

        ArrayList<GetPEBtechSlotResponseModel> tempArraylist = new ArrayList<>();
        int minsToAdd = 30;
        String newTime = "";
        String newTime1 = "";
        Date date = new Date();
        Date date1 = new Date();
        for (int i = 0; i < slotResponseModels.size(); i++) {
            String str = slotResponseModels.get(i).getSlot();
            String[] separated = str.split("-");
            String str1 = separated[0];
            String str2 = separated[1];
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                date = sdf.parse(str1);
                date1 = sdf.parse(str2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minsToAdd);
            newTime = sdf.format(cal.getTime());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            cal1.add(Calendar.MINUTE, minsToAdd);
            newTime1 = sdf.format(cal1.getTime());
            String strNEw = new StringBuilder(newTime).append("-").append(newTime1).toString();
            GetPEBtechSlotResponseModel slotMasterModel = new GetPEBtechSlotResponseModel();
            slotMasterModel.setId(slotResponseModels.get(i).getId());
            slotMasterModel.setSlot(slotResponseModels.get(i).getSlot());
            slotMasterModel.setSlotMasterId(slotResponseModels.get(i).getSlotMasterId());
            slotMasterModel.setNewSlot(strNEw);
            tempArraylist.add(slotMasterModel);
        }

        slotResponseModels.clear();
        slotResponseModels = tempArraylist;

        SlotAppointmentTimeAdapter timeAdapter = new SlotAppointmentTimeAdapter(this, slotResponseModels);
        rec_slotList.setAdapter(timeAdapter);

    }

    public void appointmentslotChecked(GetPEBtechSlotResponseModel slot) {
        String[] slotSplit = slot.getSlot().split(" ");
        SlotID = slot.getId();
        slot1 = slotSplit[0];
        slot2 = date + " " + slot1;
    }

    public void slotSubmitresponse(ResponseModel responseModel) {
        if (responseModel != null) {
            if (responseModel.getResponse().equalsIgnoreCase("SUCCESS")) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                builder.setTitle("Order Status")
                        .setMessage("Order Rescheduled successfully")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(activity, B2BVisitOrdersDisplayFragment.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
//                                startActivity(new Intent(activity, B2BVisitOrdersDisplayFragment.class));
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                TastyToast.makeText(activity, responseModel.getResponseMessage(), TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }
        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPEBenWiseSlot(ArrayList<GetPEBtechSlotResponseModel> slotResponseModelArrayList) {

        try {
            SlotAppointmentTimeAdapter timeAdapter = new SlotAppointmentTimeAdapter(this, slotResponseModelArrayList);
            rec_slotList.setAdapter(timeAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void TCslotResponse(ArrayList<GetPEBtechSlotResponseModel> slotResponseModelArrayList) {
        SlotAppointmentTimeAdapter timeAdapter = new SlotAppointmentTimeAdapter(this, slotResponseModelArrayList);
        rec_slotList.setAdapter(timeAdapter);

    }
}