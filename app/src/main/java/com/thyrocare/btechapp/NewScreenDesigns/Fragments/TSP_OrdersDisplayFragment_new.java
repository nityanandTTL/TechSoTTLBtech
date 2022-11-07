package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.TSP_OrderDisplayAdapterNew;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.OrderStatusChangeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.SetDispositionDataModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.DispositionDataModel;
import com.thyrocare.btechapp.models.data.DispositionDetailsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;


/**
 * A simple {@link Fragment} subclass.
 */
public class TSP_OrdersDisplayFragment_new extends AppCompatActivity {
    public static final String TAG_FRAGMENT = TSP_OrdersDisplayFragment_new.class.getSimpleName();
    private static SwipeRefreshLayout swipeRefreshLayout;
    Activity activity;
    Global global;
    ConnectionDetector connectionDetector;
    LinearLayoutManager linearLayoutManager;
    OrderDetailsModel orderDetailsModel;
    AppPreferenceManager appPreferenceManager;
    Dialog dialog_ready;
    TextView tv_toolbar;
    ImageView iv_home, iv_back;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels;
    private RecyclerView recyOrderList;
    private TextView txtNoRecord, txtEstDistance, txtEstEarnings, txtEstKits;
    private TSP_OrderDisplayAdapterNew tsp_orderDisplayAdapter_new;
    private ConfirmRequestReleaseDialog crr;
    private ArrayList<DispositionDetailsModel> remarksDataModelsarr;
    private ArrayList<String> remarksarr;
    private ArrayList<String> remarks_notc_arr;
    private DispositionDetailsModel remarksDataModel;
    private String remarks_notc_str = "";
    private int DispositionStatusCode;
    private ProgressDialog progressDialog;

