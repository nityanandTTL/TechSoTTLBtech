package com.dhb.utils.app;

/**
 * Created by ISRO on 4/27/2017.
 */
import java.lang.String;


public class BundleConstants {
    //Intent and Activity Result related constants
    public static final int VOMD_START = 1004;
    public static final int VOMD_ARRIVED = 1005;
    public static final int HMD_START = 1006;
    public static final int HMD_ARRIVED = 1007;
    public static final int EDIT_TESTS_START = 1008;
    public static final int EDIT_TESTS_FINISH = 1009;
    public static final int EDIT_START = 1010;
    public static final int EDIT_FINISH = 1011;
    public static final int ADD_START = 1023;
    public static final int ADD_FINISH = 1024;
    public static final int BCMD_START = 1012;
    public static final int BCMD_ARRIVED =1013;
    public static final int CMD_START =1014;
    public static final int CMD_ARRIVED =1015;
    public static final int START_BARCODE_SCAN = 0x0000c0de;
    public static final int PAYMENTS_START = 1021;
    public static final int PAYMENTS_FINISH = 1022;

    //Bundle Constants
    public static final String VISIT_ORDER_DETAILS_MODEL = "visitOrderDetailsModel";
    public static final String CAMP_ORDER_DETAILS_MODEL = "campOrderDetailsModel";
    public static final String BENEFICIARY_DETAILS_MODEL = "beneficiaryDetailsModel";
    public static final String ORDER_DETAILS_MODEL = "orderDetailsModel";
    public static final String HUB_BTECH_MODEL = "hubBtechModel";
    public static final String BTECH_CLIENTS_MODEL = "btechClientsModel";



    public static final String BENEFICIARY_TEST_LIST = "beneficiaryTestList";
    public static final String TESTS_LIST = "testsList";
    public static final String SELECTED_TESTS_LIST = "selectedTestsList";
    public static final String IS_BENEFICIARY_EDIT = "isBeneficiaryEdit";
    public static final String IS_BENEFICIARY_ADD = "isBeneficiaryAdd";
    public static final String SELECTED_TESTS_COST = "selectedTestsCost";
    public static final String SELECTED_TESTS_TOTAL_COST = "selectedTestsTotalCost";
    public static final String REST_BEN_TESTS_LIST = "restBeneficiaryTestList";
    public static final String SELECTED_TESTS_DISCOUNT = "selectedTestsDiscount";
    public static final String SELECTED_TESTS_INCENTIVE = "selectedTestsIncentive";
    public static final String CAMP_SCAN_OR_RESPONSE_MODEL = "campResponseModel";
    public static final String PAYMENTS_NARRATION_ID = "paymentsNarrationId";
    public static final String PAYMENTS_ORDER_NO = "paymentsOrderNo";
    public static final String PAYMENTS_AMOUNT = "paymentsAmount";
    public static final String PAYMENTS_SOURCE_CODE = "paymentsSourceCode";
    public static final String PAYMENT_STATUS = "PaymentStatus";

    public static final String PAYMENTS_BILLING_NAME = "paymentsBillingStatus";
    public static final String PAYMENTS_BILLING_ADDRESS = "paymentsBillingAddress";
    public static final String PAYMENTS_BILLING_PIN = "paymentsBillingPin";
    public static final String PAYMENTS_BILLING_MOBILE = "paymentsBillingMobile";
    public static final String PAYMENTS_BILLING_EMAIL = "paymentsBillingEmail";

    public static final String CHECK_PAYMENT_RESPONSE_JSON_REQUEST = "checkPaymentResponseJsonRequest";
    public static final String CAMP_ALL_ORDER_DETAIL = "CampAllOrderDetail";
    public static final String PAYMENTS_RECHECK_REQUEST = "paymentsRecheckRequest";
    public static final String PAYMENTS_RECHECK_REQUEST_URL= "paymentsRecheckRequestURL";
    public static final String BENEFICIARY_DETAILS_MODEL_ARRAY = "beneficiaryDetailArray";
    public static final String IS_TEST_EDIT = "isTestEdit";
    public static final String BRAND_ID = "brandId";
}
