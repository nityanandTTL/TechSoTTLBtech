package com.thyrocare.network;


import com.thyrocare.utils.app.AppConstants;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.HashMap;
import java.util.List;

public class AbstractApiModel implements AppConstants {

    public static final String FILE_UPLOAD = "file_upload";

    /* Requset urlt_layout of api call */
    private String requestUrl;

    /* Json string to post/put method */
    private String postData = "";

    /* Headers to be added as list */
    private HashMap<String, String> headerMap;

    public int methodType;

    private UrlEncodedFormEntity entity;

    //   public static String appEnvironment = "DEVELOPMENT";
    //public static String appEnvironment = "DEMO";
    public static String appEnvironment = "PRODUCTION";


    // New DB changes Amazon Dev
    public static String SERVER_BASE_API_URL_DEV = "http://apibeta.thyrocare.com";


    // New DB changes Demo Amazon
    public static String SERVER_BASE_API_URL_DEMO = "http://apibeta.thyrocare.com";

    // New DB changes Amazon Production
    // https://www.dxscloud.com/techso
    public static String SERVER_BASE_API_URL_PROD = "http://bts.dxscloud.com/btsapi";
// public static String SERVER_BASE_API_URL_PROD = "https://www.dxscloud.com/techsoapi";



    public static String SERVER_BASE_API_URL = appEnvironment.equals("DEVELOPMENT") ? SERVER_BASE_API_URL_DEV : appEnvironment.equals("DEMO") ? SERVER_BASE_API_URL_DEMO : appEnvironment.equals("PRODUCTION") ? SERVER_BASE_API_URL_PROD : SERVER_BASE_API_URL_DEV;


    /**
     * RESPECTIVE URLS
     */

    public static String DEVICE_TYPE = "android";

    public static String APP_NAME = "BTECH";

    // Version Specific Base URL


    public String VERSION_API_URL = "/api";

    public String LOGIN = "/Token";

    public String LOG_OUT = VERSION_API_URL + "/Account/Logout";

    public String CHANGE_PASSWORD = VERSION_API_URL + "/Account/ChangePassword";

    public String RESET_PASSWORD = VERSION_API_URL + "/Account/ResetPassword";

    public String SELFIE_UPLOAD = VERSION_API_URL + "/SelfiUpload";

    public String FETCH_ORDER_DETAIL = VERSION_API_URL + "/OrderVisitDetails";

    public String SET_MATERIALORDER_DETAIL = VERSION_API_URL + "/MaterialOrderHome/PostMaterialOrderApprove";

    public String CAMP_COUNT = VERSION_API_URL + "/CampDetails/MyCampCount";

    public String TSP_SEND_CONSIGNMENT = VERSION_API_URL + "/SpecimenTrack/SendConsignment";

    public String CAMP_QR_DETAIL = VERSION_API_URL + "/CampDetails/OrderDetails";
    public String PAYMENT_MODE_MASTER = VERSION_API_URL + "/Masters/PaymentModeMaster";
    public String BANK_MASTER = VERSION_API_URL + "/Masters/BankMaster";

    public String FETCH_SLOT_DETAIL = VERSION_API_URL + "/ServingSlotDetails";

    public String ORDER_STATUS_CHANGE = VERSION_API_URL + "/OrderStatusChange";

    public String CAMP_STARTED = VERSION_API_URL + "/CampDetails/CampStarted";

    public String FETCH_BRAND_MASTER = VERSION_API_URL + "/BrandMaster";

    public String FETCH_BRAND_WISE_TEST_MASTER = VERSION_API_URL + "/BrandTestRateList";

   // public String BTECH_AVAILABILITY = VERSION_API_URL + "/BtechAvaibility";


    public String BTECH_AVAILABILITY = VERSION_API_URL + "/BtechAvaibilityNew/Avaibility";

    public String LOGOUT = VERSION_API_URL + "/Account/Logout";

    public String FETCH_LEDGER_DETAIL = VERSION_API_URL + "/Ledger/CashRegister";

    public String SET_BTECH_MATERIAL_INV = VERSION_API_URL + "/Inventory/DailyInvUpdate";

    // public String FETCH_EARNINGREGISTER_DETAIL = VERSION_API_URL + "/Ledger/EarningRegister";

    public String FETCH_DEPOSITREGISTER_DETAIL = VERSION_API_URL + "/Ledger/DepositPayments";
    public String FETCH_EARNINGREGISTER_DETAIL = VERSION_API_URL + "/OrdersCredit/BtechEarnings";

    public String FETCH_VERSION_CONTROL_DETAIL = VERSION_API_URL + "/VersionControl";

    public String FETCH_MATERIALMASTER_DETAIL = VERSION_API_URL + "/Masters/CategoryWiseMaterialMaster";

    public String FETCH_MATERIALINV_DETAIL = VERSION_API_URL + "/Inventory/StockInHand";

    public String BTECH_COLLECTIONS = VERSION_API_URL + "/BtechCollections";

