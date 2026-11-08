[1mdiff --git a/.gitignore b/.gitignore[m
[1mindex b970ad2..c018ea1 100644[m
[1m--- a/.gitignore[m
[1m+++ b/.gitignore[m
[36m@@ -21,4 +21,7 @@[m [mThumbs.db[m
 *.log[m
 [m
 # Local DB credentials[m
[31m-src/main/resources/db.properties[m
\ No newline at end of file[m
[32m+[m[32msrc/main/resources/db.properties[m
[32m+[m
[32m+[m[32m# Local database configuration[m
[32m+[m[32m/src/main/resources/db.properties[m
\ No newline at end of file[m
[1mdiff --git a/src/main/java/com/study/ghostarchive/config/spring/RootConfig.java b/src/main/java/com/study/ghostarchive/config/spring/RootConfig.java[m
[1mindex e95aaeb..57dc99b 100644[m
[1m--- a/src/main/java/com/study/ghostarchive/config/spring/RootConfig.java[m
[1m+++ b/src/main/java/com/study/ghostarchive/config/spring/RootConfig.java[m
[36m@@ -1,5 +1,8 @@[m
 package com.study.ghostarchive.config.spring;[m
 [m
[32m+[m[32mimport java.io.IOException;[m
[32m+[m[32mimport java.util.Properties;[m
[32m+[m
 import javax.sql.DataSource;[m
 [m
 import org.apache.ibatis.session.SqlSessionFactory;[m
[36m@@ -20,20 +23,28 @@[m [mpublic class RootConfig {[m
 [m
 	//1. Oracle DB 연결 설정[m
 	@Bean[m
[31m-	public DataSource dataSource() {[m
[31m-	[m
[31m-		HikariConfig config = new HikariConfig();	//HikariCP 의 설정 정보를 담는 객체[m
[32m+[m	[32mpublic DataSource dataSource() throws IOException {[m
[32m+[m
[32m+[m		[32m//db.properties 파일 열기[m
[32m+[m		[32mProperties props = new Properties();[m
 		[m
[31m-		config.setDriverClassName("oracle.jdbc.OracleDriver");[m
[32m+[m		[32mClassPathResource resource = new ClassPathResource("db.properties");[m
 		[m
[31m-		config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/XEPDB1");	//Oracle DB에 접속할 주소를 설정하는 코드[m
[32m+[m		[32mtry(java.io.InputStream input = resource.getInputStream()){[m
[32m+[m			[32mprops.load(input);[m
[32m+[m			[32m}[m[41m	[m
 		[m
[31m-		config.setUsername("GHOST");[m
[31m-		config.setPassword("1234");[m
[32m+[m		[32m//HikariCP 설정[m
[32m+[m		[32mHikariConfig config = new HikariConfig();[m
 		[m
[32m+[m		[32mconfig.setDriverClassName(props.getProperty("db.driver"));[m
[32m+[m		[32mconfig.setJdbcUrl(props.getProperty("db.url"));[m
[32m+[m		[32mconfig.setUsername(props.getProperty("db.username"));[m
[32m+[m		[32mconfig.setPassword(props.getProperty("db.password"));[m
[32m+[m[41m			[m
 		return new HikariDataSource(config);[m
 	}[m
[31m-	[m
[32m+[m[41m		[m
 	//2. Mybatis 설정[m
 	@Bean[m
 	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{[m
