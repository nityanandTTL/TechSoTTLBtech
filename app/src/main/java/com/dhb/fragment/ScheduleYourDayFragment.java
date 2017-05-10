package com.dhb.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.adapter.SlotsDisplayAdapter;
import com.dhb.delegate.SlotsSelectionDelegate;
import com.dhb.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.dhb.models.data.SlotModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleYourDayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "SCHEDULE_YOUR_DAY_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtNo,txtYes;
    private LinearLayout llSlotsDisplay;
    private GridView gvSlots;
    private Button btnProceed;
    private ArrayList<SlotModel> slotsArr;
    private ArrayList<SlotModel> selectedSlotsArr;
    private SlotsDisplayAdapter slotsDisplayAdapter;
    private boolean isAvailable = false;
    private SetBtechAvailabilityAPIRequestModel savedModel;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
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
        rootView = inflater.inflate(R.layout.fragment_schedule_your_day, container, false);
        initUI();
        initListeners();
        return rootView;
    }

    private void initListeners() {
        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtYes.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtNo.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                llSlotsDisplay.setVisibility(View.VISIBLE);
                isAvailable = true;
                btnProceed.setVisibility(View.VISIBLE);
                fetchData();
            }
        });
        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtYes.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
                llSlotsDisplay.setVisibility(View.GONE);
                btnProceed.setVisibility(View.INVISIBLE);
                isAvailable = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Are you sure that you are not available tomorrow ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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
                                calendar.add(Calendar.DAY_OF_MONTH,1);
                                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));


                                Fragment mfrFragment = new LeaveIntimationFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_homeScreen,mfrFragment).commit();

                                ApiCallAsyncTask setBtechAvailabilityAsyncTask = new AsyncTaskForRequest(activity).getPostBtechAvailabilityRequestAsyncTask(setBtechAvailabilityAPIRequestModel);
                                setBtechAvailabilityAsyncTask.setApiCallAsyncTaskDelegate(new SetBtechAvailabilityAsyncTaskDelegateResult());
                                if(isNetworkAvailable(activity)){
                                    setBtechAvailabilityAsyncTask.execute(setBtechAvailabilityAsyncTask);
                                }
                                else{
                                    Toast.makeText(activity,activity.getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
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
                if(selectedSlotsArr!=null && selectedSlotsArr.size()>0) {
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
                calendar.add(Calendar.DAY_OF_MONTH,1);
                setBtechAvailabilityAPIRequestModel.setAvailableDate(sdf.format(calendar.getTime()));

                ApiCallAsyncTask setBtechAvailabilityAsyncTask = new AsyncTaskForRequest(activity).getPostBtechAvailabilityRequestAsyncTask(setBtechAvailabilityAPIRequestModel);
                setBtechAvailabilityAsyncTask.setApiCallAsyncTaskDelegate(new SetBtechAvailabilityAsyncTaskDelegateResult());
                if(isNetworkAvailable(activity)){
                    setBtechAvailabilityAsyncTask.execute(setBtechAvailabilityAsyncTask);
                }
                else{
                    Toast.makeText(activity,activity.getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchData() {
        ApiCallAsyncTask fetchSlotsAsyncTask = new AsyncTaskForRequest(activity).getFetchSlotDetailsRequestAsyncTask();
        fetchSlotsAsyncTask.setApiCallAsyncTaskDelegate(new FetchSlotsAsyncTaskDelegateResult());
        if(isNetworkAvailable(activity)){
            fetchSlotsAsyncTask.execute(fetchSlotsAsyncTask);
        }else{
            Toast.makeText(activity,getResources().getString(R.string.internet_connetion_error),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        txtYes = (TextView) rootView.findViewById(R.id.txt_yes);
        txtNo = (TextView) rootView.findViewById(R.id.txt_no);
        btnProceed = (Button) rootView.findViewById(R.id.btn_proceed);
        btnProceed.setVisibility(View.INVISIBLE);
        llSlotsDisplay = (LinearLayout) rootView.findViewById(R.id.ll_slots_display);
        gvSlots = (GridView) rootView.findViewById(R.id.gv_slots);
    }

    private class FetchSlotsAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                slotsArr = new ResponseParser(activity).getSlotDetailsResponseModel(json,statusCode);
                selectedSlotsArr = new ArrayList<>();
                for (SlotModel slotModel :
                        slotsArr) {
                    if(slotModel.isMandatorySlot()){
                        selectedSlotsArr.add(slotModel);
                    }
                }
                initData();
            }
            else{
                Toast.makeText(activity,"Failed to Fetch Slots",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void initData() {
        slotsDisplayAdapter = new SlotsDisplayAdapter(slotsArr, activity, new SlotsSelectionDelegate() {
            @Override
            public void onSlotSelected(ArrayList<SlotModel> selectedSlotModels) {
                selectedSlotsArr = selectedSlotModels;
                slotsDisplayAdapter.notifyDataSetChanged();
            }
        },selectedSlotsArr);
        gvSlots.setAdapter(slotsDisplayAdapter);
        llSlotsDisplay.setVisibility(View.VISIBLE);
    }

    private class SetBtechAvailabilityAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200||statusCode==201){
                Toast.makeText(activity,"Availability set Successfully",Toast.LENGTH_SHORT).show();
                appPreferenceManager.setBtechAvailabilityResponseModel(new Gson().fromJson(json,SetBtechAvailabilityAPIRequestModel.class));
                appPreferenceManager.setSelectedSlotsArr(selectedSlotsArr);
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
            else{
                Toast.makeText(activity, "Failed to set Availability", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity,"Failed to set Availability",Toast.LENGTH_SHORT).show();
        }
    }
}
