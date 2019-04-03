package com.thyrocare.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.adapter.SlotsDisplayAdapter;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.delegate.SlotsSelectionDelegate;
import com.thyrocare.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.models.data.SlotModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by E4904 on 8/12/2017.
 */

public class ScheduleYourDayActivity extends AbstractActivity {

    public static final String TAG_FRAGMENT = "SCHEDULE_YOUR_DAY_FRAGMENT";
    private ScheduleYourDayActivity activity;
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
    private String lasScheduleDate;
    private String today;
    private int camefrom = 3;
    String value;
    String tomorrowAsString;
    private String disableNo = "";

    public ScheduleYourDayActivity() {
        // Required empty public constructor
    }

    // txt_no


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule_your_day1);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
      /*  //Jai
        Bundle bundle = new Bundle();
        camefrom = bundle.getInt(BundleConstants.WHEREFROM, 0);
        Logger.error("camefrom: " + camefrom);
        //jai*/
        value = getIntent().getExtras().getString("WHEREFROM");
        Logger.error("value " + value);

        try {
            if (getIntent().getExtras().getString("SHOWNO") != null) {
                disableNo = getIntent().getExtras().getString("SHOWNO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   Toast.makeText(activity, "WHere", Toast.LENGTH_SHORT).show();
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        savedModel = appPreferenceManager.getBtechAvailabilityAPIRequestModel();
        selectedSlotsArr = appPreferenceManager.getSelectedSlotsArr();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        tomorrowAsString = dateFormat.format(tomorrow);

        initUI();
        initListeners();
        findSchedularDate();


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
       /* Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        startActivity(i);
        activity.finish();*/
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
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changes_5june2017
 /*               if (null == appPreferenceManager.getScheduleCounter() || appPreferenceManager.getScheduleCounter().isEmpty() || appPreferenceManager.getScheduleCounter().equals("n")) {*/
                    txtYes.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                    txtNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    llSlotsDisplay.setVisibility(View.VISIBLE);
                    isAvailable = true;
                    btnProceed.setVisibility(View.VISIBLE);
                    fetchData();
               /* } else if (null != appPreferenceManager.getScheduleCounter() && appPreferenceManager.getScheduleCounter().equals("y")) {
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
                builder.setMessage("Are you sure you are not available tomorrow ?")
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
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                                ApiCallAsyncTask setBtechAvailabilityAsyncTask = new AsyncTaskForRequest(activity).getPostBtechAvailabilityRequestAsyncTask(setBtechAvailabilityAPIRequestModel);
                                setBtechAvailabilityAsyncTask.setApiCallAsyncTaskDelegate(new SetBtechAvailabilityAsyncTaskDelegateResult(false));
                                if (isNetworkAvailable(activity)) {
                                    setBtechAvailabilityAsyncTask.execute(setBtechAvailabilityAsyncTask);
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
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                ApiCallAsyncTask setBtechAvailabilityAsyncTask = new AsyncTaskForRequest(activity).getPostBtechAvailabilityRequestAsyncTask(setBtechAvailabilityAPIRequestModel);
                setBtechAvailabilityAsyncTask.setApiCallAsyncTaskDelegate(new SetBtechAvailabilityAsyncTaskDelegateResult(true));
                if (isNetworkAvailable(activity)) {
                    setBtechAvailabilityAsyncTask.execute(setBtechAvailabilityAsyncTask);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchData() {
        ApiCallAsyncTask fetchSlotsAsyncTask = new AsyncTaskForRequest(activity).getFetchSlotDetailsRequestAsyncTask();
        fetchSlotsAsyncTask.setApiCallAsyncTaskDelegate(new FetchSlotsAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchSlotsAsyncTask.execute(fetchSlotsAsyncTask);
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

    private class FetchSlotsAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                slotsArr = new ResponseParser(activity).getSlotDetailsResponseModel(json, statusCode);
                selectedSlotsArr = new ArrayList<>();
                for (SlotModel slotModel :
                        slotsArr) {
                    if (slotModel.isMandatorySlot()) {
                        selectedSlotsArr.add(slotModel);
                    }
                }
                initData();
            }else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                Toast.makeText(activity, "Failed to Fetch Slots", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void CallLogOutFromDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
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

            Intent n = new Intent(activity, LoginScreenActivity.class);
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

    private class SetBtechAvailabilityAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        boolean isAvailable;

        public SetBtechAvailabilityAsyncTaskDelegateResult(boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200 || statusCode == 201) {
                Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                //Avaliablity

//                if (isAvailable) {

                    appPreferenceManager.setScheduleCounter("y");
                    String scheduledDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    appPreferenceManager.setScheduleDate(scheduledDate);

                  /*  appPreferenceManager.setBtechAvailabilityResponseModel(new Gson().fromJson(json, SetBtechAvailabilityAPIRequestModel.class));*/
                    appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.MILLISECOND, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.HOUR_OF_DAY, 0);

                    if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2()==1){
                        Logger.error("THREEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay3()==1){
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



              /*  } else {
                    Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                    i.putExtra("LEAVEINTIMATION", "0");
                    startActivity(i);

                    //  pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                }*/
            } else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
        }
    }

}
