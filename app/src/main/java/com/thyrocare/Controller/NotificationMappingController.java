package com.thyrocare.Controller;

import android.app.Activity;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.thyrocare.models.api.request.NotificationMappingModel;

import com.thyrocare.models.api.response.NotificationMappingResponseModel;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.Global;

import org.json.JSONObject;

public class NotificationMappingController {
    Activity activity;
    RequestQueue requestQueue;
    Gson gson;
    NotificationMappingResponseModel notificationMappingResponseModel;

    public NotificationMappingController(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public void getNotificationMapping(final NotificationMappingModel notificationMappingModel) {
        if (requestQueue == null) {
            requestQueue = Global.setVolleyReq(activity);

            String jsonString = gson.toJson(notificationMappingModel);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String url = AbstractApiModel.NotificationMapping;
            Log.e("shami -- ", "getNotificationMapping: "+url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        notificationMappingResponseModel = gson.fromJson(response.toString(), NotificationMappingResponseModel.class);
                        if (notificationMappingResponseModel.getResponseId().equalsIgnoreCase("RES0000")) {
                            Log.e("shami -- ", "onResponse Success: " + response.toString());
                        } else {
                            Log.e("shami -- ", "onResponse null: " + response.toString());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("shami -- ", "onErrorResponse: "+error);
                }
            });
            requestQueue.add(jsonObjectRequest);
            RetryPolicy policy = new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
        }
    }
}