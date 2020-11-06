package com.thyrocare.btechapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.PaymentsActivity;
import com.thyrocare.btechapp.adapter.DialogMaterialListAdapter;
import com.thyrocare.btechapp.models.api.request.MaterialorderRequestModel;
import com.thyrocare.btechapp.models.api.response.MaterialINVResponseModel;
import com.thyrocare.btechapp.models.data.BTMaterialsModel;
import com.thyrocare.btechapp.models.data.FinalMaterialModel;
import com.thyrocare.btechapp.models.data.MaterialDetailsModel;
import com.thyrocare.btechapp.models.data.MaterialOrderDataModel;


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
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;

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
    private ArrayList<FinalMaterialModel> Filterarraylst;
    /*  String PROMOTIONAL="205";
      String OPERATIONAL="204";*/
    String Category = "204";
    Button Material_order;
    EditText searchbar;
    RadioButton Operational_radio, Promotional_radio;
    RadioGroup group;
    LinearLayout btn_virtual, btn_material;
    private float Grandtotal = 0;
    private Dialog material_sel_dialog;
    static MaterialOrderPlaceFragment fragment;
    private TableRow tbl_header;
    private Global global;

    public MaterialOrderPlaceFragment() {
        // Required empty public constructor
    }

    public static MaterialOrderPlaceFragment newInstance() {
        fragment = new MaterialOrderPlaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        global = new Global(activity);
        try {
            if (activity.toolbarHome != null) {
                activity.toolbarHome.setTitle("Place Material Order");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        tbl_header = (TableRow) rootView.findViewById(R.id.tbl_header);
        tbl_header.setVisibility(View.GONE);
        btnFab = (FloatingActionButton) rootView.findViewById(R.id.btnFloatingAction);
        searchbar = (EditText) rootView.findViewById(R.id.searchbar);
        Operational_radio = (RadioButton) rootView.findViewById(R.id.operational);
        Promotional_radio = (RadioButton) rootView.findViewById(R.id.promotional);
        group = (RadioGroup) rootView.findViewById(R.id.group);
        Material_order = (Button) rootView.findViewById(R.id.btn_material_order);
        btn_virtual = (LinearLayout) rootView.findViewById(R.id.virtual_stock);
        btn_material = (LinearLayout) rootView.findViewById(R.id.material_order);

    }


    private void setListners() {

        Material_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (materialsOrderArr != null) {
                    if (materialsOrderArr.size() != 0) {
                        CallOrderedListDialog();
                    } else {
                        Toast.makeText(activity, "Add Material Quantity.", LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Add Material Quantity.", LENGTH_SHORT).show();
                }

            }
        });


        btnFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // searchbar.setVisibility(View.VISIBLE);
                searchbar.setVisibility((searchbar.getVisibility() == View.VISIBLE)
                        ? View.INVISIBLE : View.VISIBLE);


            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) rootView.findViewById(checkedId);
                String text = checkedRadioButton.getText().toString();

                if (Promotional_radio.isChecked()) {

                    Category = "205";
                    Toast.makeText(getActivity(), text, LENGTH_SHORT).show();
                    materialordertable.removeAllViews();
                    tbl_header.setVisibility(View.GONE);

                    if (materialsOrderArr != null) {
                        materialsOrderArr = null;
                    }
                    materialsOrderArr = new ArrayList<>();

                    fetchMaterialsDetails2();
                } else {
                    Category = "204";
                    Toast.makeText(getActivity(), text, LENGTH_SHORT).show();
                    materialordertable.removeAllViews();
                    tbl_header.setVisibility(View.GONE);

                    if (materialsOrderArr != null) {
                        materialsOrderArr = null;
                    }
                    materialsOrderArr = new ArrayList<>();

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

    private void CallOrderedListDialog() {

        try {
            material_sel_dialog = new Dialog(activity);
            material_sel_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            material_sel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            material_sel_dialog.setContentView(R.layout.dialog_material_orderedlist);

            Grandtotal = 0;

            initMaterialUI(material_sel_dialog);


            material_sel_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMaterialUI(final Dialog material_sel_dialog) {

        for (MaterialOrderDataModel materialOrderDataModel :
                materialsOrderArr) {
            for (FinalMaterialModel finalMaterialModel : finalMaterialModelsArr
                    ) {
                if (materialOrderDataModel.getMaterialId() == finalMaterialModel.getMaterialDetailsModel().getMaterialId()) {

                    Grandtotal += materialOrderDataModel.getOrderQty() * Float.parseFloat(finalMaterialModel.getMaterialDetailsModel().getUnitCost());

                }
            }
        }

        Button tv_place_order_amt = (Button) material_sel_dialog.findViewById(R.id.btn_material_order_dia);
        TextView txt_amount = (TextView) material_sel_dialog.findViewById(R.id.txt_amount);
        txt_amount.setText("Total : " + Grandtotal);

        RecyclerView recy_itemlist = (RecyclerView) material_sel_dialog.findViewById(R.id.recy_itemlist);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity().getApplicationContext());
        recy_itemlist.setLayoutManager(lm);
        recy_itemlist.setHasFixedSize(true);

        DialogMaterialListAdapter adp = new DialogMaterialListAdapter(activity, materialsOrderArr, fragment);
        recy_itemlist.setAdapter(adp);

        ImageView img_cancel = (ImageView) material_sel_dialog.findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                material_sel_dialog.dismiss();
            }
        });

        tv_place_order_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                material_sel_dialog.dismiss();
                initTable();
            }
        });
    }

    private void initTable() {
        MaterialorderRequestModel materialorderRequestModel = new MaterialorderRequestModel();
        materialorderRequestModel.setBTechId(appPreferenceManager.getLoginResponseModel().getUserID());
        materialorderRequestModel.setBTTransactionCode("NA");
        materialorderRequestModel.setOrderId("0");
        materialorderRequestModel.setFinalStatus("BTECHGENERATE");
        materialorderRequestModel.setRemarks("NA");
        materialorderRequestModel.setMaterialDetails(materialsOrderArr);
        Grandtotal = 0;
        for (MaterialOrderDataModel materialOrderDataModel :
                materialsOrderArr) {
            for (FinalMaterialModel finalMaterialModel : finalMaterialModelsArr

                    ) {
                if (materialOrderDataModel.getMaterialId() == finalMaterialModel.getMaterialDetailsModel().getMaterialId()) {

                    Grandtotal += materialOrderDataModel.getOrderQty() * Float.parseFloat(finalMaterialModel.getMaterialDetailsModel().getUnitCost());
                    Logger.error("Grandtotal123 " + Grandtotal);
                }
            }
        }
        if (isNetworkAvailable(activity)) {
            CallGetPostMaterialOrderApi(materialorderRequestModel);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, LENGTH_SHORT).show();
        }
    }


    private void fetchMaterialsDetails2() {

        if (isNetworkAvailable(activity)) {
            CallGetMaterialsDetailsApi(Category);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMaterialsINV2() {
        if (isNetworkAvailable(activity)) {
            CallGetMaterialINVDetailsApi();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void CallGetPostMaterialOrderApi(MaterialorderRequestModel materialorderRequestModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallGetPostMaterialOrderApi(materialorderRequestModel);
        global.showProgressDialog(activity, "Fetching products. Please wait..");
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                global.hideProgressDialog(activity);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject jsonResponse = new JSONObject(response.body());
                        if (String.valueOf(jsonResponse.getString("ResponseMessage")).equals("SUCCESS")) {

                            Toast.makeText(getActivity(), "Success", LENGTH_SHORT).show();
                            Logger.error("response material :" + jsonResponse);
                            final int orderNo = jsonResponse.getInt("OrderId");
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("")
                                    .setMessage("Your Order has been Placed Successfully. Your Order No is " + orderNo + ". Please Proceed with Payment in Case you want to pay Material Payment.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            int totalAMount = Math.round(Grandtotal);
                                            Intent intentPayments = new Intent(activity, PaymentsActivity.class);
                                            intentPayments.putExtra(BundleConstants.PAYMENTS_ORDER_NO, orderNo + "");
                                            intentPayments.putExtra(BundleConstants.PAYMENTS_AMOUNT, totalAMount + "");
                                            intentPayments.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                                            intentPayments.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 1);
                                            startActivityForResult(intentPayments, BundleConstants.PAYMENTS_START);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            Toast.makeText(activity, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallGetMaterialsDetailsApi(String category) {

        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<MaterialDetailsModel>> responseCall = apiInterface.CallGetMaterialsDetailsApi(category);
        global.showProgressDialog(activity, "Fetching products. Please wait..");
        responseCall.enqueue(new Callback<ArrayList<MaterialDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<MaterialDetailsModel>> call, retrofit2.Response<ArrayList<MaterialDetailsModel>> response) {
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    materialDetailsModels = new ArrayList<>();
                    materialDetailsModels = response.body();
                    fetchMaterialsINV2();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MaterialDetailsModel>> call, Throwable t) {
                global.hideProgressDialog(activity);
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
                global.hideProgressDialog(activity);
                if (response.isSuccessful() && response.body() != null) {
                    materialINVResponseModel = response.body();
                    fetchData();
                }
            }
            @Override
            public void onFailure(Call<MaterialINVResponseModel> call, Throwable t) {
                global.hideProgressDialog(activity);
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void fetchData() {
        ArrayList<Integer> insertedMaterialIDs = new ArrayList<>();
        finalMaterialModelsArr = new ArrayList<>();
        Filterarraylst = new ArrayList<>();
        if (materialDetailsModels != null) {
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
//            TableRow trmH = (TableRow)  LayoutInflater.from(activity).inflate(R.layout.item_title_materials2, null);
            /*materialordertable.addView(trmH);*/

            tbl_header.setVisibility(View.VISIBLE);

            for (final FinalMaterialModel finalMaterialModels :
                    Filterarraylst) {


                EditText Quantity;
                final TextView Total;

                TableRow trm = (TableRow) LayoutInflater.from(activity).inflate(R.layout.materialorderfinal, null);
                TextView item = (TextView) trm.findViewById(R.id.txt_finalitem);

                TextView Rate = (TextView) trm.findViewById(R.id.txt_rate);

                Quantity = (EditText) trm.findViewById(R.id.edit_quantity);
                Total = (TextView) trm.findViewById(R.id.txt_total);

                if(!InputUtils.isNull( finalMaterialModels.getMaterialDetailsModel().getUnitSize())){
                    item.setText(finalMaterialModels.getMaterialDetailsModel().getMaterialName() + " (" + finalMaterialModels.getMaterialDetailsModel().getUnitSize() + ")");
                }else{
                    item.setText(finalMaterialModels.getMaterialDetailsModel().getMaterialName());
                }


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
                            materialOrderDataModel.setItem_name(finalMaterialModels.getMaterialDetailsModel().getMaterialName());
                            materialOrderDataModel.setItem_UnitCost(finalMaterialModels.getMaterialDetailsModel().getUnitCost());
                            materialOrderDataModel.setItem_UnitSize(finalMaterialModels.getMaterialDetailsModel().getUnitSize());


                            if (materialsOrderArr.contains(materialOrderDataModel)) {
                                materialsOrderArr.remove(materialOrderDataModel);
                            }
                            materialsOrderArr.add(materialOrderDataModel);
                        } else {

                            Total.setText(0 + "");
                            MaterialOrderDataModel materialOrderDataModel = new MaterialOrderDataModel();
                            materialOrderDataModel.setMaterialId(finalMaterialModels.getMaterialDetailsModel().getMaterialId());
                            if (materialsOrderArr.contains(materialOrderDataModel)) {
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
        try {
            String filterString = constraint.toUpperCase();

            ArrayList<FinalMaterialModel> list = finalMaterialModelsArr;
            int count = 0;
            if (list != null) {
                count = list.size();
            } else {
                count = 0;
            }
            ArrayList<FinalMaterialModel> nlist = new ArrayList<FinalMaterialModel>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getMaterialDetailsModel().getMaterialName();
                if (filterableString.toUpperCase().contains(filterString) || InputUtils.isNull(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            Filterarraylst = nlist;
            materialordertable.removeAllViews();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}