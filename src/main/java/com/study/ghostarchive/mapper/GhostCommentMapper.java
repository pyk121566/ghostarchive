package com.study.ghostarchive.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.study.ghostarchive.dto.GhostCommentDTO;

@Mapper
public interface GhostCommentMapper {

	//특정 괴담에 작성된 댓글 목록 조회
	List<GhostCommentDTO> findByGhostId(int ghostId);	//하나의 괴담에 여러가지 댓글이 달릴 수 있으니까 List로

	//댓글 등록
	int insert(GhostCommentDTO comment);
	
	//댓글 삭제
	int delete(@Param("commentId") int commentId,
					@Param("ghostId") int ghostId);
	
	//댓글 수정
	int update(@Param("comment") GhostCommentDTO comment,
					@Param("ghostId") int ghostId);
}
