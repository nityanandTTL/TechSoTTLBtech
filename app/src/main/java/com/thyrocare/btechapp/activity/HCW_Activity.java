package com.thyrocare.btechapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.thyrocare.btechapp.Controller.HCWController;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.models.data.HCWRequestModel;
import com.thyrocare.btechapp.models.data.HCWResponseModel;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

public class HCW_Activity extends AppCompatActivity {

    ConnectionDetector cd;
    WebView wv_hcw;
    String url = "";
    Global globalClass;
    String ecode = "";
    Activity activity;
    Toolbar toolbarHCW;
    AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_c_w_);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        initUI();

        setView(url);
        postEcode();

    }

    public void getSubmitDataResponse(HCWResponseModel hcwResponseModel) {
        if (hcwResponseModel != null && hcwResponseModel.getResult() != null) {
            url = hcwResponseModel.getResult();
            setView(url);
        }
    }

    private void setView(String url) {
//        wv_hcw.loadUrl(url);
        wv_hcw.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
        wv_hcw.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                globalClass.hideProgressDialog(activity);

            }
        });
    }

    private void postEcode() {
        try {
            if (cd.isConnectingToInternet()) {

                HCWRequestModel hcwRequestModel = new HCWRequestModel();
                hcwRequestModel.setUserCode((appPreferenceManager.getLoginResponseModel().getUserID()));
                hcwRequestModel.setDomain("BTECH");
                HCWController hcwControlller = new HCWController(this);
                hcwControlller.postHCW(hcwRequestModel);
            } else {
                globalClass.showCustomToast(activity, ConstantsMessages.CHECK_INTERNET_CONN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("NewApi")
    private void initUI() {
        toolbarHCW = findViewById(R.id.toolbarHCW);
        toolbarHCW.setTitle("HCW Letter");
        toolbarHCW.setTitleTextColor(Color.WHITE);
        appPreferenceManager = new AppPreferenceManager(this);

        globalClass = new Global(this);

        activity = this;

        ecode = appPreferenceManager.getLoginResponseModel().getUserID();


        cd = new ConnectionDetector(this);

        wv_hcw = findViewById(R.id.wv_hcw);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity.finish();
    }
}