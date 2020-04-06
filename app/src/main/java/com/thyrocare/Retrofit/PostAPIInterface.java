package com.thyrocare.Retrofit;



import com.thyrocare.NewScreenDesigns.Models.RequestModels.BtechWiseVersionTrackerRequestModel;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.LogoutRequestModel;
import com.thyrocare.NewScreenDesigns.Models.RequestModels.NotificationMappingRequestModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.NotificationMappingResponseModel;
import com.thyrocare.models.api.request.GetVideoLanguageWiseRequestModel;
import com.thyrocare.models.api.request.OrderPassRequestModel;
import com.thyrocare.models.api.request.Post_DeviceID;
import com.thyrocare.models.api.request.SendOTPRequestModel;
import com.thyrocare.models.api.response.CommonResponseModel2;
import com.thyrocare.models.api.response.CommonResponseModel1;
import com.thyrocare.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.models.api.response.LoginResponseModel;
import com.thyrocare.models.api.response.VideosResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostAPIInterface {




    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);

    @POST("api/OrderAllocation/EditNameToSendOTP")
    Call<CommonResponseModel2> CallSendOTPAPI(@Body SendOTPRequestModel sendOTPRequestModel);

    @POST("api/AllSMS/PostVerifyOTP")
    Call<String> CallValidateOTPAPI(@Body OrderPassRequestModel orderPassRequestModel);

    @Multipart
    @POST("api/OrderAllocation/TRFUPLOAD")
    Call<CommonResponseModel1> uploadTRFToServer(@Part MultipartBody.Part TRFImage,
                                                 @Part("BENID") RequestBody BENID);

    @POST("api/UserLoginDevice/PostUserLogOut")
    Call<CommonResponseModel> CallLogoutAPI(@Body LogoutRequestModel logoutRequestModel);

    @POST("COMMON.svc/Mapping")
    Call<NotificationMappingResponseModel> NotificationTokenMappingAPI(@Body NotificationMappingRequestModel notificationMappingRequestModel);

    @FormUrlEncoded
    @POST("Token")
    Call<LoginResponseModel> CallLoginAPI(@Field("UserName") String username, @Field("Password") String password, @Field("grant_type") String grant_type);

    @POST("api/UserLoginDevice/PostUserLogin")
    Call<LoginDeviceResponseModel> PostLoginUserDeviceAPI(@Body Post_DeviceID post_deviceID);

    @POST("api/OrderAllocation/BtechAppVersion")
    Call<String> BtechWiseVersionTrackerAPI(@Body BtechWiseVersionTrackerRequestModel requestModel);


}
