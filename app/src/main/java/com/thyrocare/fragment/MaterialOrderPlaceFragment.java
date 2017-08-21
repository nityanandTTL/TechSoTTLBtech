package com.thyrocare.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.PaymentsActivity;
import com.thyrocare.models.api.request.MaterialorderRequestModel;
import com.thyrocare.models.api.response.MaterialINVResponseModel;
import com.thyrocare.models.data.BTMaterialsModel;
import com.thyrocare.models.data.FinalMaterialModel;
import com.thyrocare.models.data.MaterialDetailsModel;
import com.thyrocare.models.data.MaterialOrderDataModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Orion on 5/3/2017.
 */

public class MaterialOrderPlaceFragment extends AbstractFragment {
    public static final String TAG_FRAGMENT = "MATERIAL_ORDER_PLACE_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;
    FloatingActionButton btnFab;
    private ArrayList<MaterialDetailsModel> materialDetailsModels;
    private ArrayList<MaterialOrderDataModel> materialsOrderArr = new ArrayList<>();
    private MaterialINVResponseModel materialINVResponseModel;
    TableLayout materialordertable;
    private ArrayList<FinalMaterialModel> finalMaterialModelsArr;
    private ArrayList<FinalMaterialModel> Filterarraylst ;
  /*  String PROMOTIONAL="205";
    String OPERATIONAL="204";*/
    String Category="204";
    Button Material_order;
    EditText searchbar;
    RadioButton Operational_radio,Promotional_radio;
    RadioGroup group;
    LinearLayout btn_virtual,btn_material;
    private float Grandtotal = 0;

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
        activity.toolbarHome.setTitle("Place Material Order");
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_place_order, container, false);
        initUI();

        setListners();
        fetchMaterialsDetails2();
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
        materialordertable = (TableLayout) rootView.findViewById(R.id.materialordertable);
        btnFab = (FloatingActionButton) rootView.findViewById(R.id.btnFloatingAction);
        searchbar=(EditText) rootView.findViewById(R.id.searchbar);
        Operational_radio=(RadioButton) rootView.findViewById(R.id.operational);
        Promotional_radio=(RadioButton) rootView.findViewById(R.id.promotional);
        group=(RadioGroup) rootView.findViewById(R.id.group);
        Material_order =(Button) rootView.findViewById(R.id.btn_material_order);
        btn_virtual=(LinearLayout) rootView.findViewById(R.id.virtual_stock);
        btn_material=(LinearLayout) rootView.findViewById(R.id.material_order);

    }


    private void setListners() {

        Material_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialorderRequestModel materialorderRequestModel = new MaterialorderRequestModel();
                materialorderRequestModel.setBTechId(appPreferenceManager.getLoginResponseModel().getUserID());
                materialorderRequestModel.setBTTransactionCode("NA");
                materialorderRequestModel.setOrderId("0");
                materialorderRequestModel.setFinalStatus("BTECHGENERATE");
                materialorderRequestModel.setRemarks("NA");
                materialorderRequestModel.setMaterialDetails(materialsOrderArr);
                Grandtotal= 0;
                for (MaterialOrderDataModel materialOrderDataModel:
                materialsOrderArr) {
                    for (FinalMaterialModel finalMaterialModel:finalMaterialModelsArr

                         ) {
                        if (materialOrderDataModel.getMaterialId()==finalMaterialModel.getMaterialDetailsModel().getMaterialId()){

                            Grandtotal+=materialOrderDataModel.getOrderQty()*Float.parseFloat(finalMaterialModel.getMaterialDetailsModel().getUnitCost());

                        }
                    }
                }
                AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
                ApiCallAsyncTask setMaterialsDetailApiAsyncTask = asyncTaskForRequest.getPostMaterialOrderAsyncTask(materialorderRequestModel);
                setMaterialsDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new MaterialOrderPlaceFragment.setMaterialOrderDetailsApiAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    setMaterialsDetailApiAsyncTask.execute(setMaterialsDetailApiAsyncTask);
                } else {
                    Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
                }



            }
        });





        btnFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // searchbar.setVisibility(View.VISIBLE);
                searchbar.setVisibility((searchbar.getVisibility() == View.VISIBLE)
                        ? View.INVISIBLE: View.VISIBLE);


            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton) rootView.findViewById(checkedId);
                String text  = checkedRadioButton.getText().toString();

                if (Promotional_radio.isChecked()){

                    Category="205";
                    Toast.makeText(getActivity(),text,LENGTH_SHORT).show();
                    materialordertable.removeAllViews();
                    fetchMaterialsDetails2();
                }
                else
                {
                    Category="204";
                    Toast.makeText(getActivity(),text,LENGTH_SHORT).show();
                    materialordertable.removeAllViews();
                    fetchMaterialsDetails2();

                }


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
        btn_virtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new  MaterialFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment ).commit();


            }
        });

        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment mFragment = new MaterialOrderPlaceFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment ).commit();


            }
        });





    }

    //set Deleagte
    private class setMaterialOrderDetailsApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                Toast.makeText(getActivity(), "Success", LENGTH_SHORT).show();
                JSONObject jsonResponse = new JSONObject(json);
                final int orderNo = jsonResponse.getInt("OrderId");

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("")
                        .setMessage("Your Order has been Placed Successfully. Your Order No is "+orderNo+". Please Proceed with Payment in Case you want to pay Material Payment.")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                                int totalAMount = Math.round(Grandtotal);
                                Intent intentPayments = new Intent(activity, PaymentsActivity.class);
                                intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO,orderNo+"");
                                intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT,totalAMount+"");
                                intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE,Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID,1);
                                startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                            }
                        })
                        .setCancelable(false)
                        .show();



            }
        }
        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(activity, R.string.network_error, LENGTH_SHORT).show();
        }


    }






    private void fetchMaterialsDetails2() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask fetchMaterialDetailApiAsyncTask = asyncTaskForRequest.getMaterialsDetailsRequestAsyncTask(Category);
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
        if(materialDetailsModels!= null) {
            for (MaterialDetailsModel materialDetailsModel :
                    materialDetailsModels) {
                if (materialINVResponseModel.getBTMaterials() != null) {
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
        }
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
                            Float temprate = Float.parseFloat(finalMaterialModels.getMaterialDetailsModel().getUnitCost().toString());

                            //Toast.makeText(getActivity(),finalMaterialModels.getMaterialDetailsModel().getMaterialName()+"",Toast.LENGTH_SHORT).show();
                            Float total = temprate * (Float.parseFloat(s.toString()));
                            Total.setText(total + "");
                            MaterialOrderDataModel materialOrderDataModel = new MaterialOrderDataModel();
                            materialOrderDataModel.setMaterialId(finalMaterialModels.getMaterialDetailsModel().getMaterialId());
                            materialOrderDataModel.setOrderQty(Integer.parseInt(s.toString()));
                            if(materialsOrderArr.contains(materialOrderDataModel)){
                                materialsOrderArr.remove(materialOrderDataModel);
                            }
                            materialsOrderArr.add(materialOrderDataModel);
                        }
                        else {

                            Total.setText(0 + "");
                            MaterialOrderDataModel materialOrderDataModel = new MaterialOrderDataModel();
                            materialOrderDataModel.setMaterialId(finalMaterialModels.getMaterialDetailsModel().getMaterialId());
                            if(materialsOrderArr.contains(materialOrderDataModel)){
                                materialsOrderArr.remove(materialOrderDataModel);
                            }

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