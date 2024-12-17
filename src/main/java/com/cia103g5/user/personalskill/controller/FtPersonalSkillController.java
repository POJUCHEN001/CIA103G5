package com.cia103g5.user.personalskill.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.ftskill.model.FtSkillService;
import com.cia103g5.user.ftskill.model.FtSkillVO;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.personalskill.model.PersonalSkillRepository;
import com.cia103g5.user.personalskill.model.PersonalSkillService;
import com.cia103g5.user.personalskill.model.PersonalSkillVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ft-end/personalskills")
public class FtPersonalSkillController {

	@Autowired
	private PersonalSkillService personalSkillService;

	@Autowired
	private FtSkillService ftSkillService;
	
	@Autowired
    private PersonalSkillRepository personalSkillRepository;

	// 顯示該占卜師所有 FtSkillVO 資料
	@GetMapping
	public String getAllFtSkills(Model model, HttpSession session) {
		
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		
		// 獲取該占卜師已有的技能
		List<FtSkillVO> existingFtSkills = personalSkillService.getFtSkillsByFtId(loggedInMember.getFtId());
		List<Integer> existingSkillNos = existingFtSkills.stream()
				.map(ftSkill -> ftSkill.getSkillNo())
				.toList();
		// 篩選出尚未添加的技能
		List<FtSkillVO> availableFtSkills = ftSkillService.getAllFtSkills().stream()
				.filter(ftSkill -> !existingSkillNos.contains(ftSkill.getSkillNo()))
				.toList();
		model.addAttribute("ftSkills", existingFtSkills);
		model.addAttribute("ftSkillOptions", availableFtSkills);
		return "ft-end/personalskills/list"; // 顯示技能列表的頁面
	}

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addFtSkill(@RequestParam Integer skillNo, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getFtId();
		personalSkillService.addPersonalSkill(ftId, skillNo);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@DeleteMapping("/delete/{skillNo}")
	public ResponseEntity<Map<String, String>> deleteFtSkill(@PathVariable Integer skillNo, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getFtId();
		personalSkillService.deletePersonalSkill(ftId, skillNo);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@GetMapping("/{ftId}")
    public ResponseEntity<List<FtSkillVO>> getPersonalSkills(@PathVariable Integer ftId) {
        // 查詢指定占卜師的技能關聯
        List<PersonalSkillVO> personalSkills = personalSkillRepository.findByFtVO_FtId(ftId);

        if (personalSkills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Collections.emptyList());
        }

        // 提取 FtSkillVO 直接返回
        List<FtSkillVO> skills = personalSkills.stream()
                .map(PersonalSkillVO::getFtSkillVO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(skills);

    }
	
}
