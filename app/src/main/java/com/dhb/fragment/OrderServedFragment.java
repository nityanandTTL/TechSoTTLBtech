package com.dhb.fragment;

/**
 * Created by E4904 on 6/14/2017.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;


/**
 * Created by E4904 on 5/2/2017.
 */

public class OrderServedFragment extends AbstractFragment {


    public static final String TAG_FRAGMENT = "ORDER_FRAGMENT";
    HomeScreenActivity activity;
    AppPreferenceManager appPreferenceManager;
    private View rootView;


    public OrderServedFragment() {
        // Required empty public constructor
    }

    public static OrderServedFragment newInstance() {
        OrderServedFragment fragment = new OrderServedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Order Served");
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_served, container, false);
        initUI();
        setListners();
        initData();
        return rootView;
    }

    @Override
    public void initUI() {
        super.initUI();
    }

    private void setListners() {
    }


    private void initData() {
    }
}




