package com.thyrocare.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.api.response.BtechEstEarningsResponseModel;
import com.thyrocare.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.KitsCountModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
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

import org.json.JSONException;
import org.json.JSONObject;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();

        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Visit Orders");
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
        txtTotalDistance.setText(totalDistance + "");
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
        prepareRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void prepareRecyclerView() {

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


            });
            recyclerView.setAdapter(visitOrderDisplayRecyclerViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            txtNoRecord.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
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


        recyclerView = (ListView) rootView.findViewById(R.id.rv_visit_orders_display);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_visit_orders_display);
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

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile()));
            activity.startActivity(intent);



            /*CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
            callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
            callPatchRequestModel.setDestNumber(orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
            Logger.error("orderVisitDetailsModelsArr"+orderVisitDetailsModel.getAllOrderdetails().get(0).getMobile());
            callPatchRequestModel.setVisitID(orderVisitDetailsModel.getVisitId());
            ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
            callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
            callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);*/

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
                    txtTotalDistance.setText("" + btechEstEarningsResponseModel.getDistance());
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

}
