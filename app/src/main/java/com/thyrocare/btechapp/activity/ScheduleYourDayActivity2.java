package com.thyrocare.btechapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.adapter.SlotsDisplayAdapter;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.delegate.SlotsSelectionDelegate;
import com.thyrocare.btechapp.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.btechapp.models.data.SlotModel;


import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;


/**
 * The type Schedule your day activity 2.
 */
public class ScheduleYourDayActivity2 extends AbstractActivity {

    /**
     * The constant TAG_FRAGMENT.
     */
    public static final String TAG_FRAGMENT = "SCHEDULE_YOUR_DAY_FRAGMENT";
    private ScheduleYourDayActivity2 activity;
    private AppPreferenceManager appPreferenceManager;
    private Button txtNo, txtYes;
    private LinearLayout llSlotsDisplay;
    private GridView gvSlots;
    private Button btnProceed;
    private ArrayList<SlotModel> slotsArr;
    private ArrayList<SlotModel> selectedSlotsArr;
    private SlotsDisplayAdapter slotsDisplayAdapter;
    private boolean isAvailable = false;
    private SetBtechAvailabilityAPIRequestModel savedModel;
    private TextView date;
    private String value;

    private String lasScheduleDate;
    /**
     * The Day afterttomorrow as string.
     */
    String dayAfterttomorrowAsString;
    private String disableNo = "";
    private Global global;
    Switch btn_switch;


    /**
     * Instantiates a new Schedule your day activity 2.
     */
    public ScheduleYourDayActivity2() {
        // Required empty public constructor
    }

    // txt_no




    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("canBackpress") && getIntent().getBooleanExtra("canBackpress",false)) {
            super.onBackPressed();
        }
