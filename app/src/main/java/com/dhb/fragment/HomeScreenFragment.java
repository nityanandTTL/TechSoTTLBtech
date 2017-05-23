package com.dhb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.PaymentsActivity;
import com.dhb.customview.RoundedImageView;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreenFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtUserName,txt_no_of_camps;
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
        getCampDetailCount();
        initListeners();
        return rootView;
    }

    private void getCampDetailCount() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask campDetailCountApiAsyncTask = asyncTaskForRequest.getCampDetailsCountRequestAsyncTask();
        campDetailCountApiAsyncTask.setApiCallAsyncTaskDelegate(new CampDetailsCountApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            campDetailCountApiAsyncTask.execute(campDetailCountApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
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
                pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false, VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(HubListDisplayFragment.newInstance(),false,false, HubListDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
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
        imgOLCPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(OLCPickupListDisplayFragment.newInstance(),false,false, OLCPickupListDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
         imgCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(CampListDisplayFragment.newInstance(),false,false,CampListDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_FRAGMENT);
            }
        });
        imgCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPaymentsActivity = new Intent(activity, PaymentsActivity.class);
                startActivity(intentPaymentsActivity);
            }
        });
        /*imgOrders.setOnClickListener(new View.OnClickListener() {
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
        imgOLCPickup=(ImageView)rootView.findViewById(R.id.olc_pickup_icon);
        txt_no_of_camps=(TextView)rootView.findViewById(R.id.txt_no_of_camps);
    }

    private class CampDetailsCountApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode==200){
                JSONObject jsonObject=new JSONObject(json);
                txt_no_of_camps.setText(""+jsonObject.getString("CampCount"));
            }
            else {
                Toast.makeText(activity, ""+json, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
