package com.thyrocare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.thyrocare.R;
import com.thyrocare.dao.DhbDao;
import com.thyrocare.fragment.CreditFragment;
import com.thyrocare.fragment.HomeScreenFragment;
import com.thyrocare.fragment.LeaveIntimationFragment;
import com.thyrocare.fragment.ResetPasswordFragment;
import com.thyrocare.network.ApiCallAsyncTask;
import com.thyrocare.network.ApiCallAsyncTaskDelegate;
import com.thyrocare.network.AsyncTaskForRequest;
import com.thyrocare.uiutils.AbstractActivity;
import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;
import com.thyrocare.utils.app.CommonUtils;
import com.thyrocare.utils.app.InputUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;

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
    private String value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        activity = this;
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
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
            if (appPreferenceManager.getLeaveFlag() != 0) {
                pushFragments(LeaveIntimationFragment.newInstance(), false, false, LeaveIntimationFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
                appPreferenceManager.setCameFrom(1);
                toolbarHome.setVisibility(View.GONE);

            } else {
                toolbarHome.setVisibility(View.VISIBLE);
                pushFragments(HomeScreenFragment.newInstance(), false, false, HomeScreenFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            }
            initData();
            // pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        }
    }

    public void setTitle(String title) {
        toolbarHome.setTitle(title);
    }

    private void initData() {
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()))
            txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
        if (!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()))
            txtUserId.setText(appPreferenceManager.getLoginResponseModel().getUserID());
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
        if (appPreferenceManager.getLoginRole().equalsIgnoreCase("9")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_leave).setVisible(false);
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
        txt_version_code.setText("Version: " + CommonUtils.getAppVersion(activity));
        navigationView.addHeaderView(NavHeaderHomeScreen);
        super.initUI();
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isOnHome) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
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
            pushFragments(CreditFragment.newInstance(), false, false, CreditFragment.TAG_FRAGMENT, R.id.fl_homeScreen, TAG_ACTIVITY);
            //   Toast.makeText(activity, "Feature coming soon...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            toolbarHome.setVisibility(View.VISIBLE);


            /*//delete the image from storage_change_2june_2017
            File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
            boolean deleted = file.delete();
            //delete the image from storage_change_2june_2017*/

            ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
            logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
            if (isNetworkAvailable(activity)) {
                logoutAsyncTask.execute(logoutAsyncTask);
            } else {
                Toast.makeText(activity, "Logout functionality is only available in Online Mode", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_communication) {
            toolbarHome.setVisibility(View.VISIBLE);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if (statusCode == 200) {
                appPreferenceManager.clearAllPreferences();
                dhbDao.deleteTablesonLogout();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            } else {
                Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }

}
