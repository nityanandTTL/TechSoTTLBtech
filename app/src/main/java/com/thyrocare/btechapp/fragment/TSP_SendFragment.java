package com.thyrocare.btechapp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.Tsp_HubScanBarcodeListAdapter;
import com.thyrocare.btechapp.models.api.request.Tsp_Send_RequestModel;
import com.thyrocare.btechapp.models.api.response.Tsp_ScanBarcodeResponseModel;
import com.thyrocare.btechapp.models.data.Tsp_ScanBarcodeDataModel;
import com.thyrocare.btechapp.models.data.Tsp_SendMode_DataModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * i)/SpecimenTrack/TSPScannedBarcode/Btechid<br/>
 * ii)/SpecimenTrack/SendConsignment<br/>
 * iii)/SpecimenTrack/CourierModes<br/>
 */
public class TSP_SendFragment extends AppCompatActivity implements Tsp_HubScanBarcodeListAdapter.CallbackInterface {

    public static final String TAG_FRAGMENT = TSP_SendFragment.class.getSimpleName();
    View rootview;
    EditText edt_datetimepicker;
    private int sampleCollectedYear;
    private int sampleCollectedMonth;
    private int sampleCollectedDay;
    private Calendar now;
    private int TIME_PICKER_INTERVAL = 30;
    EditText edt_cid, edt_rpl, edt_cpl, edt_routingmode, edt_remarks;//edt_barcode;
    Button btn_send;
    Spinner spinnerMode;
    private ArrayList<Tsp_SendMode_DataModel> tsp_sendMode_dataModelsArr;
    private ArrayList<String> Modes;
    Tsp_SendMode_DataModel tsp_sendMode_dataModel1;
    String regexp = ".{1,7}";
    /*barcode*/
    private IntentIntegrator intentIntegrator;
    private RecyclerView recyclerView;
    private ArrayList<Tsp_ScanBarcodeDataModel> barcodeModels = new ArrayList<>();
    private Tsp_HubScanBarcodeListAdapter hubScanBarcodeListAdapter;
    private Global global;
    private Activity mActivity;
    AppPreferenceManager appPreferenceManager;

