package com.thyrocare.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.models.api.request.ApplyLeaveRequestModel;
import com.thyrocare.models.data.LeaveNatureMasterModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class LeaveIntimationFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEAVE_INTIMATION_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    TextView fromdate, todate, leavetype, leaveremark, days, textcalender;
    int daysdiff;
    String defdate;
    Button Applyleave,showHistory;
    RadioButton one, more;
    RadioGroup group;
    private int mYear, mMonth, mDay;
    private View rootView;
    private ArrayList<LeaveNatureMasterModel> leaveNatureMasterModels;
    private Calendar fromDt, toDt;
    private LeaveNatureMasterModel leaveNatureModel;
    private ArrayList<String> Nature;
    String finalsetfromdate, finalsettodate;
    String fromdate1, todate2;
    Spinner sp;
    private ScrollView sc_leave;
    private FrameLayout Fl_list_history;
    private FloatingActionButton apply_leave;
    public LeaveIntimationFragment() {
        // Required empty public constructor
    }

    public static LeaveIntimationFragment newInstance() {
        LeaveIntimationFragment fragment = new LeaveIntimationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_leave_intimation, container, false);
        fromDt = Calendar.getInstance();
        toDt = Calendar.getInstance();

        initUI();


        setListners();
        if (appPreferenceManager.getCameFrom() == 1) {


            todate.setVisibility(View.VISIBLE);

            finalsetfromdate = appPreferenceManager.getLeaveFromDate();
            finalsettodate = appPreferenceManager.getLeaveToDate();
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = (Date) formatter.parse(finalsetfromdate);
                Date date2 = (Date) formatter.parse(finalsettodate);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                fromDt.setTime(date1);
                toDt.setTime(date2);
                toDt.add(Calendar.HOUR, 24);


                fromdate.setEnabled(false);
                fromdate.setClickable(false);

                todate.setEnabled(false);
                todate.setClickable(false);
                fromdate1 = sdf.format(date1);
                todate2 = sdf.format(date2);

                fromdate.setText("" + fromdate1);
                todate.setText("" + todate2);
                appPreferenceManager.setCameFrom(3);
                sp.setEnabled(false);
                sp.setClickable(false);
                calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
                days.setText(daysdiff + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (activity.toolbarHome != null) {
                activity.toolbarHome.setTitle("Leave Intimation");
            }
            activity.isOnHome = false;
            defdate = getCalculatedDate("yyyy-MM-dd", 1);
            todate.setVisibility(View.INVISIBLE);
            fromdate.setText(defdate);
            todate.setText(defdate);
        }


        fetchLeaveDetails();

        return rootView;
    }


    private void setListners() {
        apply_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc_leave.setVisibility(View.VISIBLE);
                Fl_list_history.setVisibility(View.GONE);
            }
        });

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fromdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar td = Calendar.getInstance();
                                td.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                if (fromDt.getTimeInMillis() <= td.getTimeInMillis()) {
                                    todate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    toDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                    toDt.add(Calendar.HOUR, 24);
                                    calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
                                    days.setText(daysdiff + "");
                                } else {
                                    Toast.makeText(activity, "From Date cannot be greater than To Date", LENGTH_SHORT).show();
                                }
                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.show();


            }
        });

        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LeaveHistoryFragment.newInstance(), false, false, LeaveHistoryFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        Applyleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    leaveApply();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Are you sure you want to apply for leave?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }


            }
        });
    }

    private void leaveApply() {
        if(validate()){
            calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());

//                Toast.makeText(getActivity(), "Days:" + daysdiff, LENGTH_SHORT).show();


            ApplyLeaveRequestModel applyLeaveRequestModel = new ApplyLeaveRequestModel();
            if(sp.getSelectedItem().equals("CANCEL")){
                applyLeaveRequestModel.setNature(7);
            }else {
                applyLeaveRequestModel.setNature(leaveNatureModel.getId());
            }

            applyLeaveRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
            applyLeaveRequestModel.setLeaveType("NA");//As Per Ganesh Sir Remarks
            applyLeaveRequestModel.setFromdate(fromdate.getText().toString());
            applyLeaveRequestModel.setTodate(todate.getText().toString());
            applyLeaveRequestModel.setRemarks(leaveremark.getText().toString());
            applyLeaveRequestModel.setEnteredBy(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));

            if (one.isChecked()) {
                daysdiff = 1;
                days.setText("1");
                applyLeaveRequestModel.setTodate(fromdate.getText().toString());
                applyLeaveRequestModel.setDays(daysdiff);
            } else {
                days.setText(daysdiff + "");
                applyLeaveRequestModel.setTodate(todate.getText().toString());
                applyLeaveRequestModel.setDays(daysdiff);
            }
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            ApiCallAsyncTask setApplyLeaveDetailApiAsyncTask = asyncTaskForRequest.getPostApplyLeaveRequestAsyncTask(applyLeaveRequestModel);
            setApplyLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setApplyLeaveDetailsApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                setApplyLeaveDetailApiAsyncTask.execute(setApplyLeaveDetailApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate() {
        if(sp.getSelectedItem().equals("--SELECT--")){
            TastyToast.makeText(activity, "Select Nature", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }
        return true;
    }

    private void calNumDays(long toTime, long fromTime) {
        long diffTime = toTime - fromTime;
        daysdiff = (int) (diffTime / (1000 * 60 * 60 * 24));
    }


    private void fetchLeaveDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLeaveDetailApiAsyncTask = asyncTaskForRequest.getLeaveDetailsRequestAsyncTask();
        fetchLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new LeaveIntimationFragment.FetchLeaveDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchLeaveDetailApiAsyncTask.execute(fetchLeaveDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }


    private class FetchLeaveDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                leaveNatureMasterModels = new ArrayList<>();

                leaveNatureMasterModels = responseParser.getLeaveNatureMasterResponse(json, statusCode);
                if (leaveNatureMasterModels != null && leaveNatureMasterModels.size() > 0) {

                }

                initData();
            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }

    //set Deleagte
    private class setApplyLeaveDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                Toast.makeText(getActivity(), ""+json, LENGTH_SHORT).show();
                appPreferenceManager.setLeaveFlag(0);
                appPreferenceManager.setCameFrom(0);
                activity.toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);


            }else {

                  AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                                              alertDialog.setMessage(""+json);
                                              alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                      new DialogInterface.OnClickListener() {
                                                          public void onClick(DialogInterface dialog, int which) {
                                                              dialog.dismiss();

                                                          }
                                                      });

                                              alertDialog.show();

            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }



    private void initData() {

        Nature = new ArrayList<>();

        Nature.add(0,"--SELECT--");

        if (leaveNatureMasterModels != null && leaveNatureMasterModels.size() > 0) {
            for (LeaveNatureMasterModel leaveNatureMasterModel :
                    leaveNatureMasterModels) {
                Nature.add(leaveNatureMasterModel.getNature().toUpperCase());
            }
        }

        leaveNatureModel = new LeaveNatureMasterModel();

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Nature);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinneradapter);
        sp.setSelection(0);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = Nature.get(position);
                for (LeaveNatureMasterModel leaveNatureMasterModel :
                        leaveNatureMasterModels) {
                    if (leaveNatureMasterModel.getNature().equals(selection)) {
                        leaveNatureModel = leaveNatureMasterModel;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) rootView.findViewById(checkedId);
                String text = checkedRadioButton.getText().toString();

                if (more.isChecked()) {

                    todate.setVisibility(View.VISIBLE);
                    textcalender.setText("Leave From:");
                    Toast.makeText(getActivity(), text, LENGTH_SHORT).show();
                } else {

                    todate.setVisibility(View.INVISIBLE);
                    textcalender.setText("Leave On:");
                    daysdiff = 1;
                    days.setText(daysdiff + "");
                    todate.setText("");
                }


            }
        });
    }


    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        String previous_date = s.format(new Date(cal.getTimeInMillis()));
        return previous_date;

    }

    @Override
    public void initUI() {
        super.initUI();
        fromdate = (TextView) rootView.findViewById(R.id.txt_from_date);
        sp = (Spinner) rootView.findViewById(R.id.sp_leave_nature);
        todate = (TextView) rootView.findViewById(R.id.txt_to_date);
        Applyleave = (Button) rootView.findViewById(R.id.btn_leave_apply);
        showHistory = (Button) rootView.findViewById(R.id.btn_show_history);
        /*leavetype = (EditText) rootView.findViewById(R.id.et_leave_type);*/
        leaveremark = (EditText) rootView.findViewById(R.id.et_leave_days_remark);
        one = (RadioButton) rootView.findViewById(R.id.radio_one);
        more = (RadioButton) rootView.findViewById(R.id.radio_more);
        group = (RadioGroup) rootView.findViewById(R.id.group);
        textcalender = (TextView) rootView.findViewById(R.id.Calendertextview);
        days = (TextView) rootView.findViewById(R.id.et_leave_days_value);
        apply_leave=(FloatingActionButton)rootView.findViewById(R.id.apply_leave);
        Fl_list_history=(FrameLayout)rootView.findViewById(R.id.Fl_list_history);
        sc_leave=(ScrollView)rootView.findViewById(R.id.sc_leave);
        /*String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String defdate = (today).toString();*/


    }
}
