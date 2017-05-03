package com.dhb.models.api.response;


public class ErrorModel {

	// @SerializedName("TYPE")
	private String error;

	// @SerializedName("MESSAGE")
	private String error_description;

	public String getResponse() {
		return Response;
	}

	public void setResponse(String response) {
		Response = response;
	}

	private String Response;

	// @SerializedName("STATUSCODE")
	private int status;

	private long id;

	private long errorId;


	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int statusCode) {
		this.status = statusCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getErrorId() {
		return errorId;
	}

	public void setErrorId(long errorId) {
		this.errorId = errorId;
	}

}