package com.cia103g5.user.availabletime.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AvailableTimeRepository extends JpaRepository<AvailableTimeVO, Integer> {

	// 取得占卜師的可預約時間(不指定狀態)
	@Query("select atv from AvailableTimeVO atv where atv.ftVO.ftId = ?1 and atv.startTime >= ?2 and atv.endTime < ?3 and (atv.status = ?4 or ?4 is null) order by atv.startTime")
	List<AvailableTimeVO> findByFtId(Integer ftId, LocalDateTime startTime, LocalDateTime endTime, Integer status);

	// 檢查時間是否重疊(查詢指定占卜師重疊的時間)
	@Query("select atv from AvailableTimeVO atv where atv.ftVO.ftId = ?1 and (atv.startTime < ?3 and atv.endTime > ?2)")
	List<AvailableTimeVO> findByFtIdAndTime(Integer ftId, LocalDateTime startTime, LocalDateTime endTime);

	// 取得可預約時間記錄 by id
	@Query("select atv from AvailableTimeVO atv where atv.availableTimeNo = ?1")
	Optional<AvailableTimeVO> findById(Integer availableTimeNo);

	// 插入一筆新的可預約時間記錄，設置狀態為 0（可預約）
	@Transactional
	@Modifying
	@Query(value = "insert into available_time (ft_id, start_time, end_time, status) values (?1, ?2, ?3, 0)", nativeQuery = true)
	void insert(Integer ftId, LocalDateTime startTime, LocalDateTime endTime);

	// 更新可預約時間記錄的狀態
	@Transactional
	@Modifying
	@Query("update AvailableTimeVO set status = ?3 where ftVO.ftId = ?1 and availableTimeNo = ?2")
	void updateStatus(Integer ftId, Integer availableTimeNo, Integer status);

	// 查詢指定占卜師的可預約時間，時間範圍從當前時間開始，並按開始時間排序
	@Query("select atv from AvailableTimeVO atv where atv.ftVO.ftId = ?1 and atv.startTime >= ?2 order by atv.startTime")
	List<AvailableTimeVO> findByFtIdAndStartTimeAfter(Integer ftId, LocalDateTime now);

	// 查詢狀態為 0（可預約）的可預約時間，時間範圍從當前開始
	@Query("select atv from AvailableTimeVO atv where atv.ftVO.ftId = ?1 and atv.startTime >= ?2 and atv.status = 0 order by atv.startTime")
	List<AvailableTimeVO> findByFtIdAndStartTimeAfterAndStatus(Integer ftId, LocalDateTime now);

	// 查詢指定占卜師的指定可預約時間，狀態為 0（可預約）
	@Query("select atv from AvailableTimeVO atv where atv.ftVO.ftId = ?1 and atv.startTime = ?2 and atv.status = 0 order by atv.startTime")
	List<AvailableTimeVO> findByFtIdAndStartTimeAndStatus(Integer ftId, String resvDate, LocalDateTime now);

}
