package com.dhb.utils.fileutils.fileupload;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dhb.R;

public class ProgressUpdateDialog extends Dialog {

	private RoundCornerProgressBar pbLoading;

	public ProgressUpdateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.dialog_images_load);
		initLayout();
	}

	private void initLayout() {

		pbLoading = (RoundCornerProgressBar) findViewById(R.id.progress);

		pbLoading.setMax(100);

	}

	public void setProgressBar(int perc) {

		pbLoading.setProgress(perc);

	}

}