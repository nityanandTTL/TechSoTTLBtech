package com.thyrocare.btechapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.thyrocare.btechapp.models.data.TSPNBT_AvilModel;


import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DateUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

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
    private Global global;

    public Tsp_ScheduleYourDayActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_tspnbt_day);
        activity = this;
        global = new Global(activity);
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

                                if (isNetworkAvailable(activity)) {
                                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel,false);
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

                if (isNetworkAvailable(activity)) {
                    callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel,true);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.internet_connetion_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callBtechAvailabilityRequestApi(SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel, final boolean isAvailable) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.callBtechAvailabilityRequestApi(setBtechAvailabilityAPIRequestModel);
        global.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful()) {
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
                }else if (response.code() == 401) {
                    CallLogOutFromDevice();
                } else {
                    TastyToast.makeText(activity,  "Failed to set Availability", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    //  Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void CallLogOutFromDevice() {
        try {
            TastyToast.makeText(activity, "Authorization failed, need to Login again...", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
             new LogUserActivityTagging(activity, LOGOUT);
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
        if (isNetworkAvailable(activity)) {
            CallFetchSlotDetailsApi();
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

    private void CallFetchSlotDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<SlotModel>> responseCall = apiInterface.CallFetchSlotDetailsApi(NBT_Id);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback< ArrayList<SlotModel>>() {
            @Override
            public void onResponse(Call< ArrayList<SlotModel>> call, retrofit2.Response< ArrayList<SlotModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    txt_no.setVisibility(View.VISIBLE);
                    txt_yes.setVisibility(View.VISIBLE);
                    slotsArr = response.body();
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
            public void onFailure(Call< ArrayList<SlotModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(getApplicationContext(),HomeScreenActivity.class);
        i.putExtra("LEAVEINTIMATION", "0");
        startActivity(i);
        activity.finish();*/
    }
}
