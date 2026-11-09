package com.study.ghostarchive.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GhostDTO {
	private int ghostId;
	private String title;
	private String content;
	private String author;
	private Date createdAt;
	private int hit;
}
