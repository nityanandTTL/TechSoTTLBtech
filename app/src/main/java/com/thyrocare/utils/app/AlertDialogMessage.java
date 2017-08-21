package com.thyrocare.utils.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.view.WindowManager;


public class AlertDialogMessage {


	private AlertDialogOkListener alertDialogOkListener;

	private AlertDialogCancelListener alertDialogCancelListener;

	public static AlertDialog alertDialog;

	public Typeface fontOpenSansSemiBold;

	public void setAlertDialogOkListener(AlertDialogOkListener alertDialogOkListener) {
		this.alertDialogOkListener = alertDialogOkListener;
	}

	public void setAlertDialogCancelListener(AlertDialogCancelListener alertDialogCancelListener) {
		this.alertDialogCancelListener = alertDialogCancelListener;
	}

	public static void cancelPreviousAlertDialog() {

		try {
			if (alertDialog != null && alertDialog.getContext() != null){
				alertDialog.dismiss();

			}
		} catch (Exception e){
		}

	}

	@SuppressLint("NewApi")
	public void showAlert(Context context, String title, String message,
	                      boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setTitle(title);
		builder1.setMessage(message);

		builder1.setCancelable(isCancellable);
		builder1.setPositiveButton("OK", new AlertDialogOkButtonListener());
		alertDialog = builder1.create();
		alertDialog.show();

	}

	public void showAlert(Context context, String message, boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(isCancellable);
		builder.setPositiveButton("OK", new AlertDialogOkButtonListener());

		alertDialog = builder.create();
		alertDialog.setCancelable(isCancellable);
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();

	}

	public void showSingleButtonAlert(Context context, String message,
	                                  String positiveButton, boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(isCancellable);
		builder.setPositiveButton(positiveButton,
		                          new AlertDialogOkButtonListener());
		alertDialog = builder.create();
		alertDialog.setCancelable(isCancellable);
		alertDialog.show();

	}

	public void showAlertOkCancel(Context context, String message,
	                              boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(isCancellable);
		builder.setPositiveButton("Ok", new AlertDialogOkButtonListener());
		builder.setNegativeButton("Cancel", new AlertDialogCancelButtonListener());

		alertDialog = builder.create();
		alertDialog.setCancelable(isCancellable);
		alertDialog.show();

	}

	public void showAlertYesNo(Context context, String message,
	                           boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(isCancellable);
		builder.setPositiveButton("Yes", new AlertDialogOkButtonListener());
		builder.setNegativeButton("No", new AlertDialogCancelButtonListener());

		alertDialog = builder.create();
		alertDialog.setCancelable(isCancellable);
		alertDialog.show();

	}

	public void showAlertTwoButtonAlert(Context context, String message,
	                                    String positiveButton, String negativeButton, boolean isCancellable) {

		if (context == null){
			return;
		}

		cancelPreviousAlertDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setCancelable(isCancellable);
		builder.setPositiveButton(positiveButton,
		                          new AlertDialogOkButtonListener());
		builder.setNegativeButton(negativeButton,
		                          new AlertDialogCancelButtonListener());

		alertDialog = builder.create();
		alertDialog.setCancelable(isCancellable);
		alertDialog.show();

	}

	private class AlertDialogOkButtonListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
			if (alertDialogOkListener != null){
				alertDialogOkListener.onAlertDialogOkButtonListener();
			}

		}

	}

	private class AlertDialogCancelButtonListener implements OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
			if (alertDialogCancelListener != null){
				alertDialogCancelListener.onAlertDialogCancelButtonListener();
			}

		}

	}

	public interface AlertDialogOkListener {

		public void onAlertDialogOkButtonListener();

	}

	public interface AlertDialogCancelListener {

		public void onAlertDialogCancelButtonListener();

	}

	public interface AlertDialogListener {

		public void onAlertDialogOkButtonListener();

	}

	public interface AlertDialogOkSweptListener {
		public void OKSweptOnclickListener(DialogInterface dialogInterface);

	}

	/*public Dialog showSweptAlert(Context context, String message,
	                             boolean isCancellable) {

	    if (context == null) {
	        return null;
	    }
	    cancelPreviousAlertDialog();

	    final Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	    dialog.setContentView(R.layout.dialog_default_alert);
	    // set the custom dialog components - text, image and button
	    TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_name);
	    TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_alert_message);
	    Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

	    fontOpenSansSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
	    txtTitle.setTypeface(fontOpenSansSemiBold);
	    txtMessage.setTypeface(fontOpenSansSemiBold);
	    btnOk.setTypeface(fontOpenSansSemiBold);

	    txtTitle.setText("Alert");
	    txtMessage.setText(message);
	    dialog.setCancelable(isCancellable);
	    btnOk.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            dialog.dismiss();
	        }
	    });
	    dialog.show();
	    return dialog;

	   }


	   public interface SweptDialogOnclickListener {
	    public void onClick();
	   }
	 */
}