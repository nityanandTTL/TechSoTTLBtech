package com.dhb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.adapter.CampListDetailDisplayAdapter;
import com.dhb.dao.DhbDao;
import com.dhb.dao.models.BeneficiaryDetailsDao;
import com.dhb.dao.models.OrderDetailsDao;
import com.dhb.delegate.CampListDisplayRecyclerViewAdapterDelegate;
import com.dhb.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.dhb.dialog.ConfirmOrderReleaseDialog;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.api.response.FetchOrderDetailsResponseModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.CampBtechModel;
import com.dhb.models.data.CampDetailModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import org.json.JSONException;

import java.util.ArrayList;

public class CampListDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = CampListDisplayFragment.class.getSimpleName();
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private OrderDetailsDao orderDetailsDao;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private View rootView;
    private ListView recyclerView;

    private TextView txtNoRecord;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConfirmOrderReleaseDialog cdd;
    private ArrayList<CampBtechModel> btechs;
    private ArrayList <CampDetailModel> campDetailModels;
    private CampDetailModel campDetailsResponseModel;
    private CampListDisplayResponseModel campListDisplayResponseModel;

    public CampListDisplayFragment() {
        // Required empty public constructor
    }

    public static CampListDisplayFragment newInstance() {
        CampListDisplayFragment fragment = new CampListDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

  /*  @Override
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
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camp_list, container, false);
        initUI();
        activity = (HomeScreenActivity) getActivity();
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
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchCampListDetailApiAsyncTask = asyncTaskForRequest.getCampListDetailsDisplayRequestAsyncTask();
        fetchCampListDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new CampListApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchCampListDetailApiAsyncTask.execute(fetchCampListDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
          //  initData();
        }
    }

    private void initData() {
     //   orderDetailsResponseModels = orderDetailsDao.getAllModels();
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        if (campListDisplayResponseModel != null) {
            Log.e(TAG_FRAGMENT, "prepareRecyclerView:vtech size "+campDetailModels.size() );
            CampListDetailDisplayAdapter campListDetailDisplayAdapter = new CampListDetailDisplayAdapter(activity, campDetailModels/*,campDetailsResponseModel,*/ ,new CampListDisplayRecyclerViewAdapterDelegateResult());
            recyclerView.setAdapter(campListDetailDisplayAdapter);
            txtNoRecord.setVisibility(View.GONE);
        } else {
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private class CampListApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("" + json);
                ResponseParser responseParser = new ResponseParser(activity);
               // campDetailsResponseModel = new CampDetailModel();
                campListDisplayResponseModel=new CampListDisplayResponseModel();
                campListDisplayResponseModel = responseParser.getCampDetailResponseModel(json, statusCode);
                if (campListDisplayResponseModel != null/* && campDetailsResponseModel.getBtechs().size() > 0*/) {
                    campDetailModels = campListDisplayResponseModel.getCampDetail();
                    Logger.error("campDetailModels size " + campListDisplayResponseModel.getCampDetail().size());
                    prepareRecyclerView();
                } else {
                    Logger.error("else " + json);
                }
            } else {
                Logger.error("error " + json);
                Toast.makeText(activity, "error " + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }



    @Override
    public void initUI() {
        recyclerView = (ListView) rootView.findViewById(R.id.rv_camp_list_display);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_camp_list_display);
        txtNoRecord = (TextView) rootView.findViewById(R.id.txt_no_camp_detail);
    }

    private class CampListDisplayRecyclerViewAdapterDelegateResult implements CampListDisplayRecyclerViewAdapterDelegate {


        @Override
        public void onItemClick(CampDetailModel campDetailModel) {

        }

        @Override
        public void onNavigationStart(CampDetailModel campListDisplayResponseModel) {

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
                OrderDetailsDao orderDetailsDao = new OrderDetailsDao(dhbDao.getDb());
                orderDetailsDao.deleteByVisitId(orderVisitDetailsModel.getVisitId());
              //  initData();
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