//        super.onBackPressed();
       /* Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        startActivity(i);
        activity.finish();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_your_day2);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        value = getIntent().getExtras().getString("WHEREFROM");

        try {
            if (getIntent().getExtras().getString("SHOWNO") != null) {
                disableNo = getIntent().getExtras().getString("SHOWNO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        dayAfterttomorrowAsString = dateFormat.format(tomorrow);
        initUI();
        initListeners();
        findSchedularDate();
        savedModel = appPreferenceManager.getBtechAvailabilityAPIRequestModel();
        selectedSlotsArr = appPreferenceManager.getSelectedSlotsArr();


    }



      /*  activity2.toolbarHome.setTitle("Schedule your Day");

      */


    //changes_5june2017
    private void findSchedularDate() {

        if (!appPreferenceManager.getScheduleDate().isEmpty()) {
            lasScheduleDate = appPreferenceManager.getScheduleDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date strDate = null;
            try {
                strDate = sdf.parse(lasScheduleDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date todayDate = null;
            try {
                String todayDateStr = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                SimpleDateFormat todaySdf = new SimpleDateFormat("dd/MM/yyyy");
                todayDate = todaySdf.parse(todayDateStr);

                Logger.debug("*******************************************************************");
                Logger.debug(String.valueOf(strDate));
                Logger.debug(String.valueOf(todayDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (todayDate.after(strDate)) {
                //Toast.makeText(activity, "after valid date", Toast.LENGTH_SHORT).show();
                appPreferenceManager.setScheduleCounter("n");
            } else {
                //Toast.makeText(activity, "same or before valid date", Toast.LENGTH_SHORT).show();
                appPreferenceManager.setScheduleCounter("y");
            }

        }
    }
    //changes_5june2017

    private void initListeners() {
        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     llSlotsDisplay.setVisibility(View.VISIBLE);
                     isAvailable = true;
                     btnProceed.setVisibility(View.VISIBLE);
                     fetchData();
                 }else{
                     llSlotsDisplay.setVisibility(View.GONE);
                     btnProceed.setVisibility(View.INVISIBLE);
                     isAvailable = false;
                     AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                     builder.setMessage("Are you sure you are not available Day After tomorrow?")
                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int id) {
                                     SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                                     setBtechAvailabilityAPIRequestModel.setAvailable(isAvailable);
                                     setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                     String slots = "";
                                     setBtechAvailabilityAPIRequestModel.setSlots(slots);
                                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                                     Calendar calendar = Calendar.getInstance();

                                     setBtechAvailabilityAPIRequestModel.setEntryDate(sdf.format(calendar.getTime()));
                                     setBtechAvailabilityAPIRequestModel.setLastUpdated(sdf.format(calendar.getTime()));
                                     calendar.add(Calendar.DAY_OF_MONTH, 2);
                                     setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                                     if (isNetworkAvailable(activity)) {
                                         callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
                                     } else {
                                         Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             })
                             .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.dismiss();

                                 }
                             });
                     builder.create().
                             show();
                 }
            }
        });


        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changes_5june2017
              /*  if (null == appPreferenceManager.getScheduleCounter() || appPreferenceManager.getScheduleCounter().isEmpty() || appPreferenceManager.getScheduleCounter().equals("n")) {*/
                txtYes.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                txtNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                llSlotsDisplay.setVisibility(View.VISIBLE);
                isAvailable = true;
                btnProceed.setVisibility(View.VISIBLE);
                fetchData();
              /*  } else if (null != appPreferenceManager.getScheduleCounter() && appPreferenceManager.getScheduleCounter().equals("y")) {
                    Toast.makeText(activity, "User can schedule only once per day...Please try again later.", Toast.LENGTH_SHORT).show();
                }*/
                //changes_5june2017
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNo.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                txtYes.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                llSlotsDisplay.setVisibility(View.GONE);
                btnProceed.setVisibility(View.INVISIBLE);
                isAvailable = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you are not available Day After tomorrow?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                                setBtechAvailabilityAPIRequestModel.setAvailable(isAvailable);
                                setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                String slots = "";
                                setBtechAvailabilityAPIRequestModel.setSlots(slots);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                                Calendar calendar = Calendar.getInstance();

                                setBtechAvailabilityAPIRequestModel.setEntryDate(sdf.format(calendar.getTime()));
                                setBtechAvailabilityAPIRequestModel.setLastUpdated(sdf.format(calendar.getTime()));
                                calendar.add(Calendar.DAY_OF_MONTH, 2);
                                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                                if (isNetworkAvailable(activity)) {
                                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
                                } else {
                                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                builder.create().
                        show();
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                setBtechAvailabilityAPIRequestModel.setAvailable(isAvailable);
                setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                String slots = "";
                if (selectedSlotsArr != null && selectedSlotsArr.size() > 0) {
                    for (SlotModel selecSlotModel :
                            selectedSlotsArr) {
                        if (InputUtils.isNull(slots)) {
                            slots = selecSlotModel.getId() + "";
                        } else {
                            slots = slots + "," + selecSlotModel.getId();
                        }
                    }
                }
                setBtechAvailabilityAPIRequestModel.setSlots(slots);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                Calendar calendar = Calendar.getInstance();

                setBtechAvailabilityAPIRequestModel.setEntryDate(sdf.format(calendar.getTime()));
                setBtechAvailabilityAPIRequestModel.setLastUpdated(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                if (isNetworkAvailable(activity)) {
                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();


                }

            }
        });
    }

    private void fetchData() {
        if (isNetworkAvailable(activity)) {
            CallFetchSlotDetailsApi();
        } else {
            Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        //changes
        txtYes = (Button) findViewById(R.id.txt_yes);
        txtNo = (Button) findViewById(R.id.txt_no);
        //changes
        date = (TextView) findViewById(R.id.date);
        btn_switch = findViewById(R.id.btn_switch);
        date.setText(dayAfterttomorrowAsString);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnProceed.setVisibility(View.INVISIBLE);
        llSlotsDisplay = (LinearLayout) findViewById(R.id.ll_slots_display);
        gvSlots = (GridView) findViewById(R.id.gv_slots);

        if (disableNo.toString().equals("1")) {
            txtNo.setVisibility(View.INVISIBLE);
        } else {
            txtNo.setVisibility(View.VISIBLE);
        }
    }

    private void CallFetchSlotDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<SlotModel>> responseCall = apiInterface.CallFetchSlotDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback< ArrayList<SlotModel>>() {
            @Override
            public void onResponse(Call< ArrayList<SlotModel>> call, retrofit2.Response< ArrayList<SlotModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    slotsArr = response.body();
                    selectedSlotsArr = new ArrayList<>();
                    for (SlotModel slotModel :
                            slotsArr) {
                        if (slotModel.isMandatorySlot()) {
                            selectedSlotsArr.add(slotModel);
                        }
                    }
                    initData();
                } else if (response.code() == 401) {
                    CallLogOutFromDevice();
                } else {
                    Toast.makeText(activity, "Failed to Fetch Slots", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call< ArrayList<SlotModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * Call log out from device.
     */
    public void CallLogOutFromDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
             new LogUserActivityTagging(activity, LOGOUT,"");
                    appPreferenceManager.clearAllPreferences();
            try {
                new DhbDao(activity).deleteTablesonLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(activity, LoginActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(n);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initData() {
        slotsDisplayAdapter = new SlotsDisplayAdapter(slotsArr, activity, new SlotsSelectionDelegate() {
            @Override
            public void onSlotSelected(ArrayList<SlotModel> selectedSlotModels) {
                selectedSlotsArr = selectedSlotModels;
                slotsDisplayAdapter.notifyDataSetChanged();
            }
        }, selectedSlotsArr);
        gvSlots.setAdapter(slotsDisplayAdapter);
        llSlotsDisplay.setVisibility(View.VISIBLE);
    }


    private void callBtechAvailabilityRequestApi(SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
        global.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                    //appPreferenceManager.setBtechAvailabilityResponseModel(new Gson().fromJson(json, SetBtechAvailabilityAPIRequestModel.class));
                    appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);

                    if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3()==1){
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity3.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4()==1){
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity4.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);
                    }else {
                        if (value.equals("0")) {
                            if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {

                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                i.putExtra("LEAVEINTIMATION", "0");
                                startActivity(i);
                            } else {
                                switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
                            }
                        } else {
                            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            i.putExtra("LEAVEINTIMATION", "0");
                            startActivity(i);
                        }
                    }


                } else if (response.code() == 401) {
                    CallLogOutFromDevice();
                } else {
                    Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
