package com.thyrocare.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thyrocare.delegate.RefreshBeneficiariesSliderDelegate;
import com.thyrocare.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.thyrocare.models.data.BeneficiaryDetailsModel;
import com.thyrocare.models.data.OrderDetailsModel;
import com.thyrocare.models.data.OrderVisitDetailsModel;
import com.thyrocare.utils.app.BundleConstants;

import java.util.ArrayList;

public class BeneficiaryScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr;
    private ArrayList<OrderDetailsModel> orderDetailsModelsArr;
    private Context context;
    private RefreshBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate;
    public BeneficiaryScreenSlidePagerAdapter(FragmentManager fm, Context context, ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr,ArrayList<OrderDetailsModel> orderDetailsModelsArr,RefreshBeneficiariesSliderDelegate refreshBeneficiariesSliderDelegate) {
        super(fm);
        this.context = context;
        this.beneficiaryDetailsArr = beneficiaryDetailsArr;
        this.orderDetailsModelsArr = orderDetailsModelsArr;
        this.refreshBeneficiariesSliderDelegate = refreshBeneficiariesSliderDelegate;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
        for (OrderDetailsModel orderDetails:
             orderDetailsModelsArr) {
            if(orderDetails.getOrderNo().equals(beneficiaryDetailsArr.get(position).getOrderNo())){
                orderDetailsModel = orderDetails;
                break;
            }
        }
        bundle.putParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsArr.get(position));
        bundle.putParcelable(BundleConstants.ORDER_DETAILS_MODEL,orderDetailsModel);
        return BeneficiaryDetailsScanBarcodeFragment.newInstance(bundle, new RefreshBeneficiariesSliderDelegate() {
            @Override
            public void onRefreshActionCallbackReceived(OrderVisitDetailsModel orderVisitDetailsModel) {
                refreshBeneficiariesSliderDelegate.onRefreshActionCallbackReceived(orderVisitDetailsModel);
            }
        });
    }

    @Override
    public int getCount() {
        return beneficiaryDetailsArr.size();
    }

}