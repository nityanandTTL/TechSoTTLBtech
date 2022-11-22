package com.thyrocare.btechapp.Retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.grapesnberries.curllogger.CurlLoggerInterceptor;
import com.thyrocare.btechapp.BuildConfig;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetSSLKeyResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.network.AbstractApiModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroFit_APIClient {


    public static RetroFit_APIClient apiClient;
    private Retrofit retrofit = null;
    private AppPreferenceManager appPreferenceManager;

    public static RetroFit_APIClient getInstance() {
        if (apiClient == null) {
            apiClient = new RetroFit_APIClient();
        }
        return apiClient;
    }

    public Retrofit getClient(final Activity mActivity, final String BASE_URL) {
        appPreferenceManager = new AppPreferenceManager(mActivity);
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);   // development build
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);    // production build
//            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);    // production build
        }
        client.addInterceptor(interceptor);
//TODO  Api timeout is increase to handle the get order details api slowness (Previous: 120 sec)
        client.protocols(Util.immutableList(Protocol.HTTP_1_1));
        client.readTimeout(300, TimeUnit.SECONDS);
        client.writeTimeout(300, TimeUnit.SECONDS);
        client.connectTimeout(300, TimeUnit.SECONDS);
        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request;
                if (BASE_URL.equalsIgnoreCase(EncryptionUtils.Dcrp_Hex(mActivity.getString(R.string.SERVER_BASE_API_URL_PROD))) && appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                    request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, Global.getHeaderValue(mActivity))
                            .addHeader(AbstractApiModel.AUTHORIZATION, "Bearer " + appPreferenceManager.getAPISessionKey()).build();
                } else {
                    request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, Global.getHeaderValue(mActivity)).build();
                }

                return chain.proceed(request);
            }
        });
        if (BuildConfig.DEBUG) {
            client.addInterceptor(new CurlLoggerInterceptor());
        }

        //client.addInterceptor(new ChuckerInterceptor(mActivity));

        /*try {
            SharedPreferences pref_SSL = mActivity.getSharedPreferences("SSLKeysPref", 0);
            Gson gson = new Gson();
            boolean ApplySSLPining = pref_SSL.getBoolean("ApplySSLPining", false);
            String json = pref_SSL.getString("SSLKeyResponseModel", null);
            GetSSLKeyResponseModel getSSLKeyResponseModel = gson.fromJson(json, GetSSLKeyResponseModel.class);
            if (ApplySSLPining && getSSLKeyResponseModel != null && getSSLKeyResponseModel.getLstKeys() != null && getSSLKeyResponseModel.getLstKeys().size() > 0){
                CertificatePinner.Builder certificatePinng23er = new CertificatePinner.Builder();
                for (int i = 0; i < getSSLKeyResponseModel.getLstKeys().size(); i++) {
                    if (getSSLKeyResponseModel.getLstKeys().get(i).isForcefullyAllow()
                            && !StringUtils.isNull(getSSLKeyResponseModel.getLstKeys().get(i).getDomain())
                            && !StringUtils.isNull(getSSLKeyResponseModel.getLstKeys().get(i).getSSL_Key())){
                        try {
                            certificatePinng23er.add(getSSLKeyResponseModel.getLstKeys().get(i).getDomain(),EncryptionUtils.decryptSSL(getSSLKeyResponseModel.getLstKeys().get(i).getSSL_Key().trim()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                CertificatePinner certificatePinner = certificatePinng23er.build();
                client.certificatePinner(certificatePinner)
                        .build();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }*/

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }

    public Retrofit getClient(final Context context, final String BASE_URL) {
        appPreferenceManager = new AppPreferenceManager(context);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);   // development build
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);    // production build
//            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);    // production build
        }
        client.addInterceptor(interceptor);


        client.readTimeout(300, TimeUnit.SECONDS);
        client.writeTimeout(300, TimeUnit.SECONDS);
        client.connectTimeout(300, TimeUnit.SECONDS);
        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request;
                if (BASE_URL.equalsIgnoreCase(EncryptionUtils.Dcrp_Hex(context.getString(R.string.SERVER_BASE_API_URL_PROD))) && appPreferenceManager.getLoginResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getAPISessionKey())) {
                    request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, Global.getHeaderValue(context))
                            .addHeader(AbstractApiModel.AUTHORIZATION, "Bearer " + appPreferenceManager.getAPISessionKey()).build();
                } else {
                    request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, Global.getHeaderValue(context)).build();
                }

                return chain.proceed(request);
            }
        });
        if (BuildConfig.DEBUG) {
            client.addInterceptor(new CurlLoggerInterceptor());
        }
        //client.addInterceptor(new ChuckerInterceptor(context));

      /*  SharedPreferences pref_SSL = context.getSharedPreferences("SSLKeysPref", 0);
        Gson gson = new Gson();
        boolean ApplySSLPining = pref_SSL.getBoolean("ApplySSLPining", false);
        String json = pref_SSL.getString("SSLKeyResponseModel", null);
        GetSSLKeyResponseModel getSSLKeyResponseModel = gson.fromJson(json, GetSSLKeyResponseModel.class);
        if (ApplySSLPining && getSSLKeyResponseModel != null && getSSLKeyResponseModel.getLstKeys() != null && getSSLKeyResponseModel.getLstKeys().size() > 0){
            CertificatePinner.Builder certificatePinng23er = new CertificatePinner.Builder();
            for (int i = 0; i < getSSLKeyResponseModel.getLstKeys().size(); i++) {
                if (getSSLKeyResponseModel.getLstKeys().get(i).isForcefullyAllow()
                        && !StringUtils.isNull(getSSLKeyResponseModel.getLstKeys().get(i).getDomain())
                        && !StringUtils.isNull(getSSLKeyResponseModel.getLstKeys().get(i).getSSL_Key())){
                    try {
                        certificatePinng23er.add(getSSLKeyResponseModel.getLstKeys().get(i).getDomain(),EncryptionUtils.decryptSSL(getSSLKeyResponseModel.getLstKeys().get(i).getSSL_Key().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            CertificatePinner certificatePinner = certificatePinng23er.build();
            client.certificatePinner(certificatePinner)
                    .build();
        }*/

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }
}

