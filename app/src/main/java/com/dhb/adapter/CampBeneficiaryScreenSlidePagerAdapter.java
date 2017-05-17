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
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.CampAllOrderDetailsModel;
import com.dhb.models.data.CampDetailsBenMasterModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.models.data.OrderVisitDetailsModel;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

public class CampBeneficiaryScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<CampDetailsBenMasterModel> beneficiaryDetailsArr;
    private ArrayList<CampAllOrderDetailsModel> allOrderDetailsModels;
    private Context context;
    private RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate;
    public CampBeneficiaryScreenSlidePagerAdapter(FragmentManager fm, Context context, ArrayList<CampDetailsBenMasterModel> beneficiaryDetailsArr, ArrayList<CampAllOrderDetailsModel> orderDetailsModelsArr, RefreshCampBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate) {
        super(fm);
        this.context = context;
        this.beneficiaryDetailsArr = beneficiaryDetailsArr;
        this.allOrderDetailsModels = orderDetailsModelsArr;
        this.refreshBeneficiariesSliderDelegate = refreshBeneficiariesSliderDelegate;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        CampAllOrderDetailsModel campAllOrderDetailsModel = new CampAllOrderDetailsModel();
        for (CampAllOrderDetailsModel orderDetails:
                allOrderDetailsModels) {
            if(orderDetails.getOrderNo().equals(beneficiaryDetailsArr.get(position).getName())){
                campAllOrderDetailsModel = orderDetails;
                break;
            }
        }
        bundle.putParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsArr.get(position));
        bundle.putParcelable(BundleConstants.ORDER_DETAILS_MODEL,campAllOrderDetailsModel);
      /* return CampBeneficiaryDetailsScanBarcodeFragment.newInstance(bundle, new RefreshBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(OrderVisitDetailsModel orderVisitDetailsModel) {
               //refreshBeneficiariesSliderDelegate.onRefreshActionCallbackReceived(orderVisitDetailsModel);
            }
        });*/
      return  CampBeneficiaryDetailsScanBarcodeFragment.newInstance(bundle, new RefreshCampBeneficiariesSliderDelegate() {
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