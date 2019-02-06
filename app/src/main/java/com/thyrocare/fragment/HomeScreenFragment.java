package com.thyrocare.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.thyrocare.R;
import com.thyrocare.activity.HomeScreenActivity;
import com.thyrocare.activity.LoginScreenActivity;
import com.thyrocare.activity.PaymentsActivity;
import com.thyrocare.customview.CustomDeviceResetDailog;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.thyrocare.fragment.LME.LME_OrdersDisplayFragment;
import com.thyrocare.fragment.LME.LME_WLMISFragment;
import com.thyrocare.fragment.tsp.TSP_OrdersDisplayFragment;
import com.thyrocare.models.data.DeviceLoginDetailsModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.network.ResponseParser;
import com.thyrocare.service.TrackerService;
import com.thyrocare.uiutils.AbstractFragment;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * <br/> <b>*TITLE:-HUB</b><br/>
 * HomescreenFragment.java
 * <p>
 * #For Send Functionality :<br/>
 * HubListDisplayFragment.java<br/>
 * HubMasterBarcodeScanFragment.java<br/>
 * DispatchtoHubDisplayDetailsAdapter.java<br/>
 * HubScanBarcodeListAdapter.java<br/>
 * DispatchHubDisplayDetailsResponseModel.java<br/>
 * HubBtechModel.java<br/>
 * BtechCollectionsResponseModel.java<br/>
 * MasterBarcodeMappingRequestModel.java<br/>
 * HubBarcodeModel.java<br/>
 * <p>
 * For Receive Functionality :<br/>
 * BtechwithHub_HubMasterBarcodeScanFragment.java<br/>
 * BtechwithHub_HubScanBarcodeListAdapter.java<br/>
 * BtechwithHubResponseModel.java<br/>
 * BtechwithHubBarcodeDataModel.java<br/>
 * BtechwithHub_MasterBarcodeMappingRequestModel.java<br/>
 * <p>
 * Details :<br/>
 * Btech with hub login has a login role of 6 & the flow goes through selfieupload screen.<br/>
 * 2)When we click on the Hub icon on the homescreen then depending upon the role the flow changes between btech & btech_with_hub.<br/>
 * If the role is 6 then btech_with_hub flow starts else normal btech flow executes.<br/>
 * When the login is btech with hub then on the click of hub icon on the homescreen a popup<br/>
 * Appears with two options : 1)send , 2) receive<br/>
 * When we click on the send option then the flow is exactly same as the normal btech hub<br/>
 * <p>
 * <br/> <b>*TITLE:-TSP</b><br/>
 * #For Send Functionality :<br/>
 * TSP_SendFragment.java<br/>
 * Tsp_HubScanBarcodeListAdapter.java<br/>
 * Tsp_SendMode_DataModel.java<br/>
 * Tsp_ScanBarcodeResponseModel.java<br/>
 * Tsp_Send_RequestModel.java<br/>
 * Tsp_ScanBarcodeDataModel.java<br/>
 * #For Receive Functionality :<br/>
 * Tsp_HubMasterBarcodeScanFragment.java<br/>
 * BtechwithHub_HubScanBarcodeListAdapter.java<br/>
 * BtechwithHubResponseModel.java<br/>
 * BtechwithHub_MasterBarcodeMappingRequestModel.java<br/>
 * BtechwithHub_BarcodeDataModel.java<br/>
 * <p>
 * Details :<br/>
 * TSP module has a role of 9 and the flow does not flow through selfieupload screen.After login the user is redirected to Homescreen directly.<br/>
 * TSP module has a different layout(tsp_fragment_home_screen.xml) file but the java code is written in HomescreenFragment.java only.<br/>
 * On the HomescreenFragment the flow varies from TSP module and Btech/Btech_with_Hub module according to the ROLE.<br/>
 * If the role is 9 (tsp) then tsp_fragment_home_screen.xml is used else (btech & btech_with_hub) fragment_home_screen.xml is used.<br/>
 * There are 3 modules inside tsp : 1)Receive, 2)Send, 3)Earning.<br/>
 * The Earning module is coming soon.<br/>
 * When we click on the receive icon then the page opens which fetches the barcode details from the api.The tsp has to scan those barcodes and the master barcodes and then has to click the receive to submit.<br/>
 * When we click on the send icon then a page with a form opens which fetches the barcode details as well as the the modes of transfer from the api.The tsp has to scan those barcodes, select the mode of transfer and then fill the rest of the form and the click the send to dispatch the result.<br/>
 * Once this is done the tsp is redirected to the homescreen on the successful response.<br/>
 */
public class HomeScreenFragment extends AbstractFragment {

    public static final String TAG_FRAGMENT = "HOME_SCREEN_FRAGMENT";
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private View rootView;
    private TextView txtUserName, txt_no_of_camps;
    private CircularImageView rvSelfie;
    private ImageView imgPayment, imgOrders, imgSchedule, imgMaterials, imgOLCPickup, imgHub, imgCamp, ordersserved, imgLedger;
    private ImageView bellicon, lme_orders_list, lme_mis_icon, lme_material_icon;
    Dialog MainDailog;

