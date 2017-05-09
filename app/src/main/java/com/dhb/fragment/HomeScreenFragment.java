package com.dhb.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.customview.RoundedImageView;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.InputUtils;

public class HomeScreenFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtUserName;
    private RoundedImageView rvSelfie;
    private ImageView imgPayment, imgOrders,imgSchedule, imgMaterials, imgOLCPickup, imgHub,imgCamp,imgCommunication,imgFeedback;
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
        rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        initUI();
        initData();
        initListeners();
        return rootView;
    }

    private void initData() {
        txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
        if(appPreferenceManager.getSelfieResponseModel()!=null && !InputUtils.isNull(appPreferenceManager.getSelfieResponseModel().getPic())) {
            rvSelfie.setImageBitmap(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));
        }
    }

    private void initListeners() {
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(DispatchToHubFragment.newInstance(),false,false, DispatchToHubFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LedgerDisplayFragment.newInstance(),false,false,LedgerDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(ScheduleYourDayFragment.newInstance(),false,false,ScheduleYourDayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(MaterialFragment.newInstance(),false,false,MaterialFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        /*
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });*/
    }

    @Override
    public void initUI() {
        super.initUI();
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        rvSelfie = (RoundedImageView) rootView.findViewById(R.id.img_user_picture);
        imgPayment = (ImageView) rootView.findViewById(R.id.payment_icon);
        imgOrders = (ImageView) rootView.findViewById(R.id.orders_booked);
        imgSchedule = (ImageView) rootView.findViewById(R.id.schedule_icon);
        imgHub = (ImageView) rootView.findViewById(R.id.hub_icon);
        imgOLCPickup = (ImageView) rootView.findViewById(R.id.olc_pickup_icon);
        imgCommunication = (ImageView) rootView.findViewById(R.id.communication_icon);
        imgFeedback= (ImageView) rootView.findViewById(R.id.feedback_icon);
        imgCamp = (ImageView) rootView.findViewById(R.id.camp_icon);
        imgMaterials = (ImageView) rootView.findViewById(R.id.materials_icon);
    }
}
