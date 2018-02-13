package com.thyrocare.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.PaymentsActivity;
import com.thyrocare.models.api.response.FetchLedgerResponseModel;
import com.thyrocare.models.data.DepositRegisterModel;
import com.thyrocare.models.data.EarningRegisterModel;
import com.thyrocare.models.data.Earning_NewRegisterModel;
import com.thyrocare.models.data.LedgerDetailsModeler;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * API used<br/>
 * /Ledger/CashRegister<br/>
 *  http://bts.dxscloud.com/btsapi/api/OrdersCredit/BtechEarnings/884543107<br/>
 *  http://bts.dxscloud.com/btsapi/api/PayThyrocare/SelectMode/3<br/>
 */
public class LedgerDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEDGER_DISPLAY_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private LinearLayout sevenlay, noledgerlay, nodepositlay, noearninglay;
    private TextView txtFromDate, txtToDate, seven, outstanding, norecordsearnings, norecordsdeposit, noledger, balance;
    private Button btnFilter, depositbuttn;
    private TableLayout tlCR, t1ER; //t1DR;//earning
    private FetchLedgerResponseModel fetchLedgerResponseModel;
    private ArrayList<EarningRegisterModel> earningRegisterModels;
    private ArrayList<DepositRegisterModel> depositRegisterModels;

    //earning
    private ArrayList<Earning_NewRegisterModel> earning_newRegisterModelsArr;
    Earning_NewRegisterModel earning_newRegisterModel;

    TextView tv_cr_date, tv_cr_amount, tv_fasting_ord, tv_nonfast_ord, tv_estm_earning;

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
        activity.toolbarHome.setTitle("Ledger");
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ledger_display, container, false);
        //today date
        todate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        //previous 7  days
        fromdate = getCalculatedDate("yyyy-MM-dd", -7);

        initUI();
        txtToDate.setText(todate);
        txtFromDate.setText(fromdate);

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


        depositbuttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentPaymentsActivity = new Intent(activity, PaymentsActivity.class);
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_ORDER_NO, "VST090543");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_AMOUNT, "1");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 3);
                startActivity(intentPaymentsActivity);


            }
        });


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
                                             //t1DR.removeAllViews();
                                             fetchLedgerDetails();
                                             seven.setVisibility(View.GONE);
                                             sevenlay.setVisibility(View.GONE);


                                         }
                                     }
        );
    }


    private void fetchLedgerDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchLedgerDetailApiAsyncTask = asyncTaskForRequest.getFetchLedgerDetailsRequestAsyncTask(fromdate, todate);
        fetchLedgerDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new LedgerDisplayFragment.FetchLedgerDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchLedgerDetailApiAsyncTask.execute(fetchLedgerDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchEarningRegister() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchEarningDetailApiAsyncTask = asyncTaskForRequest.getFetchEarningDetailsRequestAsyncTask_new(fromdate, todate);
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
        fetchDepositDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new LedgerDisplayFragment.FetchDepositDetailsApiAsyncTaskDelegateResult());
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
              /*  ResponseParser responseParser = new ResponseParser(activity);
                earningRegisterModels = new ArrayList<>();

                earningRegisterModels = responseParser.getEarningRegisterResponseModel(json, statusCode);
                if (earningRegisterModels != null && earningRegisterModels.size() > 0) {

                }*/

                //earning
                ResponseParser responseParser = new ResponseParser(activity);
                earning_newRegisterModelsArr = new ArrayList<>();

                //earning_newRegisterModelsArr = responseParser.getEarningRegisterResponseModel(json, statusCode);
                earning_newRegisterModel = responseParser.getEarningRegisterResponseModel(json, statusCode);

                if (earning_newRegisterModelsArr != null && earning_newRegisterModelsArr.size() > 0) {

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
                noledgerlay.setVisibility(View.GONE);
                depositbuttn.setVisibility(View.VISIBLE);

            }
            balance.setText(fetchLedgerResponseModel.getOutstandingBalance() + "");
        } else {
            depositbuttn.setVisibility(View.GONE);
            noledger.setVisibility(View.VISIBLE);
            noledgerlay.setVisibility(View.VISIBLE);
            outstanding.setVisibility(View.GONE);
            balance.setVisibility(View.GONE);

        }


        //deposit
        if (depositRegisterModels != null && depositRegisterModels.size() > 0) {
            Logger.debug("result***" + "1");
            TableRow trDrH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledger_earnings, null);
            t1ER.addView(trDrH);
            for (DepositRegisterModel depositRegisterModel :
                    depositRegisterModels) {
                Logger.debug("result***" + "2");
                TableRow trDr = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_ledgersub_earning, null);
                TextView txtDate = (TextView) trDr.findViewById(R.id.txt_Registerdate);
                TextView txtAmount = (TextView) trDr.findViewById(R.id.txt_Amount);
                TextView txtRemarks = (TextView) trDr.findViewById(R.id.txt_Remarks);

                Logger.debug("result***" + depositRegisterModel.getRemarks());
                txtDate.setText(depositRegisterModel.getDate() + "");
                txtAmount.setText(depositRegisterModel.getAmount() + "");
                txtRemarks.setText(depositRegisterModel.getRemarks() + "");

                t1ER.addView(trDr);
                norecordsearnings.setVisibility(View.GONE);
                noearninglay.setVisibility(View.GONE);
            }

        } else {
            Logger.debug("result***" + "else");
            norecordsearnings.setVisibility(View.VISIBLE);
            noearninglay.setVisibility(View.VISIBLE);
        }
        //earning
        if (earning_newRegisterModel != null /*earning_newRegisterModelsArr != null && earning_newRegisterModelsArr.size() > 0*/) {

            if (null == earning_newRegisterModel.getCreditDate()) {
                tv_cr_date.setText("-");
            } else {
                tv_cr_date.setText(earning_newRegisterModel.getCreditDate());
            }

            tv_cr_amount.setText(earning_newRegisterModel.getCreditedAmount() + "");
            tv_fasting_ord.setText(earning_newRegisterModel.getFastingOrders() + "");
            tv_nonfast_ord.setText(earning_newRegisterModel.getNonFastingOrders() + "");
            tv_estm_earning.setText(earning_newRegisterModel.getTotalEarning() + "");

            //t1DR.addView(trDr);
            norecordsdeposit.setVisibility(View.GONE);
            nodepositlay.setVisibility(View.GONE);

        } else {
            norecordsdeposit.setVisibility(View.VISIBLE);
            nodepositlay.setVisibility(View.VISIBLE);

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
        //t1DR = (TableLayout) rootView.findViewById(R.id.depositsRegisterTable);//earning
        seven = (TextView) rootView.findViewById((R.id.seven));
        sevenlay = (LinearLayout) rootView.findViewById((R.id.sevendays_layout));
        outstanding = (TextView) rootView.findViewById(R.id.outstandig);
        norecordsearnings = (TextView) rootView.findViewById(R.id.norecordearning);
        norecordsdeposit = (TextView) rootView.findViewById(R.id.nodeposit);
        noledger = (TextView) rootView.findViewById(R.id.noledger);
        noledgerlay = (LinearLayout) rootView.findViewById((R.id.noleaderlay));
        nodepositlay = (LinearLayout) rootView.findViewById((R.id.nodepositlay));
        noearninglay = (LinearLayout) rootView.findViewById((R.id.recordearninglay));
        depositbuttn = (Button) rootView.findViewById(R.id.deposit_button);
        balance = (TextView) rootView.findViewById(R.id.balance);

        //earning
        tv_cr_date = (TextView) rootView.findViewById(R.id.tv_cr_date);
        tv_cr_amount = (TextView) rootView.findViewById(R.id.tv_cr_amount);
        tv_fasting_ord = (TextView) rootView.findViewById(R.id.tv_fasting_ord);
        tv_nonfast_ord = (TextView) rootView.findViewById(R.id.tv_nonfast_ord);
        tv_estm_earning = (TextView) rootView.findViewById(R.id.tv_estm_earning);
    }
}
