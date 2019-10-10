package com.thyrocare.Controller;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.thyrocare.fragment.ClientEntryFragment;
import com.thyrocare.models.api.request.NewClientModel;
import com.thyrocare.models.api.response.ResponseClientEntryModel;
import com.thyrocare.utils.app.Global;

import org.json.JSONException;
import org.json.JSONObject;

import static com.thyrocare.network.AbstractApiModel.BDN_API_VERSION;


public class ClientEntryController {
    Context context;
    ClientEntryFragment newClientEntry;
    ResponseClientEntryModel responsenewCliententryModel;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    Global globalclass;

    public ClientEntryController(Context context, ClientEntryFragment newClientEntry) {
        this.context = context;
        this.newClientEntry = newClientEntry;
        progressDialog = new ProgressDialog(context);
        globalclass = new Global(context);
    }
    public void PostRegisterapicall(NewClientModel newClientpostmodel){

        progressDialog.setMessage("Processing... Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(context);
        String url = BDN_API_VERSION+ "api/ClientEntry/PostRegister";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Address",newClientpostmodel.getAddress());
            jsonObject.put("Brand",newClientpostmodel.getBrand());
            jsonObject.put("Channel",newClientpostmodel.getChannel());
            jsonObject.put("Country",newClientpostmodel.getCountry());
            jsonObject.put("Email",newClientpostmodel.getEmail());
            jsonObject.put("InchargeName",newClientpostmodel.getInchargeName());
            jsonObject.put("Name",newClientpostmodel.getName());
            jsonObject.put("TspOLc",newClientpostmodel.getTspOLc());
            jsonObject.put("VerificationSource",newClientpostmodel.getVerificationSource());
            jsonObject.put("apikey",newClientpostmodel.getApikey());
            jsonObject.put("mobile",newClientpostmodel.getMobile());
            jsonObject.put("phone_no",newClientpostmodel.getPhone_no());
            jsonObject.put("EntryType",newClientpostmodel.getEntryType());
            jsonObject.put("pincode",newClientpostmodel.getPincode());
            jsonObject.put("website",newClientpostmodel.getWebsite());
            jsonObject.put("APP",newClientpostmodel.getAPP());
            jsonObject.put("File",newClientpostmodel.getFile());
            jsonObject.put("opType",newClientpostmodel.getOpType());
            jsonObject.put("Latitute",newClientpostmodel.getLatitute());
            jsonObject.put("Longitude",newClientpostmodel.getLongitude());
            jsonObject.put("Visiting_Card",newClientpostmodel.getVisiting_Card());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {
                    if (response.getString("Response").equalsIgnoreCase("Success")){
                        responsenewCliententryModel = new ResponseClientEntryModel();
                        responsenewCliententryModel.setResponse(response.getString("Response"));
                        responsenewCliententryModel.setMessage(response.getString("message"));
                        responsenewCliententryModel.setDocStatus(response.getString("docStatus"));
                        responsenewCliententryModel.setResId(response.getString("ResId"));

                        Toast.makeText(context,response.getString("Response"),Toast.LENGTH_LONG).show();
                        newClientEntry.RefreshFields();
                    }else{
                        globalclass.showalert_OK(response.optString("message","Something went wrong, please try after sometime."),context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(context,"Failure",Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(jsonObjectRequest);
        RetryPolicy policy = new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

    }
}