package com.thyrocare.btechapp.NewScreenDesigns.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Btech_VisitDisplayAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.delegate.refreshDelegate;
import com.thyrocare.btechapp.dialog.ConfirmOrderPassDialog;
import com.thyrocare.btechapp.dialog.ConfirmOrderReleaseDialog;
import com.thyrocare.btechapp.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.SetDispositionDataModel;
import com.thyrocare.btechapp.models.api.response.BtechEstEarningsResponseModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.DispositionDataModel;
import com.thyrocare.btechapp.models.data.DispositionDetailsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;


import com.thyrocare.btechapp.service.TrackerService;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
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

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;


/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class VisitOrdersDisplayFragment_new extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "VISIT_ORDERS_FRAGMENT";

    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private RecyclerView recyOrderList;
    private TextView txtEstDistance;
    private TextView txtEstEarnings;
    private TextView txtEstKits;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_RoutineOrders = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_AayushmanOrders = new ArrayList<>();
    private TextView txtNoRecord;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConfirmOrderReleaseDialog cdd;
    private ConfirmRequestReleaseDialog crr;
    private ConfirmOrderPassDialog Cop;
    private String kits;
    private String[] kits_arr;
    private RescheduleOrderDialog rod;
    private String MaskedPhoneNumber = "";
    private boolean isFetchingOrders = false;
    public static boolean edit;
    private ArrayList<DispositionDetailsModel> remarksDataModelsarr;
    private ArrayList<String> remarksarr;
    private ArrayList<String> remarks_notc_arr;
    private DispositionDetailsModel remarksDataModel;
    private String remarks_notc_str = "";
    Dialog dialog_ready;
    int statusCode;
    private ProgressDialog progressDialog;
    private TextView tv_RoutineOrders, tv_AayushmanOrders;
    private LinearLayout layoutEarnings, lin_categories;
    private boolean isClicledonAayushmanOrders = false;
    private Global global;
    private Btech_VisitDisplayAdapter btech_VisitDisplayAdapter;
    private ConnectionDetector connectionDetector;
    private GPSTracker gpsTracker;
    private Intent FirebaselocationUpdateIntent;
    private boolean isphonecallstarted = false;
    TextView tv_toolbar;
    Activity activity;
    ImageView iv_home, iv_back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_visit_orders_display_new);

        activity = this;

        connectionDetector = new ConnectionDetector(activity);
        global = new Global(activity);
       /* if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Visit orders");
        }*/

