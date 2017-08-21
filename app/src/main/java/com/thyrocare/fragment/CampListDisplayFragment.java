package com.thyrocare.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.CampOrderBookingActivity;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.adapter.CampListDetailDisplayAdapter;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.dao.models.OrderDetailsDao;
import com.thyrocare.delegate.CampListDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.dialog.ConfirmOrderReleaseDialog;
import com.thyrocare.models.api.request.CallPatchRequestModel;
import com.thyrocare.models.api.request.CampStartedRequestModel;
import com.thyrocare.models.api.response.CampListDisplayResponseModel;
import com.thyrocare.models.api.response.CampScanQRResponseModel;
import com.thyrocare.models.data.CampAllOrderDetailsModel;
import com.thyrocare.models.data.CampBtechModel;
import com.thyrocare.models.data.CampDetailModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * API used<br/>
 * /CampDetails/MyCampDetails<br/>
 * /CampDetails/CampStarted<br/>
 */
public class CampListDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = CampListDisplayFragment.class.getSimpleName();
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private OrderDetailsDao orderDetailsDao;
    private BeneficiaryDetailsDao beneficiaryDetailsDao;
    private View rootView;
    private ListView recyclerView;
    public static String products;
    private TextView txtNoRecord;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConfirmOrderReleaseDialog cdd;
    private ArrayList<CampBtechModel> btechs;
    private ArrayList<CampDetailModel> campDetailModels;
    private CampDetailModel campDetailsResponseModel=new CampDetailModel();
    private CampListDisplayResponseModel campListDisplayResponseModel;
    CampListDetailDisplayAdapter campListDetailDisplayAdapter;
    private int position;
    IntentIntegrator integrator;
    private ArrayList<CampAllOrderDetailsModel> campAllOrderDetailsModelslist;

    public CampListDisplayFragment() {
        // Required empty public constructor
    }

    public static CampListDisplayFragment newInstance() {
        return new CampListDisplayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camp_list, container, false);
        initUI();
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Camp");
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);
        fetchData();
        setListener();
        return rootView;
    }

    public Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date stringToDate(String dtStart) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date date = format.parse(dtStart);

            System.out.println(date);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchData();
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
            Log.e(TAG_FRAGMENT, "prepareRecyclerView:vtech size " + campDetailModels.size());
            campListDetailDisplayAdapter = new CampListDetailDisplayAdapter(activity, campDetailModels/*,campDetailsResponseModel,*/, new CampListDisplayRecyclerViewAdapterDelegateResult());
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
                campListDisplayResponseModel = new CampListDisplayResponseModel();
                campListDisplayResponseModel = responseParser.getCampDetailResponseModel(json, statusCode);
                if (campListDisplayResponseModel != null/* && campDetailsResponseModel.getBtechs().size() > 0*/) {
                    campDetailModels = campListDisplayResponseModel.getCampDetail();
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
        public void onItemClick(CampDetailModel campDetailModel, int status, int pos) {
            Logger.error("Item Clicked");
            Logger.error("" + campDetailModel.getCampId());
            startCampApi(campDetailModel, status, pos);
        }

        @Override
        public void onNavigationStart(CampDetailModel campListDisplayResponseModel) {

        }
    }

    private void startCampApi(CampDetailModel campDetailModel, int i, int pos) {
        position = pos;
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        CampStartedRequestModel campStartedRequestModel = new CampStartedRequestModel();
        campStartedRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        campStartedRequestModel.setCampId(campDetailModel.getId());
        campStartedRequestModel.setType(i);
        ApiCallAsyncTask campStartedApiAsyncTask = asyncTaskForRequest.getCampStartedRequestAsyncTask(campStartedRequestModel);
        if (i == 7) {
            campStartedApiAsyncTask.setApiCallAsyncTaskDelegate(new CampStartedAsyncTaskDelegateResult());

            if (isNetworkAvailable(activity)) {
                campStartedApiAsyncTask.execute(campStartedApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 8) {
            campStartedApiAsyncTask.setApiCallAsyncTaskDelegate(new CampAcceptedAsyncTaskDelegateResult());

            if (isNetworkAvailable(activity)) {
                campStartedApiAsyncTask.execute(campStartedApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 3) {
            campStartedApiAsyncTask.setApiCallAsyncTaskDelegate(new CampArrivedAsyncTaskDelegateResult(campDetailModel));

            if (isNetworkAvailable(activity)) {
                campStartedApiAsyncTask.execute(campStartedApiAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 0) {
            if (isNetworkAvailable(activity)) {
                CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
                callPatchRequestModel.setSrcnumber(new AppPreferenceManager(activity).getLoginResponseModel().getUserID());
                callPatchRequestModel.setDestNumber(campDetailModel.getLeaderContactNo()+"");
                ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
                callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
                callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }

        }

    }



    private class CallPatchRequestAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + json));
                    if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        android.Manifest.permission.CALL_PHONE},
                                AppConstants.APP_PERMISSIONS);
                    }
                    else {
                        activity.startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
    private class CampStartedAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                    Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
                Logger.error("" + position);
                campDetailModels.get(position).setStarted(true);
                campListDetailDisplayAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(activity, "error : " + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class CampAcceptedAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "error : " + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class CampArrivedAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        private CampDetailModel campDetailModel;
        public CampArrivedAsyncTaskDelegateResult(CampDetailModel campDetailModel) {
            this.campDetailModel = campDetailModel;
        }

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Intent intentOrderBooking = new Intent(activity, CampOrderBookingActivity.class);
                Logger.error("campDetailModels after arrived "+campDetailModels.size());
                intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
                startActivity(intentOrderBooking);
            }
            else{
                Toast.makeText(activity,""+json,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(activity, "scanned res " + scanningResult, Toast.LENGTH_SHORT).show();
        if (requestCode == BundleConstants.CMD_START && resultCode == BundleConstants.CMD_ARRIVED) {

        }
        if (scanningResult != null && scanningResult.getContents() != null) {
            //  String scanned_barcode = scanningResult.getContents();
            Logger.error("" + scanningResult);
            Logger.error("scanned_barcode " + scanningResult.getContents());
            Toast.makeText(activity, "" + scanningResult, Toast.LENGTH_SHORT).show();
            callsendQRCodeApi(scanningResult.getContents());

        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callsendQRCodeApi(String contents) {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderDetailApiAsyncTask = asyncTaskForRequest.getSendQRCodeRequestAsyncTask(contents);
        fetchOrderDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new SendQRCodeApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderDetailApiAsyncTask.execute(fetchOrderDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private class SendQRCodeApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                CampScanQRResponseModel campScanQRResponseModel = new CampScanQRResponseModel();
                campScanQRResponseModel = responseParser.getcampOrderDetailsResponseModel(json, statusCode);
                if (campScanQRResponseModel != null && campScanQRResponseModel.getAllOrderdetails().size() > 0) {
                    for (int i = 0; i < campScanQRResponseModel.getAllOrderdetails().size(); i++) {
                        for (int j = 0; j <
                                campScanQRResponseModel.getAllOrderdetails().get(i).getBenMaster().size(); j++) {
                            campScanQRResponseModel.getAllOrderdetails().get(i).getBenMaster().get(j).setOrderNo(campScanQRResponseModel.getAllOrderdetails().get(i).getOrderNo());
                        }
                    }
                    campAllOrderDetailsModelslist = campScanQRResponseModel.getAllOrderdetails();
                    Logger.error("camp detail size " + campScanQRResponseModel.getAllOrderdetails().size());
                }
                Intent intentOrderBooking = new Intent(activity, CampOrderBookingActivity.class);
                intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModels);
              //  intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campScanQRResponseModel);
                startActivity(intentOrderBooking);
            } else {
                Toast.makeText(activity, "error : " + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
