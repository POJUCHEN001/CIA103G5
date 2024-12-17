package com.cia103g5.user.availabletime.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia103g5.user.ft.model.FtVO;

@Service
public class AvailableTimeService {

	@Autowired
	private AvailableTimeRepository repository;

	public AvailableTimeVO getAvailableTimeById(Integer availableTimeNo) {
		return repository.findByPK(availableTimeNo);
	}
	
	public FtVO getFtVOByAvailableTimeNo(Integer availableTimeNo) {
        FtVO ftVO = repository.findFtVOByAvailableTimeNo(availableTimeNo);
        return ftVO;
	}
	
	public Optional<AvailableTimeVO> getById(Integer availableTimeNo) {
		return repository.findById(availableTimeNo);
	}

	// 查詢特定占卜師的全部時間
	public List<AvailableTimeVO> getAvailableTimesByFtId(Integer ftId, LocalDateTime startTime, LocalDateTime endTime,
			Integer status) {
		return repository.findByFtId(ftId, startTime, endTime, status);
	}

	// 檢查重疊時間
	public boolean checkTimeOverlap(Integer ftId, LocalDateTime startTime, LocalDateTime endTime) {
		return repository.findByFtIdAndTime(ftId, startTime, endTime).size() > 0;
	}

	// 新增 0 時間
	public void addAvailableTime(Integer ftId, LocalDateTime startTime, LocalDateTime endTime) {
		repository.save(ftId, startTime, endTime);
	}

	// 占卜師手動更新狀態 0 / 2
	public void updateStatus(Integer ftId, Integer availableTimeNo, Integer status) {
		repository.updateStatus(ftId, availableTimeNo, status);
	}

	// 查詢 3 天後所有時間
	public List<AvailableTimeVO> getAvailableTimesByFtIdAndAfterDateTime(Integer ftId) {
		
		// 如果 ftId 為 null，回傳空的列表
		if (ftId == null) {
	        return List.of();
	    }
		
	    LocalDate threeDaysLaterDate = LocalDate.now().plusDays(3);
	    LocalDateTime threeDaysLater = threeDaysLaterDate.atStartOfDay(); // 設置為 3 天後的 00:00
	
	    return repository.findByFtIdAndStartTimeAfter(ftId, threeDaysLater);
	}
	

	// 查詢 3 天後的 0 時間
	public List<AvailableTimeVO> getAvailableTimesByFtIdAndStatus(Integer ftId) {
		LocalDate threeDaysLaterDate = LocalDate.now().plusDays(3);
	    LocalDateTime threeDaysLater = threeDaysLaterDate.atStartOfDay(); // 設置為 3 天後的 00:00
		return repository.findByFtIdAndStartTimeAfterAndStatus(ftId, threeDaysLater);
	}

	// 會員預約自動更新狀態 = 3 保留中
	public void updateStatusByMem(Integer availableTimeNo, Integer status) {
		AvailableTimeVO availableTime = repository.findById(availableTimeNo)
				.orElseThrow(() -> new RuntimeException("時間段不存在"));
		availableTime.setStatus(status);
		repository.save(availableTime);

	}

	// 查詢特定日期的 0 時間
	public List<AvailableTimeVO> getAvailableTimesByDate(Integer ftId, String resvDate, LocalDateTime now) {
		return repository.findByFtIdAndStartTimeAndStatus(ftId, resvDate, now);
	}

}
