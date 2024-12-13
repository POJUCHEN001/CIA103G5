package com.cia103g5.user.availabletime.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailableTimeService {

	@Autowired
	private AvailableTimeRepository repository;

	// 取得可預約時間 By Id
	public Optional<AvailableTimeVO> getAvailableTimeById(Integer availableTimeNo) {
		return repository.findById(availableTimeNo);
	}

	// 查詢可預約時間
	public List<AvailableTimeVO> getAvailableTimesByFtId(Integer ftId, LocalDateTime startTime, LocalDateTime endTime,
			Integer status) {
		return repository.findByFtId(ftId, startTime, endTime, status);
	}

	// 檢查重疊時間
	public boolean checkTimeOverlap(Integer ftId, LocalDateTime startTime, LocalDateTime endTime) {
		return repository.findByFtIdAndTime(ftId, startTime, endTime).size() > 0;
	}

	// 新增可預約時間
	public void addAvailableTime(Integer ftId, LocalDateTime startTime, LocalDateTime endTime) {
		repository.insert(ftId, startTime, endTime);
	}

	// 占卜師手動更新狀態
	public void updateStatus(Integer ftId, Integer availableTimeNo, Integer status) {
		repository.updateStatus(ftId, availableTimeNo, status);
	}

	// 查詢未來可預約時間
	public List<AvailableTimeVO> getAvailableTimesByFtIdAndAfterDateTime(Integer ftId, LocalDateTime now) {
		return repository.findByFtIdAndStartTimeAfter(ftId, now);
	}

	// 查詢狀態為 0（可預約）的可預約時間
	public List<AvailableTimeVO> getAvailableTimesByFtIdAndStatus(Integer ftId, LocalDateTime now) {
		return repository.findByFtIdAndStartTimeAfterAndStatus(ftId, now);
	}

	// 會員預約自動更新狀態
	public void updateStatusByMem(Integer availableTimeNo, Integer status) {
		AvailableTimeVO availableTime = repository.findById(availableTimeNo)
				.orElseThrow(() -> new RuntimeException("時間段不存在"));
		availableTime.setStatus(status);
		repository.save(availableTime);

	}

	// 查詢特定日期的可預約時間
	public List<AvailableTimeVO> getAvailableTimesByDate(Integer ftId, String resvDate, LocalDateTime now) {
		return repository.findByFtIdAndStartTimeAndStatus(ftId, resvDate, now);
	}

}
