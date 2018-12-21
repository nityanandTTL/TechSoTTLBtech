package com.thyrocare.models.api.response;

import com.google.gson.annotations.SerializedName;

public class BusinessErrorModel {

	private String type;

	@SerializedName("status")
	private int statusCode;

	@SerializedName("messages")
	private BusinessErrorTypeModel messages;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BusinessErrorTypeModel getMessages() {
		return messages;
	}

	public void setMessages(BusinessErrorTypeModel messages) {
		this.messages = messages;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}