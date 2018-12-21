package com.thyrocare.utils.fileutils.fileupload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Window;

import com.google.gson.JsonSyntaxException;
import com.thyrocare.network.AbstractApiModel;
import com.thyrocare.utils.app.AppPreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileUploadAsyncTask extends AsyncTask<Void, Integer, String>{

	private static DefaultHttpClient httpClient;
	private FileUploadApiDelegate delegate;
	private long totalSize;
	private HttpPost httpPost;
	private HashMap<String, String> fileMap;
	private HashMap<String, String> stringMap;
	private ProgressUpdateDialog progressUpdateDialog;
	private Activity activity;
	private Context context;
	private String fileUploadUrl;
	private int statusCode;

	public FileUploadAsyncTask() {

	}

	public FileUploadAsyncTask(FileUploadApiDelegate delegate,
	                           HashMap<String, String> fileMap, HashMap<String, String> stringMap) {
		super();

		this.delegate = delegate;
		this.fileMap = fileMap;
		this.stringMap = stringMap;
	}

	public FileUploadAsyncTask(FileUploadApiDelegate delegate,
	                           HashMap<String, String> fileMap) {
		super();
		this.delegate = delegate;
		this.fileMap = fileMap;
	}

	public static void cancelHttpClient() {

		if (httpClient != null){
			httpClient.getConnectionManager().shutdown();
		}
	}

	public void setDelegate(FileUploadApiDelegate delegate) {
		this.delegate = delegate;
	}

	public void setFileMap(HashMap<String, String> fileMap) {
		this.fileMap = fileMap;
	}

	public void setStringMap(HashMap<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public void addFilePart(String fileKey, String filePath) {

		if (fileMap == null){
			fileMap = new HashMap<String, String>();
		}

		fileMap.put(fileKey, filePath);

	}

	public void addStringPart(String key, String value) {

		if (stringMap == null){
			stringMap = new HashMap<String, String>();
		}

		stringMap.put(key, value);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getFileUploadUrl() {
		return fileUploadUrl;
	}

	public void setFileUploadUrl(String fileUploadUrl) {
		this.fileUploadUrl = fileUploadUrl;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		delegate.preExecute();

		if (activity != null){

			progressUpdateDialog = new ProgressUpdateDialog(activity);

			progressUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			progressUpdateDialog.setCanceledOnTouchOutside(false);

			progressUpdateDialog.setCancelable(false);

			progressUpdateDialog.show();
		}
	}

	@Override
	protected String doInBackground(Void ... params) {

		String error = "Error :";

		android.os.Process.setThreadPriority(12);

		String serverResponse;
		CustomMultipartEntity multipartContent = null;
		try {
			httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();

			httpPost = new HttpPost(fileUploadUrl);

			httpPost.addHeader(AbstractApiModel.X_API_KEY, new AppPreferenceManager(activity).getAPISessionKey());

			multipartContent = new CustomMultipartEntity(

			        new CustomMultipartEntity.ProgressListener() {

				@Override
				public void transferred(long num) {
				        publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			if (fileMap != null){

				Iterator<Map.Entry<String, String> > it = fileMap.entrySet()
				                                          .iterator();
				while (it.hasNext()){
					Map.Entry<String, String> pairs = (Map.Entry<String, String>)it
					                                  .next();

					File file = new File(pairs.getValue());
					BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					int nRead;
					byte[] data = new byte[16384];

					while ((nRead = bufferedInputStream.read(data, 0, data.length)) != -1){
						buffer.write(data, 0, nRead);
					}

					buffer.flush();

					multipartContent.addPart(file.getName(),
					                         new ByteArrayBody(buffer.toByteArray(),"image/jpeg", file.getName()));

//					BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//					Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
//
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//					byte[] data = bos.toByteArray();
//
//					multipartContent.addPart(file.getName(),
//					                         new ByteArrayBody(data,"image/jpeg", file.getName()));

//					multipartContent.addPart(pairs.getKey(), new FileBody(
//					                                 new File(pairs.getValue())));

					it.remove(); // avoids a ConcurrentModificationException
				}

			}

			if (stringMap != null){

				Iterator<Map.Entry<String, String> > it = stringMap.entrySet()
				                                          .iterator();
				while (it.hasNext()){
					Map.Entry<String, String> pairs = (Map.Entry<String, String>)it
					                                  .next();

					multipartContent.addPart(pairs.getKey(), new StringBody(
					                                 pairs.getValue()));

					it.remove(); // avoids a ConcurrentModificationException
				}

			}

			totalSize = multipartContent.getContentLength();

			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());
			statusCode = response.getStatusLine().getStatusCode();

			return serverResponse;

		} catch (ClientProtocolException e){
			error = e + e.getMessage();
			e.printStackTrace();
		} catch (ParseException e){
			error = e + e.getMessage();
			e.printStackTrace();
		} catch (IOException e){
			error = e + e.getMessage();
			e.printStackTrace();
		} catch (Exception e){
			error = e + e.getMessage();
			e.printStackTrace();
		} finally {

			try {
				multipartContent.consumeContent();
			} catch (UnsupportedOperationException e){
				e.printStackTrace();
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		return error;
	}

	@Override
	protected void onPostExecute(String jsonPostResponse) {

		if (activity != null && progressUpdateDialog != null){

			progressUpdateDialog.dismiss();
		}

		if (jsonPostResponse.startsWith("Error")){

			delegate.onError(jsonPostResponse);

			return;

		}

		try {
			delegate.postExecute(jsonPostResponse, statusCode);
		} catch (JsonSyntaxException jsonSyntaxException){
			jsonSyntaxException.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onProgressUpdate(Integer ... values) {
		if (progressUpdateDialog != null){
			progressUpdateDialog.setProgressBar(values[0]);
		}
		delegate.publishProgress(values);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void execute(AsyncTask<Void, Integer, String> as) {

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1){
			as.execute();
		} else {
			as.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

}