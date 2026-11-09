package com.study.ghostarchive.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.ghostarchive.common.web.Pager;
import com.study.ghostarchive.dto.GhostDTO;

@Mapper
//Mybatis 에서 Mapper는 java 메서드와 SQL 문을 연결하는 역할을 함
public interface GhostMapper {

		//전체 괴담 목록 조회
	List<GhostDTO> findAll();
	
		//괴담 한 개 조회
	GhostDTO findById(int ghostId);
	
		//괴담 등록
	int insert(GhostDTO ghost);
	
		//괴담 수정
	int update(GhostDTO ghost);
	
		//괴담 삭제
	int delete(int ghostId);
	
		//괴담 검색
	List<GhostDTO> searchByTitle(String keyword);	//List로 반환하는 이유 : 검색 결과가 한개 이상 나올 수 있기 때문에
	
		//전체 괴담 개수 조회
	int count();
	
		//페이징된 괴담 목록 조회
	List<GhostDTO> selectByPage(Pager pager);
	
	}
