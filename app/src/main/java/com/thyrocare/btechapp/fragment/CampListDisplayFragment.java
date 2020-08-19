package com.thyrocare.btechapp.fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.CampOrderBookingActivity;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.adapter.CampListDetailDisplayAdapter;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.dao.models.BeneficiaryDetailsDao;
import com.thyrocare.btechapp.dao.models.OrderDetailsDao;
import com.thyrocare.btechapp.delegate.CampListDisplayRecyclerViewAdapterDelegate;
import com.thyrocare.btechapp.dialog.ConfirmOrderReleaseDialog;
import com.thyrocare.btechapp.models.api.request.CampStartedRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.response.BtechClientsResponseModel;
import com.thyrocare.btechapp.models.api.response.CampListDisplayResponseModel;
import com.thyrocare.btechapp.models.api.response.CampScanQRResponseModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.BtechClientsModel;
import com.thyrocare.btechapp.models.data.CampAllOrderDetailsModel;
import com.thyrocare.btechapp.models.data.CampBtechModel;
import com.thyrocare.btechapp.models.data.CampDetailModel;



import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

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
    private Global global;

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
        global = new Global(activity);
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
            MessageLogger.PrintMsg(date.toString());
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
        if (isNetworkAvailable(activity)) {
            CallGetCampListDetailsDisplayApi();
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
            MessageLogger.LogError(TAG_FRAGMENT, "prepareRecyclerView:vtech size " + campDetailModels.size());
            campListDetailDisplayAdapter = new CampListDetailDisplayAdapter(activity, campDetailModels/*,campDetailsResponseModel,*/, new CampListDisplayRecyclerViewAdapterDelegateResult());
            recyclerView.setAdapter(campListDetailDisplayAdapter);
            txtNoRecord.setVisibility(View.GONE);
        } else {
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private void CallGetCampListDetailsDisplayApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<CampListDisplayResponseModel> responseCall = apiInterface.CallGetCampListDetailsDisplayApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Please wait..");
        responseCall.enqueue(new Callback<CampListDisplayResponseModel>() {
            @Override
            public void onResponse(Call<CampListDisplayResponseModel> call, retrofit2.Response<CampListDisplayResponseModel> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    campListDisplayResponseModel = new CampListDisplayResponseModel();
                    campListDisplayResponseModel = response.body();
                    if (campListDisplayResponseModel != null/* && campDetailsResponseModel.getBtechs().size() > 0*/) {
                        campDetailModels = campListDisplayResponseModel.getCampDetail();
                        prepareRecyclerView();
                    }
                } else {
                    Toast.makeText(activity, SomethingWentwrngMsg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CampListDisplayResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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
        CampStartedRequestModel campStartedRequestModel = new CampStartedRequestModel();
        campStartedRequestModel.setBtechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
        campStartedRequestModel.setCampId(campDetailModel.getId());
        campStartedRequestModel.setType(i);
        if (i == 0) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + campDetailModel.getLeaderContactNo()));
            activity.startActivity(intent);
        }else{
            if (isNetworkAvailable(activity)) {
                callCampStartRequestApi(campStartedRequestModel,campDetailModel);
            } else {
                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void callCampStartRequestApi(final CampStartedRequestModel campStartedRequestModel, final CampDetailModel campDetailModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.callCampStartRequestApi(campStartedRequestModel);
        global.showProgressDialog(activity, ConstantsMessages.PLEASE_WAIT);

        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                global.hideProgressDialog();
                if (response.isSuccessful()) {
                    if (campStartedRequestModel.getType() == 7){
                        Toast.makeText(activity, "" + response.body(), Toast.LENGTH_SHORT).show();
                        Logger.error("" + position);
                        campDetailModels.get(position).setStarted(true);
                        campListDetailDisplayAdapter.notifyDataSetChanged();
                    }else if (campStartedRequestModel.getType() == 8){
                        Toast.makeText(activity, "" + response.body(), Toast.LENGTH_SHORT).show();
                    }else if (campStartedRequestModel.getType() == 3){
                        Intent intentOrderBooking = new Intent(activity, CampOrderBookingActivity.class);
                        Logger.error("campDetailModels after arrived "+campDetailModels.size());
                        intentOrderBooking.putExtra(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
                        startActivity(intentOrderBooking);
                    }
                } else {
                    Toast.makeText(activity, ConstantsMessages.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog();
                MessageLogger.LogDebug("Errror", t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(activity, "scanned res " + scanningResult, Toast.LENGTH_SHORT).show();
        if (requestCode == BundleConstants.CMD_START && resultCode == BundleConstants.CMD_ARRIVED) {

        }
        if (scanningResult != null && scanningResult.getContents() != null) {
            if (scanningResult.getContents().startsWith("0")|| scanningResult.getContents().startsWith("$")){
                Toast.makeText(activity, "Invalid Barcode", Toast.LENGTH_SHORT).show();
            }else {
                //  String scanned_barcode = scanningResult.getContents();
                Logger.error("" + scanningResult);
                Logger.error("scanned_barcode " + scanningResult.getContents());
                Toast.makeText(activity, "" + scanningResult, Toast.LENGTH_SHORT).show();
                callsendQRCodeApi(scanningResult.getContents());
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callsendQRCodeApi(String contents) {
        if (isNetworkAvailable(activity)) {
            CallGetSendQRCodeApi(contents);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private void CallGetSendQRCodeApi(String contents) {

            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<CampScanQRResponseModel> responseCall = apiInterface.CallgetSendQRCodeRequestAPI(contents);
            global.showProgressDialog(activity, "Fetching products. Please wait..");
            responseCall.enqueue(new Callback<CampScanQRResponseModel>() {
                @Override
                public void onResponse(Call<CampScanQRResponseModel> call, retrofit2.Response<CampScanQRResponseModel> response) {
                    global.hideProgressDialog();

                    if (response.isSuccessful() && response.body() != null) {

                        CampScanQRResponseModel campScanQRResponseModel = response.body();

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
                        Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CampScanQRResponseModel> call, Throwable t) {
                    global.hideProgressDialog();
                    global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });
    }

}
