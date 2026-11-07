package com.study.ghostarchive.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.study.ghostarchive.dto.GhostDTO;
import com.study.ghostarchive.service.GhostService;

@Controller	//이 클래스가 웹요청을 처리하는 Controller 라는 사실을 알려줌
public class GhostController {
	
	private final GhostService ghostService;	//컨트롤러에서 GhostDAO를 사용하기위해 멤버변수로 선언
	
	//생성자를 통한 의존성 주입
	public GhostController(GhostService ghostService) {
		this.ghostService = ghostService;
	}
	
	//전체 괴담 목록 조회
	@GetMapping("/ghosts")		//브라우저에서 /ghost 주소로 GET 요청이 들어오면 아래의 list() 메서드를 실행하도록 연결
	public String getGhostList(Model model) {	//model은 Controller 에서 JSP 로 데이터를 전달할 때 사용하는 객체

		//Service 를 통해 괴담 목록 조회
		List<GhostDTO> ghostList = ghostService.findAll();
		
		//JSP에 데이터 전달
		model.addAttribute("ghostList",ghostList);
		
		return "/ghost/list";
	}
	
		//특정 괴담 상세 조회
		@GetMapping("/ghosts/{ghostId}")
		public String getGhostDetail(@PathVariable("ghostId") int ghostId, Model model) {
			
			// Service 를 통해 해당 괴담 조회
			GhostDTO ghost = ghostService.findById(ghostId);
			
			//해당 번호의 괴담이 없는 경우
			if(ghost == null) {
				return "redirect:/ghosts";
			}
		
			//JSP에 괴담 한 개 전달
			model.addAttribute("ghost", ghost);
			
			// 상세 페이지로 이동
			return "/ghost/detail";
	}
		
		//괴담 등록 폼 보여주기
		@GetMapping("/ghosts/new")
		public String getGhostForm() {
			return "/ghost/form";
		}
		
		//괴담 등록 처리
		@PostMapping("/ghosts")
		public String insertGhost(@ModelAttribute GhostDTO ghost) {
			
			System.out.println("제목 : "+ghost.getTitle());
			System.out.println("작성자 : "+ghost.getAuthor());
			System.out.println("내용 : "+ghost.getContent());
			
			
			//Service 를 통해 Oracle DB 에 저장
			ghostService.insert(ghost);
			
			//등록 후 목록 페이지로 이동
			return "redirect:/ghosts";
		}
		
		//괴담 수정 폼 보여주기
		@GetMapping("/ghost/{ghostId}/edit")
		public String getEditForm(@PathVariable("ghostId") int ghostId, Model model) {
			
			//기존 괴담 정보 조회
			GhostDTO ghost = ghostService.findById(ghostId);
			
			if(ghost ==null) {
				return "redirect:/ghosts";
			}
			
			//수정 폼으로 데이터 전달
			model.addAttribute("ghost", ghost);
			
			return "/ghost/edit";
		}
		
		//괴담 수정 처리
		@PostMapping("/ghosts/{ghostId}/edit")
		public String updateGhost(@PathVariable("ghostId") int ghostId, @ModelAttribute GhostDTO ghost) {
			
			//URL 에서 받은 괴담 번호 설정
			ghost.setGhostId(ghostId);
			
			//DB 수정
			ghostService.update(ghost);
			
			//수정한 괴담의 상세 페이지로 이동
			return "redirect:/ghosts/" + ghostId;
		}


	}
