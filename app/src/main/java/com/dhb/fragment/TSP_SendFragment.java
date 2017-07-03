package com.dhb.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.uiutils.AbstractFragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TSP_SendFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = TSP_SendFragment.class.getSimpleName();
    View rootview;
    static EditText edt_datetimepicker;
    private int sampleCollectedYear;
    private int sampleCollectedMonth;
    private int sampleCollectedDay;
    private Calendar now;
    private int TIME_PICKER_INTERVAL = 30;

    public TSP_SendFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_tsp__send, container, false);
        getActivity().setTitle("TSP");

        initUI();
        listeners();
        return rootview;
    }

    private void listeners() {
        // Get Current Date
        edt_datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sampleCollectedYear = now.get(Calendar.YEAR);
                sampleCollectedMonth = now.get(Calendar.MONTH);
                sampleCollectedDay = now.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sampleCollectedDay = dayOfMonth;
                        sampleCollectedMonth = monthOfYear;
                        sampleCollectedYear = year;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String yyyy = sampleCollectedYear + "";
                                String MM = (sampleCollectedMonth + 1) < 10 ? "0" + (sampleCollectedMonth + 1) : (sampleCollectedMonth + 1) + "";
                                String dd = sampleCollectedDay < 10 ? "0" + sampleCollectedDay : sampleCollectedDay + "";
                                String HH = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                                //minute = getRoundedMinute(minute);
                                String mm = minute < 10 ? "0" + minute : minute + "";
                                String sampleCollectedTime = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm;
                                Calendar cl = Calendar.getInstance();
                                cl.set(sampleCollectedYear, sampleCollectedMonth, sampleCollectedDay, hourOfDay, minute);
                                if (cl.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                                    edt_datetimepicker.setText(sampleCollectedTime);
                                } else {
                                    Toast.makeText(getActivity(), "Past time cannot be selected", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
                        timePickerDialog.show();
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });
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

    public void initUI() {
        super.initUI();
        edt_datetimepicker = (EditText) rootview.findViewById(R.id.edt_datetimepicker);
        now = Calendar.getInstance();
    }

    public static Fragment newInstance() {
        return new TSP_SendFragment();
    }

}
