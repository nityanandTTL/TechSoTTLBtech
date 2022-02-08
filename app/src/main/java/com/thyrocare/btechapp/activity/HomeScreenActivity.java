package com.thyrocare.btechapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.thyrocare.btechapp.Controller.DeviceLogOutController;
import com.thyrocare.btechapp.Controller.SignINOUTController;
import com.thyrocare.btechapp.Controller.SignSummaryController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.BtechCertificateFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.FeedbackFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Leave_intimation_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.ServerdOrderFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.TSP_OrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConnectionDetector;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.Constants;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.GetAPIInterface;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.customview.CircleImageView;
import com.thyrocare.btechapp.customview.CustomDeviceResetDailog;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.delegate.CustomUpdateDialogOkButtonOnClickedDelegate;
import com.thyrocare.btechapp.fragment.BtechwithHub_HubMasterBarcodeScanFragment;
import com.thyrocare.btechapp.fragment.ChangePasswordFragment;
import com.thyrocare.btechapp.fragment.HubListDisplayFragment;
import com.thyrocare.btechapp.fragment.LeadGenerationFragment;
import com.thyrocare.btechapp.fragment.TSP_HubMasterBarcodeScanFragment;
import com.thyrocare.btechapp.fragment.TSP_SendFragment;
import com.thyrocare.btechapp.models.api.request.SignINSummaryRequestModel;
import com.thyrocare.btechapp.models.api.request.SignInRequestModel;
import com.thyrocare.btechapp.models.api.response.SignInResponseModel;
import com.thyrocare.btechapp.models.api.response.SignSummaryResponseModel;
import com.thyrocare.btechapp.models.data.DeviceLoginDetailsModel;
import com.thyrocare.btechapp.service.Timecheckservice;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.NewScreenDesigns.Utils.ConstantsMessages.SomethingWentwrngMsg;
import static com.thyrocare.btechapp.utils.api.NetworkUtils.isNetworkAvailable;
import static com.thyrocare.btechapp.utils.app.AppConstants.ORDERS;
import static com.thyrocare.btechapp.utils.app.AppConstants.PickupOrder;
import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class HomeScreenActivity extends AppCompatActivity {
    public static final String TAG_ACTIVITY = "HOME_SCREEN_ACTIVITY";
    private FloatingActionButton fabBtn;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public Toolbar toolbarHome, nbt_toolbar, tsp_toolbar;
    private CircularImageView rivSelfie;
    private TextView txtUserName, txt_version_code, txt_no_of_camps;
    private LinearLayout llNavHeader;
    private Activity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    private int camefrom = 0;
    private boolean doubleBackToExitPressedOnce = false;
    public boolean isOnHome = false;
    ActionBarDrawerToggle toggle;
    private String value = "";
    private Object mCurrentFragment;
    public static boolean isFromPayment = false;
    private static Intent TImeCheckerIntent;
    String y = "";
    public static String mCurrentFragmentName = "";//jai
    CharSequence[] items;
    Global globalclass;
    String s;
    Boolean isFromNotification = false;
    int screenCategory;
    ConnectionDetector cd;
    int flag_sign = 0;
    CircleImageView civ_profile, civ_tsp_profile, civ_nbt_profile;
    TextView txt_username, txt_nbt_username;
    public AppBarLayout appbarLayout;
    ImageView iv_gqc;
    //for B-Tech
    LinearLayout ll_schedule, ll_orders, ll_served, ll_hub, ll_lead, ll_pick_orders, ll_leaves, ll_hcw, ll_password, ll_feedback, ll_video, ll_certificate, ll_logout, ll_gqc;
    private Dialog MainDailog;
    //for TSP
    LinearLayout ll_tsp_certificate, ll_tsp_logout, ll_tsp_hub, ll_tsp_orders, ll_tsp_served, ll_tsp_earning, ll_tsp_send, ll_tsp_receive, ll_tsp_lead, ll_tsp_pick_orders, ll_tsp_hcw, ll_tsp_password, ll_tsp_feedback, ll_tsp_video, ll_tsp_leaves;
    //    public ImageView toolbar_image;
    ImageView iv_tsp_gqc, iv_nbt_gqc;

    LinearLayout ll_nbt_served, ll_nbt_certificate, ll_nbt_logout, ll_nbt_hub, ll_nbt_orders, ll_nbt_leaves, ll_nbt_send, ll_nbt_receive, ll_nbt_lead, ll_nbt_pick_orders, ll_nbt_hcw, ll_nbt_password, ll_nbt_feedback, ll_nbt_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        appPreferenceManager = new AppPreferenceManager(this);
        dhbDao = new DhbDao(activity);
        globalclass = new Global(activity);
        cd = new ConnectionDetector(activity);
        //For Cache Clear
        CommonUtils.deleteCache(activity);
        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {//loginRole.equalsIgnoreCase("9")
            setContentView(R.layout.tsp_home_fragment_new);
            initUI_TSP();
            initData_Tsp();
//            getCampDetailCount();
            initListeners_TSP();
        } else if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {//loginRole.equalsIgnoreCase("11")
            setContentView(R.layout.fragment_home_screen_nbt);
            initUI_NBT();
//            initData();
            iniTNBT();
            initListeners_NBT();
        } else {//for btech & hub login
            setContentView(R.layout.activity_home_screen);
            initUI();
            initData();
            initListeners();
        }
//        getCampDetailCount();

        CallCheckUserLoginDeviceId();

       /* try {
            value = getIntent().getExtras().getString("LEAVEINTIMATION");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*if (value.equals("1")) {

            pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);

        } else {*/

//            initUI();
//            toolbar_image.setVisibility(View.GONE);
            /*if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                toolbarHome.setVisibility(View.VISIBLE);
//                toolbar_image.setVisibility(View.VISIBLE);
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            } else {

                if (BundleConstants.ORDER_Notification) {
                    BundleConstants.ORDER_Notification = false;
                    if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.TSP_ROLE_ID)) {
                        pushFragments(TSP_OrdersDisplayFragment_new.newInstance(), false, false, TSP_OrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                    } else if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.BTECH_ROLE_ID)) {
                        pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                    }
                } else {
                    if (appPreferenceManager.getLeaveFlag() != 0) {
                        pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                        appPreferenceManager.setCameFrom(1);
                        toolbarHome.setVisibility(View.GONE);

                    } else {
                        toolbarHome.setVisibility(View.VISIBLE);
//                        toolbar_image.setVisibility(View.VISIBLE);
                        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                    }
                }
            }*/
        // pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
//        }
//        CallBTechSignINOUTSummaryAPI();

        TImeCheckerIntent = new Intent(this, Timecheckservice.class);
        startService(TImeCheckerIntent);

        /*if (isFromPayment) {
            pushFragments(CreditFragment.newInstance(), false, false,
                    CreditFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        }*/

        if (getIntent().hasExtra("isFromNotification") && getIntent().hasExtra("screenCategory")) {
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
            screenCategory = getIntent().getIntExtra("screenCategory", 0);
            getNotification(navigationView);
        }

    }

    private void iniTNBT() {
        txt_nbt_username = (TextView) findViewById(R.id.txt_nbt_username);
        globalclass.DisplayDeviceImages(activity, appPreferenceManager.getSelfieResponseModel().getPic(), civ_nbt_profile);
        txt_nbt_username.setText("Welcome ,\n" + appPreferenceManager.getLoginResponseModel().getUserName());

        try {
            if (EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD)).equals(EncryptionUtils.Dcrp_Hex(getString(R.string.BASE_URL_TOCHECK)))) {
                txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
            } else {
                txt_version_code.setText("Stag Version: " + CommonUtils.getAppVersion(activity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
        }


    }

    private void initListeners_NBT() {
        iv_nbt_gqc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.QrClick = true;
                Intent gqc = new Intent(activity, KIOSK_Scanner_Activity.class);
                startActivity(gqc);
            }
        });

        ll_nbt_served.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ServerdOrderFragment.class));
            }
        });


        ll_nbt_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LeadGenerationFragment.class);
                startActivity(intent);
            }
        });
        ll_nbt_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VisitOrdersDisplayFragment_new.class);
                startActivity(intent);
            }
        });

        ll_nbt_leaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Leave_intimation_fragment_new.class);
                intent.putExtra(BundleConstants.isNBT, true);
                startActivity(intent);
                startActivity(new Intent(activity, Leave_intimation_fragment_new.class));
            }
        });


        ll_nbt_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BtechCertificateFragment.class);
                startActivity(intent);
            }
        });

        ll_nbt_hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btech_hub
                //for btech with hub login...role will be 6 for this
                Logger.error("Role" + appPreferenceManager.getLoginRole());
                if (appPreferenceManager.getLoginRole().equals("6")) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
                    final View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.btech_dialog_btechwithhub, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
                    LinearLayout receive = bottomSheet.findViewById(R.id.receive);
                    LinearLayout send = bottomSheet.findViewById(R.id.send);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, HubListDisplayFragment.class);
                            startActivity(intent);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, BtechwithHub_HubMasterBarcodeScanFragment.class));
                            bottomSheetDialog.dismiss();

                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheet);
                    bottomSheetDialog.setCancelable(false);
                    bottomSheetDialog.show();

                } else {//for normal btech login...
                    Logger.error("Role" + appPreferenceManager.getLoginRole());
                    Intent intent = new Intent(activity, HubListDisplayFragment.class);
                    startActivity(intent);
                }

            }
        });

        ll_nbt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        ll_nbt_pick_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderPickUpActivity.class);
                startActivity(intent);
            }
        });


        ll_nbt_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChangePasswordFragment.class);
                startActivity(intent);
            }
        });


        ll_nbt_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ThyrocareVideos.class);
                startActivity(intent);
            }
        });

        ll_nbt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FeedbackFragment_new.class);
                startActivity(intent);
            }
        });


        ll_nbt_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LeadGenerationFragment.class);
                startActivity(intent);
            }
        });

        ll_nbt_hcw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, HCW_Activity.class));
            }
        });
    }

    private void initUI_NBT() {

        iv_nbt_gqc = findViewById(R.id.iv_nbt_gqc);
        ll_nbt_served = findViewById(R.id.ll_nbt_served);
        ll_nbt_logout = findViewById(R.id.ll_nbt_logout);
        ll_nbt_hub = findViewById(R.id.ll_nbt_hub);
        ll_nbt_orders = findViewById(R.id.ll_nbt_orders);
        ll_nbt_leaves = findViewById(R.id.ll_nbt_leaves);
        ll_nbt_lead = findViewById(R.id.ll_nbt_lead);
        ll_nbt_pick_orders = findViewById(R.id.ll_nbt_pick_orders);
        ll_nbt_hcw = findViewById(R.id.ll_nbt_hcw);
        ll_nbt_password = findViewById(R.id.ll_nbt_password);
        ll_nbt_feedback = findViewById(R.id.ll_nbt_feedback);
        ll_nbt_video = findViewById(R.id.ll_nbt_video);
        ll_nbt_certificate = findViewById(R.id.ll_nbt_certificate);
        civ_nbt_profile = findViewById(R.id.civ_nbt_profile);
        txt_nbt_username = findViewById(R.id.txt_nbt_username);
        nbt_toolbar = findViewById(R.id.nbt_toolbar);
        txt_version_code = findViewById(R.id.txt_version_code);
        nbt_toolbar.setTitle("");
    }

    private void initListeners_TSP() {


        ll_tsp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, TSP_SendFragment.class);
                startActivity(intent);
            }
        });
        iv_tsp_gqc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.QrClick = true;
                Intent gqc = new Intent(activity, KIOSK_Scanner_Activity.class);
                startActivity(gqc);
            }
        });

        ll_tsp_served.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ServerdOrderFragment.class));
            }
        });


        ll_tsp_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LeadGenerationFragment.class);
                startActivity(intent);
            }
        });
        ll_tsp_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TSP_OrdersDisplayFragment_new.class);
                startActivity(intent);
            }
        });

        ll_tsp_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BtechCertificateFragment.class);
                startActivity(intent);
            }
        });

        ll_tsp_hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btech_hub
                //for btech with hub login...role will be 6 for this
                Logger.error("Role" + appPreferenceManager.getLoginRole());
                if (appPreferenceManager.getLoginRole().equals("6")) {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
                    final View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.btech_dialog_btechwithhub, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
                    LinearLayout receive = bottomSheet.findViewById(R.id.receive);
                    LinearLayout send = bottomSheet.findViewById(R.id.send);
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, HubListDisplayFragment.class);
                            startActivity(intent);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, BtechwithHub_HubMasterBarcodeScanFragment.class));
                            bottomSheetDialog.dismiss();

                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheet);
                    bottomSheetDialog.setCancelable(false);
                    bottomSheetDialog.show();

                } else {//for normal btech login...
                    Logger.error("Role" + appPreferenceManager.getLoginRole());
                    Intent intent = new Intent(activity, HubListDisplayFragment.class);
                    startActivity(intent);
                }

            }
        });

        ll_tsp_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        ll_tsp_pick_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderPickUpActivity.class);
                startActivity(intent);
            }
        });


        ll_tsp_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChangePasswordFragment.class);
                startActivity(intent);
            }
        });

        ll_tsp_leaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Leave_intimation_fragment_new.class);
                startActivity(intent);
            }
        });

        ll_tsp_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ThyrocareVideos.class);
                startActivity(intent);
            }
        });

        ll_tsp_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FeedbackFragment_new.class);
                startActivity(intent);
            }
        });
        ll_tsp_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, TSP_HubMasterBarcodeScanFragment.class));
            }
        });


        ll_tsp_hcw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HCW_Activity.class);
                startActivity(intent);
            }
        });

        ll_tsp_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData_Tsp() {
        txt_username = (TextView) findViewById(R.id.tv_tsp_username);
//        globalclass.DisplayDeviceImages(activity, appPreferenceManager.getSelfieResponseModel().getPic(), civ_tsp_profile);
        txt_username.setText("Welcome ,\n" + Global.toCamelCase(appPreferenceManager.getLoginResponseModel().getUserName()));

        try {
            if (EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD)).equals(EncryptionUtils.Dcrp_Hex(getString(R.string.BASE_URL_TOCHECK)))) {
                txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
            } else {
                txt_version_code.setText("Stag Version: " + CommonUtils.getAppVersion(activity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
        }
    }

    private void CallBTechSignINOUTSummaryAPI() {
        if (cd.isConnectingToInternet()) {
            SignINSummaryRequestModel requestModel = new SignINSummaryRequestModel();
            requestModel.setType(Constants.GETIGNINOUT);
            requestModel.setBtechid(appPreferenceManager.getLoginResponseModel().getUserID());
            SignSummaryController inoutController = new SignSummaryController(this);
            inoutController.signINOUTSummary(requestModel);

        } else {
            Global.showCustomStaticToast(activity, ConstantsMessages.CHECK_INTERNET_CONN);
        }

    }

    private void initData() {
        try {

            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()))
                txtUserName.setText("Welcome ,\n" + Global.toCamelCase(appPreferenceManager.getLoginResponseModel().getUserName()));
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()))
                y = appPreferenceManager.getLoginResponseModel().getUserID();

            try {
                if (EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD)).equals(EncryptionUtils.Dcrp_Hex(getString(R.string.BASE_URL_TOCHECK)))) {
                    txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
                } else {
                    txt_version_code.setText("Stag Version: " + CommonUtils.getAppVersion(activity));
                }
            } catch (Exception e) {
                e.printStackTrace();
                txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUI() {
        View v_1 = findViewById(R.id.v_1);
        View v_2 = findViewById(R.id.v_2);
        View v_3 = findViewById(R.id.v_3);
        iv_gqc = findViewById(R.id.iv_gqc);
        ll_schedule = findViewById(R.id.ll_schedule);
        ll_orders = findViewById(R.id.ll_orders);
        ll_served = findViewById(R.id.ll_served);
        ll_leaves = findViewById(R.id.ll_leaves);
        ll_hub = findViewById(R.id.ll_hub);
        ll_lead = findViewById(R.id.ll_lead);
        ll_pick_orders = findViewById(R.id.ll_pick_orders);
        ll_hcw = findViewById(R.id.ll_hcw);
        ll_password = findViewById(R.id.ll_password);
        ll_feedback = findViewById(R.id.ll_feedback);
        ll_video = findViewById(R.id.ll_video);
        ll_certificate = findViewById(R.id.ll_certificate);
        ll_logout = findViewById(R.id.ll_logout);
        txt_version_code = findViewById(R.id.txt_version_code);
        ll_gqc = findViewById(R.id.ll_gqc);


        //Todo 3 - Pharmeasy 2 for Thyrocare
        if (Global.checkLogin(appPreferenceManager.getLoginResponseModel().getCompanyName())) {
            ll_leaves.setVisibility(View.GONE);
            ll_pick_orders.setVisibility(View.GONE);
            ll_gqc.setVisibility(View.GONE);
            ll_lead.setVisibility(View.GONE);
            v_1.setVisibility(View.GONE);
            v_2.setVisibility(View.GONE);
            v_3.setVisibility(View.GONE);
        } else {
            ll_gqc.setVisibility(View.VISIBLE);
            ll_lead.setVisibility(View.VISIBLE);
            ll_leaves.setVisibility(View.VISIBLE);
            ll_pick_orders.setVisibility(View.VISIBLE);
            v_1.setVisibility(View.VISIBLE);
            v_2.setVisibility(View.VISIBLE);
            v_3.setVisibility(View.VISIBLE);
        }
        txt_no_of_camps = (TextView) findViewById(R.id.txt_no_of_camps);

        toolbarHome = (Toolbar) findViewById(R.id.toolbar);
        civ_profile = (CircleImageView) findViewById(R.id.civ_profile);
//        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);
        txtUserName = (TextView) findViewById(R.id.txt_username);
        toolbarHome.setTitle("");


        globalclass.DisplayDeviceImages(activity, appPreferenceManager.getSelfieResponseModel().getPic(), civ_profile);
        setSupportActionBar(toolbarHome);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        appbarLayout.setVisibility(View.VISIBLE);

        /**/
        /*if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID) || appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_leave).setVisible(false);
            Menu nav_Menu1 = navigationView.getMenu();
            nav_Menu1.findItem(R.id.nav_credit).setVisible(false);
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
                Menu nav_Menu2 = navigationView.getMenu();
                nav_Menu2.findItem(R.id.nav_certificates).setVisible(false);
            }
        }*/

        /*if (appPreferenceManager.getLoginResponseModel().getRole().equalsIgnoreCase(AppConstants.NBT_ROLE_ID) || appPreferenceManager.getLoginResponseModel().getRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
            Menu nav_menu = navigationView.getMenu();
            nav_menu.findItem(R.id.nav_gqc).setVisible(true);
        }*/

        try {
            if (EncryptionUtils.Dcrp_Hex(getString(R.string.SERVER_BASE_API_URL_PROD)).equals(EncryptionUtils.Dcrp_Hex(getString(R.string.BASE_URL_TOCHECK)))) {
                txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
            } else {
                txt_version_code.setText("Stag Version: " + CommonUtils.getAppVersion(activity));
            }
        } catch (Exception e) {
            e.printStackTrace();
            txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        stopService(TImeCheckerIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopService(TImeCheckerIntent);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Close Application")
                    .setMessage("Are you sure you want to close the application")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
//                            stopService(TImeCheckerIntent);
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    public void CallLogoutRequestApi() {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(PostAPIInterface.class);
        Call<String> responseCall = apiInterface.CallLogoutRequestApi();
        responseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.code() == 200) {
                    try {
                        new LogUserActivityTagging(activity, LOGOUT, "");
                        appPreferenceManager.clearAllPreferences();
                        dhbDao.deleteTablesonLogout();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

//                        Toast.makeText(activity, "Logout successfully", Toast.LENGTH_SHORT).show();
//                        globalclass.showCustomToast(activity, "Logout successfully");
                        Intent n = new Intent(activity, LoginActivity.class);
                        n.setAction(Intent.ACTION_MAIN);
                        n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(n);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    CommonUtils.CallLogOutFromDevice(activity, (Activity) activity, appPreferenceManager, dhbDao);
                } else {
                    Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MessageLogger.LogDebug("Errror", t.getMessage());

            }
        });
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @SuppressLint("MissingPermission")
    private void CallLogOutDevice() {
        try {
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                String device_id = "";

                device_id = DeviceUtils.getDeviceId(activity);

                if (ApplicationController.mDeviceLogOutController != null) {
                    ApplicationController.mDeviceLogOutController = null;
                }

                ApplicationController.mDeviceLogOutController = new DeviceLogOutController(activity);
                ApplicationController.mDeviceLogOutController.CallLogOutDevice(appPreferenceManager.getLoginResponseModel().getUserID(), device_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOptionsinAlert() {
        items = new String[]{"Chat", "WhatsApp", "Training"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Chat")) {
                    //  loginChat();//anand sir requirement
                    Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("WhatsApp")) {
                    onClickWhatsApp();
                } else {
                    Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    public void onClickWhatsApp() {

        PackageManager pm = getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void getNotification(NavigationView navigationView) {
        /*if (screenCategory == HOME) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_home, 0);
        } else if (screenCategory == LEAVES) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_leave, 0);
        } else if (screenCategory == CHANGEPASSWORD) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_change_password, 0);
        } else if (screenCategory == CLIENTENTRY) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_clientEntry, 0);
        } else if (screenCategory == CREDIT) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_credit, 0);
        } else if (screenCategory == AppConstants.STOCKAVAILABILITY) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_stock, 0);
        } else if (screenCategory == COMMUNICATE) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_communication, 0);
        } else if (screenCategory == VIDEOS) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_Video, 0);
        } else if (screenCategory == FEEDBACK) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_feedback, 0);
        } else if (screenCategory == SCHEDULE) {
            pushFragments(ScheduleYourDayFragment.newInstance(), false, false, ScheduleYourDayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else*/ if (screenCategory == ORDERS) {
//            pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);

            Intent intent = new Intent(activity, VisitOrdersDisplayFragment_new.class);
            startActivity(intent);

        } /*else if (screenCategory == MATERIALS) {
            pushFragments(MaterialFragment.newInstance(), false, false, MaterialFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == HUB) {
            pushFragments(HubListDisplayFragment.newInstance(), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == ORDERSERVED) {
            pushFragments(OrderServedFragment.newInstance(), false, false, OrderServedFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == LEDGER) {
            pushFragments(Ledger_module_fragment_new.newInstance(), false, false, Ledger_module_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == FAQ) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_faq, 0);
        } else if (screenCategory == PickupOrder) {
            Intent n = new Intent(this, OrderPickUpActivity.class);
            n.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(n);
        }*/
    }

    public void getSubmitDataResponse(SignSummaryResponseModel responseModel) {
        if (responseModel != null) {
            if (!InputUtils.isNull(responseModel.getSigninTime()) && InputUtils.isNull(responseModel.getSignoutTime()) && InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), ConstantsMessages.RES0000)) {
                flag_sign = 1;
//                globalclass.showCustomToast(activity, "" + responseModel.getResponse(), Toast.LENGTH_SHORT);
            } else if (InputUtils.isNull(responseModel.getSigninTime()) && (InputUtils.isNull(responseModel.getSignoutTime()))) {
//                tv_punch.setText("Sign IN/OUT");
                flag_sign = 0;
            } else if (!InputUtils.isNull(responseModel.getSigninTime()) && (!InputUtils.isNull(responseModel.getSignoutTime()))) {
//                tv_punch.setVisibility(View.GONE);
                flag_sign = 2;
            }
        }
    }

    private void callSignINOUTAPI() {
        if (flag_sign == 0) {
            s = Constants.SIGNIN;
            callAPI(s);
        }
        if (flag_sign == 1) {
            s = Constants.SIGNOUT;
            callAPI(s);
        }
    }

    private void callAPI(String s) {
        if (cd.isConnectingToInternet()) {
            SignInRequestModel requestModel = new SignInRequestModel();
            if (InputUtils.CheckEqualIgnoreCase(s, Constants.SIGNIN)) {
                requestModel.setType(s);
                requestModel.setBtechid(appPreferenceManager.getLoginResponseModel().getUserID());
                requestModel.setLatitude(CommonUtils.getCurrentLatLong(activity).getmLatitude());
                requestModel.setLongitude(CommonUtils.getCurrentLatLong(activity).getmLongitude());
                requestModel.setSignInRemarks("");
                requestModel.setSignOutRemarks("");
                requestModel.setApikey("");
                requestModel.setOutlongitude("");
                requestModel.setOutLatitude("");
            } else if (InputUtils.CheckEqualIgnoreCase(s, Constants.SIGNOUT)) {
                requestModel.setType(s);
                requestModel.setLatitude("");
                requestModel.setLongitude("");
                requestModel.setBtechid(appPreferenceManager.getLoginResponseModel().getUserID());
                requestModel.setOutLatitude(CommonUtils.getCurrentLatLong(activity).getmLatitude());
                requestModel.setOutlongitude(CommonUtils.getCurrentLatLong(activity).getmLongitude());
                requestModel.setSignInRemarks("");
                requestModel.setSignOutRemarks("");
                requestModel.setApikey("");
            }
            SignINOUTController signINOUTController = new SignINOUTController(this);
            signINOUTController.signINOUT(requestModel);
        } else {
            Global.showCustomStaticToast(activity, ConstantsMessages.CHECK_INTERNET_CONN);
        }
    }

    public void getSignINdata(SignInResponseModel responseModel) {
        if (responseModel != null && InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), ConstantsMessages.RES0000)) {
            globalclass.showCustomToast(activity, "" + responseModel.getResponse(), Toast.LENGTH_SHORT);
            CallBTechSignINOUTSummaryAPI();
        } else if (responseModel != null && InputUtils.CheckEqualIgnoreCase(responseModel.getResponseId(), Constants.RES0001)) {
            globalclass.showCustomToast(activity, "" + responseModel.getResponse(), Toast.LENGTH_SHORT);
            CallBTechSignINOUTSummaryAPI();
        } else {
            globalclass.showCustomToast(activity, ConstantsMessages.SOMETHING_WENT_WRONG);
        }
    }


    private void initUI_TSP() {

        iv_tsp_gqc = findViewById(R.id.iv_tsp_gqc);
        ll_tsp_hub = findViewById(R.id.ll_tsp_hub);
        ll_tsp_orders = findViewById(R.id.ll_tsp_orders);
        ll_tsp_served = findViewById(R.id.ll_tsp_served);
        ll_tsp_earning = findViewById(R.id.ll_tsp_earning);
        ll_tsp_send = findViewById(R.id.ll_tsp_send);
        ll_tsp_receive = findViewById(R.id.ll_tsp_receive);
        ll_tsp_lead = findViewById(R.id.ll_tsp_lead);
        ll_tsp_pick_orders = findViewById(R.id.ll_tsp_pick_orders);
        ll_tsp_hcw = findViewById(R.id.ll_tsp_hcw);
        ll_tsp_password = findViewById(R.id.ll_tsp_password);
        ll_tsp_feedback = findViewById(R.id.ll_tsp_feedback);
        ll_tsp_video = findViewById(R.id.ll_tsp_video);
        ll_tsp_logout = findViewById(R.id.ll_tsp_logout);
        ll_tsp_certificate = findViewById(R.id.ll_tsp_certificate);
        ll_tsp_leaves = findViewById(R.id.ll_tsp_leaves);
        civ_tsp_profile = findViewById(R.id.civ_tsp_profile);
        txt_username = (TextView) findViewById(R.id.tv_tsp_username);
        tsp_toolbar = findViewById(R.id.tsp_toolbar);
        tsp_toolbar.setTitle("");

        txt_version_code = findViewById(R.id.txt_version_code);

    }


    private void getCampDetailCount() {
        if (isNetworkAvailable(activity)) {
            CallGetCampDetailsCountAPI();
        } else {
            Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
            initData();
        }
    }

    private void CallGetCampDetailsCountAPI() {
        try {
            GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
            Call<String> responseCall = apiInterface.CallGetCampDetailsCountAPI(appPreferenceManager.getLoginResponseModel().getUserID());
            globalclass.showProgressDialog(activity, "Please wait..");
            responseCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    globalclass.hideProgressDialog(activity);


                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Logger.error("camp count " + jsonObject);
                            //btech_hub
                            String btechID = jsonObject.getString("BtechId");
                            appPreferenceManager.setBtechID(btechID);
                            //tsp
                            if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID))
                                if (jsonObject.getString("CampCount").equals("0")) {
//                                    txt_no_of_camps.setVisibility(View.GONE);
                                } else {
//                                    txt_no_of_camps.setText("" + jsonObject.getString("CampCount"));
                                }

                        } else {
                            //tsp
                            if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID))
                                Toast.makeText(activity, "Failed to Fetch Camp Count", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(activity, "Failed to Fetch Hub ID", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    globalclass.hideProgressDialog(activity);
                    globalclass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {

        iv_gqc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.QrClick = true;
                Intent gqc = new Intent(activity, KIOSK_Scanner_Activity.class);
                startActivity(gqc);
            }
        });

        ll_served.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ServerdOrderFragment.class));
            }
        });


        ll_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LeadGenerationFragment.class);
                startActivity(intent);
            }
        });
        ll_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VisitOrdersDisplayFragment_new.class);
                startActivity(intent);

