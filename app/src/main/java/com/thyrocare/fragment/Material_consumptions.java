package com.thyrocare.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.fragment.LME.LME_WLMISFragment;
import com.thyrocare.models.api.response.MaterialINVResponseModel;
import com.thyrocare.models.data.BTMaterialsModel;
import com.thyrocare.models.data.FinalMaterialModel;
import com.thyrocare.models.data.MaterialDetailsModel;
import com.thyrocare.models.data.MaterialOrderDataModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import java.util.ArrayList;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;


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
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getContext());
        ApiCallAsyncTask fetchMaterialDetailApiAsyncTask = asyncTaskForRequest.getMaterialsDetailsRequestAsyncTask(Category);
        fetchMaterialDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchMaterialsDetailsApiAsyncTaskDelegateResult2());
        if (isNetworkAvailable(getContext())) {
            fetchMaterialDetailApiAsyncTask.execute(fetchMaterialDetailApiAsyncTask);
        } else {
            Toast.makeText(getContext(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }


    }

    private class FetchMaterialsDetailsApiAsyncTaskDelegateResult2 implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(getContext());
                materialDetailsModels = new ArrayList<>();

                materialDetailsModels = responseParser.getMaterialdetailsResponseModel(json, statusCode);
                if (materialDetailsModels != null && materialDetailsModels.size() > 0) {
                    // Toast.makeText(activity, "MaterialdetailsResponseModel not null", Toast.LENGTH_SHORT).show();
                }
                fetchMaterialsINV2();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void fetchMaterialsINV2() {
        Logger.error(TAG_FRAGMENT + "--fetchData: ");
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(getContext());
        ApiCallAsyncTask fetchMaterialINVApiAsyncTask = asyncTaskForRequest.getMaterialINVDetailsRequestAsyncTask();
        fetchMaterialINVApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchMaterialsINVApiAsyncTaskDelegateResult2());
        if (isNetworkAvailable(getContext())) {
            fetchMaterialINVApiAsyncTask.execute(fetchMaterialINVApiAsyncTask);
        } else {
            Toast.makeText(getContext(), R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchMaterialsINVApiAsyncTaskDelegateResult2 implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            Logger.debug(TAG_FRAGMENT + "--apiCallResult: ");
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(getContext());
                MaterialINVResponseModel materialINVDetailsResponseModel = new MaterialINVResponseModel();

                materialINVDetailsResponseModel = responseParser.getMaterialINVDetailsResponseModel(json, statusCode);
                materialINVResponseModel = materialINVDetailsResponseModel;
                fetchData();
            }
        }

        @Override
        public void onApiCancelled() {
            Logger.error(TAG_FRAGMENT + "onApiCancelled: ");
            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
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
