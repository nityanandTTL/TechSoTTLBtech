package com.thyrocare.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.GetAvailableStockController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.models.api.response.MainMaterialModel;
import com.thyrocare.models.api.response.MaterialDetailsModel2;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class StockAvailabilityActivity extends AppCompatActivity {
    Gson gson;
    private TextView tv_srno, tv_materialName, tv_opStock, tv_clStock, tv_noResult;
    private TableLayout tablelayout;
    private TableRow tableRow;
    private Activity mActivity;
    private ConnectionDetector cd;
    private LinearLayout ll_tableView;
    AppPreferenceManager appPreferenceManager;
    String dac_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_availability);
        mActivity = StockAvailabilityActivity.this;
        setTitle("Stock Availability");
        cd = new ConnectionDetector(mActivity);
        initUI();
        appPreferenceManager = new AppPreferenceManager(mActivity);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet())
            FetchAvailableStock();
        else
            TastyToast.makeText(mActivity, "Oops! Internet connection is not available", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
    }

    private void initUI() {
        tv_noResult = (TextView) findViewById(R.id.tv_noResult);
        tablelayout = (TableLayout) findViewById(R.id.tablelayout);
        ll_tableView = (LinearLayout) findViewById(R.id.ll_tableView);
    }

    private void FetchAvailableStock() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("APIKey", AppConstants.STOCKAPIKEY);
            jsonObject.put("userCode", appPreferenceManager.getLoginResponseModel().getUserID());
//            jsonObject.put("userCode", "884544150");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ApplicationController.getAvailableStockController != null) {
            ApplicationController.getAvailableStockController = null;
        }
        ApplicationController.getAvailableStockController = new GetAvailableStockController(StockAvailabilityActivity.this, mActivity);
        ApplicationController.getAvailableStockController.getAvailableStock(jsonObject);
    }

    public void getStockResponse(JSONObject response) {
        gson = new Gson();
        MainMaterialModel mainMaterialModel = gson.fromJson(String.valueOf(response), MainMaterialModel.class);
        if (mainMaterialModel != null) {
            dac_code = mainMaterialModel.getCode();
            if (mainMaterialModel.getResponseId().equalsIgnoreCase("RES0000")) {
                if (mainMaterialModel.getMaterialDetails() != null && mainMaterialModel.getMaterialDetails().size() > 0) {
                    ll_tableView.setVisibility(View.VISIBLE);
                    tv_noResult.setVisibility(View.GONE);
                    displayData(mainMaterialModel.getMaterialDetails());
                } else {
                    ll_tableView.setVisibility(View.GONE);
                    tv_noResult.setVisibility(View.VISIBLE);
                }
            } else {
                ll_tableView.setVisibility(View.GONE);
                tv_noResult.setVisibility(View.VISIBLE);
                TastyToast.makeText(mActivity, mainMaterialModel.getResponse(), TastyToast.LENGTH_SHORT,TastyToast.ERROR);
            }
        } else {
            ll_tableView.setVisibility(View.GONE);
            tv_noResult.setVisibility(View.VISIBLE);
            TastyToast.makeText(mActivity,"Something went wrong. Please try after sometime", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }

    private void displayData(final ArrayList<MaterialDetailsModel2> materialDetailsModelArrayList) {
        tablelayout.removeAllViews();
        int srno;
        for (int i = 0; i < materialDetailsModelArrayList.size(); i++) {
            tableRow = (TableRow) LayoutInflater.from(mActivity).inflate(R.layout.material_item_view, null);

            tv_srno = (TextView) tableRow.findViewById(R.id.tv_srno);
            tv_materialName = (TextView) tableRow.findViewById(R.id.tv_materialName);
            tv_opStock = (TextView) tableRow.findViewById(R.id.tv_opStock);
            tv_clStock = (TextView) tableRow.findViewById(R.id.tv_clStock);

            srno = i + 1;
            tv_srno.setText("" + srno);
            tv_materialName.setText(materialDetailsModelArrayList.get(i).getMaterialName());
            tv_opStock.setText(materialDetailsModelArrayList.get(i).getOpeningStock());

            tv_clStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow row = (TableRow) v.getParent();
                    int index = tablelayout.indexOfChild(row);
                    MaterialDetailsModel2 materialDetailsModel = materialDetailsModelArrayList.get(index);
                    Intent intent = new Intent(StockAvailabilityActivity.this, UpdateMaterialActivity.class);
                    intent.putExtra("main_model", dac_code);
                    intent.putExtra("material_model", materialDetailsModel);
                    startActivity(intent);
                }
            });

            tablelayout.addView(tableRow);
        }
    }
}
