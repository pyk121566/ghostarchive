package com.study.ghostarchive.config.spring;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration	//이 클래스가 Spring 설정 클래스라는 것을 알려줌 나중에 이곳에 DB를 추가함
@MapperScan("com.study.ghostarchive.mapper")	//이 패키지 안에 있는 MyBatis Mapper 인터페이스들을
																		//찾아서 Bean 으로 등록해줘!!?
public class RootConfig {

	//1. Oracle DB 연결 설정
	@Bean
	public DataSource dataSource() throws IOException {

		//db.properties 파일 열기
		Properties props = new Properties();
		
		ClassPathResource resource = new ClassPathResource("db.properties");
		
		try(java.io.InputStream input = resource.getInputStream()){
			props.load(input);
			}	
		
		//HikariCP 설정
		HikariConfig config = new HikariConfig();
		
		config.setDriverClassName(props.getProperty("db.driver"));
		config.setJdbcUrl(props.getProperty("db.url"));
		config.setUsername(props.getProperty("db.username"));
		config.setPassword(props.getProperty("db.password"));
			
		return new HikariDataSource(config);
	}
		
	//2. Mybatis 설정
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		
		//Mybatis 의 SqlSessionFactory 를 생성하기 위한 객체
		SqlSessionFactoryBean sqlSessionFactoryBean= new SqlSessionFactoryBean();
		
		//Oracle DB 연결
		sqlSessionFactoryBean.setDataSource(dataSource);
		
		//Mybatis 설정 파일 위치
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("com/study/ghostarchive/config/mybatis/mybatis-config.xml"));
		
		//Sql Mapper XML 파일 위치
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/**/*.xml"));
	
		return sqlSessionFactoryBean.getObject();
		}
	}