package com.dhb.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.models.api.response.MaterialINVResponseModel;
import com.dhb.models.data.BTMaterialsModel;
import com.dhb.models.data.FinalMaterialModel;
import com.dhb.models.data.MaterialDetailsModel;
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
import java.util.Collections;

/**
 * Created by E4904 on 5/3/2017.
 */

public class MaterialOrderPlaceFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = "MATERIAL_ORDER_PLACE_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    FloatingActionButton btnFab;
    private ArrayList<MaterialDetailsModel> materialDetailsModels;
    private MaterialINVResponseModel materialINVResponseModel;
    TableLayout materialordertable;
    private ArrayList<FinalMaterialModel> finalMaterialModelsArr;
    private ArrayList<FinalMaterialModel> Filterarraylst ;
    private ArrayList<FinalMaterialModel> templst;
    Integer temprate,total =0;
    EditText searchbar;

    public MaterialOrderPlaceFragment() {
        // Required empty public constructor
    }

    public static MaterialOrderPlaceFragment newInstance() {
        MaterialOrderPlaceFragment fragment = new MaterialOrderPlaceFragment();
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
        rootView = inflater.inflate(R.layout.orderplacefragment, container, false);
        initUI();
        fetchMaterialsDetails2();
        setListners();
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        materialordertable = (TableLayout) rootView.findViewById(R.id.materialordertable);
        btnFab = (FloatingActionButton) rootView.findViewById(R.id.btnFloatingAction);
        searchbar=(EditText) rootView.findViewById(R.id.searchbar);

    }


    private void setListners() {
        btnFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // searchbar.setVisibility(View.VISIBLE);
                searchbar.setVisibility((searchbar.getVisibility() == View.VISIBLE)
                        ? View.INVISIBLE: View.VISIBLE);


            }
        });

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                performFiltering(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });

    }




    private void fetchMaterialsDetails2() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialDetailApiAsyncTask = asyncTaskForRequest.getMaterialsDetailsRequestAsyncTask();
        fetchMaterialDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new MaterialOrderPlaceFragment.FetchMaterialsDetailsApiAsyncTaskDelegateResult2());
        if (isNetworkAvailable(activity)) {
            fetchMaterialDetailApiAsyncTask.execute(fetchMaterialDetailApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMaterialsINV2() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialINVApiAsyncTask = asyncTaskForRequest.getMaterialINVDetailsRequestAsyncTask();
        fetchMaterialINVApiAsyncTask.setApiCallAsyncTaskDelegate(new MaterialOrderPlaceFragment.FetchMaterialsINVApiAsyncTaskDelegateResult2());
        if (isNetworkAvailable(activity)) {
            fetchMaterialINVApiAsyncTask.execute(fetchMaterialINVApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private class FetchMaterialsDetailsApiAsyncTaskDelegateResult2 implements ApiCallAsyncTaskDelegate {

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

                fetchMaterialsINV2();

            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }


    private class FetchMaterialsINVApiAsyncTaskDelegateResult2 implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                MaterialINVResponseModel materialINVDetailsResponseModel = new MaterialINVResponseModel();

                materialINVDetailsResponseModel = responseParser.getMaterialINVDetailsResponseModel(json, statusCode);
                materialINVResponseModel = materialINVDetailsResponseModel;
                fetchData();
            }

        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchData(){
        ArrayList<Integer> insertedMaterialIDs = new ArrayList<>();
        finalMaterialModelsArr = new ArrayList<>();
        Filterarraylst = new ArrayList<>();
        for (MaterialDetailsModel materialDetailsModel :
                materialDetailsModels) {
            for (BTMaterialsModel btMaterialsModel :
                    materialINVResponseModel.getBTMaterials()) {
                if ((materialDetailsModel.getMaterialId() + "").equals(btMaterialsModel.getMaterialID())) {
                    FinalMaterialModel finalMaterialModel = new FinalMaterialModel();
                    finalMaterialModel.setBtMaterialsModel(btMaterialsModel);
                    finalMaterialModel.setMaterialDetailsModel(materialDetailsModel);
                    finalMaterialModelsArr.add(finalMaterialModel);
                    insertedMaterialIDs.add(materialDetailsModel.getMaterialId());
                }
            }
        }

        for (MaterialDetailsModel materialDetailsModel :
                materialDetailsModels) {
            if (!insertedMaterialIDs.contains(materialDetailsModel.getMaterialId())) {
                FinalMaterialModel finalMaterialModel = new FinalMaterialModel();
                finalMaterialModel.setBtMaterialsModel(new BTMaterialsModel());
                finalMaterialModel.setMaterialDetailsModel(materialDetailsModel);
                finalMaterialModelsArr.add(finalMaterialModel);
            }
        }
        Filterarraylst = finalMaterialModelsArr;
        initData();
    }

    private void initData() {
        if (Filterarraylst != null) {
            TableRow trmH = (TableRow)  LayoutInflater.from(activity).inflate(R.layout.item_title_materials2, null);
            materialordertable.addView(trmH);

            for (final FinalMaterialModel finalMaterialModels :
                    Filterarraylst) {


                EditText Quantity;
                final TextView Total;

                TableRow trm = (TableRow)  LayoutInflater.from(activity).inflate(R.layout.materialorderfinal, null);
                TextView item = (TextView) trm.findViewById(R.id.txt_finalitem);
                TextView Stock = (TextView) trm.findViewById(R.id.txt_stock);
                TextView Rate = (TextView) trm.findViewById(R.id.txt_rate);

                Quantity = (EditText) trm.findViewById(R.id.edit_quantity);
                Total= (TextView) trm.findViewById(R.id.txt_total);

                item.setText(finalMaterialModels.getMaterialDetailsModel().getMaterialName() + "");
                Stock.setText(finalMaterialModels.getBtMaterialsModel().getVirtualStock() + "");
                Rate.setText(finalMaterialModels.getMaterialDetailsModel().getUnitCost() + "");

                Quantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!InputUtils.isNull(s.toString())) {
                            Integer temprate= Integer.parseInt (finalMaterialModels.getMaterialDetailsModel().getUnitCost().toString());
                            //Toast.makeText(getActivity(),finalMaterialModels.getMaterialDetailsModel().getMaterialName()+"",Toast.LENGTH_SHORT).show();
                            Integer total = ((temprate) * (Integer.parseInt((s).toString())));
                            Total.setText(total+"");

                        }
                    }
                });
                materialordertable.addView(trm);
            }
        }
    }

    private void performFiltering(String constraint) {
        String filterString = constraint.toUpperCase();

        ArrayList<FinalMaterialModel> list = finalMaterialModelsArr;

        int count = list.size();
        ArrayList<FinalMaterialModel> nlist = new ArrayList<FinalMaterialModel>(count);

        String filterableString;

        for (int i = 0; i < count; i++){
            filterableString = list.get(i).getMaterialDetailsModel().getMaterialName();
            if (filterableString.toUpperCase().contains(filterString)|| InputUtils.isNull(filterString)){
                nlist.add(list.get(i));
            }
        }
        Filterarraylst = nlist;
        materialordertable.removeAllViews();
        initData();
    }
}