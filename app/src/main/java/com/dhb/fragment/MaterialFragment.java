package com.dhb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.customview.CustomOKDialog;
import com.dhb.delegate.CustomOkDialogOkButtonOnClickedDelegate;
import com.dhb.models.api.request.BtechsRequestModel;
import com.dhb.models.api.response.MaterialINVResponseModel;
import com.dhb.models.data.BTMaterialsModel;
import com.dhb.models.data.BtechIdModel;
import com.dhb.models.data.MaterialDetailsModel;
import com.dhb.models.data.MaterialsStocksModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.network.ResponseParser;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by E4904 on 5/2/2017.
 */

public class MaterialFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "MATERIAL_FRAGMENT";
    HomeScreenActivity activity;
    LinearLayout orderstatus;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ArrayList<MaterialDetailsModel> materialDetailsModels;
    private MaterialINVResponseModel materialINVResponseModel;
    TableLayout materialtable;
    Button update;
    Integer finalstock;
    CustomOKDialog cdd;
    ArrayList<MaterialsStocksModel> stockModelsArr;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
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
        fetchMaterialsDetails();
        setListners();
        return rootView;


    }

    @Override
    public void initUI() {
        super.initUI();
        materialtable = (TableLayout) rootView.findViewById(R.id.materialtable);
        update = (Button) rootView.findViewById(R.id.updateRecord);
        orderstatus = (LinearLayout) rootView.findViewById(R.id.order_stock);
    }

    private void setListners() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdd = new CustomOKDialog(activity, new CustomOkDialogOkButtonOnClickedDelegate() {
                    @Override
                    public void onClicked(String remarks) {
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
                });
                cdd.show();
                cdd.setCancelable(false);
            }
        });


        orderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new MaterialOrderPlaceFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment ).commit();


            }
        });

    }


    private void fetchMaterialsDetails() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialDetailApiAsyncTask = asyncTaskForRequest.getMaterialsDetailsRequestAsyncTask();
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
                    Toast.makeText(activity, "MaterialdetailsResponseModel not null", Toast.LENGTH_SHORT).show();

                }

                fetchMaterialsINV();

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

                fetchMaterialsDetails();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private void initData() {

        if (materialINVResponseModel != null && materialINVResponseModel.getBTMaterials().size() > 0) {
            TableRow trmH = (TableRow)  LayoutInflater.from(activity).inflate(R.layout.item_title_materials, null);
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
                        if(!InputUtils.isNull(s.toString())) {
                            if(Integer.parseInt(s.toString())<=Integer.parseInt(btMaterialsModel.getVirtualStock())) {
                                MaterialsStocksModel materialsStocksModel = new MaterialsStocksModel();
                                materialsStocksModel.setMaterialID(Integer.parseInt(btMaterialsModel.getMaterialID()));
                                materialsStocksModel.setActualStock(Integer.parseInt(s.toString()));
                                if (stockModelsArr.contains(materialsStocksModel)) {
                                    stockModelsArr.remove(materialsStocksModel);
                                    stockModelsArr.add(materialsStocksModel);
                                } else {
                                    stockModelsArr.add(materialsStocksModel);
                                }
                            }
                            else{
                                actual.setText(btMaterialsModel.getVirtualStock());
                                Toast.makeText(activity,"Actual Stock cannot be greater than Virtual Stock",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                materialtable.addView(trm);
            }
        }

    }
}


