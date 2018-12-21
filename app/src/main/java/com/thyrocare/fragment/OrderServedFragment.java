package com.thyrocare.fragment;

/**
 * Created by Orion on 6/14/2017.
 */

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.adapter.OrderServedDisplayDetailsAdapter;
import com.thyrocare.delegate.OrderServedDisplayDetailsAdapterClickedDelegate;
import com.thyrocare.models.api.response.OrderServedResponseModel;
import com.thyrocare.models.data.BtechOrderModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Orion on 5/2/2017.<br/>
 * for getting orders<br/>
 * http://bts.dxscloud.com/btsapi/api/BtechOrderSummary/BtechServedOrders/884543107/2017-07-19<br/>
 */

public class OrderServedFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "ORDER_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView tv_date, no_orders;
    RecyclerView recycler_view;
    private String todaysDate;
    private ArrayList<BtechOrderModel> btechOrderModels = new ArrayList<>();
    private OrderServedDisplayDetailsAdapter orderServedDisplayDetailsAdapter;
    private int mYear, mMonth, mDay;
    private ImageView img_search;
    private String todate = "";
    private String MaskedPhoneNumber = "";

    public OrderServedFragment() {
        // Required empty public constructor
    }

    public static OrderServedFragment newInstance() {
        OrderServedFragment fragment = new OrderServedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        try {
            if (activity != null) {
                if (activity.toolbarHome != null) {
                    activity.toolbarHome.setTitle("Orders served");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_served, container, false);
        Calendar calendar = Calendar.getInstance();
        Date jd = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        todaysDate = simpleDateFormat.format(jd);
        todate = sdf.format(jd);
        initUI();

        setListners();
        initData(todaysDate);
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_date.setText("" + todate);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);
        no_orders = (TextView) rootView.findViewById(R.id.no_orders);
    }

    private void setListners() {


        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                todaysDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                //changes_20june2017
                                initData(todaysDate);
                                //changes_20june2017
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changes_20june2017
                //initData(todaysDate);
                //changes_20june2017
            }
        });
    }


    private void initData(String date) {

        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderServedListListDetailApiAsyncTask = asyncTaskForRequest.getOrderServedDetailsDisplayRequestAsyncTask(date);
        fetchOrderServedListListDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new OrderServedDisplayApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchOrderServedListListDetailApiAsyncTask.execute(fetchOrderServedListListDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class OrderServedDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);

                OrderServedResponseModel orderServedResponseModel = new OrderServedResponseModel();
                orderServedResponseModel = responseParser.getOrderServedDetailsResponseModel(json, statusCode);
                if (orderServedResponseModel != null && orderServedResponseModel.getBtchOrd().size() > 0) {
                    btechOrderModels = orderServedResponseModel.getBtchOrd();
                    no_orders.setVisibility(View.GONE);
                } else {
                    btechOrderModels = new ArrayList<>();
                    no_orders.setVisibility(View.VISIBLE);
                }
                prepareRecyclerView();
            } else {
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareRecyclerView() {
        orderServedDisplayDetailsAdapter = new OrderServedDisplayDetailsAdapter(btechOrderModels, activity, new OrderServedDisplayDetailsAdapterClickedDelegate() {
            @Override
            public void onCallCustomer(String customerMobile, String orderNo) {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + customerMobile));
                activity.startActivity(intent);



/*
                CallPatchRequestModel callPatchRequestModel = new CallPatchRequestModel();
                callPatchRequestModel.setSrcnumber(appPreferenceManager.getLoginResponseModel().getUserID());
                callPatchRequestModel.setDestNumber(customerMobile);
                callPatchRequestModel.setVisitID(orderNo);
                ApiCallAsyncTask callPatchRequestAsyncTask = new AsyncTaskForRequest(activity).getCallPatchRequestAsyncTask(callPatchRequestModel);
                callPatchRequestAsyncTask.setApiCallAsyncTaskDelegate(new CallPatchRequestAsyncTaskDelegateResult());
                callPatchRequestAsyncTask.execute(callPatchRequestAsyncTask);*/
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(orderServedDisplayDetailsAdapter);

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
}




