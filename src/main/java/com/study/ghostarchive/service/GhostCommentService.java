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
		
		//작성자 검사
		if(comment.getAuthor() == null || comment.getAuthor().trim().isEmpty()) {
			throw new IllegalArgumentException("작성자를 입력해 주세요.");
		}
		
		// 작성자 길이 검사
		if(comment.getAuthor().length() > 50) {
			throw new IllegalArgumentException("작성자는 50자 이내로 입력해 주세요.");
		}
		
		//댓글 내용 검사
		if(comment.getContent() == null || comment.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("댓글 내용을 입력해 주세요.");
		}
		
		// 댓글 길이 검사
		if(comment.getContent().length() > 1000) {
			throw new IllegalArgumentException("댓글은 1000자 이내로 입력해 주세요.");
		}
		
		return ghostCommentMapper.insert(comment);
	}
	
	//댓글 삭제 
	public int delete(int commentId, int ghostId) {
		return ghostCommentMapper.delete(commentId, ghostId);
	}
	
	//댓글 수정
	public int update(GhostCommentDTO comment, int ghostId) {
		
		if(comment.getContent() == null || comment.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("댓글 내용을 입력해 주세요.");
		}
		
		if(comment.getContent().length() > 1000) {
			throw new IllegalArgumentException("댓글은 1000자 이내로 입력해 주세요.");
		}
		
		return ghostCommentMapper.update(comment, ghostId);
	}
}
