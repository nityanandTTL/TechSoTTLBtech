package com.thyrocare.btechapp.NewScreenDesigns.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Adapters.DisplayBtechCertificateViewPagerAdapter;
import com.thyrocare.btechapp.NewScreenDesigns.Models.RequestModels.GetBtechCertificateRequestModel;
import com.thyrocare.btechapp.NewScreenDesigns.Models.ResponseModel.GetBtechCertifcateResponseModel;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.StringUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.activity.HomeScreenActivity;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BtechCertificateFragment extends Fragment {

    public static final String TAG_FRAGMENT = "Btech Certificatess Fragment";
    private HomeScreenActivity activity;
    private Global globalclass;
    private ConnectionDetector cd;
    private AppPreferenceManager appPreferenceManager;
    private UltraViewPager ultraViewPager;
    private TextView tv_noDatafound;

    public BtechCertificateFragment() {
        // Required empty public constructor
    }

    public static BtechCertificateFragment newInstance() {
        BtechCertificateFragment fragment = new BtechCertificateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_btech_certificate, container, false);
        initContext();
        initViews(V);
        initData();
        return V ;
    }

    private void initContext() {
        activity = (HomeScreenActivity) getActivity();
        globalclass = new Global(activity);
        cd = new ConnectionDetector(activity);
        try {
            activity.toolbarHome.setTitle("My Certificatess");
        } catch (Exception e) {
            e.printStackTrace();
        }
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    private void initViews(View v) {
        tv_noDatafound = (TextView)v.findViewById(R.id.tv_noDatafound);
        ultraViewPager = (UltraViewPager)v.findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
    }


    private void initData() {
        if (cd.isConnectingToInternet()){
            CallCampWOEMisAPI();
        }else{
            globalclass.showCustomToast(activity, ConstantsMessages.CHECK_INTERNET_CONN);
        }
    }

    private void CallCampWOEMisAPI() {

        GetBtechCertificateRequestModel model = new GetBtechCertificateRequestModel();
        model.setBtechId(appPreferenceManager.getLoginResponseModel().getUserID());
        model.setDTLType("Certificates");
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.DecodeString64(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<GetBtechCertifcateResponseModel> responseCall = apiInterface.CallGetBtechCertificatesAPI(model);
        globalclass.showProgressDialog(activity,ConstantsMessages.PLEASE_WAIT);
        responseCall.enqueue(new Callback<GetBtechCertifcateResponseModel>() {
            @Override
            public void onResponse(Call<GetBtechCertifcateResponseModel> call, Response<GetBtechCertifcateResponseModel> response) {
                globalclass.hideProgressDialog();
                InitViewPager(response.body());
            }
            @Override
            public void onFailure(Call<GetBtechCertifcateResponseModel> call, Throwable t) {
                ultraViewPager.setVisibility(View.GONE);
                tv_noDatafound.setVisibility(View.VISIBLE);
                globalclass.hideProgressDialog();
            }
        });
    }

    private void InitViewPager(GetBtechCertifcateResponseModel model){

        if (model != null && StringUtils.CheckEqualIgnoreCase(model.getRespId(),"RES0000") && model.getCertificates() != null && model.getCertificates().size() > 0){
            ultraViewPager.setVisibility(View.VISIBLE);
            tv_noDatafound.setVisibility(View.GONE);


            DisplayBtechCertificateViewPagerAdapter certificateViewPagerAdapter = new DisplayBtechCertificateViewPagerAdapter(activity,model.getCertificates());
            ultraViewPager.setAdapter(certificateViewPagerAdapter);

            ultraViewPager.initIndicator(); //initialize built-in indicator
            //set style of indicators
            ultraViewPager.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusColor(getResources().getColor(R.color.colorOrange))
                    .setNormalColor(Color.WHITE)
                    .setMargin(0,0,0,40)
                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));

            //set the alignment
            ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            //construct built-in indicator, and add it to  UltraViewPager
            ultraViewPager.getIndicator().build();
            ultraViewPager.setItemMargin(10,10,10, 10);
            ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());

            //set an infinite loop
//            ultraViewPager.setInfiniteLoop(true);
            //enable auto-scroll mode
//            ultraViewPager.setAutoScroll(2000);

        }else{
            ultraViewPager.setVisibility(View.GONE);
            tv_noDatafound.setVisibility(View.VISIBLE);
        }
    }
}
