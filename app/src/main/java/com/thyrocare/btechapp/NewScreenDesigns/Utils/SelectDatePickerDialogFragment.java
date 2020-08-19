package com.thyrocare.btechapp.NewScreenDesigns.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;



@SuppressLint("ValidFragment")
public class SelectDatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Activity mActivity;
    long Mindate, MaxDate;
    String Title;
    String OutputDateFormat;
    private OnDateSelectedListener onDateSelectedListener;

    public SelectDatePickerDialogFragment(Activity mActivity, String title, long mindate, long maxDate, String outputDateFormat) {
        this.mActivity = mActivity;
        Mindate = mindate;
        MaxDate = maxDate;
        Title = title;
        OutputDateFormat = outputDateFormat;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, AlertDialog.THEME_HOLO_DARK, this, yy, mm, dd);
        datePickerDialog.setTitle(Title);
        DatePicker dp = datePickerDialog.getDatePicker();
        dp.setMinDate(Mindate);
        dp.setMaxDate(MaxDate);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {
        int year = selectedYear;
        int month = selectedMonth;
        int day = selectedDay;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);


        String strSelectedDate = DateUtil.getDateFromLong(calendar.getTimeInMillis(),OutputDateFormat);
        if (onDateSelectedListener != null){
            onDateSelectedListener.onDateSelected(strSelectedDate,calendar.getTime());
        }

    }


    public void setDateSelectedListener(OnDateSelectedListener dateSelectedListener) {
        this.onDateSelectedListener = dateSelectedListener;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(String strSelectedDate, Date SelectedDate);
    }
}
