package com.tus.jpa.wine_validator;

public enum ErrorMessages {
	EMPTY_FIELDS("One or more empty fields"),
	ALREADY_EXISTS("Wine with given name already exists"),
	INVALID_COUNTRY("Not accepting more wines from that country"),
	BAD_GRAPES("Type of grape not acceptable");
	
	private String errorMessage;
	
	ErrorMessages(String errMsg){
		this.errorMessage=errMsg;
	}
	
	public String getMsg(){
		return errorMessage;
	}
}
