package com.thyrocare.btechapp.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

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

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.models.api.response.FetchLedgerResponseModel;
import com.thyrocare.btechapp.models.data.DepositRegisterModel;
import com.thyrocare.btechapp.models.data.EarningRegisterModel;
import com.thyrocare.btechapp.models.data.Earning_NewRegisterModel;
import com.thyrocare.btechapp.models.data.LedgerDetailsModeler;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * API used<br/>
 * /Ledger/CashRegister<br/>
 * http://bts.dxscloud.com/btsapi/api/OrdersCredit/BtechEarnings/884543107<br/>
 * http://bts.dxscloud.com/btsapi/api/PayThyrocare/SelectMode/3<br/>
 */
public class LedgerDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LEDGER_DISPLAY_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    Earning_NewRegisterModel earning_newRegisterModel;
    TextView tv_cr_date, tv_cr_amount, tv_fasting_ord, tv_nonfast_ord, tv_estm_earning;
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
    private int mYear, mMonth, mDay;
    private String fromdate = "", todate = "";
    private Global global;


    public LedgerDisplayFragment() {
        // Required empty public constructor
    }

    public static LedgerDisplayFragment newInstance() {
        LedgerDisplayFragment fragment = new LedgerDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        String previous_date = s.format(new Date(cal.getTimeInMillis()));
        return previous_date;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Ledger");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        global = new Global(activity);
        try {
            if (activity != null) {
                if (activity.toolbarHome != null) {
                    activity.toolbarHome.setTitle("Ledger");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                                             if (validate_date()) {
                                                 tlCR.removeAllViews();
                                                 t1ER.removeAllViews();
                                                 //t1DR.removeAllViews();
                                                 fetchLedgerDetails();
                                                 seven.setVisibility(View.GONE);
                                                 sevenlay.setVisibility(View.GONE);
                                             } else {
                                                 Toast.makeText(activity, "Invalid date selection", Toast.LENGTH_SHORT).show();
                                             }


                                         }
                                     }
        );
    }

    private boolean validate_date() {

        SimpleDateFormat simpleDateFormat_date = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        Date date_fromtime = null, date_totime = null;
        try {
            date_fromtime = simpleDateFormat_date.parse(txtFromDate.getText().toString());
            date_totime = simpleDateFormat_date.parse(txtToDate.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        MessageLogger.PrintMsg("rohit from date :" + date_fromtime.toString());
        MessageLogger.PrintMsg("rohit to date :" + date_totime.toString());
        try {
            if (date_fromtime.before(date_totime) || date_fromtime.equals(date_totime)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private void fetchLedgerDetails() {
        if (isNetworkAvailable(activity)) {
            CallgetFetchDepositDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchEarningRegister() {
        if (isNetworkAvailable(activity)) {
            CallFetchEarningDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDepositLedger() {
        if (isNetworkAvailable(activity)) {
            CallgetFetchDepositPaymentDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallgetFetchDepositDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<FetchLedgerResponseModel> responseCall = apiInterface.CallgetFetchDepositDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<FetchLedgerResponseModel>() {
            @Override
            public void onResponse(Call<FetchLedgerResponseModel> call, retrofit2.Response<FetchLedgerResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    FetchLedgerResponseModel fetchLedgerDetailsResponseModel = response.body();
                    fetchLedgerResponseModel = fetchLedgerDetailsResponseModel;
                    fetchEarningRegister();
                } else {
                    Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchLedgerResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void CallFetchEarningDetailsApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Earning_NewRegisterModel> responseCall = apiInterface.CallFetchEarningDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<Earning_NewRegisterModel>() {
            @Override
            public void onResponse(Call<Earning_NewRegisterModel> call, retrofit2.Response<Earning_NewRegisterModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    earning_newRegisterModelsArr = new ArrayList<>();
                    earning_newRegisterModel = response.body();
                    if (earning_newRegisterModelsArr != null && earning_newRegisterModelsArr.size() > 0) {

                    }
                    fetchDepositLedger();
                } else {
                    Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Earning_NewRegisterModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallgetFetchDepositPaymentDetailsApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<DepositRegisterModel>> responseCall = apiInterface.CallgetFetchDepositPaymentDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID(), fromdate, todate);
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<ArrayList<DepositRegisterModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DepositRegisterModel>> call, retrofit2.Response<ArrayList<DepositRegisterModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    depositRegisterModels = new ArrayList<>();
                    depositRegisterModels = response.body();
                    if (depositRegisterModels != null && depositRegisterModels.size() > 0) {

                    }

                }
                initData();
            }

            @Override
            public void onFailure(Call<ArrayList<DepositRegisterModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void initData() {
        if (fetchLedgerResponseModel != null && fetchLedgerResponseModel.getLedgerDetails() != null && fetchLedgerResponseModel.getLedgerDetails().size() > 0) {


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
