package com.cia103g5.user.availabletime.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.availabletime.controller.SseController;

@Service
public class StatusScheduler {

	@Autowired
	private AvailableTimeRepository availableTimeRepository;

	@Autowired
	private SseController sseController;
	
	// 每隔 15 秒檢查一次狀態 = 3 且超過 5 秒的記錄 (實際上可設為每 30 分鐘檢查超過 4 小時的紀錄)
	@Scheduled(fixedRate = 15000)
	@Transactional
	public void checkAndUpdateStatus() {
		// 當前時間減 20 秒
		LocalDateTime timeThreshold = LocalDateTime.now().minusSeconds(5);
		// 查找需要更新的記錄
		List<AvailableTimeVO> records = availableTimeRepository.findStaleRecords(3, timeThreshold);
		for (AvailableTimeVO record : records) {
			record.setStatus(0); // status 3 -> 0
			availableTimeRepository.save(record);
		}
		
		// 通知前端刷新
        sseController.notifyClients("refresh");

	}

}
