package com.semi.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


import com.semi.model.vo.PagingQna;
import com.semi.model.vo.Qna;
import com.semi.service.QnaService;

@Controller
public class QnaController {

	@Autowired
	private QnaService service;
	
	private String path = "D:\\upload\\qna\\";
	
	// 파일 업로드 기능 
		public String fileUpload(MultipartFile file) throws IllegalStateException, IOException {
			
	// 중복 방지를 위한 UUID 적용
		UUID uuid = UUID.randomUUID();
		String filename = uuid.toString() + "_" + file.getOriginalFilename();
			
		File copyFile = new File(path + filename);
		file.transferTo(copyFile);  // 업로드한 파일이 지정한 path 위치로 저장
			
			return filename;
		}
		
		@GetMapping("writeQna")
		public String write() {
			return "qna/insertQna";
		}
		
		// 글 등록
		@PostMapping("insertQna")
		public String insertQna(Qna qna) throws IllegalStateException, IOException {
			if(!qna.getFile().isEmpty()) {
			// 파일 업로드 처리 로직
			String url = fileUpload(qna.getFile());
			
			// Board에 url <-- 업로드 된 경로 set으로 넣고 
			qna.setUrl(url);
			
			}
			// 비즈니스 로직 처리 -> service.Boardwrite
			service.insert(qna);
			//System.out.println("추가 후 : " + qna);
			//return "redirect:/view?no=" + qna.getQnaNum();
			//return "redirect:/qna";
			System.out.println(qna.getQnaNum());
			return "redirect:/listQna";
		}
		
		// 리스트 페이징 처리 
		@GetMapping("listQna")
		public String showFilm(Model model, PagingQna paging) {
			
			//System.out.println(paging);
			
			List<Qna> list = service.showAllQna(paging);
			model.addAttribute("list", list);
			model.addAttribute("paging", new PagingQna(paging.getPage(), service.total()));
			
			return "qna/listQna";
		}
		Qna qna= null;
		
	//	리스트에서 제목 누르면 qna 내용 적혀있는 페이지로 넘어가는!
		@GetMapping("viewQna")
		public String view(Model model, String qnaNum) {
			//System.out.println("no : "+ qnaNum );
			int qnanum = Integer.parseInt(qnaNum);
			//System.out.println("qnanum : "  + qnanum);
			qna = service.select(qnanum);
			//System.out.println("qna : " + qna);
			model.addAttribute("qna", qna);
			return "/qna/viewQna";
		}
  
	@GetMapping("updateQna")
	public String updateQna(Model model) {
		model.addAttribute("qna", qna);
		return "qna/updateQna";
	}
		
	@PostMapping("updateQna")
		public String update(Qna qna) throws IllegalStateException, IOException {
			if(!qna.getFile().isEmpty()) {
				if(qna.getUrl()!=null) {
					File file = new File(path+qna.getUrl());
					file.delete();
				}
				String url = fileUpload(qna.getFile());
				qna.setUrl(url);
			}
			service.update(qna);
			return "redirect:/viewQna?qnaNum="+qna.getQnaNum();
		}
  

 @GetMapping("/deleteQna")
	public String delete(String qnaNum) {
		
		int parsingNo = Integer.parseInt(qnaNum);
		
		// 업로드한 파일도 삭제! 필요!!!!
		// 필요한 정보 가져오기
		Qna qna = service.select(parsingNo);
		if(qna.getUrl()!=null) {
			// url이 null이 아닌 경우 정보 삭제!
			File file = new File(path+qna.getUrl());
			file.delete();
		}
		
		// 삭제
		service.delete(parsingNo);
		return "redirect:/listQna";
	} 
  


}