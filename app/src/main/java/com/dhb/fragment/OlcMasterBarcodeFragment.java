package com.dhb.fragment;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.adapter.OlcMasterBarcodeScanAdapter;
import com.dhb.delegate.OlcMasterBarcodeScanAdapterDelegate;
import com.dhb.models.data.BarcodeDetailsModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.InputUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class OlcMasterBarcodeFragment extends Fragment {
    public static final String TAG_FRAGMENT = OlcMasterBarcodeFragment.class.getSimpleName();
    private TextView txt_name, tv_distance, txt_call;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private HomeScreenActivity activity;
    OlcMasterBarcodeScanAdapter mAdapter;
    ArrayList<String> olcMasterBarcodeList=new ArrayList<>();
    String scanned_barcode;
    public OlcMasterBarcodeFragment() {
        // Required empty public constructor
    }

    public static OlcMasterBarcodeFragment newInstance() {
        OlcMasterBarcodeFragment fragment = new OlcMasterBarcodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_olc_master_barcode, container, false);
        initUi(view);
        activity = (HomeScreenActivity) getActivity();
        prepareRecyclerView();
        prepareData();
        return view;
    }

    private void prepareData() {

        olcMasterBarcodeList.add(0," ");
        mAdapter.notifyDataSetChanged();
    }

    private void prepareRecyclerView() {
        mAdapter = new OlcMasterBarcodeScanAdapter(olcMasterBarcodeList, activity, new OlcMasterBarcodeScanAdapterDelegate() {
            @Override
            public void onItemClick(int position) {
                Logger.error("item clicked");
                new IntentIntegrator(activity).initiateScan();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null && scanningResult.getContents() != null) {
             scanned_barcode = scanningResult.getContents();
            Toast.makeText(activity, ""+scanningResult, Toast.LENGTH_SHORT).show();
        }
    }

    private void initUi(View view) {
        txt_name = (TextView) view.findViewById(R.id.txt_name);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        txt_call = (TextView) view.findViewById(R.id.txt_call);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

}
