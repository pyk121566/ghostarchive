package com.study.ghostarchive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.ghostarchive.dto.GhostCommentDTO;
import com.study.ghostarchive.mapper.GhostCommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GhostCommentService {

	private final GhostCommentMapper ghostCommentMapper;
	
	//특정 괴담의 댓글 목록 조회
	public List<GhostCommentDTO> findByGhostId(int ghostId){
		return ghostCommentMapper.findByGhostId(ghostId);
	}
	
	//댓글 등록
	public int regist(GhostCommentDTO comment) {
		return ghostCommentMapper.insert(comment);
	}
	
	//댓글 삭제 
	public int delete(int commentId) {
		return ghostCommentMapper.delete(commentId);
	}
	
	//댓글 수정
	public int update(GhostCommentDTO comment) {
		return ghostCommentMapper.update(comment);
	}
}
