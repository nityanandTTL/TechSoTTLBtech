package com.thyrocare.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.models.api.request.ApplyLeaveRequestModel;

import com.thyrocare.models.api.response.Leaveapplied_responsemodel;
import com.thyrocare.models.data.LeaveNatureMasterModel;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.DateUtils;
import com.thyrocare.utils.app.Global;

import org.joda.time.DateTimeComparator;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class LeaveIntimationFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEAVE_INTIMATION";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    TextView fromdate, todate, leavetype, leaveremark, days, textcalender;
    int daysdiff;
    String defdate;
    Button Applyleave, showHistory;
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
    private LinearLayout ll_leave_days;
    Spinner sp;
    private ScrollView sc_leave;
    private FrameLayout Fl_list_history;
    private FloatingActionButton apply_leave;
    private TextView img_view_applied_leaves;
    Gson gson;
    Leaveapplied_responsemodel leaveapplied_responsemodel;

    EditText edt_test_id;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Leave Intimation");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        activity.mCurrentFragmentName = "";
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_leave_intimation_modified, container, false);


        fromDt = Calendar.getInstance();
        fromDt.add(Calendar.DAY_OF_MONTH, 1);

        toDt = Calendar.getInstance();
        toDt.add(Calendar.DAY_OF_MONTH, 1);

        initUI();


        isAutoTimeSelected();
        setListners();
        if (appPreferenceManager.getCameFrom() == 1) {
            todate.setVisibility(View.VISIBLE);

            finalsetfromdate = appPreferenceManager.getLeaveFromDate();
            finalsettodate = appPreferenceManager.getLeaveToDate();
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = (Date) formatter.parse(finalsetfromdate);
                Date date2 = (Date) formatter.parse(finalsettodate);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

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
                Log.e(TAG_FRAGMENT, "todate3: " + todate2);
                todate.setText("" + todate2);
                appPreferenceManager.setCameFrom(3);
                sp.setEnabled(false);
                sp.setClickable(false);
                int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());
                calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
                days.setText(tempDiff + "");
                //  days.setText(daysdiff + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (activity.toolbarHome != null) {
                activity.toolbarHome.setTitle("Leave Intimation");
            }
            activity.isOnHome = false;
            defdate = getCalculatedDate("dd-MM-yyyy", 2);
            todate.setVisibility(View.INVISIBLE);

            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.DAY_OF_MONTH, 1);

            Date c = cal1.getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c);

            fromdate.setText("" + formattedDate);
            Log.e(TAG_FRAGMENT, "todate1: " + defdate);

            //  Calendar calendar = toDt;//.add(Calendar.HOUR, 24);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 2);
            Date c1 = calendar.getTime();

            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate1 = df1.format(c1);

            todate.setText("" + formattedDate1);
            // todate.setText("" + defdate);
        }


        fetchLeaveDetails();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isAutoTimeSelected();
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
        img_view_applied_leaves = (TextView) rootView.findViewById(R.id.img_view_applied_leaves);
        ll_leave_days = (LinearLayout) rootView.findViewById(R.id.ll_leave_days);
        group = (RadioGroup) rootView.findViewById(R.id.group);
        textcalender = (TextView) rootView.findViewById(R.id.Calendertextview);
        days = (TextView) rootView.findViewById(R.id.et_leave_days_value);
        apply_leave = (FloatingActionButton) rootView.findViewById(R.id.apply_leave);
        Fl_list_history = (FrameLayout) rootView.findViewById(R.id.Fl_list_history);
        sc_leave = (ScrollView) rootView.findViewById(R.id.sc_leave);
        gson = new Gson();
        ///leaveapplied_responsemodel = new Leaveapplied_responsemodel();
        /*String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String defdate = (today).toString();*/


