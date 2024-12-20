package com.tim.amortization.calculator.exception;

public class InputValidationException extends RuntimeException {

	/**
	 * Default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public InputValidationException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
