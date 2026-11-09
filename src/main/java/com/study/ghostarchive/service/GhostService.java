package com.study.ghostarchive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.ghostarchive.common.web.Pager;
import com.study.ghostarchive.dto.GhostDTO;
import com.study.ghostarchive.mapper.GhostMapper;

@Service
public class GhostService {

	private final GhostMapper ghostMapper;	//Mapper을 사용하기 위해 멤버변수로 선언
	
	//생성자를 통한 의존성 주입
	public GhostService(GhostMapper ghostMapper) {
		this.ghostMapper = ghostMapper;
	}
	
	//전체 괴담 목록 조회
	public List<GhostDTO> findAll(){
		return ghostMapper.findAll();
	}
	
	//특정 괴담 상세 조회
	public GhostDTO findById(int ghostId) {
		return ghostMapper.findById(ghostId);
	}
	
	//괴담 등록
	public int insert(GhostDTO ghost) {
		return ghostMapper.insert(ghost);
	}
	
	//괴담 수정
	public int update(GhostDTO ghost) {
		return ghostMapper.update(ghost);
	}
	
	//괴담 삭제
	public int delete(int ghostId) {
		return ghostMapper.delete(ghostId);
	}
	
	//괴담 검색
	public List<GhostDTO> searchByTitle(String keyword){
		return ghostMapper.searchByTitle(keyword);
	}
	
	//전체 괴담 개수 조회
	public int count() {
		return ghostMapper.count();
	}
	
	//페이징된 괴담 목록 조회
	public List<GhostDTO> selectByPage(Pager pager){
		return ghostMapper.selectByPage(pager);
	}
}
