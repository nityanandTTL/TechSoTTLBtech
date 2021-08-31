package com.thyrocare.btechapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;


public class HubListDisplayFragment extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_view;
    LinearLayout ll_hub_display_footer;
    TextView tv_collection_sample,tv_toolbar;
    ImageView iv_back,iv_home;
    Activity activity;
    ArrayList<HUBBTechModel> hubbTechModels = new ArrayList<>();
    DispatchToHubDisplayDetailsAdapter dispatchToHubDisplayDetailsAdapter;
    AppPreferenceManager appPreferenceManager;
    public static final String TAG_FRAGMENT = HubListDisplayFragment.class.getSimpleName();
    public static int flowDecider = 0;//for btech_hub flow
    private Global global;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_btech_collections_list);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        global = new Global(activity);
        initUI();
        listeners();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                prepareRecyclerView();
            }
        });
        fetchData();
    }

    private void listeners() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            Intent intent = new Intent(activity,HubListDisplayFragment.class);
            intent.putExtra("hubtech",hubbTechModel);
            startActivity(intent);
//            pushFragments(HubMasterBarcodeScanFragment.newInstance(hubbTechModel), false, false, HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
        }
    }

    private void initUI() {
        tv_toolbar = findViewById(R.id.tv_toolbar);
        iv_back = findViewById(R.id.iv_back);
        iv_home = findViewById(R.id.iv_home);
        tv_toolbar.setText("Hub List");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        ll_hub_display_footer = (LinearLayout) findViewById(R.id.ll_hub_display_footer);
        tv_collection_sample = findViewById(R.id.tv_collection_sample);
        tv_collection_sample.setVisibility(View.GONE);
        ll_hub_display_footer.setVisibility(View.GONE);

    }

    private void CallGetDispatchHubDetailsDisplayApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
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
