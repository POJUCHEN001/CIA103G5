package com.cia103g5.user.memreservation.model;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.availabletime.model.AvailableTimeVO;
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
	
	// 改預約表格的付款狀態 0 -> 1
	@Transactional
	@Modifying
	@Query(value = "update reservation set payment = 1 where available_time_no = ?1", nativeQuery = true)
	void updatePaymentByAvailableTimeNo(Integer availableTimeNo);
	
	// 改預約表格的成立狀態 0 -> 2 (傳入VO)
	@Modifying
    @Transactional
    @Query("update ReservationVO r SET r.rsvStatus = 2 where r.availableTimeNo = :availableTimeVO")
    Integer updateRsvStatusByAvailableTime(@Param("availableTimeVO") AvailableTimeVO availableTimeVO);
	
	// 根據 AvailableTimeVO 刪除對應的 ReservationVO
    @Modifying
    @Transactional
    @Query("delete from ReservationVO r where r.availableTimeNo = :availableTimeNo")
    void deleteByAvailableTimeVO(@Param("availableTimeNo") AvailableTimeVO availableTimeVO);
	
}
