package com.thyrocare.models.api.response;

import com.google.gson.annotations.SerializedName;

public class BusinessErrorTypeModel {

	@SerializedName("field")
	private String fieldName;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}