package com.thyrocare.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.models.api.response.RemarksResponseModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Orion on 4/24/2017.
 */

public class RescheduleOrderDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private Dialog d;
    private Button btn_yes, btn_no;
    private TextView txt_from_date;
    private EditText edt_remark;
    private Calendar now;
    private int sampleCollectedYear;
    private int sampleCollectedMonth;
    private int sampleCollectedDay;
    Calendar mCalendarOpeningTime;
    Calendar mCalendarClosingTime;
    AppPreferenceManager appPreferenceManager;
    private Spinner spn_remark;
    private RemarksResponseModel remarksResponseModelmain;
    private ArrayList<RemarksResponseModel> remarksResponseModelsarr;
    private ArrayList<String> remarks;
    int onlyTime;
    private OrderRescheduleDialogButtonClickedDelegate orderRescheduleDialogButtonClickedDelegate;
    private OrderDetailsModel orderDetailsModel;
    private int TIME_PICKER_INTERVAL = 30;

    public RescheduleOrderDialog(Activity activity, OrderRescheduleDialogButtonClickedDelegate orderRescheduleDialogButtonClickedDelegate, OrderDetailsModel orderDetailsModel) {
        super(activity);
        this.activity = activity;
        this.orderRescheduleDialogButtonClickedDelegate = orderRescheduleDialogButtonClickedDelegate;
        this.orderDetailsModel = orderDetailsModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reschedule);
        initUI();
        setListners();
        now = Calendar.getInstance();
        fetchremarks();
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
        txt_from_date.setOnClickListener(this);

    }

    private void fetchremarks() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLeaveDetailApiAsyncTask = asyncTaskForRequest.getremarksRequestAsyncTask(11);
        fetchLeaveDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchRemarksDetailsApiAsyncTaskDelegateResult());

        fetchLeaveDetailApiAsyncTask.execute(fetchLeaveDetailApiAsyncTask);


    }

    private class FetchRemarksDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                remarksResponseModelsarr = new ArrayList<>();

                remarksResponseModelsarr = responseParser.getRemarksResponseModel(json, statusCode);
                if (remarksResponseModelsarr != null) {
                    remarks = new ArrayList<>();
                    //jai
                    remarks.add(0, "--SELECT--");
                    //jai
                    for (final RemarksResponseModel remarksResponseModelss :
                            remarksResponseModelsarr) {
                        remarks.add(remarksResponseModelss.getReason().toUpperCase());
                        remarksResponseModelmain = new RemarksResponseModel();
                        ArrayAdapter<String> spinneradapter71 = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, remarks);
                        spinneradapter71.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_remark.setAdapter(spinneradapter71);
                        edt_remark.setSelection(0);
                        remarksResponseModelmain = remarksResponseModelsarr.get(0);

                        spn_remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //jai

                                //  remarksResponseModelmain = remarksResponseModelsarr.get(position);
                                //  String Remarksstr = remarks.get(position);
                                if (position > 0) {

                                    remarksResponseModelmain = remarksResponseModelsarr.get(position - 1);
                                    String Remarksstr = remarks.get(position - 1);
                                    if (Remarksstr.equals("OTHERS")) {
                                        edt_remark.setVisibility(View.VISIBLE);

                                    } else {
                                        edt_remark.setVisibility(View.GONE);

                                    }

                                    for (RemarksResponseModel RRM :
                                            remarksResponseModelsarr) {
                                        if (RRM.getReason().equals(Remarksstr)) {

                                            remarksResponseModelmain = RRM;

                                            break;
                                        }
                                    }
                                }
                                //jai


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }


                }


            }

        }


        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }

    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        txt_from_date = (TextView) findViewById(R.id.txt_from_date);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
        spn_remark = (Spinner) findViewById(R.id.spn_remark);
    }

    private boolean valtdate() {
        if (edt_remark.isShown() && InputUtils.isNull(edt_remark.getText().toString().trim())) {
            TastyToast.makeText(activity, "Please Enter Remark", TastyToast.LENGTH_LONG, TastyToast.WARNING);

            // Toast.makeText(activity, R.string.enter_remarks, Toast.LENGTH_SHORT).show();
            return false;
        } else if (txt_from_date.getText().toString().equals("")) {
            TastyToast.makeText(activity, "Select date and time", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            //  Toast.makeText(activity, "Select date and time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spn_remark.getSelectedItem().equals("--SELECT--")) {
            TastyToast.makeText(activity, "Select Remark", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            //  Toast.makeText(activity, "Select date and time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (valtdate()) {

                if (edt_remark.isShown() && edt_remark != null) {

                    orderRescheduleDialogButtonClickedDelegate.onOkButtonClicked(orderDetailsModel, edt_remark.getText().toString() + " " + remarksResponseModelmain.getReason().trim(), txt_from_date.getText().toString());
                } else {
                   Logger.error("remark: "+remarksResponseModelmain.getReason().trim());
                   orderRescheduleDialogButtonClickedDelegate.onOkButtonClicked(orderDetailsModel, remarksResponseModelmain.getReason().trim(), txt_from_date.getText().toString());

                }
                dismiss();
            }
        }
        if (v.getId() == R.id.btn_no) {
            orderRescheduleDialogButtonClickedDelegate.onCancelButtonClicked();
            dismiss();
        }
        if (v.getId() == R.id.txt_from_date) {
            sampleCollectedYear = now.get(Calendar.YEAR);
            sampleCollectedMonth = now.get(Calendar.MONTH);
            sampleCollectedDay = now.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    sampleCollectedDay = dayOfMonth;
                    sampleCollectedMonth = monthOfYear;
                    sampleCollectedYear = year;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String yyyy = sampleCollectedYear + "";
                            String MM = (sampleCollectedMonth + 1) < 10 ? "0" + (sampleCollectedMonth + 1) : (sampleCollectedMonth + 1) + "";
                            String dd = sampleCollectedDay < 10 ? "0" + sampleCollectedDay : sampleCollectedDay + "";
                            String HH = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                            minute = getRoundedMinute(minute);
                            String mm = minute < 10 ? "0" + minute : minute + "";
                            onlyTime = Integer.parseInt(HH);
                            Logger.error(" onlyTime " + onlyTime);
                            //SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");

                            String sampleCollectedTime = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm;

                            Logger.error("sampleCollectedTime: " + sampleCollectedTime);
                            Calendar cl = Calendar.getInstance();
                            cl.set(sampleCollectedYear, sampleCollectedMonth, sampleCollectedDay, hourOfDay, minute);
                            if (cl.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                if (5 <= onlyTime && onlyTime <= 17) {
                                    Logger.error("onlyTime1 " + onlyTime);
                                    txt_from_date.setText(sampleCollectedTime);
                                } else {
                                    Logger.error("onlyTime2 " + onlyTime);
                                    TastyToast.makeText(activity, "Select time between 5 AM to 5 PM", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                    //Toast.makeText(activity, "Select time between 5 AM to 5 PM", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                TastyToast.makeText(activity, "Past time cannot be selected", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                //  Toast.makeText(activity, "Past time cannot be selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), DateFormat.is24HourFormat(activity));

                    timePickerDialog.show();


                }
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();


        }
    }


    private int getRoundedMinute(int minute) {
        if (minute % TIME_PICKER_INTERVAL != 0) {
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)
                minute = 0;
        }

        return minute;
    }
}
