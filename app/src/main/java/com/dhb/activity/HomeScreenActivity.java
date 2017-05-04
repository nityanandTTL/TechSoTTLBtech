package com.dhb.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.dhb.R;
import com.dhb.customview.RoundedImageView;
import com.dhb.dao.DhbDao;
import com.dhb.fragment.HomeScreenFragment;
import com.dhb.fragment.ResetPasswordFragment;
import com.dhb.network.ApiCallAsyncTask;
import com.dhb.network.ApiCallAsyncTaskDelegate;
import com.dhb.network.AsyncTaskForRequest;
import com.dhb.uiutils.AbstractActivity;
import com.dhb.utils.app.AppPreferenceManager;
import com.dhb.utils.app.CommonUtils;
import com.dhb.utils.app.InputUtils;

import org.json.JSONException;

public class HomeScreenActivity extends AbstractActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG_ACTIVITY = "HOME_SCREEN_ACTIVITY";
    private FloatingActionButton fabBtn;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbarHome;
    private RoundedImageView rivSelfie;
    private TextView txtUserName;
    private TextView txtUserId;
    private LinearLayout llNavHeader;
    private HomeScreenActivity activity;
    private AppPreferenceManager appPreferenceManager;
    private DhbDao dhbDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        activity = this;
        dhbDao = new DhbDao(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        initUI();
        initData();
        pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
    }

    private void initData() {
        if(!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserName()))
            txtUserName.setText(appPreferenceManager.getLoginResponseModel().getUserName());
        if(!InputUtils.isNull(appPreferenceManager.getLoginResponseModel().getUserID()))
            txtUserId.setText(appPreferenceManager.getLoginResponseModel().getUserID());
        if(appPreferenceManager.getSelfieResponseModel()!=null && !InputUtils.isNull(appPreferenceManager.getSelfieResponseModel().getPic())) {
            rivSelfie.setImageBitmap(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));
            BitmapDrawable bmpDrawable = new BitmapDrawable(CommonUtils.decodeImage(appPreferenceManager.getSelfieResponseModel().getPic()));
            llNavHeader.setBackgroundDrawable(bmpDrawable);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View NavHeaderHomeScreen = LayoutInflater.from(activity).inflate(R.layout.nav_header_home_screen,null);
        llNavHeader = (LinearLayout) NavHeaderHomeScreen.findViewById(R.id.ll_nav_header);
        rivSelfie = (RoundedImageView) NavHeaderHomeScreen.findViewById(R.id.img_user_picture);
        txtUserId = (TextView) NavHeaderHomeScreen.findViewById(R.id.txt_user_id);
        txtUserName = (TextView) NavHeaderHomeScreen.findViewById(R.id.txt_user_name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_visit_orders) {
            Toast.makeText(getApplicationContext(),"in visit orders",Toast.LENGTH_SHORT).show();
            pushFragments(VisitOrdersDisplayFragment.newInstance(),false,false,VisitOrdersDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_schedule_your_day) {
            Toast.makeText(getApplicationContext(),"in schedule your day",Toast.LENGTH_SHORT).show();
            pushFragments(ScheduleYourDayFragment.newInstance(),false,false,ScheduleYourDayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_ledger) {
            Toast.makeText(getApplicationContext(),"in ledger",Toast.LENGTH_SHORT).show();
            pushFragments(LedgerDisplayFragment.newInstance(),false,false,LedgerDisplayFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_hub) {
            Toast.makeText(getApplicationContext(),"in Hub drop",Toast.LENGTH_SHORT).show();
            pushFragments(DispatchToHubFragment.newInstance(),false,false, DispatchToHubFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_materials) {

        }*/
        if (id == R.id.nav_home) {
            Toast.makeText(getApplicationContext(),"in home",Toast.LENGTH_SHORT).show();
            pushFragments(HomeScreenFragment.newInstance(),false,false,HomeScreenFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_change_password) {
            Toast.makeText(getApplicationContext(),"in change password",Toast.LENGTH_SHORT).show();
            pushFragments(ResetPasswordFragment.newInstance(),false,false, ResetPasswordFragment.TAG_FRAGMENT,R.id.fl_homeScreen,TAG_ACTIVITY);
        } else if (id == R.id.nav_logout) {
            ApiCallAsyncTask logoutAsyncTask = new AsyncTaskForRequest(activity).getLogoutRequestAsyncTask();
            logoutAsyncTask.setApiCallAsyncTaskDelegate(new LogoutAsyncTaskDelegateResult());
            if(isNetworkAvailable(activity)){
                logoutAsyncTask.execute(logoutAsyncTask);
            }
            else{
                Toast.makeText(activity,"Logout functionality is only available in Online Mode",Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_communication) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LogoutAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {
        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {
            if(statusCode==200){
                appPreferenceManager.clearAllPreferences();
                dhbDao.deleteTablesonLogout();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
            else{
                Toast.makeText(activity,"Failed to Logout",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onApiCancelled() {

        }
    }
}
