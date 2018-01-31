package com.thyrocare.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.thyrocare.R;
import com.thyrocare.fragment.CampManualWOEFragment;
import com.thyrocare.models.data.CampDetailModel;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

/**
 * Created by Orion on 5/2/2017.
 */

public class CampOrderBookingActivity extends AbstractActivity {
    public static final String TAG_ACTIVITY = "CAMP_ORDER_BOOKING_ACTIVITY";
    private CampOrderBookingActivity activity;
    private ImageView imgBack;
    private Toolbar tbOBA;
    CampDetailModel campDetailModel=new CampDetailModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_order_booking);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        if(getIntent().getExtras()!=null){
            campDetailModel=getIntent().getExtras().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        }

        initUI();
        initListeners();
        pushFragments(CampManualWOEFragment.newInstance(campDetailModel),false,false,CampManualWOEFragment.TAG_FRAGMENT,R.id.fl_camp_order_booking,TAG_ACTIVITY);
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
        tbOBA = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) tbOBA.findViewById(R.id.img_back);
    }
}
