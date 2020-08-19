package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Get_Deposite_Adapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.LedgerCash_register_AdapterRV;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_BtechEarning_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_cash_register_details_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_deposite_details_model;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.SelectDatePickerDialogFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;



/**
 * A simple {@link Fragment} subclass.
 */
public class Ledger_module_fragment_new extends Fragment {

    public static final String TAG_FRAGMENT = "LEDGER_DISPLAY_FRAGMENT";
    HomeScreenActivity homeScreenActivity;
    Button btn_filter, deposit_button;
    TextView txt_to_date, txt_from_date;
    Global global;
    Activity activity;
    Context context;
    RecyclerView rv_cashRegistertable_id, rv_deposite_register;
    String btechid = "";
    LedgerCash_register_AdapterRV ledgerCash_register_adapterRV;
    Get_Deposite_Adapter get_deposite_adapter;
    LinearLayoutManager layoutManager, layoutManager_deposite;
    TextView tv_estm_earning, tv_nonfast_ord, tv_fasting_ord, tv_cr_amount, tv_cr_date, txt_balance;
    LinearLayout ll_norecord_id, ll_norecord_deposite_id, ll_rv_last7update_id, ll_rv_deposite_id;
    ConnectionDetector connectionDetector;
    SelectDatePickerDialogFragment selectDatePickerDialogFragment;
    Date d_from_date, d_to_date;
    private AppPreferenceManager appPreferenceManager;


    public Ledger_module_fragment_new() {
        // Required empty public constructor
    }

