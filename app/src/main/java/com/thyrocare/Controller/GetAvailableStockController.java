package com.thyrocare.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thyrocare.activity.StockAvailabilityActivity;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.Global;
import org.json.JSONObject;

public class GetAvailableStockController {
    private static final String TAG = GetAvailableStockController.class.getSimpleName();
    private int flag;
    private Activity mActivity;
    private RequestQueue requestQueue;
    private StockAvailabilityActivity stockAvailabilityActivity;
    private Global globalClass;

    public GetAvailableStockController(StockAvailabilityActivity stockAvailabilityActivity, Activity activity) {
        this.stockAvailabilityActivity = stockAvailabilityActivity;
        this.mActivity = activity;
        flag = 0;
    }

    public void getAvailableStock(final JSONObject jsonObject) {
        try {
            globalClass = new Global(mActivity);
            globalClass.showProgressDialog(mActivity, "Loading...");
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(mActivity);
            String url = AbstractApiModel.StockAvailability;
            Log.e(TAG, "getAvailableStockAPI: " + url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "getAvailableStockAPI gson: " + jsonObject);
                    globalClass.hideProgressDialog();
                    Log.e(TAG, "getAvailableStockAPI Response: " + response);
                    if (response != null) {
                        if (flag == 0)
                            stockAvailabilityActivity.getStockResponse(response);
                    } else {
                        Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "onErrorResponse: "+error);
                }
            });
            requestQueue.add(request);
            RetryPolicy policy = new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }
}