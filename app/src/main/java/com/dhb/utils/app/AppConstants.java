package com.dhb.utils.app;

import android.net.Uri;


public interface AppConstants {

	// ------------ CONSTANTS -------------

	public static final String ANDROID_APP_VERSION = "1.2.4";

	//public static final String LOGIN_TYPE = "Reception";
	//public static final String LOGIN_TYPE = "Doctor";
	//public static final String LOGIN_TYPE = "Pharmacist";
	public static final String LOGIN_TYPE = "Lab Tech User";

	public static final String LOGIN_TYPE_LABTECH = "Pathology";
//	public static final String LOGIN_TYPE_LABTECH = "Radiology";

	public static final int GET_METHOD = 0;

	public static final int POST_METHOD = 1;

	public static final String PINCODE_GOV_DATA_API_KEY = "22c9cbd39f3d7d5c73ef5c6c85a72a63";

	public static final int PUT_METHOD = 2;

	public static final int DELETE_METHOD = 3;

	public static final int SELECT_PICTURE_FROM_GALLERY = 64;

	public static final int REQUEST_TAKE_PHOTO = 128;

	public static final int PIC_CROP = 32;

	public static final int CHAT_LIMIT = 10;
	public static final int CHAT_LIMIT_LOADER = 11;

	public static final Uri PATIENT_QUEUE_CONTENT_URI = Uri.parse("dhbcontent://com.dhb/queue");

	public static final Uri PATIENT_VISIT_CONTENT_URI = Uri.parse("dhbcontent://com.dhb/visit");

	public static final Uri FAMILY_CONTENT_URI = Uri.parse("dhbcontent://com.dhb/FAMILY_MEMBER_INFO");

	public static final Uri PATIENT_DOCUMENT_URI = Uri.parse("dhbcontent://com.dhb/PATIENT_DOCUMENT_INFO");

	public static final Uri PATIENT_SAMPLE_URI = Uri.parse("dhbcontent://com.dhb/patient_samples");

	public static final String ERROR_MESSAGE = "Error";

	public static final String ERROR_MESSAGE_OPTIONAL = "ERROR-BUSSINESS";

	public static final String SUCCESS_MESSAGE = "SUCCESS";

	public static final String SUCCESS_MESSAGE_OPTIONAL = "SUCCESS-BUSSINESS";

	public static final String MSG_NO_MORE_RECORDS = "No more records found";

	public static final String MSG_CONFIGURATION_FILE_MISSING = "Important configuration files were not find, application will exit now.Please restart the appliction.";

	public static final String MSG_SERVER_EXCEPTION = "Something went wrong. Please try after sometime.";

//    public static final String MSG_INTERNET_CONNECTION_SLOW = "Internet connection is slow, please try again.";

	public static final String MSG_INTERNET_CONNECTION_SLOW = "Unable to connect to server. Please try again.";

	public static final String MSG_COMMUNICATION_PROBLEM = "Communication problem, please try after sometime.";

	public static final String MSG_NETWORK_ERROR = "Network error! Please try after some time.";

	public static final String MSG_UNKNOW_ERROR = "Unknow error! Please try again.";
	public static final String PHARMACY_TEXT = "Pharmacy";

	public static final String CODE_TABLES_VERSION_DATE = "codeTablesVersionDate";


	public static final String MESSAGE = "MESSAGE";

	public static final long SPLASH_SCREEN_TIMEOUT = 2000;
	public static final String ERROR_MSG = "ERR-BUSS";

	public static final String SUCCESS_MSG = "SUCCESS";
	public static final String HEADER_CONTENT_TYPE_KEY = "Content-Type";

	public static final String HEADER_CONTENT_TYPE_VALUE = "application/json";
	public static final String NOMEDIA_FILE = ".nomedia";

	public static final String EXTERNAL_DIR = "/DHB";

	public static final String RECEPTION_PRESENT = "0";
	public static final String DOCTOR_PRESENT = "1";
	public static final String DEPARTMENT_PRESENT = "2";
	public static final String NO_SHOW = "3";
	public static final String EXAMINED = "4";
	public static final String PHARMACY_PRESENT = "5";
	public static final String PATHOLOGY_PRESENT = "6";
//	public static final String PHARMACY_TEXT = "Pharmacy";


