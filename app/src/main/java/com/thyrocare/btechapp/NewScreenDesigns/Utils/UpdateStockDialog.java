package com.thyrocare.btechapp.NewScreenDesigns.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.thyrocare.btechapp.NewScreenDesigns.Activities.StockAvailabilityActivityNew;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.UpdateStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.MainMaterialModel;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.utils.app.Global;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;



public class UpdateStockDialog extends Dialog {
    public static String TAG = UpdateStockDialog.class.getSimpleName();
    Activity activity;
    private ConnectionDetector cd;
    Global global;
    TextView txtExpectedStock, txtClosingStock, txtActualStock;
    private EditText edtActualStock, edtWastedStock, edtDefectiveStock;
    private Button btnUpdate, btnCancel;
    private MainMaterialModel.MaterialDetailsBean materialDetailsBean;
    private MainMaterialModel mainMaterialModel;
    private int opStock = 0, usedStock = 0, wastageStock = 0, defStock = 0, clStock = 0, calStock = 0, actualStock = 0;
    private boolean usedFlag = false, wastFlag = false, defFlag = false;

    public UpdateStockDialog(Activity activity, MainMaterialModel.MaterialDetailsBean materialDetailsBean, MainMaterialModel mainMaterialModel) {
        super(activity);
        this.activity = activity;
        this.materialDetailsBean = materialDetailsBean;
        this.mainMaterialModel = mainMaterialModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_name_order_item_new);
        cd = new ConnectionDetector(activity);
        global = new Global(activity);
        initUI();
        initListeners();
    }

    private void initUI() {
        txtExpectedStock = (TextView) findViewById(R.id.txtExpectedStock);
        txtActualStock = (TextView) findViewById(R.id.txtActualStock);
        edtWastedStock = (EditText) findViewById(R.id.edtWastedStock);
        edtDefectiveStock = (EditText) findViewById(R.id.edtDefectiveStock);
        txtClosingStock = (TextView) findViewById(R.id.txtClosingStock);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        if (materialDetailsBean != null) {
            opStock = (int) Double.parseDouble(materialDetailsBean.getOpeningStock());
        }
    }

    private void initListeners() {
        /*edtActualStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = true;
                wastFlag = false;
                defFlag = false;
                if (!edtActualStock.getText().toString().trim().isEmpty()) {
                    usedStock = (int) Double.parseDouble(edtActualStock.getText().toString().trim());
                } else
                    usedStock = 0;
                calculateCLstock(usedStock, wastageStock, defStock, edtActualStock);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

        edtWastedStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = true;
                defFlag = false;
                if (!edtWastedStock.getText().toString().trim().isEmpty()) {
                    wastageStock = Integer.parseInt(edtWastedStock.getText().toString().trim());
                } else
                    wastageStock = 0;

                calculateCLstock(usedStock, wastageStock, defStock, edtWastedStock);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtDefectiveStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usedFlag = false;
                wastFlag = false;
                defFlag = true;
                if (!edtDefectiveStock.getText().toString().trim().isEmpty()) {
                    defStock = Integer.parseInt(edtDefectiveStock.getText().toString().trim());
                } else
                    defStock = 0;

                calculateCLstock(usedStock, wastageStock, defStock, edtDefectiveStock);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtExpectedStock.setText(materialDetailsBean.getExpectedStock());
        opStock = (int) Double.parseDouble(materialDetailsBean.getOpeningStock());
        usedStock = (int) Double.parseDouble(materialDetailsBean.getUsedStock());
        actualStock = opStock - usedStock;
        txtActualStock.setText(""+actualStock);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (/*edtActualStock.getText().toString().isEmpty() &&*/ edtWastedStock.getText().toString().isEmpty() && edtDefectiveStock.getText().toString().isEmpty()) {
                    global.showCustomToast(activity, activity.getResources().getString(R.string.enterProperValues), Toast.LENGTH_SHORT);
                } else {
                    if (clStock > 0) {
                        if (clStock < opStock) {
                            if (usedStock == 0 && wastageStock == 0 && defStock == 0) {
                                global.showCustomToast(activity, activity.getResources().getString(R.string.enterProperValues), Toast.LENGTH_SHORT);
                            } else {
                                if (cd.isConnectingToInternet())
                                    updateAvailableStock();
                                else
                                    global.showCustomToast(activity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
                            }
                        } else {
                            global.showCustomToast(activity, activity.getResources().getString(R.string.enterProperValues), Toast.LENGTH_SHORT);
                        }
                    } else {
                        global.showCustomToast(activity, activity.getResources().getString(R.string.enterProperValues), Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void calculateCLstock(int usedStock, int wastageStock, int defStock, EditText edt_usedStock) {
        calStock = usedStock + wastageStock + defStock;
        MessageLogger.LogError("TAG", "Totalcalstock: " + calStock);
        clStock = opStock - calStock;
        MessageLogger.LogError("TAG", "Clstock: " + clStock);

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
            txtClosingStock.setText("" + clStock);
        } else {
            txtClosingStock.setText("" + clStock);
        }
    }

    private void updateAvailableStock() {
        final UpdateStockModel updateStockModel = new UpdateStockModel();
        updateStockModel.setAPIKey(activity.getResources().getString(R.string.stockAPIKey));
        updateStockModel.setDac_code(mainMaterialModel.getCode());
        updateStockModel.setMaterial_id(materialDetailsBean.getMaterialId());
        updateStockModel.setUsed_stock(String.valueOf(usedStock));
        updateStockModel.setWastage_stock(String.valueOf(wastageStock));
        updateStockModel.setDefective_stock(String.valueOf(defStock));
        updateStockModel.setClosing_stock(txtClosingStock.getText().toString().trim());
        try {
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
            Call<CommonResponseModel> commonResponseModelCall = postAPIInterface.updateStock(updateStockModel);
            global = new Global(activity);
            global.showProgressDialog(activity, activity.getResources().getString(R.string.updatingStock), false);
            commonResponseModelCall.enqueue(new Callback<CommonResponseModel>() {
                @Override
                public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                    global.hideProgressDialog();
                    CommonResponseModel commonResponseModel = response.body();
                    getStockUpdateResponse(commonResponseModel);
                }

                @Override
                public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                    global.hideProgressDialog();
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                    MessageLogger.LogDebug(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MessageLogger.LogError(TAG, "postAvailableStock: " + e);
        }
    }

    private void getStockUpdateResponse(CommonResponseModel commonResponseModel) {
        if (commonResponseModel != null) {
            if (commonResponseModel.getResponse() != null) {
                if (commonResponseModel.getResponse().equalsIgnoreCase("Entry done successfully")) {
                    global.showCustomToast(activity, activity.getResources().getString(R.string.stockUpdated), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(activity, StockAvailabilityActivityNew.class);
                    activity.startActivity(intent);
                    activity.finish();
                    dismiss();
                } else {
                    global.showCustomToast(activity, commonResponseModel.getResponse(), Toast.LENGTH_SHORT);
                }
            } else {
                MessageLogger.LogError(TAG, "onSuccess: ERROR" + commonResponseModel.getResponse());
                global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
            }
        } else {
            global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
        }
    }

}