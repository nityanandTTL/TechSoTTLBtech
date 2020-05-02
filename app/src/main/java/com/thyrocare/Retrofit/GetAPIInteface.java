package com.thyrocare.Retrofit;



import com.thyrocare.models.api.response.VideoLangaugesResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface GetAPIInteface {



    @GET("COMMON.svc/Showlang")
    Call<VideoLangaugesResponseModel> getVideoLanguages();




}

