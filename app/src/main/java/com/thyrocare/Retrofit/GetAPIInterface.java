package com.thyrocare.Retrofit;



import com.thyrocare.NewScreenDesigns.Models.ResponseModel.TSP_NBT_AvailabilityResponseModel;
import com.thyrocare.models.api.response.NewBtechAvaliabilityResponseModel;
import com.thyrocare.NewScreenDesigns.Models.ResponseModel.VersionControlResponseModel;
import com.thyrocare.models.api.response.VideoLangaugesResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface GetAPIInterface {



    @GET("COMMON.svc/Showlang")
    Call<VideoLangaugesResponseModel> getVideoLanguages();

    @GET("api/VersionControl/1")
    Call<VersionControlResponseModel> VersionControlAPI();

    @GET("api/BtechAvaibilityNew/BtechMarkedAvailability/{BtechID}")
    Call<NewBtechAvaliabilityResponseModel> GetBtechAvailability(@Path("BtechID")String BtechID);

    @GET("api/BtechAvaibilityNew/NBTMarkedAvailability/{ID}")
    Call<ArrayList<TSP_NBT_AvailabilityResponseModel>> GetTSP_NBT_Avialability(@Path("ID")String ID);




}