    /*public TSP_OrdersDisplayFragment_new() {
        // Required empty public constructor
    }

    public static TSP_OrdersDisplayFragment_new newInstance() {
        TSP_OrdersDisplayFragment_new fragment = new TSP_OrdersDisplayFragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tsp__orders_display_new);
        activity = this;
        global = new Global(activity);
        connectionDetector = new ConnectionDetector(activity);
        orderDetailsResponseModels = new ArrayList<>();
        orderDetailsModel = new OrderDetailsModel();
        appPreferenceManager = new AppPreferenceManager(activity);
        initUI();
        initListeners();
    }

    public void initUI() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_visit_orders_display);
        recyOrderList = (RecyclerView) findViewById(R.id.recyOrderList);
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyOrderList.setLayoutManager(linearLayoutManager);
        txtNoRecord = (TextView) findViewById(R.id.txt_no_orders);
//        txtEstDistance = (TextView) findViewById(R.id.txtEstDistance);
        txtEstEarnings = (TextView) findViewById(R.id.txtEstEarnings);
        txtEstKits = (TextView) findViewById(R.id.txtEstKits);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("Visit Orders");
        txtEstKits.setSelected(true);
    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchData() {
        try {
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getOrderDetail(BundleConstants.setTspSelectedOrder);
            global.showProgressDialog(activity, activity.getResources().getString(R.string.fetchingOrders), false);
            fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
                @Override
                public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                    global.hideProgressDialog(activity);
                    orderDetailsResponseModels = null;
                    orderDetailsResponseModels = new ArrayList<>();
                    FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = response.body();
                    if (fetchOrderDetailsResponseModel != null && fetchOrderDetailsResponseModel.getOrderVisitDetails().size() > 0) {
                        for (OrderVisitDetailsModel orderVisitDetailsModel :
                                fetchOrderDetailsResponseModel.getOrderVisitDetails()) {
                            if (orderVisitDetailsModel.getAllOrderdetails() != null && orderVisitDetailsModel.getAllOrderdetails().size() > 0) {
                                for (OrderDetailsModel orderDetailsModel :
                                        orderVisitDetailsModel.getAllOrderdetails()) {
                                    orderDetailsModel.setVisitId(orderVisitDetailsModel.getVisitId());
                                    orderDetailsModel.setResponse(orderVisitDetailsModel.getResponse());
                                    orderDetailsModel.setSlot(orderVisitDetailsModel.getSlot());
                                    orderDetailsModel.setSlotId(orderVisitDetailsModel.getSlotId());
                                    orderDetailsModel.setAmountPayable(orderDetailsModel.getAmountDue());
                                    orderDetailsModel.setEstIncome(orderVisitDetailsModel.getEstIncome());
                                    orderDetailsModel.setAppointmentDate(orderVisitDetailsModel.getAppointmentDate());
                                    orderDetailsModel.setBtechName(orderVisitDetailsModel.getBtechName());
                                    if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                        for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                                orderDetailsModel.getBenMaster()) {
                                            beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                            beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                            for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                                beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                            }
                                        }
                                    }
                                }
                                orderDetailsResponseModels.add(orderVisitDetailsModel);
                                MessageLogger.LogError(TAG_FRAGMENT, "onResponse: " + orderDetailsResponseModels.size());
                            }
                        }
                    }
                    initData();
                }

                @Override
                public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            global.hideProgressDialog(activity);
            e.printStackTrace();
            global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
        }
    }

    private void initData() {
        float totalDistance = 0;
        float estIncome = 0;
        HashMap<String, Integer> kitsCount = new HashMap<>();
        String kitsReq = "";
        for (OrderVisitDetailsModel orderVisitDetailsModel :
                orderDetailsResponseModels) {
            totalDistance = totalDistance + orderVisitDetailsModel.getDistance();
            estIncome = estIncome + orderVisitDetailsModel.getEstIncome();

            for (OrderDetailsModel orderDetailsModel :
                    orderVisitDetailsModel.getAllOrderdetails()) {

                for (KitsCountModel kt :
                        orderDetailsModel.getKits()) {
                    if (kitsCount.containsKey(kt.getKit())) {
                        kitsCount.put(kt.getKit(), kitsCount.get(kt.getKit()) + kt.getValue());
                    } else {
                        kitsCount.put(kt.getKit(), kt.getValue());
                    }
                }
            }
        }
//        txtEstDistance.setText(totalDistance + "");
        int amount_estIncome = Math.round(estIncome);
        txtEstEarnings.setText(amount_estIncome + "");
        Iterator it = kitsCount.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            if (StringUtils.isNull(kitsReq)) {
                kitsReq = pair.getValue() + " " + pair.getKey();
            } else {
                kitsReq = kitsReq + " | " + pair.getValue() + " " + pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        txtEstKits.setText(kitsReq);
        prepareRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void prepareRecyclerView() {
        if (orderDetailsResponseModels.size() > 0) {
            recyOrderList.setVisibility(View.VISIBLE);
            txtNoRecord.setVisibility(View.GONE);

            if (tsp_orderDisplayAdapter_new != null) {
                tsp_orderDisplayAdapter_new.UpdateList(orderDetailsResponseModels);
                tsp_orderDisplayAdapter_new.notifyDataSetChanged();
            } else {
                tsp_orderDisplayAdapter_new = new TSP_OrderDisplayAdapterNew(this, activity, orderDetailsResponseModels);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                recyOrderList.setLayoutManager(mLayoutManager);
                recyOrderList.setAdapter(tsp_orderDisplayAdapter_new);
            }

            tsp_orderDisplayAdapter_new.setOnItemClickListener(new TSP_OrderDisplayAdapterNew.OnClickListeners() {
                @Override
                public void onAcceptClicked(OrderVisitDetailsModel orderVisitDetailsModel) {
                    onOrderAccepted(orderVisitDetailsModel);
                }

                @Override
                public void onCallCustomer(final OrderVisitDetailsModel orderVisitDetailsModels) {

                    try {
                        callgetDispositionData(orderVisitDetailsModels);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*if (orderVisitDetailsModels != null && orderVisitDetailsModels.getAllOrderdetails() != null && orderVisitDetailsModels.getAllOrderdetails().size() > 0) {
                        String strnumber = !InputUtils.isNull(orderVisitDetailsModels.getAllOrderdetails().get(0).getMobile()) ? orderVisitDetailsModels.getAllOrderdetails().get(0).getMobile() : "";
                        CallCustomerDirectly(strnumber);
                    } else {
                        global.showCustomToast(activity, "Mobile number no found");
                    }*/


