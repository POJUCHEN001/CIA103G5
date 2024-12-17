package com.cia103g5.user.favft.controller;

import com.cia103g5.user.favft.model.FavFtService;
import com.cia103g5.user.ftlist.model.FtDTO;
import com.cia103g5.user.ftlist.model.FtListService;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mem-end/favfts")
public class MemFavFtController {

	@Autowired
	private FavFtService favFtService;

	@Autowired
	private FtListService ftListService;

	@GetMapping
	public String getAllFts(HttpSession session, Model model) {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();

		// 驗證會員是否存在
		if (sessionMember != null) {
			String memberName = sessionMember.getName();

			model.addAttribute("memberName", memberName);
		}

		List<FtDTO> fts = ftListService.getFtByFavFts(memberId);
		model.addAttribute("fts", fts);

		return "mem-end/favfts/list";
	}

	@GetMapping("/checkLogin")
	public ResponseEntity<Map<String, Boolean>> checkLogin(HttpSession session) {
		Map<String, Boolean> response = new HashMap<>();

		Boolean isLogin = Boolean.TRUE.equals(session.getAttribute("isLogin"));
		response.put("isLoggedIn", isLogin);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/toggleFavorite")
	public ResponseEntity<Map<String, String>> toggleFavorite(HttpSession session, @RequestParam("ftId") Integer ftId,
			@RequestParam("action") String action) {

		// 檢查是否有登入的會員資訊
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		if (sessionMember == null) {

			// 非會員，返回 401 Unauthorized 並提示需要登入
			Map<String, String> response = new HashMap<>();

			response.put("message", "請先登入以使用收藏功能！");
			response.put("redirectUrl", "/login"); // 指定跳轉的登入頁面 URL
			response.put("reloadCurrentPage", "true"); // 加入一個標誌，指示前端重新加載當前頁面
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		// 獲取會員 ID
		Integer memberId = sessionMember.getMemberId();

		if (action.equals("add")) {
			favFtService.addFavoriteFt(memberId, ftId);
		} else if (action.equals("delete")) {
			favFtService.deleteFavoriteFt(memberId, ftId);
		}
		return ResponseEntity.ok().build();
	}

}
