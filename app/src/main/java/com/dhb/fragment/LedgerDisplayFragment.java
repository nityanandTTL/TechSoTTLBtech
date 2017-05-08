package com.dhb.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.models.api.response.EraningRegisterResponseModel;
import com.dhb.models.api.response.FetchLedgerResponseModel;
import com.dhb.models.data.DepositRegisterModel;
import com.dhb.models.data.EarningRegisterModel;
import com.dhb.models.data.LedgerDetailsModeler;
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

public class LedgerDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEDGER_DISPLAY_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtFromDate, txtToDate, seven, outstanding, norecordsearnings, norecordsdeposit, noledger, balance;
    private Button btnFilter,depositbuttn;
    private TableLayout tlCR, t1ER, t1DR;
    private FetchLedgerResponseModel fetchLedgerResponseModel;
    private ArrayList<EarningRegisterModel> earningRegisterModels;
    private ArrayList<DepositRegisterModel> depositRegisterModels;
    private int mYear, mMonth, mDay;
    private String fromdate = "", todate = "";


    public LedgerDisplayFragment() {
        // Required empty public constructor
    }

    public static LedgerDisplayFragment newInstance() {
        LedgerDisplayFragment fragment = new LedgerDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ledger_display, container, false);
        Logger.error(TAG_FRAGMENT + "onCreateView: ");
        activity = (HomeScreenActivity) getActivity();


        //today date
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        todate = (today).toString();


        //previous 7  days
        fromdate = getCalculatedDate("yyyy-MM-dd", -7);
        initUI();
        fetchLedgerDetails();
        setListners();
        return rootView;
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        String previous_date = s.format(new Date(cal.getTimeInMillis()));
        return previous_date;
    }

    private void setListners() {
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtToDate) {

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

                                    txtToDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    todate = txtToDate.getText().toString();
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            }
        });


        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == txtFromDate) {

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

                                    txtFromDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                    fromdate = txtFromDate.getText().toString();
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             tlCR.removeAllViews();
                                             t1ER.removeAllViews();
                                             t1DR.removeAllViews();
                                             fetchLedgerDetails();
                                             seven.setVisibility(View.GONE);


                                         }
                                     }
        );
    }


    private void fetchLedgerDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLedgerDetailApiAsyncTask = asyncTaskForRequest.getFetchLedgerDetailsRequestAsyncTask(fromdate, todate);
        fetchLedgerDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchLedgerDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchLedgerDetailApiAsyncTask.execute(fetchLedgerDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchEarningRegister() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchEarningDetailApiAsyncTask = asyncTaskForRequest.getFetchEarningDetailsRequestAsyncTask(fromdate, todate);
        fetchEarningDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchEarningDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchEarningDetailApiAsyncTask.execute(fetchEarningDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDepositLedger() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchDepositDetailApiAsyncTask = asyncTaskForRequest.getFetchDepositDetailsRequestAsyncTask(fromdate, todate);
        fetchDepositDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchDepositDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchDepositDetailApiAsyncTask.execute(fetchDepositDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchLedgerDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                FetchLedgerResponseModel fetchLedgerDetailsResponseModel = new FetchLedgerResponseModel();

                fetchLedgerDetailsResponseModel = responseParser.getFetchledgerDetailsResponseModel(json, statusCode);
                fetchLedgerResponseModel = fetchLedgerDetailsResponseModel;

                fetchEarningRegister();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private class FetchEarningDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                earningRegisterModels = new ArrayList<>();

                earningRegisterModels = responseParser.getEarningRegisterResponseModel(json, statusCode);
                if (earningRegisterModels != null && earningRegisterModels.size() > 0) {
                    Toast.makeText(activity, "earningRegisterResponseModel not null", Toast.LENGTH_SHORT).show();

                }
                fetchDepositLedger();
            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private class FetchDepositDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                depositRegisterModels = new ArrayList<>();

                depositRegisterModels = responseParser.getDepositRegisterResponseModel(json, statusCode);
                if (depositRegisterModels != null && depositRegisterModels.size() > 0) {
                    Toast.makeText(activity, "DepositRegisterResponseModel not null", Toast.LENGTH_SHORT).show();

                }

            }
            initData();
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private void initData() {
        if (fetchLedgerResponseModel != null && fetchLedgerResponseModel.getLedgerDetails().size() > 0) {




                    TableRow trCrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
            tlCR.addView(trCrH);

            for (LedgerDetailsModeler ledgerDetailsModel :
                    fetchLedgerResponseModel.getLedgerDetails()) {
                        /*ledgerDetailsModel.setBTechId(ledgerDetailsModel.getBTechId());
                        ledgerDetailsModel.setBTechName(ledgerDetailsModel.getBTechName());*/
                TableRow trCr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_titlesub_ledger_cash_register, null);
                TextView txtDate = (TextView) trCr.findViewById(R.id.txt_date);
                TextView txtopeningbal = (TextView) trCr.findViewById(R.id.txt_openingbalance);
                TextView txtCredit = (TextView) trCr.findViewById(R.id.txt_credit);
                TextView txtDebit = (TextView) trCr.findViewById(R.id.txt_debit);
                TextView txtclosingbal = (TextView) trCr.findViewById(R.id.txt_closingBalance);

                txtDate.setText(ledgerDetailsModel.getDate() + "");
                txtopeningbal.setText(ledgerDetailsModel.getOpeningBal() + "");
                txtCredit.setText(ledgerDetailsModel.getCredit() + "");
                txtDebit.setText(ledgerDetailsModel.getDebit() + "");
                txtclosingbal.setText(ledgerDetailsModel.getClosingBal() + "");
                tlCR.addView(trCr);
                outstanding.setVisibility(View.VISIBLE);
                noledger.setVisibility(View.GONE);
                depositbuttn.setVisibility(View.VISIBLE);
            }
        } else {
            depositbuttn.setVisibility(View.GONE);
            noledger.setVisibility(View.VISIBLE);
            outstanding.setVisibility(View.GONE);

        }
        if (earningRegisterModels != null && earningRegisterModels.size() > 0) {
            TableRow trErH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
            t1ER.addView(trErH);
            for (EarningRegisterModel earningRegisterModel :
                    earningRegisterModels) {

                Toast.makeText(getActivity(), "InSide Initview", Toast.LENGTH_SHORT).show();
                TableRow trEr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledgersub_earning, null);
                TextView txtDate = (TextView) trEr.findViewById(R.id.txt_Registerdate);
                TextView txtAmount = (TextView) trEr.findViewById(R.id.txt_Amount);
                TextView txtRemarks = (TextView) trEr.findViewById(R.id.txt_Remarks);

                txtDate.setText(earningRegisterModel.getDate() + "");
                txtAmount.setText(earningRegisterModel.getAmount() + "");
                txtRemarks.setText(earningRegisterModel.getRemarks() + "");
                t1ER.addView(trEr);
                norecordsdeposit.setVisibility(View.GONE);
            }

        } else {
            norecordsdeposit.setVisibility(View.VISIBLE);

        }


        if (depositRegisterModels != null && depositRegisterModels.size() > 0) {
            TableRow trDrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
            t1DR.addView(trDrH);
            for (DepositRegisterModel depositRegisterModel :
                    depositRegisterModels) {

                Toast.makeText(getActivity(), "InSide Initview", Toast.LENGTH_SHORT).show();
                TableRow trDr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledgersub_earning, null);
                TextView txtDate = (TextView) trDr.findViewById(R.id.txt_Registerdate);
                TextView txtAmount = (TextView) trDr.findViewById(R.id.txt_Amount);
                TextView txtRemarks = (TextView) trDr.findViewById(R.id.txt_Remarks);

                txtDate.setText(depositRegisterModel.getDate() + "");
                txtAmount.setText(depositRegisterModel.getAmount() + "");
                txtRemarks.setText(depositRegisterModel.getRemarks() + "");

                t1DR.addView(trDr);
                norecordsearnings.setVisibility(View.GONE);
            }

        } else {

            norecordsearnings.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void initUI() {
        super.initUI();
        txtFromDate = (TextView) rootView.findViewById(R.id.txt_from_date);
        txtToDate = (TextView) rootView.findViewById(R.id.txt_to_date);
        btnFilter = (Button) rootView.findViewById(R.id.btn_filter);
        tlCR = (TableLayout) rootView.findViewById(R.id.cashRegistertable);
        t1ER = (TableLayout) rootView.findViewById(R.id.earningsRegisterTable);
        t1DR = (TableLayout) rootView.findViewById(R.id.depositsRegisterTable);
        seven = (TextView) rootView.findViewById((R.id.seven));
        outstanding = (TextView) rootView.findViewById(R.id.outstandig);
        norecordsearnings = (TextView) rootView.findViewById(R.id.norecordsearning);
        norecordsdeposit = (TextView) rootView.findViewById(R.id.norecordsdeposit);
        noledger = (TextView) rootView.findViewById(R.id.noledger);
        depositbuttn =(Button) rootView.findViewById(R.id.deposit_button);
         balance =(TextView) rootView.findViewById(R.id.balance);

    }
}
