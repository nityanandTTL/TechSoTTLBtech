package com.dhb.network;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.dhb.utils.app.DateUtils;

import java.io.File;
import java.io.FileOutputStream;


public class SaveBitmapToFileTask extends AsyncTask<Void, Void, Void> {

	private ProgressDialog progressDialog;
	private Context context;
	private Bitmap bitmap;
	private String saveToPath;
	private FileSaveDeligate fileSaveDeligate;
	private int bitmapSize;
	private String oldPath;
	private String fileFormat;

	public SaveBitmapToFileTask(Context context, Bitmap bitmap, String oldPath,
	                            String saveToPath, FileSaveDeligate fileSaveDeligate, int bitmapSize) {
		this.context = context;
		this.bitmap = bitmap;
		this.saveToPath = saveToPath;
		this.fileSaveDeligate = fileSaveDeligate;
		this.bitmapSize = bitmapSize;
		this.oldPath = oldPath;
	}

	public SaveBitmapToFileTask(Context context, Bitmap bitmap, String oldPath,
	                            String saveToPath, FileSaveDeligate fileSaveDeligate,
	                            int bitmapSize, String fileFormat) {
		this.context = context;
		this.bitmap = bitmap;
		this.saveToPath = saveToPath;
		this.fileSaveDeligate = fileSaveDeligate;
		this.bitmapSize = bitmapSize;
		this.oldPath = oldPath;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		progressDialog = new ProgressDialog(context);
//		progressDialog.setError_description("Please wait...");
//		progressDialog.setCancelable(false);
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.show();
	}

	@Override
	protected Void doInBackground(Void ... params) {

		CompressFormat compressFormat = null;

		if (saveToPath == null){

			saveToPath = oldPath + "_sei_crop_" + (DateUtils.getCurrentTimeInDefault() / 100)
			             + ".jpeg";

		}

		if (fileFormat == null){
			fileFormat = saveToPath.substring(saveToPath.lastIndexOf(".") + 1);
		}

		if (fileFormat == null){
			compressFormat = CompressFormat.JPEG;
		} else if (fileFormat.equals("jpg") || fileFormat.equals("jpeg")){
			compressFormat = CompressFormat.JPEG;
		} else if (fileFormat.equals("png")){
			compressFormat = CompressFormat.PNG;
		} else {
			compressFormat = CompressFormat.JPEG;
		}

		File filename = new File(saveToPath);
		if (!filename.exists()){
			try {
				filename.createNewFile();
			} catch (Exception e){
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(filename);

			// BitmapFactory.Options bitopt=new BitmapFactory.Options();

			bitmap.compress(compressFormat, bitmapSize, out);
			out.close();
		} catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection,
		                                                   null, null, null);
		// startManagingCursor(cursor);
		int column_index = cursor
		                   .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		String filePath = null;
		if (cursor != null){
			cursor.moveToFirst();
			filePath = cursor.getString(column_index);
		}
		return filePath;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

//		if (progressDialog.isShowing()) {
//			progressDialog.cancel();
//		}


		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, saveToPath);

		Uri uri = context.getContentResolver().insert(
		        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		fileSaveDeligate.onFileSaveComplete(uri);
	}

	public interface FileSaveDeligate {
		public void onFileSaveComplete(Uri uri);
	}
}