    //tsp
    ImageView send_icon, receive_icon, earning_icon, orders_icon;
    private Intent FirebaselocationUpdateIntent;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadSpotlight(final View view) {
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SpotlightSequence.getInstance(activity, null)
                                    .addSpotlight(view.findViewById(R.id.schedule_icon), "Schedule", "Schedule your availability ", "Schedule")
                                    .addSpotlight(view.findViewById(R.id.orders_booked), "Orders", "Orders assigned to you are here ", "orderassigng")
                                    .addSpotlight(view.findViewById(R.id.payment_icon), "Payments", "You can make payments from here", "payments")
                                    .addSpotlight(view.findViewById(R.id.ordersserved), "Served Orders", "Order history is here", "sorders")

                                    .startSequence();
                        }
                    }, 400);
                }
            });


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeScreenActivity) getActivity();
        try {
            activity.toolbarHome.setTitle("Home");
        } catch (Exception e) {
            e.printStackTrace();
        }

        appPreferenceManager = new AppPreferenceManager(activity);
        activity.isOnHome = true;

//        CommonUtils.exportDB(activity);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //tsp

        Logger.error("hhhhhhrole " + appPreferenceManager.getLoginRole());
        Logger.error("hhhhid " + appPreferenceManager.getLoginResponseModel().getUserID());
        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {//loginRole.equalsIgnoreCase("9")
            rootView = inflater.inflate(R.layout.tsp_fragment_home_screen, container, false);
            initUI_TSP();
            initData_Tsp();
            getCampDetailCount();
            initListeners_TSP();
        } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {//loginRole.equalsIgnoreCase("9")
            rootView = inflater.inflate(R.layout.lme_fragment_home_screen, container, false);
            initUI_LME();
            initData_LME();
            initListeners_LME();
        } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {//loginRole.equalsIgnoreCase("11")
            rootView = inflater.inflate(R.layout.fragment_home_screen_nbt, container, false);
            initUI_NBT();
            initData();
            initListeners_NBT();
        } else {//for btech & hub login
            rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
            initUI();
            initData();
            getCampDetailCount();
            initListeners();

            if (!appPreferenceManager.isLoadSpotlightOnHome()) {
                appPreferenceManager.setLoadSpotlightOnHome(true);
                loadSpotlight(rootView);
            }

        }

        setHasOptionsMenu(true);
        CallCheckUserLoginDeviceId();
