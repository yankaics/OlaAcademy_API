package com.kzsrm.utils;

public class CustomException extends Exception {
	private static final long serialVersionUID = 8954039586908598508L;

	public CustomException() {
		super();
	}

	public CustomException(String msg) {
		super(msg);
	}

	public CustomException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}
}