	// public static final DisplayImageOptions COMPANY_IMAGE_OPTIONS = new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnLoading(R.drawable.img_company_default).showImageForEmptyUri(R.drawable.img_company_default).cacheOnDisk(true).resetViewBeforeLoading(true).build();
	public static final String OFFLINE_MESSAGE = "Are you sure you want to Sign out?";
	public static final String DELETED_RECORD_STATUS = "D";
	public static final String ACTIVE_RECORD_STATUS = "A";
	public static final String PENDING_RECORD_STATUS = "P";
	public static final String WORK_ORDER_ENTRY_OFFLINE_STATUS = "OFFLINE";
	public static final String INSERT = "I";
	public static final String UPDATE = "U";
	public static final String STATUS_NON_SYNC = "0";
	public static final String STATUS_SYNCED = "1";
	public static final String DOCUMENT_TYPE_PRESCRIPTION = "Prescription";
	public static final int DOCUMENT_TYPE_PRESCRIPTION_ID = 2;
	public static final String DOCUMENT_TYPE_TREATMENT_PLAN = "Treatment Plan";
	public static final String DOCUMENT_TYPE_IMAGE = "Images";
	public static final String DOCUMENT_TYPE_LAB_REPORTS = "Lab Report";
	public static final int DOCUMENT_TYPE_LAB_REPORTS_ID = 3;
	public static final int DOCUMENT_TYPE_IMAGES_ID = 4;
	public static final String DOCUMENT_TYPE_IMAGES = "Images";
	public static final String DOCUMENT_TYPE_PATIENT_REGISTRATION = "Patient Registration";
	public static final int DOCUMENT_TYPE_PATIENT_REGISTRATION_ID = 1;
	public static final String DOCUMENT_TYPE_PROFILE = "Profile";
	public static final int DOCUMENT_TYPE_PROFILE_ID = 6;
	public static final int DOCUMENT_TYPE_PATIENT_PHOTOS_ID = 5;
	public static final String DOCUMENT_TYPE_PATIENT_PHOTOS = "Patient Photos";
	public static final String DOCUMENT_TYPE_CLINICAL_ORDERS = "Lab/Clinical Order";
	public static final String DOCUMENT_TYPE_VITALS = "Vitals";
	public static final String PROFILE_PHOTO = "Patient Photo";
	public static final String DOCUMENT_TYPE_SCAN_SUGGESTION = "Suggestion";
	public static final int DOCUMENT_TYPE_SCAN_SUGGESTION_ID = 7;
	/*public static final String DOCTOR = "Doctor";
	public static final String RECEPTION = "Reception";*/
	public static final String PATHOLOGY = "Lab Tech User";
	/*public static final String PHARMACY = "Pharmacist";*/
	public static final String MY_LIST = "My List";
	public static final String COLLECT_SAMPLE = "Collect Samples";
	public static final String UPLOAD_RESULT = "Upload Results";
	public static final String COMPLETED = "Completed";
	public static final String REGULAR = "Regular";
	public static final String OCCASIONAL = "Occasional";
	public static final String YES = "1";
	public static final String NO = "0";
	public static final String SPECIAL_INSTRUCTION = "special_instruction";
	public static final String TEMPLET_ID = "templet_id";
	public static String PRESCRITION_ID = "prescription_id";
	public static final int INSPECTION_TYPE_VISIT = 1;
	public static final int INSPECTION_TYPE_FOLLOWUP = 2;
	public static final String MY_LIST_NAVIGATION = "my_list_navigation";


	public static final String PATIENT_ID_PROOF_TYPE_PASSPORT = "Passport";
	public static final String PATIENT_ID_PROOF_TYPE_PANCARD = "Pan Card";
	public static final String PATIENT_ID_PROOF_TYPE_ADHARCARD = "Adhaar Card";
	public static final String PATIENT_ID_PROOF_TYPE_VOTER_ID = "Voter Id";
	public static final String PATIENT_ID_PROOF_TYPE_NONE = "None";

	public static final String BLOOD_GROUP_A_POS = "A+";
	public static final String BLOOD_GROUP_B_POS = "B+";
	public static final String BLOOD_GROUP_AB_POS = "AB+";
	public static final String BLOOD_GROUP_A_NEG = "A-";
	public static final String BLOOD_GROUP_B_NEG = "B-";
	public static final String BLOOD_GROUP_AB_NEG = "AB-";
	public static final String BLOOD_GROUP_O_POS = "O+";
	public static final String BLOOD_GROUP_O_NEG = "O-";

//	public static final DisplayImageOptions OPTIONS = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).showImageForEmptyUri(android.R.color.transparent).build();

	public static final String TRANSACTION_ID_KEY = "transaction_id";
	public static final String USER_ID_KEY = "user_id";

	public static final int VISIT_LIMIT = 6;

	public static final String OFFLINE_STATUS_ACTION_DONE = "com.app.dhb.offlineStatusDone";
	public static final String OFFLINE_STATUS_ACTION_NO_DATA = "com.app.dhb.offlineStatusNoData";
	public static final String OFFLINE_STATUS_ACTION_ISSUE = "com.app.dhb.offlineStatusIssue";
	public static final String OFFLINE_STATUS_ACTION_IN_PROGRESS = "com.app.dhb.offlineStatusInProgress";
	public static final String OFFLINE_TOTAL_COUNT = "offline_total_count";
	public static final String OFFLINE_COMPLETED_COUNT = "offline_completed_count";
	public static final String OFFLINE_ISSUE_FOUND = "offline_issue_found";
	public static final String OFFLINE_UPLOAD_IMAGE_COMPLETE = "offline_upload_image_complete";
	public static final String OFFLINE_UPLOAD_IMAGE_SHOW_TOAST = "offline_upload_image_toast_show";

