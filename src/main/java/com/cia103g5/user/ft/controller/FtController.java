package com.cia103g5.user.ft.controller;

import com.cia103g5.user.ft.dto.FtUpdateInfoDTO;
import com.cia103g5.user.ft.model.FtService;
import com.cia103g5.user.ft.model.FtVO;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ftAPI")
public class FtController {

    @Autowired
    private FtService ftService;

    // 占卜師註冊資料
//    @PostMapping("/public/ft-register")
//    public ResponseEntity<Map<String, Object>> registeredFt(@ModelAttribute FtRegisteredInfoDTO ftRegisteredInfoDTO){
//        ftService.addFortuneTeller(
//                ftRegisteredInfoDTO.getCompanyName(),
//                ftRegisteredInfoDTO.getPhoto(),
//                ftRegisteredInfoDTO.getBusinessPhoto(),
//                ftRegisteredInfoDTO.getBusinessNo(),
//                ftRegisteredInfoDTO.getNickname()
//        );
//        return ResponseEntity.ok(Map.of(
//                "message", "註冊成功，請等待審核"
//                ));
//    }

    // 占卜師中心資料
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getFtInformation(HttpSession session){
//        if(session.getAttribute("isLogin")!=null){
//
//        }
        SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
        if (sessionMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登入請前往登入"));
        }
        // 根據 memberId 查詢 ftId
        Integer memberId = sessionMember.getMemberId();
        Integer ftId = ftService.findFtIdByMemberId(memberId);
        if (ftId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "您不是占卜師或是無訪問權限，請洽平台管理員"));
        }

        // 根據 ftId 取得占卜師詳細資料
        FtVO fortuneTeller = ftService.findFtByFtId(ftId);

        Map<String, Object> ft = new HashMap<>();
        ft.put("ftId", fortuneTeller.getFtId());
        ft.put("nickname", fortuneTeller.getNickname());
        ft.put("companyName", fortuneTeller.getCompanyName());
        ft.put("businessNo", fortuneTeller.getBusinessNo());
        ft.put("ftRank", fortuneTeller.getFtRank());
        ft.put("registeredTime", fortuneTeller.getRegisteredTime());
        ft.put("approvedTime", fortuneTeller.getApprovedTime());
        ft.put("price", fortuneTeller.getPrice());
        ft.put("intro", fortuneTeller.getIntro());
        ft.put("status", fortuneTeller.getStatus());

        // 照片處理 (格式轉換)
        byte[] photo = fortuneTeller.getPhoto();
        byte[] businessPhoto = fortuneTeller.getBusinessPhoto();
        if(photo != null){
            String encodedPhoto = Base64.getEncoder().encodeToString(photo);
            ft.put("photo", encodedPhoto);
        } else{
            ft.put("photo", null);
        }
        if(businessPhoto != null){
            String encodedBusinessPhoto = Base64.getEncoder().encodeToString(businessPhoto);;
            ft.put("businessPhoto", encodedBusinessPhoto);
        } else{
            ft.put("businessPhoto", null);
        }
        // 簡化寫法
//        ft.put("photo", encodePhoto(fortuneTeller.getPhoto()));
//        ft.put("businessPhoto", encodePhoto(fortuneTeller.getBusinessPhoto()));

        return  ResponseEntity.ok(ft);
    }

    // 更新占卜師資訊
    @PatchMapping("/updateinfo")
    public ResponseEntity<String> updateFtInformation(HttpSession session, @RequestBody @Valid FtUpdateInfoDTO FtUpdateInfoDTO){
        SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
        if (sessionMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }
        Integer memberId = sessionMember.getMemberId();
        Integer ftId = ftService.findFtIdByMemberId(memberId);

        String nickname = FtUpdateInfoDTO.getNickname();
        String intro = FtUpdateInfoDTO.getIntro();
        Integer price = FtUpdateInfoDTO.getPrice();
        ftService.updateFortuneTellerIntroduction(ftId, intro, nickname, price);

        return ResponseEntity.ok("資料更新成功");
    }

    // 更新占卜師形象照
    @PatchMapping("/updatephoto")
    public ResponseEntity<Map<String, Object>> updateFtPhoto(HttpSession session,
                                                             @RequestParam("photo")MultipartFile photo){
        SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
        Integer memberId = sessionMember.getMemberId();
        Integer ftId = ftService.findFtIdByMemberId(memberId);

        ftService.updateFortuneTellerProfilePhoto(ftId, photo);
        System.out.println("照片更新成功");
        return ResponseEntity.ok(Map.of("message", "照片更新成功"));
    }

    // 照片處理
    private String encodePhoto(byte[] photo) {
        return photo != null ? Base64.getEncoder().encodeToString(photo) : null;
    }

}



