package com.thyrocare.btechapp.Controller;


import android.app.Activity;
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
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.fragment.ClientEntryFragment;
import com.thyrocare.btechapp.models.api.request.NewClientModel;
import com.thyrocare.btechapp.models.api.response.ResponseClientEntryModel;
import com.thyrocare.btechapp.utils.app.Global;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static android.widget.Toast.LENGTH_SHORT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.PLEASE_WAIT;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;


public class ClientEntryController {
    Activity activity;
    ClientEntryFragment newClientEntry;
    ResponseClientEntryModel responsenewCliententryModel;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    Global globalclass;

    public ClientEntryController(Activity activity, ClientEntryFragment newClientEntry) {
        this.activity = activity;
        this.newClientEntry = newClientEntry;
        globalclass = new Global(activity);
    }
    public void PostRegisterapicall(NewClientModel newClientpostmodel){
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.B2B_API_VERSION))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallNewClientEntryRegistrationAPI(newClientpostmodel);
        globalclass.showProgressDialog(activity, PLEASE_WAIT);
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> res) {
                globalclass.hideProgressDialog();
                if (res.isSuccessful() && res.body() != null) {
                    try {
                        JSONObject response = new JSONObject(res.body());
                        if (response.getString("Response").equalsIgnoreCase("Success")){
                            responsenewCliententryModel = new ResponseClientEntryModel();
                            responsenewCliententryModel.setResponse(response.getString("Response"));
                            responsenewCliententryModel.setMessage(response.getString("message"));
                            responsenewCliententryModel.setDocStatus(response.getString("docStatus"));
                            responsenewCliententryModel.setResId(response.getString("ResId"));
                            Toast.makeText(activity,response.getString("Response"),Toast.LENGTH_LONG).show();
                            newClientEntry.RefreshFields();
                        }else{
                            globalclass.showalert_OK(response.optString("message","Something went wrong, please try after sometime."),activity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(activity,SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                globalclass.hideProgressDialog();
                Toast.makeText(activity,SOMETHING_WENT_WRONG, LENGTH_SHORT).show();
            }
        });

    }


}