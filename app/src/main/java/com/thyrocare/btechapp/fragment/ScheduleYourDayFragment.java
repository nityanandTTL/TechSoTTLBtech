package com.thyrocare.btechapp.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Leave_intimation_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity2;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity3;
import com.thyrocare.btechapp.activity.ScheduleYourDayActivity4;
import com.thyrocare.btechapp.adapter.SlotsDisplayAdapter;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.delegate.SlotsSelectionDelegate;
import com.thyrocare.btechapp.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.btechapp.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.models.data.SlotModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

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
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class ScheduleYourDayFragment extends AppCompatActivity {


    public static final String TAG_FRAGMENT = "SCHEDULE_YOUR_DAY_FRAGMENT";
    private Activity activity;
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
    private Global global;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        activity = this;
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        savedModel = appPreferenceManager.getBtechAvailabilityAPIRequestModel();
        selectedSlotsArr = appPreferenceManager.getSelectedSlotsArr();
        fetchData1();
    }

    private void fetchData1() {

        if (isNetworkAvailable(activity)) {
            GetBtechAvailability();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void GetBtechAvailability() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<NewBtechAvaliabilityResponseModel> responseCall = apiInterface.GetBtechAvailability(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait ..", false);
        responseCall.enqueue(new Callback<NewBtechAvaliabilityResponseModel>() {
            @Override
            public void onResponse(Call<NewBtechAvaliabilityResponseModel> call, Response<NewBtechAvaliabilityResponseModel> response) {
                global.hideProgressDialog(activity);
                MessageLogger.PrintMsg("VersionApi Onsuccess");
                if (response.isSuccessful() && response.body() != null) {
                    NewBtechAvaliabilityResponseModel btechAvaliabilityResponseModel = response.body();
                    onBtechAvailabilityResponseReceived(btechAvaliabilityResponseModel);
                } else {
                    global.showCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<NewBtechAvaliabilityResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }

    private void onBtechAvailabilityResponseReceived(NewBtechAvaliabilityResponseModel newBtechAvaliabilityResponseModel) {

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
            if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay1() == 1) {
                Logger.error("ONEEEE");
                Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                mIntent.putExtra("WHEREFROM", "0");
                startActivity(mIntent);

            } else if (appPreferenceManager.getNEWBTECHAVALIABILITYRESPONSEMODEL().getNumberOfDays().getDay2() == 1) {
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
                Logger.error("ZERRO");

                try {
                    Toast.makeText(activity, "Availability Already Done", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleConstants.WHEREFROM, 1);
                    Intent intent = new Intent(activity, HomeScreenActivity.class);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
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

                                if (isNetworkAvailable(activity)) {
                                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel, false);
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
                if (isNetworkAvailable(activity)) {
                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel, true);
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
    //changes
     /*   txtYes = (Button) rootView.findViewById(R.id.txt_yes);
        txtNo = (Button) rootView.findViewById(R.id.txt_no);*/
    //changes

       /* btnProceed = (Button) rootView.findViewById(R.id.btn_proceed);
        btnProceed.setVisibility(View.INVISIBLE);
        llSlotsDisplay = (LinearLayout) rootView.findViewById(R.id.ll_slots_display);
        gvSlots = (GridView) rootView.findViewById(R.id.gv_slots);*/


    private void CallFetchSlotDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<SlotModel>> responseCall = apiInterface.CallFetchSlotDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<ArrayList<SlotModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SlotModel>> call, retrofit2.Response<ArrayList<SlotModel>> response) {
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


    private void callBtechAvailabilityRequestApi(SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel, final boolean isAvailable) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
        global.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    TastyToast.makeText(activity, "Availability set Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    //  Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                    if (isAvailable) {

                        //changes_5june2017
                        appPreferenceManager.setScheduleCounter("y");
                        String scheduledDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                        appPreferenceManager.setScheduleDate(scheduledDate);
                        //changes_5june2017

                        appPreferenceManager.setBtechAvailabilityResponseModel(new Gson().fromJson(response.body(), SetBtechAvailabilityAPIRequestModel.class));
                        appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                        startActivity(new Intent(activity, HomeScreenActivity.class));
                    }
                } else if (response.code() == 401) {
                    CallLogOutFromDevice();
                } else {
                    TastyToast.makeText(activity, "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    //Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
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
