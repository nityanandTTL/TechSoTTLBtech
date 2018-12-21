package com.thyrocare.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.HubDetailMapDisplayFragmentActivity;
import com.thyrocare.adapter.DispatchToHubDisplayDetailsAdapter;
import com.thyrocare.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.thyrocare.models.api.response.DispatchHubDisplayDetailsResponseModel;
import com.thyrocare.models.data.HUBBTechModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.BundleConstants;

import org.json.JSONException;

import java.util.ArrayList;
/**
 *　APi Used BtechHubs/userid
 * Created by Orion on 7/4/2017.
 */


public class HubListDisplayFragment extends AbstractFragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_view;
    LinearLayout ll_hub_display_footer;
    HomeScreenActivity activity;
    ArrayList<HUBBTechModel> hubbTechModels = new ArrayList<>();
    DispatchToHubDisplayDetailsAdapter dispatchToHubDisplayDetailsAdapter;
    public static final String TAG_FRAGMENT = HubListDisplayFragment.class.getSimpleName();

    public static int flowDecider = 0;//for btech_hub flow

    public HubListDisplayFragment() {
    }

    public static HubListDisplayFragment newInstance() {
        return new HubListDisplayFragment();
    }

    public static Fragment newInstance(int i) {//for btech_hub flow
        flowDecider = i;
        return new HubListDisplayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_btech_collections_list, container, false);
        initUI(rootview);
        activity = (HomeScreenActivity) getActivity();
        try {
            if(activity.toolbarHome != null) {
                activity.toolbarHome.setTitle("Hub List");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.isOnHome = false;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                prepareRecyclerView();
            }
        });
        fetchData();
        return rootview;
    }

    private void fetchData() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchHubListDetailApiAsyncTask = asyncTaskForRequest.getDispatchHubDetailsDisplayRequestAsyncTask();
        fetchHubListDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new DispatchToHubDetailDisplayApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchHubListDetailApiAsyncTask.execute(fetchHubListDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareRecyclerView() {
        dispatchToHubDisplayDetailsAdapter = new DispatchToHubDisplayDetailsAdapter(hubbTechModels, activity, new DispatchToHubAdapterOnItemClickedDelegate() {
            @Override
            public void onItemClicked(HUBBTechModel hubbTechModel) {
                Logger.error("item clicked");
                Intent intentMapDisplay = new Intent(activity, HubDetailMapDisplayFragmentActivity.class);
                intentMapDisplay.putExtra(BundleConstants.HUB_BTECH_MODEL, hubbTechModel);
                startActivityForResult(intentMapDisplay, BundleConstants.HMD_START);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(dispatchToHubDisplayDetailsAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleConstants.HMD_START && resultCode == BundleConstants.HMD_ARRIVED) {
            HUBBTechModel hubbTechModel = data.getExtras().getParcelable(BundleConstants.HUB_BTECH_MODEL);
            pushFragments(HubMasterBarcodeScanFragment.newInstance(hubbTechModel), false, false, HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
        }
    }

    private void initUI(View rootview) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        ll_hub_display_footer = (LinearLayout) rootview.findViewById(R.id.ll_hub_display_footer);
        ll_hub_display_footer.setVisibility(View.GONE);
    }

    private class DispatchToHubDetailDisplayApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                DispatchHubDisplayDetailsResponseModel dispatchHubDisplayDetailsResponseModel = new DispatchHubDisplayDetailsResponseModel();
                dispatchHubDisplayDetailsResponseModel = responseParser.getDispatchTohubDetailsResponseModel(json, statusCode);
                if (dispatchHubDisplayDetailsResponseModel != null && dispatchHubDisplayDetailsResponseModel.getHubMaster().size() > 0) {
                    hubbTechModels = dispatchHubDisplayDetailsResponseModel.getHubMaster();
                    Logger.error("hubbTechModels size " + dispatchHubDisplayDetailsResponseModel.getHubMaster().size());
                    prepareRecyclerView();
                } else {
                    Logger.error("else " + json);
                }
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
