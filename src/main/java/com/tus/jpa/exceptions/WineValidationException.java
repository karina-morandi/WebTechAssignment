package com.tus.jpa.exceptions;

public class WineValidationException extends WineException {

	private static final long serialVersionUID = 334051992916748022L;

	public WineValidationException(final String errorMessage) {
		super(errorMessage);
	}

}

