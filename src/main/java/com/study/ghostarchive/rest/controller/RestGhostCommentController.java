package com.study.ghostarchive.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.ghostarchive.dto.GhostCommentDTO;
import com.study.ghostarchive.dto.response.ApiResponse;
import com.study.ghostarchive.exception.BusinessException;
import com.study.ghostarchive.exception.ErrorCode;
import com.study.ghostarchive.service.GhostCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestGhostCommentController {
	
	private final GhostCommentService ghostCommentService;

	@GetMapping("/api/comments/test")
	public ResponseEntity<ApiResponse<String>> test(){
		
		return ResponseEntity.ok(ApiResponse.success("REST API 연결 성공!"));
				
	}
	
	//특정 괴담의 댓글 목록 조회
	@GetMapping("/api/ghosts/{ghostId}/comments")
	public ResponseEntity<ApiResponse<List<GhostCommentDTO>>> getComments(
			@PathVariable("ghostId") int ghostId) {
		
		List<GhostCommentDTO> commentList = ghostCommentService.findByGhostId(ghostId);
		
		return ResponseEntity.ok(ApiResponse.success(commentList));
	}
	
	//댓글 수정 REST API
	@PutMapping("/api/ghosts/{ghostId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<String>> updateComment(
			@PathVariable("ghostId") int ghostId,
			@PathVariable("commentId") int coomentId,
			@RequestBody GhostCommentDTO comment){
		
		comment.setCommentId(coomentId);
		comment.setGhostId(ghostId);	
		
		int result = ghostCommentService.update(comment, ghostId);
		
		if(result == 0) {
			throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);	
		}
		
		return ResponseEntity.ok(ApiResponse.success("댓글 수정 완료"));
	}
	
	//댓글 삭제 REST API
	@DeleteMapping("/api/ghosts/{ghostId}/comments/{commentId}")
	public ResponseEntity<ApiResponse<String>> deleteComment(
			@PathVariable("ghostId") int ghostId,
			@PathVariable("commentId") int commentId) {
		
		int result = ghostCommentService.delete(commentId, ghostId);
		
		if(result == 0) {
			throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
		}
		
		return ResponseEntity.ok(
				ApiResponse.success("댓글 삭제 완료")
				);
	}
	
	//댓글 등록 REST API
	@PostMapping("/api/ghosts/{ghostId}/comments")
	public ResponseEntity<ApiResponse<String>> resgistComment(
			@PathVariable("ghostId") int ghostId,
			@RequestBody GhostCommentDTO comment) {
		
		//URL에서 받은 괴담 번호를 DTO에 저장
		comment.setGhostId(ghostId);
		
		//기존 Service의 댓글 등록 기능 사용
		ghostCommentService.regist(comment);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("댓글 등록 완료"));
	}
	
	
	
}
