package com.thyrocare.uiutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thyrocare.utils.api.Logger;
import com.thyrocare.utils.app.AppPreferenceManager;

import java.util.regex.Pattern;

/**
 * Created by Orion on 18/3/15.
 */
public class AbstractFragment extends Fragment implements
        ActivityHelper {

    private Context context;
    protected int sdk;
    protected AppPreferenceManager appPreferenceManager;
    protected Typeface fontOpenRobotoRegular, fontOpenRobotoMedium, fontOpenRobotoLight;
    private ActivityHelper ah;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ah = new ActivityHelperImpl(
                getActivity());


        sdk = android.os.Build.VERSION.SDK_INT;
        activity = getActivity();
        appPreferenceManager = new AppPreferenceManager(activity);
        setFont();
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container,
                savedInstanceState);
    }

    private void setFont() {
        fontOpenRobotoRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-regular.ttf");
        fontOpenRobotoMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-medium.ttf");
        fontOpenRobotoLight = Typeface.createFromAsset(activity.getAssets(), "fonts/roboto-light.ttf");

    }

    @Override
    public void hideKeyboard(View view) {

        ah.hideKeyboard(view);
    }

    @Override
    public Typeface createTypeFace(String fontName) {

        return ah.createTypeFace(fontName);
    }

    @Override
    public Drawable createRepeatableDrawable(int imageId) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {

            Logger.debug("couldn't get connectivity manager");

        } else {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
                }
            }
        }

        return false;
    }



    @Override
    public void switchToActivity(Activity current,
                                 Class<? extends Activity> otherActivityClass,
                                 Bundle extras) {

        ah.switchToActivity(current, otherActivityClass, extras);

    }

    @Override
    public void goToActivity(Activity current,
                             Class<? extends Activity> otherActivityClass,
                             Bundle extras) {

        ah.goToActivity(current, otherActivityClass, extras);

    }

    @Override
    public void initUI() {

        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

// validate First Name

    public static boolean validateFName(String firstName) {

        return firstName.matches("[a-zA-Z-']*");
    }

    // validate last name
    public static boolean validateLName(String lastName) {

        return lastName.matches("[a-zA-Z'-]*");
    }

    public static boolean validatePhoneNumber(CharSequence target) {
        Pattern digitPattern = Pattern.compile("[0-9+-]*");

        return !TextUtils.isEmpty(target)
                && digitPattern.matcher(target)
                .matches();


    }

    public static boolean validateDigit(CharSequence target) {
        Pattern digitPattern = Pattern.compile("[0-9]*");

        return !TextUtils.isEmpty(target)
                && digitPattern.matcher(target)
                .matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

    public void pushFragments(Fragment fragment, boolean shouldAnimate,
                              boolean shouldAdd, String destinationFragmetTag, int frameLayoutContainerId, String CurrentFragmentTag) {
        try {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (shouldAnimate) {
                // ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                // R.animator.fragment_slide_left_exit,
                // R.animator.fragment_slide_right_enter,
                // R.animator.fragment_slide_right_exit);
            }

            ft.replace(frameLayoutContainerId, fragment, CurrentFragmentTag);

            //ft.add(R.id.fr_layout_container, fragment, TAG_FRAGMENT);

            if (shouldAdd) {
                /*
                 * here you can create named backstack for realize another logic.
                   <<<<<<< HEAD
                                                                                                                                                                                                                                                                                                                                                                                                                   <<<<<<< HEAD
                                                                                                                                                                                                                                                                                                                                                                                                                           /*
                 * here you can create named backstack for realize another logic.
                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> bf799f243c1bd10ee4fb953d6481aa806925783f
                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> 7054f2ddd15b92e9724794839f298ccd266af5f2
                   =======
                                                                                                                                                                                                                                                                                                                                                                                                                                                                   <<<<<<< HEAD
                                                                                                                                                                                                                                                                                                                                                                                                                                                                           /*
                 * here you can create named backstack for realize another logic.
                                                                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> bf799f243c1bd10ee4fb953d6481aa806925783f
                                                                                                                                                                                                                                                                                                                                                                                                                                                                   >>>>>>> 7054f2ddd15b92e9724794839f298ccd266af5f2
                   >>>>>>> 596a4e066e37214dd935f8db9f0f637d7af457c3
                 * ft.addToBackStack("name of your backstack");
                 */
                ft.addToBackStack(destinationFragmetTag);
            } else {
                /*
                 * and remove named backstack:
                 * manager.popBackStack("name of your backstack",
                 * FragmentManager.POP_BACK_STACK_INCLUSIVE); or remove whole:
                 * manager.popBackStack(null,
                 * FragmentManager.POP_BACK_STACK_INCLUSIVE);
                 */
                try {
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}