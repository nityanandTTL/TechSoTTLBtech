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
import com.thyrocare.activity.UpdateMaterialActivity;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.Global;
import org.json.JSONObject;

public class UpdateStockController {
    private static final String TAG = UpdateStockController.class.getSimpleName();
    private Activity mActivity;
    private int flag;
    private RequestQueue requestQueue;
    private UpdateMaterialActivity bmc_updateMaterialActivity;
    Global globalClass;

    public UpdateStockController(UpdateMaterialActivity updateMaterialActivity, Activity activity) {
        this.bmc_updateMaterialActivity = updateMaterialActivity;
        this.mActivity = activity;
        flag = 0;
    }

    public void UpdateAvailableStock(final JSONObject jsonObject) {
        try {
            globalClass = new Global(mActivity);
            globalClass.showProgressDialog(mActivity, "Loading...");
            if (requestQueue == null)
                requestQueue = Global.setVolleyReq(mActivity);
            String url = AbstractApiModel.Materialupdate;
            Log.e(TAG, "UpdateAvailableStockAPI: " + url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "UpdateAvailableStockAPI gson: " + jsonObject);
                    globalClass.hideProgressDialog();
                    if (response != null) {
                        if (flag == 0)
                            bmc_updateMaterialActivity.getUpdatedResponse(response);
                            Log.e(TAG, "UpdateAvailableStockAPI Response: " + response);
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