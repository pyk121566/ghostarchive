package com.study.ghostarchive.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GhostCommentDTO {

	private int commentId;	//댓글 번호
	private int ghostId;	//괴담 게시글 번호
	private String author;		//댓글 작성자
	private String content;	//댓글 내용
	private Date createdAt;	//댓글 작성일
}
