package com.cia103g5.user.memreservation.model;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.reservation.model.ReservationVO;

public interface MemReservationRepository extends JpaRepository<ReservationVO, Integer> {
	
	// 新增預約(無設置專長)
	@Transactional
	@Modifying
	@Query(value = "insert into reservation (mem_id, ft_id, available_time_no, price, created_time, payment, rsv_status) values (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
	void insert(Integer memId, Integer ftId, Integer availableTimeNo, Integer price, Timestamp createdTime, Integer payment,
			Integer rsvStatus);
	
	// 新增預約(有設置專長)
	@Transactional
	@Modifying
	@Query(value = "insert into reservation (mem_id, ft_id, available_time_no, price, skill_no, payment, rsv_status) values (?1, ?2, ?3, ?4, ?5, 0, 0)", nativeQuery = true)
	void insert(Integer memId, Integer ftId, Integer availableTimeNo, Integer price, Integer skillNo);

	// 根據 availableTimeNo 查詢 memId
	@Query(value = "select mem_id from reservation where available_time_no = ?1", nativeQuery = true)
	Optional<Integer> findSingleMemIdByAvailableTimeNo(Integer availableTimeNo);
	
}
