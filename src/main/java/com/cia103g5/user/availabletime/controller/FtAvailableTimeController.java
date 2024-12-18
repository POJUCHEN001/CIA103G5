package com.cia103g5.user.availabletime.controller;

import com.cia103g5.common.mail.MailService;
import com.cia103g5.user.availabletime.model.AvailableTimeService;
import com.cia103g5.user.availabletime.model.AvailableTimeVO;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftlist.model.FtListService;
import com.cia103g5.user.ftskill.model.FtSkillService;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.memreservation.model.MemReservationService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ft-end/availabletimes")
public class FtAvailableTimeController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private FtListService ftListService;

	@Autowired
	private AvailableTimeService availableTimeService;

	@Autowired
	private FtSkillService ftSkillService;
	
	@Autowired
	private MemReservationService memReservationService;

	@Autowired
	private MailService mailService; // 用於發送 Email
	
	// 列表
	@GetMapping
	public String listAvailableTimes(
			@RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			@RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
			Model model, HttpSession session) {

		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getFtId();
		String nickname = ftListService.getNicknameByFtId(ftId); // 透過 Service 取得 nickname
		model.addAttribute("nickname", nickname);

		// 查詢所有未過期的時段
		List<AvailableTimeVO> availableTimes = availableTimeService.getAvailableTimesByFtIdAndAfterDateTime(ftId);

		// 格式化時間並傳遞給前端
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEE", Locale.TAIWAN); // 中文星期格式

		List<Map<String, Object>> formattedAvailableTimes = availableTimes.stream().map(at -> {
			Map<String, Object> formattedTime = new HashMap<>();
			formattedTime.put("startDate", at.getStartTime().toLocalDate().format(dateFormatter));
			formattedTime.put("dayOfWeek", at.getStartTime().format(dayOfWeekFormatter));
			formattedTime.put("startTime", at.getStartTime().toLocalTime().format(timeFormatter));
			formattedTime.put("endTime", at.getEndTime().toLocalTime().format(timeFormatter));
			formattedTime.put("status", at.getStatus());
			formattedTime.put("availableTimeNo", at.getAvailableTimeNo());
			return formattedTime;
		}).collect(Collectors.toList());

		// 將結果傳遞給前端
		model.addAttribute("availableTimes", formattedAvailableTimes);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		return "ft-end/availabletimes/list";
	}

	@PostMapping("/add")
	public ResponseEntity<Map<String, String>> addAvailableTime(@RequestParam String startTime, HttpSession session) {

		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getFtId();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime start = LocalDateTime.parse(startTime, formatter);

		// 自動設置 endTime 為 startTime + 1 小時
		LocalDateTime end = start.plusHours(1);

		// 確保 startTime 是未來時間
		if (start.isBefore(LocalDateTime.now())) {
			return ResponseEntity.ok(Map.of("status", "error", "message", "開始時間已過期，請重新選擇。"));
		}

		// 判斷時間是否重疊
		if (availableTimeService.checkTimeOverlap(ftId, start, end)) {
			return ResponseEntity.ok(Map.of("status", "error", "message", "所選時段已存在，請於列表開放時段或重新選擇。"));
		}

		availableTimeService.addAvailableTime(ftId, start, end);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@PutMapping("/updateStatusByPaid") // status 0 -> 3
	public ResponseEntity<Map<String, String>> updateStatusByPaid(@RequestParam Integer availableTimeNo,
			@RequestParam Integer status, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getMemberId();
		availableTimeService.updateStatus(ftId, availableTimeNo, status);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@PutMapping("/statusUpdate") // status 3 -> 1
	public ResponseEntity<Map<String, String>> statusUpdate(@RequestParam Integer availableTimeNo,
			@RequestParam Integer status, HttpSession session) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer ftId = loggedInMember.getFtId();
		availableTimeService.updateStatus(ftId, availableTimeNo, status);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@PutMapping("/notyet")
	public ResponseEntity<Map<String, String>> notyet(@RequestParam Integer availableTimeNo,
			@RequestParam(required = false) Integer skillNo, @RequestParam Integer status, HttpSession session,
			Model model) {
		
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		
		// 取得會員和占卜師 ID（從 session 或其他數據源獲取）
		Integer ftId = loggedInMember.getFtId();
		Integer memId = memReservationService.getSingleMemIdByAvailableTimeNo(availableTimeNo);
		
		// 更新 availableTime 狀態為 4（時間內未付款，避開排程器檢查）
		availableTimeService.updateStatusByMem(availableTimeNo, 4);
				
		return ResponseEntity.ok(Map.of("status", "success"));
		
	}
	
	
	@PutMapping("/paid")
	public ResponseEntity<Map<String, String>> paid(@RequestParam Integer availableTimeNo,
			@RequestParam(required = false) Integer skillNo, @RequestParam Integer status, HttpSession session,
			Model model) {
		Object isLogin = session.getAttribute("isLogin");
		SessionMemberDTO loggedInMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		
		// 取得會員和占卜師 ID（從 session 或其他數據源獲取）
		Integer ftId = loggedInMember.getFtId();
		Integer memId = memReservationService.getSingleMemIdByAvailableTimeNo(availableTimeNo);
		
		// 1. 獲取會員資訊
		MemberVO member = memberService.getById(memId);
		String name = member != null ? member.getName() : "會員";

		// 2. 獲取占卜師資訊
		FtVO ft = ftListService.getById(ftId); // 獲取占卜師資訊
		String companyName = ft.getCompanyName();
		String nickname = ft.getNickname();
		Integer price = ft.getPrice();
		MemberVO memberft = ft.getMember(); // 獲取會員資訊
		String memberName = memberft.getName(); // 獲取關聯的會員名字

		// 3. 獲取預約時間
		AvailableTimeVO timeSlot = availableTimeService.getById(availableTimeNo).get(); // 獲取預約時間段
		DateTimeFormatter formatterD = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");
		DateTimeFormatter formatterT = DateTimeFormatter.ofPattern("HH:mm");

		// 4. 格式化日期
		String formattedStartDate = timeSlot.getStartTime().format(formatterD);
		String formattedStartTime = timeSlot.getStartTime().format(formatterT);
		String formattedEndTime = timeSlot.getEndTime().format(formatterT);

		// 5. 獲取專長名稱
		String skillName = skillNo != null ? ftSkillService.getFtSkillNameById(skillNo) : "一般占卜";

		// 6. 更新 availableTime 狀態為 1（已預約）
		availableTimeService.updateStatusByMem(availableTimeNo, 1);
		
		// 7. 更新 reservation table payment 狀態為 1（已付款）
		memReservationService.updatePaymentToOneByAvailableTimeNo(availableTimeNo);

		// 8. 發送通知信

		// 會員的郵件內容
//		String to = member.getEmail(); // 動態抓取
//		String subject = "預約成功通知";
//		String messageText = String.format(
//				"\n親愛的 %s ，您好：\n\n" + "感謝使用我們的預約服務。您已成功預約！\n\n" + "預約資訊如下：\n\n" + "- 公司名稱：%s\n" + "- 占卜師：%s\n"
//						+ "- 占卜日期：%s\n" + "- 占卜時間：%s - %s\n" + "- 預約項目：%s\n" + "- 價格：NT$ %d\n\n" + "期待為您提供專業的占卜服務！\n"
//						+ "如有任何問題，請聯絡我們的客服團隊。\n\n" + "敬祝順心\n			-修卜人生媒合平台\n",
//				name, companyName, nickname, formattedStartDate, formattedStartTime, formattedEndTime, skillName,
//				price);
//		mailService.sendSimpleEmail(to, subject, messageText);
//
//		// 占卜師的郵件內容
//		String ftmail = ft.getMember().getEmail(); // 實際上從占卜師資料中獲取郵件地址
//		String ftSubject = "預約成立通知";
//		String ftMessageText = String.format(
//				"\n親愛的 %s / %s ，您好：\n\n" + "您有一筆新的預約。\n\n" + "詳細資訊如下：\n\n" + "- 預約會員：%s\n" + "- 占卜日期：%s\n"
//						+ "- 占卜時間：%s - %s\n" + "- 預約項目：%s\n" + "- 價格：NT$ %d\n\n" + "請準時為會員提供專業服務。\n"
//						+ "如有任何問題，請聯絡平台客服。\n\n" + "敬祝順心\n			-修卜人生媒合平台\n",
//				memberName, nickname, name, formattedStartDate, formattedStartTime, formattedEndTime, skillName, price);
//		mailService.sendSimpleEmail(ftmail, ftSubject, ftMessageText);

		return ResponseEntity.ok(Map.of("status", "success"));

	}

}
