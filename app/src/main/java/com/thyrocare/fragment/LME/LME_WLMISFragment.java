package com.thyrocare.fragment.LME;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.thyrocare.Controller.TSPLMESampleDropController;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.adapter.LME.LMEVisitsListAdapter;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.models.data.ScannedMasterBarcodebyLMEPOSTDATAModel;
import com.thyrocare.models.data.WLMISDetailsModel;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.DateUtils;
import com.thyrocare.utils.app.GPSTracker;

import java.util.ArrayList;

/**
 * http://bts.dxscloud.com/btsapi/api/OrderVisitDetails/884543107
 */
public class LME_WLMISFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "LME_OrdersDisplayFragment";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    static LME_WLMISFragment fragment;
    private SampleDropDetailsbyTSPLMEDetailsModel msampleDropDetailsbyTSPLMEDetailsModel;
    TextView nodata;
    LinearLayout ll_staffstat;

    public LME_WLMISFragment() {
        // Required empty public constructor
    }

    public static LME_WLMISFragment newInstance() {
        fragment = new LME_WLMISFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        if (activity.toolbarHome != null) {
            activity.toolbarHome.setTitle("WL MIS");
        }
        activity.isOnHome = false;
        appPreferenceManager = new AppPreferenceManager(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_wlmis_display, container, false);
        initUI();
        fetchData();
        return rootView;
    }

    @Override
    public void initUI() {
        nodata = (TextView) rootView.findViewById(R.id.nodata);
        ll_staffstat = (LinearLayout) rootView.findViewById(R.id.ll_staffstat);
    }

    private void fetchData() {
        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }

        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallGetWLMIS(appPreferenceManager.getLoginResponseModel().getUserID());
    }

    public void SetOrdersList(ArrayList<WLMISDetailsModel> materialDetailsModels) {
        if (materialDetailsModels.size() != 0) {
            nodata.setVisibility(View.GONE);
            ll_staffstat.setVisibility(View.VISIBLE);
            InItData(materialDetailsModels);
        } else {
            NodataFound();
        }
    }

    private void InItData(ArrayList<WLMISDetailsModel> materialDetailsModels) {
        if(materialDetailsModels != null){
            for (int i = 0; i < materialDetailsModels.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.wlmis_row, null, false);

                TextView txt_datetime, txt_batch, txt_code, txt_wl, txt_turnover, txt_sr;

                txt_sr = (TextView) view.findViewById(R.id.txt_sr);
                txt_datetime = (TextView) view.findViewById(R.id.txt_datetime);
                txt_batch = (TextView) view.findViewById(R.id.txt_batch);
                txt_wl = (TextView) view.findViewById(R.id.txt_wl);
                txt_turnover = (TextView) view.findViewById(R.id.txt_turnover);
                txt_code = (TextView) view.findViewById(R.id.txt_code);

                txt_sr.setText(""+(i+1));
                txt_datetime.setText(""+DateUtils.Req_Date_Req(materialDetailsModels.get(i).getCompletedAt(), "dd-MM-yyyy HH:mm a", "dd-MM-yyyy")+" \n "+DateUtils.Req_Date_Req(materialDetailsModels.get(i).getCompletedAt(), "dd-MM-yyyy HH:mm a", "HH:mm a"));
                txt_batch.setText(""+materialDetailsModels.get(i).getBatch());
                txt_wl.setText(""+materialDetailsModels.get(i).getWL());
                txt_turnover.setText("");
                txt_code.setText(""+materialDetailsModels.get(i).getLMECode());

                if (i % 2 == 0) {
                    txt_sr.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                    txt_datetime.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                    txt_batch.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                    txt_wl.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                    txt_turnover.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                    txt_code.setBackgroundColor(getResources().getColor(R.color.zebragrey));
                } else {
                    txt_sr.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                    txt_datetime.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                    txt_batch.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                    txt_wl.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                    txt_turnover.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                    txt_code.setBackgroundColor(getResources().getColor(R.color.zebrablue));
                }

                ll_staffstat.addView(view);
            }
        }
    }

    public void NodataFound() {
        nodata.setVisibility(View.VISIBLE);
        ll_staffstat.setVisibility(View.GONE);
    }

}
