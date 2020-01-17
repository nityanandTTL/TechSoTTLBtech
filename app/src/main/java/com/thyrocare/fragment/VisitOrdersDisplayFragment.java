package com.thyrocare.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.OrderBookingActivity;
import com.thyrocare.activity.VisitOrderDetailMapDisplayFragmentActivity;
import com.thyrocare.adapter.VisitOrderDisplayAdapter;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.CallbackforShowCaseDelegate;
import com.thyrocare.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.delegate.OrderPassRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.OrderRescheduleDialogButtonClickedDelegate;
import com.thyrocare.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.VisitOrderDisplayyRecyclerViewAdapterDelegate;
import com.thyrocare.delegate.refreshDelegate;
import com.thyrocare.dialog.ConfirmOrderPassDialog;
import com.thyrocare.dialog.ConfirmOrderReleaseDialog;
import com.thyrocare.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.dialog.RescheduleOrderDialog;
import com.thyrocare.models.api.request.CallPatchRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.api.request.SetDispositionDataModel;
import com.thyrocare.models.api.response.BtechEstEarningsResponseModel;
import com.thyrocare.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.DespositionDataModel;
import com.thyrocare.models.data.DispositionDataModel;
import com.thyrocare.models.data.DispositionDetailsModel;
import com.thyrocare.models.data.KitsCountModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.InputUtils;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class VisitOrdersDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "VISIT_ORDERS_DISPLAY_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private OrderDetailsDao orderDetailsDao;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private View rootView;
    private ListView recyclerView;
    private TextView txtTotalDistance;
    private TextView txtTotalEarnings;
    private TextView txtTotalKitsRequired;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_RoutineOrders = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_AayushmanOrders = new ArrayList<>();
    private TextView txtNoRecord;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConfirmOrderReleaseDialog cdd;
    private ConfirmRequestReleaseDialog crr;
    private ConfirmOrderPassDialog Cop;
    private boolean isToFromMap = false;
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
    private LinearLayout ll_visit_orders_display_footer,lin_categories;
    private boolean isClicledonAayushmanOrders = false;

    public VisitOrdersDisplayFragment() {
        // Required empty public constructor
    }

    public static VisitOrdersDisplayFragment newInstance() {
        VisitOrdersDisplayFragment fragment = new VisitOrdersDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Visit Orders");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();

        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Visit orders");
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        beneficiaryDetailsDao = new BeneficiaryDetailsDao(dhbDao.getDb());

        if (getArguments() != null) {

        }
        getBtechEstEarnings();
    }

    private void getBtechEstEarnings() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchBtechEstEarningsApiAsyncTask = asyncTaskForRequest.getBtechEstEarningsRequestAsyncTask();
        fetchBtechEstEarningsApiAsyncTask.setApiCallAsyncTaskDelegate(new BtechEarningsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchBtechEstEarningsApiAsyncTask.execute(fetchBtechEstEarningsApiAsyncTask);
        } else {
            TastyToast.makeText(activity, "Check internet connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            //  initData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_visit_orders_display, container, false);
        initUI();
        fetchData();
        setListener();

        return rootView;
    }

    private void setListener() {


        tv_RoutineOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isClicledonAayushmanOrders = false;
                tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled);
                tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty);
                tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
                tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                ll_visit_orders_display_footer.setVisibility(View.VISIBLE);
                if (orderDetailsResponseModels_RoutineOrders.size() > 0) {
                    prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txtNoRecord.setVisibility(View.VISIBLE);
                }

            }
        });

        tv_AayushmanOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isClicledonAayushmanOrders = true;

                tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_empty);
                tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_filled);
                tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
                ll_visit_orders_display_footer.setVisibility(View.GONE);
                if (orderDetailsResponseModels_AayushmanOrders.size() > 0) {
                    prepareRecyclerView(orderDetailsResponseModels_AayushmanOrders);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    txtNoRecord.setVisibility(View.VISIBLE);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TastyToast.makeText(activity, "View Refreshed", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                // Toast.makeText(activity, "refresh on realease", Toast.LENGTH_SHORT).show();
                fetchData();
            }
        });

        txtTotalKitsRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = activity.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alert_test_edit, null);
                    builder.setView(dialogView);
                    ListView lv_test_codes = (ListView) dialogView.findViewById(R.id.lv_test_codes);
                    Button btn_edit = (Button) dialogView.findViewById(R.id.btn_edit);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


        // If request is cancelled, the result arrays are empty.
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

    // other 'case' lines to check for other permissions activity app might request.
    // You can add here other case statements according to your requirement.


    @Override
    public void onResume() {
        if (!isToFromMap) {
            fetchData();
        }
        isToFromMap = false;
        super.onResume();
    }

    private void fetchData() {
        beneficiaryDetailsDao.deleteAll();
        orderDetailsDao.deleteAll();
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchOrderDetailsRequestAsyncTask(true);
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            if (!isFetchingOrders) {
                isFetchingOrders = true;
                fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
            }
        } else {
            TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);

            // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private void initData() {
        orderDetailsResponseModels = orderDetailsDao.getAllModels();

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
        txtTotalDistance.setText(totalDistance + " Kms");
        int amount_estIncome = Math.round(estIncome);
        txtTotalEarnings.setText(amount_estIncome + "");
        Iterator it = kitsCount.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            if (InputUtils.isNull(kitsReq)) {
                kitsReq = pair.getValue() + " " + pair.getKey();
            } else {
                kitsReq = kitsReq + " | " + pair.getValue() + " " + pair.getKey();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        String regex = "\\s*\\bKIT\\b\\s*";
        kitsReq = kitsReq.replaceAll(regex, "");

        txtTotalKitsRequired.setText(kitsReq);

        if (orderDetailsResponseModels_RoutineOrders != null) {
            orderDetailsResponseModels_RoutineOrders = null;
        }
        orderDetailsResponseModels_RoutineOrders = new ArrayList<>();

        if (orderDetailsResponseModels_AayushmanOrders != null) {
            orderDetailsResponseModels_AayushmanOrders = null;
        }
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
            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_empty);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_filled);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            ll_visit_orders_display_footer.setVisibility(View.GONE);
            prepareRecyclerView(orderDetailsResponseModels_AayushmanOrders);
        } else {

            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
        }

        if (orderDetailsResponseModels_AayushmanOrders.size() > 0) {
            lin_categories.setVisibility(View.VISIBLE);
            tv_RoutineOrders.setBackgroundResource(R.drawable.rounded_background_filled);
            tv_AayushmanOrders.setBackgroundResource(R.drawable.rounded_background_empty);
            tv_RoutineOrders.setTextColor(ContextCompat.getColor(activity, R.color.white));
            tv_AayushmanOrders.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
        } else {

            prepareRecyclerView(orderDetailsResponseModels_RoutineOrders);
            lin_categories.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setRefreshing(false);

    }

    private void prepareRecyclerView(ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels) {

        if (orderDetailsResponseModels.size() > 0) {
            VisitOrderDisplayAdapter visitOrderDisplayRecyclerViewAdapter = new VisitOrderDisplayAdapter(activity, orderDetailsResponseModels, new VisitOrderDisplayRecyclerViewAdapterDelegateResult(), new refreshDelegate() {
                @Override
                public void onRefreshClicked() {
                    fetchData();
                }
            }, new VisitOrderDisplayyRecyclerViewAdapterDelegate() {

                @Override
                public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
                    crr = new ConfirmRequestReleaseDialog(activity, new CConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
                    crr.show();
                }
            }, new OrderPassRecyclerViewAdapterDelegate() {
                @Override
                public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
                   /* Cop = new ConfirmOrderPassDialog(activity, new ConfirmOrderPassDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
                    Cop.show();*/
                }

                @Override
                public void onItemReleaseto(String Pincode, OrderVisitDetailsModel orderVisitDetailsModel) {
                    /* Cop = new ConfirmOrderPassDialog(activity, new ConfirmOrderPassDialogButtonClickedDelegateResult(),Pincode,orderVisitDetailsModel );*/
                    Cop = new ConfirmOrderPassDialog(activity, new refreshDelegate() {
                        @Override
                        public void onRefreshClicked() {
                            fetchData();
                            swipeRefreshLayout.setRefreshing(true);
                            pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, VisitOrdersDisplayFragment.TAG_FRAGMENT);
                        }
                    }, Pincode, orderVisitDetailsModel);
                    Cop.show();
                }
            }, new CallbackforShowCaseDelegate() {
                @SuppressLint("LongLogTag")
                @Override
                public void onFirstPosition(View view, boolean isAccepted) {

                    Log.e(TAG_FRAGMENT, "onFirstPosition: ");
                    if (!appPreferenceManager.isLoadSpotlightOnOrderd()) {
                        appPreferenceManager.setLoadSpotlightOnOrderd(true);

                        loadSpotlight(view, isAccepted);

                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onAcceptOrderFirstPosition(View view) {

                    Log.e(TAG_FRAGMENT, "onAcceptOrderFirstPosition: ");

                    loadSpotlightAfterAceepting(view);

                }
            });
            recyclerView.setAdapter(visitOrderDisplayRecyclerViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            txtNoRecord.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private void loadSpotlight(final View view, final boolean isAccepted) {
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("run", "run: ");
                            SpotlightSequence.getInstance(activity, null)
                                    .addSpotlight(view.findViewById(R.id.img_release2), "Order Edit", "You can release, reschedule Order", "bin");
//                                    .addSpotlight(view.findViewById(R.id.img_view_test), "View tests ", "You can view all the tests", "viewtest");


                            /*if (!isAccepted) {
                                SpotlightSequence.getInstance(activity, null).addSpotlight(view.findViewById(R.id.img_oas), "Accept Order ", "Accept alloted order", "accepto");
                            } else {
                                SpotlightSequence.getInstance(activity, null).addSpotlight(view.findViewById(R.id.call), "Call ", "Tap here to call customer", "callcustm");
                            }
*/

                            SpotlightSequence.getInstance(activity, null).startSequence();
                        }
                    }, 400);
                }
            });
        }
    }

    private void loadSpotlightAfterAceepting(final View view) {
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("run11222", "run: ");
                            SpotlightSequence.getInstance(activity, null)
                                    .addSpotlight(view.findViewById(R.id.call), "Call customer", "Tap here to call customer", "callcustjk")

                                    .startSequence();
                        }
                    }, 400);
                }
            });
        }
    }


    private class FetchOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                //jai
                JSONObject jsonObject = new JSONObject(json);
                ResponseParser responseParser = new ResponseParser(activity);
                FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = new FetchOrderDetailsResponseModel();
                fetchOrderDetailsResponseModel = responseParser.getFetchOrderDetailsResponseModel(json, statusCode);
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
                                if (orderDetailsModel.getBenMaster() != null && orderDetailsModel.getBenMaster().size() > 0) {
                                    for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                            orderDetailsModel.getBenMaster()) {
                                        beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
                                        beneficiaryDetailsModel.setTests(beneficiaryDetailsModel.getTestsCode());
                                        for (int i = 0; i < beneficiaryDetailsModel.getSampleType().size(); i++) {
                                            beneficiaryDetailsModel.getSampleType().get(i).setBenId(beneficiaryDetailsModel.getBenId());
                                        }
                                        beneficiaryDetailsDao.insertOrUpdate(beneficiaryDetailsModel);
                                    }
                                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                                }
                            }
                        }
                    }
                }
            }
            isFetchingOrders = false;
            initData();
        }

        @Override
        public void onApiCancelled() {
            isFetchingOrders = false;
            TastyToast.makeText(activity, getString(R.string.network_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //  Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initUI() {

        tv_RoutineOrders = (TextView) rootView.findViewById(R.id.tv_RoutineOrders);
        tv_AayushmanOrders = (TextView) rootView.findViewById(R.id.tv_AayushmanOrders);
        recyclerView = (ListView) rootView.findViewById(R.id.rv_visit_orders_display);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_visit_orders_display);
        ll_visit_orders_display_footer = (LinearLayout) rootView.findViewById(R.id.ll_visit_orders_display_footer);
        lin_categories = (LinearLayout) rootView.findViewById(R.id.lin_categories);
        txtTotalDistance = (TextView) rootView.findViewById(R.id.title_est_distance);
        txtTotalEarnings = (TextView) rootView.findViewById(R.id.title_est_earnings);
        txtTotalKitsRequired = (TextView) rootView.findViewById(R.id.title_est_kits);
        txtTotalKitsRequired.setSelected(true);
        txtNoRecord = (TextView) rootView.findViewById(R.id.txt_no_orders);


    }

    private class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    MaskedPhoneNumber = json;
                    intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
//                    intent.setData(Uri.parse("tel:" + "02248900190"));
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    } else {
                        activity.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onApiCancelled() {

        }

    }

    private class VisitOrderDisplayRecyclerViewAdapterDelegateResult implements VisitOrderDisplayRecyclerViewAdapterDelegate {
        @Override
        public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
            cdd = new ConfirmOrderReleaseDialog(activity, new ConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
            cdd.show();
        }

        @Override
        public void onCallCustomer(OrderVisitDetailsModel orderVisitDetailsModel) {

            try {
                callgetDispositionData(orderVisitDetailsModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile()));
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }*/


            CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
            callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
            callPatchRequestModel.setDestNumber(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
            Logger.error("orderVisitDetailsModelsArr" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
            callPatchRequestModel.setVisitID(orderVisitDetailsModel.getVisitId());
            ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
            callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
            callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);

        }

        @Override
        public void onItemReschedule(OrderVisitDetailsModel orderVisitDetailsModel) {
            rod = new RescheduleOrderDialog(activity, new OrderRescheduleDialogButtonClickedDelegateResult(), orderVisitDetailsModel.getAllOrderdetails().get(0));
            rod.show();
        }

        @Override
        public void onNavigationStart(OrderVisitDetailsModel orderVisitDetailsModel) {
            Intent intentNavigate = new Intent(activity, VisitOrderDetailMapDisplayFragmentActivity.class);
            intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            isToFromMap = true;
            startActivityForResult(intentNavigate, BundleConstants.VOMD_START);
        }

        @Override
        public void onOrderAccepted(OrderVisitDetailsModel orderVisitDetailsModel) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks("");
            orderStatusChangeRequestModel.setStatus(8);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeConfirmedApiAsyncTaskDelegateResult(orderVisitDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void callgetDispositionData(OrderVisitDetailsModel orderVisitDetailsModel) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchDispositionApiAsyncTask = asyncTaskForRequest.getDispositionAsyncTask();
        fetchDispositionApiAsyncTask.setApiCallAsyncTaskDelegate(new fetchDispositionAsyncTaskDelegateResult(orderVisitDetailsModel));
        if (isNetworkAvailable(activity)) {
            fetchDispositionApiAsyncTask.execute(fetchDispositionApiAsyncTask);
        } else {
            TastyToast.makeText(activity, "Check Internet Connection..", TastyToast.LENGTH_LONG, TastyToast.INFO);
        }
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

        ArrayAdapter<String> spnrnotconnectedremarks = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, remarks_notc_arr);
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

            ArrayAdapter<String> spinneradapterremarks = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, remarksarr);
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
                    builder = new android.app.AlertDialog.Builder(getActivity());
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
                        Log.e(TAG_FRAGMENT, "onClick: " + s.substring(0, Math.min(s.length(), 18)));
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

    private ArrayList<DespositionDataModel> getDespData() {
        ArrayList<DespositionDataModel> ent = new ArrayList<>();
        DespositionDataModel e = new DespositionDataModel();
        e.setId(0);
        e.setDesp_key("SELECT");
        ent.add(e);

        e = new DespositionDataModel();
        e.setId(1);
        e.setDesp_key("Received");
        ent.add(e);

        e = new DespositionDataModel();
        e.setId(2);
        e.setDesp_key("Not Responding");
        ent.add(e);

        e = new DespositionDataModel();
        e.setId(3);
        e.setDesp_key("Busy");
        ent.add(e);

        e = new DespositionDataModel();
        e.setId(4);
        e.setDesp_key("Responded but busy");
        ent.add(e);


        return ent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleConstants.VOMD_START && resultCode == BundleConstants.VOMD_ARRIVED) {
            OrderVisitDetailsModel orderVisitDetailsModel = data.getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
            Intent intentOrderBooking = new Intent(activity, OrderBookingActivity.class);
            intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            startActivity(intentOrderBooking);
        }
    }

    public class ConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(6);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderVisitDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //   Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    public class CConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderVisitDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                //  Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    public class ConfirmOrderPassDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
           /* AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remarks);
            orderStatusChangeRequestModel.setStatus(27);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeApiAsyncTaskDelegateResult(orderVisitDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }*/
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class OrderStatusChangeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderVisitDetailsModel orderVisitDetailsModel;

        public OrderStatusChangeApiAsyncTaskDelegateResult(OrderVisitDetailsModel orderVisitDetailsModel) {
            this.orderVisitDetailsModel = orderVisitDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 204 || statusCode == 200) {
                TastyToast.makeText(activity, "Order Released Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                // Toast.makeText(activity, "Order Released Successfully", Toast.LENGTH_SHORT).show();
                fetchData();
            } else {
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                // Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity, "Network Error", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            //   Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }


    private class OrderStatusChangeConfirmedApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderVisitDetailsModel orderVisitDetailsModel;

        public OrderStatusChangeConfirmedApiAsyncTaskDelegateResult(OrderVisitDetailsModel orderVisitDetailsModel) {
            this.orderVisitDetailsModel = orderVisitDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 204 || statusCode == 200) {
                TastyToast.makeText(activity, "Order Accepted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                // Toast.makeText(activity, "Order Accepted Successfully", Toast.LENGTH_SHORT).show();
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                for (OrderDetailsModel orderDetailsModel :
                        orderVisitDetailsModel.getAllOrderdetails()) {
                    orderDetailsModel.setStatus("ACCEPTED");
                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                }
                initData();
            } else {
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                //  Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity, "Network Error", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    private class BtechEarningsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            int totalEarning = 0;
            HashMap<String, Integer> kitsCount = new HashMap<>();
            String kitsReq = "";
            String kitsReq1 = "";

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                BtechEstEarningsResponseModel btechEstEarningsResponseModel = new BtechEstEarningsResponseModel();
                btechEstEarningsResponseModel = responseParser.getBtecheSTEarningResponseModel(json, statusCode);
                if (btechEstEarningsResponseModel != null && btechEstEarningsResponseModel.getBtechEarnings().size() > 0) {
                    txtTotalDistance.setText("" + btechEstEarningsResponseModel.getDistance() + " Kms");
                    for (int i = 0; i < btechEstEarningsResponseModel.getBtechEarnings().size(); i++) {
                        for (int j = 0; j < btechEstEarningsResponseModel.getBtechEarnings().get(i).getVisitEarnings().size(); j++) {
                            totalEarning = totalEarning + btechEstEarningsResponseModel.getBtechEarnings().get(i).getVisitEarnings().get(j).getEstIncome();
                            Logger.error("totaldistance: " + totalEarning);
                            txtTotalEarnings.setText("" + totalEarning);

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

                    txtTotalKitsRequired.setText(kitsReq);
                }

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private class OrderRescheduleDialogButtonClickedDelegateResult implements OrderRescheduleDialogButtonClickedDelegate {

        @Override
        public void onOkButtonClicked(OrderDetailsModel orderDetailsModel, String remark, String date) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderDetailsModel.getSlotId() + "");
            orderStatusChangeRequestModel.setRemarks(remark);
            orderStatusChangeRequestModel.setStatus(11);
            orderStatusChangeRequestModel.setAppointmentDate(date);
            ApiCallAsyncTask orderStatusChangeApiAsyncTask = asyncTaskForRequest.getOrderStatusChangeRequestAsyncTask(orderStatusChangeRequestModel);
            orderStatusChangeApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderStatusChangeRescheduledApiAsyncTaskDelegateResult(orderDetailsModel));
            if (isNetworkAvailable(activity)) {
                orderStatusChangeApiAsyncTask.execute(orderStatusChangeApiAsyncTask);
            } else {
                TastyToast.makeText(activity, getString(R.string.internet_connetion_error), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                // Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelButtonClicked() {

        }
    }

    private class OrderStatusChangeRescheduledApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderDetailsModel orderDetailsModel;

        public OrderStatusChangeRescheduledApiAsyncTaskDelegateResult(OrderDetailsModel orderDetailsModel) {
            this.orderDetailsModel = orderDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
               /* orderDetailsModel.setStatus("RESCHEDULED");
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                orderDetailsDao.insertOrUpdate(orderDetailsModel);*/
                TastyToast.makeText(activity, "Order rescheduled successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                //Toast.makeText(activity, "Order rescheduled successfully", Toast.LENGTH_SHORT).show();
            } else {
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.INFO);
                // Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
            fetchData();
        }

        @Override
        public void onApiCancelled() {
            TastyToast.makeText(activity, "Network Error", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            //  Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class fetchDispositionAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        OrderVisitDetailsModel orderDet;

        public fetchDispositionAsyncTaskDelegateResult(OrderVisitDetailsModel orderVisitDetailsModel) {
            this.orderDet = orderVisitDetailsModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {

                ResponseParser responseParser = new ResponseParser(activity);
                DispositionDataModel dispositionDataModel = new DispositionDataModel();
                dispositionDataModel = responseParser.getDispositionAPIResponseModel(json, statusCode);

                if (dispositionDataModel != null) {
                    System.out.println("");
                    if (dispositionDataModel.getAllDisp() != null) {
                        if (dispositionDataModel.getAllDisp().size() != 0) {
                            CallDespositionDialog(orderDet, dispositionDataModel.getAllDisp());
                        }
                    }
                }

            } else {
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

        }

        @Override
        public void onApiCancelled() {

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
                HttpPost request = new HttpPost(AbstractApiModel.SERVER_BASE_API_URL + "/api/OrderAllocation/MediaUpload");

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
                    System.out.println("Nitya >> " + json);

                } else {
                    HttpEntity responseEntity = response.getEntity();
                    InputStream content = responseEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();
                    System.out.println("Nitya >> " + json);
                }

            } catch (IllegalStateException e) {
                Log.e("FileUpload Illegal", e.getMessage());
            } catch (IOException e) {
                Log.e("FileUpload IOException ", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception ", e.getMessage());
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

    private class setDispositionDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                if (dialog_ready != null) {
                    if (dialog_ready.isShowing()) {
                        dialog_ready.dismiss();
                    }
                }
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            } else {
                TastyToast.makeText(activity, "" + json, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
