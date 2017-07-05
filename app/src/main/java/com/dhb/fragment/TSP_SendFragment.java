package com.dhb.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.models.api.request.Tsp_Send_RequestModel;
import com.dhb.models.api.response.BtechwithHubResponseModel;
import com.dhb.models.api.response.Tsp_SendConsignment_Modes_ResponseModel;
import com.dhb.models.data.LeaveNatureMasterModel;
import com.dhb.models.data.Tsp_SendMode_DataModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.util.ArrayList;
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
    EditText edt_cid, edt_rpl, edt_cpl, edt_barcode, edt_routingmode, edt_remarks;
    Button btn_send;
    Spinner spinnerMode;
    private ArrayList<Tsp_SendMode_DataModel> tsp_sendMode_dataModelsArr;
    private ArrayList<String> Modes;
    Tsp_SendMode_DataModel tsp_sendMode_dataModel1;

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
        fetchModeData();
        listeners();
        return rootview;
    }

    private void fetchModeData() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getActivity());
        ApiCallAsyncTask fetchTspModeDataApiAsyncTask = asyncTaskForRequest.getTspModeDataApiAsyncTaskRequestAsyncTask();
        fetchTspModeDataApiAsyncTask.setApiCallAsyncTaskDelegate(new TspModeDataApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(getActivity())) {
            fetchTspModeDataApiAsyncTask.execute(fetchTspModeDataApiAsyncTask);
        } else {
            Toast.makeText(getActivity(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void listeners() {
        // Get Current DateTime...
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

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Tsp_Send_RequestModel tsp_send_requestModel = new Tsp_Send_RequestModel();

                tsp_send_requestModel.setConsignId(edt_cid.getText().toString().trim());
                tsp_send_requestModel.setRoutingMode(edt_routingmode.getText().toString().trim());
                tsp_send_requestModel.setConsignTime(edt_datetimepicker.getText().toString().trim());
                tsp_send_requestModel.setBarcode(edt_barcode.getText().toString().trim());
                tsp_send_requestModel.setRPL(Integer.parseInt(edt_rpl.getText().toString().trim()));
                tsp_send_requestModel.setCPL(Integer.parseInt(edt_cpl.getText().toString().trim()));
                tsp_send_requestModel.setRemarks(edt_remarks.getText().toString().trim());
                tsp_send_requestModel.setInstructions("");
                tsp_send_requestModel.setTSP(Integer.parseInt(appPreferenceManager.getBtechID()));
                tsp_send_requestModel.setMode(tsp_sendMode_dataModel1.getMode());

                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getActivity());
                ApiCallAsyncTask tspSendConsignmentApiAsyncTask = asyncTaskForRequest.getTspSendConsignmentRequestAsyncTask(tsp_send_requestModel);
                tspSendConsignmentApiAsyncTask.setApiCallAsyncTaskDelegate(new tspSendConsignmentApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(getActivity())) {
                    tspSendConsignmentApiAsyncTask.execute(tspSendConsignmentApiAsyncTask);
                } else {
                    Toast.makeText(getActivity(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
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
        appPreferenceManager = new AppPreferenceManager(getActivity());
        edt_datetimepicker = (EditText) rootview.findViewById(R.id.edt_datetimepicker);
        edt_cid = (EditText) rootview.findViewById(R.id.edt_cid);
        edt_rpl = (EditText) rootview.findViewById(R.id.edt_rpl);
        edt_cpl = (EditText) rootview.findViewById(R.id.edt_cpl);
        edt_barcode = (EditText) rootview.findViewById(R.id.edt_barcode);
        edt_routingmode = (EditText) rootview.findViewById(R.id.edt_routingmode);
        edt_remarks = (EditText) rootview.findViewById(R.id.edt_remarks);
        btn_send = (Button) rootview.findViewById(R.id.btn_send);
        spinnerMode = (Spinner) rootview.findViewById(R.id.spnr_mode);
        now = Calendar.getInstance();
    }

    public static Fragment newInstance() {
        return new TSP_SendFragment();
    }

    private class tspSendConsignmentApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            System.out.println("@---->\n" + json);
            if (statusCode == 200) {
                Toast.makeText(getActivity(), "" + json, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                    }
                }, 2000);

            } else {
                if (IS_DEBUG)
                    Toast.makeText(getActivity(), "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(getActivity(), "api cancelled ", Toast.LENGTH_SHORT).show();
        }
    }

    private class TspModeDataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            System.out.println("@---->\n" + json);
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(getActivity());

                tsp_sendMode_dataModelsArr = new ArrayList<>();
                tsp_sendMode_dataModelsArr = responseParser.getTspModesResponseModel(json, statusCode).getCourierModes();
                Modes = new ArrayList<>();

                if (tsp_sendMode_dataModelsArr != null && tsp_sendMode_dataModelsArr.size() > 0) {
                    for (Tsp_SendMode_DataModel tsp_sendMode_dataModel :
                            tsp_sendMode_dataModelsArr) {
                        Modes.add(tsp_sendMode_dataModel.getMode());
                    }
                }
                tsp_sendMode_dataModel1 = new Tsp_SendMode_DataModel();

                ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Modes);
                spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMode.setAdapter(spinneradapter);

                spinnerMode.setSelection(0);
                spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selection = Modes.get(position);
                        for (Tsp_SendMode_DataModel tsp_sendMode_dataModel :
                                tsp_sendMode_dataModelsArr) {
                            if (tsp_sendMode_dataModel.getMode().equals(selection)) {
                                tsp_sendMode_dataModel1 = tsp_sendMode_dataModel;
                                Logger.debug("result***" + tsp_sendMode_dataModel1.getMode());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else {
                if (IS_DEBUG)
                    Toast.makeText(getActivity(), "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(getActivity(), "api cancelled ", Toast.LENGTH_SHORT).show();
        }
    }
}
