package com.dhb.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;

public class HomeScreenFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
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
        rootView = inflater.inflate(R.layout.fragment_schedule_your_day, container, false);
        initUI();
        initListeners();
        return rootView;
    }

    private void initListeners() {

    }

    @Override
    public void initUI() {
        super.initUI();
    }
}
