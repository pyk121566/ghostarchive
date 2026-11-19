package com.study.ghostarchive.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ghostarchive.dto.response.ApiResponse;
import com.study.ghostarchive.service.GhostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestGhostLikeController {

	private final GhostService ghostService;
	
	//특정 괴담 좋아요 증가
	@PostMapping("/api/ghosts/{ghostId}/likes")
	public ResponseEntity<ApiResponse<Integer>> increaseLike(@PathVariable("ghostId") int ghostId){
		
		int result = ghostService.increaseLikeCount(ghostId);	//좋아요의 개수가 아니라 update 로 수정된 행의 개수
		
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