//        edt_test_id = rootView.findViewById(R.id.edt_test_id);


    }


    private void setListners() {
        img_view_applied_leaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LeaveHistoryFragment.newInstance(), false, false, LeaveHistoryFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
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
                                fromdate.setText("" + DateUtils.Req_Date_Req(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, "dd-MM-yyyy", "dd-MM-yyyy"));
                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                try {
                                    if (toDt != null && more.isChecked()) {
                                        if (fromDt.getTimeInMillis() <= toDt.getTimeInMillis()) {

                                            int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());

                                            calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
                                            if (String.valueOf(tempDiff).contains("-")) {
                                                Toast.makeText(activity, "From Date cannot be greater than To Date", Toast.LENGTH_SHORT).show();
                                            } else {
                                                days.setText(tempDiff + "");
                                            }

                                            //    days.setText(daysdiff + "");
                                        } else {
                                            Toast.makeText(activity, "From Date cannot be greater than To Date", LENGTH_SHORT).show();
                                        }
                                      /*  if(fromDt.getTimeInMillis()>toDt.getTimeInMillis()){
                                            Toast.makeText(activity, "from date cannot be greater than To Date", Toast.LENGTH_SHORT).show();
                                        }*/
                                    } else {
                                        daysdiff = 1;
                                        days.setText(1 + "");

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                try {

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

                    datePickerDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                                    Log.e(TAG_FRAGMENT, "todate: " + DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "dd-MM-yyyy"));
                                    todate.setText("" + DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "dd-MM-yyyy"));
                                    toDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                                    toDt.add(Calendar.HOUR, 24);

                                    int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());
                                    calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());

                                    days.setText(tempDiff + "");
                                    // days.setText(daysdiff + "");
                                } else {
                                    Toast.makeText(activity, "From Date cannot be greater than To Date", LENGTH_SHORT).show();
                                }
                            }
                        }, mYear, mMonth, mDay);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 2);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                if (one.isChecked()) {
                    datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                }
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

//                if (validate()){
                if (isAutoTimeSelected()) {
                    timegrtsix();
                }
              /*     leaveApply();
                }*/

