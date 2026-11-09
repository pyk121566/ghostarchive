package com.study.ghostarchive.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Pager {

	List list;
	
	int totalCount;	//전체 게시글의 수 
	int pageSize=10;		//한 페이지에 표시할 게시글 수
	int totalPage;		//전체 페이지의 수
	int blockSize = 10;
	int currentPage = 1;	//현재 페이지 번호
	int firstPage;
	int lastPage;
	int num;
	int offset;
	
	public void init(HttpServletRequest request) {
		
		//1.전체 페이지 수 계산
		totalPage = (int) Math.ceil((float) totalCount / pageSize);
		
		//2.사용자가 선택한 페이지 번호 확인
		if(request.getParameter("currentPage") != null) {
			
			//3.문자열을 정수로 변환
		try {	currentPage = Integer.parseInt(request.getParameter("currentPage"));
			
		}catch (NumberFormatException e ) {
			currentPage =1;
		}
		}
			// 페이지 번호가 1보다 작으면 1페이지로 변경 (잘못된 페이지 번호 보정)
			if(currentPage <1) {
				currentPage=1;
			}
			
			//전체 페이지 수를 초과하면 마지막 페이지로 변경
			if(totalPage >0 && currentPage > totalPage) {
				currentPage = totalPage;
			}
			
			//4. 현재 페이지 블록의 시작 번호
			firstPage = currentPage - (currentPage -1) % blockSize;
			
		
		
		//5.현재 페이지 블록의 마지마 번호
		lastPage = firstPage + (blockSize -1);
		
		//6.DB 조회 시 건너뛸 게시글 수
		offset = (currentPage -1) * pageSize;
		
		//7.화면에 표시할 게시글 시작 번호
		num = totalCount - offset;
	}
	
	
}
