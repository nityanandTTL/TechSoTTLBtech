package com.dhb.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
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

import java.util.ArrayList;
import java.util.Calendar;

public class LedgerDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEDGER_DISPLAY_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtFromDate, txtToDate;
    private Button btnFilter;
    public Calendar myCalendar;
    private TableLayout tlCR, t1ER,t1DR;
    private FetchLedgerResponseModel fetchLedgerResponseModel;
    private ArrayList<EarningRegisterModel> earningRegisterModels;
    private ArrayList<DepositRegisterModel> depositRegisterModels;

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
        initUI();
        fetchLedgerDetails();
        setListners();
        return rootView;
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

            }
        });

    }

    private void fetchLedgerDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLedgerDetailApiAsyncTask = asyncTaskForRequest.getFetchLedgerDetailsRequestAsyncTask();
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
        ApiCallAsyncTask fetchEarningDetailApiAsyncTask = asyncTaskForRequest.getFetchEarningDetailsRequestAsyncTask();
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
        ApiCallAsyncTask fetchDepositDetailApiAsyncTask = asyncTaskForRequest.getFetchDepositDetailsRequestAsyncTask();
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
                if (earningRegisterModels != null && earningRegisterModels.size()>0) {
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
                if (depositRegisterModels != null && depositRegisterModels.size()>0) {
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
            View vtrcrH = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
            TableRow trCrH = new TableRow(activity);
            trCrH.addView(vtrcrH);
            tlCR.addView(trCrH);

            for (LedgerDetailsModeler ledgerDetailsModel :
                    fetchLedgerResponseModel.getLedgerDetails()) {
                        /*ledgerDetailsModel.setBTechId(ledgerDetailsModel.getBTechId());
                        ledgerDetailsModel.setBTechName(ledgerDetailsModel.getBTechName());*/
                View vtrcr = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_cash_register, null);
                TextView txtDate = (TextView) vtrcr.findViewById(R.id.txt_date);
                TextView txtopeningbal = (TextView) vtrcr.findViewById(R.id.txt_openingbalance);
                TextView txtCredit = (TextView) vtrcr.findViewById(R.id.txt_credit);
                TextView txtDebit = (TextView) vtrcr.findViewById(R.id.txt_debit);
                TextView txtclosingbal = (TextView) vtrcr.findViewById(R.id.txt_closingBalance);
                txtDate.setText(ledgerDetailsModel.getDate() + "");
                txtopeningbal.setText(ledgerDetailsModel.getOpeningBal() + "");
                txtCredit.setText(ledgerDetailsModel.getCredit() + "");
                txtDebit.setText(ledgerDetailsModel.getDebit() + "");
                txtclosingbal.setText(ledgerDetailsModel.getClosingBal() + "");
                TableRow trCr = new TableRow(activity);
                trCr.addView(vtrcr);
                tlCR.addView(trCr);
            }
        }
        if (earningRegisterModels != null && earningRegisterModels.size()>0) {
            View vtrerH = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
            TableRow trErH = new TableRow(activity);
            trErH.addView(vtrerH);
            t1ER.addView(trErH);
            for (EarningRegisterModel earningRegisterModel :
                    earningRegisterModels) {

                Toast.makeText(getActivity(), "InSide Initview", Toast.LENGTH_SHORT).show();
                View vtrer = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
                TextView txtDate = (TextView) vtrer.findViewById(R.id.txt_Registerdate);
                TextView txtAmount = (TextView) vtrer.findViewById(R.id.txt_Amount);
                TextView txtRemarks = (TextView) vtrer.findViewById(R.id.txt_Remarks);

                txtDate.setText(earningRegisterModel.getDate() + "");
                txtAmount.setText(earningRegisterModel.getAmount() + "");
                txtRemarks.setText(earningRegisterModel.getRemarks() + "");
                TableRow trEr = new TableRow(activity);
                trEr.addView(vtrer);
                t1ER.addView(trEr);
            }

        }

        if (depositRegisterModels != null && depositRegisterModels.size()>0) {
            View vtrdrH = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
            TableRow trDrH = new TableRow(activity);
            trDrH.addView(vtrdrH);
            t1DR.addView(trDrH);
            for (DepositRegisterModel depositRegisterModel :
                    depositRegisterModels) {

                Toast.makeText(getActivity(), "InSide Initview", Toast.LENGTH_SHORT).show();
                View vtrer = LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
                TextView txtDate = (TextView) vtrer.findViewById(R.id.txt_Registerdate);
                TextView txtAmount = (TextView) vtrer.findViewById(R.id.txt_Amount);
                TextView txtRemarks = (TextView) vtrer.findViewById(R.id.txt_Remarks);

                txtDate.setText(depositRegisterModel.getDate() + "");
                txtAmount.setText(depositRegisterModel.getAmount() + "");
                txtRemarks.setText(depositRegisterModel.getRemarks() + "");
                TableRow trDr = new TableRow(activity);
                trDr.addView(vtrer);
                t1DR.addView(trDr);
            }

        }
    }


    @Override
    public void initUI() {
        super.initUI();
        txtFromDate = (TextView) rootView.findViewById(R.id.txt_from_date);
        txtToDate = (TextView) rootView.findViewById(R.id.txt_to_date);
        tlCR = (TableLayout) rootView.findViewById(R.id.cashRegistertable);
        t1ER = (TableLayout) rootView.findViewById(R.id.earningsRegisterTable);
        t1DR = (TableLayout) rootView.findViewById(R.id.depositsRegisterTable);


    }
}
