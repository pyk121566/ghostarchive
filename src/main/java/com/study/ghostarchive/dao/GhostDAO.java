package com.study.ghostarchive.dao;

import com.study.ghostarchive.config.spring.RootConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository	//이 클래스가 데이터베이스에 접근하는 역할을 담당한다는 것을 spring 에게 알려주는 어노테이션
public class GhostDAO {
	private final RootConfig rootConfig;
	private final DataSource dataSource;
	
	public GhostDAO(DataSource dataSource, RootConfig rootConfig) {
		this.dataSource = dataSource;
		this.rootConfig = rootConfig;
	}
	
	public void findAll() {
		
		String sql = "SELECT * FROM TB_GHOST ORDER BY GHOST_ID DESC";
		
		try(
				Connection con = dataSource.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				){
			
				while (rs.next()) {
					
					int ghostId = rs.getInt("Ghost_ID");
					String title = rs.getString("TITLE");
					String author = rs.getString("AUTHOR");
					
					System.out.println("괴담 번호: " + ghostId);
					System.out.println("제목: " + title);
					System.out.println("작성자: " + author);
					System.out.println(" ----------------------- ");
				}
				
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

