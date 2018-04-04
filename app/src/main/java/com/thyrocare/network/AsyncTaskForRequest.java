package com.thyrocare.network;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.thyrocare.R;
import com.thyrocare.models.api.request.ApplyLeaveRequestModel;
import com.thyrocare.models.api.request.BtechAppVersion;
import com.thyrocare.models.api.request.BtechsRequestModel;
import com.thyrocare.models.api.request.BtechwithHub_MasterBarcodeMappingRequestModel;
import com.thyrocare.models.api.request.CallPatchRequestModel;
import com.thyrocare.models.api.request.CampStartedRequestModel;
import com.thyrocare.models.api.request.CartAPIRequestModel;
import com.thyrocare.models.api.request.CashDepositEntryRequestModel;
import com.thyrocare.models.api.request.ChangePasswordRequestModel;
import com.thyrocare.models.api.request.ChatRequestModel;
import com.thyrocare.models.api.request.DownloadDetailsRequestModel;
import com.thyrocare.models.api.request.HubStartRequestModel;
import com.thyrocare.models.api.request.LocusPushLocationRequestModel;
import com.thyrocare.models.api.request.LoginRequestModel;
import com.thyrocare.models.api.request.MasterBarcodeMappingRequestModel;
import com.thyrocare.models.api.request.MaterialorderRequestModel;
import com.thyrocare.models.api.request.OlcScanPickUpRequestModel;
import com.thyrocare.models.api.request.OlcStartRequestModel;
import com.thyrocare.models.api.request.OrderAllocationTrackLocationRequestModel;
import com.thyrocare.models.api.request.OrderBookingRequestModel;
import com.thyrocare.models.api.request.OrderPassRequestModel;
import com.thyrocare.models.api.request.OrderStatusChangeRequestModel;
import com.thyrocare.models.api.request.RemoveBeneficiaryAPIRequestModel;
import com.thyrocare.models.api.request.ResetPasswordRequestModel;
import com.thyrocare.models.api.request.SelfieUploadRequestModel;
import com.thyrocare.models.api.request.ServiceUpdateRequestModel;
import com.thyrocare.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.thyrocare.models.api.request.TrackBtechLocationRequestModel;
import com.thyrocare.models.api.request.Tsp_Send_RequestModel;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;

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
/*
     * Order Booking Api Integration*/

    public ApiCallAsyncTask getCashDepositEntryRequestAsyncTask(CashDepositEntryRequestModel cashDepositEntryRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(cashDepositEntryRequestModel);
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CASH_DEPOSIT_ENTRY);
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_STATUS_CHANGE + "/" + orderStatusChangeRequestModel.getId());
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


    public ApiCallAsyncTask getTrackBtechLocationRequestAsyncTask(TrackBtechLocationRequestModel trackBtechLocationRequestModel) {
        apiCallAsyncTask = null;
        try {
            Logger.error("LOCUS");
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            String postJson = new Gson().toJson(trackBtechLocationRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeaderLocus(AbstractApiModel.APPLICATION_JSON));
            Logger.error("LOCUS2");
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TRACK_BTECH_LOCATION/* + appPreferenceManager.getLoginResponseModel().getUserID() + "/location"*/);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));

            Logger.error("LOCUS3");
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    //Order Allocation //
    public ApiCallAsyncTask getOrderAllocationpost(OrderAllocationTrackLocationRequestModel orderAllocationTrackLocationRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderAllocationTrackLocationRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_ALLOCATION_POST);
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

    //POst Order PAss //
    public ApiCallAsyncTask getOrderPassRequestModelAsyncTask(OrderPassRequestModel orderPassRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(orderPassRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.POST_ORDER_PASS);
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
     * GetTestList*/

    public ApiCallAsyncTask getTestListAsyncTask(String sp_id) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getTestListAsyncTask: ");
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            //  abstractApiModel.setRequestUrl("http://bts.dxscloud.com/techsoapi/api/BenTestList/GetTestList/SP33337413");
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GET_TEST_LIST + "/" + sp_id);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.viewing_tests));
            apiCallAsyncTask.setProgressBarCancellable(true);

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
     * Service Update  Api Integration*/

    public ApiCallAsyncTask getPostServiceUpdateRequestAsyncTask(ServiceUpdateRequestModel serviceUpdateRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(serviceUpdateRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SERVICE_UPDATE);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_cancelling_visit_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }
    /*
	 * Fetch Order Detail Api Integration*/

    public ApiCallAsyncTask getFetchOrderDetailsRequestAsyncTask(boolean b) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchOrderDetailsRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader_new(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_ORDER_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_order_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /*
	 * Fetch Order Detail by visit Api Integration*/

    public ApiCallAsyncTask getFetchOrderDetailsByVisitRequestAsyncTask(String orderID) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchOrderDetailsByVisitRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            //  abstractApiModel.setRequestUrl("https://www.dxscloud.com/techsoapi/api/OrderDetailsByVisit/AP002977468");
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_DETAILS_BY_VISIT + "/" + orderID);
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

       /* SetDownload Details  Api Integration*/

    public ApiCallAsyncTask getDownloadDetails(DownloadDetailsRequestModel downloadDetailsRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(downloadDetailsRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DOWNLOAD_DETAILS);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_Downloading_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    /* SetDownload Details  Api Integration*/

    public ApiCallAsyncTask getBtechAppVersion(BtechAppVersion btechAppVersion) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(btechAppVersion);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DOWNLOAD_DETAILS);
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_Downloading_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    //Slot master //
    public ApiCallAsyncTask getSlotMasterDetailsRequestAsyncTask(String pincode, String Appdate) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getSlotMasterDetailsRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SUB_SLOT_MASTER + "/" + pincode + "/" + Appdate);
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


    public ApiCallAsyncTask getBtechAvaliability() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getBtechAvaliability: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.SCHEDULEOPEN + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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

	 /* get Camp Details Count Api Integration*/

    public ApiCallAsyncTask getCampDetailsCountRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getCampDetailsCountRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMP_COUNT + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_camp_count_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(false);
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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getSendQRCodeRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMP_QR_DETAIL + "/" +/*"VISTT001109154"*/qrContent);
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
    }/*
	 * PaymentModeMaster Api Integration*/

    public ApiCallAsyncTask getPaymentModeMasterRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getPaymentModeMasterRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_MODE_MASTER/*"VISTT001109154"*/);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_getting_master_payment_mode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }/*
	 * PaymentModeMaster Api Integration*/

    public ApiCallAsyncTask getBankMasterRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getBankMasterRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BANK_MASTER + "/" + appPreferenceManager.getLoginResponseModel().getUserID()/*"VISTT001109154"*/);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_getting_master_payment_mode_please_wait));
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_SLOT_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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

    // NITYANAND Start

    public ApiCallAsyncTask getTSPNBTFetchSlotDetailsRequestAsyncTask(String id) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_SLOT_DETAIL + "/" + id);
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

    // Nityanand End
    //Nityanand start

    public ApiCallAsyncTask getTSPNBTAvaliability(String id) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSPNBTAVAILABILITY + "/" + id);
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

    //Nityanand end

    /*
	 * Fetch Brand wise Test Master Api Integration*/

    public ApiCallAsyncTask getFetchBrandwiseTestMasterRequestAsyncTask(String BrandId) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeaderAccept(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_BRAND_WISE_TEST_MASTER + "/" + BrandId);
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

    public ApiCallAsyncTask getFetchLedgerDetailsRequestAsyncTask(String fromdate, String todate) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchLedgerDetailsRequestAsyncTask: ");
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_LEDGER_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID() + "/" + fromdate + "/" + todate);
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

    public ApiCallAsyncTask getFetchEarningDetailsRequestAsyncTask(String fromdate, String todate) {

       /* apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_DEPOSITREGISTER_DETAIL + "/" + appPreferenceManager.getBtechID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Earning_details_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;*/

        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_EARNINGREGISTER_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID() + "/" + fromdate + "/" + todate);
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

    public ApiCallAsyncTask getFetchDepositDetailsRequestAsyncTask(String fromdate, String todate) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_DEPOSITREGISTER_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID() + "/" + fromdate + "/" + todate);
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

    public ApiCallAsyncTask getMaterialsDetailsRequestAsyncTask(String Category) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            abstractApiModel.setHeader(getHeaderAccept(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALMASTER_DETAIL + "/" + Category);
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

    public ApiCallAsyncTask getMaterialINVDetailsRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALINV_DETAIL + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_COLLECTIONS + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_HUB_DETAILS_DISPLAY + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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

    public ApiCallAsyncTask ChatPostapi(ChatRequestModel chatRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(chatRequestModel);
            abstractApiModel.setPostData(postJson);
            //  abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl("http://192.168.0.152:3456/users");
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage("Chat is happening ");
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    //Chat get api
    public ApiCallAsyncTask getChatAPi() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl("http://192.168.0.152:3456/users");
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage("FETCHING REQUEST ");
            apiCallAsyncTask.setProgressBarVisible(false);
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ORDER_SERVED_DETAILS_DISPLAY + "/" + appPreferenceManager.getLoginResponseModel().getUserID() + "/" + date);
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
    }


    // Leave Applied list
    public ApiCallAsyncTask getLeaveHistoryDetailsDisplayRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.ALREADY_APPLIED_LEAVE_HISTORY_DETAILS_DISPLAY + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_leave_applied));
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_CLIENTS_DETAILS_DISPLAY + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.CAMPS_DETAILS_DISPLAY + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
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
/*
	 * Btech Est earnings  Api Integration*/

    public ApiCallAsyncTask getBtechEstEarningsRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_EST_EARNINGS + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_btech_est_earning_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

     /* Set ApplyLeave Api Integration*/

    public ApiCallAsyncTask getPostApplyLeaveRequestAsyncTask(ApplyLeaveRequestModel setApplyLeaveRequestModel) {
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

    public ApiCallAsyncTask getLeaveDetailsRequestAsyncTask() {
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

    public ApiCallAsyncTask getVersionControlDetailsRequestAsyncTask(int Appnameid) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_VERSION_CONTROL_DETAIL + "/" + Appnameid);
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
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.PAYMENT_SELECT_MODE + "/" + narrationId);
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


    public ApiCallAsyncTask getremarksRequestAsyncTask(int id) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.REMARKS + "/" + id);
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


    //Order Allocation //

    public ApiCallAsyncTask getorderallocation(int btechid, String pincode) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_ORDER_ALLOCATION + "/" + btechid + "/" + pincode);
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

    public ApiCallAsyncTask getDoCaptureResponseRequestAsyncTask(JSONObject jsonRequest, String URL) {
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

    public ApiCallAsyncTask getRecheckPaymentResponseRequestAsyncTask(String jsonRequest, String URL) {
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
            Logger.error("LOCUS");
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            String postJson = new Gson().toJson(locusPushLocationRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeaderLocus(AbstractApiModel.APPLICATION_JSON));
            Logger.error("LOCUS2");
            abstractApiModel.setRequestUrl(abstractApiModel.LOCUS_PUSH_LOCATIONS_API + appPreferenceManager.getLoginResponseModel().getUserID() + "/location");
            apiCallAsyncTask.setHttpMethod((APICall.POST_METHOD));

            Logger.error("LOCUS3");
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarVisible(true);
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

    private List<HeaderData> getHeader_new(String contentType) {
        HeaderData headerData = new HeaderData();
        headerData.setHeaderKey(AbstractApiModel.AUTHORIZATION);
        headerData.setHeaderValue("Bearer " + appPreferenceManager.getAPISessionKey());

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Accept-Encoding");
        headerData1.setHeaderValue("gzip");

        HeaderData headerData2 = new HeaderData();
        headerData2.setHeaderKey("Content-Type");
        headerData2.setHeaderValue(contentType);

        HeaderData headerData3 = new HeaderData();
        headerData3.setHeaderKey("Content-Encoding");
        headerData3.setHeaderValue("gzip");

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData);
        header.add(headerData1);
        header.add(headerData2);
        header.add(headerData3);
        return header;
    }


    private List<HeaderData> getHeaderAccept(String contentType) {
        HeaderData headerData = new HeaderData();
        headerData.setHeaderKey(AbstractApiModel.AUTHORIZATION);
        headerData.setHeaderValue("Bearer " + appPreferenceManager.getAPISessionKey());

        HeaderData headerData2 = new HeaderData();
        headerData2.setHeaderKey(AbstractApiModel.ACCEPT);
        headerData2.setHeaderValue(contentType);

        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue(contentType);

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData);
        header.add(headerData1);
        header.add(headerData2);
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

    public ApiCallAsyncTask getfetchBtechwithHubBarcodeApiAsyncTaskRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECHWITH_HUB_BARCODE + "/" + appPreferenceManager.getBtechID());//appPreferenceManager.getBtechID() instead of 5
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetcihng_btechwithhub_barcode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getbTECHWITHhUB_MasterBarcodeMapRequestAsyncTask(BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(masterBarcodeMappingRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECHWITHHUB_MASTER_BARCODE_MAPPING);
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

    public ApiCallAsyncTask getTspBarcodeApiAsyncTaskRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSP_BARCODE + "/" + appPreferenceManager.getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetcihng_btechwithhub_barcode_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getTSP_MasterBarcodeMapRequestAsyncTask(BtechwithHub_MasterBarcodeMappingRequestModel masterBarcodeMappingRequestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(masterBarcodeMappingRequestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSP_MASTER_BARCODE_MAPPING);
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

    public ApiCallAsyncTask getTspSendConsignmentRequestAsyncTask(Tsp_Send_RequestModel tsp_send_requestModel) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();

            String postJson = new Gson().toJson(tsp_send_requestModel);
            abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSP_SEND_CONSIGNMENT);
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

    public ApiCallAsyncTask getTspModeDataApiAsyncTaskRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSP_SEND_MODES);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetcihngtsp_modes_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getTspScanBarcodeApiAsyncTaskRequestAsyncTask() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.TSP_BARCODE_SCAN + "/" + appPreferenceManager.getBtechID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetcihngtsp_modes_please_wait));
            apiCallAsyncTask.setProgressBarVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


    //tejas t -----------------------------------------

    public ApiCallAsyncTask getBtechBlockDetails() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getBtechBlockDetailsRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_BLOCK_CHECK + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            //abstractApiModel.setRequestUrl("http://bts.dxscloud.com/techsoapi/api/BtechNotification/BlockedBTS/884543173");
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getAcceptOrderNotification() {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getAcceptOrderNotificationRequestAsyncTask: ");
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_ACCEPTORDER_CHECK + "/" + appPreferenceManager.getLoginResponseModel().getUserID());
            //abstractApiModel.setRequestUrl("http://bts.dxscloud.com/techsoapi/api/BtechNotification/OrderAssigned/884543107");
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getEmailVailidation(String email, String email2) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getTestListAsyncTask: ");
            abstractApiModel.setHeader(getHeaderContentType(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(abstractApiModel.GET_Email + email + "&apikey=" + email2);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.vailidatingemail));
            apiCallAsyncTask.setProgressBarCancellable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getEriecept(String orderid) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getTestListAsyncTask: ");
            abstractApiModel.setHeader(getHeaderContentType(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(abstractApiModel.GET_ERecipt + "/" + orderid);

            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.Sending_Ereciept));
            apiCallAsyncTask.setProgressBarCancellable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    //tejas t ------------------------------------------
}