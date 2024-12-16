package com.cia103g5.user.member.controller;

import com.cia103g5.user.member.dto.MemberManageDTO;
import com.cia103g5.user.member.model.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member_management")
public class MemberManageController {

    @Autowired
    private MemberService memberService;

    // 會員管理
    public ResponseEntity<List<MemberManageDTO>> findAllMember(@PathVariable Integer status){
        List<MemberManageDTO> members = memberService.getAllMember(status);
        return ResponseEntity.ok(members);
    }


    // 會員停權處理
    public ResponseEntity<String> updateMemberStatus(@PathVariable Integer memberId){
        memberService.updateMemberStatus(memberId);
        return ResponseEntity.ok("success");
    }





}
