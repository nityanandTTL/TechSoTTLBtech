package com.thyrocare.btechapp.NewScreenDesigns.Activities;


import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.thyrocare.btechapp.NewScreenDesigns.Adapters.All_VisitDisplayAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.TSP_OrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class Tsp_B2BVisitOrdersDisplayActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "VISIT_ORDERS_FRAGMENT";
    TextView tv_toolbar;
    Activity activity;
    ImageView iv_home, iv_back;
    LinearLayout ll_tab;
    private AppPreferenceManager appPreferenceManager;
    private RecyclerView recyOrderList;
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_RoutineOrders = new ArrayList<>();
    private ArrayList<OrderVisitDetailsModel> orderDetailsResponseModels_AayushmanOrders = new ArrayList<>();
    private TextView txtNoRecord;
    private Global global;
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
        Constants.clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());


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
                Intent intent = new Intent(activity, HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
            //setCleverTapEventForAllOrders(allOrders);
            All_VisitDisplayAdapter btech_VisitDisplayAdapter = new All_VisitDisplayAdapter(this, allOrders, activity);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyOrderList.setLayoutManager(mLayoutManager);
            recyOrderList.setAdapter(btech_VisitDisplayAdapter);
        } else {
            recyOrderList.setVisibility(View.GONE);
            txtNoRecord.setVisibility(View.VISIBLE);
        }
    }

    private void setCleverTapEventForAllOrders(GetOrderDetailsResponseModel allOrders) {
        for (int i = 0; i < allOrders.getGetVisitcount().size(); i++) {
            HashMap<String, Object> PeAllOrderEventMap = new HashMap<>();
            PeAllOrderEventMap.put("Order ID", allOrders.getGetVisitcount().get(i).getVisitId());
            PeAllOrderEventMap.put("Phlebo Phone No", appPreferenceManager.getLoginResponseModel().getMobile());
            PeAllOrderEventMap.put("Order date and slot time", allOrders.getGetVisitcount().get(i).getAppointmentDate());
            PeAllOrderEventMap.put("timestamp of event trigger", Global.getCurrentDateandTime());
            PeAllOrderEventMap.put("order status", allOrders.getGetVisitcount().get(i).getStatus());
            Constants.clevertapDefaultInstance.pushEvent("order_summary_order_slot", PeAllOrderEventMap);
        }
    }

    public void orderSelected() {
        Intent intent = new Intent(activity, TSP_OrdersDisplayFragment_new.class);
        startActivity(intent);
    }
}
