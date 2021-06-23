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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.thyrocare.btechapp.Controller.DeviceLogOutController;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.LoginActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.NewCampWOEModuleActivity;
import com.thyrocare.btechapp.NewScreenDesigns.Activities.StockAvailabilityActivityNew;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.BtechCertificateFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.FAQ_Fragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.FeedbackFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Leave_intimation_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.Ledger_module_fragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.TSP_OrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.VisitOrdersDisplayFragment_new;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.LogUserActivityTagging;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.Retrofit.PostAPIInterface;
import com.thyrocare.btechapp.Retrofit.RetroFit_APIClient;
import com.thyrocare.btechapp.dao.DhbDao;
import com.thyrocare.btechapp.fragment.BtechwithHub_HubMasterBarcodeScanFragment;
import com.thyrocare.btechapp.fragment.ChangePasswordFragment;
import com.thyrocare.btechapp.fragment.ClientEntryFragment;
import com.thyrocare.btechapp.fragment.CreditFragment;
import com.thyrocare.btechapp.fragment.HomeScreenFragment;
import com.thyrocare.btechapp.fragment.HubListDisplayFragment;
import com.thyrocare.btechapp.fragment.MaterialFragment;
import com.thyrocare.btechapp.fragment.OrderServedFragment;
import com.thyrocare.btechapp.fragment.QrCodeWoeFragment;
import com.thyrocare.btechapp.fragment.ScheduleYourDayFragment;
import com.thyrocare.btechapp.uiutils.AbstractActivity;
import com.thyrocare.btechapp.utils.api.Logger;
import com.thyrocare.btechapp.utils.app.AppConstants;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.BundleConstants;
import com.thyrocare.btechapp.utils.app.CommonUtils;
import com.thyrocare.btechapp.utils.app.DeviceUtils;
import com.thyrocare.btechapp.utils.app.Global;
import com.thyrocare.btechapp.utils.app.InputUtils;

import java.util.List;

import application.ApplicationController;
import retrofit2.Call;
import retrofit2.Callback;

import static com.thyrocare.btechapp.utils.app.BundleConstants.LOGOUT;

public class HomeScreenActivity extends AbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG_ACTIVITY = "HOME_SCREEN_ACTIVITY";
    private FloatingActionButton fabBtn;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public Toolbar toolbarHome;
    private CircularImageView rivSelfie;
    private TextView txtUserName, txt_version_code;
    private TextView txtUserId;
    private LinearLayout llNavHeader;
    private HomeScreenActivity activity;
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

    Boolean isFromNotification = false;
    int screenCategory;
    public ImageView toolbar_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        activity = this;
        dhbDao = new DhbDao(activity);
        globalclass = new Global(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        //For Cache Clear
        CommonUtils.deleteCache(activity);

        try {
            value = getIntent().getExtras().getString("LEAVEINTIMATION");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value.equals("1")) {

            pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);

        } else {

            initUI();
            toolbar_image.setVisibility(View.GONE);

            if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                toolbarHome.setVisibility(View.VISIBLE);
                toolbar_image.setVisibility(View.VISIBLE);
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
                        toolbar_image.setVisibility(View.VISIBLE);
                        pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                    }
                }
            }


            initData();
            // pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        }
       /* TImeCheckerIntent = new Intent(this, Timecheckservice.class);
        startService(TImeCheckerIntent);*/

        if (isFromPayment) {
            pushFragments(CreditFragment.newInstance(), false, false,
                    CreditFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        }

        if (getIntent().hasExtra("isFromNotification") && getIntent().hasExtra("screenCategory")) {
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
            screenCategory = getIntent().getIntExtra("screenCategory", 0);
            getNotification(navigationView);
        }
    }

    public void setTitle(String title) {
        toolbarHome.setTitle(title);
    }

    private void initData() {
        try {

            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()))
                txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()))
                y = appPreferenceManager.getLoginResponseModel().getUserID();

            if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.LME_ROLE_ID)) {

            } else {
                y = y.substring(y.length() - 4);
            }
