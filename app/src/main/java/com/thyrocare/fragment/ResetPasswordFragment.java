package com.thyrocare.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.models.api.request.ChangePasswordRequestModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;

import org.json.JSONException;

public class ResetPasswordFragment extends AbstractFragment implements View.OnClickListener {
    public static final String TAG_FRAGMENT = ResetPasswordFragment.class.getSimpleName();
    private EditText edt_old_password, edt_new_password, edt_confirm_password;
    private Button btn_reset_password;
    String regexp = ".{6,12}";
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
        activity = (HomeScreenActivity) getActivity();
        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("Reset Password");
        }
        activity.isOnHome = false;
        initUI(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        btn_reset_password.setOnClickListener(this);
    }


    private void initUI(View view) {
        edt_old_password = (EditText) view.findViewById(R.id.edt_old_password);
        edt_new_password = (EditText) view.findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) view.findViewById(R.id.edt_confirm_password);
        btn_reset_password = (Button) view.findViewById(R.id.btn_reset_password);
    }


    private boolean validate() {
        if (!edt_old_password.getText().toString().matches(regexp)) {
            edt_old_password.setError(getString(R.string.password_criteria));
            edt_old_password.requestFocus();
            return false;
        } else if (!edt_new_password.getText().toString().matches(regexp)) {
            edt_new_password.setError(getString(R.string.password_criteria));
            edt_new_password.requestFocus();
            return false;
        } else if (!edt_confirm_password.getText().toString().matches(regexp)) {
            edt_confirm_password.setError(getString(R.string.password_criteria));
            edt_confirm_password.requestFocus();
            return false;
        } else if (!edt_new_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
            edt_confirm_password.setError(getString(R.string.password_do_not_match));
            edt_confirm_password.requestFocus();
            return false;
        }else if (edt_old_password.getText().toString().equals(edt_new_password.getText().toString())) {
            edt_new_password.setError(getString(R.string.old_pwd_and_new_pwd_should_not_be_same));
            edt_new_password.requestFocus();
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
                Fragment mFragment = new HomeScreenFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_homeScreen, mFragment).commit();

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
