package com.thyrocare.btechapp.BtechInterfaces;

import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.PEQrCodeResponse;
import com.thyrocare.btechapp.models.api.response.PEVerifyQRResponseModel;
import com.thyrocare.btechapp.models.api.response.TCVerifyCouponResponseModel;
import com.thyrocare.btechapp.models.api.response.UpdatePaymentResponseModel;

public class AppInterfaces {
    public interface getClickedPECoupon {
        void onApplyClick(String coupon);

        void onTCCouponVerification(CouponCodeResponseModel.Coupons verifyTcCoupon);
    }

    public interface getVerifyCoupon {
        void OnTCCouponResponse(TCVerifyCouponResponseModel model);
    }

    public interface getPEQRApiResponse {
        void onResponse(PEQrCodeResponse response);
    }

    public interface getPEQRVerifyApiResponse {
        void onResponse(PEVerifyQRResponseModel response);
    }

    public interface getUpdatePaymentResponse {
        void onResponse(UpdatePaymentResponseModel response);
    }

}
