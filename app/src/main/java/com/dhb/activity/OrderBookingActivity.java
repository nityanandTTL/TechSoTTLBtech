package com.dhb.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.dao.DhbDao;
import com.dhb.fragment.BeneficiariesDisplayFragment;
import com.dhb.models.data.BeneficiaryBarcodeDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

import static com.dhb.activity.HomeScreenActivity.TAG_ACTIVITY;

/**
 * Created by Orion on 5/2/2017.
 */

public class OrderBookingActivity extends AbstractActivity {
    public final static String TAG_ACTIIVTY = "ORDER_BOOKING_ACTIVITY";
    private OrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private FrameLayout flOrderBooking;
    private TextView txtHeaderText;
    private ImageView imgBack;
    private Toolbar tbOBA;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    public ArrayList<BeneficiaryBarcodeDetailsModel> scannedBarcodes = new ArrayList<>();

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Confirm Action")
                .setMessage("All changes made by you will get reset, do you want confirm.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderBookingActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_booking_screen);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(activity);
        dhbDao = new DhbDao(activity);
        orderVisitDetailsModel = getIntent().getExtras().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        initUI();
        initListeners();
        pushFragments(BeneficiariesDisplayFragment.newInstance(orderVisitDetailsModel),false,false,BeneficiariesDisplayFragment.TAG_FRAGMENT,R.id.fl_order_booking,TAG_ACTIVITY);
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
