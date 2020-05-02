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
import com.thyrocare.models.data.TSPNBT_AvilModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.DateUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Nityanand on 9/21/2017.
 */

public class Tsp_ScheduleYourDayActivity extends AbstractActivity {

    public static final String TAG_FRAGMENT = "Tsp_ScheduleYourDayActivity";
    Tsp_ScheduleYourDayActivity activity;
    private ArrayList<TSPNBT_AvilModel> TSP_NBTAvailArr;
    TextView txt_sh_msg;
    Button txt_no, txt_yes, btn_proceed;
    LinearLayout ll_slots_display;
    GridView gv_slots;
    private ArrayList<SlotModel> slotsArr;
    private ArrayList<SlotModel> selectedSlotsArr;
    private SlotsDisplayAdapter slotsDisplayAdapter;
    int CounterFlag = 1;
    String NBTName = "";
    String NBT_Id = "";
    String NBT_date = "";
    String NBT_date_pass = "";

    public Tsp_ScheduleYourDayActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_tspnbt_day);
        activity = this;
        Bundle bdl = getIntent().getExtras();
        TSP_NBTAvailArr = bdl.getParcelableArrayList(CommonUtils.TSP_NBT_Str);
//        TSP_NBTAvailArr = ar();
        Logger.error("");

        InItUI();
        ShowData();
        InItUIListener();
    }

    private void ShowData() {
        if (TSP_NBTAvailArr != null) {
            if (TSP_NBTAvailArr.size() != 0) {
                for (int i = 0; i < TSP_NBTAvailArr.size(); i++) {
                    if (CounterFlag <= TSP_NBTAvailArr.size()) {
                        if ((i + 1) == CounterFlag) {

                            txt_no.setVisibility(View.VISIBLE);
                            txt_yes.setVisibility(View.VISIBLE);
                            btn_proceed.setVisibility(View.GONE);

                            NBT_Id = TSP_NBTAvailArr.get(i).getBtechID();
                            NBTName = TSP_NBTAvailArr.get(i).getName();
                            NBT_date = DateUtils.Req_Date_Req(TSP_NBTAvailArr.get(i).getDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyy");
                            NBT_date_pass = TSP_NBTAvailArr.get(i).getDate();
                            txt_sh_msg.setText("" + NBTName + " is available on " + NBT_date + "?");
                        }
                    } else {
                        ShowTsphomeScreen();
                    }
                }
            }
        }
    }

    public ArrayList<TSPNBT_AvilModel> ar() {
        ArrayList<TSPNBT_AvilModel> entL = new ArrayList<>();
        TSPNBT_AvilModel n = new TSPNBT_AvilModel();
        n.setBtechID("884543101");
        n.setDate("22 Sep 2017");
        n.setName("test 1");
        entL.add(n);

        n = new TSPNBT_AvilModel();
        n.setBtechID("884543101");
        n.setDate("23 Sep 2017");
        n.setName("test 2");
        entL.add(n);

        n = new TSPNBT_AvilModel();
        n.setBtechID("884543101");
        n.setDate("24 Sep 2017");
        n.setName("test 3");
        entL.add(n);

        n = new TSPNBT_AvilModel();
        n.setBtechID("884543101");
        n.setDate("25 Sep 2017");
        n.setName("test 4");
        entL.add(n);

        return entL;
    }

    private void InItUI() {
        txt_sh_msg = (TextView) findViewById(R.id.txt_sh_msg);
        txt_no = (Button) findViewById(R.id.txt_no);
        txt_yes = (Button) findViewById(R.id.txt_yes);
        ll_slots_display = (LinearLayout) findViewById(R.id.ll_slots_display);
        gv_slots = (GridView) findViewById(R.id.gv_slots);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);

        btn_proceed.setVisibility(View.GONE);
    }

    private void InItUIListener() {
        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });

        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure " + NBTName + " is not available on " + NBT_date + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                ll_slots_display.setVisibility(View.GONE);
                                btn_proceed.setVisibility(View.INVISIBLE);

                                SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                                setBtechAvailabilityAPIRequestModel.setAvailable(false);
                                setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(NBT_Id));
                                String slots = "";
                                setBtechAvailabilityAPIRequestModel.setSlots(slots);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
                                Calendar calendar = Calendar.getInstance();

                                setBtechAvailabilityAPIRequestModel.setEntryDate(sdf.format(calendar.getTime()));
                                setBtechAvailabilityAPIRequestModel.setLastUpdated(sdf.format(calendar.getTime()));
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                setBtechAvailabilityAPIRequestModel.setAvailableDate(NBT_date_pass);

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

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel = new SetBtechAvailabilityAPIRequestModel();
                setBtechAvailabilityAPIRequestModel.setAvailable(true);
                setBtechAvailabilityAPIRequestModel.setBtechId(Integer.parseInt(NBT_Id));
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
                setBtechAvailabilityAPIRequestModel.setAvailableDate(NBT_date_pass);

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

    private class SetBtechAvailabilityAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        boolean isAvailable;

        public SetBtechAvailabilityAsyncTaskDelegateResult(boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200 || statusCode == 201) {
                CounterFlag++;
                TastyToast.makeText(activity,  "Availability set Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
               // Toast.makeText(activity, "Availability set Successfully", Toast.LENGTH_SHORT).show();
                if (isAvailable) {
                    if (CounterFlag > TSP_NBTAvailArr.size()) {
                        Toast.makeText(activity, "uploaded All", Toast.LENGTH_LONG).show();
                        ShowTsphomeScreen();
                    } else {
                        ll_slots_display.setVisibility(View.GONE);
                        ShowData();
                    }
                } else {
                    ShowData();
                }
            }else if (statusCode == 401) {
                CallLogOutFromDevice();
            } else {
                TastyToast.makeText(activity,  "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
              //  Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity,  "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
           // Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
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

    private void ShowTsphomeScreen() {
        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        /*Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);

        if (appPreferenceManager.getSelfieResponseModel() != null && c.getTimeInMillis() < appPreferenceManager.getSelfieResponseModel().getTimeUploaded()) {
            Logger.error("Aaata Gela");
            Logger.error("Selfie" + String.valueOf(appPreferenceManager.getSelfieResponseModel()));
            Logger.error("LOgeeererereeere" + String.valueOf(appPreferenceManager.getSelfieResponseModel().getTimeUploaded()));
            Logger.error("LOgeeererereeereMIllis" + String.valueOf(c.getTimeInMillis()));

            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
            i.putExtra("LEAVEINTIMATION", "0");
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            switchToActivity(activity, SelfieUploadActivity.class, new Bundle());
        }*/
    }

    private void fetchData() {
        ApiCallAsyncTask fetchSlotsAsyncTask = new AsyncTaskForRequest(activity).getTSPNBTFetchSlotDetailsRequestAsyncTask(NBT_Id);
        fetchSlotsAsyncTask.setApiCallAsyncTaskDelegate(new FetchSlotsAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchSlotsAsyncTask.execute(fetchSlotsAsyncTask);
        } else {
            TastyToast.makeText(activity,  "Please check internet connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
           // Toast.makeText(activity, getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {

        btn_proceed.setVisibility(View.VISIBLE);

        slotsDisplayAdapter = new SlotsDisplayAdapter(slotsArr, activity, new SlotsSelectionDelegate() {
            @Override
            public void onSlotSelected(ArrayList<SlotModel> selectedSlotModels) {
                selectedSlotsArr = selectedSlotModels;
                slotsDisplayAdapter.notifyDataSetChanged();
            }
        }, selectedSlotsArr);
        gv_slots.setAdapter(slotsDisplayAdapter);
        ll_slots_display.setVisibility(View.VISIBLE);
    }

    private class FetchSlotsAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                txt_no.setVisibility(View.VISIBLE);
                txt_yes.setVisibility(View.VISIBLE);
                slotsArr = new ResponseParser(activity).getSlotDetailsResponseModel(json, statusCode);
                selectedSlotsArr = new ArrayList<>();
                for (SlotModel slotModel :
                        slotsArr) {
                    if (slotModel.isMandatorySlot()) {
                        selectedSlotsArr.add(slotModel);
                    }
                }
                initData();
            } else {
                Toast.makeText(activity, "Failed to Fetch Slots", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(getApplicationContext(),HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        startActivity(i);
        activity.finish();*/
    }
}
