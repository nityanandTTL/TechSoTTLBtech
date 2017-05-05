package com.dhb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhb.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.models.data.OrderDetailsModel;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

public class BeneficiaryScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr;
    private ArrayList<OrderDetailsModel> orderDetailsModelsArr;
    private Context context;
    public BeneficiaryScreenSlidePagerAdapter(FragmentManager fm, Context context, ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr,ArrayList<OrderDetailsModel> orderDetailsModelsArr) {
        super(fm);
        this.context = context;
        this.beneficiaryDetailsArr = beneficiaryDetailsArr;
        this.orderDetailsModelsArr = orderDetailsModelsArr;
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
        return BeneficiaryDetailsScanBarcodeFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return beneficiaryDetailsArr.size();
    }

}