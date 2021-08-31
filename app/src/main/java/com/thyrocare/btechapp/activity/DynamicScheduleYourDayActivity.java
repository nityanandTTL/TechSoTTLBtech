package com.thyrocare.btechapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
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
import com.thyrocare.btechapp.models.api.response.DynamicBtechAvaliabilityResponseModel;
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

import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

/**
 * Created by E4904 on 8/12/2017.
 */

public class DynamicScheduleYourDayActivity extends AbstractActivity {

    public static final String TAG_FRAGMENT = "DynamicScheduleYourDayActivity";
    private DynamicScheduleYourDayActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private Button txtNo, txtYes;
    private LinearLayout llSlotsDisplay,ll_btn;
    private GridView gvSlots;
    private Button btnProceed;
    private ArrayList<SlotModel> slotsArr;
    private ArrayList<SlotModel> selectedSlotsArr;
    private SlotsDisplayAdapter slotsDisplayAdapter;
    private boolean isAvailable = false;
    private TextView date;
    private String lasScheduleDate;
    String value;
    String tomorrowAsString;
    private String disableNo = "";
    private Global global;
    TextView  tv_toolbar;
    ImageView iv_back,iv_home;

    int gbtkn = 0;

    public DynamicScheduleYourDayActivity() {
        // Required empty public constructor
    }

    // txt_no


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_your_day1);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        value = getIntent().getExtras().getString("WHEREFROM");
        Logger.error("value " + value);

        try {
            if (getIntent().getExtras().getString("SHOWNO") != null) {
                disableNo = getIntent().getExtras().getString("SHOWNO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectedSlotsArr = appPreferenceManager.getSelectedSlotsArr();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        if (validateDays()) {
            tomorrowAsString = getDateFromobj();
        }else {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR_OF_DAY, 0);

            if (value.equals("0")) {
                if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                    Logger.error("Aaata Gela");
                    Logger.error("Selfie" + String.valueOf(appPreferenceManager.getSelfieResponseModel()));
                    Logger.error("LOgeeererereeere" + String.valueOf(appPreferenceManager.getSelfieResponseModel().getTimeUploaded()));
                    Logger.error("LOgeeererereeereMIllis" + String.valueOf(c.getTimeInMillis()));

                    // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
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
//        tomorrowAsString = dateFormat.format(tomorrow);


        initUI();
        initListeners();
        findSchedularDate();


    }

    private boolean validateDays() {
        if (Constants.setAvailabiltity != null) {
            if (Constants.setAvailabiltity.getAllDays() != null) {
                for (int i = 0; i < Constants.setAvailabiltity.getAllDays().size(); i++) {
                    if (Constants.setAvailabiltity.getAllDays().get(i).getDay() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getDateFromobj() {
        String st = "";
        if (Constants.setAvailabiltity != null) {
            if (Constants.setAvailabiltity.getAllDays() != null) {
                for (int i = 0; i < Constants.setAvailabiltity.getAllDays().size(); i++) {
                    if (Constants.setAvailabiltity.getAllDays().get(i).getDay() == 1) {
                        gbtkn = i;
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, Constants.setAvailabiltity.getAllDays().get(i).getDayCount());
                        Date tomorrow = calendar.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        st = dateFormat.format(tomorrow);
                        break;
                    }
                }
            }
        }
        return st;
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("canBackpress") && getIntent().getBooleanExtra("canBackpress", false)) {
            super.onBackPressed();
        }
    }

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
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtYes.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                txtNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                llSlotsDisplay.setVisibility(View.VISIBLE);
                isAvailable = true;
                ll_btn.setVisibility(View.VISIBLE);
                btnProceed.setVisibility(View.VISIBLE);
                fetchData();
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNo.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                txtYes.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                llSlotsDisplay.setVisibility(View.GONE);
                ll_btn.setVisibility(View.GONE);
                btnProceed.setVisibility(View.INVISIBLE);
                isAvailable = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you are not available ?")
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
                                calendar.add(Calendar.DAY_OF_MONTH, Constants.setAvailabiltity.getAllDays().get(gbtkn).getDayCount());
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
                if (!activity.isFinishing()) {
                    builder.create().show();
                }
            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                setBtechAvailabilityAPIRequestModel.setAvailable(isAvailable);
                if (appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                    setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                }

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
                calendar.add(Calendar.DAY_OF_MONTH, Constants.setAvailabiltity.getAllDays().get(gbtkn).getDayCount());
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
        txtYes = (Button) findViewById(R.id.txt_yes);
        txtNo = (Button) findViewById(R.id.txt_no);
        date = (TextView) findViewById(R.id.date);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        ll_btn = findViewById(R.id.ll_btn);

        tv_toolbar.setText("Schedule");
        tv_toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        iv_home.setVisibility(View.INVISIBLE);
        iv_back.setVisibility(View.INVISIBLE);
        Logger.error("TOdayYYYYYY" + tomorrowAsString);

        date.setText(tomorrowAsString);
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
        responseCall.enqueue(new Callback<ArrayList<SlotModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SlotModel>> call, Response<ArrayList<SlotModel>> response) {
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
            public void onFailure(Call<ArrayList<SlotModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    public void CallLogOutFromDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            new LogUserActivityTagging(activity, LOGOUT, "");
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
//                    Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                    Constants.setAvailabiltity.getAllDays().get(gbtkn).setDay(0);
                    appPreferenceManager.setScheduleCounter("y");
                    String scheduledDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    appPreferenceManager.setScheduleDate(scheduledDate);

                    appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);

                    Intent mIntent = new Intent(activity, DynamicScheduleYourDayActivity.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    startActivity(mIntent);
                    finish();

                   /* if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2() == 1) {
                        Logger.error("THREEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3() == 1) {
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity3.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay4() == 1) {
                        Logger.error("FOUR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity4.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);
                    } else {
                        if (value.equals("0")) {
                            if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
                                Logger.error("Aaata Gela");
                                Logger.error("Selfie" + String.valueOf(appPreferenceManager.getSelfieResponseModel()));
                                Logger.error("LOgeeererereeere" + String.valueOf(appPreferenceManager.getSelfieResponseModel().getTimeUploaded()));
                                Logger.error("LOgeeererereeereMIllis" + String.valueOf(c.getTimeInMillis()));

                                // switchToActivity(activity, ScheduleYourDayActivity.class, new Bundle());
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
                    }*/

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
