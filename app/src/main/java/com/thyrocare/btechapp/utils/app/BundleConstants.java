package com.thyrocare.btechapp.utils.app;

import com.thyrocare.btechapp.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.btechapp.models.data.Venupunture_Temporary_ImageModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/27/2017.
 */

public class BundleConstants {
    //Intent and Activity Result related constants
    public static final int VOMD_START = 1004;
    public static final int VOMD_ARRIVED = 1005;
    public static final int HMD_START = 1006;
    public static final int LME_START = 10064;
    public static final int LME_ARRIVED = 1007;
    public static final int HMD_ARRIVED = 1007;
    public static final int ADD_TESTS_START = 1008;
    public static final int ADD_TESTS_FINISH = 1009;
    public static final int EDIT_START = 1010;
    public static final int EDIT_FINISH = 1011;
    public static final int ADD_START = 1023;
    public static final int ADD_FINISH = 1024;
    public static final int BCMD_START = 1012;
    public static final int BCMD_ARRIVED = 1013;
    public static final int CMD_START = 1014;
    public static final int CMD_ARRIVED = 1015;
    public static final int START_BARCODE_SCAN = 0x0000c0de;
    public static final int START_PATIENT_QRCODE_SCAN = 0x0000f0de;
    public static final int PAYMENTS_START = 1021;
    public static final int PAYMENTS_FINISH = 1022;

    //Bundle Constants
    public static final int ADDEDITBENREQUESTCODE = 3214;
    public static final int ADDEDITTESTREQUESTCODE = 3457;
    public static final String PE_TEST_LIST_MODEL = "petestlistmodel";
    public static final String VISIT_ORDER_DETAILS_MODEL = "visitOrderDetailsModel";
    public static final String CAMP_ORDER_DETAILS_MODEL = "campOrderDetailsModel";
    public static final String BENEFICIARY_DETAILS_MODEL = "beneficiaryDetailsModel";
    public static final String ADD_BEN_SELECTED_TESTLIST = "addBenSelectedTestlist";
    public static final String ADD_BEN_SELECTED_TESTLISTPE = "addBenSelectedTestlistPE";
    public static final String EDIT_BEN_SELECTED_TESTLIST = "EditBenSelectedTestlist";
    public static final String ORDER_DETAILS_MODEL = "orderDetailsModel";
    public static final String HUB_BTECH_MODEL = "hubBtechModel";
    public static final String LME_ORDER_MODEL = "lmeOrderModel";
    public static final String BTECH_CLIENTS_MODEL = "btechClientsModel";

    public static final String IS_BENEFICIARY_EDIT = "isBeneficiaryEdit";
    public static final String IS_BENEFICIARY_ADD = "isBeneficiaryAdd";
    public static final String CAMP_SCAN_OR_RESPONSE_MODEL = "campResponseModel";
    public static final String PAYMENTS_NARRATION_ID = "paymentsNarrationId";
    public static final String PAYMENTS_ORDER_NO = "paymentsOrderNo";
    public static final String PAYMENTS_AMOUNT = "paymentsAmount";
    public static final String PAYMENTS_SOURCE_CODE = "paymentsSourceCode";
    public static final String PAYMENT_STATUS = "PaymentStatus";
    public static final String PAYMENTS_OPTION_FLAG = "paymentsOptionFlag";
    public static final String PAYMENTS_BILLING_NAME = "paymentsBillingStatus";
    public static final String PAYMENTS_BILLING_ADDRESS = "paymentsBillingAddress";
    public static final String PAYMENTS_BILLING_PIN = "paymentsBillingPin";
    public static final String PAYMENTS_BILLING_MOBILE = "paymentsBillingMobile";
    public static final String PAYMENTS_BILLING_EMAIL = "paymentsBillingEmail";

