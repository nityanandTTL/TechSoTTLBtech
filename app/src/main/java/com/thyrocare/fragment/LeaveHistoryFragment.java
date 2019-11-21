package com.thyrocare.fragment;

/**
 * Created by Orion on 6/14/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.ScheduleYourDayActivity;
import com.thyrocare.activity.ScheduleYourDayActivity2;
import com.thyrocare.adapter.AppliedLeaveDisplayDetailsAdapter;
import com.thyrocare.delegate.LeaveAppliedDisplayDetailsAdapterClickedDelegate;
import com.thyrocare.models.api.response.LeaveAppliedResponseModel;
import com.thyrocare.models.data.BtechOrderModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Orion on 5/2/2017.<br/>
 * for getting orders<br/>
 * http://bts.dxscloud.com/btsapi/api/BtechOrderSummary/BtechServedOrders/884543107/2017-07-19<br/>
 */

public class LeaveHistoryFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "LEAVE_HISTORY_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView tv_date, no_orders;
    RecyclerView recycler_view;
    SwipeRefreshLayout swipeRefreshLayout;
    private String todaysDate;
    private ArrayList<BtechOrderModel> btechOrderModels = new ArrayList<>();
    // private OrderServedDisplayDetailsAdapter orderServedDisplayDetailsAdapter;
    private AppliedLeaveDisplayDetailsAdapter appliedLeaveDisplayDetailsAdapter;
    private int mYear, mMonth, mDay;
    private String todate = "";
    private FloatingActionButton apply_leave;
    private String MaskedPhoneNumber = "";
    public static boolean isFromLeaveHistory = false;
    private ArrayList<LeaveAppliedResponseModel> leaveAppliedResponseModels = new ArrayList<>();

    public LeaveHistoryFragment() {
        // Required empty public constructor
    }

    public static LeaveHistoryFragment newInstance() {
        LeaveHistoryFragment fragment = new LeaveHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        try {
            activity.toolbarHome.setTitle("Leave Applied");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            activity.mCurrentFragmentName=""+LeaveHistoryFragment.class.getSimpleName();
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
        rootView = inflater.inflate(R.layout.fragment_leave_history, container, false);

        initUI();

        setListners();
        initData();
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        no_orders = (TextView) rootView.findViewById(R.id.no_orders);
        apply_leave = (FloatingActionButton) rootView.findViewById(R.id.apply_leave);
    }

    private void setListners() {
        apply_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LeaveIntimationFragment.newInstance(), false, false, TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    initData();
                }
            }
        });

    }


    private void initData() {

        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchOrderServedListListDetailApiAsyncTask = asyncTaskForRequest.getLeaveHistoryDetailsDisplayRequestAsyncTask();
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
                Logger.error(TAG_FRAGMENT + "json " + json);
                JSONArray jsonArray = new JSONArray(json);
                ResponseParser responseParser = new ResponseParser(activity);

                leaveAppliedResponseModels = responseParser.getLeaveAppliedResponse(json, statusCode);
                if (leaveAppliedResponseModels != null && leaveAppliedResponseModels.size() > 0) {
                    no_orders.setVisibility(View.GONE);
                    prepareRecyclerView();
                } else {
                    no_orders.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "No record found", Toast.LENGTH_SHORT).show();
                }


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
        appliedLeaveDisplayDetailsAdapter = new AppliedLeaveDisplayDetailsAdapter(leaveAppliedResponseModels, new LeaveAppliedDisplayDetailsAdapterClickedDelegate() {

            @Override
            public void onClickLeave(String date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                //  Date fd= DateUtils.dateFromString(""+date,sdf);
                String currStringDate = sdf.format(new Date());
                Logger.error("currStringDate " + currStringDate.substring(8, 10));
                Logger.error("clicked date123 " + date.substring(8, 10));
                if (Integer.parseInt(currStringDate.substring(8, 10)) + 1 == Integer.parseInt(date.substring(8, 10))) {
                    Logger.error("tommorrow");
                    Intent mIntent = new Intent(activity, ScheduleYourDayActivity.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    mIntent.putExtra("SHOWNO", "1");
                    mIntent.putExtra("canBackpress", true);
                    startActivity(mIntent);
                } else if (Integer.parseInt(currStringDate.substring(8, 10)) == Integer.parseInt(date.substring(8, 10))) {
                    Logger.error("today");
                } else if (Integer.parseInt(currStringDate.substring(8, 10)) + 2 == Integer.parseInt(date.substring(8, 10))) {
                    Logger.error("day after tommorrow");
                    Intent mIntent = new Intent(activity, ScheduleYourDayActivity2.class);
                    mIntent.putExtra("WHEREFROM", "0");
                    mIntent.putExtra("SHOWNO", "1");
                    mIntent.putExtra("canBackpress", true);
                    startActivity(mIntent);
                } else {
                    Toast.makeText(activity, "cannot edit availability", Toast.LENGTH_SHORT).show();
                }


            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(appliedLeaveDisplayDetailsAdapter);

    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return "" + elapsedDays;
    }
}




