package com.min429.common_data.exception;

public class ApiException extends RuntimeException {
	public ApiException(String message) {
		super(message);
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