    TextView tv_toolbar;
    ImageView iv_back,iv_home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tsp__send);
        mActivity = this;
        appPreferenceManager = new AppPreferenceManager(mActivity);
        global = new Global(mActivity);
        initUI();
        fetchBarcodeData();/***Barcode***/
        //fetchModeData();
        listeners();
    }


    private void fetchModeData() {
        if (isNetworkAvailable(mActivity)) {
            CallgetTspModeDataApi();
        } else {
            Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void listeners() {
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get Current DateTime...
        edt_datetimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sampleCollectedYear = now.get(Calendar.YEAR);
                sampleCollectedMonth = now.get(Calendar.MONTH);
                sampleCollectedDay = now.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sampleCollectedDay = dayOfMonth;
                        sampleCollectedMonth = monthOfYear;
                        sampleCollectedYear = year;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
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
                                    Toast.makeText(mActivity, "Past time cannot be selected", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), DateFormat.is24HourFormat(mActivity));
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

                if (validate()) {
                    View view = mActivity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    Tsp_Send_RequestModel tsp_send_requestModel = new Tsp_Send_RequestModel();

                    tsp_send_requestModel.setConsignId(edt_cid.getText().toString().trim());
                    tsp_send_requestModel.setRoutingMode(edt_routingmode.getText().toString().trim());
                    tsp_send_requestModel.setConsignTime(edt_datetimepicker.getText().toString().trim());
                    //tsp_send_requestModel.setBarcode(edt_barcode.getText().toString().trim());
                    /***/
                    ArrayList<Tsp_ScanBarcodeDataModel> scannedBarcodesArr = new ArrayList<>();
                    for (Tsp_ScanBarcodeDataModel hbm :
                            barcodeModels) {
                        if (hbm.isReceived()) {
                            scannedBarcodesArr.add(hbm);
                        }
                    }
                    tsp_send_requestModel.setTstBarcode(scannedBarcodesArr);

                    tsp_send_requestModel.setRPL(Integer.parseInt(edt_rpl.getText().toString().trim()));
                    tsp_send_requestModel.setCPL(Integer.parseInt(edt_cpl.getText().toString().trim()));
                    tsp_send_requestModel.setRemarks(edt_remarks.getText().toString().trim());
                    tsp_send_requestModel.setInstructions("");
                    tsp_send_requestModel.setTSP(Integer.parseInt(appPreferenceManager.getBtechID()));
                    tsp_send_requestModel.setMode(tsp_sendMode_dataModel1.getMode());

                    if (isNetworkAvailable(mActivity)) {
                        CallgetTspSendConsignmentAPI(tsp_send_requestModel);
                    } else {
                        Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        edt_routingmode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_routingmode.getText().toString().trim().length() > 7) {
                    edt_routingmode.setError(getString(R.string.tsp_routemode_criteria));
                }
            }
        });
    }

    private boolean validate() {

        if (edt_cid.getText().toString().length() == 0) {
            edt_cid.setError(getString(R.string.tsp_empty_criteria));
            edt_cid.requestFocus();
            return false;
        } else if (edt_rpl.getText().toString().length() == 0) {
            edt_rpl.setError(getString(R.string.tsp_empty_criteria));
            edt_rpl.requestFocus();
            return false;
        } else if (edt_cpl.getText().toString().length() == 0) {
            edt_cpl.setError(getString(R.string.tsp_empty_criteria));
            edt_cpl.requestFocus();
            return false;
        } /* else if (!edt_routingmode.getText().toString().matches(regexp)) {
            edt_routingmode.setError(getString(R.string.tsp_routemode_criteria));
            edt_routingmode.requestFocus();
            return false;
        } */
        return true;
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
        edt_datetimepicker = (EditText) findViewById(R.id.edt_datetimepicker);
        edt_cid = (EditText) findViewById(R.id.edt_cid);
        edt_rpl = (EditText) findViewById(R.id.edt_rpl);
        edt_cpl = (EditText) findViewById(R.id.edt_cpl);
        //edt_barcode = (EditText) findViewById(R.id.edt_barcode);
        edt_routingmode = (EditText) findViewById(R.id.edt_routingmode);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        btn_send = (Button) findViewById(R.id.btn_send);
        spinnerMode = (Spinner) findViewById(R.id.spnr_mode);
        now = Calendar.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_toolbar =  findViewById(R.id.tv_toolbar);
        iv_back =  findViewById(R.id.iv_back);
        iv_home =  findViewById(R.id.iv_home);

        tv_toolbar.setText("Send");
    }


    private void CallgetTspSendConsignmentAPI(Tsp_Send_RequestModel tsp_send_requestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallgetTspSendConsignmentAPI(tsp_send_requestModel);
        global.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage("Dispatched Successfully.");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    startActivity(new Intent(mActivity, HomeScreenActivity.class));
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(mActivity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(mActivity);
            }
        });
    }


    private void CallgetTspModeDataApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<Tsp_SendMode_DataModel>> responseCall = apiInterface.CallgetTspModeDataApi();
        global.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<ArrayList<Tsp_SendMode_DataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<Tsp_SendMode_DataModel>> call, retrofit2.Response<ArrayList<Tsp_SendMode_DataModel>> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    tsp_sendMode_dataModelsArr = new ArrayList<>();
                    tsp_sendMode_dataModelsArr = response.body();
                    Modes = new ArrayList<>();

                    if (tsp_sendMode_dataModelsArr != null && tsp_sendMode_dataModelsArr.size() > 0) {
                        for (Tsp_SendMode_DataModel tsp_sendMode_dataModel :
                                tsp_sendMode_dataModelsArr) {
                            Modes.add(tsp_sendMode_dataModel.getMode());
                        }
                    }
                    tsp_sendMode_dataModel1 = new Tsp_SendMode_DataModel();

                    ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, Modes);
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
                    global.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tsp_SendMode_DataModel>> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    /*barcode**************************************************************************************/

    /***barcode***/
    private void fetchBarcodeData() {

        if (isNetworkAvailable(mActivity)) {
            CallgetTspScanBarcodeApi();
        } else {
            Toast.makeText(mActivity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void CallgetTspScanBarcodeApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Tsp_ScanBarcodeResponseModel> responseCall = apiInterface.CallgetTspScanBarcodeApi(appPreferenceManager.getBtechID());
        global.showProgressDialog(mActivity, "Please wait..");
        responseCall.enqueue(new Callback<Tsp_ScanBarcodeResponseModel>() {
            @Override
            public void onResponse(Call<Tsp_ScanBarcodeResponseModel> call, retrofit2.Response<Tsp_ScanBarcodeResponseModel> response) {
                global.hideProgressDialog(mActivity);
                if (response.isSuccessful() && response.body() != null) {
                    Tsp_ScanBarcodeResponseModel tsp_scanBarcodeResponseModel = response.body();
                    if (tsp_scanBarcodeResponseModel != null && tsp_scanBarcodeResponseModel.getTspBarcodes().size() > 0) {
                        barcodeModels = tsp_scanBarcodeResponseModel.getTspBarcodes();
                        prepareRecyclerView();

                        fetchModeData();
                    } else {
                        //Toast.makeText(getActivity(), "No records found", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                        alertDialogBuilder.setMessage("No Barcode Records Found.");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        startActivity(new Intent(mActivity,HomeScreenActivity.class));

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                } else {
                    global.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Tsp_ScanBarcodeResponseModel> call, Throwable t) {
                global.hideProgressDialog(mActivity);
                global.showcenterCustomToast(mActivity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    /*barcode*/
    private void prepareRecyclerView() {
        Logger.debug("onsode prepareRecyclerView" + "true");
        hubScanBarcodeListAdapter = new Tsp_HubScanBarcodeListAdapter(barcodeModels, mActivity);
        hubScanBarcodeListAdapter.setOnShareClickedListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hubScanBarcodeListAdapter);
    }

    /*barcode*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
            String scanned_barcode = scanningResult.getContents();
            Logger.debug("result***" + "scanned" + scanned_barcode);

            for (int i = 0; i < barcodeModels.size(); i++) {
                if (barcodeModels.get(i).getBarcode().equals(scanned_barcode)) {
                    Logger.debug("inside loop" + "1");

                    if (barcodeModels.get(i).isReceived()) {
                        Logger.debug("inside loop" + "true");
                        Toast.makeText(mActivity, "Same Barcode is Already Scanned", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        barcodeModels.get(i).setReceived(true);
                        break;
                    }

                }
            }

            hubScanBarcodeListAdapter.notifyDataSetChanged();
        } else {
            Logger.error("Cancelled from fragment");
        }
    }

    /*barcode*/
    @Override
    public void onHandleSelection(int position) {
        intentIntegrator = new IntentIntegrator(mActivity) {
            @Override
            protected void startActivityForResult(Intent intent, int code) {
                TSP_SendFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override
            }
        };
        intentIntegrator.initiateScan();
    }
}
