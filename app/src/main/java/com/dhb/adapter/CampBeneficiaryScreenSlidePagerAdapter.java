package com.dhb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhb.delegate.RefreshBeneficiariesSliderDelegate;
import com.dhb.delegate.RefreshCampBeneficiariesSliderDelegate;
import com.dhb.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.dhb.fragment.CampBeneficiaryDetailsScanBarcodeFragment;
import com.dhb.models.api.response.CampListDisplayResponseModel;
import com.dhb.models.api.response.CampScanQRResponseModel;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.CampAllOrderDetailsModel;
import com.dhb.models.data.CampDetailModel;
import com.dhb.models.data.CampDetailsBenMasterModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

public class CampBeneficiaryScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<CampDetailsBenMasterModel> beneficiaryDetailsArr;
    private ArrayList<CampAllOrderDetailsModel> allOrderDetailsModels;
    private Context context;
    private CampScanQRResponseModel campScanQRResponseModel;
    private RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate;
    private CampDetailModel campDetailModel = new CampDetailModel();

    public CampBeneficiaryScreenSlidePagerAdapter(FragmentManager fm, Context context, CampDetailModel campDetailModel, ArrayList<CampDetailsBenMasterModel> beneficiaryDetailsArr, ArrayList<CampAllOrderDetailsModel> orderDetailsModelsArr, CampScanQRResponseModel campScanQRResponseModel, RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate) {
        super(fm);
        this.context = context;
        this.campDetailModel = campDetailModel;
        this.beneficiaryDetailsArr = beneficiaryDetailsArr;
        this.allOrderDetailsModels = orderDetailsModelsArr;
        this.campScanQRResponseModel = campScanQRResponseModel;
        this.refreshBeneficiariesSliderDelegate = refreshBeneficiariesSliderDelegate;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        CampAllOrderDetailsModel campAllOrderDetailsModel = new CampAllOrderDetailsModel();
        for (CampAllOrderDetailsModel orderDetails :
                allOrderDetailsModels) {
            if (orderDetails.getOrderNo().equals(beneficiaryDetailsArr.get(position).getOrderNo())) {
                campAllOrderDetailsModel = orderDetails;
                break;
            }
        }
        bundle.putParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL, beneficiaryDetailsArr.get(position));
        bundle.putParcelable(BundleConstants.CAMP_ALL_ORDER_DETAIL, campAllOrderDetailsModel);
        bundle.putParcelable(BundleConstants.CAMP_ORDER_DETAILS_MODEL, campDetailModel);
        bundle.putParcelable(BundleConstants.CAMP_SCAN_OR_RESPONSE_MODEL, campScanQRResponseModel);
        return CampBeneficiaryDetailsScanBarcodeFragment.newInstance(bundle, new RefreshCampBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(CampListDisplayResponseModel campListDisplayResponseModel) {

            }
        });
    }

    @Override
    public int getCount() {
        return beneficiaryDetailsArr.size();
    }

}