package com.cia103g5.user.memreservation.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.common.mail.MailService;
import com.cia103g5.user.availabletime.model.AvailableTimeService;
import com.cia103g5.user.availabletime.model.AvailableTimeVO;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftskill.model.FtSkillService;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.memreservation.model.MemReservationService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem-end/reservations")
public class MemReservationController {

	@Autowired
	private AvailableTimeService availableTimeService;

	@Autowired
	private MemReservationService memReservationService;

	@Autowired
	private FtSkillService ftSkillService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MailService mailService;

	@GetMapping("/check-session")
	public ResponseEntity<?> checkSession(HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (sessionMember == null) {
			// 未登入返回 401 狀態碼
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// 已登入返回 200 OK
		return ResponseEntity.ok().build();
	}

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addReservation(@RequestParam Integer availableTimeNo,
			@RequestParam Integer price, @RequestParam(required = false) Integer skillNo, HttpSession session,
			Model model) {
		// 檢查是否登入
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");

		if (sessionMember == null) {
			// 未登入時返回 401 狀態碼和提示訊息
			Map<String, String> response = new HashMap<>();
			response.put("message", "Authentication required. Please log in to make a reservation.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}

		try {

			Integer memberId = sessionMember.getMemberId();

			// 1. 獲取會員資訊
			MemberVO member = memberService.getById(memberId);
			String name = member.getName();

			// 2. 獲取占卜師資訊
			FtVO ft = availableTimeService.getFtVOByAvailableTimeNo(availableTimeNo); // 獲取占卜師資訊
			Integer ftId = ft.getFtId();
			String companyName = ft.getCompanyName();
			String nickname = ft.getNickname();

			// 3. 獲取預約時間
			AvailableTimeVO timeSlot = availableTimeService.getAvailableTimeById(availableTimeNo); // 獲取預約時間段
			DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");
			DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH:mm");

			// 4. 格式化日期
			String formattedStartDate = timeSlot.getStartTime().format(formatterD);
			String formattedStartTime = timeSlot.getStartTime().format(formatterT);
			String formattedEndTime = timeSlot.getEndTime().format(formatterT);

			// 5. 獲取技能名稱
			String skillName = skillNo != null ? ftSkillService.getFtSkillNameById(skillNo) : "一般占卜";

			// 6. 插入預約
			memReservationService.addReservation(memberId, ftId, availableTimeNo, price, skillNo);

			// 7. 更新 availableTime 狀態為 3（保留中）
			availableTimeService.updateStatusByMem(availableTimeNo, 3);

			// 8. 發送通知信
			String to = "mp20136@gmail.com"; // cia103.g5@gmail.com
			String subject = "付款通知";
			String messageText = String.format(
					"\n親愛的 %s ，您好：\n\n" + "感謝您使用我們的占卜服務。\n\n" + "付款資訊如下：\n\n" + "- 公司名稱：%s\n" + "- 占卜師：%s\n"
							+ "- 占卜日期：%s\n" + "- 占卜時間：%s - %s\n" + "- 預約項目：%s\n" + "- 應付金額：NT$ %d\n\n"
							+ "請於 4 小時內完成付款，以確保您的預約有效。\n\n" + "如有任何問題，請聯絡我們的客服團隊。\n\n" + "敬祝順心\n			-修卜人生媒合平台\n",
					name, companyName, nickname, formattedStartDate, formattedStartTime, formattedEndTime, skillName,
					price);
			mailService.sendSimpleEmail(to, subject, messageText);

			return ResponseEntity.ok(Map.of("status", "success"));

		} catch (Exception e) {

			return ResponseEntity.ok(Map.of("status", "error", "message", "系統錯誤"));
		}

	}

}