//                pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }
        });

        ll_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BtechCertificateFragment.class);
                startActivity(intent);
            }
        });

        ll_hub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btech_hub
                //for btech with hub login...role will be 6 for this
                Logger.error("Role" + appPreferenceManager.getLoginRole());
                if (appPreferenceManager.getLoginRole().equals("6")) {
                    MainDailog = new Dialog(activity);
                    MainDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    MainDailog.setContentView(R.layout.btech_dialog_btechwithhub);
                    LinearLayout send, receive;
                    send = (LinearLayout) MainDailog.findViewById(R.id.send);
                    receive = (LinearLayout) MainDailog.findViewById(R.id.receive);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainDailog.dismiss();
                            Intent intent = new Intent(activity, HubListDisplayFragment.class);
                            startActivity(intent);
                        }
                    });

                    receive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, BtechwithHub_HubMasterBarcodeScanFragment.class));
                            MainDailog.dismiss();
                        }
                    });

                    MainDailog.show();
                } else {//for normal btech login...
                    Logger.error("Role" + appPreferenceManager.getLoginRole());
                    Intent intent = new Intent(activity, HubListDisplayFragment.class);
                    startActivity(intent);
                }

            }
        });

        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        ll_pick_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderPickUpActivity.class);
                startActivity(intent);
            }
        });

        ll_leaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pushFragments(ScheduleYourDayFragment.newInstance(), false, false, ScheduleYourDayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                Intent intent = new Intent(activity, Leave_intimation_fragment_new.class);
                intent.putExtra(BundleConstants.isNBT, false);
                startActivity(intent);
            }
        });

        ll_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChangePasswordFragment.class);
                startActivity(intent);
            }
        });


        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ThyrocareVideos.class);
                startActivity(intent);
            }
        });

        ll_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FeedbackFragment_new.class);
                startActivity(intent);
            }
        });

       /* ll_served.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Feature coming soon.", Toast.LENGTH_SHORT).show();
                pushFragments(ServerdOrderFragment.newInstance(), false, false, ServerdOrderFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_FRAGMENT);
            }
        });*/

        ll_hcw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HCW_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void logOut() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, AppConstants.APP_PERMISSIONS);
        } else {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);

            View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.logout_bottomsheet, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
            TextView tv_text = bottomSheet.findViewById(R.id.tv_text);
            tv_text.setText("Are you sure you want to LOGOUT?");
            Button btn_yes = bottomSheet.findViewById(R.id.btn_yes);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable(activity)) {
                        CallLogoutRequestApi();
                        if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                            CallLogOutDevice();
                        }
                    } else {
                        Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button btn_no = bottomSheet.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                }
            });

            bottomSheetDialog.setContentView(bottomSheet);
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.show();

        }
    }

    public void CallCheckUserLoginDeviceId() {
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
            if (InputUtils.CheckEqualIgnoreCase(appPreferenceManager.getLoginResponseModel().getCompanyName(), Global.PE_BTech)) {

            } else {
                if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {

                } else {
                    if (isNetworkAvailable(activity)) {
                        CallgetLoginDeviceDataApi();
                    } else {
                        Toast.makeText(activity, R.string.internet_connetion_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void CallgetLoginDeviceDataApi() {
        GetAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD))).create(GetAPIInterface.class);
        Call<ArrayList<DeviceLoginDetailsModel>> responseCall = apiInterface.CallgetLoginDeviceDataApi(appPreferenceManager.getLoginResponseModel().getUserID());
        responseCall.enqueue(new Callback<ArrayList<DeviceLoginDetailsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DeviceLoginDetailsModel>> call, retrofit2.Response<ArrayList<DeviceLoginDetailsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<DeviceLoginDetailsModel> materialDetailsModels = response.body();
                    if (materialDetailsModels != null) {
                        if (materialDetailsModels.size() != 0) {
                            String device_id = "";
                            try {
                                device_id = DeviceUtils.getDeviceId(activity);

                                if (!device_id.toString().trim().equalsIgnoreCase("")) {
                                    if (!device_id.toString().trim().equalsIgnoreCase(materialDetailsModels.get(0).getDeviceId())) {
                                        CallDeviceNotMatchedDialog();
                                    } else {
                                        CheckSTechLoginandShowPopUp(materialDetailsModels.get(0));
                                    }
                                } else {
                                    CheckSTechLoginandShowPopUp(materialDetailsModels.get(0));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    globalclass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DeviceLoginDetailsModel>> call, Throwable t) {
                globalclass.showcenterCustomToast(activity, SomethingWentwrngMsg, Toast.LENGTH_LONG);
            }
        });
    }

    private void CallDeviceNotMatchedDialog() {


        CustomDeviceResetDailog cudd = new CustomDeviceResetDailog(activity, new CustomUpdateDialogOkButtonOnClickedDelegate() {
            @Override
            public void onUpdateClicked() {

            }

            @Override
            public void onOkClicked() {
                try {
                    new LogUserActivityTagging(activity, LOGOUT, "");
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

                    Intent n = new Intent(activity, LoginActivity.class);
                    n.setAction(Intent.ACTION_MAIN);
                    n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(n);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (!activity.isFinishing()) {
            cudd.setCancelable(false);
            cudd.show();
        }


    }

    private void CheckSTechLoginandShowPopUp(DeviceLoginDetailsModel deviceLoginDetailsModel) {
        if (BundleConstants.setStechDialogFlag == 0) {
            if (deviceLoginDetailsModel != null) {
                if (deviceLoginDetailsModel.getIsStech() == 1) {
                    ShowPopUpDialog(deviceLoginDetailsModel.getUrl());
                }
            }
        }
    }

    private void ShowPopUpDialog(String imageUrl) {
        if (imageUrl != null) {
            if (!imageUrl.toString().trim().equalsIgnoreCase("")) {
                BundleConstants.setStechDialogFlag = 1;
                ShowImageDialogFromUrl(imageUrl.toString().trim());
            }
        }
    }

    private void ShowImageDialogFromUrl(String imageurl) {
        try {

            final Dialog openDialog = new Dialog(activity);
            openDialog.setContentView(R.layout.imagepopup_dialog);
            openDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.99);
            int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.99);
            openDialog.getWindow().setLayout(width, height);
            openDialog.setTitle("");

            ImageView img_cancel = (ImageView) openDialog.findViewById(R.id.img_cancel);
            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog.dismiss();
                }
            });

            ImageView image_view = (ImageView) openDialog.findViewById(R.id.image_view);
            Glide.with(activity)
                    .load(imageurl)
                    .into(image_view);

            openDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}