//            y = y.substring(y.length() - 4);
            txtUserId.setText(y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // txtUserId.setText(appPreferenceManager.getLoginResponseModel().getUserID());

        if (appPreferenceManager.getSelfieResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getSelfieResponseModel().getPic())) {

            globalclass.DisplayDeviceImages(activity, appPreferenceManager.getSelfieResponseModel().getPic(), rivSelfie);
//            rivSelfie.setImageBitmap(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));

        }
    }

    @Override
    public void initUI() {
        toolbarHome = (Toolbar) findViewById(R.id.toolbar);
        toolbar_image = (ImageView) findViewById(R.id.toolbar_image);

        setSupportActionBar(toolbarHome);
        fabBtn = (FloatingActionButton) findViewById(R.id.fab);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setEnabled(false);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        /**/
        if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID) || appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.NBTTSP_ROLE_ID)) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_leave).setVisible(false);
            Menu nav_Menu1 = navigationView.getMenu();
            nav_Menu1.findItem(R.id.nav_credit).setVisible(false);
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.TSP_ROLE_ID)) {
                Menu nav_Menu2 = navigationView.getMenu();
                nav_Menu2.findItem(R.id.nav_certificates).setVisible(false);
            }
        }


        /**/

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View NavHeaderHomeScreen = LayoutInflater.from(activity).inflate(R.layout.nav_header_home_screen, null);
        llNavHeader = (LinearLayout) NavHeaderHomeScreen.findViewById(R.id.ll_nav_header);
        rivSelfie = (CircularImageView) NavHeaderHomeScreen.findViewById(R.id.img_user_picture);
        txtUserId = (TextView) NavHeaderHomeScreen.findViewById(R.id.txt_user_id);
        txtUserName = (TextView) NavHeaderHomeScreen.findViewById(R.id.txt_user_name);
        txt_version_code = (TextView) findViewById(R.id.txt_version_code);

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

        navigationView.addHeaderView(NavHeaderHomeScreen);
        super.initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //stopService(TImeCheckerIntent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopService(TImeCheckerIntent);
    }

    @Override
    public void onBackPressed() {
        List fragments = getSupportFragmentManager().getFragments();
        mCurrentFragment = fragments.get(fragments.size() - 1);
        Logger.error("mCurrentFragment: " + mCurrentFragment);
        if (mCurrentFragment == null) {
            //stopService(TImeCheckerIntent);
            if (mCurrentFragmentName.equals("LeaveHistoryFragment")) {
                //mCurrentFragmentName="";
                //  pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                // finishAffinity();

            } else {
                finishAffinity();
            }
        } else if (mCurrentFragment.toString().contains("HomeScreenFragment")) {
            //  stopService(TImeCheckerIntent);
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
//            finishAffinity();
        }


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //  stopService(TImeCheckerIntent);
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Logger.error("testdrive");
            if (mCurrentFragmentName.equals("LeaveHistoryFragment")) {
                pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            } else {
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        toolbar_image.setVisibility(View.GONE);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            toolbar_image.setVisibility(View.VISIBLE);
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_leave) {
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                Toast.makeText(activity, "You are not authorize to view this module.", Toast.LENGTH_LONG).show();
            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(Leave_intimation_fragment_new.newInstance(), false, false, Leave_intimation_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }
        } else if (id == R.id.nav_change_password) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(ChangePasswordFragment.newInstance(), false, false, ChangePasswordFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_QrcodeWOE) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(QrCodeWoeFragment.newInstance(), false, false,
                    QrCodeWoeFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_clientEntry) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(ClientEntryFragment.newInstance(), false, false,
                    ClientEntryFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_credit) {
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                Toast.makeText(activity, "You are not authorize to view this module.", Toast.LENGTH_LONG).show();
            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(CreditFragment.newInstance(), false, false,
                        CreditFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }
            //   Toast.makeText(activity, "Feature coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_camp) {
          /*  toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(FeedbackFragment_new.newInstance(), false, false,
                    FeedbackFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);*/

            Intent intentPaymentsActivity = new Intent(HomeScreenActivity.this, NewCampWOEModuleActivity.class);
            startActivity(intentPaymentsActivity);
        } else if (id == R.id.nav_feedback) {

            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(FeedbackFragment_new.newInstance(), false, false,
                    FeedbackFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);

        }else if (id == R.id.nav_pickup) {
            Intent intent = new Intent(HomeScreenActivity.this, OrderPickUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {

            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(FAQ_Fragment.newInstance(), false, false,
                    FAQ_Fragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_logout) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, AppConstants.APP_PERMISSIONS);
            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                if (isNetworkAvailable(activity)) {
                    CallLogoutRequestApi();
                    if (!appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                        CallLogOutDevice();
                    }
                } else {
                    Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.nav_communication) {
            if (appPreferenceManager.getLoginRole().equalsIgnoreCase(AppConstants.LME_ROLE_ID)) {
                Toast.makeText(activity, "You are not authorize to view this module.", Toast.LENGTH_LONG).show();
            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                // Toast.makeText(activity, "Coming Soon..", Toast.LENGTH_SHORT).show();

                showOptionsinAlert();
            }

        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(HomeScreenActivity.this, StockAvailabilityActivityNew.class);
            startActivity(intent);
        } else if (id == R.id.nav_Video) {
            Intent intent = new Intent(HomeScreenActivity.this, ThyrocareVideos.class);
            startActivity(intent);

        } else if (id == R.id.nav_certificates) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(BtechCertificateFragment.newInstance(), false, false, BtechCertificateFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_hcw) {
            Intent intent = new Intent(HomeScreenActivity.this,HCW_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_Hub) {
            //btech_hub
            //for btech with hub login...role will be 6 for this
            Logger.error("Role" + appPreferenceManager.getLoginRole());
            if (appPreferenceManager.getLoginRole().equals("6")) {
                final Dialog MainDailog = new Dialog(this);
                MainDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MainDailog.setContentView(R.layout.btech_dialog_btechwithhub);
                LinearLayout send, receive;
                send = (LinearLayout) MainDailog.findViewById(R.id.send);
                receive = (LinearLayout) MainDailog.findViewById(R.id.receive);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainDailog.dismiss();
                        pushFragments(HubListDisplayFragment.newInstance(1), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                    }
                });

                receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pushFragments(BtechwithHub_HubMasterBarcodeScanFragment.newInstance(), false, false, BtechwithHub_HubMasterBarcodeScanFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                        MainDailog.dismiss();
                    }
                });

                MainDailog.show();
            } else {//for normal btech login...
                Logger.error("Role" + appPreferenceManager.getLoginRole());
                pushFragments(HubListDisplayFragment.newInstance(), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
                        new LogUserActivityTagging(activity, LOGOUT);
                        appPreferenceManager.clearAllPreferences();
                        dhbDao.deleteTablesonLogout();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

                        globalclass.showCustomToast(activity, "Logout successfully");
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
        if (screenCategory == HOME) {
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
        } else if (screenCategory == ORDERS) {
            pushFragments(VisitOrdersDisplayFragment_new.newInstance(), false, false, VisitOrdersDisplayFragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == MATERIALS) {
            pushFragments(MaterialFragment.newInstance(), false, false, MaterialFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == HUB) {
            pushFragments(HubListDisplayFragment.newInstance(), false, false, HubListDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == ORDERSERVED) {
            pushFragments(OrderServedFragment.newInstance(), false, false, OrderServedFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == LEDGER) {
            pushFragments(Ledger_module_fragment_new.newInstance(), false, false, Ledger_module_fragment_new.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (screenCategory == FAQ) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_faq, 0);
        }
    }

}
