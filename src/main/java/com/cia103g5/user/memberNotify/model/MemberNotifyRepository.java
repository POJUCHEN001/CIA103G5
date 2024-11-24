package com.cia103g5.user.memberNotify.model;

import com.cia103g5.user.member.model.MemberVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberNotifyRepository extends JpaRepository<MemberNotifyVo , Integer> {

    @Query("SELECT mn FROM MemberNotifyVo mn where mn.member = :member")
    List<MemberNotifyVo> findByMember(@Param("member") MemberVO member);

    @Query("SELECT mn FROM MemberNotifyVo mn WHERE mn.member = :member ORDER BY mn.notifiedTime DESC")
    Page<MemberNotifyVo> findByMember(@Param("member") MemberVO member, Pageable pageable);



}

