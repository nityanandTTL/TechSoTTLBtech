package com.dhb.utils.fileutils.fileupload;

import android.app.Activity;
import android.content.Context;

import com.dhb.network.AbstractApiModel;

import java.io.File;

/**
 * Created by Jay Nair on 24/8/15.
 */
public class FileUploadManager {

	public static void upload(Activity activity, File document, FileUploadApiDelegate delegate) {
		FileUploadAsyncTask fileUploadAsyncTask = new FileUploadAsyncTask();
		fileUploadAsyncTask.setActivity(activity);
		fileUploadAsyncTask.addFilePart("file", document.getAbsolutePath());
		fileUploadAsyncTask.setDelegate(delegate);
		fileUploadAsyncTask.setFileUploadUrl(AbstractApiModel.SERVER_BASE_API_URL + AbstractApiModel.FILE_UPLOAD);
		fileUploadAsyncTask.execute(fileUploadAsyncTask);
	}

	public static void uploadInBackground(Context context, String documentFilePath, FileUploadApiDelegate delegate) {
		FileUploadAsyncTask fileUploadAsyncTask = new FileUploadAsyncTask();
		fileUploadAsyncTask.setContext(context);
		fileUploadAsyncTask.addFilePart("file", documentFilePath);
		fileUploadAsyncTask.setDelegate(delegate);
		fileUploadAsyncTask.setFileUploadUrl(AbstractApiModel.SERVER_BASE_API_URL + AbstractApiModel.FILE_UPLOAD);
		fileUploadAsyncTask.execute(fileUploadAsyncTask);
	}

}