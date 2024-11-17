package com.cia103g5.mem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.mem.model.MemberInfo;
import com.cia103g5.mem.model.MemberInfoService;

@RestController
@RequestMapping("/api/members")
public class MemberController {
	
	@Autowired
    private MemberInfoService service;

    // 新增會員
    @PostMapping("/register")
    public String registerMember(
            @RequestParam("account") String account,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "gender", required = false) Integer gender,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) throws IOException {
        service.registMember(account, password, name, email, gender, photo);
        return "會員註冊成功！";
    }

    // 查詢會員
    @GetMapping("/{id}")
    public MemberInfo getMemberById(@PathVariable("id") Integer memId) {
        return service.findMemberById(memId);
    }

    // 查詢會員 (根據帳號)
    @GetMapping("/account/{account}")
    public MemberInfo getMemberByAccount(@PathVariable("account") String account) {
        return service.findMemberByAccount(account);
    }

    // 查詢所有會員
    @GetMapping
    public List<MemberInfo> getAllMembers() {
        return service.getAllMembers();
    }

    // 更新會員照片
    @PatchMapping("/{id}/photo")
    public String updateMemberPhoto(@PathVariable("id") Integer memId, @RequestParam("photo") MultipartFile photo) throws IOException {
        service.updateMemberPhoto(memId, photo);
        return "會員照片更新成功！";
    }

	
}
