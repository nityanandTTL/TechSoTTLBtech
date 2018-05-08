package com.thyrocare.Controller;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.LoginScreenActivity;
import com.thyrocare.models.api.request.Post_DeviceID;
import com.thyrocare.models.api.response.LoginDeviceResponseModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;

import static com.thyrocare.utils.api.NetworkUtils.isNetworkAvailable;

/**
 * Created by E5233 on 4/30/2018.
 */

public class DeviceLogOutController {

    Context mContext;
    LoginScreenActivity mLoginScreenActivity;

    public DeviceLogOutController(Context activity) {
        this.mContext = activity;
    }

    public DeviceLogOutController(LoginScreenActivity activity) {
        this.mLoginScreenActivity = activity;
        this.mContext = activity;
    }

    public void CallLogOutDevice(String UserId, String device_id) {

        Post_DeviceID n = new Post_DeviceID();
        n.setUserId("" + UserId);
        n.setDeviceId("" + device_id);

        ApiCallAsyncTask logoutDeviceAsyncTask = new AsyncTaskForRequest(mContext).getPostUserLogOutRequestAsyncTask(n);
        logoutDeviceAsyncTask.setApiCallAsyncTaskDelegate(new LogoutDeviceAsyncTaskDelegateResult());
        if (isNetworkAvailable(mContext)) {
            logoutDeviceAsyncTask.execute(logoutDeviceAsyncTask);
        } else {
//            Toast.makeText(mContext, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
        }
    }

    private class LogoutDeviceAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {

            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void CallLogInDevice(String userID, String device_id) {
        Post_DeviceID n = new Post_DeviceID();
        n.setUserId("" + userID);
        n.setDeviceId("" + device_id);

        ApiCallAsyncTask logoutDeviceAsyncTask = new AsyncTaskForRequest(mContext).getPostUserLogInRequestAsyncTask(n);
        logoutDeviceAsyncTask.setApiCallAsyncTaskDelegate(new LogInDeviceAsyncTaskDelegateResult());
        if (isNetworkAvailable(mContext)) {
            logoutDeviceAsyncTask.execute(logoutDeviceAsyncTask);
        } else {
//            Toast.makeText(mContext, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
        }
    }

    private class LogInDeviceAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(mContext);
                LoginDeviceResponseModel loginResponseModel = new LoginDeviceResponseModel();
                loginResponseModel = responseParser.getLoginDeviceResponseModel(json, statusCode);

                if (loginResponseModel != null) {
                    if (loginResponseModel.getRespId() == 1) {
                        mLoginScreenActivity.setLoginDeviceResponse();
                    } else if (loginResponseModel.getRespId() == 0) {
                        TastyToast.makeText(mContext, "" + loginResponseModel.getRespMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    } else {
                        TastyToast.makeText(mContext, "" + loginResponseModel.getRespMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                }
            }else {
                TastyToast.makeText(mContext, "" + json, TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}