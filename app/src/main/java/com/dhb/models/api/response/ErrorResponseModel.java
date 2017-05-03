package com.dhb.models.api.response;

/**
 * Created by Pratik Ambhore on 9/11/15.
 */
public class ErrorResponseModel {
	private String error_description;
	private String error;
	private int status;

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}