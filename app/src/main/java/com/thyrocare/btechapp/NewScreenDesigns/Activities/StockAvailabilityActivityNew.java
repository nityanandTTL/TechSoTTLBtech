package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.StockAvailabilityAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.AvailableStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.MainMaterialModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants.StockAvailability_API_KEY;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;


public class StockAvailabilityActivityNew extends AppCompatActivity {

    public static String TAG = StockAvailabilityActivityNew.class.getSimpleName();
    Global global;
    RecyclerView recyclerView;
    StockAvailabilityAdapter stockAvailabilityAdapter;
    private TextView tv_noResult;
    private Activity mActivity;
    private ConnectionDetector connectionDetector;
    private LinearLayout ll_tableView;
    private AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_availability_new);
        mActivity = StockAvailabilityActivityNew.this;
        global = new Global(mActivity);
        setTitle("Stock Availability");
        connectionDetector = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);
        fetchAvailableStock();
        initUI();
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStockAvailablity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {
        tv_noResult = (TextView) findViewById(R.id.tv_noResult);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ll_tableView = (LinearLayout) findViewById(R.id.ll_tableView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void fetchAvailableStock() {
        AvailableStockModel availableStockModel = new AvailableStockModel();
        availableStockModel.setAPIKey(StockAvailability_API_KEY);
        availableStockModel.setUserCode(appPreferenceManager.getLoginResponseModel().getUserID());

        if (connectionDetector.isConnectingToInternet()) {
            postAvailableStock(availableStockModel);
        } else {
            global.showCustomToast(mActivity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }

    }

    private void postAvailableStock(AvailableStockModel availableStockModel) {
        try {
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
            Call<MainMaterialModel> mainMaterialModelCall = postAPIInterface.getAvailableStock(availableStockModel);
            global.showProgressDialog(mActivity, mActivity.getResources().getString(R.string.loading), false);
            mainMaterialModelCall.enqueue(new Callback<MainMaterialModel>() {
                @Override
                public void onResponse(Call<MainMaterialModel> call, Response<MainMaterialModel> response) {
                    global.hideProgressDialog(mActivity);
                    MainMaterialModel mainMaterialModel = response.body();
                    getAvailableStock(mainMaterialModel);
                }

                @Override
                public void onFailure(Call<MainMaterialModel> call, Throwable t) {
                    global.hideProgressDialog(mActivity);
                    global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                    MessageLogger.LogDebug(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MessageLogger.LogError(TAG, "postAvailableStock: " + e);
        }
    }

    public void getAvailableStock(MainMaterialModel mainMaterialModel) {
        if (mainMaterialModel != null && mainMaterialModel.getResponse() != null) {
            if (mainMaterialModel.getResponse().equalsIgnoreCase("SUCCESS")) {
                ll_tableView.setVisibility(View.VISIBLE);
                tv_noResult.setVisibility(View.GONE);
                stockAvailabilityAdapter = new StockAvailabilityAdapter(mActivity, mainMaterialModel);
                recyclerView.setAdapter(stockAvailabilityAdapter);
            } else if (mainMaterialModel.getResponse().equalsIgnoreCase("DATA NOT FOUND")) {
                tv_noResult.setVisibility(View.VISIBLE);
                ll_tableView.setVisibility(View.GONE);
                global.showCustomToast(mActivity, mainMaterialModel.getResponse(), Toast.LENGTH_SHORT);
            } else {
                global.showCustomToast(mActivity, mainMaterialModel.getResponse(), Toast.LENGTH_SHORT);
            }
        } else {
            global.showCustomToast(mActivity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
        }
    }
}
