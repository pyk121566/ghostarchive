package com.study.ghostarchive.exception;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages="com.study.ghostarchive.controller")
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ModelAndView handlerBusinessException(BusinessException e) {
		
		ModelAndView mav = new ModelAndView("/error");
		
		mav.addObject("errorMessage", e.getMessage());
		mav.setStatus(e.getErrorCode().getStatus());
		
		return mav;
	}
}
