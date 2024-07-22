package com.min429.common_data.exception;

public class DataException extends RuntimeException {
	public DataException(String message) {
		super(message);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}
}
