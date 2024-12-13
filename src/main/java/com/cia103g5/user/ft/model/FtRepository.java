package com.cia103g5.user.ft.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cia103g5.user.member.model.MemberVO;

@Repository
public interface FtRepository extends JpaRepository<FtVO, Integer>{

//    Optional<FtVO> findById(Integer ftId);

    Optional<FtVO> findByMember(MemberVO member);

    // 根據 memId 查詢 ftId
    @Query("SELECT f.ftId FROM FtVO f WHERE f.member.memberId = :memId")
    Optional<Integer> findFtIdByMemId(@Param("memId") Integer memId);

//    Optional<FtVO> findByMemberAndFtId(MemberVO member, Integer ftId);

//    List<FtVO> findAll();
    
    // 
//    List<FtVO> findByStatus();

}
