package com.cia103g5.user.ftlist.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.ftgrade.model.FtGrade;
import com.cia103g5.user.ftgrade.model.FtGradeService;
import com.cia103g5.user.ftlist.model.FtDTO;
import com.cia103g5.user.ftlist.model.FtListService;
import com.cia103g5.user.ftskill.model.FtSkillService;
import com.cia103g5.user.ftskill.model.FtSkillVO;
import com.cia103g5.user.member.dto.SessionMemberDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ftslist")
public class FtListController {

	@Autowired
	private FtListService ftListService;

	@Autowired
	private FtSkillService ftSkillService;

	@Autowired
	private FtGradeService ftGradeService;

	@GetMapping
	public String getAllFts(@RequestParam(value = "keyword", required = false) String keywordParam,
			@RequestParam(value = "nickname", required = false) String nicknameParam,
			@RequestParam(value = "skillNo", required = false) String skillNoParam,
			@RequestParam(value = "minPrice", required = false) String minPriceParam,
			@RequestParam(value = "maxPrice", required = false) String maxPriceParam,
			@RequestParam(value = "startDate", required = false) String startDateParam,
			@RequestParam(value = "ftRank", required = false) String ftRankParam, Model model, HttpSession session) {
		
		// 檢查 session 是否有會員資訊，非會員也可以使用
	    Integer memberId = null;
	    SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
	    if (sessionMember != null) {
	        memberId = sessionMember.getMemberId();
	    }
		
		// 初始化查詢條件變數
		String keyword = null;
		String nickname = null;
		Integer skillNo = null;
		Integer minPrice = null;
		Integer maxPrice = null;
		Integer ftRank = null;
		LocalDate startDate = null;

		// 檢查並處理查詢參數，將其轉換為相應的類型
		if (keywordParam != null && !keywordParam.isEmpty()) {
			keyword = keywordParam;
		}
		if (nicknameParam != null && !nicknameParam.isEmpty()) {
			nickname = nicknameParam;
		}
		if (skillNoParam != null && !skillNoParam.isEmpty()) {
			skillNo = Integer.parseInt(skillNoParam);
		}
		if (ftRankParam != null && !ftRankParam.isEmpty()) {
			ftRank = Integer.parseInt(ftRankParam);
		}
		if (startDateParam != null && !startDateParam.isEmpty()) {
			startDate = LocalDate.parse(startDateParam);
		}
		if (minPriceParam != null && !minPriceParam.isEmpty()) {
			minPrice = Integer.parseInt(minPriceParam);
		}
		if (maxPriceParam != null && !maxPriceParam.isEmpty()) {
			maxPrice = Integer.parseInt(maxPriceParam);
		}

		// 把處理過後的查詢參數傳遞給 model 以便在前端頁面顯示
		model.addAttribute("keyword", keyword);
		model.addAttribute("nickname", nickname);
		model.addAttribute("skillNo", skillNo);
		model.addAttribute("ftRank", ftRank);
		model.addAttribute("startDate", startDate);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);

		// 查詢所有專長選項並添加到 model
		List<FtSkillVO> ftSkills = ftSkillService.getAllFtSkills();
		model.addAttribute("ftSkillOptions", ftSkills);

		// 根據查詢條件呼叫 Service 查詢符合的占卜師列表
		List<FtDTO> fts = ftListService.getAllFts(memberId, keyword, nickname, skillNo, startDate, minPrice, maxPrice, ftRank);
		model.addAttribute("fts", fts);

		return "ftslist";

	}

	@GetMapping("/{ftId}")
	public String getFtById(@PathVariable Integer ftId, Model model, HttpSession session) {
		
		// 檢查 session 是否有會員資訊，非會員也可以使用
	    Integer memberId = null;
	    SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
	    if (sessionMember != null) {
	        memberId = sessionMember.getMemberId();
	    }

		// 根據占卜師 ID 查詢詳細資訊
		FtDTO ft = ftListService.getFtById(memberId, ftId);
		model.addAttribute("ft", ft);
		return "ftpage";

	}
	
	// 將共享數據列表 (List<VO>) 添加到 model
	@ModelAttribute("ftGradeOptions")
	protected List<FtGrade> referenceListData(Model model) {
		// 創建一個 FtGrade 並添加到 model 進行表單綁定
		model.addAttribute("ftGrade", new FtGrade());
		// 從 Service 獲取所有的 FtGrade
		List<FtGrade> list = ftGradeService.getAll();
		return list; 
	}

}
