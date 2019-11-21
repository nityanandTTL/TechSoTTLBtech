package com.thyrocare.utils.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thyrocare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Global {

    private Context context;
    public String fontDefault = "OPENSANS-REGULAR_3.TTF";
    public String fontRegular = "OPENSANS-REGULAR_3.TTF";
    public String fontLight = "OPENSANS-LIGHT_3.TTF";
    public String fontBold = "OPENSANS-BOLD_2.TTF";
    public String fontSemiBold = "OPENSANS-SEMIBOLD_2.TTF";
    public String fontItalic = "OPENSANS-ITALIC_2.TTF";

    public String tableAarogyam = "Aarogyam";
    public String tableProfile = "Profile";
    public String tableTests = "Tests";
    public String tableOffer = "Offer";
    public String tableOfferCart = "OfferCart";
    public String tableThyronomicOfferCart = "ThyronomicOfferCart";

    public String tableAarogyamChilds = "Aarogyam_childs";
    public String tableProfileChilds = "Profile_childs";
    public String tableTestsChilds = "Tests_childs";
    public String tableOfferChilds = "Offer_childs";
    public String tableOfferCartChilds = "OfferCart_childs";
    public String tableThyronomicOfferCartChilds = "ThyronomicOfferCart_childs";

    public String tableCart = "Cart";
//Live ---------------------------
    public static String BASE_URL = "https://www.thyrocare.com/APIs/";
    public static String SERVER_BASE_API_URL_PROD = "https://www.dxscloud.com/techsoapi";

//Staging--------------------------------
//    public static String BASE_URL = "https://www.thyrocare.com/API_BETA/";
//    public static String SERVER_BASE_API_URL_PROD = "http://bts.dxscloud.com/techsoapi";


    ProgressDialog progressDialog;

    public Global(Context context) {
        this.context = context;
    }


    public String getBtsSchema() {
       /* if (BuildConfig.DEBUG) {
			return "http://bts.dxscloud.com";
		} else {
			// for release
			return "https://www.dxscloud.com";
		}*/

        //Staging--------------------------------
        //return "http://bts.dxscloud.com";

        //Live ---------------------------
        return "https://www.dxscloud.com";
    }

    public String convertNumberToPrice(String s) {
        Double price = Double.parseDouble(s);
        Locale locale = new Locale("en", "IN");
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        if (checkForApi14())
            symbols.setCurrencySymbol("\u20B9"); // Don't use null.
        else
            symbols.setCurrencySymbol("\u20A8"); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(0);
        //System.out.println(formatter.format(price));
        s = formatter.format(price);
        return s;
    }

    public Boolean checkForApi11() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public Boolean checkForApi14() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public Boolean checkForApi21() {
        Boolean boolStatus = false;
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public void showCustomToast(Activity activity, String message) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void showcenterCustomToast(Activity activity, String message, int lengthLong) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(lengthLong);
        toast.show();
    }
    public void setLoadingGIF(Activity activity) {
		/*InputStream stream = null;
		try {
			stream = activity.getAssets().open("thyrocare_gif.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
		GifWebView gifWebView = new GifWebView(activity, "file:///android_asset/thyrocare_gif.gif");
		gifWebView.setBackgroundColor(Color.TRANSPARENT);*/

		/*LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout linLoading = (LinearLayout) view.findViewById(R.id.linLoading);*/

        //linLoading.removeAllViews();
        //linLoading.addView(gifWebView);

		/*progressDialog = new Dialog(activity);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setContentView(view);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		progressDialog.setCanceledOnTouchOutside(false);*/

        //progressDialog = ProgressDialog.show(activity, null, activity.getResources().getString(R.string.loading), false);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e52d2e")));
    }

    public void showProgressDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    public void showProgressDialog(Activity activity , String msg){

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        try {
            if (progressDialog != null && !progressDialog.isShowing())

                if (!((Activity) context).isFinishing()) {
                    progressDialog.show();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public String formatDate(String currentFormat, String outputFormat, String date) {

        SimpleDateFormat curFormater = new SimpleDateFormat(currentFormat);
        SimpleDateFormat postFormater = new SimpleDateFormat(outputFormat);
        //SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");
        //SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(date);
            date = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public static String toUpperCase(String inputString) {
        String result = "";
        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char currentCharToUpperCase = Character.toUpperCase(currentChar);
            result = result + currentCharToUpperCase;
        }
        return result;
    }

    public static String toLowerCase(String inputString) {
        String result = "";
        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char currentCharToLowerCase = Character.toLowerCase(currentChar);
            result = result + currentCharToLowerCase;
        }
        return result;
    }

    public static String toSentenceCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        boolean terminalCharacterEncountered = false;
        char[] terminalCharacters = {'.', '?', '!'};
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (terminalCharacterEncountered) {
                if (currentChar == ' ') {
                    result = result + currentChar;
                } else {
                    char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = result + currentCharToUpperCase;
                    terminalCharacterEncountered = false;
                }
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
            for (int j = 0; j < terminalCharacters.length; j++) {
                if (currentChar == terminalCharacters[j]) {
                    terminalCharacterEncountered = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean checkValidation(String type, int length) {
        boolean result = false;
        if (type.equals("mobile")) {
            result = length != 10 ? false : true;
        } else if (type.equals("address")) {
            result = length <= 25 ? false : true;
        }

        return result;
    }

    public String checkJsonNullStringValue(JSONObject jsonObject, String key) {
        String value = "";
        try {
            value = jsonObject.isNull(key) ? "" : jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void showalert_OK(String message, Context context) {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlert_OK_WithTitle(String message, Context context, String title) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void DisplayImagewithoutDefaultImage(Activity activity, String Url, ImageView imageView) {

        Glide.get(activity).clearMemory();
        Glide.with(activity).load(Url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);

    }

}
