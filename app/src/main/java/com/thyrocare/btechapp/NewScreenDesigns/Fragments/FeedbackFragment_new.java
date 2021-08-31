package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.thyrocare.btechapp.Controller.BottomSheetController;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.FeedbackModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.CommonResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.FeedbackListModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.CHECK_INTERNET_CONN;
import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SOMETHING_WENT_WRONG;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment_new extends AppCompatActivity {

    public static final String TAG_FRAGMENT = FeedbackFragment_new.class.getSimpleName();
    Global global;
    ConnectionDetector connectionDetector;
    Spinner spnType;
    EditText edtQuery;
    Button btnSubmit;
    Activity activity;
    ArrayList<String> arrFeedbackList;
    ArrayAdapter<String> spinnerAdapter;
    String feedbackType, query;
    private AppPreferenceManager appPreferenceManager;
    private String deviceInfo = "";
    TextView tv_toolbar;
    Toolbar toolbar;
    ImageView iv_back,iv_home;

   /* public FeedbackFragment_new() {
        // Required empty public constructor
    }

    public static FeedbackFragment_new newInstance() {
        FeedbackFragment_new fragment = new FeedbackFragment_new();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback_new);

        activity = this;

        global = new Global(activity);

//        Global.appBar(activity);

        connectionDetector = new ConnectionDetector(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        initUI();
        initListeners();
        setDeviceInfo();

        if (connectionDetector.isConnectingToInternet()) {
            getFeedbackList();
        } else {
            global.showCustomToast(activity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
        }

    }

    public void initUI() {
        spnType =findViewById(R.id.spinType);
        edtQuery = findViewById(R.id.edtQuery);
        btnSubmit = findViewById(R.id.btnSubmit);
        toolbar = findViewById(R.id.toolbar);
        iv_back =   findViewById(R.id.iv_back);
        iv_home =   findViewById(R.id.iv_home);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Feedback");
    }

    public void initListeners() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDeviceInfo() {
        String myDeviceModel = "";
        try {
            myDeviceModel = android.os.Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            myDeviceModel = "";
        }

        String myDevice = "";
        try {
            myDevice = android.os.Build.DEVICE;
        } catch (Exception e) {
            e.printStackTrace();
            myDevice = "";
        }

        String myDevicePRODUCT = "";
        try {
            myDevicePRODUCT = android.os.Build.PRODUCT;
        } catch (Exception e) {
            e.printStackTrace();
            myDevicePRODUCT = "";
        }

        String myDeviceVersion = "";
        try {
            myDeviceVersion = android.os.Build.VERSION.SDK;
        } catch (Exception e) {
            e.printStackTrace();
            myDeviceVersion = "";
        }

        deviceInfo = myDeviceModel + " " + myDevice + " " + myDevicePRODUCT + " " + myDeviceVersion;

        MessageLogger.PrintMsg("Nitya >> " + deviceInfo);
    }
//    https://b2capi.thyrocare.com/APIs/MASTER.svc//dNBJfkhL5z8ng3lsNOuAEL2D73G@FDOC0z3fvmKQjiw=//FEEDBACK/getlist

    public void getFeedbackList() {
        try {
            GetAPIInterface getAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2C_API_VERSION))).create(GetAPIInterface.class);
            Call<FeedbackListModel> feedbackListModelCall = getAPIInterface.getFeedbackList(AppConstants.API_KEY);
            global.showProgressDialog(activity, activity.getResources().getString(R.string.loading_feedbacklist), false);
            feedbackListModelCall.enqueue(new Callback<FeedbackListModel>() {
                @Override
                public void onResponse(Call<FeedbackListModel> call, Response<FeedbackListModel> response) {
                    global.hideProgressDialog(activity);
                    FeedbackListModel feedbackListModel = response.body();
                    if (feedbackListModel != null && feedbackListModel.getRESPONSE() != null) {
                        if (feedbackListModel.getRESPONSE().equalsIgnoreCase("SUCCESS")) {
                            fetchFeedbackList(feedbackListModel);
                        } else {
                            global.showCustomToast(activity, feedbackListModel.getRESPONSE(), Toast.LENGTH_SHORT);
                        }
                    } else {
                        global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(Call<FeedbackListModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchFeedbackList(FeedbackListModel feedbackListModel) {
        arrFeedbackList = new ArrayList<>();
        arrFeedbackList.add("Select Feedback Type*");
        if (feedbackListModel.getMASTER() != null && feedbackListModel.getMASTER().size() > 0) {
            for (int i = 0; i < feedbackListModel.getMASTER().size(); i++) {
                arrFeedbackList.add(feedbackListModel.getMASTER().get(i).getVALUE());
            }
        } else {
            MessageLogger.LogDebug(TAG_FRAGMENT, "fetchFeedbackList NULL");
        }
        setFeedbackData();
    }

    private void setFeedbackData() {
        spinnerAdapter = new ArrayAdapter<>(activity, R.layout.spinneritems, R.id.feedbackCategory, arrFeedbackList);
        spnType.setAdapter(spinnerAdapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                feedbackType = (String) spnType.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean validateFeedback() {
        if (spnType.getSelectedItemPosition() == 0) {
            spnType.requestFocus();
            global.showCustomToast(activity, activity.getResources().getString(R.string.select_feedback_type), Toast.LENGTH_SHORT);
            return false;
        } else if (edtQuery.getText().toString().trim().equals("")) {
            edtQuery.setError("Enter Feedback");
            edtQuery.requestFocus();
            return false;
        }
        return true;
    }

    public void submitFeedback() {
        if (validateFeedback()) {
            query = edtQuery.getText().toString().trim();
            FeedbackModel feedbackModel = new FeedbackModel();
            feedbackModel.setApi_key(AppConstants.API_KEY);
            feedbackModel.setDisplay_type("FEEDBACK");
            feedbackModel.setPurpose(feedbackType);
            feedbackModel.setName(appPreferenceManager.getLoginResponseModel().getUserName());
            feedbackModel.setEmail(appPreferenceManager.getLoginResponseModel().getEmailId());
            feedbackModel.setMobile(appPreferenceManager.getLoginResponseModel().getMobile());
            feedbackModel.setFeedback(query + " - Btech app " + deviceInfo);
            feedbackModel.setEmotion_text(":-|");
            feedbackModel.setRating("NEUTRAL");
            if (connectionDetector.isConnectingToInternet()) {
                postFeedback(feedbackModel);
            } else {
                global.showCustomToast(activity, CHECK_INTERNET_CONN, Toast.LENGTH_SHORT);
            }
        }
    }

    public void postFeedback(FeedbackModel feedbackModel) {
        try {
            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(getString(R.string.B2C_API_VERSION))).create(PostAPIInterface.class);
            Call<CommonResponseModel> commonResponeModelCall = postAPIInterface.postFeedback(feedbackModel);
            global.showProgressDialog(activity, activity.getResources().getString(R.string.submitting_feedback), false);
            commonResponeModelCall.enqueue(new Callback<CommonResponseModel>() {
                @Override
                public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                    global.hideProgressDialog(activity);
                    CommonResponseModel commonResponseModel = response.body();
                    fetchResponse(commonResponseModel);
                }

                @Override
                public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                    global.hideProgressDialog(activity);
                    global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchResponse(CommonResponseModel commonResponseModel) {
        if (commonResponseModel != null && commonResponseModel.getRESPONSE1() != null) {
            if (commonResponseModel.getRESPONSE1().equalsIgnoreCase("SUCCESS")) {
//                global.showCustomToast(activity, activity.getResources().getString(R.string.feedback_submitted), Toast.LENGTH_SHORT);

                BottomSheetController bottomSheetController = new BottomSheetController(activity);
                bottomSheetController.SetOKBottomSheet(activity.getString(R.string.feedback_submitted));
                edtQuery.setText("");
                spnType.setSelection(0);

            } else {
                global.showCustomToast(activity, commonResponseModel.getRESPONSE1(), Toast.LENGTH_SHORT);
            }
        } else {
            global.showCustomToast(activity, SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT);
        }
    }

}
