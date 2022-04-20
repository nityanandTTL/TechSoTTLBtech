package com.thyrocare.btechapp.Retrofit;

import com.google.gson.JsonObject;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.AvailableStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.BtechWiseVersionTrackerRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampPatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWisePatientDetailRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.CampWoeMISReuestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.EmailVaildationPostModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FeedbackModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FixAppointmentDataModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetAccessTokenForOTPRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetBtechCertificateRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetSSLKeyRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LeaveIntimation_SubmitModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.RequestOTPModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SendSMSAfterBenRemovedRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.SubmitB2BWoeRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.TrackUserActivityRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.UpdateStockModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.WOEOtpValidationRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.B2BWoeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampModuleMISResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampPatientSearchDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CampWisePatientDetailResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonPOSTResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.EmailValidationResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetBtechCertifcateResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetSSLKeyResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.MainMaterialModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.btechapp.models.api.request.AddONRequestModel;
import com.thyrocare.btechapp.models.api.request.BtechsRequestModel;
import com.thyrocare.btechapp.models.api.request.BtechwithHub_MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.request.CampStartedRequestModel;
import com.thyrocare.btechapp.models.api.request.CartAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.CashDepositEntryRequestModel;
import com.thyrocare.btechapp.models.api.request.ChangePasswordRequestModel;
import com.thyrocare.btechapp.models.api.request.GetNBTDetailRequestModel;
import com.thyrocare.btechapp.models.api.request.GetPatientDetailsRequestModel;
import com.thyrocare.btechapp.models.api.request.GetTestCodeRequestModel;
import com.thyrocare.btechapp.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.btechapp.models.api.request.HubStartRequestModel;
import com.thyrocare.btechapp.models.api.request.LeadGenerationRequestModel;
import com.thyrocare.btechapp.models.api.request.MasterBarcodeMappingRequestModel;
import com.thyrocare.btechapp.models.api.request.MaterialorderRequestModel;
import com.thyrocare.btechapp.models.api.request.NewClientModel;
import com.thyrocare.btechapp.models.api.request.NotificationTokenRequestModel;
import com.thyrocare.btechapp.models.api.request.OlcScanPickUpRequestModel;
import com.thyrocare.btechapp.models.api.request.OlcStartRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderBookingRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderPassRequestModel;
import com.thyrocare.btechapp.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.btechapp.models.api.request.PECutomerIntimationSMSRequestModel;
import com.thyrocare.btechapp.models.api.request.PEOrderEditRequestModel;
import com.thyrocare.btechapp.models.api.request.PEPaymentRequestModel;
import com.thyrocare.btechapp.models.api.request.PEUpdatePatientRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMRequestModel;
import com.thyrocare.btechapp.models.api.request.PayTMVerifyRequestModel;
import com.thyrocare.btechapp.models.api.request.PaytypeRequestModel;
import com.thyrocare.btechapp.models.api.request.PickupOrderRequestModel;
import com.thyrocare.btechapp.models.api.request.PostPickupOrderRequestClass;
import com.thyrocare.btechapp.models.api.request.Post_DeviceID;
import com.thyrocare.btechapp.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.RemoveUrineReqModel;
import com.thyrocare.btechapp.models.api.request.ResetPasswordRequestModel;
import com.thyrocare.btechapp.models.api.request.SendOTPRequestModel;
import com.thyrocare.btechapp.models.api.request.SendScannedbarcodeLME;
import com.thyrocare.btechapp.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.btechapp.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.btechapp.models.api.request.SignINSummaryRequestModel;
import com.thyrocare.btechapp.models.api.request.SignInRequestModel;
import com.thyrocare.btechapp.models.api.request.StockAvailabilityRequestModel;
import com.thyrocare.btechapp.models.api.request.Tsp_Send_RequestModel;
import com.thyrocare.btechapp.models.api.request.UpdateMaterial;
import com.thyrocare.btechapp.models.api.response.AddOnResponseModel;
import com.thyrocare.btechapp.models.api.response.CartAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel1;
import com.thyrocare.btechapp.models.api.response.CommonResponseModel2;
import com.thyrocare.btechapp.models.api.response.GetCollectionReqModel;
import com.thyrocare.btechapp.models.api.response.GetCollectionRespModel;
import com.thyrocare.btechapp.models.api.response.GetNBTDetailResponseModel;
import com.thyrocare.btechapp.models.api.response.GetTestResponseModel;
import com.thyrocare.btechapp.models.api.response.LeadChannelRespModel;
import com.thyrocare.btechapp.models.api.response.LeadPurposeResponseModel;
import com.thyrocare.btechapp.models.api.response.LeadgenerationResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.btechapp.models.api.response.LoginResponseModel;
import com.thyrocare.btechapp.models.api.response.NewCommonResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderBookingResponseVisitModel;
import com.thyrocare.btechapp.models.api.response.PECutomerIntimationSMSResponeModel;
import com.thyrocare.btechapp.models.api.response.PEOrderEditResponseModel;
import com.thyrocare.btechapp.models.api.response.PEPaymentResponseModel;
import com.thyrocare.btechapp.models.api.response.PEUpdatePatientResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMResponseModel;
import com.thyrocare.btechapp.models.api.response.PayTMVerifyResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentDoCaptureResponseAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentStartTransactionAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.PaytypeResponseModel;
import com.thyrocare.btechapp.models.api.response.PickupOrderResponseModel;
import com.thyrocare.btechapp.models.api.response.PostPickupOrderResponseModel;
import com.thyrocare.btechapp.models.api.response.QrcodeBasedPatientDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.RemoveUrineSampleRespModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ResponseModel;
import com.thyrocare.btechapp.models.api.response.SelfieUploadResponseModel;
import com.thyrocare.btechapp.models.api.response.SignInResponseModel;
import com.thyrocare.btechapp.models.api.response.SignSummaryResponseModel;
import com.thyrocare.btechapp.models.api.response.TestBookingResponseModel;
import com.thyrocare.btechapp.models.api.response.VideosResponseModel;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
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

    @Headers("Content-Type: application/json")
    @POST("COMMON.svc/otp")
    Call<CommonPOSTResponseModel> CallValidateOTPForQRcodeBasedWOEAPI(@Body RequestOTPModel model);

    @Multipart
    @POST("api/OrderAllocation/TRFUPLOAD")
    Call<CommonResponseModel1> uploadTRFToServer(@Part MultipartBody.Part TRFImage,
                                                 @Part("BENID") RequestBody BENID);

    @POST("commonservices/LeadChannel")
    Call<LeadChannelRespModel> getLeadChannel();

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
                                                         @Part("Btechid") RequestBody Btechid, @Part("APPDOWNLOAD") RequestBody requestAPPDOWNLOAD, @Part("TEMP") RequestBody requestTEMP);

    @POST("api/OrderAllocation/TrackLocation")
    Call<String> SendBtechLatlongToServer(@Body OrderAllocationTrackLocationRequestModel model);

    @POST("api/OrderAllocation/ServiceUpdate")
    Call<String> CallServiceUpdateAPI(@Body ServiceUpdateRequestModel model);

    @POST("api/OrderStatusChange/{ID}")
    Call<String> CallOrderStatusChangeAPI(@Body OrderStatusChangeRequestModel model, @Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @POST("api/RemoveBeneficiary")
    Call<OrderVisitDetailsModel> CallRemoveBenAPI(@Header("Authorization") String token, @Body RemoveBeneficiaryAPIRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/AllSMS/PostRemoveBenSMS")
    Call<String> CallSendSMSafterBeneficaryRemovedAPI(@Header("Authorization") String token, @Body SendSMSAfterBenRemovedRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Cart")
    Call<CartAPIResponseModel> CallTechsoCartAPI(@Header("Authorization") String token, @Body CartAPIRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/OrderBooking")
    Call<String> CallTechsoOrderBookingAPI(@Header("Authorization") String token, @Body OrderBookingRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/OrderBooking")
    Call<OrderBookingResponseVisitModel> CallTechsoOrderBookingAPIFirst(@Header("Authorization") String token, @Body OrderBookingRequestModel model);

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

    @POST("CommonServices/Appuser")
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

    @POST("api/PayThyrocare/ReStartTransaction")
    Call<PaymentStartTransactionAPIResponseModel> CallFetchTransactionResponseOnReStartTransactionApi(@Body JsonObject jsonRequest);

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
    Call<CampModuleMISResponseModel> CallGetCampWOEMISAPI(@Body CampWoeMISReuestModel campWoeMISReuestModel);

    @POST("B2B/WO.svc/postworkorder")
    Call<B2BWoeResponseModel> CallSubmitCampWOEAPI(@Body SubmitB2BWoeRequestModel submitB2BWoeRequestModel);

    @Multipart
    @POST("pickso/api/Campintimation/PatientDetails")
    Call<CommonResponseModel1> CalluploadCAmpWOEPatientVailPhotoAPI(@Part MultipartBody.Part VailPhoto,
                                                                    @Part("uniqueID") RequestBody uniqueID,
                                                                    @Part("woepatientID") RequestBody woepatientID,
                                                                    @Part("campID") RequestBody campID);

    @POST("api/OrderAllocation/AdditionalTechnicianDTL")
    Call<GetBtechCertifcateResponseModel> CallGetBtechCertificatesAPI(@Body GetBtechCertificateRequestModel btechCertificateRequestModel);

    @Multipart
    @POST("Aayushman/Booking")
    Call<TestBookingResponseModel> SubitReferAFriendformToServer(@Part("order_by") RequestBody order_by,
                                                                 @Part("mobile") RequestBody mobile,
                                                                 @Part("email") RequestBody email,
                                                                 @Part("address") RequestBody address,
                                                                 @Part("remarks") RequestBody remarks,
                                                                 @Part("bookingtype") RequestBody bookingtype,
                                                                 @Part("report_code") RequestBody report_code,
                                                                 @Part("bencount") RequestBody bencount,
                                                                 @Part("pincode") RequestBody pincode,
                                                                 @Part("product") RequestBody product,
                                                                 @Part("rate") RequestBody rate,
                                                                 @Part("reports") RequestBody reports,
                                                                 @Part("ref_code") RequestBody ref_code,
                                                                 @Part("service_type") RequestBody service_type,
                                                                 @Part("api_key") RequestBody api_key,
                                                                 @Part("bendataxml") RequestBody bendataxml,
                                                                 @Part("orderid") RequestBody orderid,
                                                                 @Part("hc") RequestBody hc,
                                                                 @Part("tsp") RequestBody tsp,
                                                                 @Part("pay_type") RequestBody pay_type,
                                                                 @Part MultipartBody.Part imageFileMultiBody,
                                                                 @Part MultipartBody.Part audioFileMultiBody);

    @POST("B2B/WO.svc/Btechpostworkorder")
    Call<B2BWoeResponseModel> CallQrCodeBasedSubmitWOEAPI(@Body SubmitB2BWoeRequestModel submitB2BWoeRequestModel);

    @POST("B2B/Common.svc/CampPatientDetails")
    Call<QrcodeBasedPatientDetailsResponseModel> CallGetQRCodeBasedPatientDetailsAPI(@Body GetPatientDetailsRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("COMMON.svc/otp")
    Call<CommonPOSTResponseModel> CallGenerateOTPAPI(@Body RequestOTPModel model);

    @Headers("Content-Type: application/json")
    @POST("CommonServices/LeadPurpose")
    Call<LeadPurposeResponseModel> CallGetLeadPurposeAPI();

    @Headers("Content-Type: application/json")
    @POST("bookingmaster/lead")
    Call<LeadgenerationResponseModel> CallSubmitLeadAPI(@Body LeadGenerationRequestModel requestModel);

    @POST("Complaint_module/Letter")
    Call<HCWResponseModel> postHCW(@Body HCWRequestModel hcwRequestModel);

    @POST("CommonServices/PincodewiseCollectionPoint")
    Call<GetCollectionRespModel> GetCollectionCenter(@Body GetCollectionReqModel getCollectionReqModel);

    @POST("CommonServices/Notification")
    Call<NewCommonResponseModel> PostToken(@Body NotificationTokenRequestModel tokenRequestModel);

    @Multipart
    @POST("api/OrderAllocation/SelfiafterWOE")
    Call<String> CallUploadSelfieAPI(@Header("Authorization") String Token,
                                     @Part("Btechid") RequestBody BtechID,
                                     @Part("ORDERNO") RequestBody OrderNo,
                                     @Part MultipartBody.Part selfieFileMultiBody);

    @POST("api/OrderAllocation/Pickuporders")
    Call<PickupOrderResponseModel> getPickupOrder(@Body PickupOrderRequestModel pickupOrderRequestModel);

    @POST("api/OrderAllocation/PostPickuporders")
    Call<PostPickupOrderResponseModel> postPickupOrder(@Body PostPickupOrderRequestClass getCollectionReqModel);

    @POST("GetTestCode")
    Call<GetTestResponseModel> postTest(@Body GetTestCodeRequestModel getTestCodeRequestModel);

    @POST("api/PendingVisitsOptimize/RemoveSample")
    Call<RemoveUrineSampleRespModel> removeUrineSample(@Header("Authorization") String Token,@Body RemoveUrineReqModel removeUrineReqModel);

    @POST("api/Account/BtechSignInout")
    Call<SignInResponseModel> signINOUT(@Body SignInRequestModel signInRequestModel);

    @POST("api/Account/BtechSignInsummary")
    Call<SignSummaryResponseModel> signInSummary(@Body SignINSummaryRequestModel summaryRequestModel);

    @POST("api/PayThyrocare/PaytmSendPaymentLink")
    Call<PayTMResponseModel> payTM(@Body PayTMRequestModel payTMRequestModel);

    @POST("api/PayThyrocare/CheckPaymentLinkResponse")
    Call<PayTMVerifyResponseModel> payTMVerify(@Body PayTMVerifyRequestModel payTMVerifyRequestModel);

    @Multipart
    @POST("api/OrderAllocation/Uploadblob")
    Call<String> CallUploadVialAPI(@Part("ORDERNO") RequestBody OrderNO,
                                   @Part("BENID") RequestBody benID,
                                   @Part("TEST") RequestBody test,
                                   @Part("TYPE") RequestBody type,
                                   @Part("APPID") RequestBody appID,
                                   @Part MultipartBody.Part vialFileMultiBody);

    @POST("api/PayThyrocare/PEVerifyPayment")
    Call<PEPaymentResponseModel> PEVerifyPayment(@Body PEPaymentRequestModel pePaymentRequestModel);

    @POST("api/OrderAllocation/GetOrderPaytype")
    Call<PaytypeResponseModel> getOrderPaytype(@Body PaytypeRequestModel paytypeRequestModel);

    @POST("/api/partner-integration/v1/events")
    Call<PaytypeResponseModel> getStatusUpdate(@Body PaytypeRequestModel paytypeRequestModel);

    @POST("api/Account/getNBTDetail")
    Call<GetNBTDetailResponseModel> getNBTDetails(@Body GetNBTDetailRequestModel getNBTDetailRequestModel);

    @Headers({"Content-Type: application/json",
            "x-source: THYROCARE",
            "x-api-auth: 9551825306485694"
    })
    @POST("/api/partner-integration/v1/order/{orderID}/add-on-order")
    Call<AddOnResponseModel> getAddOnOrder(@Path("orderID")String OrderID, @Body AddONRequestModel addONRequestModel);

    @POST("api/PEEvents/PEUpdatePatient")
    Call<PEUpdatePatientResponseModel> postPEUpdatePatient(@Body PEUpdatePatientRequestModel peUpdatePatientRequestModel);

    @POST("api/PEEvents/PEOrderEdit")
    Call<PEOrderEditResponseModel> postPEOrderEdit(@Body PEOrderEditRequestModel peOrderEditRequestModel);

    @POST("api/PEEvents/PECutomerIntimationSMS")
    Call<PECutomerIntimationSMSResponeModel> getSMS(@Body PECutomerIntimationSMSRequestModel smsRequestModel);

    @POST("api/YNCStatusChange/PostUpdateOrderHistory")
    Call<ResponseModel> updateOrderHistory(@Header("Authorization") String token, @Body FixAppointmentDataModel fixAppointmentDataModel);
}
