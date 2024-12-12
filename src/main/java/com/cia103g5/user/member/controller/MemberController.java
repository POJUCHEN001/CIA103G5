package com.cia103g5.user.member.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.user.member.dto.MemberUpdateRequestDTO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/membersAPI")
public class MemberController {

	@Autowired
	private MemberService service;

	// 會員登出
	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logoutMember(HttpSession session) {
		if (session != null) {
	        session.invalidate(); // 清除 Session
	    }
		return ResponseEntity.ok(Map.of("message", "登出成功"));
	}

	// 會員中心: 從資料庫取得會員詳細資訊
	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> getMemberProfile(HttpSession session) {
		// 從sessionMemberDTO裡面取得存入的
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
		MemberVO member = service.findMemberById(memberId);

		Map<String, Object> memberInfo = new HashMap<>();
		memberInfo.put("account", member.getAccount());
		memberInfo.put("name", member.getName());
		memberInfo.put("nickname", member.getNickname());
		memberInfo.put("email", member.getEmail());
		memberInfo.put("emailState", member.getEmailState());
		memberInfo.put("phone", member.getPhone());
		memberInfo.put("gender", member.getGender());
		memberInfo.put("points", member.getPoints());
		memberInfo.put("registeredTime", member.getRegisteredTime());
		memberInfo.put("bankAccount", member.getBankAccount());
		memberInfo.put("status", member.getStatus());

		// 照片處理
		byte[] photo = member.getPhoto();
		if (photo != null) {
			String encodedPhoto = Base64.getEncoder().encodeToString(photo);
			memberInfo.put("photo", encodedPhoto);
		} else {
			memberInfo.put("photo", null);
		}

		return ResponseEntity.ok(memberInfo);
	}

	// 更新會員資料
	@PatchMapping("/updateinfo/{account}")
	public ResponseEntity<String> updateMemberInfo(HttpSession session,
			@RequestBody @Valid MemberUpdateRequestDTO memberUpdateRequest) {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();

		service.updateMemberInfo(memberId, memberUpdateRequest);
		return ResponseEntity.ok("會員資料更新成功！");
	}

	// 更新會員照片
	@PatchMapping("/photo")
	public ResponseEntity<Map<String, Object>> updateMemberPhoto(HttpSession session,
			@RequestParam("photo") MultipartFile photo) {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
//		if(sessionMember == null) {
//			System.out.println("沒有抓到sessionMember, 就沒有 memberId");
//		} else {
//			System.out.println("有 memberId");
//		}

		System.out.println(memberId);

		service.updateMemberPhoto(memberId, photo);

		return ResponseEntity.ok(Map.of("message", "會員照片更新成功"));
	}

	// 修改會員登入密碼
	@PostMapping("changepassword")
	public ResponseEntity<Map<String, Object>> updateMemberPassword(
			@RequestBody Map<String, String> passwordRequest, HttpSession session) {

		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
		// 從請求體中提取密碼
		String currentPassword = passwordRequest.get("currentPassword");
		String newPassword = passwordRequest.get("newPassword");

		// 驗證請求參數是否完整
		if (currentPassword == null || newPassword == null || currentPassword.isEmpty() || newPassword.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "密碼更新失敗", "error", "請提供完整的密碼信息"));
		}
		
		try {
			// 更新密碼
			service.updatePassword(memberId, currentPassword, newPassword);
			// 清除 Session
            session.invalidate();
			// 返回成功響應，由 AOP 清理會話
			return ResponseEntity.ok(
					Map.of("message", "密碼更新成功，請重新登入")
			);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(
					Map.of("message", "密碼更新失敗", "error", e.getMessage())
			);
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	                Map.of("message", "密碼更新失敗，請稍後再試")
	        );
	    }
	}

	// 重新寄送驗證信
	@PostMapping("/resend-verification-code")
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
				return ResponseEntity.ok(Map.of("message", "驗證碼正確，已更新信箱驗證狀態"));
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


	// 查詢會員
	@GetMapping("/{id}")
	public MemberVO getMemberById(@PathVariable("id") Integer memberId) {
		return service.findMemberById(memberId);
	}

	// 查詢會員 (根據帳號)
	@GetMapping("/account/{account}")
	public MemberVO getMemberByAccount(@PathVariable("account") String account) {
		return service.findMemberByAccount(account);
	}

	// 查詢所有會員
	@GetMapping("/listall")
	public ResponseEntity<Map<String, Object>> getAllMembers() {
		List<MemberVO> members = service.getAllMembers();

		return ResponseEntity
				.ok(Map.of("message", "會員清單獲取成功", "data", members.stream().map(this::mapMemberInfo).toList()));
	}

	// 工具方法：映射會員詳細資訊
	private Map<String, Object> mapMemberInfo(MemberVO member) {
		Map<String, Object> memberInfo = new HashMap<>();
		memberInfo.put("account", member.getAccount());
		memberInfo.put("name", member.getName());
		memberInfo.put("nickname", member.getNickname());
		memberInfo.put("email", member.getEmail());
		memberInfo.put("phone", member.getPhone());
		memberInfo.put("gender", member.getGender());
		memberInfo.put("points", member.getPoints());
		memberInfo.put("registeredTime", member.getRegisteredTime());
		memberInfo.put("bankAccount", member.getBankAccount());
		memberInfo.put("status", member.getStatus());

		// 處理照片（Base64 編碼）
		byte[] photo = member.getPhoto();
		memberInfo.put("photo", photo != null ? Base64.getEncoder().encodeToString(photo) : null);

		return memberInfo;
	}

}