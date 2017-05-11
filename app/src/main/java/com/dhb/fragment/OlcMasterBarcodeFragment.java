package com.dhb.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.dhb.activity.OlcPickupActivity;
import com.dhb.adapter.OlcMasterBarcodeScanAdapter;
import com.dhb.delegate.OlcMasterBarcodeScanAdapterDelegate;
import com.dhb.models.data.BtechClientsModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.BundleConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class OlcMasterBarcodeFragment extends Fragment {
    public static final String TAG_FRAGMENT = OlcMasterBarcodeFragment.class.getSimpleName();
    private TextView txt_name, tv_distance, txt_call;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private OlcPickupActivity activity;
    OlcMasterBarcodeScanAdapter mAdapter;
    ArrayList<String> olcMasterBarcodeList=new ArrayList<>();
    String scanned_barcode;
    BtechClientsModel btechClientsModel;
    int currentPosition = 0;
    IntentIntegrator integrator;
    public OlcMasterBarcodeFragment() {
        // Required empty public constructor
    }

    public static OlcMasterBarcodeFragment newInstance(BtechClientsModel btechClientsModel) {
        OlcMasterBarcodeFragment fragment = new OlcMasterBarcodeFragment();
        Bundle args = new Bundle();
        args.putParcelable("btechClientsModel",btechClientsModel);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_olc_master_barcode, container, false);
        initUi(view);
        activity = (OlcPickupActivity) getActivity();

        integrator = new IntentIntegrator(activity);
      //  btechClientsModel = getArguments().getParcelable(BundleConstants.BTECH_CLIENTS_MODEL);
        prepareRecyclerView();
        prepareData();
        setListeners();
        return view;
    }

    private void setListeners() {
        txt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +btechClientsModel.getMobile()));
                startActivity(intent);
            }
        });
    }

    private void prepareData() {
        btechClientsModel = getArguments().getParcelable("btechClientsModel");
        tv_distance.setText(""+btechClientsModel.getDistance());
        txt_name.setText(""+btechClientsModel.getName());
        olcMasterBarcodeList.add(0,"");
        mAdapter.notifyDataSetChanged();
    }

    private void prepareRecyclerView() {
        mAdapter = new OlcMasterBarcodeScanAdapter(olcMasterBarcodeList, activity, new OlcMasterBarcodeScanAdapterDelegate() {
            @Override
            public void onScanClick(int position) {
                Logger.error("item clicked");
                currentPosition = position;
               integrator = new IntentIntegrator(getActivity()) {
                    @Override
                    protected void startActivityForResult(Intent intent, int code) {
                        OlcMasterBarcodeFragment.this.startActivityForResult(intent, BundleConstants.START_BARCODE_SCAN); // REQUEST_CODE override

                    }
                };
                integrator.initiateScan();
            }

            @Override
            public void onAddClick(int position) {
                olcMasterBarcodeList.add(position+1,"");
                mAdapter.notifyDataSetChanged();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast.makeText(activity, "scanned res "+scanningResult, Toast.LENGTH_SHORT).show();
        if (scanningResult != null && scanningResult.getContents() != null) {
            scanned_barcode = scanningResult.getContents();
            Logger.error("scanned_barcode "+scanningResult.getContents());
            Toast.makeText(activity, ""+scanningResult, Toast.LENGTH_SHORT).show();
            olcMasterBarcodeList.set(currentPosition,scanned_barcode);
            mAdapter.notifyDataSetChanged();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(activity, "no result", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }


    private void initUi(View view) {
        txt_name = (TextView) view.findViewById(R.id.txt_name);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        txt_call = (TextView) view.findViewById(R.id.txt_call);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

}
