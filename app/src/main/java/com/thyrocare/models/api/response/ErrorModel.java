package com.thyrocare.models.api.response;


public class ErrorModel {

	// @SerializedName("TYPE")
	private String CustomErrorCode;

	// @SerializedName("MESSAGE")
	private String Message;

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


	public String getCustomErrorCode() {
		return CustomErrorCode;
	}

	public void setCustomErrorCode(String customErrorCode) {
		this.CustomErrorCode = customErrorCode;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		this.Message = message;
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