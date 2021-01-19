package com.thyrocare.btechapp.Retrofit;

import android.app.Activity;


import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.api.request.LeadGenerationRequestModel;
import com.thyrocare.btechapp.models.api.response.LeadPurposeResponseModel;
import com.thyrocare.btechapp.models.api.response.LeadgenerationResponseModel;
import com.thyrocare.btechapp.models.api.response.TestBookingResponseModel;
import com.thyrocare.btechapp.utils.app.Global;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostAPI_SingletonClass {

    public static PostAPI_SingletonClass postAPI_singletonClass;
    static Global globalClass;
    static Activity mActivity;

    public PostAPI_SingletonClass() {

    }

    public static PostAPI_SingletonClass getInstance() {
        if (postAPI_singletonClass == null) {
            postAPI_singletonClass = new PostAPI_SingletonClass();
        }
        return postAPI_singletonClass;
    }

    public void CallReferAFriendBookingAPI(final Activity mActivity, final boolean ShowProgressbar, String name, String mobile1, String email1, String city, String remarks1, String type, File imagefile, File f_AudioSavePathInDevice, String refcode, String pincode1, String lead_id, final CallReferAFriendBookingListener APIResponseListener) {
        globalClass = new Global(mActivity);

        // add another part within the multipart request
        RequestBody order_by = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mobile1);
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), email1);
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody remarks = RequestBody.create(MediaType.parse("multipart/form-data"), remarks1 + " ~BtechApp");
        RequestBody bookingtype = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody report_code = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody bencount = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody pincode = RequestBody.create(MediaType.parse("multipart/form-data"), pincode1);
        RequestBody product = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody rate = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody reports = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody ref_code = RequestBody.create(MediaType.parse("multipart/form-data"), refcode);
        RequestBody service_type = RequestBody.create(MediaType.parse("multipart/form-data"), "H");
        RequestBody api_key = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.BOOKING_API_KEY);
        RequestBody bendataxml = RequestBody.create(MediaType.parse("multipart/form-data"), "<NewDataSet><Ben_details><Name>" + name + "</Name><Age>" + "" + "</Age><Gender>" + "" + "</Gender></Ben_details></NewDataSet>");
        RequestBody orderid = RequestBody.create(MediaType.parse("multipart/form-data"), lead_id);
        RequestBody hc = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        RequestBody tsp = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody pay_type = RequestBody.create(MediaType.parse("multipart/form-data"), "Postpaid");


        MultipartBody.Part ImageFileMultiBody = null, AudioFileMultiBody = null;
        if (imagefile != null && imagefile.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagefile);
            ImageFileMultiBody = MultipartBody.Part.createFormData("file_img", imagefile.getName(), requestFile);
        }
        if (f_AudioSavePathInDevice != null && f_AudioSavePathInDevice.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f_AudioSavePathInDevice);
            AudioFileMultiBody = MultipartBody.Part.createFormData("file_aud", f_AudioSavePathInDevice.getName(), requestFile);
        }

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.NUCLEAR_URL))).create(PostAPIInterface.class);
        Call<TestBookingResponseModel> responseCall = apiInterface.SubitReferAFriendformToServer(order_by, mobile, email, address, remarks, bookingtype, report_code, bencount, pincode,
                product, rate, reports, ref_code, service_type, api_key, bendataxml, orderid, hc, tsp, pay_type, ImageFileMultiBody, AudioFileMultiBody);
        if (ShowProgressbar) {
            globalClass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);
        }
        responseCall.enqueue(new Callback<TestBookingResponseModel>() {
            @Override
            public void onResponse(Call<TestBookingResponseModel> call, Response<TestBookingResponseModel> response) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }

                if (response.isSuccessful() && response.body() != null) {
                    TestBookingResponseModel responseModel = response.body();
                    if (APIResponseListener != null) {
                        APIResponseListener.onSuccess(responseModel);
                    }
                } else {
                    if (APIResponseListener != null) {
                        APIResponseListener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<TestBookingResponseModel> call, Throwable t) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }
                if (APIResponseListener != null) {
                    APIResponseListener.onFailure();
                }
            }
        });
    }


    public void CallGetLeadPurposeAPI(final Activity mActivity, final boolean ShowProgressbar, final CallGetLeadPurposeAPIListener APIResponseListener) {
        globalClass = new Global(mActivity);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.VELSO_URL))).create(PostAPIInterface.class);
        Call<LeadPurposeResponseModel> responseCall = apiInterface.CallGetLeadPurposeAPI();
        if (ShowProgressbar) {
            globalClass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);
        }
        responseCall.enqueue(new Callback<LeadPurposeResponseModel>() {
            @Override
            public void onResponse(Call<LeadPurposeResponseModel> call, Response<LeadPurposeResponseModel> response) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }

                if (response.isSuccessful() && response.body() != null) {
                    LeadPurposeResponseModel responseModel = response.body();
                    if (APIResponseListener != null) {
                        APIResponseListener.onSuccess(responseModel);
                    }
                } else {
                    if (APIResponseListener != null) {
                        APIResponseListener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<LeadPurposeResponseModel> call, Throwable t) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }
                if (APIResponseListener != null) {
                    APIResponseListener.onFailure();
                }
            }
        });
    }

    public void CallSubmitLeadGenerationAPI(final Activity mActivity, final boolean ShowProgressbar, LeadGenerationRequestModel model, final CallSubmitLeadAPIListener APIResponseListener) {
        globalClass = new Global(mActivity);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.VELSO_URL))).create(PostAPIInterface.class);
        Call<LeadgenerationResponseModel> responseCall = apiInterface.CallSubmitLeadAPI(model);
        if (ShowProgressbar) {
            globalClass.showProgressDialog(mActivity, ConstantsMessages.PLEASE_WAIT);
        }
        responseCall.enqueue(new Callback<LeadgenerationResponseModel>() {
            @Override
            public void onResponse(Call<LeadgenerationResponseModel> call, Response<LeadgenerationResponseModel> response) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }

                if (response.isSuccessful() && response.body() != null) {
                    LeadgenerationResponseModel responseModel = response.body();
                    if (APIResponseListener != null) {
                        APIResponseListener.onSuccess(responseModel);
                    }
                } else {
                    if (APIResponseListener != null) {
                        APIResponseListener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<LeadgenerationResponseModel> call, Throwable t) {
                if (ShowProgressbar) {
                    globalClass.hideProgressDialog(mActivity);
                }
                if (APIResponseListener != null) {
                    APIResponseListener.onFailure();
                }
            }
        });
    }


    // TODO INTERFACE --------------------------------------------------------------------------------------------------------------------------------------------------------

    public interface CallReferAFriendBookingListener {
        void onSuccess(TestBookingResponseModel testBookingResponseModel);

        void onFailure();
    }

    public interface CallGetLeadPurposeAPIListener {
        void onSuccess(LeadPurposeResponseModel model);

        void onFailure();
    }


    public interface CallSubmitLeadAPIListener {
        void onSuccess(LeadgenerationResponseModel model);

        void onFailure();
    }


}
