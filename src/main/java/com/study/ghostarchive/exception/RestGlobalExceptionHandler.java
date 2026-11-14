package com.study.ghostarchive.exception;

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
	
	//잘못된 입력값 예외 처리
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
			IllegalArgumentException e){
		
		return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
	}
}
