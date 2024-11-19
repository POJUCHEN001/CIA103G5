package com.cia103g5.ft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cia103g5.ft.model.FtInfoService;

@RestController
@RequestMapping("/api/ftinfo")
public class FtInfoController {

	@Autowired
	private FtInfoService ftInfoService;

	// 檢查是否可以執行某操作
	@GetMapping("/{memId}/canPerform")
	public ResponseEntity<Boolean> canPerformAction(@PathVariable Integer memId, @RequestParam String actionType) {
		boolean canPerform = ftInfoService.canPerformAction(memId, actionType);
		return ResponseEntity.ok(canPerform);
	}

	// 停權占卜師
	@PostMapping("/{memId}/suspend")
	public ResponseEntity<String> suspendFtInfo(@PathVariable Integer memId) {
		ftInfoService.suspendFtInfo(memId);
		return ResponseEntity.ok("占卜師已停權");
	}

	// 恢復/啟用占卜師帳號
	@PostMapping("/{memId}/activate")
	public ResponseEntity<String> activateFtInfo(@PathVariable Integer memId) {
		ftInfoService.activateFtInfo(memId);
		return ResponseEntity.ok("占卜師帳號已啟用");
	}
}
