package com.study.ghostarchive.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.study.ghostarchive.common.web.Pager;
import com.study.ghostarchive.dto.GhostCommentDTO;
import com.study.ghostarchive.dto.GhostDTO;
import com.study.ghostarchive.exception.BusinessException;
import com.study.ghostarchive.service.GhostCommentService;
import com.study.ghostarchive.service.GhostService;

@Controller	//이 클래스가 웹요청을 처리하는 Controller 라는 사실을 알려줌
public class GhostController {
	
	private final GhostService ghostService;	//컨트롤러에서 GhostDAO를 사용하기위해 멤버변수로 선언
	private final GhostCommentService ghostCommentService;
	
	//생성자를 통한 의존성 주입
	public GhostController(GhostService ghostService, 
									GhostCommentService ghostCommentService) {
		this.ghostService = ghostService;
		this.ghostCommentService=ghostCommentService;
	}
	
	//전체 괴담 목록 조회 + 제목 검색 + 페이징
	@GetMapping("/ghosts")		//브라우저에서 /ghost 주소로 GET 요청이 들어오면 아래의 list() 메서드를 실행하도록 연결
	public String getGhostList(@RequestParam(value = "keyword", required=false)String keyword, 
				HttpServletRequest request ,Model model) {
		
		//1.Pager 객체 생성
		Pager pager= new Pager();
		
		List<GhostDTO> ghostList;
		
		if(keyword == null || keyword.trim().isEmpty()) {
			
			//2. 전체 괴담 개수 설정
			pager.setTotalCount(ghostService.count());
			
			//3.페이지 정보 계산
			pager.init(request);
			
			//4.현재 페이지에 해당하는 괴담 조회
			ghostList = ghostService.selectByPage(pager);
			
		}else {
		
			//검색 기능은 기존 방식 유지
			List<GhostDTO> searchList=ghostService.searchByTitle(keyword.trim());
			
			pager.setTotalCount(searchList.size());
			pager.init(request);
			
			int start = Math.min(pager.getOffset(), searchList.size());
			int end = Math.min(start + pager.getPageSize(), searchList.size());
			
			ghostList = searchList.subList(start, end);
		}
		
		//5.Pager 에 목록 저장
		pager.setList(ghostList);
		
		//6.JSP에 데이터 전달
		model.addAttribute("ghostList",ghostList);
		model.addAttribute("pager", pager);
		model.addAttribute("keyword",keyword);
		
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
			
			//조회수 1 증가
			ghostService.increaseHit(ghostId);	
		
			//증가된 조회수 반영
			ghost.setHit(ghost.getHit()+1);
			
			//JSP에 전달
			model.addAttribute("ghost", ghost);
			
			// 해당 괴담의 댓글 조회
			List<GhostCommentDTO> commentList = ghostCommentService.findByGhostId(ghostId);
			
			//JSP에 댓글 목록 전달
			model.addAttribute("commentList",commentList);
			
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
		@GetMapping("/ghosts/{ghostId}/edit")
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
		
		//괴담 삭제 처리
		@PostMapping("/ghosts/{ghostId}/delete")
		public String deleteGhost(@PathVariable("ghostId") int ghostId) {
			
			//Service 룰 통해 괴담 삭제
			ghostService.delete(ghostId);
			
			//삭제 후 목록 페이지로 이동
			return "redirect:/ghosts";
		}
		
		//댓글 등록
		@PostMapping("/ghosts/{ghostId}/comments")
		public String registComment(@PathVariable("ghostId") int ghostId, 
													@ModelAttribute GhostCommentDTO comment,
												//RedirectAttributes : 리다이렉트 한 다음 페이지에 데이터를 전달할때 사용하는 Spring MVC
													RedirectAttributes redirectAttributes) {	
			comment.setGhostId(ghostId);
			
			try {
				ghostCommentService.regist(comment);
				
				//댓글 등록 성공 메시지
				redirectAttributes.addFlashAttribute("successMessage","댓글이 등록되었습니다.");
			
			}catch(BusinessException e) {
				redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
			}
						
			return "redirect:/ghosts/" + ghostId;
		}
		
		//댓글 삭제
		@PostMapping("/ghosts/{ghostId}/comments/{commentId}/delete")
		public String deleteComment(@PathVariable("ghostId") int ghostId,
												@PathVariable("commentId") int commentId,
												RedirectAttributes redirectAttributes) {
			
			int result=ghostCommentService.delete(commentId, ghostId);
			
			if(result > 0) {
				redirectAttributes.addFlashAttribute("successMessage","댓글이 삭제되었습니다.");
			}else {
				redirectAttributes.addFlashAttribute("errorMessage","삭제할 댓글이 없습니다.");
			}
			
			return "redirect:/ghosts/"+ghostId;
		}

		//댓글 수정
		@PostMapping("/ghosts/{ghostId}/comments/{commentId}/edit")
		public String updateComment(
				@PathVariable("ghostId") int ghostId,
				@PathVariable("commentId") int commentId,
				@RequestParam("content") String content,
				RedirectAttributes redirectAttributes) {
			
			GhostCommentDTO comment = new GhostCommentDTO();
			
			comment.setCommentId(commentId);
			comment.setContent(content);
			
			try {
				int result = ghostCommentService.update(comment, ghostId);
				
				if(result > 0) {
					redirectAttributes.addFlashAttribute("successMessage", "댓글이 수정되었습니다.");
				}else {
					redirectAttributes.addFlashAttribute("errorMessage","수정할 댓글이 없습니다.");
				}
				
				}catch(IllegalArgumentException e){
					redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
				}
				
				
			
			return "redirect:/ghosts/" + ghostId;
		}
}