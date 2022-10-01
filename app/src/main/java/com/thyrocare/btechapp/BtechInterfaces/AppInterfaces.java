package com.thyrocare.btechapp.BtechInterfaces;

import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_PEPostCheckoutOrderResponseModel;
import com.thyrocare.btechapp.models.api.request.GetPatientListResponseModel;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.PEQrCodeResponse;
import com.thyrocare.btechapp.models.api.response.PEVerifyQRResponseModel;
import com.thyrocare.btechapp.models.api.response.TCVerifyCouponResponseModel;
import com.thyrocare.btechapp.models.api.response.UpdatePaymentResponseModel;

import java.util.ArrayList;

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

    public interface getPostPatientDetailsResponse {
        void getPostPatientResponse(Get_PEPostCheckoutOrderResponseModel get_pePostCheckoutOrderResponseModel);
    }

    public interface PE_postPatientDetailsAdapterClick{
        void selectPatientDetailsClick();
        void editPatientDetailsClick();
    }

    public  interface getBenList{
        void getBeneficiaryList(GetPatientListResponseModel responseModel);
    }
    public interface PatientSelector{
        void addPatient(GetPatientListResponseModel addpatientModel);
        void editPatient (GetPatientListResponseModel.patients editingPatientData);
    }

}