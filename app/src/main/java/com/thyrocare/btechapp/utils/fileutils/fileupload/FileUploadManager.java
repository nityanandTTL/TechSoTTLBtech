package com.thyrocare.btechapp.utils.fileutils.fileupload;

import android.app.Activity;
import android.content.Context;

import com.thyrocare.btechapp.NewScreenDesigns.Utils.EncryptionUtils;
import com.thyrocare.btechapp.R;
import com.thyrocare.btechapp.network.AbstractApiModel;

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
        fileUploadAsyncTask.setFileUploadUrl(EncryptionUtils.Dcrp_Hex(activity.getString(R.string.SERVER_BASE_API_URL_PROD)) + AbstractApiModel.FILE_UPLOAD);
        fileUploadAsyncTask.execute(fileUploadAsyncTask);
    }

    public static void uploadInBackground(Context context, String documentFilePath, FileUploadApiDelegate delegate) {
        FileUploadAsyncTask fileUploadAsyncTask = new FileUploadAsyncTask();
        fileUploadAsyncTask.setContext(context);
        fileUploadAsyncTask.addFilePart("file", documentFilePath);
        fileUploadAsyncTask.setDelegate(delegate);
        fileUploadAsyncTask.setFileUploadUrl(EncryptionUtils.Dcrp_Hex(context.getString(R.string.SERVER_BASE_API_URL_PROD)) + AbstractApiModel.FILE_UPLOAD);
        fileUploadAsyncTask.execute(fileUploadAsyncTask);
    }

}