package com.thyrocare.btechapp.models.api.response;


import com.google.gson.annotations.SerializedName;

public class MessageModel {

	@SerializedName("fieldErrors")
	private FieldError[] messages;

	public FieldError[] getMessages() {
		return messages;
	}

	public void setMessages(FieldError[] messages) {
		this.messages = messages;
	}

	private String type;
	@SerializedName("status")
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static class FieldError {
		private String field;
		private String message;

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}