	public static final String MASTER_TABLE_UPDATE_ACTION_DONE = "com.app.dhb.masterTableUpdateStatusDone";
	public static final String MASTER_TABLE_UPDATE_ACTION_NO_DATA = "com.app.dhb.masterTableUpdateStatusNoData";
	public static final String MASTER_TABLE_UPDATE_ACTION_ISSUE = "com.app.dhb.masterTableUpdateStatusIssue";
	public static final String MASTER_TABLE_UPDATE_ACTION_IN_PROGRESS = "com.app.dhb.masterTableUpdateStatusInProgress";
	public static final String MASTER_TABLE_UPDATE_TOTAL_COUNT = "master_table_update_total_count";
	public static final String MASTER_TABLE_UPDATE_COMPLETED_COUNT = "master_table_update_completed_count";
	public static final String MASTER_TABLE_UPDATE_ISSUE_FOUND = "master_table_update_issue_found";

	public static final String POSTPAID_BILL_TYPE = "1";
	public static final String PREPAID_BILL_TYPE = "2";

	public static final int DISEASE_STATUS_YES = 1;
	public static final int DISEASE_STATUS_NO = 0;

	public static final String IS_MASTER = "1";
	public static final String IS_NO_MASTER = "0";

	public static final String CURRENCY_INDIAN = "INR";

	public static final long DIALOG_OPENING_DELAY = 1000;

	public static final String IS_COLLECTED = "1";
	public static final String IS_NOT_COLLECTED = "0";

	public static final String LOGIN_PATHOLOGY = "Pathology";
	public static final String LOGIN_RADIOLOGY = "Radiology";
	public static final String VACCINATION_INSTRACTION = "*In selected states Pentavalent vaccine (Hepatitis B, Diphtheria, Pertussis, Tetanus and Haemophilus influenzae type b) is given instead of DPT and Hep B.<br><br>**In Japanese Encephalitis (JE) endemic districts.<br><br>3rd to 9th doses of Vitamin A are given at 6 monthly intervals to children 2 -5 years old.<br><br>For pregnant women: Give TT-2 or Booster doses before 36 weeks of pregnancy. However, give these even if more than 36 weeks have passed. Give TT to a woman in labour, if she has not previously received TT.";

	public static final int DOCTOR_LIST = 0;

	public static final int DEPARTMENT_LIST = 1;

	public static final int RECEPTION_LIST = 2;

	public static final long TEST_DIALOG_OPENING_DELAY = 400;

	public static final String SUCCESS = "SUCCESS-BUSSINESS";

	public static final String WOE_SUCCESS = "SUCCESS";

	public static final String UPDATED_AT = "updated_at";


	//Print all constants
	public static final String DISTRICT = "District";

	public static final String DEPARTMENT = "Department";

	public static final String HOSPITAL_NAME = "Hospital Name";

	public static final String ADDRESS_LINE1 = "Address line1";

	public static final String ADDRESS_LINE2 = "Address line2";

	public static final String AREA = "Area";

	public static final String TALUKA = "Taluka";

	public static final String PINCODE = "Pincode";

	public static final String DOCTOR_NAME = "Doctor Name";

	public static final String QUALIFICATION = "Qualification";

	public static final String HIGHER_QUALIFICATION = "Higher Qualification";

	public static final String TITLE = "Title";

	public static final String SPECIALITY = "Speciality";

	public static final String REGISTRATION_NO = "Registration No";

	public static final String PHONE = "Phone";

	public static final String MOBILE = "Mobile";

	public static final String NAME = "Name";

	public static final String DAY = "Day";

	public static final String TIME = "Time";

	public static final String PATIENT_NAME = "Name";

	public static final String AGE = "Age";

	public static final String GENDER = "Gender";

	public static final String DATE_OF_BIRTH = "Date of Birth";

	public static final String PLACE = "Place";

	public static final String VILLAGE = "Village";

	public static final String ADDRESS = "Address";

	public static final String BRANCH_NAME = "Branch Name";

	public static final String DATE = "Date";

	public static final String HIN = "HIN";

	public static final String QUEUE = "queue";
	public static final int TEMPLATE_DEFAULT_YES = 1;
	public static final int TEMPLATE_DEFAULT_NO = 0;

	public static final String PATIENT_ID = "patient_id";

	public static final String WOE_ILS_TYPE = "ILS";
	public static final String WOE_TTL_TYPE = "TTL";

	public static final int APP_PERMISSIONS = 1001;
}