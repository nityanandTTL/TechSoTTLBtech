package com.dhb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.BtechClientDetailMapDisplayFragmentActivity;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.OlcPickupActivity;
import com.dhb.adapter.BtechClientDetailsAdapter;
import com.dhb.delegate.BtechClientDetailsAdapterOnItemClickDelegate;
import com.dhb.dialog.ConfirmCallDialog;
import com.dhb.models.api.response.BtechClientsResponseModel;
import com.dhb.models.data.BtechClientsModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.BundleConstants;

import org.json.JSONException;

import java.util.ArrayList;


public class BtechClientsListFragment extends AbstractFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private TextView tv_noof_pickup, tv_est_distance;
    private HomeScreenActivity activity;
    public static final String TAG_FRAGMENT = BtechClientsListFragment.class.getSimpleName();
    ArrayList<BtechClientsModel> btechClientsModels = new ArrayList<>();
    BtechClientDetailsAdapter btechClientDetailsAdapter;
    ConfirmCallDialog cod;

    public BtechClientsListFragment() {
    }

    public static BtechClientsListFragment newInstance() {
        BtechClientsListFragment fragment = new BtechClientsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btech_client_list, container, false);
        initUi(view);
        activity = (HomeScreenActivity) getActivity();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                fetchData();
            }
        });
        fetchData();
        return view;
    }

    private void fetchData() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchBtechClientsListDetailApiAsyncTask = asyncTaskForRequest.getBtechClientsListDetailsDisplayRequestAsyncTask();
        fetchBtechClientsListDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new BtechClientsListApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchBtechClientsListDetailApiAsyncTask.execute(fetchBtechClientsListDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareRecyclerView() {
        btechClientDetailsAdapter = new BtechClientDetailsAdapter(btechClientsModels, activity, new BtechClientDetailsAdapterOnItemClickDelegate() {
            @Override
            public void onItemClick(BtechClientsModel btechClientsModel) {
                Logger.error("item clicked");

                Intent intentMapDisplay = new Intent(activity, BtechClientDetailMapDisplayFragmentActivity.class);
                intentMapDisplay.putExtra(BundleConstants.BTECH_CLIENTS_MODEL,btechClientsModel);
                startActivityForResult(intentMapDisplay,BundleConstants.BCMD_START);

            }

            @Override
            public void onItemCallClick(BtechClientsModel btechClientsModel, int position) {
                Logger.error("call button clicked");
                cod = new ConfirmCallDialog(activity, btechClientsModel);
                cod.show();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(btechClientDetailsAdapter);
    }

    private void initUi(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_est_distance = (TextView) view.findViewById(R.id.tv_est_distance);
        tv_noof_pickup = (TextView) view.findViewById(R.id.tv_noof_pickup);
    }


    private class BtechClientsListApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Logger.error("" + json);
                ResponseParser responseParser = new ResponseParser(activity);
                BtechClientsResponseModel btechClientsResponseModel = new BtechClientsResponseModel();
                btechClientsResponseModel = responseParser.getBtechClientsResponseModel(json, statusCode);
                if (btechClientsResponseModel != null && btechClientsResponseModel.getBtechClients().size() > 0) {
                    btechClientsModels = btechClientsResponseModel.getBtechClients();
                    Logger.error("btechTechModels size " + btechClientsResponseModel.getBtechClients().size());
                    tv_noof_pickup.setText(btechClientsResponseModel.getBtechClients().size()+" Pickups");
                    int dst = 0;
                    for (BtechClientsModel btechClientsModel :
                            btechClientsResponseModel.getBtechClients()) {
                        dst = dst + btechClientsModel.getDistance();
                    }
                    tv_est_distance.setText("Est. Distance "+dst+" KM");
                    prepareRecyclerView();
                } else {
                    Logger.error("else " + json);
                }
            } else {
                Logger.error("" + json);
                Toast.makeText(activity, "" + json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BundleConstants.BCMD_START && resultCode==BundleConstants.BCMD_ARRIVED){
            BtechClientsModel btechClientsModel = data.getExtras().getParcelable(BundleConstants.BTECH_CLIENTS_MODEL);
            Intent intentOrderBooking = new Intent(activity, OlcPickupActivity.class);
            intentOrderBooking.putExtra(BundleConstants.BTECH_CLIENTS_MODEL,btechClientsModel);
            startActivity(intentOrderBooking);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
}
