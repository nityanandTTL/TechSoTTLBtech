package com.thyrocare.utils.fileutils.fileupload;

public interface FileUploadApiDelegate {

	public void preExecute();

	public void publishProgress(Integer[] values);

	public void postExecute(String json, int statusCode);

	public void onError(String error);

}