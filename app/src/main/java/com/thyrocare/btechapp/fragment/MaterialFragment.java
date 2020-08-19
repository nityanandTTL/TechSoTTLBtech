package com.thyrocare.btechapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
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

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.CampOrderBookingActivity;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.customview.CustomOKDialog;
import com.thyrocare.btechapp.delegate.CustomOkDialogOkButtonOnClickedDelegate;
import com.thyrocare.btechapp.models.api.request.BtechsRequestModel;
import com.thyrocare.btechapp.models.api.request.MaterialorderRequestModel;
import com.thyrocare.btechapp.models.api.response.CampScanQRResponseModel;
import com.thyrocare.btechapp.models.api.response.MaterialINVResponseModel;
import com.thyrocare.btechapp.models.data.BTMaterialsModel;
import com.thyrocare.btechapp.models.data.BtechIdModel;
import com.thyrocare.btechapp.models.data.MaterialDetailsModel;
import com.thyrocare.btechapp.models.data.MaterialsStocksModel;



import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

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
    private Global global;


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
        global  = new Global(activity);

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

                            if (isNetworkAvailable(activity)) {
                                CallPostMaterialInvRequestApi(btechsRequestModel);
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
        if (isNetworkAvailable(activity)) {
            CallGetMaterialsDetailsApi(Category);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMaterialsINV() {
        if (isNetworkAvailable(activity)) {
            CallGetMaterialINVDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void CallGetMaterialsDetailsApi(String category) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<MaterialDetailsModel>> responseCall = apiInterface.CallGetMaterialsDetailsApi(category);
        global.showProgressDialog(activity, "Fetching products. Please wait..");
        responseCall.enqueue(new Callback<ArrayList<MaterialDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MaterialDetailsModel>> call, retrofit2.Response<ArrayList<MaterialDetailsModel>> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    materialDetailsModels = new ArrayList<>();
                    materialDetailsModels = response.body();
                    if (materialDetailsModels != null && materialDetailsModels.size() > 0) {
                    } else {
                        Toast.makeText(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MaterialDetailsModel>> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallGetMaterialINVDetailsApi() {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<MaterialINVResponseModel> responseCall = apiInterface.CallGetMaterialINVDetailsApi(appPreferenceManager.getLoginResponseModel().getUserID());
        global.showProgressDialog(activity, "Fetching products. Please wait..");
        responseCall.enqueue(new Callback<MaterialINVResponseModel>() {
            @Override
            public void onResponse(Call<MaterialINVResponseModel> call, retrofit2.Response<MaterialINVResponseModel> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    materialINVResponseModel = response.body();
                    initData();
                }
            }
            @Override
            public void onFailure(Call<MaterialINVResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallPostMaterialInvRequestApi(BtechsRequestModel btechsRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallPostMaterialInvRequestApi(btechsRequestModel);
        global.showProgressDialog(activity, "Fetching products. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                global.hideProgressDialog();
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        materialtable.removeAllViews();
                        fetchMaterialsINV();
                    }else{
                        global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
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


