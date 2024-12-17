package com.cia103g5.user.availabletime.controller;

import com.cia103g5.user.availabletime.model.AvailableTimeService;
import com.cia103g5.user.availabletime.model.AvailableTimeVO;
import com.cia103g5.user.ftlist.model.FtDTO;
import com.cia103g5.user.ftlist.model.FtListService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mem-end/availabletimes")
public class MemAvailableTimeController {

	@Autowired
	private AvailableTimeService availableTimeService;

	@Autowired
	private FtListService ftListService;

	@GetMapping("/{ftId}")
	public String getAllAvailableTimes(@PathVariable Integer ftId,
			@RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			@RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
			Model model
//			) {
		
		, HttpSession session) {
			
			Boolean isLogin = Boolean.TRUE.equals(session.getAttribute("isLogin"));
			if (!isLogin) {
				session.setAttribute("redirectURL", "/mem-end/availabletimes/" + ftId);
				return "redirect:/login";
			}

		// 獲取占卜師的詳細資訊
		FtDTO ft = ftListService.getFtById(ftId);
		model.addAttribute("ft", ft);
		model.addAttribute("ftId", ftId);

		// 查詢 3 天後的 0 時間
		List<AvailableTimeVO> availableTimes = availableTimeService.getAvailableTimesByFtIdAndStatus(ftId);

		// 格式化時間
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

		return "availabletimeslist";

	}

}
