package com.thyrocare.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.UpdateStockController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.dao.utils.ConnectionDetector;
import com.thyrocare.models.api.response.MaterialDetailsModel2;
import com.thyrocare.utils.app.AppConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateMaterialActivity extends AppCompatActivity {
    private Activity mActivity;
    private ConnectionDetector cd;
    private EditText edt_opStock, edt_usedStock, edt_wastageStock, edt_defStock, edt_clStock;
    private Button btn_submit;
    private MaterialDetailsModel2 materialDetailsModel;
    private int opStock = 0, usedStock = 0, wastageStock = 0, defStock = 0, clStock = 0, calStock = 0;
    private boolean usedFlag = false, wastFlag = false, defFlag = false;
    String dac_code;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_material);

        mActivity = UpdateMaterialActivity.this;
        cd = new ConnectionDetector(mActivity);
        setTitle("Stock Availability");
        initUI();
        initListeners();

    }

    private void initUI() {
        edt_opStock = (EditText) findViewById(R.id.edt_opStock);
        edt_usedStock = (EditText) findViewById(R.id.edt_usedStock);
        edt_wastageStock = (EditText) findViewById(R.id.edt_wastageStock);
        edt_defStock = (EditText) findViewById(R.id.edt_defStock);
        edt_clStock = (EditText) findViewById(R.id.edt_clStock);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        dac_code = getIntent().getStringExtra("main_model");
        materialDetailsModel = getIntent().getExtras().getParcelable("material_model");
        if (materialDetailsModel != null) {
            opStock = (int) Double.parseDouble(materialDetailsModel.getOpeningStock());
        }
    }

    private void initListeners() {
        edt_usedStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = true;
                wastFlag = false;
                defFlag = false;
                if (!edt_usedStock.getText().toString().trim().isEmpty()) {
                    usedStock = (int) Double.parseDouble(edt_usedStock.getText().toString().trim());
                } else
                    usedStock = 0;


                calculateCLstock(usedStock, wastageStock, defStock, edt_usedStock);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_wastageStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = true;
                defFlag = false;
                if (!edt_wastageStock.getText().toString().trim().isEmpty()) {
                    wastageStock = Integer.parseInt(edt_wastageStock.getText().toString().trim());
                } else
                    wastageStock = 0;

                    calculateCLstock(usedStock, wastageStock, defStock, edt_wastageStock);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_defStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = false;
                defFlag = true;
                if (!edt_defStock.getText().toString().trim().isEmpty()) {
                    defStock = Integer.parseInt(edt_defStock.getText().toString().trim());
                } else
                    defStock = 0;

                    calculateCLstock(usedStock, wastageStock, defStock, edt_defStock);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_opStock.setText(materialDetailsModel.getOpeningStock());
        edt_usedStock.setText(materialDetailsModel.getUsedStock());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_usedStock.getText().toString().isEmpty() && edt_wastageStock.getText().toString().isEmpty() && edt_defStock.getText().toString().isEmpty()) {
                    TastyToast.makeText(mActivity, "Enter proper values", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                } else {
                    if (clStock > 0) {
                        if (clStock < opStock) {
                            if (usedStock == 0 && wastageStock == 0 && defStock == 0) {
                                TastyToast.makeText(mActivity, "Enter proper values", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            } else {
                                if (cd.isConnectingToInternet())
                                    UpdateAvailableStock();
                                else
                                    TastyToast.makeText(mActivity, "Oops! Internet connection is not available", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            }
                        } else {
                            TastyToast.makeText(mActivity, "Enter proper values", TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                        }
                    } else {
                        TastyToast.makeText(mActivity, "Enter proper values", TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
            }
        });
    }

    private void calculateCLstock(int usedStock, int wastageStock, int defStock, EditText edt_usedStock) {
        calStock = usedStock + wastageStock + defStock;
        Log.e("TAG", "Totalcalstock: " + calStock);
        clStock = opStock - calStock;
        Log.e("TAG", "Clstock: " + clStock);


        if (clStock < 0) {
            edt_usedStock.setText("0");
            if (usedFlag) {
                calStock = 0 + wastageStock + defStock;
            } else if (wastFlag) {
                calStock = usedStock + 0 + defStock;
            } else {
                calStock = usedStock + wastageStock + 0;
            }
            clStock = opStock - calStock;
            edt_clStock.setText("" + clStock);
        } else {
            edt_clStock.setText("" + clStock);
        }
    }

    private void UpdateAvailableStock() {
        JSONObject jsonObject = new JSONObject();
        try {
            Log.e("shami -- ", "UpdateAvailableStock: "+dac_code);
            jsonObject.put("APIKey", AppConstants.STOCKAPIKEY);
            jsonObject.put("Dac_code", dac_code);
            jsonObject.put("Material_id", materialDetailsModel.getMaterialId());
            jsonObject.put("Used_stock", "" + usedStock);
            jsonObject.put("Wastage_stock", "" + wastageStock);
            jsonObject.put("Defective_stock", "" + defStock);
            jsonObject.put("Closing_stock", edt_clStock.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (ApplicationController.updateStockController != null) {
            ApplicationController.updateStockController = null;
        }
        ApplicationController.updateStockController = new UpdateStockController(UpdateMaterialActivity.this, mActivity);
        ApplicationController.updateStockController.UpdateAvailableStock(jsonObject);
    }

    public void getUpdatedResponse(JSONObject response) {
        String Response = response.optString("Response");
        String ResponseId = response.optString("Responseid");
        if (ResponseId.equalsIgnoreCase("RES0000")) {
            TastyToast.makeText(mActivity, "Stock updated successfully",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            finish();
        } else {
            TastyToast.makeText(mActivity, Response, TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }
}