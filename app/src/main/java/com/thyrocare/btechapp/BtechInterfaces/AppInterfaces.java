package com.thyrocare.btechapp.BtechInterfaces;

import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.TCVerifyCouponResponseModel;

public class AppInterfaces {
    public interface getClickedPECoupon {
        void onApplyClick( String coupon);
        void onTCCouponVerification(CouponCodeResponseModel.Coupons verifyTcCoupon);
    }
    public interface getVerifyCoupon{
        void OnTCCouponResponse(TCVerifyCouponResponseModel model);
    }

}
