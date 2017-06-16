package com.dhb.fragment;

/**
 * Created by E4904 on 6/14/2017.
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.HubDetailMapDisplayFragmentActivity;
import com.dhb.adapter.DispatchToHubDisplayDetailsAdapter;
import com.dhb.adapter.OrderServedDisplayDetailsAdapter;
import com.dhb.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.dhb.models.api.response.DispatchHubDisplayDetailsResponseModel;
import com.dhb.models.api.response.OrderServedResponseModel;
import com.dhb.models.data.BtechOrderModel;
import com.dhb.models.data.HUBBTechModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by E4904 on 5/2/2017.
 */

public class OrderServedFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "ORDER_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView tv_date,no_orders;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_view;
    private String todaysDate;
    private ArrayList<BtechOrderModel> btechOrderModels=new ArrayList<>();
    OrderServedDisplayDetailsAdapter orderServedDisplayDetailsAdapter;
    private int mYear, mMonth, mDay;
    private ImageView img_search;
    private String fromdate = "", todate = "";
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
        activity.toolbarHome.setTitle("Order Served");
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
        todate=sdf.format(jd);
        initUI();

        setListners();
        initData(todaysDate);
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        tv_date=(TextView)rootView.findViewById(R.id.tv_date);
        tv_date.setText(""+todate);
        img_search=(ImageView)rootView.findViewById(R.id.img_search);
        no_orders=(TextView)rootView.findViewById(R.id.no_orders);
    }

    private void setListners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    initData(todaysDate);
                }
            }
        });

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

                                tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" +year );
                                todaysDate = year + "-" + (monthOfYear + 1) + "-" +dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initData(todaysDate);
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
               // Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();

                ResponseParser responseParser = new ResponseParser(activity);

                OrderServedResponseModel orderServedResponseModel = new OrderServedResponseModel();
                orderServedResponseModel = responseParser.getOrderServedDetailsResponseModel(json, statusCode);
                if (orderServedResponseModel != null && orderServedResponseModel.getBtchOrd().size() > 0) {
                    btechOrderModels = orderServedResponseModel.getBtchOrd();
                    Logger.error("btechOrderModels size " + orderServedResponseModel.getBtchOrd().size());
                    no_orders.setVisibility(View.GONE);
                } else {
                    btechOrderModels = new ArrayList<>();
                    Logger.error("else " + json);
                    no_orders.setVisibility(View.VISIBLE);
                }
                prepareRecyclerView();
            } else {
                Toast.makeText(activity, "" + statusCode, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareRecyclerView() {
        orderServedDisplayDetailsAdapter=new OrderServedDisplayDetailsAdapter(btechOrderModels,activity);
       // orderServedDisplayDetailsAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(orderServedDisplayDetailsAdapter);

    }
}




