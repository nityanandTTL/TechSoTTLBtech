package com.dhb.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhb.R;
import com.dhb.activity.HomeScreenActivity;
import com.dhb.activity.PaymentsActivity;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractFragment;
import com.dhb.utils.api.Logger;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.BundleConstants;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.InputUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeScreenFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtUserName, txt_no_of_camps;
    private CircularImageView rvSelfie;
    private ImageView imgPayment, imgOrders, imgSchedule, imgMaterials, imgOLCPickup, imgHub, imgCamp, ordersserved, imgLedger;
    private ImageView bellicon;
    Dialog MainDailog;

    //tsp
    ImageView send_icon, receive_icon, earning_icon;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        activity.toolbarHome.setTitle("Home");
        appPreferenceManager = new AppPreferenceManager(activity);
        activity.isOnHome = true;

        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //tsp
        if (appPreferenceManager.getLoginRole().equalsIgnoreCase("9")) {//loginRole.equalsIgnoreCase("9")
            rootView = inflater.inflate(R.layout.tsp_fragment_home_screen, container, false);
            initUI_TSP();
            initData();
            getCampDetailCount();
            initListeners_TSP();
        } else {//for btech & hub login
            rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
            initUI();
            initData();
            getCampDetailCount();
            initListeners();

        }
        return rootView;
    }

    private void initListeners_TSP() {
        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(TSP_SendFragment.newInstance(), false, false, TSP_SendFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });

        receive_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(TSP_HubMasterBarcodeScanFragment.newInstance(), false, false, TSP_HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });

        earning_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI_TSP() {
        rvSelfie = (CircularImageView) rootView.findViewById(R.id.img_user_picture);
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        send_icon = (ImageView) rootView.findViewById(R.id.send_icon);
        receive_icon = (ImageView) rootView.findViewById(R.id.receive_icon);
        earning_icon = (ImageView) rootView.findViewById(R.id.earning_icon);
    }

    private void getCampDetailCount() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(activity);
        ApiCallAsyncTask campDetailCountApiAsyncTask = asyncTaskForRequest.getCampDetailsCountRequestAsyncTask();
        campDetailCountApiAsyncTask.setApiCallAsyncTaskDelegate(new CampDetailsCountApiAsyncTaskDelegateResult());
        if (isNetworkAvailable(activity)) {
            campDetailCountApiAsyncTask.execute(campDetailCountApiAsyncTask);
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private void initData() {
        txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
        if (appPreferenceManager.getSelfieResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getSelfieResponseModel().getPic())) {

            //changed_for_selfie_2june_2017
            rvSelfie.setImageBitmap(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));
            /*File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
            Uri outPutfileUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
            Bitmap thumbnailToDisplay = null;
            try {
                thumbnailToDisplay = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outPutfileUri);
                ByteArrayOutputStream bytesToDisplay = new ByteArrayOutputStream();
                thumbnailToDisplay.compress(Bitmap.CompressFormat.JPEG, 90, bytesToDisplay);
                Drawable img = new BitmapDrawable(getResources(), thumbnailToDisplay);
                rvSelfie.setImageDrawable(img);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //changed_for_selfie_2june_2017
        }
    }

    private void initListeners() {
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btech_hub
                //for btech with hub login...role will be 6 for this
                if (appPreferenceManager.getLoginRole().equalsIgnoreCase("6")) {//loginRole.equalsIgnoreCase("6")
                    MainDailog = new Dialog(getActivity());
                    MainDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MainDailog.setContentView(R.layout.btech_dialog_btechwithhub);

                    LinearLayout send, receive;
                    send = (LinearLayout) MainDailog.findViewById(R.id.send);
                    receive = (LinearLayout) MainDailog.findViewById(R.id.receive);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainDailog.dismiss();
                            pushFragments(HubListDisplayFragment.newInstance(1), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                        }
                    });

                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pushFragments(BtechwithHub_HubMasterBarcodeScanFragment.newInstance(), false, false, BtechwithHub_HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                            MainDailog.dismiss();
                        }
                    });

                    MainDailog.show();
                } else {//for normal btech login...
                    pushFragments(HubListDisplayFragment.newInstance(), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                }

            }
        });

        imgPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPaymentsActivity = new Intent(activity, PaymentsActivity.class);
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_ORDER_NO, "");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_AMOUNT, "0");
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_SOURCE_CODE, Integer.parseInt(appPreferenceManager.getLoginResponseModel().getUserID()));
                intentPaymentsActivity.putExtra(BundleConstants.PAYMENTS_NARRATION_ID, 3);
                startActivity(intentPaymentsActivity);
            }
        });
        imgSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(ScheduleYourDayFragment.newInstance(), false, false, ScheduleYourDayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(activity, "Feature Coming Soon..", Toast.LENGTH_SHORT).show();
                pushFragments(MaterialFragment.newInstance(), false, false, MaterialFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgOLCPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(OLCPickupListDisplayFragment.newInstance(), false, false, OLCPickupListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(CampListDisplayFragment.newInstance(), false, false, CampListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        ordersserved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Feature coming soon.", Toast.LENGTH_SHORT).show();
                pushFragments(OrderServedFragment.newInstance(), false, false, OrderServedFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LedgerDisplayFragment.newInstance(), false, false, LedgerDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
    }

    @Override
    public void initUI() {
        super.initUI();
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        rvSelfie = (CircularImageView) rootView.findViewById(R.id.img_user_picture);
        imgPayment = (ImageView) rootView.findViewById(R.id.payment_icon);
        imgOrders = (ImageView) rootView.findViewById(R.id.orders_booked);
        imgSchedule = (ImageView) rootView.findViewById(R.id.schedule_icon);
        imgHub = (ImageView) rootView.findViewById(R.id.hub_icon);
        imgOLCPickup = (ImageView) rootView.findViewById(R.id.olc_pickup_icon);
        ordersserved = (ImageView) rootView.findViewById(R.id.ordersserved);
        imgLedger = (ImageView) rootView.findViewById(R.id.Ledger_icon);
        imgCamp = (ImageView) rootView.findViewById(R.id.camp_icon);
        imgMaterials = (ImageView) rootView.findViewById(R.id.materials_icon);
        imgOLCPickup = (ImageView) rootView.findViewById(R.id.olc_pickup_icon);
        txt_no_of_camps = (TextView) rootView.findViewById(R.id.txt_no_of_camps);

        //bell_icon
        bellicon = (ImageView) rootView.findViewById(R.id.bellicon);
        Logger.debug("bellicon_counter" + appPreferenceManager.getScheduleCounter());

        if (appPreferenceManager.getScheduleCounter().isEmpty() || appPreferenceManager.getScheduleCounter().equals("n")) {
            bellicon.setVisibility(View.VISIBLE);
        } else {
            bellicon.setVisibility(View.INVISIBLE);
        }
        //bell_icon
    }

    private class CampDetailsCountApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                JSONObject jsonObject = new JSONObject(json);

                //btech_hub
                String btechID = jsonObject.getString("BtechId");
                appPreferenceManager.setBtechID(btechID);

                //tsp
                if (!appPreferenceManager.getLoginRole().equalsIgnoreCase("9"))
                    txt_no_of_camps.setText("" + jsonObject.getString("CampCount"));
            } else {
                //tsp
                if (!appPreferenceManager.getLoginRole().equalsIgnoreCase("9"))
                    Toast.makeText(activity, "Failed to Fetch Camp Count", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, "Failed to Fetch Hub ID", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
