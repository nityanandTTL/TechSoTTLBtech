package com.dhb.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.activity.VisitOrderDetailMapDisplayFragmentActivity;
import com.dhb.adapter.VisitOrderDisplayAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.dao.models.TestRateMasterDao;
import com.dhb.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.dhb.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.dhb.dialog.ConfirmOrderReleaseDialog;
import com.dhb.models.api.request.CallPatchRequestModel;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.response.FetchOrderDetailsResponseModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.KitsCountModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private boolean isToFromMap = false;


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
        activity.toolbarHome.setTitle("Visit Orders");
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
        beneficiaryDetailsDao = new BeneficiaryDetailsDao(dhbDao.getDb());
        if (getArguments() != null) {

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
                fetchData();
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

            String MaskedPhoneNumber = appPreferenceManager.getMaskNumber();
            intent.setData(Uri.parse("tel:" + MaskedPhoneNumber));
            startActivity(intent);
        } else {

            // Permission denied, Disable the functionality that depends on activity permission.
            Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
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
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getFetchOrderDetailsRequestAsyncTask();
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchOrderDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private void initData() {
        orderDetailsResponseModels = orderDetailsDao.getAllModels();
        int totalDistance = 0;
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
        txtTotalKitsRequired.setText(kitsReq);
        prepareRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void prepareRecyclerView() {
        if (orderDetailsResponseModels.size() > 0) {
            VisitOrderDisplayAdapter visitOrderDisplayRecyclerViewAdapter = new VisitOrderDisplayAdapter(activity, orderDetailsResponseModels, new VisitOrderDisplayRecyclerViewAdapterDelegateResult());
            recyclerView.setAdapter(visitOrderDisplayRecyclerViewAdapter);
            txtNoRecord.setVisibility(View.GONE);
        } else {
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private class FetchOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
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
            initData();
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    private class VisitOrderDisplayRecyclerViewAdapterDelegateResult implements VisitOrderDisplayRecyclerViewAdapterDelegate {
        @Override
        public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
            cdd = new ConfirmOrderReleaseDialog(activity, new ConfirmOrderReleaseDialogButtonClickedDelegateResult(), orderVisitDetailsModel);
            cdd.show();
        }

        @Override
        public void onNavigationStart(OrderVisitDetailsModel orderVisitDetailsModel) {
            Intent intentNavigate = new Intent(activity, VisitOrderDetailMapDisplayFragmentActivity.class);
            intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
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
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleConstants.VOMD_START && resultCode == BundleConstants.VOMD_ARRIVED) {
            isToFromMap = true;
            OrderVisitDetailsModel orderVisitDetailsModel = data.getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
            Intent intentOrderBooking = new Intent(activity, OrderBookingActivity.class);
            intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL, orderVisitDetailsModel);
            startActivity(intentOrderBooking);
        }
    }

    private class ConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
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
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
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
                Toast.makeText(activity, "Order Released Successfully", Toast.LENGTH_SHORT).show();
                fetchData();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(activity, "Order Accepted Successfully", Toast.LENGTH_SHORT).show();
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                for (OrderDetailsModel orderDetailsModel :
                        orderVisitDetailsModel.getAllOrderdetails()) {
                    orderDetailsModel.setStatus("ACCEPTED");
                    orderDetailsDao.insertOrUpdate(orderDetailsModel);
                }
                initData();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

}
