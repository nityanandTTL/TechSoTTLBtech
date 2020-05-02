package com.thyrocare.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.LoginScreenActivity;
import com.thyrocare.activity.ScheduleYourDayActivity;
import com.thyrocare.activity.ScheduleYourDayActivity2;
import com.thyrocare.activity.ScheduleYourDayActivity3;
import com.thyrocare.activity.ScheduleYourDayActivity4;
import com.thyrocare.activity.ScheduleYourDayIntentActivity;
import com.thyrocare.adapter.SlotsDisplayAdapter;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.delegate.SlotsSelectionDelegate;
import com.thyrocare.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.models.api.response.BtechAvaliabilityResponseModel;
import com.thyrocare.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.models.data.SlotModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleYourDayFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "SCHEDULE_YOUR_DAY_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private Button txtNo, txtYes;
    private LinearLayout llSlotsDisplay;
    private GridView gvSlots;
    private Button btnProceed;
    private ArrayList<SlotModel> slotsArr;
    private ArrayList<SlotModel> selectedSlotsArr;
    private SlotsDisplayAdapter slotsDisplayAdapter;
    private boolean isAvailable = false;
    private SetBtechAvailabilityAPIRequestModel savedModel;

    private String lasScheduleDate;

    public ScheduleYourDayFragment() {
        // Required empty public constructor
    }

    // txt_no
    public static ScheduleYourDayFragment newInstance() {
        ScheduleYourDayFragment fragment = new ScheduleYourDayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Schedule");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (HomeScreenActivity) getActivity();
        try {
            activity.toolbarHome.setTitle("Schedule your Day");
            activity.toolbarHome.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);


        savedModel = appPreferenceManager.getBtechAvailabilityAPIRequestModel();
        selectedSlotsArr = appPreferenceManager.getSelectedSlotsArr();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.blank, container, false);
      /*  initUI();
        initListeners();*/
        fetchData1();
      /*  findSchedularDate();*/
        return rootView;
    }


    private void fetchData1() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchbtechavailAsyncTask = asyncTaskForRequest.getBtechAvaliability();
        fetchbtechavailAsyncTask.setApiCallAsyncTaskDelegate(new DispatchToHubDetailDisplayApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchbtechavailAsyncTask.execute(fetchbtechavailAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class DispatchToHubDetailDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "dayssssssssss ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                NewBtechAvaliabilityResponseModel newBtechAvaliabilityResponseModel = new NewBtechAvaliabilityResponseModel();
                newBtechAvaliabilityResponseModel = responseParser.getNewBtechAvaliabilityResponseModel(json, statusCode);
                appPreferenceManager.setNEWBTECHAVALIABILITYRESPONSEMODEL(newBtechAvaliabilityResponseModel);
                if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL() != null) {
                    /*if (btechAvaliabilityResponseModel.getNumberofDays()== 0) {
                        Logger.error("ZERRO");
                        Toast.makeText(activity, "Avability Already Done", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleConstants.WHEREFROM, 1);
                        Intent intent = new Intent(activity, HomeScreenActivity.class);
                        // intent.putExtra("camefrom","1");
                        startActivity(intent);
                        //   switchToActivity(activity, HomeScreenActivity.class, bundle);
                    } else if (btechAvaliabilityResponseModel.getNumberofDays()== 1) {
                        Logger.error("ONEEEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "1");
                        startActivity(mIntent);
                    } else if (btechAvaliabilityResponseModel.getNumberofDays() == 3) {
                        Logger.error("THREEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                        mIntent.putExtra("WHEREFROM", "1");
                        startActivity(mIntent);
                    } else if (btechAvaliabilityResponseModel.getNumberofDays() == 2) {
                        Logger.error("FOURRRRR");
                        Intent mIntent = new Intent(activity, ScheduleYourDayIntentActivity.class);
                        mIntent.putExtra("WHEREFROM", "1");
                        startActivity(mIntent);
                    }*/
                    if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay1()==1){
                        Logger.error("ONEEEE");
                        Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                        mIntent.putExtra("WHEREFROM", "0");
                        startActivity(mIntent);

                    }else if(appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2()==1){
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
                        Logger.error("ZERRO");
                        Toast.makeText(activity, "Avability Already Done", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleConstants.WHEREFROM, 1);
                        Intent intent = new Intent(activity, HomeScreenActivity.class);
                        // intent.putExtra("camefrom","1");
                        startActivity(intent);
                        //   switchToActivity(activity, HomeScreenActivity.class, bundle);
                    }
                } else {
                    Logger.error("else " + json);
                }
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

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
                if (null == appPreferenceManager.getScheduleCounter() || appPreferenceManager.getScheduleCounter().isEmpty() || appPreferenceManager.getScheduleCounter().equals("n")) {
                    txtYes.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                    txtNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    llSlotsDisplay.setVisibility(View.VISIBLE);
                    isAvailable = true;
                    btnProceed.setVisibility(View.VISIBLE);
                    fetchData();
                } else if (null != appPreferenceManager.getScheduleCounter() && appPreferenceManager.getScheduleCounter().equals("y")) {
                    Toast.makeText(activity, "User can schedule only once per day...Please try again later.", Toast.LENGTH_SHORT).show();
                }
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
        //changes
     /*   txtYes = (Button) rootView.findViewById(R.id.txt_yes);
        txtNo = (Button) rootView.findViewById(R.id.txt_no);*/
        //changes

       /* btnProceed = (Button) rootView.findViewById(R.id.btn_proceed);
        btnProceed.setVisibility(View.INVISIBLE);
        llSlotsDisplay = (LinearLayout) rootView.findViewById(R.id.ll_slots_display);
        gvSlots = (GridView) rootView.findViewById(R.id.gv_slots);*/
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
            } else if (statusCode == 401) {
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
            getActivity().startActivity(homeIntent);
            // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

            Intent n = new Intent(activity, LoginScreenActivity.class);
            n.setAction(Intent.ACTION_MAIN);
            n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(n);
            getActivity().finish();
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
                TastyToast.makeText(activity,   "Availability set Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                //  Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                if (isAvailable) {

                    //changes_5june2017
                    appPreferenceManager.setScheduleCounter("y");
                    String scheduledDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    appPreferenceManager.setScheduleDate(scheduledDate);
                    //changes_5june2017

                    appPreferenceManager.setBtechAvailabilityResponseModel(new Gson().fromJson(json, SetBtechAvailabilityAPIRequestModel.class));
                    appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                    pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                } else {
                    pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                }
            } else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                TastyToast.makeText(activity,  "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity,  "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            // Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
        }
    }
}
