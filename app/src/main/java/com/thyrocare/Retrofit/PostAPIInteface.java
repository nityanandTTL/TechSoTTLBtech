package com.thyrocare.Retrofit;



import com.thyrocare.models.api.request.GetAccessTokenForOTPRequestModel;
import com.thyrocare.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.models.api.request.OrderPassRequestModel;
import com.thyrocare.models.api.request.RequestOTPModel;
import com.thyrocare.models.api.request.SendOTPRequestModel;
import com.thyrocare.models.api.request.WOEOtpValidationRequestModel;
import com.thyrocare.models.api.response.CommonPOSTResponseModel;
import com.thyrocare.models.api.response.CommonResponseModel;
import com.thyrocare.models.api.response.CommonResponseModel1;
import com.thyrocare.models.api.response.VideosResponseModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PostAPIInteface {




    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);

    @POST("api/OrderAllocation/EditNameToSendOTP")
    Call<CommonResponseModel> CallSendOTPAPI(@Body SendOTPRequestModel sendOTPRequestModel);

    @POST("api/AllSMS/PostVerifyOTP")
    Call<String> CallValidateOTPAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @Multipart
    @POST("api/OrderAllocation/TRFUPLOAD")
    Call<CommonResponseModel1> uploadTRFToServer(@Part MultipartBody.Part TRFImage,
                                                 @Part("BENID") RequestBody BENID);

    @Headers("Content-Type: application/json")
    @POST("COMMON.svc/Token")
    Call<CommonPOSTResponseModel> RequestForOTPTokenAPI(@Body GetAccessTokenForOTPRequestModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Orderallocation/OTP")
    Call<CommonPOSTResponseModel> RequestForOTPAPI(@Body RequestOTPModel model);

    @Headers("Content-Type: application/json")
    @POST("api/Orderallocation/VerifyOTP")
    Call<CommonPOSTResponseModel> ValidateWoeOTPAPI(@Body WOEOtpValidationRequestModel model);



}