    public String BTECH_HUB_DETAILS_DISPLAY = VERSION_API_URL + "/BtechHubs";

    public String ORDER_SERVED_DETAILS_DISPLAY = VERSION_API_URL + "/BtechOrderSummary/BtechServedOrders";

    public String BTECH_CLIENTS_DETAILS_DISPLAY = VERSION_API_URL + "/BtechClients";

    public String CAMPS_DETAILS_DISPLAY = VERSION_API_URL + "/CampDetails/MyCampDetails";

    public String BTECH_EST_EARNINGS = VERSION_API_URL + "/BtechEstEarning";

    public String HUBSTART = VERSION_API_URL + "/HubStart";

    public String OLCSTART = VERSION_API_URL + "/ClientPickup";

    public String MASTER_BARCODE_MAPPING = VERSION_API_URL + "/MasterBarcodeMapping";

    public String BTECHWITHHUB_MASTER_BARCODE_MAPPING = VERSION_API_URL + "/SpecimenTrack/ReceiveBarcodes";

    public String TSP_MASTER_BARCODE_MAPPING = VERSION_API_URL + "/SpecimenTrack/ReceiveHubBarcodes";

    public String LAB_ALERT_MASTER = VERSION_API_URL + "/LabAlerts";

    public String FETCH_LEAVE_NATURE_MASTER = VERSION_API_URL + "/Masters/LeaveNatureMaster/L1";

    public String SET_APPLY_LEAVE = VERSION_API_URL + "/ManageBtechLeave/ApplyLeave";

    public String REMOVE_BENEFICIARY = VERSION_API_URL + "/RemoveBeneficiary";

    public String OLC_PICKUP_SUBMIT_SCAN_BARCODE = VERSION_API_URL + "/ScanPickup";

    public String REMARKS = VERSION_API_URL + "/Masters/ReleaseRemarksMaster";

    public String SCHEDULEOPEN = VERSION_API_URL + "/BtechAvaibilityNew/BtechMarkedAvailability";

    public String ORDER_BOOKING = VERSION_API_URL + "/OrderBooking";
    public String CASH_DEPOSIT_ENTRY = VERSION_API_URL + "/CashDeposit/CashDepositEntry";

    public String WOE = VERSION_API_URL + "/WOE";

    public String CART = VERSION_API_URL + "/Cart";

    public String PAYMENT_NARRATION_MASTER = VERSION_API_URL + "/Masters/NarrationMaster";

    public String PAYMENT_SELECT_MODE = VERSION_API_URL + "/PayThyrocare/SelectMode";

    public String PAYMENT_PASS_INPUTS = VERSION_API_URL + "/PayThyrocare/PassInputs";

    public String PAYMENT_START_TRANSACTION = VERSION_API_URL + "/PayThyrocare/StartTransaction";

    public String PATCH_CALL_REQUEST = VERSION_API_URL + "/CallPatchSrcDest/CallPatchRequest";

    public String BTECHWITH_HUB_BARCODE = VERSION_API_URL + "/SpecimenTrack/ReceiveScannedBarcode";

    public String TSP_SEND_MODES = VERSION_API_URL + "/SpecimenTrack/CourierModes";

    public String TSP_BARCODE_SCAN = VERSION_API_URL + "/SpecimenTrack/TSPScannedBarcode";

    public String TSP_BARCODE = VERSION_API_URL + "/SpecimenTrack/ReceiveHubBarcode";

    public String LOCUS_PUSH_LOCATIONS_API = "https://api.locus.sh/v1/client/thyrocare/user/";

    public String ADD_BENEFICIARY = "/api/AddBeneficiary";

    public static final String X_API_KEY = "x-api-key";

    public static final String AUTHORIZATION = "Authorization";

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_X_WWW_FROM_URLENCODED = "application/x-www-form-urlencoded";

    /* Json string to post/put method */
    private String postJsonString;
    /* Headers to be added as list */
    private List<HeaderData> header;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String rquestUrl) {
        this.requestUrl = rquestUrl;
    }

    public String getPostJsonString() {
        return postJsonString;
    }

    public void setPostJsonString(String postJsonString) {
        this.postJsonString = postJsonString;
    }

    public List<HeaderData> getHeader() {
        return header;
    }

    public void setHeader(List<HeaderData> header) {
        this.header = header;
    }

    public void createJson() {

    }

    public void setParam(String key, String value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + value;
        } else if (key.equals("action")) {
            this.requestUrl = this.requestUrl + key + "=" + value;
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + value;
        }
    }

    public void setParam(String key, float value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public void setParam(String key, int value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public AbstractApiModel() {
        super();
        headerMap = new HashMap<String, String>();
    }

    public String getRequestUrlt_layout() {
        return requestUrl;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public void putHeader(String key, String value) {

        headerMap.put(key, value);
    }

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public UrlEncodedFormEntity getEntity() {
        return entity;
    }

    public void setEntity(UrlEncodedFormEntity entity) {
        this.entity = entity;
    }
}