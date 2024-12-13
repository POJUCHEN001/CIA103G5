package com.cia103g5.user.reservation.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftskill.model.FtSkillVO;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.reservation.model.ReservationVO;



public interface ReservationRepository extends JpaRepository<ReservationVO, Integer> {

//	List<ReservationVO> findByMemId(MemberVO memId);
	List<ReservationVO> findByMemberId(MemberVO member);

	List<ReservationVO> findByFtId(FtVO ftId);

	List<ReservationVO> findBySkillNo(FtSkillVO skillNo);
	
	Optional<ReservationVO> findByRsvNo(int rsvNo);
	
	
	
	List<ReservationVO> findByMemberIdAndRsvStatus(MemberVO member, Byte rsvStatus);
    List<ReservationVO> findByFtIdAndRsvStatus(FtVO ftId, Byte rsvStatus);
    List<ReservationVO> findByMemberIdAndCreatedTimeBetween(MemberVO member, Date startDate, Date endDate);
    List<ReservationVO> findByFtIdAndCreatedTimeBetween(FtVO ftId, Date startDate, Date endDate);
    

}
