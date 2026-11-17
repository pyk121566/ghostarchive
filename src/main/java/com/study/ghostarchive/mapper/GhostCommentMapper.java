package com.study.ghostarchive.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.ghostarchive.dto.GhostCommentDTO;

@Mapper
public interface GhostCommentMapper {

	//특정 괴담의 댓글 페이지별 조회
	List<GhostCommentDTO> findByGhostIdPage(
			@Param("ghostId") int ghostId,
			@Param("sort") String sort,
			@Param("offset") int offset,
			@Param("size") int size);
	
	//특정 괴담의 전체 댓글 개수 조회
	int countByGhostId(@Param("ghostId") int ghostId);
	
	//댓글 등록
	int insert(GhostCommentDTO comment);
	
	//댓글 삭제
	int delete(@Param("commentId") int commentId,
					@Param("ghostId") int ghostId);
	
	//댓글 수정
	int update(@Param("comment") GhostCommentDTO comment,
					@Param("ghostId") int ghostId);
}
