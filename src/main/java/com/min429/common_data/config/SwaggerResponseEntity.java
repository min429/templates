package com.min429.common_data.config;

import org.springframework.http.HttpStatus;

public record SwaggerResponseEntity<T>(HttpStatus httpStatus, T data) {

	public static <T> SwaggerResponseEntity<T> success() {
		return new SwaggerResponseEntity<T>(HttpStatus.OK, null);
	}

	public static <T> SwaggerResponseEntity<T> success(T data) {
		return new SwaggerResponseEntity<T>(HttpStatus.OK, data);
	}

	public static <T> SwaggerResponseEntity<T> badRequest(T data) {
		return new SwaggerResponseEntity<T>(HttpStatus.BAD_REQUEST, data);
	}

	public static <T> SwaggerResponseEntity<T> unAuthorized(T data) {
		return new SwaggerResponseEntity<T>(HttpStatus.UNAUTHORIZED, data);
	}

	public static <T> SwaggerResponseEntity<T> status(HttpStatus httpStatus, T data) {
		return new SwaggerResponseEntity<T>(httpStatus, data);
	}
}
