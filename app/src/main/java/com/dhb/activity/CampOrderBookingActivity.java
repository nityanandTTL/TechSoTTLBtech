package com.dhb.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.fragment.CampManualWOEFragment;
import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.data.CampDetailModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import static com.dhb.activity.HomeScreenActivity.TAG_ACTIVITY;

/**
 * Created by ISRO on 5/2/2017.
 */

public class CampOrderBookingActivity extends AbstractActivity {
    public final static String TAG_ACTIIVTY = CampOrderBookingActivity.class.getSimpleName();
    private CampOrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private FrameLayout flOrderBooking;
    private TextView txtHeaderText;
    private ImageView imgBack;
    private Toolbar tbOBA;
    private CampScanQRResponseModel campScanQRResponseModel;//=new CampScanQRResponseModel();
    private CampListDisplayResponseModel campListDisplayResponseModel;
   CampDetailModel campDetailModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_order_booking);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        campScanQRResponseModel = getIntent().getExtras().getParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL);
        campListDisplayResponseModel=getIntent().getExtras().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        campDetailModels=getIntent().getExtras().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        initUI();
        initListeners();
        pushFragments(CampManualWOEFragment.newInstance(campScanQRResponseModel,campListDisplayResponseModel,campDetailModels),false,false,CampManualWOEFragment.TAG_FRAGMENT,R.id.fl_camp_order_booking,TAG_ACTIVITY);
    }

    private void initListeners() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void initUI() {
        super.initUI();
        flOrderBooking = (FrameLayout) findViewById(R.id.fl_order_booking);
        tbOBA = (Toolbar) findViewById(R.id.toolbar);
        txtHeaderText = (TextView) tbOBA.findViewById(R.id.txt_header_text);
        imgBack = (ImageView) tbOBA.findViewById(R.id.img_back);
    }
}