    public static final String CHECK_PAYMENT_RESPONSE_JSON_REQUEST = "checkPaymentResponseJsonRequest";
    public static final String CAMP_ALL_ORDER_DETAIL = "CampAllOrderDetail";
    public static final String PAYMENTS_RECHECK_REQUEST = "paymentsRecheckRequest";
    public static final String PAYMENTS_RECHECK_REQUEST_URL = "paymentsRecheckRequestURL";
    public static final String IS_TEST_EDIT = "isTestEdit";
    public static final String IS_MOBILE_EMAIL_EDIT = "isMobileEmailEdit";
    public static final String BRAND_ID = "brandId";
    public static final String SELECTED_TESTS_LIST = "selectedTestsList";
    public static final String WHEREFROM = "cameFrom";
    public static final String EDITSELECTEDTEST = "SelectedTest";
    public static final String PEADDON = "PEAddON";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String POSITION = "position";
    public static final String URINE = "URINE";
    public static final String BENPRODUCT = "BENPRODUCT";
    public static final String RELEASE_REMARKS = "RELEASE_REMARKS";
    public static final String POS = "POS";
    public static final String ORDER = "OrderNo";
    public static final String RES_REMARKS = "RES_REMARKS";
    public static final String RES_RESPONSE = "RES_RESPONSE";
    public static final String APPOINTMENT_DATE = "APPOINTMENT_DATE";
    public static final String REMARKS_ID = "REMARKS_ID";
    public static final String PE_OTP_ARRIVED = "OTP ARRIVED VERIFIED";
    public static final Integer EVENT_STATUS = 60;
    public static String PINCODE="PINCODE";

    // neha G --------------------------
    public static int DataInVisitModel = 0;
    public static int OrderAccept = 0;
    public static int not_avail_tom = 0;
    public static int Day_aftr_tom = 0;
    public static String ShowTimeInNotificatn = "";
    public static int DoneworkOrder = 0;
    public static int delay = 0;
    public static String statusBen = "Select Beneficiary";
    public static String KITSNOTAVAILABLE = "Kits not available.";
    public static int RemoveBenId = 0;
    public static String VISIT_ID = "OrderId";
    public static String FlagAcceptReject = "FlagAcceptRej";
    //neha G---------------------------------

    public static final String YES_ACTION = "YES_ACTION";
    public static final String NO_ACTION = "NO_ACTION";
    public static String YESNO_ID = "yesno";
    public static String ORDER_SLOTID = "orderslotid";
    public static boolean ORDER_Notification = false;
    public static int batch_code = 0;

    //Flag 1 for Face detection, 0 for disable, 2 for faceCount
    public static int Flag_facedetection = 0;
    public static boolean b_facedetection = true;
    public static SampleDropDetailsbyTSPLMEDetailsModel setsampleDropDetailsModel;
    public static String locationText = "Select Location";
    public static ArrayList<Venupunture_Temporary_ImageModel> TempVenuImageArylist = new ArrayList<>();
    public static boolean isBarcodeConfirmPopupShown = false;

    public static final int HARDCOPY_CHARGES = 75;

    public static final int APPID_TRACKACTIVITY = 2;
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";

    public static final String API_FOR_OTP = "sNhdlQjqvoD7zCbzf56sxppBJX3MmdWSAomi@RBhXRrVcGyko7hIzQ==";
    public static final String Apikey_WOE = "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=";
    public static int addPaymentFlag = 0;
    public static int setRefreshStartArriveActivity = 0;
    public static int setStechDialogFlag = 0;
    public static boolean isKIOSKOrder = false;

    public static String WOE = "WOE";
    public static int callOTPFlag = 0;
    public static String PROCESSING_LOCATION="processing_location";
    public static String ADDRESS="address";
    public static String newTimeTWO="newTimeTWO";
    public static String newTimeTWOHalf = "newTimeTWOHalf";
    public static String isNBT = "isNBT";
    public static String PERATES ="PETestRates";
    public static int days = 7;
    public static String B2BLogin ="Y";
    public static String CheckInternetConnection ="Please check internet connection.";
    public static String Reschedule = "OrderReschedule";
    public static String setSelectedOrder="";
    public static boolean setVisitOrderScreen = false;
    public static String EDIT_TEST = "EDIT_TESTS";
    public static boolean setPEOrderEdit = false;
    public static boolean setSecondOrderFlag = false;
    public static String OrderReleaseOptionsID ="58";
    public static Integer ToDisplayTimerOnSelectedPosition = 381;

    public static boolean isPEPartner = false;
    public static boolean PEDSAOrder = false;

    public static String VenepunctureImage_path ="";
}
