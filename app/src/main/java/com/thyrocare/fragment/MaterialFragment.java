package com.thyrocare.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.customview.CustomOKDialog;
import com.thyrocare.delegate.CustomOkDialogOkButtonOnClickedDelegate;
import com.thyrocare.models.api.request.BtechsRequestModel;
import com.thyrocare.models.api.response.MaterialINVResponseModel;
import com.thyrocare.models.data.BTMaterialsModel;
import com.thyrocare.models.data.BtechIdModel;
import com.thyrocare.models.data.MaterialDetailsModel;
import com.thyrocare.models.data.MaterialsStocksModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Orion on 5/2/2017.
 */

public class MaterialFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "MATERIAL_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ArrayList<MaterialDetailsModel> materialDetailsModels;
    private MaterialINVResponseModel materialINVResponseModel;
    TableLayout materialtable;
    Button update;
    LinearLayout btn_virtual, btn_material;
    Integer finalstock;
    CustomOKDialog cdd;
    String Category = "180";
    ArrayList<MaterialsStocksModel> stockModelsArr;
    private AlertDialog.Builder alertDialogBuilder;


    public MaterialFragment() {
        // Required empty public constructor
    }

    public static MaterialFragment newInstance() {
        MaterialFragment fragment = new MaterialFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Virtual Stock");
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
        rootView = inflater.inflate(R.layout.fragement_material, container, false);
        stockModelsArr = new ArrayList<>();
        initUI();
        //  fetchMaterialsDetails();
        fetchMaterialsINV();
        setListners();
        return rootView;


    }

    @Override
    public void initUI() {
        super.initUI();
        materialtable = (TableLayout) rootView.findViewById(R.id.materialtable);
        update = (Button) rootView.findViewById(R.id.updateRecord);
        btn_virtual = (LinearLayout) rootView.findViewById(R.id.virtual_stock);
        btn_material = (LinearLayout) rootView.findViewById(R.id.material_order);

    }

    private void setListners() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdd = new CustomOKDialog(activity, new CustomOkDialogOkButtonOnClickedDelegate() {
                    @Override
                    public void onClicked(String remarks) {

                        if (InputUtils.isNull(remarks.trim())){
                            alertDialogBuilder = new AlertDialog.Builder(activity);
                            alertDialogBuilder
                                    .setMessage("Please enter remarks.")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }else{
                            BtechsRequestModel btechsRequestModel = new BtechsRequestModel();

                            BtechIdModel btechIdModel = new BtechIdModel();
                            btechIdModel.setBTechId(Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                            btechIdModel.setRemarks(remarks);

                            btechsRequestModel.setBTechs(btechIdModel);

                            btechsRequestModel.setStocks(stockModelsArr);
                            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                            ApiCallAsyncTask setMaterialDetailApiAsyncTask = asyncTaskForRequest.getPostMaterialInvRequestAsyncTask(btechsRequestModel);
                            setMaterialDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new setMaterialsDetailsApiAsyncTaskDelegateResult());
                            if (isNetworkAvailable(activity)) {
                                setMaterialDetailApiAsyncTask.execute(setMaterialDetailApiAsyncTask);
                            } else {
                                Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                cdd.show();
                cdd.setCancelable(false);
            }
        });


        btn_virtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new MaterialFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment).commit();


            }
        });

        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new MaterialOrderPlaceFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment).commit();


            }
        });

    }


    private void fetchMaterialsDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialDetailApiAsyncTask = asyncTaskForRequest.getMaterialsDetailsRequestAsyncTask(Category);
        fetchMaterialDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchMaterialsDetailsApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchMaterialDetailApiAsyncTask.execute(fetchMaterialDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMaterialsINV() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialINVApiAsyncTask = asyncTaskForRequest.getMaterialINVDetailsRequestAsyncTask();
        fetchMaterialINVApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchMaterialsINVApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            fetchMaterialINVApiAsyncTask.execute(fetchMaterialINVApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private class FetchMaterialsDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                materialDetailsModels = new ArrayList<>();

                materialDetailsModels = responseParser.getMaterialdetailsResponseModel(json, statusCode);
                if (materialDetailsModels != null && materialDetailsModels.size() > 0) {
//                    Toast.makeText(activity, "MaterialdetailsResponseModel not null", Toast.LENGTH_SHORT).show();

                }


            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private class FetchMaterialsINVApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                MaterialINVResponseModel materialINVDetailsResponseModel = new MaterialINVResponseModel();

                materialINVDetailsResponseModel = responseParser.getMaterialINVDetailsResponseModel(json, statusCode);
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

    private class setMaterialsDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                materialtable.removeAllViews();

                // fetchMaterialsDetails();
                fetchMaterialsINV();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private void initData() {

        if (materialINVResponseModel != null && materialINVResponseModel.getBTMaterials() != null && materialINVResponseModel.getBTMaterials().size() > 0) {
            TableRow trmH = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_title_materials, null);
            materialtable.addView(trmH);
            for (final BTMaterialsModel btMaterialsModel :
                    materialINVResponseModel.getBTMaterials()) {
                TableRow trm = (TableRow) LayoutInflater.from(activity).inflate(R.layout.item_material_layout, null);
                TextView item = (TextView) trm.findViewById(R.id.txt_item);
                TextView virtualstock = (TextView) trm.findViewById(R.id.txt_virtualstock);
                final EditText actual = (EditText) trm.findViewById(R.id.edit_actual);

                item.setText(btMaterialsModel.getMaterialName() + "");
                virtualstock.setText(btMaterialsModel.getVirtualStock() + "");
                actual.setText(btMaterialsModel.getVirtualStock() + "");
                actual.setTag(btMaterialsModel);
                MaterialsStocksModel materialsStocksModel = new MaterialsStocksModel();
                materialsStocksModel.setMaterialID(Integer.parseInt(btMaterialsModel.getMaterialID()));
                materialsStocksModel.setActualStock(Integer.parseInt(btMaterialsModel.getVirtualStock()));
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
                            try{
                                if (Integer.parseInt(s.toString()) <= Integer.parseInt(btMaterialsModel.getVirtualStock())) {
                                    MaterialsStocksModel materialsStocksModel = new MaterialsStocksModel();
                                    materialsStocksModel.setMaterialID(Integer.parseInt(btMaterialsModel.getMaterialID()));
                                    materialsStocksModel.setActualStock(Integer.parseInt(s.toString()));
                                    if (stockModelsArr.contains(materialsStocksModel)) {
                                        stockModelsArr.remove(materialsStocksModel);
                                        stockModelsArr.add(materialsStocksModel);
                                    } else {
                                        stockModelsArr.add(materialsStocksModel);
                                    }
                                } else {
                                    actual.setText(btMaterialsModel.getVirtualStock());
                                    Toast.makeText(activity, "Actual Stock cannot be greater than Virtual Stock", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
                materialtable.addView(trm);
            }
        }

    }
}


