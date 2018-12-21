package com.thyrocare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thyrocare.R;
import com.thyrocare.delegate.AddSampleBarcodeDialogDelegate;
import com.thyrocare.utils.app.InputUtils;


public class AddSampleBarcodeDialog extends Dialog {
	private Activity activity;
	private EditText etBarcodeValue;
	private EditText etConfirmBarcodeValue;
	private TextView txtTitle;
	private TextView txtBarcodeValue;
	private TextView txtConfirmBarcodeValue;
	private Button btnDone;
	private AddSampleBarcodeDialog addSampleBarcodeDialog;
	private AddSampleBarcodeDialogDelegate addSampleBarcodeDialogDelegate;
	private LinearLayout ll_test_color;
	private String  sampleType;

	public AddSampleBarcodeDialog(String currentScanSampleType,Activity activity, AddSampleBarcodeDialogDelegate addSampleBarcodeDialogDelegate) {
		super(activity);
		this.activity = activity;
		addSampleBarcodeDialog = this;
		this.addSampleBarcodeDialogDelegate = addSampleBarcodeDialogDelegate;
		this.setCanceledOnTouchOutside(false);
		this.sampleType=currentScanSampleType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_add_sample_type_barcode);
		initUI();
	}

	private void initUI() {
		etBarcodeValue = (EditText) findViewById(R.id.et_barcode_value);
		etConfirmBarcodeValue = (EditText) findViewById(R.id.et_confirm_barcode_value);
		txtBarcodeValue = (TextView) findViewById(R.id.txt_barcode_value);
		txtConfirmBarcodeValue = (TextView) findViewById(R.id.txt_confirm_barcode_value);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		btnDone = (Button) findViewById(R.id.btn_done);
		ll_test_color=(LinearLayout)findViewById(R.id.ll_test_color);

		if(sampleType.equals("SERUM")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_serum));
		}else if(sampleType.equals("EDTA")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_edta));
		}else if(sampleType.equals("FLUORIDE")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
		}else if(sampleType.equals("HEPARIN")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_heparin));
		}else if(sampleType.equals("URINE")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_urine));
		}else if(sampleType.equals("FLUORIDE-F")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
		}else if(sampleType.equals("FLUORIDE-PP")){
			ll_test_color.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.bg_sample_type_fluoride));
		}

		setListener();
	}

	private void setListener() {
		btnDone.setOnClickListener(new DoneBtnClickListener());




	}

	private class DoneBtnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {




			if (!isFieldValid(etBarcodeValue.getText().toString()) ||etBarcodeValue.getText().toString().startsWith("0") || etBarcodeValue.getText().toString().startsWith("$")){
				Toast.makeText(activity, activity.getResources().getString(R.string.alert_invalid_barcode_value), Toast.LENGTH_SHORT).show();
			}else if ( !isFieldValid(etConfirmBarcodeValue.getText().toString())||etConfirmBarcodeValue.getText().toString().startsWith("0") || etConfirmBarcodeValue.getText().toString().startsWith("$")){
				Toast.makeText(activity,  activity.getResources().getString(R.string.re_enter_barcode), Toast.LENGTH_SHORT).show();
			} else if (!etBarcodeValue.getText().toString().equals(etConfirmBarcodeValue.getText().toString())){
				Toast.makeText(activity, activity.getResources().getString(R.string.alert_unmatched_barcode_values), Toast.LENGTH_SHORT).show();
			}


			else {
				InputUtils.hideKeyboard(activity,v);
				addSampleBarcodeDialog.dismiss();
				addSampleBarcodeDialogDelegate.onSampleBarcodeAdded(etBarcodeValue.getText().toString());

			}
		}
}

	private boolean isFieldValid(String text) {




		if (!InputUtils.isNull(text)&&text.length()==8){
			return true;
		}

		return false;
	}

}