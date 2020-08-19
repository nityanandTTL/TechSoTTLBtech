package com.thyrocare.btechapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.OLCPickupActivity;
import com.thyrocare.btechapp.activity.OLCPickupDetailMapDisplayFragmentActivity;
import com.thyrocare.btechapp.adapter.BtechClientDetailsAdapter;
import com.thyrocare.btechapp.delegate.BtechClientDetailsAdapterOnItemClickDelegate;
import com.thyrocare.btechapp.models.api.response.BtechClientsResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderServedResponseModel;
import com.thyrocare.btechapp.models.data.BtechClientsModel;



import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

/**
 * for getting olc list<br/>
 *  http://bts.dxscloud.com/btsapi/api/BtechClients/884543107<br/>
 */

public class OLCPickupListDisplayFragment extends AbstractFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private TextView tv_noof_pickup, tv_est_distance;
    private HomeScreenActivity activity;
    public static final String TAG_FRAGMENT = OLCPickupListDisplayFragment.class.getSimpleName();
    ArrayList<BtechClientsModel> btechClientsModels = new ArrayList<>();
    BtechClientDetailsAdapter btechClientDetailsAdapter;
    private Global global;

    public OLCPickupListDisplayFragment() {
    }

    public static OLCPickupListDisplayFragment newInstance() {
        OLCPickupListDisplayFragment fragment = new OLCPickupListDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btech_client_list, container, false);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("OLC Pickup");
        activity.isOnHome = false;
        global = new Global(activity);
        initUI(view);
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
        if (isNetworkAvailable(activity)) {
            CallGetBtechClientsListDetailsDisplayApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareRecyclerView() {
        btechClientDetailsAdapter = new BtechClientDetailsAdapter(btechClientsModels, activity, new BtechClientDetailsAdapterOnItemClickDelegate() {
            @Override
            public void onItemClick(BtechClientsModel btechClientsModel) {
                Logger.error("item clicked");

                Intent intentMapDisplay = new Intent(activity, OLCPickupDetailMapDisplayFragmentActivity.class);
                intentMapDisplay.putExtra(BundleConstants.BTECH_CLIENTS_MODEL,btechClientsModel);
                startActivityForResult(intentMapDisplay,BundleConstants.BCMD_START);

            }

            @Override
            public void onItemCallClick(BtechClientsModel btechClientsModel, int position) {
                Logger.error("call button clicked");


                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + btechClientsModel.getMobile()));
                activity.startActivity(intent);

               /* cod = new ConfirmCallDialog(activity, btechClientsModel);
                cod.show();*/
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(btechClientDetailsAdapter);
    }

    private void initUI(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_est_distance = (TextView) view.findViewById(R.id.tv_est_distance);
        tv_noof_pickup = (TextView) view.findViewById(R.id.tv_noof_pickup);
    }

    private void CallGetBtechClientsListDetailsDisplayApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<BtechClientsResponseModel> responseCall = apiInterface.CallGetBtechClientsListDetailsDisplayApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<BtechClientsResponseModel>() {
            @Override
            public void onResponse(Call<BtechClientsResponseModel> call, retrofit2.Response<BtechClientsResponseModel> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    BtechClientsResponseModel btechClientsResponseModel = response.body();
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
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BtechClientsResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BundleConstants.BCMD_START && resultCode==BundleConstants.BCMD_ARRIVED){
            BtechClientsModel btechClientsModel = data.getExtras().getParcelable(BundleConstants.BTECH_CLIENTS_MODEL);
            Intent intentOrderBooking = new Intent(activity, OLCPickupActivity.class);
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
