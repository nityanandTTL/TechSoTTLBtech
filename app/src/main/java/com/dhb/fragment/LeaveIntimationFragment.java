package com.dhb.fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class LeaveIntimationFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEAVE_INTIMATION_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    TextView fromdate, todate, leavetype, leaveremark, days;
    int daysdiff;
    String defdate;
    Button Applyleave;
    RadioButton one,more;
    RadioGroup group;

    private int mYear, mMonth, mDay;
    private View rootView;
    private ArrayList<LeaveNatureMasterModel> leaveNatureMasterModels;
    private Calendar fromDt,toDt;
    private LeaveNatureMasterModel leaveNatureModel;
    private ArrayList<String> Nature;

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
        defdate = getCalculatedDate("yyyy-MM-dd", 1);
        fromdate.setText(defdate);
        todate.setText(defdate);
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
                                fromDt.set(year,monthOfYear,dayOfMonth,0,0,0);
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
                                toDt.set(year,monthOfYear,dayOfMonth,0,0,0);
                                toDt.add(Calendar.HOUR,24);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });



        Applyleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long diff;


                ApplyLeaveRequestModel applyLeaveRequestModel = new ApplyLeaveRequestModel();
                applyLeaveRequestModel.setNature(leaveNatureModel.getId());
                applyLeaveRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                applyLeaveRequestModel.setLeaveType(leavetype.getText().toString());
                applyLeaveRequestModel.setFromdate(fromdate.getText().toString());
                applyLeaveRequestModel.setTodate(todate.getText().toString());
                applyLeaveRequestModel.setRemarks(leaveremark.getText().toString());
//                applyLeaveRequestModel.setDays(Integer.parseInt(days.getText().toString()));
                applyLeaveRequestModel.setEnteredBy(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                applyLeaveRequestModel.setDays(daysdiff);
                try {
                    long diffTime = toDt.getTimeInMillis()-fromDt.getTimeInMillis();
                    daysdiff = (int) (diffTime/ (1000*60*60*24));
                    Toast.makeText(getActivity(), "Days:"+daysdiff, LENGTH_SHORT).show();
                    applyLeaveRequestModel.setDays(daysdiff);

                }
                catch(Exception e){
                    e.printStackTrace();
                }




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

            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }


    private void initData() {
        Spinner sp;
        Nature = new ArrayList<>();
        if (leaveNatureMasterModels != null && leaveNatureMasterModels.size() > 0) {
            for (LeaveNatureMasterModel leaveNatureMasterModel :
                    leaveNatureMasterModels) {
                Nature.add(leaveNatureMasterModel.getNature());
            }
        }

        leaveNatureModel = new LeaveNatureMasterModel();
        sp = (Spinner) rootView.findViewById(R.id.sp_leave_nature);
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
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton) rootView.findViewById(checkedId);
                String text  = checkedRadioButton.getText().toString();

                if (more.isChecked()){

                    todate.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),text,LENGTH_SHORT).show();
                }
                else
                {
                    todate.setVisibility(View.INVISIBLE);
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
        todate = (TextView) rootView.findViewById(R.id.txt_to_date);
        Applyleave = (Button) rootView.findViewById(R.id.btn_leave_apply);
        leavetype = (EditText) rootView.findViewById(R.id.et_leave_type);
        leaveremark = (EditText) rootView.findViewById(R.id.et_leave_days_remark);
        one=(RadioButton) rootView.findViewById(R.id.radio_pirates);
        more=(RadioButton) rootView.findViewById(R.id.radio_ninjas);
        group=(RadioGroup) rootView.findViewById(R.id.group);

        /*String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String defdate = (today).toString();*/


    }
}