package com.cia103g5.user.ftskill.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.ftskill.model.FtSkillService;

@Controller
@RequestMapping("/back-end/ftskills")
public class BackFtSkillController {

	@Autowired
	private FtSkillService ftSkillService;

	// 顯示所有 FtSkillVO 資料
	@GetMapping
	public String getAllFtSkills(Model model) {
		model.addAttribute("ftSkills", ftSkillService.getAllFtSkills());
		return "back-end/ftskillslist"; // 顯示技能列表的頁面
	}

	// 新增專長
	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addFtSkill(@RequestParam String skillName) {
		
		if (ftSkillService.isSkillNameExists(skillName)) {
	        return ResponseEntity.ok(Map.of("status", "error", "message", "專長名稱已存在，請重新輸入。"));
	    }
		
		ftSkillService.addFtSkill(skillName);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	// 編輯專長
	@PostMapping("/edit/{skillNo}")
	public ResponseEntity<Map<String, String>> editFtSkill(@PathVariable Integer skillNo,
			@RequestBody Map<String, String> body) {
		String newSkillName = body.get("skillName");
		ftSkillService.updateSkillName(skillNo, newSkillName);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	// 刪除專長
	@DeleteMapping("/delete/{skillNo}")
	public ResponseEntity<Map<String, String>> deleteFtSkill(@PathVariable Integer skillNo) {
		ftSkillService.deleteFtSkillBySkillNo(skillNo);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

}
