package com.dhb.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhb.R;
import com.dhb.activity.OrderBookingActivity;
import com.dhb.adapter.BeneficiaryScreenSlidePagerAdapter;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

public class BeneficiariesDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "BENEFICIARIES_DISPLAY_FRAGMENT";
    private OrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ViewPager vpBeneficiaries;
    private TextView txtAmtPayable,txtAddBeneficiary;
    private Button btnProceedPayment;
    private OrderVisitDetailsModel orderVisitDetailsModel;
    private int totalAmount = 0;
    private int dotsCount;
    private ImageView[] dots;
    private BeneficiaryScreenSlidePagerAdapter beneficiaryScreenSlidePagerAdapter;
    private LinearLayout pagerIndicator;

    public BeneficiariesDisplayFragment() {
        // Required empty public constructor
    }

    public static BeneficiariesDisplayFragment newInstance(OrderVisitDetailsModel orderVisitDetailsModel) {
        BeneficiariesDisplayFragment fragment = new BeneficiariesDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL,orderVisitDetailsModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (OrderBookingActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {
            this.orderVisitDetailsModel = getArguments().getParcelable(BundleConstants.VISIT_ORDER_DETAILS_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_beneficiaries_display, container, false);
        initUI();
        initData();
        return rootView;
    }

    private void initData() {
        for (OrderDetailsModel orderDetailsModel :
                orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmount = totalAmount + orderDetailsModel.getAmountDue();
        }
        txtAmtPayable.setText(totalAmount+"");
        ArrayList<BeneficiaryDetailsModel> beneficiariesArr = new ArrayList<>();
        for (OrderDetailsModel orderDetailsModel :
                orderVisitDetailsModel.getAllOrderdetails()) {
            for (BeneficiaryDetailsModel beneficiaryDetailsModel :
                    orderDetailsModel.getBenMaster()) {
                beneficiariesArr.add(beneficiaryDetailsModel);
            }
        }
        beneficiaryScreenSlidePagerAdapter = new BeneficiaryScreenSlidePagerAdapter(getFragmentManager(),activity,beneficiariesArr,orderVisitDetailsModel.getAllOrderdetails());
        vpBeneficiaries.setAdapter(beneficiaryScreenSlidePagerAdapter);
        vpBeneficiaries.setCurrentItem(0);
        vpBeneficiaries.setOnPageChangeListener(new BeneficiaryScreenPageChangeListener());
        setUiPageViewController();
    }

    @Override
    public void initUI() {
        super.initUI();
        vpBeneficiaries = (ViewPager) rootView.findViewById(R.id.vp_beneficiaries);
        btnProceedPayment = (Button) rootView.findViewById(R.id.btn_proceed_payment);
        txtAddBeneficiary = (TextView) rootView.findViewById(R.id.title_add_beneficiary);
        txtAmtPayable = (TextView) rootView.findViewById(R.id.title_amt_payable);
        pagerIndicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);
    }

    private void setUiPageViewController() {

        dotsCount = beneficiaryScreenSlidePagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(activity);
            dots[i].setImageDrawable(activity.getResources().getDrawable(R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pagerIndicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
    }

    private class BeneficiaryScreenPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(activity.getResources().getDrawable(R.drawable.non_selected_item_dot));
            }
            dots[position].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