    public static Ledger_module_fragment_new newInstance() {
        Ledger_module_fragment_new fragment = new Ledger_module_fragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        appPreferenceManager = new AppPreferenceManager(activity);
        btechid = appPreferenceManager.getLoginResponseModel().getUserID();
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager_deposite = new LinearLayoutManager(getActivity());

        homeScreenActivity = (HomeScreenActivity) getActivity();
        try {
            if (homeScreenActivity != null) {
                if (homeScreenActivity.toolbarHome != null) {
                    homeScreenActivity.toolbarHome.setTitle("Ledger");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        homeScreenActivity.isOnHome = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ledger_module_fragment, container, false);
        init(root);
        initListener();
        initData();

        return root;
    }

    private void init(View root) {
        global = new Global(activity);
        btn_filter = root.findViewById(R.id.btn_filter);
        txt_from_date = root.findViewById(R.id.txt_from_date);
        txt_to_date = root.findViewById(R.id.txt_to_date);
        txt_balance = root.findViewById(R.id.txt_balance);
        rv_cashRegistertable_id = root.findViewById(R.id.rv_cashRegistertable_id);
        tv_cr_amount = root.findViewById(R.id.tv_cr_amount);
        tv_cr_date = root.findViewById(R.id.tv_cr_date);
        tv_estm_earning = root.findViewById(R.id.tv_estm_earning);
        tv_fasting_ord = root.findViewById(R.id.tv_fasting_ord);
        tv_nonfast_ord = root.findViewById(R.id.tv_nonfast_ord);
        deposit_button = root.findViewById(R.id.deposit_button);
        rv_deposite_register = root.findViewById(R.id.rv_deposite_register);
        ll_norecord_id = root.findViewById(R.id.ll_norecord_id);
        ll_norecord_deposite_id = root.findViewById(R.id.ll_norecord_deposite_id);
        ll_rv_deposite_id = root.findViewById(R.id.ll_rv_deposite_id);
        ll_rv_last7update_id = root.findViewById(R.id.ll_rv_last7update_id);
        connectionDetector = new ConnectionDetector(context);
    }

    private void initListener() {
        deposit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentPaymentsActivity = new Intent(activity, PaymentsActivity.class);
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_ORDER_NO, "VST090543");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_AMOUNT, "1");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(btechid));
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 3);
                startActivity(intentPaymentsActivity);

            }
        });
        txt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                long maxdate = c.getTimeInMillis();
                selectDatePickerDialogFragment = new SelectDatePickerDialogFragment(activity, "From date", 0, maxdate, "yyyy-MM-dd");
                selectDatePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String strSelectedDate, Date SelectedDate) {
                        txt_to_date.setText(strSelectedDate);
                        d_to_date = SelectedDate;
                    }
                });
                selectDatePickerDialogFragment.show(getFragmentManager(), "From date");


            }
        });
        txt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                long maxdate = c.getTimeInMillis();
                selectDatePickerDialogFragment = new SelectDatePickerDialogFragment(activity, "From date", 0, maxdate, "yyyy-MM-dd");
                selectDatePickerDialogFragment.setDateSelectedListener(new SelectDatePickerDialogFragment.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String strSelectedDate, Date SelectedDate) {
                        txt_from_date.setText(strSelectedDate);
                        d_from_date = SelectedDate;
                    }
                });
                selectDatePickerDialogFragment.show(getFragmentManager(), "From date");
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d_from_date != null && d_to_date != null) {
                    if (d_from_date.before(d_to_date) || d_from_date.equals(d_to_date)) {
                        callall_api(txt_from_date.getText().toString(), txt_to_date.getText().toString());
                    } else {
                        global.showCustomToast(activity, getString(R.string.validdate), Toast.LENGTH_LONG);
                    }
                }
            }
        });

    }

    private void initData() {
        String currentdate = null, lastweek_date = null;
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            currentdate = df.format(c.getTime());
            d_to_date = c.getTime();
            c.add(Calendar.DATE, -6);
            lastweek_date = df.format(c.getTime());
            d_from_date = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(currentdate)) {
            txt_from_date.setText(lastweek_date);
            txt_to_date.setText(currentdate);
            callall_api(lastweek_date, currentdate);
        } else {
            global.showCustomToast(activity,SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
        }
    }

    private void callall_api(String fromdate, String todate) {
        if (connectionDetector.isConnectingToInternet()) {
            call_cashregisterapi(fromdate, todate);
          /*  call_deposite_register(fromdate, todate);
            call_btechEraningapi(fromdate, todate);*/
        }else{
            global.showCustomToast(activity,CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
        }
    }

    private void call_cashregisterapi(final String fromdate, final String todate) {
        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), EncryptionUtils.DecodeString64(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Get_cash_register_details_Model> cash_register_details_modelCall = getAPIInterface.Get_CashRegister(btechid, fromdate, todate);
        global.showProgressDialog(activity, "Processing request......", false);
        cash_register_details_modelCall.enqueue(new Callback<Get_cash_register_details_Model>() {
            @Override
            public void onResponse(Call<Get_cash_register_details_Model> call, Response<Get_cash_register_details_Model> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Get_cash_register_details_Model get_cash_register_details_model = response.body();
                    if (get_cash_register_details_model.getLedgerDetails() != null && get_cash_register_details_model.getLedgerDetails().size() > 0) {
                        ledgerCash_register_adapterRV = new LedgerCash_register_AdapterRV(activity, context, get_cash_register_details_model);
                        rv_cashRegistertable_id.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_cashRegistertable_id.setAdapter(ledgerCash_register_adapterRV);
                        txt_balance.setText(get_cash_register_details_model.getOutstandingBalance() + "");
                        ll_norecord_id.setVisibility(View.GONE);
                        ll_rv_last7update_id.setVisibility(View.VISIBLE);
                    } else {
                        ll_norecord_id.setVisibility(View.VISIBLE);
                        ll_rv_last7update_id.setVisibility(View.GONE);
                        global.showCustomToast(activity, "No data to show", Toast.LENGTH_LONG);
                    }
                } else {
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG , Toast.LENGTH_LONG);
                }

                if (connectionDetector.isConnectingToInternet()) {
                    call_deposite_register(fromdate, todate);
                }else{
                    global.showCustomToast(activity, CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Get_cash_register_details_Model> call, Throwable t) {
                global.hideProgressDialog();
                if (connectionDetector.isConnectingToInternet()) {
                    call_deposite_register(fromdate, todate);
                }else{
                    global.showCustomToast(activity, CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
                }
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
            }
        });

    }


    private void call_deposite_register(final String fromdate, final String todate) {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<Get_deposite_details_model>> getdeposit_call = getAPIInterface.Get_Deposite(btechid, fromdate, todate);
        global.showProgressDialog(activity,"Processing request......",false);
        getdeposit_call.enqueue(new Callback<ArrayList<Get_deposite_details_model>>() {
            @Override
            public void onResponse(Call<ArrayList<Get_deposite_details_model>> call, Response<ArrayList<Get_deposite_details_model>> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Get_deposite_details_model> get_deposite_details_model = response.body();
                    if ( get_deposite_details_model.size() > 0) {
                        get_deposite_adapter = new Get_Deposite_Adapter(activity, context, get_deposite_details_model);
                        rv_deposite_register.setLayoutManager(layoutManager_deposite);
                        rv_deposite_register.setAdapter(get_deposite_adapter);
                        ll_rv_deposite_id.setVisibility(View.VISIBLE);
                        ll_norecord_deposite_id.setVisibility(View.GONE);
                    } else {
                        ll_rv_deposite_id.setVisibility(View.GONE);
                        ll_norecord_deposite_id.setVisibility(View.VISIBLE);
                    }
                } else {
                    global.showCustomToast(activity,SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
                }

                if (connectionDetector.isConnectingToInternet()) {
                    call_btechEraningapi(fromdate, todate);
                }else{
                    global.showCustomToast(activity, CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Get_deposite_details_model>> call, Throwable t) {
                global.hideProgressDialog();
                if (connectionDetector.isConnectingToInternet()) {
                    call_btechEraningapi(fromdate, todate);
                }else{
                    global.showCustomToast(activity, CHECK_INTERNET_CONN,Toast.LENGTH_LONG);
                }
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);
            }
        });


    }

    private void call_btechEraningapi(String fromdate, String todate) {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(context.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<Get_BtechEarning_Model> get_btechEarning_modelCall = getAPIInterface.get_BtechEarning(btechid, fromdate, todate);
        global.showProgressDialog(activity,"Processing request......",false);
        get_btechEarning_modelCall.enqueue(new Callback<Get_BtechEarning_Model>() {
            @Override
            public void onResponse(Call<Get_BtechEarning_Model> call, Response<Get_BtechEarning_Model> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    Get_BtechEarning_Model get_btechEarning_model = response.body();
                    tv_cr_amount.setText("" + get_btechEarning_model.getCreditedAmount());
                    if (get_btechEarning_model.getCreditDate() != null) {
                        tv_cr_date.setText(!StringUtils.isNull(get_btechEarning_model.getCreditDate()) ? get_btechEarning_model.getCreditDate() : "" );
                    } else {
                        tv_cr_date.setText("" + 0);
                    }

                    tv_estm_earning.setText("" + get_btechEarning_model.getTotalEarning());
                    tv_fasting_ord.setText("" + get_btechEarning_model.getFastingOrders());
                    tv_nonfast_ord.setText("" + get_btechEarning_model.getNonFastingOrders());
                    global.showCustomToast(activity, response.body().getBtechId() + "", Toast.LENGTH_LONG);
                } else {
                    global.showCustomToast(activity, "No data present", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Get_BtechEarning_Model> call, Throwable t) {
                global.hideProgressDialog();
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG);

            }
        });
    }


}

