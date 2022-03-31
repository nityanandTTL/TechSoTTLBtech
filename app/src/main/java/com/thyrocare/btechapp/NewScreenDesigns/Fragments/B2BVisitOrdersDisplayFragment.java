package com.thyrocare.btechapp.NewScreenDesigns.Fragments;


import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.GetOrderDetailsController;
import com.thyrocare.btechapp.Controller.SendLatLongforOrderController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StartAndArriveActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.All_VisitDisplayAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.Btech_VisitDisplayAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.delegate.ConfirmOrderReleaseDialogButtonClickedDelegate;
import com.thyrocare.btechapp.delegate.refreshDelegate;
import com.thyrocare.btechapp.dialog.ConfirmOrderPassDialog;
import com.thyrocare.btechapp.dialog.ConfirmOrderReleaseDialog;
import com.thyrocare.btechapp.dialog.ConfirmRequestReleaseDialog;
import com.thyrocare.btechapp.dialog.RescheduleOrderDialog;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.SetDispositionDataModel;
import com.thyrocare.btechapp.models.api.response.BtechEstEarningsResponseModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.BeneficiaryDetailsModel;
import com.thyrocare.btechapp.models.data.DispositionDataModel;
import com.thyrocare.btechapp.models.data.DispositionDetailsModel;
import com.thyrocare.btechapp.models.data.KitsCountModel;
import com.thyrocare.btechapp.models.data.OrderDetailsModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.service.TrackerService;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.GPSTracker;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class B2BVisitOrdersDisplayFragment extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "VISIT_ORDERS_FRAGMENT";

    private AppPreferenceManager appPreferenceManager;
    private RecyclerView recyOrderList;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_RoutineOrders = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_AayushmanOrders = new ArrayList<>();
    private TextView txtNoRecord;

    private Global global;
    TextView tv_toolbar;
    Activity activity;
    ImageView iv_home, iv_back;
    LinearLayout ll_tab;
    private ConnectionDetector connectionDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_visitb2b_orders_display);

        activity = this;

        connectionDetector = new ConnectionDetector(activity);
        global = new Global(activity);

        appPreferenceManager = new AppPreferenceManager(activity);
        BundleConstants.isKIOSKOrder = false;

        initUI();
        setListener();
        CallAPIForPendingOrders();

    }


    public void initUI() {

        recyOrderList = (RecyclerView) findViewById(R.id.recyOrderList);
        txtNoRecord = (TextView) findViewById(R.id.txt_no_orders);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        ll_tab = findViewById(R.id.ll_tab);

        iv_home = findViewById(R.id.iv_home);
        iv_back = findViewById(R.id.iv_back);
        tv_toolbar.setText("Visit Orders");

    }

    private void setListener() {


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void CallAPIForPendingOrders() {
        GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<GetOrderDetailsResponseModel> fetchOrderDetailsResponseModelCall = getAPIInterface.getpendingOrderDetails(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, activity.getResources().getString(R.string.fetchingOrders), false);
        fetchOrderDetailsResponseModelCall.enqueue(new Callback<GetOrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<GetOrderDetailsResponseModel> call, Response<GetOrderDetailsResponseModel> response) {
                global.hideProgressDialog(activity);
                try {
                    if (response.isSuccessful()) {
                        setDataOnUI(response.body());
                    } else {
                        global.hideProgressDialog(activity);
                    }
                } catch (Exception e) {
                    global.hideProgressDialog(activity);
                    e.printStackTrace();
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<GetOrderDetailsResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
        });
    }

    private void setDataOnUI(GetOrderDetailsResponseModel allOrders) {
        if (allOrders.getGetVisitcount() != null && allOrders.getGetVisitcount().size() > 0) {
            All_VisitDisplayAdapter btech_VisitDisplayAdapter = new All_VisitDisplayAdapter(this, allOrders, activity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyOrderList.setLayoutManager(mLayoutManager);
            recyOrderList.setAdapter(btech_VisitDisplayAdapter);
        } else {
            recyOrderList.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    public void orderSelected(int pos) {
        Intent intent = new Intent(activity, VisitOrdersDisplayFragment_new.class);
        intent.putExtra(BundleConstants.POSITION, pos);
        startActivity(intent);
    }
}