//        Global.appBar(getActivity());

        try {
//            activity.toolbar_image.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        activity.isOnHome = false;
        FirebaselocationUpdateIntent = new Intent(VisitOrdersDisplayFragment_new.this, TrackerService.class);
        appPreferenceManager = new AppPreferenceManager(activity);
        BundleConstants.isKIOSKOrder = false;

        initUI();
        setListener();
//        fetchData();
//        getBtechEstEarnings();
        fetchData();
    }

    public void initUI() {

        tv_RoutineOrders = (TextView) findViewById(R.id.tv_RoutineOrders);
        tv_AayushmanOrders = (TextView) findViewById(R.id.tv_AayushmanOrders);
        recyOrderList = (RecyclerView) findViewById(R.id.recyOrderList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_visit_orders_display);
        layoutEarnings = (LinearLayout) findViewById(R.id.layoutEarnings);
        lin_categories = (LinearLayout) findViewById(R.id.lin_categories);
//        txtEstDistance = (TextView) findViewById(R.id.txtEstDistance);
        txtEstEarnings = (TextView) findViewById(R.id.txtEstEarnings);
        txtEstKits = (TextView) findViewById(R.id.txtEstKits);
        txtEstKits.setSelected(true);
        txtNoRecord = (TextView) findViewById(R.id.txt_no_orders);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_home = findViewById(R.id.iv_home);
        iv_back = findViewById(R.id.iv_back);
        tv_toolbar.setText("Visit Orders");

    }

    private void setListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_RoutineOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicledonAayushmanOrders = false;
                tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled_oranged);
                tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty_orange);
                tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
                tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.bg_new_color));

                layoutEarnings.setVisibility(View.VISIBLE);
                if (orderDetailsResponseModels_RoutineOrders.size() > 0) {
                    prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    recyOrderList.setVisibility(View.GONE);
                    txtNoRecord.setVisibility(View.VISIBLE);
                }

            }
        });

        tv_AayushmanOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isClicledonAayushmanOrders = true;

                tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_empty_orange);
                tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_filled_oranged);
                tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.bg_new_color));
                tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
                layoutEarnings.setVisibility(View.GONE);
                if (orderDetailsResponseModels_AayushmanOrders.size() > 0) {
                    prepareRecyclerView(orderDetailsResponseModels_AayushmanOrders);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    recyOrderList.setVisibility(View.GONE);
                    txtNoRecord.setVisibility(View.VISIBLE);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        txtEstKits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    LayoutInflater inflater = activity.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alert_test_edit, null);
                    builder.setView(dialogView);
                    ListView lv_test_codes = (ListView) dialogView.findViewById(R.id.lv_test_codes);
                    Button btn_edit = (Button) dialogView.findViewById(R.id.btn_edit);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_1, kits_arr);
                    lv_test_codes.setAdapter(adapter);
                    btn_edit.setVisibility(View.GONE);
                    builder.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        gpsTracker = new GPSTracker(activity);
        if (isphonecallstarted) {
            isphonecallstarted = false;
        } else {
            if (Constants.isWOEDone) {
                Constants.isWOEDone = false;
                fetchData();
            }
        }
    }

    private void fetchData() {

        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<FetchOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getAllVisitDetails(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.fetchingOrders), false);
        fetchOrderDetailsResponseModelCall.enqueue(new Callback<FetchOrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<FetchOrderDetailsResponseModel> call, Response<FetchOrderDetailsResponseModel> response) {
                global.hideProgressDialog(activity);
                try {
                    if (response.isSuccessful()) {

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
                    } else {
                        global.hideProgressDialog(activity);
                    }


                } catch (Exception e) {
                    global.hideProgressDialog(activity);
                    e.printStackTrace();
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<FetchOrderDetailsResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
        });

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

        if (orderDetailsResponseModels_RoutineOrders != null) {
            orderDetailsResponseModels_RoutineOrders = null;
        }
        orderDetailsResponseModels_RoutineOrders = new ArrayList<>();


        orderDetailsResponseModels_AayushmanOrders = null;
        orderDetailsResponseModels_AayushmanOrders = new ArrayList<>();

        if (orderDetailsResponseModels.size() > 0) {
            for (int i = 0; i < orderDetailsResponseModels.size(); i++) {
                for (int j = 0; j < orderDetailsResponseModels.get(i).getAllOrderdetails().size(); j++) {
                    if (!orderDetailsResponseModels.get(i).getAllOrderdetails().get(j).isEuOrders()) {
                        orderDetailsResponseModels_RoutineOrders.add(orderDetailsResponseModels.get(i));
                    } else {
                        orderDetailsResponseModels_AayushmanOrders.add(orderDetailsResponseModels.get(i));
                    }
                }

            }
        }


        // TODO By default show Routine Orders

        tv_RoutineOrders.setText("Routine Orders (" + orderDetailsResponseModels_RoutineOrders.size() + ")");
        tv_AayushmanOrders.setText("Aayushman Orders (" + orderDetailsResponseModels_AayushmanOrders.size() + ")");

        if (isClicledonAayushmanOrders) {
            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_empty_orange);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_filled_oranged);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.bg_new_color));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            layoutEarnings.setVisibility(View.GONE);
            prepareRecyclerView(orderDetailsResponseModels_AayushmanOrders);
        } else {

            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled_oranged);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty_orange);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.bg_new_color));
            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
        }

        if (orderDetailsResponseModels_AayushmanOrders.size() > 0) {
            lin_categories.setVisibility(View.VISIBLE);
            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled_oranged);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty_orange);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.bg_new_color));
            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
        } else {
            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
            lin_categories.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setRefreshing(false);

    }

    private void prepareRecyclerView(ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels) {
        if (orderDetailsResponseModels.size() > 0) {
            recyOrderList.setVisibility(View.VISIBLE);
            txtNoRecord.setVisibility(View.GONE);

            if (btech_VisitDisplayAdapter != null) {
                btech_VisitDisplayAdapter.UpdateList(orderDetailsResponseModels);
                btech_VisitDisplayAdapter.notifyDataSetChanged();
            } else {
                btech_VisitDisplayAdapter = new Btech_VisitDisplayAdapter(this, activity, orderDetailsResponseModels);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                recyOrderList.setLayoutManager(mLayoutManager);
                recyOrderList.setAdapter(btech_VisitDisplayAdapter);
            }


            btech_VisitDisplayAdapter.setOnItemClickListener(new Btech_VisitDisplayAdapter.OnClickListeners() {
                @Override
                public void onAcceptClicked(OrderVisitDetailsModel orderVisitDetailsModels) {
                    OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
                    orderStatusChangeRequestModel.setId(orderVisitDetailsModels.getSlotId() + "");
                    orderStatusChangeRequestModel.setRemarks("");
                    orderStatusChangeRequestModel.setStatus(8);
                    if (isNetworkAvailable(activity)) {
                        CallOrderStatusChangeAPIAfterAcceptButtonClicked(orderStatusChangeRequestModel);
                    } else {
                        TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                }

                @Override
                public void onCallCustomer(OrderVisitDetailsModel orderVisitDetailsModels) {

                    try {
                        callgetDispositionData(orderVisitDetailsModels);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (connectionDetector.isConnectingToInternet()) {
                        CallPatchRequestAPI(orderVisitDetailsModels);
                    } else {
                        Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
                    crr = new ConfirmRequestReleaseDialog(activity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
                    crr.show();
                }

                @Override
                public void onItemReleaseTo(String pincode, OrderVisitDetailsModel orderVisitDetailsModel) {

                    Cop = new ConfirmOrderPassDialog(VisitOrdersDisplayFragment_new.this, new refreshDelegate() {
                        @Override
                        public void onRefreshClicked() {
                            fetchData();
                            swipeRefreshLayout.setRefreshing(true);
                            startActivity(new Intent(activity, VisitOrdersDisplayFragment_new.class));
//                            pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment_new.TAG_FRAGMENT);
                        }
                    }, pincode, orderVisitDetailsModel);
                    Cop.show();
                }

                @Override
                public void onStart(OrderVisitDetailsModel orderVisitDetailsModel) {
                    gpsTracker = new GPSTracker(activity);
                    if (gpsTracker.canGetLocation()) {
                        if (orderVisitDetailsModel.getAllOrderdetails().get(0).isKCF()) {
                            ProceedToArriveScreen(orderVisitDetailsModel, false);
                        } else {
                            if (isNetworkAvailable(activity)) {
                                callOrderStatusChangeStartApi(orderVisitDetailsModel, 7);
                            } else {
                                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        gpsTracker.showSettingsAlert();
                        Toast.makeText(activity, "Check Internet connection and gps settings", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onRefresh() {
                    fetchData();
                }
            });

        } else {
            recyOrderList.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private void CallPatchRequestAPI(OrderVisitDetailsModel orderVisitDetailsModels) {
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallpatchRequestAPI(appPreferenceManager.getLoginResponseModel().getUserID(), orderVisitDetailsModels.getAllOrderdetails().get(0).getMobile(), orderVisitDetailsModels.getVisitId());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.loading));
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        MaskedPhoneNumber = response.body();
                        TedPermission.with(activity)
                                .setPermissions(Manifest.permission.CALL_PHONE)
                                .setRationaleMessage("We need permission to make call from your device.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > Permission > Telephone")
                                .setPermissionListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted() {
                                        if (!StringUtils.isNull(MaskedPhoneNumber)) {
                                            isphonecallstarted = true;
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:" + MaskedPhoneNumber.replace("\"", "")));
                                            activity.startActivity(intent);
                                        } else {
                                            global.showCustomToast(activity, "Invalid number");
                                        }
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
        if (isNetworkAvailable(activity)) {
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

        ArrayAdapter<String> spnrnotconnectedremarks = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, remarks_notc_arr);
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

                        if (isNetworkAvailable(activity)) {
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


    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            if (isNetworkAvailable(activity)) {
                callOrderStatusChangeApi(orderStatusChangeRequestModel);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private void callOrderStatusChangeStartApi(final OrderVisitDetailsModel orderVisitDetailsModel, int status) {

        OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
        orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
        orderStatusChangeRequestModel.setStatus(status);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(activity, getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.code() == 200 || response.code() == 204) {
                            if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())){
                                ProceedToArriveScreen(orderVisitDetailsModel, false);
                            }else{
                                onOrderStatusChangedResponseReceived(orderVisitDetailsModel);
                            }
                        } else {
                            try {
                                Toast.makeText(activity, response.errorBody() != null ? response.errorBody().string() : SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onOrderStatusChangedResponseReceived(final OrderVisitDetailsModel orderVisitDetailsModel) {
        try {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

            View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));

            String s = "Do you want to Open Map for Direction ?";
            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText(s);

            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProceedToArriveScreen(orderVisitDetailsModel, true);
                    bottomSheetDialog.dismiss();
                }
            });

            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProceedToArriveScreen(orderVisitDetailsModel, false);
                    bottomSheetDialog.dismiss();

                }
            });

            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

            /*AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder
                    .setMessage("Do you want to Open Map for Direction ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ProceedToArriveScreen(orderVisitDetailsModel, true);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ProceedToArriveScreen(orderVisitDetailsModel, false);
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ProceedToArriveScreen(OrderVisitDetailsModel orderVisitDetailsModel, boolean OpenMap) {

        try {
            startTrackerService();
            SendinglatlongOrderAllocation(orderVisitDetailsModel, 7);
            String remarks = "Order Started";
            new LogUserActivityTagging(activity, BundleConstants.WOE, remarks);
//            Toast.makeText(activity, "Started Successfully", Toast.LENGTH_SHORT).show();
            Intent intentNavigate = new Intent(activity, StartAndArriveActivity.class);
            intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            activity.startActivity(intentNavigate);
            if (OpenMap) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + orderVisitDetailsModel.getAllOrderdetails().get(0).getLatitude() + "," + orderVisitDetailsModel.getAllOrderdetails().get(0).getLongitude()));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SendinglatlongOrderAllocation(OrderVisitDetailsModel orderVisitDetailsModel, int status) {

        if (ApplicationController.sendLatLongforOrderController != null) {
            ApplicationController.sendLatLongforOrderController = null;
        }
        ApplicationController.sendLatLongforOrderController = new SendLatLongforOrderController(activity);
        ApplicationController.sendLatLongforOrderController.SendLatlongToToServer(orderVisitDetailsModel.getVisitId(), status);
        ApplicationController.sendLatLongforOrderController.setOnResponseListener(new SendLatLongforOrderController.OnResponseListener() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onfailure(String msg) {

            }
        });

    }

    private void startTrackerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(FirebaselocationUpdateIntent);
        } else {
            activity.startService(FirebaselocationUpdateIntent);
        }
    }


    private void callOrderStatusChangeApi(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(activity, PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
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
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }


    private void CallOrderStatusChangeAPIAfterAcceptButtonClicked(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallOrderStatusChangeAPI(orderStatusChangeRequestModel, orderStatusChangeRequestModel.getId());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.progress_message_changing_order_status_please_wait));

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog(activity);
                int statusCode = response.code();
                if (statusCode == 204 || statusCode == 200) {
                    TastyToast.makeText(activity, "Order Accepted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                    String remarks = "Order Accepted";
                    new LogUserActivityTagging(activity, BundleConstants.WOE, remarks);
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
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getBtechEstEarnings() {

        if (isNetworkAvailable(activity)) {
            CallGetBtechEstEarningsApi();
        } else {
            TastyToast.makeText(activity, "Check internet connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            //  initData();
        }
    }

    private void CallGetBtechEstEarningsApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<BtechEstEarningsResponseModel> responseCall = apiInterface.CallGetBtechEstEarningsApi(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<BtechEstEarningsResponseModel>() {
            @Override
            public void onResponse(Call<BtechEstEarningsResponseModel> call, retrofit2.Response<BtechEstEarningsResponseModel> response) {
                int totalEarning = 0;
                HashMap<String, Integer> kitsCount = new HashMap<>();
                String kitsReq = "";
                String kitsReq1 = "";

                if (response.isSuccessful() && response.body() != null) {
                    BtechEstEarningsResponseModel btechEstEarningsResponseModel = response.body();
                    if (btechEstEarningsResponseModel != null && btechEstEarningsResponseModel.getBtechEarnings().size() > 0) {
//                        txtEstDistance.setText("" + btechEstEarningsResponseModel.getDistance() + " Kms");
                        for (int i = 0; i < btechEstEarningsResponseModel.getBtechEarnings().size(); i++) {
                            for (int j = 0; j < btechEstEarningsResponseModel.getBtechEarnings().get(i).getVisitEarnings().size(); j++) {
                                totalEarning = totalEarning + btechEstEarningsResponseModel.getBtechEarnings().get(i).getVisitEarnings().get(j).getEstIncome();
                                Logger.error("totaldistance: " + totalEarning);
                                txtEstEarnings.setText("" + totalEarning);

                                for (KitsCountModel kt :
                                        btechEstEarningsResponseModel.getBtechEarnings().get(i).getVisitEarnings().get(j).getKits()) {
                                    if (kitsCount.containsKey(kt.getKit())) {
                                        kitsCount.put(kt.getKit(), kitsCount.get(kt.getKit()) + kt.getValue());
                                    } else {
                                        kitsCount.put(kt.getKit(), kt.getValue());
                                    }
                                }

                                //=====
                            }
                        }
                        Iterator it = kitsCount.entrySet().iterator();
                        while (it.hasNext()) {
                            HashMap.Entry pair = (HashMap.Entry) it.next();
                            if (InputUtils.isNull(kitsReq)) {
                                kitsReq = pair.getValue() + " " + pair.getKey();
                                kitsReq1 = pair.getValue() + " " + pair.getKey();
                            } else {
                                kitsReq = kitsReq + " | " + pair.getValue() + " " + pair.getKey();
                                kitsReq1 = kitsReq1 + "," + pair.getValue() + " " + pair.getKey();
                            }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        kits = kitsReq1;
                        kits_arr = kits.split(",");
                        Logger.error("arr: " + kits_arr.toString());
                        Logger.error("test code string: " + kits);
                        //  txtTotalKitsRequired.setText(kits_arr[0]);

                        String regex = "\\s*\\bKIT\\b\\s*";
                        kitsReq = kitsReq.replaceAll(regex, "");

                        txtEstKits.setText(kitsReq);
                    }

                }/* else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<BtechEstEarningsResponseModel> call, Throwable t) {
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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


                System.out.println("AppId :" + "" + setDispositionDataModel.getAppId());
                System.out.println("DispId :" + "" + setDispositionDataModel.getDispId());
                System.out.println("FrmNo : " + "" + setDispositionDataModel.getFrmNo());
                System.out.println("OrderNo : " + "" + setDispositionDataModel.getOrderNo());
                System.out.println("Remarks : " + "" + setDispositionDataModel.getRemarks());
                System.out.println("ToNo : " + "" + setDispositionDataModel.getToNo());
                System.out.println("UserId :" + "" + setDispositionDataModel.getUserId());

                request.setEntity(entity);
                HttpResponse response = httpClient.execute(request);
                StatusLine statusLine = response.getStatusLine();
                statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
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
            if (statusCode == 200) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
            startActivity(intent);
        } else {
            // Permission denied, Disable the functionality that depends on activity permission.
            TastyToast.makeText(activity, "permission denied", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            // Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
        }
    }
}
