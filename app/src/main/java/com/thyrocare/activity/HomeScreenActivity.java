package com.thyrocare.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.thyrocare.Controller.DeviceLogOutController;
import com.thyrocare.R;
import com.thyrocare.application.ApplicationController;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.fragment.CreditFragment;
import com.thyrocare.fragment.FeedbackFragment;
import com.thyrocare.fragment.HomeScreenFragment;
import com.thyrocare.fragment.LeaveIntimationFragment;
import com.thyrocare.fragment.ResetPasswordFragment;
import com.thyrocare.fragment.VisitOrdersDisplayFragment;
import com.thyrocare.models.api.request.Post_DeviceID;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppConstants;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.BundleConstants;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        activity = this;
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        //For Cache Clear
        CommonUtils.deleteCache(activity);

        try {
            value = getIntent().getExtras().getString("LEAVEINTIMATION");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value.equals("1")) {
            pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);


        } else {

            Crashlytics.setUserIdentifier(appPreferenceManager.getLoginResponseModel().getUserID());
            Crashlytics.setUserName(appPreferenceManager.getLoginResponseModel().getUserName());
            initUI();

            if (appPreferenceManager.getLoginResponseModel().getRole().equals(AppConstants.NBTTSP_ROLE_ID)) {
                toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            } else {

                if (BundleConstants.ORDER_Notification) {
                    BundleConstants.ORDER_Notification = false;
                    pushFragments(VisitOrdersDisplayFragment.newInstance(), false, false, VisitOrdersDisplayFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                } else {
                    if (appPreferenceManager.getLeaveFlag() != 0) {
                        pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                        appPreferenceManager.setCameFrom(1);
                        toolbarHome.setVisibility(View.GONE);

                    } else {
                        toolbarHome.setVisibility(View.VISIBLE);
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
    }

    public void setTitle(String title) {
        toolbarHome.setTitle(title);
    }

    private void initData() {
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()))
            txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()))
            y = appPreferenceManager.getLoginResponseModel().getUserID();
        y = y.substring(y.length() - 4);
        txtUserId.setText(y);
        // txtUserId.setText(appPreferenceManager.getLoginResponseModel().getUserID());

        if (appPreferenceManager.getSelfieResponseModel() != null && !InputUtils.isNull(appPreferenceManager.getSelfieResponseModel().getPic())) {

            //changed_for_selfie_2june_2017
            rivSelfie.setImageBitmap(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));
            /*File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
            Uri outPutfileUri = FileProvider.getUriForFile(HomeScreenActivity.this, HomeScreenActivity.this.getApplicationContext().getPackageName() + ".provider", file);
            Bitmap thumbnailToDisplay = null;
            try {
                thumbnailToDisplay = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                ByteArrayOutputStream bytesToDisplay = new ByteArrayOutputStream();
                thumbnailToDisplay.compress(Bitmap.CompressFormat.JPEG, 90, bytesToDisplay);
                Drawable img = new BitmapDrawable(getResources(), thumbnailToDisplay);
                rivSelfie.setImageDrawable(img);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //changed_for_selfie_2june_2017
        }
    }

    @Override
    public void initUI() {
        toolbarHome = (Toolbar) findViewById(R.id.toolbar);
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
            if (AbstractApiModel.SERVER_BASE_API_URL_PROD.equals("http://bts.dxscloud.com/techsoapi")) {
                txt_version_code.setText("Stag Version: " + CommonUtils.getAppVersion(activity));
            } else if (AbstractApiModel.SERVER_BASE_API_URL_PROD.equals("https://www.dxscloud.com/techsoapi")) {
                txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
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
                //  pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
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
                pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
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
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_leave) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_change_password) {
            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(ResetPasswordFragment.newInstance(), false, false, ResetPasswordFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
        } else if (id == R.id.nav_credit) {

            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(CreditFragment.newInstance(), false, false,
                    CreditFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            //   Toast.makeText(activity, "Feature coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {

            toolbarHome.setVisibility(View.VISIBLE);
            pushFragments(FeedbackFragment.newInstance(), false, false,
                    FeedbackFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            //   Toast.makeText(activity, "Feature coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, AppConstants.APP_PERMISSIONS);
            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
                logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
                if (isNetworkAvailable(activity)) {
                    logoutAsyncTask.execute(logoutAsyncTask);
                    CallLogOutDevice();
                } else {
                    Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.nav_communication) {
            toolbarHome.setVisibility(View.VISIBLE);
            // Toast.makeText(activity, "Coming Soon..", Toast.LENGTH_SHORT).show();

            showOptionsinAlert();


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("MissingPermission")
    private void CallLogOutDevice() {
        try {
            if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID())) {
                String device_id = "";
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    device_id = telephonyManager.getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    private void loginChat() {
        String url = "https://finalchat-df79b.firebaseio.com/users.json";
        final ProgressDialog pd = new ProgressDialog(HomeScreenActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("null")) {
                    Toast.makeText(HomeScreenActivity.this, "user not found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(appPreferenceManager.getLoginResponseModel().getUserName())) {
                            Toast.makeText(HomeScreenActivity.this, "user not found", Toast.LENGTH_LONG).show();
                        } else if (obj.getJSONObject(appPreferenceManager.getLoginResponseModel().getUserName()).getString("password").equals(appPreferenceManager.getLoginResponseModel().getUserName())) {
                            UserDetails.username = appPreferenceManager.getLoginResponseModel().getUserName();
                            UserDetails.password = appPreferenceManager.getLoginResponseModel().getUserName();
                            startActivity(new Intent(HomeScreenActivity.this, Users.class).putExtra("comeFrom", "Pre"));
                            //startActivity(new Intent(HomeScreenActivity.this, Users.class));
                        } else {
                            Toast.makeText(HomeScreenActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(HomeScreenActivity.this);
        rQueue.add(request);
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                try {
                    appPreferenceManager.clearAllPreferences();
                    dhbDao.deleteTablesonLogout();
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    // stopService(TImeCheckerIntent);
               /* finish();
                finishAffinity();*/

                    Intent n = new Intent(activity, LoginScreenActivity.class);
                    n.setAction(Intent.ACTION_MAIN);
                    n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(n);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

}
