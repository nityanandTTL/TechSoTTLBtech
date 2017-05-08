package com.dhb.network;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.models.api.response.BtechCollectionsResponseModel;
import com.dhb.models.api.response.BusinessErrorModel;
import com.dhb.models.api.response.DispatchHubDisplayDetailsResponseModel;
import com.dhb.models.api.response.ErrorModel;
import com.dhb.models.api.response.ErrorResponseModel;
import com.dhb.models.api.response.FetchLabAlertMasterAPIResponseModel;
import com.dhb.models.api.response.FetchLedgerResponseModel;
import com.dhb.models.api.response.FetchOrderDetailsResponseModel;
import com.dhb.models.api.response.LoginResponseModel;
import com.dhb.models.api.response.MaterialINVResponseModel;
import com.dhb.models.api.response.MessageModel;
import com.dhb.models.api.response.SelfieUploadResponseModel;
import com.dhb.models.api.response.SessionExpireModel;
import com.dhb.models.data.BrandMasterModel;
import com.dhb.models.data.BrandTestMasterModel;
import com.dhb.models.data.DepositRegisterModel;
import com.dhb.models.data.EarningRegisterModel;
import com.dhb.models.data.LeaveNatureMasterModel;
import com.dhb.models.data.MaterialDetailsModel;
import com.dhb.models.data.SlotModel;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AlertDialogMessage;
import com.dhb.utils.app.AppConstants;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ResponseParser implements AppConstants {


	private Activity activity;

	private Context context;

	private Gson gson;

	private AlertDialogMessage alertDialogMessage;

	private AppPreferenceManager appPreferenceManager;

	private TextView txtErrorMsg;

	private boolean isToSwitchActivity = true;

	private MessageModel messageModel;



	private boolean isToShowErrorDailog = true;

	private boolean isToShowToast = true;

	private static final int SPACE_COUNT_FOR_TOAST_TIME = 10;

	public void setToShowErrorDailog(boolean isToShowErrorDailog) {
		this.isToShowErrorDailog = isToShowErrorDailog;
	}

	public void setToShowToast(boolean isToShowToast) {
		this.isToShowToast = isToShowToast;
	}

	public ResponseParser(Activity activity) {

		this.activity = activity;

		this.context = activity.getApplicationContext();

		gson = new Gson();

		alertDialogMessage = new AlertDialogMessage();

		appPreferenceManager = new AppPreferenceManager(activity);

	}

	public ResponseParser(Context context) {

		this.context = context;

		gson = new Gson();

		alertDialogMessage = new AlertDialogMessage();

		appPreferenceManager = new AppPreferenceManager(context);

	}

	public boolean isSessionExpired(String json, int statusCode) {
		if (statusCode == 401){
			Gson gson = new Gson();
			BusinessErrorModel busiError = gson.fromJson(json, BusinessErrorModel.class );
			if (busiError != null && busiError.getMessages() != null){
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public int countSpacesInMessage(String message) {
		int counter = 0;
		for (int i = 0; i < message.length(); i++){
			if (message.charAt(i) == ' '){
				counter++;
			}
		}
		return counter;
	}

	public boolean parseIntoErrorWithoutPopUp(String json, int statusCode) {

		//For beep
//		startTimerForBeep();

		try {
			switch (statusCode){
			case 401:
				BusinessErrorModel busiError = gson.fromJson(json, BusinessErrorModel.class );
				if (busiError != null && busiError.getMessages() != null){
					return false; //parseAfterError(json, busiError);
				} else {
					SessionExpireModel sessionExpireModel = gson.fromJson(json, SessionExpireModel.class );
					if (sessionExpireModel != null){
						if (activity != null){
							return parseAfterError(json, sessionExpireModel, activity, false);
						} else {
							return parseAfterError(json, sessionExpireModel, context, false);
						}
					}
				}

				break;
			case 400:
				BusinessErrorModel businessErrorModel = gson.fromJson(json, BusinessErrorModel.class );
				if (businessErrorModel != null && businessErrorModel.getMessages() != null){
					return false; //parseAfterError(json, businessErrorModel);
				}

				ErrorResponseModel errorResponseModel = gson.fromJson(json, ErrorResponseModel.class );
				if (errorResponseModel != null && errorResponseModel.getError_description() != null){
					Toast.makeText(activity, errorResponseModel.getError_description().toString(), Toast.LENGTH_LONG).show();
					return false;
				}

				MessageModel messageModel = null;
				try {
					messageModel = gson.fromJson(json, MessageModel.class );
				} catch (JsonSyntaxException e){
					e.printStackTrace();
					return false;

				}
				if (messageModel != null
				    && messageModel.getMessages() != null
				    && (messageModel.getMessages().length > 0)){

					String message = "";

					if (messageModel.getMessages() != null){

						for (MessageModel.FieldError msg : messageModel.getMessages()){

							if (msg != null){
								message = message + msg.getMessage() + ",";
							}

						}

					}

					MessageModel.FieldError msg[] = messageModel.getMessages();

					MessageModel.FieldError fieldError = msg[0];


					//if (msg != null && msg.length>1) {
					if (msg != null){
						message = msg[0].getMessage();
						if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
							AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
							alertDialogMessage1.showAlert(activity, message.toString(), true);
							alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());
						} else if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
							if (message.toString().contains(" ") && countSpacesInMessage(message.toString()) >= SPACE_COUNT_FOR_TOAST_TIME){
								Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show();
							}


						}
					}


					if (message.lastIndexOf(',') != -1){
						message = message.substring(0, message.lastIndexOf(','));
						if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
							AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
							alertDialogMessage1.showAlert(activity, message.toString(), true);
							alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());
						} else if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
							if (message.toString().contains(" ") && countSpacesInMessage(message.toString()) >= SPACE_COUNT_FOR_TOAST_TIME){
								Toast.makeText(activity, message.toString(), Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(activity, message.toString(), Toast.LENGTH_SHORT).show();
							}
						}
					}
				}

				break;

			case 200:
				ErrorModel errorModel = gson.fromJson(json, ErrorModel.class );
				if (errorModel != null && errorModel.getError_description() != null && errorModel.getError() != null && (errorModel.getError().equalsIgnoreCase(SUCCESS_MESSAGE) || errorModel.getError().equalsIgnoreCase(ERROR_MESSAGE))){
					parseAfterError(json, errorModel);
				} else {
					errorModel = null;
					return false;
				}
				break;
			case 201:
				ErrorModel errorModels = null;
//				ErrorModel errorModel = gson.fromJson(json, ErrorModel.class );
//				if (errorModel != null && errorModel.getError_description() != null && errorModel.getError() != null && (errorModel.getError().equalsIgnoreCase(SUCCESS_MESSAGE) || errorModel.getError().equalsIgnoreCase(ERROR_MESSAGE))){
					parseAfterError(json, errorModels);
//				} else {
//					errorModel = null;
//					return false;
//				}
				break;
			case 204:
				ErrorModel errorModela = null;
//				ErrorModel errorModela = gson.fromJson(json, ErrorModel.class );
				parseAfterError(json, errorModela);
				/*if (errorModel != null && errorModel.getError_description() != null && errorModel.getError() != null && (errorModel.getError().equalsIgnoreCase(SUCCESS_MESSAGE) || errorModel.getError().equalsIgnoreCase(ERROR_MESSAGE))){

				} else {
					errorModel = null;
					return false;
				}*/
				break;
			case 500:

				BusinessErrorModel businessErrorModel1 = gson.fromJson(json, BusinessErrorModel.class );
				if (businessErrorModel1 != null){
					return parseInternalServerError(json, businessErrorModel1);
				}
				break;

			case 404:

				BusinessErrorModel businessErrorModel2 = gson.fromJson(json, BusinessErrorModel.class );
				if (businessErrorModel2 != null){
					return parseInternalServerError(json, businessErrorModel2);
				}
				break;
			default:
				ErrorModel defaultErrorModel = gson.fromJson(json, ErrorModel.class );
				if (defaultErrorModel != null && defaultErrorModel.getError_description() != null){
					if (defaultErrorModel.getError_description().equalsIgnoreCase("User logged out successfully")){
						//parseLogoutApi(json);
						return true;
					} else {

						return parseAfterError(json, defaultErrorModel);
					}
				} else {
					return parseAfterError(json, defaultErrorModel);
				}
			}

		} catch (JsonSyntaxException js){

			return true;

		}
		return false;

	}

	public boolean parseIntoError(String json, int statusCode) {

		try {

			switch (statusCode){
			case 401:
				ErrorResponseModel busiError = gson.fromJson(json, ErrorResponseModel.class );
				if (busiError != null && busiError.getError_description() != null){
					return parseAfterError(json, busiError);
				} else {
					SessionExpireModel sessionExpireModel = gson.fromJson(json, SessionExpireModel.class );
					if (sessionExpireModel != null){
						if (activity != null){
							return parseAfterError(json, sessionExpireModel, activity, false);
						} else {
							return parseAfterError(json, sessionExpireModel, context, false);
						}
					}
				}

				break;
			case 400:
				ErrorResponseModel businessErrorModel = gson.fromJson(json, ErrorResponseModel.class );
				if (businessErrorModel != null && businessErrorModel.getError_description() != null){
					return parseAfterError(json, businessErrorModel);
				}

				ErrorResponseModel errorResponseModel = gson.fromJson(json, ErrorResponseModel.class );
				if (errorResponseModel != null && errorResponseModel.getError_description() != null){
					if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)){
						if (errorResponseModel.getError_description().contains(" ") && countSpacesInMessage(errorResponseModel.getError_description()) >= SPACE_COUNT_FOR_TOAST_TIME){
							// if (!appPreferenceManager.isAppInBackground()) {
							Toast.makeText(activity, errorResponseModel.getError_description(), Toast.LENGTH_LONG).show();
							// }
						} else {
							//  if (!appPreferenceManager.isAppInBackground()) {
							Toast.makeText(context, errorResponseModel.getError_description(), Toast.LENGTH_SHORT).show();
							//  }
						}
					}
					return true;
				}

				MessageModel messageModel = null;
				try {
					messageModel = gson.fromJson(json, MessageModel.class );
				} catch (JsonSyntaxException e){
					e.printStackTrace();
					return false;

				}
				if (messageModel != null
				    && messageModel.getMessages() != null
				    && (messageModel.getMessages().length > 0)){

					String message = "";

					if (messageModel.getMessages() != null){

						for (MessageModel.FieldError msg : messageModel.getMessages()){

							if (msg != null){
								message = message + msg.getMessage() + ",";
							}

						}

					}

					MessageModel.FieldError msg[] = messageModel.getMessages();

					MessageModel.FieldError fieldError = msg[0];


					//if (msg != null && msg.length>1) {
					if (msg != null && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
						message = msg[0].getMessage();

						AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
						if (activity != null){
							alertDialogMessage1.showAlert(activity, message.toString(), true);
						} else {
							alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
						}
						//alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());


					}


					if (message.lastIndexOf(',') != -1 && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){

						message = message.substring(0, message.lastIndexOf(','));
						AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();

						if (activity != null){
							alertDialogMessage1.showAlert(activity, message.toString(), true);
						} else {
							alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
						}
						alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

					}
				}

				break;

			case 200:
				ErrorModel errorModel = gson.fromJson(json, ErrorModel.class );
				if (errorModel != null && errorModel.getError_description() != null && errorModel.getError() != null && (errorModel.getError().equalsIgnoreCase(ERROR_MESSAGE)
				                                                                                              || errorModel.getError().equals(ERROR_MESSAGE_OPTIONAL))){
					parseAfterError(json, errorModel);
				} else {
					errorModel = null;
					return false;
				}

			case 500:

				BusinessErrorModel businessErrorModel1 = gson.fromJson(json, BusinessErrorModel.class );
				if (businessErrorModel1 != null){
					return parseInternalServerError(json, businessErrorModel1);
				}
				break;

			case 404:

				BusinessErrorModel businessErrorModel2 = gson.fromJson(json, BusinessErrorModel.class );
				if (businessErrorModel2 != null){
					return parseInternalServerError(json, businessErrorModel2);
				}
				break;
			default:
				ErrorModel defaultErrorModel = gson.fromJson(json, ErrorModel.class );
				if (defaultErrorModel != null && defaultErrorModel.getError_description() != null){
					if (defaultErrorModel.getError_description().equalsIgnoreCase("User logged out successfully")){
						//parseLogoutApi(json);
						return true;
					} else {

						return parseAfterError(json, defaultErrorModel);
					}
				} else {
					return parseAfterError(json, defaultErrorModel);
				}
			}

		} catch (JsonSyntaxException js){

			return true;

		}
		return true;

	}

	private boolean parseAfterError(String json, ErrorModel errorModel) {
		if (errorModel != null && errorModel.getError() != null && errorModel.getError_description() != null){
			if (errorModel.getError_description().equals(MSG_SERVER_EXCEPTION)){

				if (errorModel.getError_description().equals(MSG_INTERNET_CONNECTION_SLOW)){

					onApiFailed(errorModel);
					return false;
				} else if (errorModel.getError_description().equals(MSG_COMMUNICATION_PROBLEM)){

					onApiFailed(errorModel);
					return false;
				} else if (errorModel.getError_description().equals(MSG_NETWORK_ERROR)){

					onApiFailed(errorModel);
					return false;
				} else if (errorModel.getError_description().equals(MSG_UNKNOW_ERROR)){

					onApiFailed(errorModel);
					return false;
				} else if (errorModel.getError_description().equals("Please check your network connection.")){

					onApiFailed(errorModel);
					return false;
				} else if (errorModel.getError().equalsIgnoreCase("ERR-BUSS")){
					onApiFailed(errorModel);
				} else if (errorModel.getError().equals(ERROR_MESSAGE) || errorModel.getError().equals(ERROR_MESSAGE_OPTIONAL) || (errorModel.getError_description() != null)){
					if (!errorModel.getError().equals(SUCCESS_MESSAGE) || !errorModel.getError().equals(SUCCESS_MESSAGE_OPTIONAL)){
						onApiFailed(errorModel);
						return false;
					} else {
						return false;
					}
				}
			} else {

				return true;
			}

		}

		return true;

	}

	private boolean parseAfterError(String json, ErrorResponseModel businessErrorModel) {
		if (businessErrorModel != null && businessErrorModel.getError() != null && businessErrorModel.getError_description() != null){

			if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
				if (activity != null){
					alertDialogMessage.showAlert(activity, businessErrorModel.getError_description(), true);
				} else {
					alertDialogMessage.showAlert(context.getApplicationContext(), businessErrorModel.getError_description(), true);
				}

				alertDialogMessage.setAlertDialogOkListener(new DialogOkButtonListener());
			}

			return false;

		}
		return true;
	}

	private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Activity activity, boolean isDialogCancelable) {
		if (sessionExpireModel != null && sessionExpireModel.getMessage() != null){
			if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
				AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
				alertDialogMessage1.showAlert(activity, sessionExpireModel.getMessage().toString(), isDialogCancelable);
				alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
			}
			return false;
		}
		return true;
	}

	private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Context context, boolean isDialogCancelable) {
		if (sessionExpireModel != null && sessionExpireModel.getMessage() != null){
			if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
				AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
				alertDialogMessage1.showAlert(context, sessionExpireModel.getMessage().toString(), true);
				alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
			}
			return false;
		}
		return true;
	}

	private boolean parseInternalServerError(String json, BusinessErrorModel businessErrorModel) {
		if (businessErrorModel != null && businessErrorModel.getType() != null && businessErrorModel.getMessages() != null
		    && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)){
			// onApiFailed(businessErrorModel);
			AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
			alertDialogMessage1.showAlert(activity, businessErrorModel.getMessages().getMessage().toString(), true);
			//alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

			return false;


		}
		return true;
	}

	private class DialogOkButtonListener implements AlertDialogMessage.AlertDialogOkListener {

		@Override
		public void onAlertDialogOkButtonListener() {

		}

	}

	public void onApiFailed(ErrorModel errorModel) {

		if (errorModel != null){

			if (activity != null && context != null && !DeviceUtils.isAppIsInBackground(context)){
				AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
				alertDialogMessage1.showAlert(activity, errorModel.getError_description().toString(), true);
				// alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

			}

		}

		Logger.debug("inside onapifailed");

	}

	private class InvalidSessionAlertDialogOkButtonListener implements
	AlertDialogMessage.AlertDialogOkListener {

		@Override
		public void onAlertDialogOkButtonListener() {

			AppPreferenceManager appPreferenceManager = new AppPreferenceManager(
			        context);

			/*appPreferenceManager.clearAllPreferences();
			DhbDao dhbDao = new DhbDao(activity);
			dhbDao.deleteDb();


			Intent intent = new Intent(context, LoginActivity.class );
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);*/

		}

	}

	//Login Response parse:
	public LoginResponseModel getLoginResponseModel(String json, int statusCode) {
		LoginResponseModel loginResponseModel = null;
		if (!parseIntoError(json, statusCode)){
			loginResponseModel = gson.fromJson(json, LoginResponseModel.class );
		}
		return loginResponseModel;
	}

	//Fetch Slot Details Response parse:
	public ArrayList<SlotModel> getSlotDetailsResponseModel(String json, int statusCode) {
		ArrayList<SlotModel> slotModels = null;
//		if (!parseIntoError(json, statusCode)){
			TypeToken<ArrayList<SlotModel>> token = new TypeToken<ArrayList<SlotModel>>(){};
			slotModels = gson.fromJson(json,token.getType());
//		}
		return slotModels;
	}

	public ArrayList<BrandMasterModel> getBrandMaster(String json, int statusCode) {
		ArrayList<BrandMasterModel> brandMasters = null;
//		if (!parseIntoError(json, statusCode)){
		TypeToken<ArrayList<BrandMasterModel>> token = new TypeToken<ArrayList<BrandMasterModel>>(){};
		brandMasters = gson.fromJson(json,token.getType());
//		}
		return brandMasters;
	}

	public ArrayList<BrandTestMasterModel> getTestMaster(String json, int statusCode) {
		ArrayList<BrandTestMasterModel> testMasters = null;
//		if (!parseIntoError(json, statusCode)){
		TypeToken<ArrayList<BrandTestMasterModel>> token = new TypeToken<ArrayList<BrandTestMasterModel>>(){};
		testMasters = gson.fromJson(json,token.getType());
//		}
		return testMasters;
	}

	//Selfie Response parse:
	public SelfieUploadResponseModel getSelfieUploadResponseModel(String json, int statusCode) {
		SelfieUploadResponseModel selfieUploadResponseModel = null;
//		if (!parseIntoError(json, statusCode)){
			selfieUploadResponseModel = gson.fromJson(json, SelfieUploadResponseModel.class );
//		}
		return selfieUploadResponseModel;
	}
	//Fetch order details Response parse:
	public FetchOrderDetailsResponseModel getFetchOrderDetailsResponseModel(String json, int statusCode) {
		FetchOrderDetailsResponseModel fetchOrderDetailsResponseModel = null;
		if (!parseIntoError(json, statusCode)){
			fetchOrderDetailsResponseModel = gson.fromJson(json, FetchOrderDetailsResponseModel.class);
		}
		return fetchOrderDetailsResponseModel;
	}
	////Fetch Ledger details Response parse:
	public FetchLedgerResponseModel getFetchledgerDetailsResponseModel(String json, int statusCode) {
		FetchLedgerResponseModel fetchledgerDetailsResponseModel = null;
		if (!parseIntoError(json, statusCode)){
			fetchledgerDetailsResponseModel = gson.fromJson(json, FetchLedgerResponseModel.class);
		}
		return fetchledgerDetailsResponseModel;
	}

	////Fetch Earning details Response parse:
	public ArrayList<EarningRegisterModel> getEarningRegisterResponseModel(String json, int statusCode) {
		ArrayList<EarningRegisterModel> earningRegisterModels = null;
//		if (!parseIntoError(json, statusCode)){
		TypeToken<ArrayList<EarningRegisterModel>> token = new TypeToken<ArrayList<EarningRegisterModel>>(){};
		earningRegisterModels = gson.fromJson(json,token.getType());
//		}
		return earningRegisterModels;
	}

	////Fetch Earning details Response parse:
	public ArrayList<DepositRegisterModel> getDepositRegisterResponseModel(String json, int statusCode) {
		ArrayList<DepositRegisterModel> depositRegisterModels = null;
//		if (!parseIntoError(json, statusCode)){
		TypeToken<ArrayList<DepositRegisterModel>> token = new TypeToken<ArrayList<DepositRegisterModel>>(){};
		depositRegisterModels = gson.fromJson(json,token.getType());
//		}
		return depositRegisterModels;
	}

	//Dispatch to hub  details Response parse:
	public DispatchHubDisplayDetailsResponseModel getDispatchTohubDetailsResponseModel(String json, int statusCode) {
		DispatchHubDisplayDetailsResponseModel dispatchHubDisplayDetailsResponseModel = null;
		//if (!parseIntoError(json, statusCode)){
		dispatchHubDisplayDetailsResponseModel = gson.fromJson(json, DispatchHubDisplayDetailsResponseModel.class);
		//}
		return dispatchHubDisplayDetailsResponseModel;
	}
	//btech collections  details Response parse:
	public BtechCollectionsResponseModel getBtechCollectionsDetailsResponseModel(String json, int statusCode) {
		BtechCollectionsResponseModel btechCollectionsResponseModel = null;
		//if (!parseIntoError(json, statusCode)){
		btechCollectionsResponseModel = gson.fromJson(json, BtechCollectionsResponseModel.class);
		//}
		return btechCollectionsResponseModel;
	}
	//Lab Alert Master Response parse:
	public FetchLabAlertMasterAPIResponseModel getLabAlertMasterAPIResponseModel(String json, int statusCode) {
		FetchLabAlertMasterAPIResponseModel fetchLabAlertMasterAPIResponseModel = null;
		//if (!parseIntoError(json, statusCode)){
		fetchLabAlertMasterAPIResponseModel = gson.fromJson(json, FetchLabAlertMasterAPIResponseModel.class);
		//}
		return fetchLabAlertMasterAPIResponseModel;
	}



	////Fetch MaterialMAster details Response parse:
	public ArrayList<MaterialDetailsModel> getMaterialdetailsResponseModel(String json, int statusCode) {
		ArrayList<MaterialDetailsModel> materialDetailsModels  = null;
//		if (!parseIntoError(json, statusCode)){
		TypeToken<ArrayList<MaterialDetailsModel>> token = new TypeToken<ArrayList<MaterialDetailsModel>>(){};
		materialDetailsModels = gson.fromJson(json,token.getType());
//		}
		return materialDetailsModels;
	}
	////Fetch Ledger details Response parse:
	public MaterialINVResponseModel getMaterialINVDetailsResponseModel(String json, int statusCode) {
		MaterialINVResponseModel materialINVResponseModel = null;
		if (!parseIntoError(json, statusCode)){
			materialINVResponseModel = gson.fromJson(json, MaterialINVResponseModel.class);
		}
		return materialINVResponseModel;
	}
	////Fetch Leave  details Response parse:
	public ArrayList<LeaveNatureMasterModel>  getLeaveNatureMasterResponse (String json, int statusCode) {
		ArrayList<LeaveNatureMasterModel> leaveNatureMasterModels = null;
		TypeToken<ArrayList<LeaveNatureMasterModel>> token =new TypeToken<ArrayList<LeaveNatureMasterModel>>(){};
		leaveNatureMasterModels = gson.fromJson(json,token.getType());
		return leaveNatureMasterModels;
	}
}