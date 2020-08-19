package com.thyrocare.btechapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.models.api.response.MaterialINVResponseModel;
import com.thyrocare.btechapp.models.data.BTMaterialsModel;
import com.thyrocare.btechapp.models.data.FinalMaterialModel;
import com.thyrocare.btechapp.models.data.MaterialDetailsModel;
import com.thyrocare.btechapp.models.data.MaterialOrderDataModel;



import com.thyrocare.btechapp.network.ResponseParser;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;


public class Material_consumptions extends Fragment {

    static Material_consumptions fragment;
    public static final String TAG_FRAGMENT = "Material_consumptions";
    Spinner select_type;
    private View rootView;
    private ArrayList<MaterialOrderDataModel> materialsOrderArr = new ArrayList<>();
    LinearLayout material_distribution, material_table, current;
    String Category = "204";
    EditText searchbar;
    private ArrayList<MaterialDetailsModel> materialDetailsModels;
    private MaterialINVResponseModel materialINVResponseModel;
    private OnFragmentInteractionListener mListener;
    private ArrayList<FinalMaterialModel> finalMaterialModelsArr;
    private ArrayList<FinalMaterialModel> Filterarraylst;
    TableLayout materialordertable;
    private Activity activity;
    private Global global;
    private AppPreferenceManager appPreferenceManager;

    public Material_consumptions() {
        // Required empty public constructor
    }

    public static Material_consumptions newInstance() {
        fragment = new Material_consumptions();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();
        global = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        rootView = inflater.inflate(R.layout.fragment_material_consumptions, container, false);

        materialordertable = (TableLayout) rootView.findViewById(R.id.materialordertable);
        select_type = (Spinner) rootView.findViewById(R.id.select_type);
        material_distribution = (LinearLayout) rootView.findViewById(R.id.material_distribution);
        material_table = (LinearLayout) rootView.findViewById(R.id.material_table);
        searchbar = (EditText) rootView.findViewById(R.id.searchbar);
        current = (LinearLayout) rootView.findViewById(R.id.current);

        select_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), select_type.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                if (select_type.getSelectedItem().toString().equalsIgnoreCase("Material Distribution(FOC)")) {
                    material_distribution.setVisibility(View.VISIBLE);
                    material_table.setVisibility(View.VISIBLE);
                    current.setVisibility(View.INVISIBLE);
                    fetchMaterialsDetails2();
                } else if (select_type.getSelectedItem().toString().equalsIgnoreCase("Material Receipt")) {
                    material_table.setVisibility(View.VISIBLE);
                    material_distribution.setVisibility(View.GONE);
                    current.setVisibility(View.INVISIBLE);
                    fetchMaterialsDetails2();
                } else if (select_type.getSelectedItem().toString().equals("Material Distribution(B2B)")) {
                    material_distribution.setVisibility(View.VISIBLE);
                    material_table.setVisibility(View.VISIBLE);
                    current.setVisibility(View.INVISIBLE);
                    fetchMaterialsDetails2();
                } else if (select_type.getSelectedItem().toString().equals("Current Stock")) {
                    material_distribution.setVisibility(View.INVISIBLE);
                    material_table.setVisibility(View.INVISIBLE);
                    current.setVisibility(View.VISIBLE);
                } else {
                    material_distribution.setVisibility(View.INVISIBLE);
                    material_table.setVisibility(View.INVISIBLE);
                    current.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void fetchMaterialsDetails2() {

        if (isNetworkAvailable(getContext())) {
            CallGetMaterialsDetailsApi(Category);
        } else {
            Toast.makeText(getContext(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
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
                    fetchMaterialsINV2();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<MaterialDetailsModel>> call, Throwable t) {
                global.hideProgressDialog();
                global.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }


    private void fetchMaterialsINV2() {
        if (isNetworkAvailable(getContext())) {
            CallGetMaterialINVDetailsApi();
        } else {
            Toast.makeText(getContext(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
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
                    fetchData();
                }
            }
            @Override
            public void onFailure(Call<MaterialINVResponseModel> call, Throwable t) {
                global.hideProgressDialog();
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


            for (final FinalMaterialModel finalMaterialModels :
                    Filterarraylst) {


                EditText Quantity;
                final TextView Total;

                TableRow trm = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.materialorderfinal, null);
                TextView item = (TextView) trm.findViewById(R.id.txt_finalitem);

                TextView Rate = (TextView) trm.findViewById(R.id.txt_rate);

                Quantity = (EditText) trm.findViewById(R.id.edit_quantity);
                Total = (TextView) trm.findViewById(R.id.txt_total);

                item.setText(finalMaterialModels.getMaterialDetailsModel().getMaterialName() + " (" + finalMaterialModels.getMaterialDetailsModel().getUnitSize() + ")");

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
                            String tot = String.format("%.2f", total);
                            Total.setText(tot + "");

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
                            Total.setText("");
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
