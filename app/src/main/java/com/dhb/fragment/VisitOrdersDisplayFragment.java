package com.dhb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.activity.VisitOrderMapDisplayFragmentActivity;
import com.dhb.adapter.VisitOrderDisplayAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.dhb.delegate.VisitOrderDisplayRecyclerViewAdapterDelegate;
import com.dhb.dialog.ConfirmOrderReleaseDialog;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.response.FetchOrderDetailsResponseModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import org.json.JSONException;

import java.util.ArrayList;

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
                swipeRefreshLayout.setRefreshing(false);
                prepareRecyclerView();
            }
        });

    }

    private void fetchData() {
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
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        if(orderDetailsResponseModels.size()>0) {
            VisitOrderDisplayAdapter visitOrderDisplayRecyclerViewAdapter = new VisitOrderDisplayAdapter(activity, orderDetailsResponseModels, new VisitOrderDisplayRecyclerViewAdapterDelegateResult());
            recyclerView.setAdapter(visitOrderDisplayRecyclerViewAdapter);
            txtNoRecord.setVisibility(View.GONE);
        }
        else{
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
                                if(orderDetailsModel.getBenMaster()!=null && orderDetailsModel.getBenMaster().size()>0) {
                                    for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                                            orderDetailsModel.getBenMaster()) {
                                        beneficiaryDetailsModel.setOrderNo(orderDetailsModel.getOrderNo());
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
    public void initUI(){
        recyclerView = (ListView) rootView.findViewById(R.id.rv_visit_orders_display);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_visit_orders_display);
        txtTotalDistance = (TextView) rootView.findViewById(R.id.title_est_distance);
        txtTotalEarnings = (TextView) rootView.findViewById(R.id.title_est_earnings);
        txtTotalKitsRequired = (TextView) rootView.findViewById(R.id.title_est_kits);
        txtNoRecord = (TextView) rootView.findViewById(R.id.txt_no_orders);
    }

    private class VisitOrderDisplayRecyclerViewAdapterDelegateResult implements VisitOrderDisplayRecyclerViewAdapterDelegate {
        @Override
        public void onItemRelease(OrderVisitDetailsModel orderVisitDetailsModel) {
            cdd = new ConfirmOrderReleaseDialog(activity, new ConfirmOrderReleaseDialogButtonClickedDelegateResult(),orderVisitDetailsModel);
            cdd.show();
        }

        @Override
        public void onNavigationStart(OrderVisitDetailsModel orderVisitDetailsModel) {
            Intent intentNavigate = new Intent(activity, VisitOrderMapDisplayFragmentActivity.class);
            intentNavigate.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL,orderVisitDetailsModel);
            startActivityForResult(intentNavigate,BundleConstants.VOMD_START);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==BundleConstants.VOMD_START && resultCode==BundleConstants.VOMD_ARRIVED){
            OrderVisitDetailsModel orderVisitDetailsModel = data.getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
            Intent intentOrderBooking = new Intent(activity, OrderBookingActivity.class);
            intentOrderBooking.putExtra(BundleConstants.VISIT_ORDER_DETAILS_MODEL,orderVisitDetailsModel);
            startActivity(intentOrderBooking);
        }
    }

    private class ConfirmOrderReleaseDialogButtonClickedDelegateResult implements ConfirmOrderReleaseDialogButtonClickedDelegate {
        @Override
        public void onOkButtonClicked(OrderVisitDetailsModel orderVisitDetailsModel, String remarks) {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
            OrderStatusChangeRequestModel orderStatusChangeRequestModel = new OrderStatusChangeRequestModel();
            orderStatusChangeRequestModel.setId(orderVisitDetailsModel.getSlotId()+"");
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
            if (statusCode == 204||statusCode==200) {
                Toast.makeText(activity, "Order Released Successfully", Toast.LENGTH_SHORT).show();
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                orderDetailsDao.deleteByVisitId(orderVisitDetailsModel.getVisitId());
                initData();
            }else {
                Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
}
