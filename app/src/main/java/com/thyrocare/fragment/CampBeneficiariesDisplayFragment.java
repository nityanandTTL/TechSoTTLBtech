package com.thyrocare.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.R;
import com.thyrocare.activity.CampOrderBookingActivity;
import com.thyrocare.adapter.CampBeneficiaryScreenSlidePagerAdapter;
import com.thyrocare.delegate.RefreshCampBeneficiariesSliderDelegate;
import com.thyrocare.models.api.response.CampListDisplayResponseModel;
import com.thyrocare.models.api.response.CampScanQRResponseModel;
import com.thyrocare.models.data.CampAllOrderDetailsModel;
import com.thyrocare.models.data.CampDetailModel;
import com.thyrocare.models.data.CampDetailsBenMasterModel;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;

import java.util.ArrayList;

public class CampBeneficiariesDisplayFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = CampBeneficiariesDisplayFragment.class.getSimpleName();
    private CampOrderBookingActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private ViewPager vpBeneficiaries;
    private TextView txtAmtPayable, txtAddBeneficiary;
    private Button btnProceedPayment;
    private CampScanQRResponseModel campScanQRResponseModel;
    private CampAllOrderDetailsModel campAllOrderDetailsModel = new CampAllOrderDetailsModel();
    private int totalAmount = 0;
    private int dotsCount;
    private ImageView[] dots;
    private CampBeneficiaryScreenSlidePagerAdapter CampBeneficiaryScreenSlidePagerAdapter;
    private LinearLayout pagerIndicator;
    private CampDetailModel campDetailModel;

    public CampBeneficiariesDisplayFragment() {
        // Required empty public constructor
    }

    public static CampBeneficiariesDisplayFragment newInstance(CampScanQRResponseModel campScanQRResponseModel, CampDetailModel campDetailModel) {
        CampBeneficiariesDisplayFragment fragment = new CampBeneficiariesDisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL, campScanQRResponseModel);
        args.putParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (CampOrderBookingActivity) getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        if (getArguments() != null) {
            campScanQRResponseModel = getArguments().getParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL);
            campDetailModel = getArguments().getParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_camp_beneficiaries_display, container, false);
        initUI();
        initData();
        initListeners();
        return rootView;
    }

    private void initListeners() {
        txtAddBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Confirm Action")
                        .setMessage("Do you really want to add a new beneficiary?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*tempOrderDetailsModel.setOrderNo(DeviceUtils.randomString(8));
                        ArrayList<BeneficiaryDetailsModel> beneficiaries = new ArrayList<BeneficiaryDetailsModel>();

                        tempBeneficiaryDetailsModel = new BeneficiaryDetailsModel();
                        tempBeneficiaryDetailsModel.setOrderNo(tempOrderDetailsModel.getOrderNo());
                        tempBeneficiaryDetailsModel.setBenId((int) (Math.random() * 999));
                        beneficiaryDetailsDao.insertOrUpdate(tempBeneficiaryDetailsModel);

                        beneficiaries.add(tempBeneficiaryDetailsModel);

                        tempOrderDetailsModel.setBenMaster(beneficiaries);
                        orderDetailsDao.insertOrUpdate(tempOrderDetailsModel);
                        Intent intentEdit = new Intent(activity, AddEditBeneficiaryDetailsActivity.class);
                        intentEdit.putExtra(BundleConstants.BENEFICIARY_DETAILS_MODEL, tempBeneficiaryDetailsModel);
                        intentEdit.putExtra(BundleConstants.ORDER_DETAILS_MODEL, tempOrderDetailsModel);
                        startActivityForResult(intentEdit, BundleConstants.ADD_EDIT_START);*/
                    }
                }).show();
            }
        });
    }

    private void initData() {
        /*for (OrderDetailsModel orderDetailsModel :
                orderVisitDetailsModel.getAllOrderdetails()) {
            totalAmount = totalAmount + orderDetailsModel.getAmountDue();
        }
*/
        //  txtAmtPayable.setText(totalAmount + "");
        ArrayList<CampDetailsBenMasterModel> beneficiariesArr = new ArrayList<>();
        for (CampAllOrderDetailsModel orderDetailsModel : campScanQRResponseModel.getAllOrderdetails()) {
            Logger.error(orderDetailsModel.getBenMaster().size() + "");
            for (CampDetailsBenMasterModel beneficiaryDetailsModel : orderDetailsModel.getBenMaster()) {
                beneficiariesArr.add(beneficiaryDetailsModel);
            }
        }
        CampBeneficiaryScreenSlidePagerAdapter = new CampBeneficiaryScreenSlidePagerAdapter(getFragmentManager(), activity, campDetailModel, beneficiariesArr, campScanQRResponseModel.getAllOrderdetails(), campScanQRResponseModel, new RefreshCampBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(CampListDisplayResponseModel campListDisplayResponseModel) {

            }
        });

        vpBeneficiaries.setAdapter(CampBeneficiaryScreenSlidePagerAdapter);
        vpBeneficiaries.setCurrentItem(0);
        vpBeneficiaries.setOnPageChangeListener(new BeneficiaryScreenPageChangeListener());
        setUiPageViewController();

        if (campScanQRResponseModel.getAllOrderdetails().size() > 0) {
            for (CampAllOrderDetailsModel orderDetailsModel :
                    campScanQRResponseModel.getAllOrderdetails()) {
                campAllOrderDetailsModel = orderDetailsModel;
                break;
            }
        }
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

        dotsCount = CampBeneficiaryScreenSlidePagerAdapter.getCount();
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
        if (dots != null && dots.length > 0) {
            dots[0].setImageDrawable(activity.getResources().getDrawable(R.drawable.selected_item_dot));
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BundleConstants.ADD_START && resultCode == BundleConstants.ADD_FINISH) {
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
