package com.thyrocare.btechapp.Retrofit;


import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CheckbarcodeResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.FAQandANSArray;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.FeedbackListModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetResponse_NatureLeaveModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_BtechEarning_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_Leave_Applied_history_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_cash_register_details_Model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.Get_deposite_details_model;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.ServedOrderResponseModel;
import com.thyrocare.btechapp.models.api.request.MaterialorderRequestModel;
import com.thyrocare.btechapp.models.api.request.OlcStartRequestModel;
import com.thyrocare.btechapp.models.api.response.BankMasterResponseModel;
import com.thyrocare.btechapp.models.api.response.BtechClientsResponseModel;
import com.thyrocare.btechapp.models.api.response.BtechCollectionsResponseModel;
import com.thyrocare.btechapp.models.api.response.BtechEstEarningsResponseModel;
import com.thyrocare.btechapp.models.api.response.BtechImageResponseModel;
import com.thyrocare.btechapp.models.api.response.BtechwithHubResponseModel;
import com.thyrocare.btechapp.models.api.response.CampListDisplayResponseModel;
import com.thyrocare.btechapp.models.api.response.CampScanQRResponseModel;
import com.thyrocare.btechapp.models.api.response.CouponCodeResponseModel;
import com.thyrocare.btechapp.models.api.response.DSAProductsResponseModel;
import com.thyrocare.btechapp.models.api.response.DispatchHubDisplayDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.DynamicBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.models.api.response.FetchLedgerResponseModel;
import com.thyrocare.btechapp.models.api.response.FetchOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetOrderDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPEBtechSlotResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPECancelRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetPETestResponseModel;
import com.thyrocare.btechapp.models.api.response.GetRemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.GetTestListResponseModel;
import com.thyrocare.btechapp.models.api.response.MaterialBtechStockResponseModel;
import com.thyrocare.btechapp.models.api.response.MaterialINVResponseModel;
import com.thyrocare.btechapp.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.VersionControlResponseModel;
import com.thyrocare.btechapp.models.api.response.OrderPassresponseModel;
import com.thyrocare.btechapp.models.api.response.OrderServedResponseModel;
import com.thyrocare.btechapp.models.api.response.PEAuthResponseModel;
import com.thyrocare.btechapp.models.api.response.PEQrCodeResponse;
import com.thyrocare.btechapp.models.api.response.PEVerifyQRResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentModeMasterResponseModel;
import com.thyrocare.btechapp.models.api.response.PaymentProcessAPIResponseModel;
import com.thyrocare.btechapp.models.api.response.QrcodeBasedPatientDetailsResponseModel;
import com.thyrocare.btechapp.models.api.response.RemarksRequestToReleaseResponseModel;
import com.thyrocare.btechapp.models.api.response.RemarksResponseModel;
import com.thyrocare.btechapp.models.api.response.Tsp_ScanBarcodeResponseModel;
import com.thyrocare.btechapp.models.api.response.VideoLangaugesResponseModel;
import com.thyrocare.btechapp.models.data.BrandTestMasterModel;
import com.thyrocare.btechapp.models.data.DateWiseWLMISDetailsModel;
import com.thyrocare.btechapp.models.data.DepositRegisterModel;
import com.thyrocare.btechapp.models.data.DeviceLoginDetailsModel;
import com.thyrocare.btechapp.models.data.DispositionDataModel;
import com.thyrocare.btechapp.models.data.Earning_NewRegisterModel;
import com.thyrocare.btechapp.models.data.LocationMasterModel;
import com.thyrocare.btechapp.models.data.MaterialDetailsModel;
import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.models.data.SlotModel;
import com.thyrocare.btechapp.models.data.TSPNBT_AvilModel;
import com.thyrocare.btechapp.models.data.Tsp_SendMode_DataModel;
import com.thyrocare.btechapp.models.data.WLMISDetailsModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface GetAPIInterface {


    @GET("B2B/COMMON.svc/Showlang")
    Call<VideoLangaugesResponseModel> getVideoLanguages();

    @GET("api/VersionControl/1")
    Call<VersionControlResponseModel> VersionControlAPI();

    @GET("api/BtechAvaibilityNew/BtechMarkedAvailability/{BtechID}")
    Call<NewBtechAvaliabilityResponseModel> GetBtechAvailability(@Path("BtechID") String BtechID);

    @GET("api/BtechAvaibilityNew/DynamicBtechMarkedAvailability/{BtechID}")
    Call<DynamicBtechAvaliabilityResponseModel> GetDynamicBtechAvailability(@Path("BtechID") String BtechID);

    @GET("api/BtechAvaibilityNew/NBTMarkedAvailability/{ID}")
    Call<ArrayList<TSPNBT_AvilModel>> GetTSP_NBT_Avialability(@Path("ID") String ID);

    @GET("api/BtechOrderSummary/BtechServedOrders/{BtechID}/{Date}")
    Call<ServedOrderResponseModel> CallServedOrderAPI(@Path("BtechID") String BtechID, @Path("Date") String Date);

    @GET("api/Ledger/OrderReceipt/{OrderNo}")
    Call<String> CallSendreceiptAPI(@Path("OrderNo") String OrderNo);

    @GET("MASTER.svc//{api_key}//FEEDBACK/getlist")
    Call<FeedbackListModel> getFeedbackList(@Path("api_key") String Api_key);

    @GET("api/Masters/LeaveNatureMaster/L1")
    Call<ArrayList<GetResponse_NatureLeaveModel>> Getnature_forleaveintimation();

    @GET("api/BtechAbsentList/AbsentList/{btechid}")
    Call<ArrayList<Get_Leave_Applied_history_Model>> GetBetch_leavehistory(@Path("btechid") String btechid);

    @GET("api/Ledger/CashRegister/{btechid}/{fromdate}/{todate}")
    Call<Get_cash_register_details_Model> Get_CashRegister(@Path("btechid") String btechid, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @GET("api/Ledger/DepositPayments/{btechid}/{fromdate}/{todate}")
    Call<ArrayList<Get_deposite_details_model>> Get_Deposite(@Path("btechid") String btechid, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @GET("api/OrdersCredit/BtechEarnings/{btechid}/{fromdate}/{todate}")
    Call<Get_BtechEarning_Model> get_BtechEarning(@Path("btechid") String btechid, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @GET("MASTER.svc/BTECH STAFF/DynamicFaq")
    Call<FAQandANSArray> getFAQ();

    @GET("api/OrderVisitDetailsOptimize/{btechId}")
    Call<FetchOrderDetailsResponseModel> getAllVisitDetails(@Path("btechId") String btechId);

    @GET("api/OrderVisitDetailsOptimize/ByOrderNo/{btechId}/{orderId}")
    Call<FetchOrderDetailsResponseModel> getOrderAllVisitDetails(@Path("btechId") String btechId, @Path("orderId") String orderId);

    //    @GET("api/OrderDetailsByVisit/{orderno}")
    @GET("api/OrderVisitDetailsOptimize/GetOrderVisitDetailsorderwise/{orderno}")
    Call<FetchOrderDetailsResponseModel> getOrderDetail(@Path("orderno") String orderno);

    @Headers("Content-Type: application/json")
    @GET("api/BrandTestRateList/2")
    Call<BrandTestMasterModel> CallGetTechsoPRoductsAPI(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("api/CampDetails/MyCampCount/{ID}")
    Call<String> CallGetCampDetailsCountAPI(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/CampDetails/OrderDetails/{qrContent}")
    Call<CampScanQRResponseModel> CallgetSendQRCodeRequestAPI(@Path("qrContent") String qrContent);

    @Headers("Content-Type: application/json")
    @GET("api/Masters/PaymentModeMaster")
    Call<ArrayList<PaymentModeMasterResponseModel>> CallGetPaymentModeMasterApi();

    @Headers("Content-Type: application/json")
    @GET("api/Masters/BankMaster/{ID}")
    Call<ArrayList<BankMasterResponseModel>> CallGetBankMasterApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/BrandMaster")
    Call<String> CallFetchBrandMasterApi();

    @Headers("Content-Type: application/json")
    @GET("api/LabAlerts")
    Call<String> CallGetFetchLabAlertMasterApi();

    @Headers("Content-Type: application/json")
    @GET("api/BrandTestRateList/{ID}")
    Call<String> CallGetFetchBrandwiseTestMasterApi(@Path("ID") String ID);


    @Headers("Content-Type: application/json")
    @GET("api/ServingSlotDetails/{ID}")
    Call<ArrayList<SlotModel>> CallFetchSlotDetailsApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/ServingSlotDetails/{ID}")
    Call<ArrayList<SlotModel>> CallFetchSlotDetailsApi(@Header("Authorization") String token, @Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/Ledger/CashRegister/{ID}/{fromdate}/{todate}")
    Call<FetchLedgerResponseModel> CallgetFetchDepositDetailsApi(@Path("ID") String ID, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @Headers("Content-Type: application/json")
    @GET("api/OrdersCredit/BtechEarnings/{ID}/{fromdate}/{todate}")
    Call<Earning_NewRegisterModel> CallFetchEarningDetailsApi(@Path("ID") String ID, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @GET("api/Ledger/DepositPayments/{btechid}/{fromdate}/{todate}")
    Call<ArrayList<DepositRegisterModel>> CallgetFetchDepositPaymentDetailsApi(@Path("btechid") String btechid, @Path("fromdate") String fromdate, @Path("todate") String todate);

    @Headers("Content-Type: application/json")
    @GET("api/Masters/CategoryWiseMaterialMaster/{Category}")
    Call<ArrayList<MaterialDetailsModel>> CallGetMaterialsDetailsApi(@Path("Category") String Category);

    @Headers("Content-Type: application/json")
    @GET("api/Inventory/StockInHand/{ID}")
    Call<MaterialINVResponseModel> CallGetMaterialINVDetailsApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/MaterialOrderTracking/BtechVirtualStoack/{ID}")
    Call<MaterialBtechStockResponseModel> CallGetBtechVirtualStoack_DETAILApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechCollections/{ID}")
    Call<BtechCollectionsResponseModel> CallGetBtechCollectionListApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechHubs/{ID}")
    Call<DispatchHubDisplayDetailsResponseModel> CallGetDispatchHubDetailsDisplayApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechOrderSummary/BtechServedOrders/{ID}/{Date}")
    Call<OrderServedResponseModel> CallGetOrderServedDetailsDisplayApi(@Path("ID") String ID, @Path("Date") String Date);

    @Headers("Content-Type: application/json")
    @GET("api/BtechClients/{ID}")
    Call<BtechClientsResponseModel> CallGetBtechClientsListDetailsDisplayApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/CampDetails/MyCampDetails/{ID}")
    Call<CampListDisplayResponseModel> CallGetCampListDetailsDisplayApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechEstEarning/{ID}")
    Call<BtechEstEarningsResponseModel> CallGetBtechEstEarningsApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/PayThyrocare/SelectMode/{ID}")
    Call<ArrayList<PaymentProcessAPIResponseModel>> CallFetchPaymentModesApi(@Path("ID") int ID);

    @Headers("Content-Type: application/json")
    @GET("api/Masters/ReleaseRemarksMasterNew/{ID}")
    Call<ArrayList<RemarksResponseModel>> CallgetremarksRequestApi(@Path("ID") int ID);

    @Headers("Content-Type: application/json")
    @GET("api/Masters/OrderRemarks/{ID}")
    Call<ArrayList<RemarksRequestToReleaseResponseModel>> GetremarksForRequestToReleaseApi(@Path("ID") int ID);

    @Headers("Content-Type: application/json")
    @GET("api/OrderAllocation/PinForOrderAllocation/{Btech_ID}/{Pincode}")
    Call<OrderPassresponseModel> CallGetorderallocationApi(@Path("Btech_ID") String Btech_ID, @Path("Pincode") String Pincode);

    @Headers("Content-Type: application/json")
    @GET("api/SpecimenTrack/ReceiveScannedBarcode/{Btech_ID}")
    Call<BtechwithHubResponseModel> CallgetfetchBtechwithHubBarcodeApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/SpecimenTrack/ReceiveHubBarcode/{Btech_ID}")
    Call<BtechwithHubResponseModel> CallgetTspBarcodeApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/SpecimenTrack/CourierModes")
    Call<ArrayList<Tsp_SendMode_DataModel>> CallgetTspModeDataApi();

    @Headers("Content-Type: application/json")
    @GET("api/SpecimenTrack/TSPScannedBarcode/{Btech_ID}")
    Call<Tsp_ScanBarcodeResponseModel> CallgetTspScanBarcodeApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechNotification/BlockedBTS/{Btech_ID}")
    Call<String> CallGetBtechBlockApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/BtechNotification/OrderAssigned/{Btech_ID}")
    Call<String> CallgetAcceptOrderNotificationApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/UserLoginDevice/GetDeviceData/{UserID}")
    Call<ArrayList<DeviceLoginDetailsModel>> CallgetLoginDeviceDataApi(@Path("UserID") String UserID);

    @Headers("Content-Type: application/json")
    @GET("api/Ledger/OrderReceipt/{OrderID}")
    Call<String> CallgetErieceptApi(@Path("OrderID") String OrderID);

    @Headers("Content-Type: application/json")
    @GET("api/TSPLMESampleDrop/GetSampleDropDetailsbyTSPLME/{Id}/{batch}")
    Call<ArrayList<SampleDropDetailsbyTSPLMEDetailsModel>> CallgetSampleDropDetailsbyTSPLMEApi(@Path("Id") String Id, @Path("batch") int batch);

    @Headers("Content-Type: application/json")
    @GET("api/TSPLMESampleDrop/GetWLMIS/{ID}")
    Call<ArrayList<WLMISDetailsModel>> CallgetWLMISApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/TSPLMESampleDrop/GetWLPickUpMIS/{ID}")
    Call<ArrayList<DateWiseWLMISDetailsModel>> CallgetWLMISDateWiseApi(@Path("ID") String ID);

    @Headers("Content-Type: application/json")
    @GET("api/OrderAllocation/BtechFaceDetection/{Btech_ID}")
    Call<BtechImageResponseModel> CallgetBtechFaceImageRequestApi(@Path("Btech_ID") String Btech_ID);

    @Headers("Content-Type: application/json")
    @GET("api/OrderAllocation/Disposition")
    Call<DispositionDataModel> CallgetDispositionApi();

    @Headers("Content-Type: application/json")
    @GET("api/Masters/LocationMaster")
    Call<ArrayList<LocationMasterModel>> CallgetLocationMasterApi();

    @Headers("Content-Type: application/json")
    @GET("api/Masters/LocationMaster")
    Call<ArrayList<LocationMasterModel>> CallCheckbarcodeApi();

    @GET("B2B/WO.svc/{api_key}/{barcode}/getcheckbarcode")
    Call<CheckbarcodeResponseModel> CallCheckBarcodeAPI(@Path("api_key") String api_key, @Path("barcode") String barcode);

    @GET("api/BenTestList/GetTestList/{benID}")
    Call<GetTestListResponseModel> CallGetTestDetailsAPI(@Path("benID") String benID);

    @GET("api/integration/v1/auth")
    Call<PEAuthResponseModel> callPEAuthorization(@Header("Content-Type") String content_type, @Header("x-source") String source, @Header("client-id") String client);

    /*@GET("/api/partner-integration/v1/catalog/pincode/{pincode}/items/{orderNo}")
    Call<GetPETestResponseModel> getPETests(@Header("Content-Type") String content_type, @Header("x-source") String source, @Header("x-api-auth") String authToken, @Path("pincode") String pincode,@Path("OrderNo") String OrderNo);*//*,@Path(value = "value",encoded = true)String value*/

    @GET("/api/partner-integration/v1/catalog/pincode/{pincode}/items")
    Call<GetPETestResponseModel> getPETests(@Header("Content-Type") String content_type, @Header("x-source") String source, @Header("x-api-auth") String authToken, @Path("pincode") String pincode, @Query("partner_order_id") String orderno);/*,@Path(value = "value",encoded = true)String value*/

    //TODO DSA brandtestmaster
    @GET("api/DSAProducts/Products/{OrderNo}")
    Call<DSAProductsResponseModel> getDSAProducts(@Path("OrderNo") String OrderNo);

    @GET("api/OrderVisitDetailsOptimize/GetOrdersdetails/{BtechID}")
    Call<GetOrderDetailsResponseModel> getpendingOrderDetails(@Path("BtechID") String BtechID);

    @GET("api/Masters/GetPECancellationRemarks/{ID}")
    Call<GetPECancelRemarksResponseModel> getpecancelRemarks(@Path("ID") String ID);

    //    @GET("api/Masters/DisplaySubSlotMastersForPE/{pincode}/{date}")
    @GET("api/Masters/DisplayBenwiseSubSlotMastersFOrPE/{pincode}/{date}/{benCount}")
    Call<ArrayList<GetPEBtechSlotResponseModel>> getPEbtechSlot(@Header("Authorization") String token, @Path("pincode") String pincode, @Path("date") String date, @Path("benCount") int size);

    @GET("api/Masters/DisplaySubSlotMasters/{pincode}/{date}")
    Call<ArrayList<GetPEBtechSlotResponseModel>> getbtechSlot(@Header("Authorization") String token, @Path("pincode") String pincode, @Path("date") String date);

    @GET("api/Masters/OrderRemarks/{ID}")
    Call<ArrayList<GetRemarksResponseModel>> getRemarks(@Path("ID") String ID);

    @GET("api/Masters/OrderReasons/{ID}")
    Call<ArrayList<GetPECancelRemarksResponseModel>> getReasons(@Path("ID") String ID);

    @GET("PEEvents/PECouponAPI/{OrderID}")
    Call<CouponCodeResponseModel> getCouponCodes(@Path("OrderID") String orderid);

    @Headers({"Content-Type: application/json", "X-source:Thyrocare"})
    @GET("api/partner-integration/v1/thyrocare/get-qr")
    Call<PEQrCodeResponse> getPEQRCode(@Query("partner_order_id") String orderno, @Header("X-Acess-Token") String accesToken);

    @Headers({"Content-Type: application/json", "X-source:Thyrocare"})
    @GET("api/integration/v1/thyrocare/validate-qr")
    Call<PEVerifyQRResponseModel> peVerifyQR(@Query("partner_order_id") String orderno);
}

