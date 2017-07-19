package com.dhb.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.dhb.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;

import java.util.Calendar;

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
    AppPreferenceManager appPreferenceManager;

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
    }

    private void setListners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
        txt_from_date.setOnClickListener(this);

    }

    private void initUI() {
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
        txt_from_date = (TextView) findViewById(R.id.txt_from_date);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
    }

    private boolean valtdate() {
        if (InputUtils.isNull(edt_remark.getText().toString().trim())) {
            Toast.makeText(activity, R.string.enter_remarks, Toast.LENGTH_SHORT).show();
            return false;
        } else if (txt_from_date.getText().toString().equals("")) {
            Toast.makeText(activity, "Select date and time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (valtdate()) {
                orderRescheduleDialogButtonClickedDelegate.onOkButtonClicked(orderDetailsModel, edt_remark.getText().toString().trim(), txt_from_date.getText().toString());
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
                            String sampleCollectedTime = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm;
                            Calendar cl = Calendar.getInstance();
                            cl.set(sampleCollectedYear, sampleCollectedMonth, sampleCollectedDay, hourOfDay, minute);
                            if (cl.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                txt_from_date.setText(sampleCollectedTime);
                            } else {
                                Toast.makeText(activity, "Past time cannot be selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), DateFormat.is24HourFormat(activity));
                    timePickerDialog.show();
                }
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
