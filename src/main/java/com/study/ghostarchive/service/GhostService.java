package com.study.ghostarchive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.ghostarchive.common.web.Pager;
import com.study.ghostarchive.dto.GhostDTO;
import com.study.ghostarchive.exception.BusinessException;
import com.study.ghostarchive.exception.ErrorCode;
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
		
		//제목 필수 입력 검사
		if(ghost.getTitle() == null || ghost.getTitle().trim().isEmpty()){
			throw new BusinessException(ErrorCode.INVALID_INPUT);
		}
		
		//제목 길이 검사
		if(ghost.getTitle().length() >200) {
			throw new BusinessException(ErrorCode.INVALID_INPUT);
		}
		
		//괴담 내용 검사
		if(ghost.getContent()==null|| ghost.getContent().trim().isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT);
		}
		
		//작성자 검사
		if(ghost.getAuthor()==null|| ghost.getAuthor().trim().isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT);
		}
		
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
	
	//괴담 조회수 증가
	public int increaseHit(int ghostId) {
		return ghostMapper.increaseHit(ghostId);
	}
}
