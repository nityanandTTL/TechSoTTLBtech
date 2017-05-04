package com.dhb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.models.api.request.ChangePasswordRequestModel;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;

import org.json.JSONException;

public class ResetPasswordFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = ResetPasswordFragment.class.getSimpleName();
    private EditText edt_old_password, edt_new_password, edt_confirm_password;
    private Button btn_reset_password;
     String regexp ="\t\n" +
             "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";
    //String regexp ="^(?=.*\\d)(?=.*[a-zA-Z]).{8,12}$";
    // String regexp = " ^.*(?=.{8,12})(?=.*\\d)(?=.*[a-zA-Z]).*$";
    HomeScreenActivity activity;

    public ResetPasswordFragment() {
    }

    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        initUi(view);
        activity = (HomeScreenActivity) getActivity();
        setListeners();
        return view;
    }

    private void setListeners() {
        btn_reset_password.setOnClickListener(this);
    }


    private void initUi(View view) {
        edt_old_password = (EditText) view.findViewById(R.id.edt_old_password);
        edt_new_password = (EditText) view.findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) view.findViewById(R.id.edt_confirm_password);
        btn_reset_password = (Button) view.findViewById(R.id.btn_reset_password);
    }


    private boolean validate() {
        if (!edt_old_password.getText().toString().matches(regexp)) {
            Toast.makeText(activity, "Old Password Length Should be Minimum 8 Character long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edt_new_password.getText().toString().matches(regexp)) {
            Toast.makeText(activity, "New Password Length Should be Minimum 8 Character long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edt_confirm_password.getText().toString().matches(regexp)) {
            Toast.makeText(activity, "Confirm Password Length Should be Minimum 8 Character long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edt_new_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
            edt_new_password.setError("New Password and Confirm Password should be same");
            return false;
        }
        return true;
    }

    private void changePasswordApi() {
        ChangePasswordRequestModel changePasswordRequestModel = new ChangePasswordRequestModel();
        changePasswordRequestModel.setOldPassword(edt_old_password.getText().toString());
        changePasswordRequestModel.setNewPassword(edt_new_password.getText().toString());
        changePasswordRequestModel.setConfirmPassword(edt_confirm_password.getText().toString());
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask changePasswordApiAsyncTask = asyncTaskForRequest.getChangePasswordRequestAsyncTask(changePasswordRequestModel);
        changePasswordApiAsyncTask.setApiCallAsyncTaskDelegate(new ChangePasswordApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            changePasswordApiAsyncTask.execute(changePasswordApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset_password) {
            if (validate()) {
                changePasswordApi();
            }
        }
    }


    private class ChangePasswordApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                Toast.makeText(activity, R.string.password_changed_successfully, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, json, Toast.LENGTH_SHORT).show();
                Logger.error(json);
            }
        }

        @Override
        public void onApiCancelled() {
            Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
