package com.thyrocare.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.customview.CustomOKDialog;
import com.thyrocare.delegate.CustomOkDialogOkButtonOnClickedDelegate;
import com.thyrocare.models.api.request.UpdateMaterial;
import com.thyrocare.models.api.response.MaterialBtechStockResponseModel;
import com.thyrocare.models.data.BTStockMaterialsModel;
import com.thyrocare.models.data.MaterialsStocksModel_new;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialFragment_new extends AbstractFragment {


    public static final String TAG_FRAGMENT = "MATERIAL_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private MaterialBtechStockResponseModel materialINVResponseModel;
    TableLayout materialtable;
    Dialog dialog_edt;
    static MaterialFragment_new fragment;
    ArrayList<MaterialsStocksModel_new> stockModelsArr;
    Button update;
    CustomOKDialog cdd;


    public MaterialFragment_new() {
        // Required empty public constructor
    }

    public static MaterialFragment_new newInstance() {
        fragment = new MaterialFragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        try {
            if (activity.toolbarHome != null) {
                activity.toolbarHome.setTitle("Virtual Stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragement_material_new, container, false);
        stockModelsArr = new ArrayList<>();
        initUI();
        setListners();
        fetchMaterialsINV();
        return rootView;


    }

    @Override
    public void initUI() {
        super.initUI();
        materialtable = (TableLayout) rootView.findViewById(R.id.materialtable);
        update = (Button) rootView.findViewById(R.id.updateRecord);
    }

    private void setListners() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stockModelsArr != null) {
                    if (stockModelsArr.size() != 0) {
                        cdd = new CustomOKDialog(activity, new CustomOkDialogOkButtonOnClickedDelegate() {
                            @Override
                            public void onClicked(String remarks) {
                                UpdateMaterial m = new UpdateMaterial();
                                m.allMaterialStock = new ArrayList<>();
                                UpdateMaterial.AllMaterialStock ms;
                                m.BtechId = "" + appPreferenceManager.getLoginResponseModel().getUserID();

                                for (int i = 0; i < stockModelsArr.size(); i++) {
                                    ms = new UpdateMaterial.AllMaterialStock();
                                    ms.materialID = "" + stockModelsArr.get(i).getMaterialID();
                                    ms.virtualStock = "" + stockModelsArr.get(i).getVirtualStock();
                                    ms.actualStock = "" + stockModelsArr.get(i).getActualStock();
                                    ms.remarks = "" + remarks;
                                    m.allMaterialStock.add(ms);
                                }

                                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                                ApiCallAsyncTask setMaterialsDetailApiAsyncTask = asyncTaskForRequest.getPostStockMaterialOrderAsyncTask(m);
                                setMaterialsDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setMaterialOrderDetailsApiAsyncTaskDelegateResult());
                                if (isNetworkAvailable(activity)) {
                                    setMaterialsDetailApiAsyncTask.execute(setMaterialsDetailApiAsyncTask);
                                } else {
                                    Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
                                }
                            }
                        });
                        cdd.show();
                        cdd.setCancelable(false);
                    }else {
                        TastyToast.makeText(activity, "No data to update", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    }
                }else {
                    TastyToast.makeText(activity, "No data to update", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                }
            }
        });
    }


    private void fetchMaterialsINV() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialINVApiAsyncTask = asyncTaskForRequest.getBtechVirtualStoack_DETAILRequestAsyncTask();
        fetchMaterialINVApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchMaterialsINVApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchMaterialINVApiAsyncTask.execute(fetchMaterialINVApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchMaterialsINVApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                MaterialBtechStockResponseModel materialINVDetailsResponseModel = new MaterialBtechStockResponseModel();

                materialINVDetailsResponseModel = responseParser.getMaterialStockDetailsResponseModel(json, statusCode);
                materialINVResponseModel = materialINVDetailsResponseModel;
                initData();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {

        if (materialINVResponseModel != null && materialINVResponseModel.getAllMaterialStock() != null && materialINVResponseModel.getAllMaterialStock().size() > 0) {
            TableRow trmH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_materials_new, null);
            materialtable.addView(trmH);
            for (final BTStockMaterialsModel btMaterialsModel :
                    materialINVResponseModel.getAllMaterialStock()) {
                TableRow trm = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_material_layout_new, null);
                TextView item = (TextView) trm.findViewById(R.id.txt_item);
                EditText virtualstock = (EditText) trm.findViewById(R.id.txt_virtualstock);
                final EditText actual = (EditText) trm.findViewById(R.id.edit_actual);

                item.setText(btMaterialsModel.getMaterialName() + "");
                virtualstock.setText(btMaterialsModel.getVirtualStock() + "");
                actual.setText(btMaterialsModel.getActualStock() + "");
                actual.setTag(btMaterialsModel);
                virtualstock.setTag(btMaterialsModel);
                MaterialsStocksModel_new materialsStocksModel = new MaterialsStocksModel_new();
                materialsStocksModel.setMaterialID(btMaterialsModel.getMaterialID());
                materialsStocksModel.setActualStock(btMaterialsModel.getActualStock());
                materialsStocksModel.setVirtualStock(btMaterialsModel.getVirtualStock());
                stockModelsArr.add(materialsStocksModel);
                actual.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!InputUtils.isNull(s.toString())) {
//                            if (Integer.parseInt(s.toString()) <= btMaterialsModel.getVirtualStock()) {
                            MaterialsStocksModel_new materialsStocksModel = new MaterialsStocksModel_new();
                            materialsStocksModel.setMaterialID(btMaterialsModel.getMaterialID());
                            materialsStocksModel.setVirtualStock(btMaterialsModel.getVirtualStock());
                            materialsStocksModel.setActualStock(Integer.parseInt(s.toString()));
                            if (stockModelsArr.contains(materialsStocksModel)) {
                                stockModelsArr.remove(materialsStocksModel);
                                stockModelsArr.add(materialsStocksModel);
                            } else {
                                stockModelsArr.add(materialsStocksModel);
                            }
                            /*} else {
                                try {
                                    actual.setText("" + btMaterialsModel.getActualStock());
                                    Toast.makeText(activity, "Actual Stock cannot be greater than Virtual Stock", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    }
                });


                virtualstock.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!InputUtils.isNull(s.toString())) {
//                            if (Integer.parseInt(s.toString()) <= btMaterialsModel.getVirtualStock()) {
                            MaterialsStocksModel_new materialsStocksModel = new MaterialsStocksModel_new();
                            materialsStocksModel.setMaterialID(btMaterialsModel.getMaterialID());
                            materialsStocksModel.setVirtualStock(Integer.parseInt(s.toString()));
                            materialsStocksModel.setActualStock(btMaterialsModel.getActualStock());
                            if (stockModelsArr.contains(materialsStocksModel)) {
                                stockModelsArr.remove(materialsStocksModel);
                                stockModelsArr.add(materialsStocksModel);
                            } else {
                                stockModelsArr.add(materialsStocksModel);
                            }
                           /* } else {
                                try {
                                    actual.setText("" + btMaterialsModel.getActualStock());
                                    Toast.makeText(activity, "Actual Stock cannot be greater than Virtual Stock", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }*/
                        }
                    }
                });

                materialtable.addView(trm);

            }
        }
    }

    private void CallUpdateDialog(final BTStockMaterialsModel btMaterialsModel) {
        dialog_edt = new Dialog(activity);
        dialog_edt.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_edt.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_edt.setContentView(R.layout.dialog_updatematerial);
        dialog_edt.setCanceledOnTouchOutside(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.9);
        dialog_edt.getWindow().setLayout(width, height);

        final EditText edt_itemname = (EditText) dialog_edt.findViewById(R.id.edt_itemname);
        final EditText edt_virtualstk = (EditText) dialog_edt.findViewById(R.id.edt_virtualstk);
        final EditText edt_actualstk = (EditText) dialog_edt.findViewById(R.id.edt_actualstk);
        final EditText edt_rem = (EditText) dialog_edt.findViewById(R.id.edt_rem);
        Button btn_updatemat = (Button) dialog_edt.findViewById(R.id.btn_updatemat);

        edt_itemname.setText("" + btMaterialsModel.getMaterialName());
        edt_virtualstk.setText("" + btMaterialsModel.getVirtualStock());
        edt_actualstk.setText("" + btMaterialsModel.getActualStock());

        ImageView img_cancel = (ImageView) dialog_edt.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_edt.dismiss();
            }
        });

        btn_updatemat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_virtualstk.getText().toString().trim().equalsIgnoreCase("")) {
                    TastyToast.makeText(activity, "Please enter Virtual Stock", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    edt_virtualstk.requestFocus();
                } else if (edt_actualstk.getText().toString().trim().equalsIgnoreCase("")) {
                    TastyToast.makeText(activity, "Please enter Actual Stock", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    edt_actualstk.requestFocus();
                } else if (edt_rem.getText().toString().trim().equalsIgnoreCase("")) {
                    TastyToast.makeText(activity, "Please enter Remarks", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    edt_rem.requestFocus();
                } else if (edt_rem.getText().toString().trim().length() < 10) {
                    TastyToast.makeText(activity, "Please Enter Remarks more than 10 digit", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    edt_rem.requestFocus();
                } else {

                    UpdateMaterial m = new UpdateMaterial();
                    m.BtechId = "" + appPreferenceManager.getLoginResponseModel().getUserID();
                    UpdateMaterial.AllMaterialStock ms = new UpdateMaterial.AllMaterialStock();
                    ms.materialID = "" + btMaterialsModel.getMaterialID();
                    ms.virtualStock = "" + edt_virtualstk.getText().toString().trim();
                    ms.actualStock = "" + edt_actualstk.getText().toString().trim();
                    ms.remarks = "" + edt_rem.getText().toString().trim();
                    m.allMaterialStock = new ArrayList<>();
                    m.allMaterialStock.add(ms);

                    AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                    ApiCallAsyncTask setMaterialsDetailApiAsyncTask = asyncTaskForRequest.getPostStockMaterialOrderAsyncTask(m);
                    setMaterialsDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setMaterialOrderDetailsApiAsyncTaskDelegateResult());
                    if (isNetworkAvailable(activity)) {
                        setMaterialsDetailApiAsyncTask.execute(setMaterialsDetailApiAsyncTask);
                    } else {
                        Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
                    }

                }
            }
        });

        dialog_edt.show();
    }

    private class setMaterialOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, "Material stock updated successfully.", LENGTH_SHORT).show();
                try {
                    activity.getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, "" + json.toString(), LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}