                    if (connectionDetector.isConnectingToInternet()) {
                        CallPatchRequestAPI(orderVisitDetailsModels);
                    } else {
                        Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onRefreshAdapter() {
                    fetchData();
                }

                @Override
                public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
                    crr = new ConfirmRequestReleaseDialog(activity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
                    crr.show();
                }
            });
        } else {
            recyOrderList.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private void CallCustomerDirectly(final String strnumber) {
        TedPermission.with(activity)
                .setPermissions(Manifest.permission.CALL_PHONE)
                .setRationaleMessage("We need permission to make call from your device.")
                .setRationaleConfirmText("OK")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Telephone")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (!InputUtils.isNull(strnumber)) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + strnumber));
                            activity.startActivity(intent);
                        } else {
                            global.showCustomToast(activity, "Invalid mobile number");
                        }

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(activity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void onOrderAccepted(OrderVisitDetailsModel orderVisitDetailsModel) {
        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
        orderStatusChangeRequestModel.setRemarks("");
        orderStatusChangeRequestModel.setStatus(8);
        if (connectionDetector.isConnectingToInternet()) {
            CallOrderStatusChangeAPIAfterAcceptButtonClicked(orderStatusChangeRequestModel);
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void CallOrderStatusChangeAPIAfterAcceptButtonClicked(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<OrderStatusChangeResponseModel> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<OrderStatusChangeResponseModel>() {
            @Override
            public void onResponse(Call<OrderStatusChangeResponseModel> call, Response<OrderStatusChangeResponseModel> response) {
                global.hideProgressDialog(activity);
                int statusCode = response.code();
                if (statusCode == 204 || statusCode == 200) {
                    TastyToast.makeText(activity, "Order Accepted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    fetchData();
                } else {
                    try {
                        Toast.makeText(activity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderStatusChangeResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CallPatchRequestAPI(OrderVisitDetailsModel orderVisitDetailsModels) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        LoginResponseModel model = appPreferenceManager.getLoginResponseModel();
        Call<String> responseCall = apiInterface.CallpatchRequestAPI(model.getUserID(), orderVisitDetailsModels.getAllOrderdetails().get(0).getMobile(), orderVisitDetailsModels.getVisitId());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        final String MaskedPhoneNumber = response.body();
                        TedPermission.with(activity)
                                .setPermissions(Manifest.permission.CALL_PHONE)
                                .setRationaleMessage("We need permission to make call from your device.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Telephone")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
                                        activity.startActivity(intent);
                                    }

                                    @Override
                                    public void onPermissionDenied(List<String> deniedPermissions) {
                                        Toast.makeText(activity, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }).check();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);

            }
        });
    }


    public void callgetDispositionData(OrderVisitDetailsModel orderVisitDetailsModel) {
        if (connectionDetector.isConnectingToInternet()) {
            CallgetDispositionApi(orderVisitDetailsModel);
        } else {
            TastyToast.makeText(activity, "Check Internet Connection..", TastyToast.LENGTH_LONG, TastyToast.INFO);
        }
    }

    private void CallgetDispositionApi(final OrderVisitDetailsModel orderDet) {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<DispositionDataModel> responseCall = apiInterface.CallgetDispositionApi();
        responseCall.enqueue(new Callback<DispositionDataModel>() {
            @Override
            public void onResponse(Call<DispositionDataModel> call, retrofit2.Response<DispositionDataModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseParser responseParser = new ResponseParser(activity);
                    DispositionDataModel dispositionDataModel = response.body();

                    if (dispositionDataModel != null) {
                        MessageLogger.PrintMsg("");
                        if (dispositionDataModel.getAllDisp() != null) {
                            if (dispositionDataModel.getAllDisp().size() != 0) {
                                CallDespositionDialog(orderDet, dispositionDataModel.getAllDisp());
                            }
                        }
                    }
                } else {
                    global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<DispositionDataModel> call, Throwable t) {
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallDespositionDialog(final OrderVisitDetailsModel orderVisitDetailsModel, ArrayList<DispositionDetailsModel> allDisp) {
        dialog_ready = new Dialog(activity);
        dialog_ready.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_ready.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_ready.setContentView(R.layout.dialog_desposition);
//        dialog_ready.setCanceledOnTouchOutside(false);
        dialog_ready.setCancelable(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);
        dialog_ready.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog_ready.show();

        ImageView img_cnc = (ImageView) dialog_ready.findViewById(R.id.img_cnc);
        img_cnc.setVisibility(View.GONE);
        img_cnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_ready.dismiss();
            }
        });

        TextView txt_odn = (TextView) dialog_ready.findViewById(R.id.txt_odn);
        txt_odn.setText("" + orderVisitDetailsModel.getVisitId());

        final EditText edt_desprem = (EditText) dialog_ready.findViewById(R.id.edt_desprem);
        final LinearLayout ll_rem = (LinearLayout) dialog_ready.findViewById(R.id.ll_rem);
        final LinearLayout ll_spnrem = (LinearLayout) dialog_ready.findViewById(R.id.ll_spnrem);
        final Spinner spn_rem = (Spinner) dialog_ready.findViewById(R.id.spn_rem);

        remarks_notc_arr = new ArrayList<>();
        remarks_notc_arr.add("Select");
        remarks_notc_arr.add("Ringing / No Response");
        remarks_notc_arr.add("Busy");
        remarks_notc_arr.add("Invalid Number");
        remarks_notc_arr.add("Number Does Not Exist");
        remarks_notc_arr.add("Switch off / Not reachable");

        ArrayAdapter<String> spnrnotconnectedremarks = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, remarks_notc_arr);
        spnrnotconnectedremarks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_rem.setAdapter(spnrnotconnectedremarks);
        spn_rem.setSelection(0);
        spn_rem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    remarks_notc_str = "";
                } else {
                    remarks_notc_str = remarks_notc_arr.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spn_desp = (Spinner) dialog_ready.findViewById(R.id.spn_desp);
        remarksDataModelsarr = allDisp;
        remarksDataModel = new DispositionDetailsModel();
        if (remarksDataModelsarr != null && remarksDataModelsarr.size() > 0) {

            remarksarr = new ArrayList<>();
            remarksarr.add("Select");
            if (remarksDataModelsarr != null && remarksDataModelsarr.size() > 0) {
                for (DispositionDetailsModel remarksDataModels :
                        remarksDataModelsarr) {
                    remarksarr.add(remarksDataModels.getDisposition().toUpperCase());
                }
            }

            ArrayAdapter<String> spinneradapterremarks = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, remarksarr);
            spinneradapterremarks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_desp.setAdapter(spinneradapterremarks);
            spn_desp.setSelection(0);
            spn_desp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        remarksDataModel = null;
                        ll_rem.setVisibility(View.GONE);
                    } else {
                        if (position == 1) {
                            ll_rem.setVisibility(View.VISIBLE);
                            ll_spnrem.setVisibility(View.GONE);
                            edt_desprem.setVisibility(View.VISIBLE);
                        } else if (position == 2) {
                            ll_rem.setVisibility(View.VISIBLE);
                            ll_spnrem.setVisibility(View.VISIBLE);
                            edt_desprem.setVisibility(View.GONE);
                        }
                        remarksDataModel = remarksDataModelsarr.get(position - 1);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        edt_desprem.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        edt_desprem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().startsWith(".") || s.toString().trim().startsWith(",")) {
                    // CommonUtils.toastytastyError(activity, "Enter valid Mobile Number ", false);

                    final android.app.AlertDialog.Builder builder;
                    builder = new android.app.AlertDialog.Builder(activity);
                    builder.setCancelable(false);
                    builder.setTitle("")
                            .setMessage("Invalid text")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    edt_desprem.setText("");
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    edt_desprem.setFocusable(true);
                }
            }
        });

        Button btn_proceed = (Button) dialog_ready.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarksDataModel != null) {
                    if (remarksDataModel.getDispId() == 0) {
                        TastyToast.makeText(activity, "Select desposition", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (edt_desprem.getText().toString().trim().equals("") && edt_desprem.getVisibility() == View.VISIBLE) {
                        TastyToast.makeText(activity, "Enter Remarks", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remarks_notc_str.equals("") && ll_spnrem.getVisibility() == View.VISIBLE) {
                        TastyToast.makeText(activity, "Select Remarks", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        String st = "";
                        if (remarksDataModel.getDispId() == 1) {
                            st = edt_desprem.getText().toString().trim();
                        } else if (remarksDataModel.getDispId() == 2) {
                            st = remarks_notc_str;
                        }

                        SetDispositionDataModel nm = new SetDispositionDataModel();
                        nm.setAppId(1);
                        nm.setDispId(remarksDataModel.getDispId());
                        nm.setOrderNo("" + orderVisitDetailsModel.getVisitId());
                        nm.setUserId("" + appPreferenceManager.getLoginResponseModel().getUserID());
                        String s = "" + appPreferenceManager.getLoginResponseModel().getUserName();
                        nm.setFrmNo("" + s.substring(0, Math.min(s.length(), 18)));
                        MessageLogger.LogError(TAG_FRAGMENT, "onClick: " + s.substring(0, Math.min(s.length(), 18)));
                        nm.setToNo("" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
                        nm.setRemarks("" + st);

                        if (connectionDetector.isConnectingToInternet()) {
                            new Btech_AsyncLoadBookingFreqApi(nm).execute();
                        } else {
                            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                } else {
                    TastyToast.makeText(activity, "Select desposition", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    private void CallInitialiseProgressDialog() {
        if (activity != null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("");
            progressDialog.setMessage("Please wait...");
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            try {
                progressDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void CallCloseProgressDialog() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callOrderStatusChangeApi(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<OrderStatusChangeResponseModel> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(activity, getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<OrderStatusChangeResponseModel>() {
            @Override
            public void onResponse(Call<OrderStatusChangeResponseModel> call, Response<OrderStatusChangeResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.code() == 200 || response.code() == 204) {
                    TastyToast.makeText(activity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    fetchData();
                } else {
                    try {
                        Toast.makeText(activity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderStatusChangeResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (connectionDetector.isConnectingToInternet()) {
            fetchData();
        } else {
            global.showCustomToast(activity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }
    }

    public class Btech_AsyncLoadBookingFreqApi extends AsyncTask<Void, Void, String> {

        SetDispositionDataModel setDispositionDataModel;

        public Btech_AsyncLoadBookingFreqApi(SetDispositionDataModel nm) {
            this.setDispositionDataModel = nm;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CallInitialiseProgressDialog();
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpClient httpClient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            String json = "";
            try {
                HttpPost request = new HttpPost(EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD)) + "/api/OrderAllocation/MediaUpload");

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("AppId", new StringBody("" + setDispositionDataModel.getAppId()));
                entity.addPart("DispId", new StringBody("" + setDispositionDataModel.getDispId()));
                entity.addPart("FrmNo", new StringBody("" + setDispositionDataModel.getFrmNo()));
                entity.addPart("OrderNo", new StringBody("" + setDispositionDataModel.getOrderNo()));
                entity.addPart("Remarks", new StringBody("" + setDispositionDataModel.getRemarks()));
                entity.addPart("ToNo", new StringBody("" + setDispositionDataModel.getToNo()));
                entity.addPart("UserId", new StringBody("" + setDispositionDataModel.getUserId()));

                request.setEntity(entity);
                HttpResponse response = httpClient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                DispositionStatusCode = statusLine.getStatusCode();
                if (DispositionStatusCode == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    InputStream content = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();
                    MessageLogger.PrintMsg("Nitya >> " + json);

                } else {
                    HttpEntity responseEntity = response.getEntity();
                    InputStream content = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();
                    MessageLogger.PrintMsg("Nitya >> " + json);
                }

            } catch (IllegalStateException e) {
                MessageLogger.LogError("FileUpload Illegal", e.getMessage());
            } catch (IOException e) {
                MessageLogger.LogError("FileUpload IOException ", e.getMessage());
            } catch (Exception e) {
                MessageLogger.LogError("Exception ", e.getMessage());
            } finally {
                // close connections
                httpClient.getConnectionManager().shutdown();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            CallCloseProgressDialog();
            if (DispositionStatusCode == 200) {
                if (dialog_ready != null) {
                    if (dialog_ready.isShowing()) {
                        dialog_ready.dismiss();
                    }
                }
                TastyToast.makeText(activity, "" + res, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            } else {
                TastyToast.makeText(activity, "" + res, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }
    }

    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {

            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            if (connectionDetector.isConnectingToInternet()) {
                callOrderStatusChangeApi(orderStatusChangeRequestModel);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }
}
