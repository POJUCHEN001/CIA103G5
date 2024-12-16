package com.cia103g5.user.member.controller;

import com.cia103g5.user.ft.dto.FtRegisteredInfoDTO;
import com.cia103g5.user.ft.model.FtService;
import com.cia103g5.user.member.dto.MemberRequestDTO;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MemberService service;

    @Autowired
    private FtService ftService;

    // 註冊會員
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerMember(@ModelAttribute MemberRequestDTO request) {

        // 驗證帳號是否已存在
        if (service.doesAccountExist(request.getAccount())) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "此帳號已有人使用，請使用其他帳號進行註冊", "error", "此帳號已有人使用，請使用其他帳號進行註冊")
            );
        }
        Integer memberId = service.addMember(
                request.getAccount(),
                request.getPassword(),
                request.getName(),
                request.getNickname(),
                request.getPhone(),
                request.getEmail(),
                request.getGender(),
                request.getPhoto());

        return ResponseEntity.ok(
                Map.of(
                        "message", "註冊成功，驗證碼已發送至信箱",
                        "account", request.getAccount(), // 返回註冊帳號
                        "memberId", memberId
        ));
    }

    // 寄送驗證碼
    @PostMapping("/send-verification-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestParam String email) {
        try {
            String result = service.sendVerificationCode(email);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "驗證碼發送失敗", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "系統錯誤，請稍後再試"));
        }
    }

    // 驗證用戶驗證碼
    @PostMapping("/validate-verification-code")
    public ResponseEntity<Map<String, Object>> validateVerificationCode(@RequestParam String email,
                                                                        @RequestParam String code) {
        try {
            boolean isValid = service.validateVerificationCode(email, code);
            if (isValid) {
                // 驗證碼正確，更新會員信箱狀態
                service.updateEmailState(email);
                return ResponseEntity.ok(Map.of("message", "驗證碼正確，請前往登入"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "驗證碼錯誤或已過期"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "驗證失敗", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "系統錯誤，請稍後再試"));
        }
    }

    
    

    // 會員登入
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginMember(@RequestBody Map<String, String> data, HttpSession session, HttpServletRequest request) {

        // 取得請求參數
        String account = data.get("account");
        String password = data.get("password");

        try {
            // 調用 Service 檢查會員帳號和密碼並回傳 memberVO 物件
            MemberVO member = service.findByAccount(account, password);

            // 根據會員編號查詢占卜師編號 回傳若為0 即沒有占卜師身分
            Integer ftId = ftService.findFtIdByMemberId(member.getMemberId());


            // 登入成功，將會員資訊存入 Session
            // 從查詢結果 MemberVO 存入前端必要的資訊 到DTO
            SessionMemberDTO sessionMember = new SessionMemberDTO(
                    member.getMemberId(),
                    member.getName(),
                    member.getNickname(),
                    member.getStatus(),
                    ftId
            );
            
            // 儲存登入狀態和會員資訊
            // 將 sessionDTO 存入HttpSession
            session.setAttribute("isLogin", true);
            session.setAttribute("loggedInMember", sessionMember);
            session.setAttribute("memberId", member.getMemberId());
            session.setAttribute("ftId", ftId);

            // 處理 redirectURL
            // 動態取得 context path 並拼接首頁路徑
    		String baseURL = request.getScheme() + "://" +    // 協議 (http 或 https)
                    request.getServerName() +       // 主機名 ( localhost 或域名)
                    ":" + request.getServerPort() + // 埠號 (8080)
                    request.getContextPath();       // Context Path (/專案名稱)
    		
    		
            String redirectURL = (String) session.getAttribute("redirectURL");
            if (redirectURL != null && !redirectURL.isEmpty()) {
                session.removeAttribute("redirectURL"); // 清理 session 中的 URL
                redirectURL = baseURL + redirectURL;
            } else {
            	if(ftId > 0) {
            		redirectURL = baseURL + "/ftcenter";
            	} else {
            		redirectURL = baseURL + "/membercenter";
            	}
            }

            
            System.out.println(session);
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);
                System.out.println("Attribute Name: " + attributeName + ", Value: " + attributeValue);
            }

            
            
            return ResponseEntity.ok(Map.of(
                    "message", "登入成功",
                    "redirectURL", redirectURL,
                    "member", Map.of(
                    		"memberId", member.getMemberId(),
                            "account", member.getAccount(),
                            "name", member.getName(),
                            "nickname", member.getNickname(),
                            "email", member.getEmail(),
                            "phone", member.getPhone(),
                            "gender", member.getGender(), 
                            "ftId", ftId
                    ),
                    "ftId", ftId,
                    "memberId", member.getMemberId()
            ));
            
        } catch (IllegalArgumentException ie) {
            // 帳號或密碼錯誤
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "登入失敗", "error", ie.getMessage()));
        } catch (Exception e) {
            // 其他異常
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "系統錯誤，請稍後再試"));
        }
    }


    // 占卜師註冊資料
    @PostMapping("/ft-register")
    public ResponseEntity<Map<String, Object>> registeredFt(@ModelAttribute FtRegisteredInfoDTO ftRegisteredInfoDTO){
        ftService.addFortuneTeller(
                ftRegisteredInfoDTO.getMemberId(),
                ftRegisteredInfoDTO.getCompanyName(),
                ftRegisteredInfoDTO.getPhoto(),
                ftRegisteredInfoDTO.getBusinessPhoto(),
                ftRegisteredInfoDTO.getBusinessNo(),
                ftRegisteredInfoDTO.getNickname()
        );
        return ResponseEntity.ok(Map.of(
                "message", "註冊成功，請等待審核"
        ));
    }
}
