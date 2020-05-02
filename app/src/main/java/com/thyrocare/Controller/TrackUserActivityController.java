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
import com.google.gson.Gson;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.Global;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackUserActivityController {
    private static final String TAG = TrackUserActivityController.class.getSimpleName();
    Activity activity;
    static RequestQueue requestQueue;
    Gson gson;

    public TrackUserActivityController(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public void trackUserActivity(final JSONObject jsonObject) {
        String trackURL = AbstractApiModel.AppUserActivity;
        Log.e(TAG, "Track User Activity: " + trackURL);

        if (requestQueue == null) {
            requestQueue = Global.setVolleyReq(activity);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, trackURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject1) {
                if (jsonObject1 != null && jsonObject1.length() > 0) {
                    try {
                        if (jsonObject1.getString("RES_ID").equalsIgnoreCase("RES0000")) {
                            Log.e(TAG, "onResponse: " + jsonObject1);
                        } else {
                            Log.e(TAG, "onResponse: " + jsonObject1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e);
                    }
                } else {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
        RetryPolicy policy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
    }
}