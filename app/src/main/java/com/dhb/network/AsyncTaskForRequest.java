package com.dhb.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dhb.R;
import com.dhb.models.api.request.BtechsRequestModel;
import com.dhb.models.api.request.ChangePasswordRequestModel;
import com.dhb.models.api.request.HubStartRequestModel;
import com.dhb.models.api.request.LoginRequestModel;
import com.dhb.models.api.request.MasterBarcodeMappingRequestModel;
import com.dhb.models.api.request.OrderStatusChangeRequestModel;
import com.dhb.models.api.request.ResetPasswordRequestModel;
import com.dhb.models.api.request.SelfieUploadRequestModel;
import com.dhb.models.api.request.SetBtechAvailabilityAPIRequestModel;
import com.dhb.utils.app.AppPreferenceManager;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

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
           // String postJson = new Gson().toJson(orderVisitDetailsModel);
          //  abstractApiModel.setPostData(postJson);
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
                    .getString(R.string.progress_message_fetching_brand_master_please_wait));
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
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_LEDGER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+"2017-04-15"+"/"+"2017-04-25");
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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchEarningDetailsRequestAsyncTask: " );
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_DEPOSITREGISTER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+"2017-04-15"+"/"+"2017-04-25");
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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchDepositDetailsRequestAsyncTask: " );
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_EARNINGREGISTER_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID()+"/"+"2017-04-15"+"/"+"2017-04-25");
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

    public ApiCallAsyncTask getMaterialsDetailsRequestAsyncTask(){
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getMaterialsDetailsRequestAsyncTask: " );
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALMASTER_DETAIL);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_Materials_details_please_wait));


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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getMaterialINVDetailsRequestAsyncTask: " );
            // String postJson = new Gson().toJson(orderVisitDetailsModel);
            //  abstractApiModel.setPostData(postJson);
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.FETCH_MATERIALINV_DETAIL+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_MaterialsINV_details_please_wait));


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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getBtechCollectionListRequestAsyncTask: " );
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_COLLECTIONS+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_btech_collections_please_wait));

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
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getFetchOrderDetailsRequestAsyncTask: " );
            abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.BTECH_HUB_DETAILS_DISPLAY+"/"+appPreferenceManager.getLoginResponseModel().getUserID());
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources()
                    .getString(R.string.progress_message_fetching_dispatch_to_hub_details_please_wait));

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    } /*
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

    private List<HeaderData> getHeaderContentType(String contentType) {
        HeaderData headerData1 = new HeaderData();
        headerData1.setHeaderKey("Content-Type");
        headerData1.setHeaderValue(contentType);

        List<HeaderData> header = new ArrayList<>();
        header.add(headerData1);
        return header;
    }

}