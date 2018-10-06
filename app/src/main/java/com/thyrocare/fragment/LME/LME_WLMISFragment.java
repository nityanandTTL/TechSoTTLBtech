package com.thyrocare.fragment.LME;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thyrocare.Controller.TSPLMESampleDropController;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.models.data.DateWiseWLMISDetailsModel;
import com.thyrocare.models.data.SampleDropDetailsbyTSPLMEDetailsModel;
import com.thyrocare.models.data.WLMISDetailsModel;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
    LinearLayout ll_staffstat, ll_wlmis;
    TableRow tr_itlcrh_dateWise,tr_itlcrh;
    Button btn_daily, btn_dateWise;
    private boolean isDailyPickup = true;

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
        initListeners();
        fetchData();
        return rootView;
    }

    private void initListeners() {
        btn_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_daily.setBackgroundColor(getResources().getColor(R.color.bgContentTop));
                btn_daily.setTextColor(getResources().getColor(R.color.white));
                btn_dateWise.setTextColor(getResources().getColor(R.color.black));
                btn_dateWise.setBackgroundColor(getResources().getColor(R.color.white));
                isDailyPickup = true;
                fetchData();
                tr_itlcrh.setVisibility(View.VISIBLE);
                tr_itlcrh_dateWise.setVisibility(View.GONE);
            }
        });
        btn_dateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_dateWise.setBackgroundColor(getResources().getColor(R.color.bgContentTop));
                btn_dateWise.setTextColor(getResources().getColor(R.color.white));
                btn_daily.setTextColor(getResources().getColor(R.color.black));
                btn_daily.setBackgroundColor(getResources().getColor(R.color.white));
                isDailyPickup = false;
                fetchDateWiseData();
                tr_itlcrh.setVisibility(View.GONE);
                tr_itlcrh_dateWise.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    public void initUI() {

        nodata = (TextView) rootView.findViewById(R.id.nodata);
        ll_staffstat = (LinearLayout) rootView.findViewById(R.id.ll_staffstat);
        ll_wlmis = (LinearLayout) rootView.findViewById(R.id.ll_wlmis);
        btn_daily = (Button) rootView.findViewById(R.id.btn_daily);
        btn_dateWise = (Button) rootView.findViewById(R.id.btn_dateWise);
        tr_itlcrh_dateWise=(TableRow)rootView.findViewById(R.id.tr_itlcrh_dateWise);
        tr_itlcrh=(TableRow)rootView.findViewById(R.id.tr_itlcrh);
        btn_daily.setBackgroundColor(getResources().getColor(R.color.bgContentTop));
        tr_itlcrh_dateWise.setVisibility(View.GONE);
    }

    private void fetchData() {
        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }
        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallGetWLMIS(appPreferenceManager.getLoginResponseModel().getUserID());
    }

    private void fetchDateWiseData() {
        if (ApplicationController.mTSPLMESampleDropController != null) {
            ApplicationController.mTSPLMESampleDropController = null;
        }
        ApplicationController.mTSPLMESampleDropController = new TSPLMESampleDropController(activity, fragment);
        ApplicationController.mTSPLMESampleDropController.CallGetDateWiseWLMIS(appPreferenceManager.getLoginResponseModel().getUserID());
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

    public void SetDateWiseOrdersList(ArrayList<DateWiseWLMISDetailsModel> materialDetailsModels) {
        if (materialDetailsModels.size() != 0) {
            nodata.setVisibility(View.GONE);
            ll_staffstat.setVisibility(View.VISIBLE);
            InItDataDateWise(materialDetailsModels);
        } else {
            NodataFound();
        }
    }


    private void InItData(ArrayList<WLMISDetailsModel> materialDetailsModels) {
        if (materialDetailsModels != null) {


            Collections.sort(materialDetailsModels, new Comparator<WLMISDetailsModel>() {
                @Override
                public int compare(WLMISDetailsModel o1, WLMISDetailsModel o2) {
                    Date date1 = null, date2 = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                    try {
                        date1 = formatter.parse(o1.getCompletedAt());
                        date2 = formatter.parse(o2.getCompletedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (o1.getCompletedAt() == null || o2.getCompletedAt() == null) {
                        return 0;
                    }
                    return date1.compareTo(date2);
                }
            });

            ll_wlmis.removeAllViews();
            for (int i = 0; i < materialDetailsModels.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.wlmis_row, null, false);

                TextView txt_datetime, txt_batch, txt_code, txt_wl, txt_turnover, txt_sr,txt_pickups;

                txt_sr = (TextView) view.findViewById(R.id.txt_sr);
                txt_datetime = (TextView) view.findViewById(R.id.txt_datetime);
                txt_batch = (TextView) view.findViewById(R.id.txt_batch);
                txt_wl = (TextView) view.findViewById(R.id.txt_wl);
                txt_turnover = (TextView) view.findViewById(R.id.txt_turnover);
                txt_code = (TextView) view.findViewById(R.id.txt_code);
                txt_pickups = (TextView) view.findViewById(R.id.txt_pickups);

                txt_batch.setVisibility(View.VISIBLE);
                txt_turnover.setVisibility(View.VISIBLE);
                txt_code.setVisibility(View.VISIBLE);
                txt_pickups.setVisibility(View.GONE);

                txt_sr.setText("" + (i + 1));
                txt_datetime.setText("" + DateUtils.Req_Date_Req(materialDetailsModels.get(i).getCompletedAt(), "dd-MM-yyyy HH:mm a", "dd-MM-yyyy") + " \n " + DateUtils.Req_Date_Req(materialDetailsModels.get(i).getCompletedAt(), "dd-MM-yyyy hh:mm a", "hh:mm a"));
                txt_batch.setText("" + materialDetailsModels.get(i).getBatch());
                txt_wl.setText("" + materialDetailsModels.get(i).getWL());
                txt_turnover.setText("");
                txt_code.setText("" + materialDetailsModels.get(i).getLMECode());

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

                ll_wlmis.addView(view);

            }
        }
    }

    private void InItDataDateWise(ArrayList<DateWiseWLMISDetailsModel> materialDetailsModels) {
        if (materialDetailsModels != null) {
ll_wlmis.removeAllViews();

            for (int i = 0; i < materialDetailsModels.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater.inflate(R.layout.wlmis_row, null, false);

                TextView txt_datetime, txt_batch, txt_code, txt_wl, txt_turnover, txt_sr,txt_pickups;

                txt_sr = (TextView) view.findViewById(R.id.txt_sr);
                txt_datetime = (TextView) view.findViewById(R.id.txt_datetime);
                txt_batch = (TextView) view.findViewById(R.id.txt_batch);
                txt_wl = (TextView) view.findViewById(R.id.txt_wl);
                txt_turnover = (TextView) view.findViewById(R.id.txt_turnover);
                txt_code = (TextView) view.findViewById(R.id.txt_code);
                txt_pickups = (TextView) view.findViewById(R.id.txt_pickups);

                txt_batch.setVisibility(View.GONE);
                txt_turnover.setVisibility(View.GONE);
                txt_code.setVisibility(View.GONE);
                txt_pickups.setVisibility(View.VISIBLE);

                txt_sr.setText("" + (i + 1));
                txt_datetime.setText("" + materialDetailsModels.get(i).getCompletedAt());
                txt_wl.setText("" + materialDetailsModels.get(i).getWL());
                txt_pickups.setText(""+materialDetailsModels.get(i).getPickUp());

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

                ll_wlmis.addView(view);
            }
        }
    }

    public void NodataFound() {
        nodata.setVisibility(View.VISIBLE);
        ll_staffstat.setVisibility(View.GONE);
    }

}
