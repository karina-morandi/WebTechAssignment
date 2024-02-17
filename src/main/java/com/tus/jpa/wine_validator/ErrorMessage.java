package com.tus.jpa.wine_validator;

public class ErrorMessage {
	String errorMessage;
	
	public ErrorMessage(String message) {
		this.errorMessage=message;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

}
