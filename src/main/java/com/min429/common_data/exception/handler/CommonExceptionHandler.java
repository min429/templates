package com.min429.common_data.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.min429.common_data.config.SwaggerResponseEntity;
import com.min429.common_data.exception.ApiException;
import com.min429.common_data.exception.DataException;
import com.min429.common_data.exception.ErrorResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 전 계층 핸들러
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler
	public SwaggerResponseEntity<ErrorResult> handleDataException(DataException e) {
		log.error("[DataException] ex", e);
		HttpStatus httpStatus;

		switch (e.getMessage()) {
			case "데이터 없음":
				httpStatus = HttpStatus.BAD_REQUEST;
				break;
			default:
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return SwaggerResponseEntity.status(httpStatus, new ErrorResult("EX", e.getMessage()));
	}

	@ExceptionHandler
	public void handleDataException(ApiException e) {
		log.error("[ApiException] ex", e);
	}
}
