package com.dhb.fragment;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.models.api.request.ApplyLeaveRequestModel;
import com.dhb.models.data.LeaveNatureMasterModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class LeaveIntimationFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEAVE_INTIMATION_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    TextView fromdate, todate, leavetype, leaveremark, days, textcalender;
    int daysdiff;
    String defdate;
    Button Applyleave;
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
        activity.isOnHome = false;
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

            } catch (ParseException e) {
                e.printStackTrace();
            }

            fromdate.setText("" + fromdate1);
            todate.setText("" + todate2);
            appPreferenceManager.setCameFrom(3);
            sp.setEnabled(false);
            sp.setClickable(false);

        } else {

            defdate = getCalculatedDate("yyyy-MM-dd", 1);
            todate.setVisibility(View.INVISIBLE);
            fromdate.setText(defdate);
            todate.setText(defdate);
        }


        fetchLeaveDetails();

        return rootView;
    }


    private void setListners() {


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


                                todate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                toDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                toDt.add(Calendar.HOUR, 24);
                            }
                        }, mYear, mMonth, mDay);


                datePickerDialog.show();


            }
        });


        Applyleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long diffTime = toDt.getTimeInMillis() - fromDt.getTimeInMillis();
                daysdiff = (int) (diffTime / (1000 * 60 * 60 * 24));

                days.setText(daysdiff + "");
                Toast.makeText(getActivity(), "Days:" + daysdiff, LENGTH_SHORT).show();


                ApplyLeaveRequestModel applyLeaveRequestModel = new ApplyLeaveRequestModel();
                applyLeaveRequestModel.setNature(leaveNatureModel.getId());
                applyLeaveRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                applyLeaveRequestModel.setLeaveType("NA");//As Per Ganesh Sir Remarks
                applyLeaveRequestModel.setFromdate(fromdate.getText().toString());
                applyLeaveRequestModel.setTodate(todate.getText().toString());
                applyLeaveRequestModel.setRemarks(leaveremark.getText().toString());
                applyLeaveRequestModel.setDays(daysdiff);
                applyLeaveRequestModel.setEnteredBy(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));


                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                ApiCallAsyncTask setApplyLeaveDetailApiAsyncTask = asyncTaskForRequest.getPostApplyLeaveRequestAsyncTask(applyLeaveRequestModel);
                setApplyLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new LeaveIntimationFragment.setApplyLeaveDetailsApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    setApplyLeaveDetailApiAsyncTask.execute(setApplyLeaveDetailApiAsyncTask);
                } else {
                    Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
                }

            }
        });
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
                Toast.makeText(getActivity(), "Success", LENGTH_SHORT).show();


                Fragment mFragment = new HomeScreenFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment).commit();
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
        if (leaveNatureMasterModels != null && leaveNatureMasterModels.size() > 0) {
            for (LeaveNatureMasterModel leaveNatureMasterModel :
                    leaveNatureMasterModels) {
                Nature.add(leaveNatureMasterModel.getNature());
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
        /*leavetype = (EditText) rootView.findViewById(R.id.et_leave_type);*/
        leaveremark = (EditText) rootView.findViewById(R.id.et_leave_days_remark);
        one = (RadioButton) rootView.findViewById(R.id.radio_pirates);
        more = (RadioButton) rootView.findViewById(R.id.radio_ninjas);
        group = (RadioGroup) rootView.findViewById(R.id.group);
        textcalender = (TextView) rootView.findViewById(R.id.Calendertextview);
        days = (TextView) rootView.findViewById(R.id.et_leave_days_value);

        /*String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String defdate = (today).toString();*/


    }
}
