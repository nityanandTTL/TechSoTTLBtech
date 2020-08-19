package com.thyrocare.btechapp.NewScreenDesigns.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thyrocare.btechapp.NewScreenDesigns.Fragments.EnteredCampWoeFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Fragments.NewEntryCampWoeFragment;
import com.thyrocare.btechapp.NewScreenDesigns.Interfaces.GoToCampMisScreen;
import com.thyrocare.btechapp.NewScreenDesigns.Utils.MessageLogger;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.dao.utils.ConnectionDetector;
import com.thyrocare.btechapp.utils.app.AppPreferenceManager;
import com.thyrocare.btechapp.utils.app.Global;

import java.util.List;

public class NewCampWOEModuleActivity extends AppCompatActivity implements GoToCampMisScreen {

    private static String TAG = NewCampWOEModuleActivity.class.getSimpleName();
    Activity mActivity;
    Global globalclass;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;
    private LinearLayout lin_Enter,lin_Entered;
    private TextView tv_enter,tv_entered;
    private FrameLayout fragment_main;
    private Object currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_camp_module);

        setTitle("Checkout");
        mActivity = NewCampWOEModuleActivity.this;
        globalclass = new Global(mActivity);
        cd = new ConnectionDetector(mActivity);
        appPreferenceManager = new AppPreferenceManager(mActivity);

        initView();
        initData();
        initListener();
        initToolbar();

    }

    private void initView() {
        lin_Enter = (LinearLayout) findViewById(R.id.lin_Enter);
        lin_Entered = (LinearLayout) findViewById(R.id.lin_Entered);

        tv_enter = (TextView) findViewById(R.id.tv_enter);
        tv_entered = (TextView) findViewById(R.id.tv_entered);
        fragment_main = (FrameLayout) findViewById(R.id.fragment_mainLayout);
    }

    private void initData() {
        lin_Enter.setBackground(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
        lin_Entered.setBackground(getResources().getDrawable(R.drawable.rounded_background_empty_orange));
        tv_enter.setTextColor(getResources().getColor(R.color.white));
        tv_entered.setTextColor(getResources().getColor(R.color.colorOrange));
        NewEntryCampWoeFragment newEntryCampWoeFragment = new NewEntryCampWoeFragment();
        replaceFragment(newEntryCampWoeFragment);
    }

    private void initListener() {
        lin_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_Enter.setBackground(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
                lin_Entered.setBackground(getResources().getDrawable(R.drawable.rounded_background_empty_orange));
                tv_enter.setTextColor(getResources().getColor(R.color.white));
                tv_entered.setTextColor(getResources().getColor(R.color.colorOrange));
                NewEntryCampWoeFragment newEntryCampWoeFragment = new NewEntryCampWoeFragment();
                replaceFragment(newEntryCampWoeFragment);
            }
        });

        lin_Entered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_Entered.setBackground(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
                lin_Enter.setBackground(getResources().getDrawable(R.drawable.rounded_background_empty_orange));
                tv_entered.setTextColor(getResources().getColor(R.color.white));
                tv_enter.setTextColor(getResources().getColor(R.color.colorOrange));
                EnteredCampWoeFragment enteredCampWoeFragment = new EnteredCampWoeFragment();
                replaceFragment(enteredCampWoeFragment);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSurvey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageLogger.LogError("Toolbar", "Clicked");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu_screen, menu);
        return true;
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        try {
            List fragments = getSupportFragmentManager().getFragments();
            currentFragment = fragments.get(fragments.size() - 1);
            if (currentFragment.toString().contains("EnteredCampWoeFragment")) {
                lin_Enter.setBackground(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
                lin_Entered.setBackground(getResources().getDrawable(R.drawable.rounded_background_empty_orange));
                tv_enter.setTextColor(getResources().getColor(R.color.white));
                tv_entered.setTextColor(getResources().getColor(R.color.colorOrange));
                NewEntryCampWoeFragment newEntryCampWoeFragment = new NewEntryCampWoeFragment();
                replaceFragment(newEntryCampWoeFragment);
            } else if (currentFragment.toString().contains("NewEntryCampWoeFragment")) {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment destFragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void GoToMisScreen() {
        lin_Entered.setBackground(getResources().getDrawable(R.drawable.rounded_background_filled_oranged));
        lin_Enter.setBackground(getResources().getDrawable(R.drawable.rounded_background_empty_orange));
        tv_entered.setTextColor(getResources().getColor(R.color.white));
        tv_enter.setTextColor(getResources().getColor(R.color.colorOrange));
        EnteredCampWoeFragment enteredCampWoeFragment = new EnteredCampWoeFragment();
        replaceFragment(enteredCampWoeFragment);
    }
}
