package com.dhb.network;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.dhb.R;
import com.dhb.activity.AddEditBeneficiaryDetailsActivity;
import com.dhb.models.api.request.ApplyLeaveRequestModel;
import com.dhb.models.api.request.BtechsRequestModel;
import com.dhb.models.api.request.CallPatchRequestModel;
import com.dhb.models.api.request.CampStartedRequestModel;
import com.dhb.models.api.request.CartAPIRequestModel;
import com.dhb.models.api.request.ChangePasswordRequestModel;
import com.dhb.models.api.request.HubStartRequestModel;
import com.dhb.models.api.request.LocusPushLocationRequestModel;
import com.dhb.models.api.request.LoginRequestModel;
import com.dhb.models.api.request.MasterBarcodeMappingRequestModel;
import com.dhb.models.api.request.MaterialorderRequestModel;
import com.dhb.models.api.request.OlcScanPickUpRequestModel;
import com.dhb.models.api.request.OlcStartRequestModel;
import com.dhb.models.api.request.OrderBookingRequestModel;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.dhb.models.api.request.ResetPasswordRequestModel;
import com.dhb.models.api.request.SelfieUploadRequestModel;
import com.dhb.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.dhb.utils.app.AppPreferenceManager;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskForRequest {

    private AppPreferenceManager appPreferenceManager;
    private Context context;
    private AbstractApiModel abstractApiModel;
    private ApiCallAsyncTask apiCallAsyncTask;

    public AsyncTaskForRequest(Activity activity) {
        super();
        this.context = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public AsyncTaskForRequest(Context context) {
        super();
        this.context = context;
        appPreferenceManager = new AppPreferenceManager(context);
    }

	/*
     * Login Api Integration*/

    public ApiCallAsyncTask getLoginRequestAsyncTask(LoginRequestModel loginRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("UserName", loginRequestModel.getUserName()));
            nameValuePairs.add(new BasicNameValuePair("Password", loginRequestModel.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("grant_type", loginRequestModel.getGrant_type()));

            abstractApiModel.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            abstractApiModel.setHeader(getHeaderContentType(AbstractApiModel.APPLICATION_X_WWW_FROM_URLENCODED));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.LOGIN);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_X_WWW_FROM_URLENCODED);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_authenticating_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
	/*
     * Call Mask Api Integration*/

    public ApiCallAsyncTask getCallPatchRequestAsyncTask(CallPatchRequestModel callPatchRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Srcnumber", callPatchRequestModel.getSrcnumber()));
            nameValuePairs.add(new BasicNameValuePair("DestNumber", callPatchRequestModel.getDestNumber()));
            nameValuePairs.add(new BasicNameValuePair("VisitID", callPatchRequestModel.getVisitID()));

            abstractApiModel.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            abstractApiModel.setHeader(getHeaderContentType(AbstractApiModel.APPLICATION_X_WWW_FROM_URLENCODED));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PATCH_CALL_REQUEST);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_X_WWW_FROM_URLENCODED);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_authenticating_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
	/*
     * Logout Api Integration*/

    public ApiCallAsyncTask getLogoutRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.LOGOUT);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_authenticating_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
	/*
	 * Selfie Upload Api Integration*/

    public ApiCallAsyncTask getSelfieUploadRequestAsyncTask(SelfieUploadRequestModel selfieUploadRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(selfieUploadRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SELFIE_UPLOAD);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_uploading_selfie_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Order Booking Api Integration*/

    public ApiCallAsyncTask getOrderBookingRequestAsyncTask(OrderBookingRequestModel orderBookingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderBookingRequestModel);
            /*// Logging the response that is too large for logcat in multiple lines -- START
            int maxLogSize = 500;
            for (int i = 0; i <= postJson.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > postJson.length() ? postJson.length() : end;
                Log.v(AddEditBeneficiaryDetailsActivity.class.getSimpleName(), postJson.substring(start, end));
            }
            // Logging the response that is too large for logcat in multiple lines -- END
*/


            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_BOOKING);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_uploading_order_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


    public ApiCallAsyncTask getAddBeneficiaryRequestAsyncTask(OrderBookingRequestModel orderBookingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderBookingRequestModel);
           /* // Logging the response that is too large for logcat in multiple lines -- START
            int maxLogSize = 500;
            for (int i = 0; i <= postJson.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > postJson.length() ? postJson.length() : end;
                Log.v(AddEditBeneficiaryDetailsActivity.class.getSimpleName(), postJson.substring(start, end));
            }
            // Logging the response that is too large for logcat in multiple lines -- END

*/          abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ADD_BENEFICIARY);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_uploading_order_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Work Order Entry Api Integration*/

    public ApiCallAsyncTask getWorkOrderEntryRequestAsyncTask(OrderBookingRequestModel orderBookingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderBookingRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.WOE);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_performing_work_order_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Order Status Change Api Integration*/

    public ApiCallAsyncTask getOrderStatusChangeRequestAsyncTask(OrderStatusChangeRequestModel orderStatusChangeRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderStatusChangeRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_STATUS_CHANGE+"/"+orderStatusChangeRequestModel.getId());
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_changing_order_status_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
  /*
	 *Camp Started Api Integration*/

    public ApiCallAsyncTask getCampStartedRequestAsyncTask(CampStartedRequestModel campStartedRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(campStartedRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMP_STARTED/*+"/"+orderStatusChangeRequestModel.getId()*/);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_changing_order_status_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Set Btech Slot Availability Api Integration*/

    public ApiCallAsyncTask getPostBtechAvailabilityRequestAsyncTask(SetBtechAvailabilityAPIRequestModel setBtechAvailabilityAPIRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(setBtechAvailabilityAPIRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_AVAILABILITY);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_changing_order_status_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
	/*
	 * Fetch Order Detail Api Integration*/

    public ApiCallAsyncTask getFetchOrderDetailsRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchOrderDetailsRequestAsyncTask: " );
           // String postJson = new Gson().toJson(orderVisitDetailsModel);
          //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_ORDER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_order_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }/*
	 * get Camp Details Count Api Integration*/

    public ApiCallAsyncTask getCampDetailsCountRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getCampDetailsCountRequestAsyncTask: " );
           // String postJson = new Gson().toJson(orderVisitDetailsModel);
          //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMP_COUNT+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_camp_count_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }/*
	 * Send QR code Api Integration*/

    public ApiCallAsyncTask getSendQRCodeRequestAsyncTask(String qrContent) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getSendQRCodeRequestAsyncTask: " );
           // String postJson = new Gson().toJson(orderVisitDetailsModel);
          //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMP_QR_DETAIL+"/"+/*"VISTT001109154"*/qrContent);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_sending_master_barcode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
	/*
	 * Fetch Lab Alert Master Api Integration*/

    public ApiCallAsyncTask getFetchLabAlertMasterAPIRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.LAB_ALERT_MASTER);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_order_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Fetch Slot Details Api Integration*/

    public ApiCallAsyncTask getFetchSlotDetailsRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_SLOT_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_slot_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Fetch Brand Master Api Integration*/

    public ApiCallAsyncTask getFetchBrandMasterRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_BRAND_MASTER);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_brand_master_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Fetch Brand wise Test Master Api Integration*/

    public ApiCallAsyncTask getFetchBrandwiseTestMasterRequestAsyncTask(String BrandId) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_BRAND_WISE_TEST_MASTER+"/"+BrandId);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_test_master_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

     /*
	 * Fetch Ledger Detail Api Integration*/

    public ApiCallAsyncTask getFetchLedgerDetailsRequestAsyncTask(String fromdate ,String todate){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchLedgerDetailsRequestAsyncTask: " );
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_LEDGER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+fromdate+"/"+todate);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Ledger_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


  /*
	 * Fetch Earning Register Api Integration*/

    public ApiCallAsyncTask getFetchEarningDetailsRequestAsyncTask(String fromdate ,String todate){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_DEPOSITREGISTER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+fromdate+"/"+todate);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Earning_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


    /*
	 * Fetch Deposit Register Api Integration*/

    public ApiCallAsyncTask getFetchDepositDetailsRequestAsyncTask(String fromdate ,String todate){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_EARNINGREGISTER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+fromdate+"/"+todate);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Deposit_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

      /*
	 * Fetch Material Master Api Integration*/

    public ApiCallAsyncTask getMaterialsDetailsRequestAsyncTask(String Category){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALMASTER_DETAIL+"/"+Category);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Materials_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }



     /*
	 * Fetch MAterialINV Detail Api Integration*/

    public ApiCallAsyncTask getMaterialINVDetailsRequestAsyncTask(){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALINV_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_MaterialsINV_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }



       /*
* Set MaterialOrder Api Integration*/

    public ApiCallAsyncTask getPostMaterialOrderAsyncTask(MaterialorderRequestModel setmaterialorderRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(setmaterialorderRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SET_MATERIALORDER_DETAIL);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_changing_MaterailsOrder_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

     /*
* Set MaterialINv Api Integration*/

    public ApiCallAsyncTask getPostMaterialInvRequestAsyncTask(BtechsRequestModel setMaterialInvAPIRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(setMaterialInvAPIRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SET_BTECH_MATERIAL_INV);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_changing_MaterailsINV_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Master Barcode mapping Api Integration*/

    public ApiCallAsyncTask getMasterBarcodeMapRequestAsyncTask(MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(masterBarcodeMappingRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.MASTER_BARCODE_MAPPING);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_mapping_master_barcode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Hub Start Api Integration*/

    public ApiCallAsyncTask getHubStartRequestAsyncTask(HubStartRequestModel hubStartRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(hubStartRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.HUBSTART);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_hub_start_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }  /*
	 * OLC Start Api Integration*/

    public ApiCallAsyncTask getOlcStartRequestAsyncTask(OlcStartRequestModel olcStartRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(olcStartRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.OLCSTART);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_olc_start_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Fetch btech collection list Api Integration*/

    public ApiCallAsyncTask getBtechCollectionListRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_COLLECTIONS+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_btech_collections_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Dispatch Hub Detail Display Detail Api Integration*/

    public ApiCallAsyncTask getDispatchHubDetailsDisplayRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_HUB_DETAILS_DISPLAY+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_dispatch_to_hub_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    // Order Served list
    public ApiCallAsyncTask getOrderServedDetailsDisplayRequestAsyncTask(String date) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_SERVED_DETAILS_DISPLAY+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+date);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_order_serverd_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    } /*
	 * Btech Clients List (OLC) Display Detail Api Integration*/

    public ApiCallAsyncTask getBtechClientsListDetailsDisplayRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_CLIENTS_DETAILS_DISPLAY+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_dispatch_to_hub_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
/*
	 * Camp details List  Api Integration*/

    public ApiCallAsyncTask getCampListDetailsDisplayRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMPS_DETAILS_DISPLAY+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_camp_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

     /* Set ApplyLeave Api Integration*/

    public ApiCallAsyncTask getPostApplyLeaveRequestAsyncTask(ApplyLeaveRequestModel setApplyLeaveRequestModel ) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(setApplyLeaveRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SET_APPLY_LEAVE);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_Applying_Leave_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Fetch Leave Details  Api Integration*/

    public ApiCallAsyncTask getLeaveDetailsRequestAsyncTask(){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_LEAVE_NATURE_MASTER);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Leave_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


     /*
	 * Fetch VersionControl Details  Api Integration*/

    public ApiCallAsyncTask getVersionControlDetailsRequestAsyncTask(){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_VERSION_CONTROL_DETAIL);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Version_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


    /*
	 * Change Password Api Integration*/

    public ApiCallAsyncTask getChangePasswordRequestAsyncTask(ChangePasswordRequestModel changePasswordRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(changePasswordRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CHANGE_PASSWORD);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_change_password_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Reset Password Api Integration*/

    public ApiCallAsyncTask getResetPasswordRequestAsyncTask(ResetPasswordRequestModel resetPasswordRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(resetPasswordRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.RESET_PASSWORD);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_reset_password_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Reset Password Api Integration*/

    public ApiCallAsyncTask getRemoveBeneficiaryRequestAsyncTask(RemoveBeneficiaryAPIRequestModel removeBeneficiaryAPIRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(removeBeneficiaryAPIRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.REMOVE_BENEFICIARY);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_removing_beneficiary_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Scan Pickup Api Integration*/

    public ApiCallAsyncTask getScanPickupRequestAsyncTask(OlcScanPickUpRequestModel olcScanPickUpRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(olcScanPickUpRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.OLC_PICKUP_SUBMIT_SCAN_BARCODE);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_updating_scanned_barcode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Fetch Narration Master Api Integration*/

    public ApiCallAsyncTask getNarrationMasterRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_NARRATION_MASTER);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_narration_master_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Fetch Payment Modes From Narration Id Api Integration*/

    public ApiCallAsyncTask getPaymentModesFromNarrationIdRequestAsyncTask(int narrationId) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_SELECT_MODE+"/"+narrationId);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_payment_modes_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Fetch Payment Pass Inputs Api Integration*/

    public ApiCallAsyncTask getTransactionInputsRequestAsyncTask(JSONObject jsonRequest) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setPostData(jsonRequest.toString());
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_PASS_INPUTS);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_payment_modes_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Start Transaction Api Integration*/

    public ApiCallAsyncTask getStartTransactionRequestAsyncTask(JSONObject jsonRequest) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setPostData(jsonRequest.toString());
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_START_TRANSACTION);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_start_transaction_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Do Capture Response Api Integration*/

    public ApiCallAsyncTask getDoCaptureResponseRequestAsyncTask(JSONObject jsonRequest,String URL) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setPostData(jsonRequest.toString());
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(URL);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_payments_otp_verification_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Recheck Payment Response Response Api Integration*/

    public ApiCallAsyncTask getRecheckPaymentResponseRequestAsyncTask(String jsonRequest,String URL) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setPostData(jsonRequest);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(URL);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_payment_status_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Cart Api Integration*/

    public ApiCallAsyncTask getCartRequestAsyncTask(CartAPIRequestModel cartAPIRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            String postJson = new Gson().toJson(cartAPIRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CART);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_payment_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Locus Push Geo Location Api Integration*/

    public ApiCallAsyncTask getLocusPushGeoLocationRequestAsyncTask(LocusPushLocationRequestModel locusPushLocationRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            String postJson = new Gson().toJson(locusPushLocationRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeaderLocus(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(abstractApiModel.LOCUS_PUSH_LOCATIONS_API+appPreferenceManager.getLoginResponseModel().getUserID()+"/location");
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    private List<HeaderData> getHeader(String contentType) {
        HeaderData headerData = new HeaderData();
        headerData.setHeaderKey(AbstractApiModel.AUTHORIZATION);
        headerData.setHeaderValue("Bearer " + appPreferenceManager.getAPISessionKey());

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue(contentType);

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData);
        header.add(headerData1);
        return header;
    }

    private List<HeaderData> getHeaderLocus(String contentType) {
        HeaderData headerData = new HeaderData();
        headerData.setHeaderKey(AbstractApiModel.AUTHORIZATION);
        String credentials = "thyrocare/personnel/demop:f5a0-4be7";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerData.setHeaderValue("Basic " + base64EncodedCredentials);

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue(contentType);

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData);
        header.add(headerData1);
        return header;
    }

    private List<HeaderData> getHeaderContentType(String contentType) {
        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue(contentType);

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData1);
        return header;
    }

}