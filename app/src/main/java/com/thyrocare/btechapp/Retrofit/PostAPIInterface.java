package com.thyrocare.btechapp.Retrofit;



import com.google.gson.JsonObject;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.AvailableStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.BtechWiseVersionTrackerRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampPatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWisePatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWoeMISReuestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FeedbackModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetAccessTokenForOTPRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetBtechCertificateRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetSSLKeyRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitCampWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampModuleMISResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampPatientSearchDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampWisePatientDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampWoeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetBtechCertifcateResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetSSLKeyResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LeaveIntimation_SubmitModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.RequestOTPModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SendSMSAfterBenRemovedRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.TrackUserActivityRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.UpdateStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.WOEOtpValidationRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.MainMaterialModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.btechapp.models.api.request.BtechsRequestModel;
import com.thyrocare.btechapp.models.api.request.BtechwithHub_MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.request.CampStartedRequestModel;
import com.thyrocare.btechapp.models.api.request.CartAPIRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.EmailVaildationPostModel;
import com.thyrocare.btechapp.models.api.request.CashDepositEntryRequestModel;
import com.thyrocare.btechapp.models.api.request.ChangePasswordRequestModel;
import com.thyrocare.btechapp.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.btechapp.models.api.request.HubStartRequestModel;
import com.thyrocare.btechapp.models.api.request.MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.request.MaterialorderRequestModel;
import com.thyrocare.btechapp.models.api.request.NewClientModel;
import com.thyrocare.btechapp.models.api.request.OlcScanPickUpRequestModel;
import com.thyrocare.btechapp.models.api.request.OlcStartRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.Post_DeviceID;
import com.thyrocare.btechapp.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.ResetPasswordRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.request.SendScannedbarcodeLME;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.StockAvailabilityRequestModel;
import com.thyrocare.btechapp.models.api.request.Tsp_Send_RequestModel;
import com.thyrocare.btechapp.models.api.request.UpdateMaterial;
import com.thyrocare.btechapp.models.api.response.CartAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel1;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.EmailValidationResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.SelfieUploadResponseModel;
import com.thyrocare.btechapp.models.api.response.VideosResponseModel;
import com.thyrocare.btechapp.models.data.OrderVisitDetailsModel;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface PostAPIInterface {

    @POST("B2B/COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);

    @POST("api/OrderAllocation/EditNameToSendOTP")
    Call<CommonResponseModel2> CallSendOTPAPI(@Body SendOTPRequestModel sendOTPRequestModel);

    @POST("api/AllSMS/PostVerifyOTP")
    Call<String> CallValidateOTPAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @Multipart
    @POST("api/OrderAllocation/TRFUPLOAD")
    Call<CommonResponseModel1> uploadTRFToServer(@Part MultipartBody.Part TRFImage,
                                                 @Part("BENID") RequestBody BENID);

    @POST("api/UserLoginDevice/PostUserLogOut")
    Call<CommonResponseModel> CallLogoutAPI(@Body LogoutRequestModel logoutRequestModel);

    @POST("B2B/COMMON.svc/Mapping")
    Call<NotificationMappingResponseModel> NotificationTokenMappingAPI(@Body NotificationMappingRequestModel notificationMappingRequestModel);

    @FormUrlEncoded
    @POST("Token")
    Call<LoginResponseModel> CallLoginAPI(@Field("UserName") String username, @Field("Password") String password, @Field("grant_type") String grant_type);

    @POST("api/UserLoginDevice/PostUserLogin")
    Call<LoginDeviceResponseModel> PostLoginUserDeviceAPI(@Body Post_DeviceID post_deviceID);

    @POST("api/OrderAllocation/BtechAppVersion")
    Call<String> BtechWiseVersionTrackerAPI(@Body BtechWiseVersionTrackerRequestModel requestModel);

    @POST("B2B/COMMON.svc/StockAvailability")
    Call<MainMaterialModel> getAvailableStock(@Body AvailableStockModel availableStockModel);

    @POST("B2B/COMMON.svc/Materialupdate")
    Call<CommonResponseModel> updateStock(@Body UpdateStockModel updateStockModel);

    @POST("MASTER.svc/feedback")
    Call<CommonResponseModel> postFeedback(@Body FeedbackModel feedbackModel);

    @POST("api/ManageBtechLeave/ApplyLeave")
    Call<CommonResponseModel> submitresponse(@Body LeaveIntimation_SubmitModel leaveIntimation_submitModel);

    @Multipart
    @POST("api/OrderAllocation/SelfiUploadd")
    Call<SelfieUploadResponseModel> uploadSelfieToServer(@Part MultipartBody.Part image,
                                                         @Part("Btechid") RequestBody Btechid,@Part("APPDOWNLOAD") RequestBody requestAPPDOWNLOAD,@Part("TEMP") RequestBody requestTEMP);

    @POST("api/OrderAllocation/TrackLocation")
    Call<String> SendBtechLatlongToServer(@Body OrderAllocationTrackLocationRequestModel model);

    @POST("api/OrderAllocation/ServiceUpdate")
    Call<String> CallServiceUpdateAPI (@Body ServiceUpdateRequestModel model);

    @POST("api/OrderStatusChange/{ID}")
    Call<String> CallOrderStatusChangeAPI(@Body OrderStatusChangeRequestModel model,@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @POST("api/RemoveBeneficiary")
    Call<OrderVisitDetailsModel> CallRemoveBenAPI(@Header("Authorization") String token, @Body RemoveBeneficiaryAPIRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/AllSMS/PostRemoveBenSMS")
    Call<String> CallSendSMSafterBeneficaryRemovedAPI(@Header("Authorization") String token,@Body SendSMSAfterBenRemovedRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Cart")
    Call<CartAPIResponseModel> CallTechsoCartAPI(@Header("Authorization") String token, @Body CartAPIRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/OrderBooking")
    Call<String> CallTechsoOrderBookingAPI(@Header("Authorization") String token,@Body OrderBookingRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("MASTER.svc/EmailValidate")
    Call<EmailValidationResponseModel> PostEmailValidationAPI(@Body EmailVaildationPostModel model);

    @Headers("Content-Type: application/json")
    @POST("COMMON.svc/Token")
    Call<CommonPOSTResponseModel> RequestForOTPTokenAPI(@Body GetAccessTokenForOTPRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Orderallocation/OTP")
    Call<CommonPOSTResponseModel> RequestForOTPAPI(@Body RequestOTPModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Orderallocation/VerifyOTP")
    Call<CommonPOSTResponseModel> ValidateWoeOTPAPI(@Body WOEOtpValidationRequestModel model);

    @POST("BDN/api/ClientEntry/PostRegister")
    Call<String> CallNewClientEntryRegistrationAPI(@Body NewClientModel newClientpostmodel);

    @POST("B2B/COMMON.svc/StockAvailability")
    Call<com.thyrocare.btechapp.models.api.response.MainMaterialModel> CallStackAvailabilityAPI(@Body StockAvailabilityRequestModel stockAvailabilityRequestModel);

    @POST("order.svc/Appuser")
    Call<CommonPOSTResponseModel> CallUserTrackingAPI(@Body TrackUserActivityRequestModel trackUserActivityRequestModel);

    @FormUrlEncoded
    @POST("api/CallPatchSrcDest/CallPatchRequest")
    Call<String> CallpatchRequestAPI(@Field("Srcnumber") String Srcnumber, @Field("DestNumber") String DestNumber, @Field("VisitID") String VisitID);


    @POST("api/Account/Logout")
    Call<String> CallLogoutRequestApi();

    @POST("api/OrderBooking")
    Call<OrderBookingResponseVisitModel> CallOrderBookingApi(@Body OrderBookingRequestModel orderBookingRequestModel);

    @POST("api/CashDeposit/CashDepositEntry")
    Call<String> CallCashDepositEntryAPI(@Body CashDepositEntryRequestModel cashDepositEntryRequestModel);

    @POST("api/WOE")
    Call<String> CallWorkOrderEntryAPI(@Body OrderBookingRequestModel orderBookingRequestModel);

    @POST("api/CampDetails/CampStarted")
    Call<String> callCampStartRequestApi(@Body CampStartedRequestModel campStartedRequestModel);

    @POST("api/BtechAvaibilityNew/Avaibility")
    Call<String> callBtechAvailabilityRequestApi(@Body SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel);


    @POST("api/MaterialOrderHome/PostMaterialOrderApprove")
    Call<String> CallGetPostMaterialOrderApi(@Body MaterialorderRequestModel materialorderRequestModel);

    @POST("api/Inventory/DailyInvUpdate")
    Call<String> CallPostMaterialInvRequestApi(@Body BtechsRequestModel btechsRequestModel);

    @POST("api/MasterBarcodeMapping")
    Call<String> CallGetMasterBarcodeMapApi(@Body MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel);

    @POST("api/HubStart")
    Call<String> CallGetHubStartRequestApi(@Body HubStartRequestModel hubStartRequestModel);

    @POST("api/HubStart")
    Call<String> CallGetOlcStartRequestApi(@Body OlcStartRequestModel olcStartRequestModel);

    @POST("api/Account/ChangePassword")
    Call<String> CallGetChangePasswordRequestApi(@Body ChangePasswordRequestModel changePasswordRequestModel);


    @Headers("Content-Type: application/json")
    @POST("COMMON.svc/SSLKey")
    Call<GetSSLKeyResponseModel> CallGetSSLAPI(@Body GetSSLKeyRequestModel model);

    @POST("api/Account/ResetPassword")
    Call<String> CallResetPasswordAPI(@Body ResetPasswordRequestModel model);

    @POST("api/ScanPickup")
    Call<String> CallgetScanPickupRequestAPI(@Body OlcScanPickUpRequestModel model);

    @POST("api/PayThyrocare/PassInputs")
    Call<PaymentProcessAPIResponseModel> CallgetTransactionInputsRequestApi(@Body JsonObject jsonRequest);

    @POST("api/PayThyrocare/StartTransaction")
    Call<PaymentStartTransactionAPIResponseModel> CallFetchTransactionResponseOnStartTransactionApi(@Body JsonObject jsonRequest);

    @POST
    Call<PaymentDoCaptureResponseAPIResponseModel> CallgetDoCaptureResponseRequestApi(@Url String URL, @Body JsonObject jsonRequest);

    @Headers("Content-Type: application/json")
    @POST
    Call<String> CallgetRecheckPaymentResponseRequestApi(@Url String URL, @Body String jsonRequest);

    @POST("api/SpecimenTrack/ReceiveBarcodes")
    Call<String> CallgetbTECHWITHhUB_MasterBarcodeMapAPI(@Body BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel);

    @POST("api/SpecimenTrack/ReceiveHubBarcodes")
    Call<String> CallgetTSP_MasterBarcodeMapRequestAPI(@Body BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel);

    @POST("api/SpecimenTrack/SendConsignment")
    Call<String> CallgetTspSendConsignmentAPI(@Body Tsp_Send_RequestModel tsp_send_requestModel);

    @POST("api/TSPLMESampleDrop/PostScannedMasterBarcodebyLME")
    Call<String> CallPostScannedMasterBarcodebyLMEAPI(@Body SendScannedbarcodeLME[] rembensmsRequestModel);

    @POST("api/MaterialOrderTracking/BtechVirtualStoackUpdate")
    Call<String> CallgetPostStockMaterialOrderAPI(@Body UpdateMaterial setmaterialorderRequestModel);

    @POST("api/OrderAllocation/TransferOrder")
    Call<String> CallgetOrderPassRequestModelAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @POST("api/AllSMS/PostSendOTP")
    Call<String> CallgetOrderPassSendOtpRequestAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @POST("api/AllSMS/PostVerifyOTP")
    Call<String> CallgetOrderPassVerifyOtpRequestModelAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @POST("pickso/api/Campintimation/CampwiseData")
    Call<CampPatientSearchDetailResponseModel> CallGetCampDetailAPI(@Body CampPatientDetailRequestModel campPatientDetailRequestModel);

    @POST("pickso/api/Campintimation/CampwisePatientDeatils")
    Call<CampWisePatientDetailResponseModel> CallGetPatientDetailAPI(@Body CampWisePatientDetailRequestModel campWisePatientDetailRequestModel);

    @POST("pickso/api/Campintimation/PatientMisDetails")
    Call<CampModuleMISResponseModel> CallGetCampWOEMISAPI (@Body CampWoeMISReuestModel campWoeMISReuestModel);

    @POST("B2B/WO.svc/postworkorder")
    Call<CampWoeResponseModel> CallSubmitCampWOEAPI (@Body SubmitCampWoeRequestModel submitCampWoeRequestModel);


    @Multipart
    @POST("pickso/api/Campintimation/PatientDetails")
    Call<CommonResponseModel1> CalluploadCAmpWOEPatientVailPhotoAPI(@Part MultipartBody.Part VailPhoto,
                                                 @Part("uniqueID") RequestBody uniqueID,
                                                 @Part("woepatientID") RequestBody woepatientID,
                                                 @Part("campID") RequestBody campID);

    @POST("api/OrderAllocation/AdditionalTechnicianDTL")
    Call<GetBtechCertifcateResponseModel> CallGetBtechCertificatesAPI (@Body GetBtechCertificateRequestModel btechCertificateRequestModel);

}
