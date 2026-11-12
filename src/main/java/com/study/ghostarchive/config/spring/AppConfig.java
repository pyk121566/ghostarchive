package com.study.ghostarchive.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration	//Spring 설정 클래스라는것을 알려줌
@EnableWebMvc	//Spring MVC 기능을 활성화하는 어노테이션
@ComponentScan(basePackages = {
		"com.study.ghostarchive.controller",
		"com.study.ghostarchive.dao",
		"com.study.ghostarchive.service",
		"com.study.ghostarchive.exception",
		"com.study.ghostarchive.rest.controller"
})	//지정한 패키지에서 Spring이 관리할 컴포넌트를 검색
public class AppConfig {

	@Bean	//메서드가 반환하는 객체를 Spring Bean 으로 등록하는 어노테이션
	public InternalResourceViewResolver viewResolver() {
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix("/WEB-INF/views");
		resolver.setSuffix(".jsp");
		
		return resolver;
	}
}
