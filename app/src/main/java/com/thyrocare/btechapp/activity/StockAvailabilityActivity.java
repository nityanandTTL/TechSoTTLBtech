package com.thyrocare.btechapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.btechapp.Controller.GetAvailableStockController;
import com.thyrocare.btechapp.R;

import application.ApplicationController;

import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.models.api.request.StockAvailabilityRequestModel;
import com.thyrocare.btechapp.models.api.response.MainMaterialModel;
import com.thyrocare.btechapp.models.api.response.MaterialDetailsModel2;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockAvailabilityActivity extends AppCompatActivity {
    Gson gson;
    AppPreferenceManager appPreferenceManager;
    String dac_code;
    int oStock = 0, uStock = 0, cStock = 0;
    private TextView tv_srno, tv_materialName, tv_opStock, tv_clStock, tv_noResult;
    private TableLayout tablelayout;
    private TableRow tableRow;
    private Activity mActivity;
    private ConnectionDetector cd;
    private LinearLayout ll_tableView;

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
            TastyToast.makeText(mActivity, "Oops! Internet connection is not available", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    private void initUI() {
        tv_noResult = (TextView) findViewById(R.id.tv_noResult);
        tablelayout = (TableLayout) findViewById(R.id.tablelayout);
        ll_tableView = (LinearLayout) findViewById(R.id.ll_tableView);
    }

    private void FetchAvailableStock() {

        StockAvailabilityRequestModel model = new StockAvailabilityRequestModel();
        model.setAPIKey(AppConstants.STOCKAPIKEY);
        model.setUserCode(appPreferenceManager.getLoginResponseModel().getUserID());

        if (ApplicationController.getAvailableStockController != null) {
            ApplicationController.getAvailableStockController = null;
        }
        ApplicationController.getAvailableStockController = new GetAvailableStockController(StockAvailabilityActivity.this, mActivity);
        ApplicationController.getAvailableStockController.getAvailableStock(model);
    }

    public void getStockResponse(MainMaterialModel mainMaterialModel) {
        gson = new Gson();
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
                TastyToast.makeText(mActivity, mainMaterialModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        } else {
            ll_tableView.setVisibility(View.GONE);
            tv_noResult.setVisibility(View.VISIBLE);
            TastyToast.makeText(mActivity, "Something went wrong. Please try after sometime", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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


            tv_materialName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            tv_opStock.setText(materialDetailsModelArrayList.get(i).getOpeningStock());

            oStock = (int) Double.parseDouble(materialDetailsModelArrayList.get(i).getOpeningStock());
            uStock = (int) Double.parseDouble(materialDetailsModelArrayList.get(i).getUsedStock());

            cStock = oStock - uStock;
            tv_clStock.setText("" + cStock);

            tv_materialName.setOnClickListener(new View.OnClickListener() {
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
