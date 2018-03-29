package com.thyrocare.utils.app;

import android.net.Uri;


public interface AppConstants {

	// ------------ CONSTANTS -------------

	public static final int ANDROID_APP_VERSION = 21;

    public static final boolean IS_DEBUG = true;

	public static final int GET_METHOD = 0;

	public static final int POST_METHOD = 1;

	public static final int PUT_METHOD = 2;

	public static final int BTECH_APP_ID = 1;

//===================================================
	public static final String  BTECH_ROLE_ID = "4";

	public static final String NBT_ROLE_ID = "13";

	public static final String TSP_ROLE_ID = "9";

	public static final String NBTTSP_ROLE_ID = "12";

	public static final String HUB_ROLE_ID = "6";
	//=================================================

	public static final int DELETE_METHOD = 3;

	public static final String ERROR_MESSAGE = "Error";

	public static final String ERROR_MESSAGE_OPTIONAL = "ERROR-BUSSINESS";

	public static final String SUCCESS_MESSAGE = "SUCCESS";

	public static final String SUCCESS_MESSAGE_OPTIONAL = "SUCCESS-BUSSINESS";

	public static final String MSG_SERVER_EXCEPTION = "Something went wrong. Please try after sometime.";

//    public static final String MSG_INTERNET_CONNECTION_SLOW = "Internet connection is slow, please try again.";

	public static final String MSG_INTERNET_CONNECTION_SLOW = "Unable to connect to server. Please try again.";

	public static final String MSG_COMMUNICATION_PROBLEM = "Communication problem, please try after sometime.";

	public static final String MSG_NETWORK_ERROR = "Network error! Please try after some time.";

	public static final String MSG_UNKNOW_ERROR = "Unknow error! Please try again.";

	public static final String MESSAGE = "MESSAGE";

	public static final long SPLASH_SCREEN_TIMEOUT = 2000;
	public static final String ERROR_MSG = "ERR-BUSS";

	public static final String SUCCESS_MSG = "SUCCESS";
	public static final String HEADER_CONTENT_TYPE_KEY = "Content-Type";

	public static final String HEADER_CONTENT_TYPE_VALUE = "application/json";
	public static final String NOMEDIA_FILE = ".nomedia";

	public static final String EXTERNAL_DIR = "/DHB";

	public static final String OFFLINE_STATUS_ACTION_DONE = "com.app.thyrocare.offlineStatusDone";
	public static final String OFFLINE_STATUS_ACTION_NO_DATA = "com.app.thyrocare.offlineStatusNoData";
	public static final String OFFLINE_STATUS_ACTION_ISSUE = "com.app.thyrocare.offlineStatusIssue";
	public static final String OFFLINE_STATUS_ACTION_IN_PROGRESS = "com.app.thyrocare.offlineStatusInProgress";
	public static final String OFFLINE_TOTAL_COUNT = "offline_total_count";
	public static final String OFFLINE_COMPLETED_COUNT = "offline_completed_count";
	public static final String OFFLINE_ISSUE_FOUND = "offline_issue_found";
	public static final String OFFLINE_UPLOAD_IMAGE_COMPLETE = "offline_upload_image_complete";
	public static final String OFFLINE_UPLOAD_IMAGE_SHOW_TOAST = "offline_upload_image_toast_show";

	public static final String MASTER_TABLE_UPDATE_ACTION_DONE = "com.app.thyrocare.masterTableUpdateStatusDone";
	public static final String MASTER_TABLE_UPDATE_ACTION_NO_DATA = "com.app.thyrocare.masterTableUpdateStatusNoData";
	public static final String MASTER_TABLE_UPDATE_ACTION_ISSUE = "com.app.thyrocare.masterTableUpdateStatusIssue";
	public static final String MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS = "com.app.thyrocare.masterTableUpdateStatusInProgress";
	public static final String MASTER_TABLE_UPDATE_TOTAL_COUNT = "master_table_update_total_count";
	public static final String MASTER_TABLE_UPDATE_COMPLETED_COUNT = "master_table_update_completed_count";
	public static final String MASTER_TABLE_UPDATE_ISSUE_FOUND = "master_table_update_issue_found";

	public static final String CHECK_PAYMENT_RESPONSE_ACTION_DONE = "com.app.thyrocare.checkPaymentResponseStatusDone";
	public static final String CHECK_PAYMENT_RESPONSE_ACTION_NO_DATA = "com.app.thyrocare.checkPaymentResponseStatusNoData";
	public static final String CHECK_PAYMENT_RESPONSE_ACTION_ISSUE = "com.app.thyrocare.checkPaymentResponseStatusIssue";
	public static final String CHECK_PAYMENT_RESPONSE_ACTION_IN_PROGRESS = "com.app.thyrocare.checkPaymentResponseStatusInProgress";
	public static final String CHECK_PAYMENT_RESPONSE_ISSUE_FOUND = "check_payment_response_issue_found";

	public static final int APP_PERMISSIONS = 1001;
	public static final String PPBS = "PPBS";
	public static final String INSPP = "INSPP";
	public static final String  FBS ="FBS" ;
    public static final String INSFA = "INSFA";
	public static final String API_KEY = "dNBJfkhL5z8ng3lsNOuAEL2D73G@FDOC0z3fvmKQjiw=";
}