package com.dhb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dhb.fragment.BeneficiaryDetailsScanBarcodeFragment;
import com.dhb.models.data.BeneficiaryDetailsModel;
import com.dhb.utils.app.BundleConstants;

import java.util.ArrayList;

public class BeneficiaryScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr;
    private Context context;
    public BeneficiaryScreenSlidePagerAdapter(FragmentManager fm, Context context, ArrayList<BeneficiaryDetailsModel> beneficiaryDetailsArr) {
        super(fm);
        this.context = context;
        this.beneficiaryDetailsArr = beneficiaryDetailsArr;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BundleConstants.BENEFICIARY_DETAILS_MODEL,beneficiaryDetailsArr.get(position));
        return BeneficiaryDetailsScanBarcodeFragment.newInstance(bundle);
    }

    @Override
    public int getCount() {
        return beneficiaryDetailsArr.size();
    }

}