//Abhishek code to commented while made live
      /*  FirebaselocationUpdateIntent = new Intent(activity, TrackerService.class);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(FirebaselocationUpdateIntent);
        } else {
            activity.startService(FirebaselocationUpdateIntent);
        }*/


        return rootView;
    }

    private void initListeners_LME() {
        lme_orders_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBatchDialog();
            }
        });

        lme_mis_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(LME_WLMISFragment.newInstance(), false, false, LME_WLMISFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });

        lme_material_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(Material_consumptions.newInstance(), false, false, Material_consumptions.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });

    }

    private void CallBatchDialog() {
        final Dialog dialog_batch = new Dialog(activity);
        dialog_batch.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog_batch.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_batch.setContentView(R.layout.dialog_batchonebatchtwo_d);
        dialog_batch.setCanceledOnTouchOutside(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);
        dialog_batch.getWindow().setLayout(width, height);

        ImageView btn_batchone = (ImageView) dialog_batch.findViewById(R.id.btn_batchone);
        ImageView btn_batchtwo = (ImageView) dialog_batch.findViewById(R.id.btn_batchtwo);

        btn_batchone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_batch.dismiss();
                    BundleConstants.batch_code = 1;
                    pushFragments(LME_OrdersDisplayFragment.newInstance(), false, false, LME_OrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_batchtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_batch.dismiss();
                    BundleConstants.batch_code = 2;
                    pushFragments(LME_OrdersDisplayFragment.newInstance(), false, false, LME_OrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        dialog_batch.show();
    }

    private void initData_LME() {
        txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
    }

    private void initUI_LME() {
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        lme_orders_list = (ImageView) rootView.findViewById(R.id.lme_orders_list);
        lme_mis_icon = (ImageView) rootView.findViewById(R.id.lme_mis_icon);
        lme_material_icon = (ImageView) rootView.findViewById(R.id.lme_material_icon);
    }

    private void initData_Tsp() {
        txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
    }

    private void initUI_NBT() {
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        rvSelfie = (CircularImageView) rootView.findViewById(R.id.img_user_picture);
        //  imgPayment = (ImageView) rootView.findViewById(R.id.payment_icon);
        imgOrders = (ImageView) rootView.findViewById(R.id.orders_booked);
        //   imgSchedule = (ImageView) rootView.findViewById(R.id.schedule_icon);
        imgHub = (ImageView) rootView.findViewById(R.id.hub_icon);
        //  imgOLCPickup = (ImageView) rootView.findViewById(R.id.olc_pickup_icon);
        ordersserved = (ImageView) rootView.findViewById(R.id.ordersserved);
        //   imgLedger = (ImageView) rootView.findViewById(R.id.Ledger_icon);
        //   imgCamp = (ImageView) rootView.findViewById(R.id.camp_icon);
        //  imgMaterials = (ImageView) rootView.findViewById(R.id.materials_icon);
        //    imgOLCPickup = (ImageView) rootView.findViewById(R.id.olc_pickup_icon);
        //   txt_no_of_camps = (TextView) rootView.findViewById(R.id.txt_no_of_camps);
    }

    private void initListeners_NBT() {
        ordersserved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Feature coming soon.", Toast.LENGTH_SHORT).show();
                pushFragments(OrderServedFragment.newInstance(), false, false, OrderServedFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logger.error("Role" + appPreferenceManager.getLoginRole());
                pushFragments(HubListDisplayFragment.newInstance(), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);


            }
        });
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

        orders_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pushFragments(TSP_HubMasterBarcodeScanFragment.newInstance(), false, false, TSP_HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                pushFragments(TSP_OrdersDisplayFragment.newInstance(), false, false, TSP_OrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
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
        try {
            rvSelfie = (CircularImageView) rootView.findViewById(R.id.img_user_picture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtUserName = (TextView) rootView.findViewById(R.id.txt_username);
        send_icon = (ImageView) rootView.findViewById(R.id.send_icon);
        receive_icon = (ImageView) rootView.findViewById(R.id.receive_icon);
        earning_icon = (ImageView) rootView.findViewById(R.id.earning_icon);
        orders_icon = (ImageView) rootView.findViewById(R.id.orders_icon);
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
            //changed_by_selfie_2june_2017
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
                Logger.error("Role" + appPreferenceManager.getLoginRole());
                if (appPreferenceManager.getLoginRole().equals("6")) {//loginRole.equalsIgnoreCase("6")
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
                    Logger.error("Role" + appPreferenceManager.getLoginRole());
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
                //  pushFragments(MaterialFragment_new.newInstance(), false, false, MaterialFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
                pushFragments(MaterialFragment.newInstance(), false, false, MaterialFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgOLCPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Feature coming soon..", Toast.LENGTH_SHORT).show();
//                pushFragments(OLCPickupListDisplayFragment.newInstance(), false, false, OLCPickupListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });
        imgCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Feature coming soon..", Toast.LENGTH_SHORT).show();
                //    pushFragments(CampListDisplayFragment.newInstance(), false, false, CampListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
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
            bellicon.setVisibility(View.INVISIBLE);
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
                Logger.error("camp count " + jsonObject);
                //btech_hub
                String btechID = jsonObject.getString("BtechId");
                appPreferenceManager.setBtechID(btechID);

                //tsp
                if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID))
                    if (jsonObject.getString("CampCount").equals("0")) {

                        txt_no_of_camps.setVisibility(View.GONE);
                    } else {
                        txt_no_of_camps.setText("" + jsonObject.getString("CampCount"));
                    }

            } else {
                //tsp
                if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID))
                    Toast.makeText(activity, "Failed to Fetch Camp Count", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, "Failed to Fetch Hub ID", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    public void CallCheckUserLoginDeviceId() {
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

            } else {
                ApiCallAsyncTask logoutDeviceAsyncTask = new AsyncTaskForRequest(activity).getLoginDeviceData(appPreferenceManager.getLoginResponseModel().getUserID());
                logoutDeviceAsyncTask.setApiCallAsyncTaskDelegate(new LogInDeviceListAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    logoutDeviceAsyncTask.execute(logoutDeviceAsyncTask);
                } else {
                    Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
    }

    private class LogInDeviceListAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @SuppressLint("MissingPermission")
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(activity);
                ArrayList<DeviceLoginDetailsModel> materialDetailsModels = new ArrayList<>();

                materialDetailsModels = responseParser.getDeviceDetailsResponseModel(json, statusCode);
                if (materialDetailsModels != null) {
                    if (materialDetailsModels.size() != 0) {
                        String device_id = "";
                        try {
                            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
                            device_id = telephonyManager.getDeviceId();

                            if (!device_id.toString().trim().equalsIgnoreCase("")) {
                                if (!device_id.toString().trim().equalsIgnoreCase(materialDetailsModels.get(0).getDeviceId())) {
                                    CallDeviceNotMatchedDialog();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

    private void CallDeviceNotMatchedDialog() {
        CustomDeviceResetDailog cudd = new CustomDeviceResetDailog(activity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
            @Override
            public void onUpdateClicked() {

            }

            @Override
            public void onOkClicked() {
                try {
                    appPreferenceManager.clearAllPreferences();
                    DhbDao dhbDao = new DhbDao(activity);
                    dhbDao.deleteTablesonLogout();
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(homeIntent);
                    // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

                    Intent n = new Intent(activity, LoginScreenActivity.class);
                    n.setAction(Intent.ACTION_MAIN);
                    n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(n);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cudd.show();
        cudd.setCancelable(false);
    }
}
