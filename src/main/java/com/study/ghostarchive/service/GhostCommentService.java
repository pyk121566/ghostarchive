package com.study.ghostarchive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.study.ghostarchive.dto.GhostCommentDTO;
import com.study.ghostarchive.exception.BusinessException;
import com.study.ghostarchive.exception.ErrorCode;
import com.study.ghostarchive.mapper.GhostCommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GhostCommentService {

	private final GhostCommentMapper ghostCommentMapper;
	
	//특정 괴담의 댓글 목록 조회(페이지네이션)
	//오버로딩이라 이름이 같아도 됨
	public List<GhostCommentDTO> findByGhostId(int ghostId, String sort, int page, int size){
		
		int offset = (page-1) * size;
		
		return ghostCommentMapper.findByGhostIdPage(ghostId, sort, offset, size);
	}
	
	//특정 괴담의 전체 댓글 개수 조회
	public int countByGhostId(int ghostId) {
		return ghostCommentMapper.countByGhostId(ghostId);
	}
	
	//댓글 등록
	public int regist(GhostCommentDTO comment) {
		
		//작성자 검사
		if(comment.getAuthor() == null || comment.getAuthor().trim().isEmpty()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT);
		}
		
		//앞뒤 공백 제거
		String author = comment.getAuthor().trim();
		
		//작성자 길이 검사
		if(author.length() > 50) {
			throw new IllegalArgumentException("작성자는 50자 이내로 입력해 주세요.");
		}
		
		//정리된 작성자 이름을 DTO에 반영
		comment.setAuthor(author);
		
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