//                alertdialogfunction();


            }
        });
    }

    private void alertdialogfunction() {
        if (validate()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
//                            timegrtsix();
                            leaveApply();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            dialog.dismiss();
                            break;
                    }
                }
            };
            String leave_str;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            if (sp.getSelectedItem().equals("CANCEL")) {
                leave_str = "Are you sure you want to cancel your leave ?";
            } else {
                leave_str = "Are you sure you want to apply for leave?";
            }
            builder.setMessage(leave_str).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();


        }
    }

    private void timegrtsix() {


        Date resultdate, date_to_check_six_pm = null, current_date = null, fromdate_tv = null, yyyy_mm_dd_date_from_autotime = null, yyyy_mm_dd_fromdate_tv = null, autodatetime_to_date = null, yyyy_mm_dd_date_from_autotime_new = null;
        Calendar c;
        int datediff = 0;

        c = Calendar.getInstance();
        resultdate = c.getTime();


        SimpleDateFormat simpleDateFormat_from = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);  //{"time":"2020-01-23 03:30 PM"
        SimpleDateFormat simpleDateFormat_to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat simpleDateFormat_convert = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        try {
//                            String dateform = DateUtils.Req_Date_Req(getcurrentTimeModel.getTime(), "yyyy-mm-dd hh:mm a", "yyyy-mm-dd");
            String str_auto_datetime = simpleDateFormat_from.format(resultdate);
            autodatetime_to_date = simpleDateFormat_from.parse(str_auto_datetime);

            System.out.println("rohit : " + autodatetime_to_date);

            String dateform = simpleDateFormat_to.format(autodatetime_to_date);

            date_to_check_six_pm = simpleDateFormat_from.parse(dateform + " " + "06:00 PM");

            current_date = simpleDateFormat_from.parse(str_auto_datetime);
            fromdate_tv = simpleDateFormat_convert.parse(fromdate.getText().toString());
            String date_from_tv = simpleDateFormat_to.format(fromdate_tv);

            yyyy_mm_dd_fromdate_tv = simpleDateFormat_to.parse(date_from_tv);

            yyyy_mm_dd_date_from_autotime = simpleDateFormat_to.parse(dateform);


            c = Calendar.getInstance();
            c.setTime(yyyy_mm_dd_date_from_autotime);
            c.add(Calendar.DAY_OF_MONTH, 1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            Date resultdate_added = c.getTime();
            String str_yyyy_mm_dd_date_from_server_new = simpleDateFormat_to.format(resultdate_added);

            yyyy_mm_dd_date_from_autotime_new = simpleDateFormat_to.parse(str_yyyy_mm_dd_date_from_server_new);

            System.out.println("\n rohit k :" + autodatetime_to_date + " current date and time");
            System.out.println("rohit k :" + date_to_check_six_pm + " validate date and time");
            System.out.println("rohit k :" + yyyy_mm_dd_fromdate_tv + " textview date");
            System.out.println("rohit k :" + yyyy_mm_dd_date_from_autotime + " current date");
            System.out.println("rohit k :" + yyyy_mm_dd_date_from_autotime_new + " curent date with 1 day added \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (current_date != null && date_to_check_six_pm != null) {
            if (current_date.before(date_to_check_six_pm)) {

                alertdialogfunction();
                System.out.println("rohit : " + current_date.toString() + " befor " + date_to_check_six_pm.toString());
//                        Toast.makeText(getContext(), current_date.toString() + " befor " + date_to_check_six_pm.toString(), Toast.LENGTH_SHORT).show();
            } else {
                if (yyyy_mm_dd_date_from_autotime_new != null && yyyy_mm_dd_fromdate_tv != null) {
                    if (yyyy_mm_dd_date_from_autotime_new.equals(yyyy_mm_dd_fromdate_tv)) {

                        System.out.println("rohit : not  allow as between " + yyyy_mm_dd_fromdate_tv.toString() + " and " + yyyy_mm_dd_date_from_autotime.toString() + " date diff is " + datediff);
                        if (sp.getSelectedItem().equals("CANCEL")) {
                            Toast.makeText(getContext(), "You cannot apply to cancel leave for next day after 6 pm", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "You cannot apply for leave for next day after 6 pm", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        alertdialogfunction();
                        System.out.println("rohit :  allow as between " + yyyy_mm_dd_fromdate_tv.toString() + " and " + yyyy_mm_dd_date_from_autotime.toString() + " date diff is " + datediff);
                        // Toast.makeText(getContext(), "allowed for leave", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Some thing went wrong", LENGTH_SHORT).show();
                }


            }

        }


    }


    private boolean isAutoTimeSelected() {
        final boolean[] isAutoTimeSelected = new boolean[1];
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME) == 1 && Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME_ZONE) == 1) {
                    isAutoTimeSelected[0] = true;
                } else {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setTitle("Warning ").setMessage("You have to Enable Automatic date and time/Timezone settings").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                                        isAutoTimeSelected[0] = true;
                                    } else {
                                        isAutoTimeSelected[0] = false;

                                    }
                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder1.setCancelable(false);
                    builder1.show();
                }

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isAutoTimeSelected[0];
    }


    private int getDifferenceBetweenDate(String fromDate, String toDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int difference_in_days = 0;
        try {
            Date date1 = simpleDateFormat.parse("" + fromDate);
            Date date2 = simpleDateFormat.parse("" + toDate);

            difference_in_days = printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference_in_days;
    }

    private int printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        int y = (int) elapsedDays;
        return y + 1;
    }

    private void leaveApply() {
        if (validate()) {

//                Toast.makeText(getActivity(), "Days:" + daysdiff, LENGTH_SHORT).show();


            ApplyLeaveRequestModel applyLeaveRequestModel = new ApplyLeaveRequestModel();
            if (sp.getSelectedItem().equals("CANCEL")) {
                applyLeaveRequestModel.setNature(7);
            } else {
                applyLeaveRequestModel.setNature(leaveNatureModel.getId());
            }

            applyLeaveRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
            applyLeaveRequestModel.setLeaveType("NA");//As Per Ganesh Sir Remarks
            applyLeaveRequestModel.setFromdate(DateUtils.Req_Date_Req(fromdate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
            applyLeaveRequestModel.setTodate(DateUtils.Req_Date_Req(todate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
            applyLeaveRequestModel.setRemarks(leaveremark.getText().toString());
            applyLeaveRequestModel.setEnteredBy(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));

            if (one.isChecked()) {
                daysdiff = 1;
                days.setText("1");
                applyLeaveRequestModel.setTodate(DateUtils.Req_Date_Req(fromdate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
                applyLeaveRequestModel.setDays(daysdiff);
            } else {
                int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());
                daysdiff = tempDiff;
                days.setText(tempDiff + "");
                // days.setText(daysdiff + "");
                applyLeaveRequestModel.setTodate(DateUtils.Req_Date_Req(todate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
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
        try {
            if (sp.getSelectedItem().equals("--SELECT--")) {
                TastyToast.makeText(activity, "Select Nature", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!leaveremark.getText().toString().equals("") /*&& leaveremark.getText().toString().matches(".{8,}")*/ &&
                leaveremark.getText().toString().startsWith("@") || leaveremark.getText().toString().startsWith("!")
                || leaveremark.getText().toString().startsWith("#") || leaveremark.getText().toString().startsWith("$")
                || leaveremark.getText().toString().startsWith("%") || leaveremark.getText().toString().startsWith("^")
                || leaveremark.getText().toString().startsWith("&") || leaveremark.getText().toString().startsWith("*")
                || leaveremark.getText().toString().startsWith("(") || leaveremark.getText().toString().startsWith(")")
                || leaveremark.getText().toString().startsWith(".") || leaveremark.getText().toString().startsWith(" ")) {
            TastyToast.makeText(activity, "Invalid remark!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;

        }
        if(TextUtils.isEmpty(leaveremark.getText().toString())){
            TastyToast.makeText(activity, "Kindly Enter Remark", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            return false;
        }
        return true;
    }

    private void calNumDays(long toTime, long fromTime) {
       /* String to = new SimpleDateFormat("MM/dd/yyyy").format(new Date(toTime));
        String from = new SimpleDateFormat("MM/dd/yyyy").format(new Date(fromTime));

        System.out.println(from +"   "+to);
        long diffTime = toTime - fromTime;
        daysdiff = (int) (diffTime / (1000 * 60 * 60 * 24));*/

        int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());
        daysdiff = tempDiff;
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
            if (!TextUtils.isEmpty(json)){
                JSONObject jsonObject=new JSONObject(json);
                try {

                    leaveapplied_responsemodel = gson.fromJson(jsonObject.toString(),Leaveapplied_responsemodel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (leaveapplied_responsemodel!=null&&leaveapplied_responsemodel.getRES_ID()!=null){
                    if (leaveapplied_responsemodel.getRES_ID().equalsIgnoreCase("RES0000")){
                        Toast.makeText(getActivity(), leaveapplied_responsemodel.getRESPONSE().toString(), LENGTH_SHORT).show();
                        appPreferenceManager.setLeaveFlag(0);
                        appPreferenceManager.setCameFrom(0);
                        activity.toolbarHome.setVisibility(View.VISIBLE);
                        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);

                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                        alertDialog.setMessage("" + leaveapplied_responsemodel.getRESPONSE().toString());
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.show();
                    }
                }else {
                    Toast.makeText(activity, "Some thing went wrong.", LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(activity, "Some thing went wrong.", LENGTH_SHORT).show();
            }

            /*if (statusCode == 200) {


                Toast.makeText(getActivity(), json, LENGTH_SHORT).show();
                appPreferenceManager.setLeaveFlag(0);
                appPreferenceManager.setCameFrom(0);
                activity.toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);


            } else {

                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

                alertDialog.setMessage("" + json);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                alertDialog.show();

            }*/
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }


    private void initData() {

        Nature = new ArrayList<>();

        Nature.add(0, "--SELECT--");

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
                    ll_leave_days.setVisibility(View.VISIBLE);
                    todate.setVisibility(View.VISIBLE);
                    todate.setText("" + defdate);
                    textcalender.setText("Leave From:");
                    Toast.makeText(getActivity(), text, LENGTH_SHORT).show();
                    if (fromDt == null) {
                        Calendar calendar = toDt;//.add(Calendar.HOUR, 24);
                        calendar.add(Calendar.HOUR, 24);
                        calNumDays(toDt.getTimeInMillis(), calendar.getTimeInMillis());
                    } else {
                        Calendar calendar = toDt;//.add(Calendar.HOUR, 24);
                        calendar.add(Calendar.HOUR, 24);
                        calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
                    }
                    int tempDiff = getDifferenceBetweenDate(fromdate.getText().toString(), todate.getText().toString());
                    days.setText("" + tempDiff);
                    //  days.setText("" + daysdiff);
                } else {
                    ll_leave_days.setVisibility(View.GONE);

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, 1);

                    Date c = cal.getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c);

                    fromdate.setText("" + formattedDate);

                    textcalender.setText("Leave On:");
                    daysdiff = 1;
                    days.setText("1");
                    // days.setText(daysdiff + "");
                    todate.setText("");
                    todate.setVisibility(View.GONE);
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


}
