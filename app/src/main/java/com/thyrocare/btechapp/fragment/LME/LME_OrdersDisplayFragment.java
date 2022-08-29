package com.thyrocare.btechapp.fragment.LME;


import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.thyrocare.btechapp.Controller.TSPLMESampleDropController;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.activity.LMEMapDisplayFragmentActivity;

import com.thyrocare.btechapp.adapter.LME.LMEVisitsListAdapter;

import application.ApplicationController;

import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.uiutils.AbstractFragment;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;

import java.util.ArrayList;

/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class LME_OrdersDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LME_OrdersDisplayFragment";
    static LME_OrdersDisplayFragment fragment;
    EditText edt_search;
    TextView txt_nodata;
    LMEVisitsListAdapter adaptor;
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private RecyclerView recy_Orderlist;
    private SampleDropDetailsbyTSPLMEDetailsModel msampleDropDetailsbyTSPLMEDetailsModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public LME_OrdersDisplayFragment() {
        // Required empty public constructor
    }

    public static LME_OrdersDisplayFragment newInstance() {
        fragment = new LME_OrdersDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Visit Orders");
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lme_orders_display, container, false);
        initUI();
        fetchData();
        return rootView;
    }

    @Override
    public void initUI() {
        recy_Orderlist = (RecyclerView) rootView.findViewById(R.id.recy_Orderlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recy_Orderlist.setLayoutManager(mLayoutManager);
        recy_Orderlist.setItemAnimator(new DefaultItemAnimator());
        txt_nodata = (TextView) rootView.findViewById(R.id.txt_nodata);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adaptor != null) {
                    adaptor.filter(s.toString());
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_visit_orders_display);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                fetchData();
            }
        });
    }

    private void fetchData() {
        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }

        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallGetSampleDropDetailsbyTSPLME(appPreferenceManager.getLoginResponseModel().getUserID(), BundleConstants.batch_code);
    }

    public void SetOrdersList(ArrayList<SampleDropDetailsbyTSPLMEDetailsModel> materialDetailsModels) {
        if (materialDetailsModels.size() != 0) {
            adaptor = new LMEVisitsListAdapter(materialDetailsModels, fragment);
            recy_Orderlist.setAdapter(adaptor);
            recy_Orderlist.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
        } else {
            NodataFound();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    public void NodataFound() {
        swipeRefreshLayout.setRefreshing(false);
        recy_Orderlist.setVisibility(View.GONE);
        txt_nodata.setVisibility(View.VISIBLE);
    }

    public void StartEndButtonClicked(final SampleDropDetailsbyTSPLMEDetailsModel sampleDropDetailsbyTSPLMEDetailsModel) {
        msampleDropDetailsbyTSPLMEDetailsModel = sampleDropDetailsbyTSPLMEDetailsModel;
        BundleConstants.setsampleDropDetailsModel = sampleDropDetailsbyTSPLMEDetailsModel;
        Intent intentMapDisplay = new Intent(activity, LMEMapDisplayFragmentActivity.class);
        startActivityForResult(intentMapDisplay, BundleConstants.LME_START);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleConstants.LME_START && resultCode == BundleConstants.LME_ARRIVED) {
            try {
                activity.getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void StartButtonClickedSuccess() {

        //ToDo Map start arrived arrived

        pushFragments(LMEMasterBarcodeScanFragment.newInstance(msampleDropDetailsbyTSPLMEDetailsModel), false, false, LMEMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
    }
}
