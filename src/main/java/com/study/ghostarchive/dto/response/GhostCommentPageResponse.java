package com.study.ghostarchive.dto.response;

import java.util.List;

import com.study.ghostarchive.dto.GhostCommentDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GhostCommentPageResponse {

		//현재 페이지의 댓글 목록
		private List<GhostCommentDTO> comments;
		
		//전체 댓글 개수
		private int totalCount;
		
		//현재 페이지 번호
		private int currentPage;
		
		//페이지당 댓글 개수
		private int pageSize;
		
		//전체 페이지 수
		private int totalPages;
}
