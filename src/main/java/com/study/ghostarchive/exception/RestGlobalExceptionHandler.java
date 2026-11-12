package com.study.ghostarchive.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.study.ghostarchive.dto.response.ApiResponse;

@RestControllerAdvice(basePackages="com.study.ghostarchive.rest.controller")
public class RestGlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e){
		ApiResponse<Void> response = ApiResponse.fail(e.getMessage());
		
		return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
	}
}
