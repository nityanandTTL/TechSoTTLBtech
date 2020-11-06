package com.thyrocare.btechapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.HubDetailMapDisplayFragmentActivity;
import com.thyrocare.btechapp.adapter.DispatchToHubDisplayDetailsAdapter;
import com.thyrocare.btechapp.delegate.DispatchToHubAdapterOnItemClickedDelegate;
import com.thyrocare.btechapp.models.api.response.DispatchHubDisplayDetailsResponseModel;
import com.thyrocare.btechapp.models.data.HUBBTechModel;


import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 *　APi Used BtechHubs/userid
 * Created by Orion on 7/4/2017.
 * API: http://techso.thyrocare.cloud/techsoapi/BtechHubs
 * Usage :
 */


public class HubListDisplayFragment extends AbstractFragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_view;
    LinearLayout ll_hub_display_footer;
    HomeScreenActivity activity;
    ArrayList<HUBBTechModel> hubbTechModels = new ArrayList<>();
    DispatchToHubDisplayDetailsAdapter dispatchToHubDisplayDetailsAdapter;
    AppPreferenceManager appPreferenceManager;
    public static final String TAG_FRAGMENT = HubListDisplayFragment.class.getSimpleName();

    public static int flowDecider = 0;//for btech_hub flow
    private Global global;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Hub List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_btech_collections_list, container, false);
        initUI(rootview);
        activity = (HomeScreenActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        global = new Global(activity);
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
        if (isNetworkAvailable(activity)) {
            CallGetDispatchHubDetailsDisplayApi();
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
                intentMapDisplay.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

    private void CallGetDispatchHubDetailsDisplayApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<DispatchHubDisplayDetailsResponseModel> responseCall = apiInterface.CallGetDispatchHubDetailsDisplayApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<DispatchHubDisplayDetailsResponseModel>() {
            @Override
            public void onResponse(Call<DispatchHubDisplayDetailsResponseModel> call, retrofit2.Response<DispatchHubDisplayDetailsResponseModel> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    DispatchHubDisplayDetailsResponseModel dispatchHubDisplayDetailsResponseModel = response.body();
                    if (dispatchHubDisplayDetailsResponseModel != null && dispatchHubDisplayDetailsResponseModel.getHubMaster()  != null && dispatchHubDisplayDetailsResponseModel.getHubMaster().size() > 0) {
                        hubbTechModels = dispatchHubDisplayDetailsResponseModel.getHubMaster();
                        Logger.error("hubbTechModels size " + dispatchHubDisplayDetailsResponseModel.getHubMaster().size());
                        prepareRecyclerView();
                    } else {
                        Logger.error("else " + SomethingWentwrngMsg);
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DispatchHubDisplayDetailsResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }
}
