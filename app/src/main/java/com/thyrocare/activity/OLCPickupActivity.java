package com.thyrocare.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.fragment.OLCMasterBarcodeScanFragment;
import com.thyrocare.models.data.BtechClientsModel;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

/**
 * Created by Orion on 5/2/2017.
 */

public class OLCPickupActivity extends AbstractActivity {
    public final static String TAG_ACTIIVTY = OLCPickupActivity.class.getSimpleName();
    private OLCPickupActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private FrameLayout flOlcPickup;
    private TextView txtHeaderText;
    private ImageView imgBack;
    private Toolbar tbOBA;
    private BtechClientsModel btechClientsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olc_picup_screen);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        btechClientsModel = getIntent().getExtras().getParcelable(BundleConstants.BTECH_CLIENTS_MODEL);
        initUI();
        initListeners();
        pushFragments(OLCMasterBarcodeScanFragment.newInstance(btechClientsModel),false,false, OLCMasterBarcodeScanFragment.TAG_FRAGMENT,R.id.fl_olc_pickup,TAG_ACTIIVTY);
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
        flOlcPickup = (FrameLayout) findViewById(R.id.fl_olc_pickup);
        tbOBA = (Toolbar) findViewById(R.id.toolbar);
        txtHeaderText = (TextView) tbOBA.findViewById(R.id.txt_header_text);
        imgBack = (ImageView) tbOBA.findViewById(R.id.img_back);


    }
}
