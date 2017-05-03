package com.dhb.network;

import org.json.JSONException;

/**
 * Created by Pratik Ambhore on 16/3/15.
 */
public interface ApiCallAsyncTaskDelegate {


	public void apiCallResult(String json, int statusCode) throws JSONException;
	public void onApiCancelled();
}