package com.cia103g5.user.member.model;

import java.io.IOException;
//import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.cia103g5.user.member.dto.MemberManageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.common.mail.MailService;
import com.cia103g5.common.verificationcode.VerificationCodeService;
import com.cia103g5.user.member.MemberNotFoundException;
import com.cia103g5.user.member.dto.MemberUpdateRequestDTO;

@Service
public class MemberService {

	@Autowired
	private MemberRepository repository;

	@Autowired
	private MailService mailService;

	@Autowired
	private VerificationCodeService verificationCodeService;
	
//	@Value("${mail.verification.subject}")
//    private String verificationSubject;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	// 新增會員
	public Integer addMember(String account, String password, String name, String nickname, String phone, String email,
			Integer gender, MultipartFile photo) {

		Date registeredTime = new Date();

		MemberVO newMember = new MemberVO();
		newMember.setAccount(account);
		newMember.setPassword(password);
		newMember.setName(name);
		newMember.setNickname(nickname);
		newMember.setPhone(phone);
		newMember.setEmail(email);
		newMember.setGender(gender);
		newMember.setRegisteredTime(registeredTime);
		newMember.setStatus(0); // 帳號啟用
		newMember.setEmailState(0); // 信箱驗證狀態預設為0(未驗證)

		// 檢查註冊資訊
		processPhoto(newMember, photo); // 處理照片
		validateMemberInfo(newMember);

		// 存進資料庫
		repository.save(newMember);
		return repository.save(newMember).getMemberId();

	}

	// 驗證碼
	// @param email 會員的電子郵件
	// @param subject 郵件主題
	// @return 生成的驗證碼
	public String sendVerificationCode(String email) {
		validateNotEmpty(email, "Email");

		String subject = "Your Verification Code"; // 固定主題
		// 使用 Collections.singletonList
		String code = mailService.sendVerificationCode(Collections.singletonList(email), subject);

		// 儲存驗證碼至 Redis
		verificationCodeService.saveVerificationCodeToRedis(email, code);

		return "Verification code sent successfully to " + email;
	}
	
	// 驗證驗證碼
	public boolean validateVerificationCode(String email, String code) {
		validateNotEmpty(email, "Email");
		validateNotEmpty(code, "Code");

		String storedCode = verificationCodeService.getVerificationCodeFromRedis(email);
		return storedCode != null && storedCode.equals(code);
	}
	
	// 更新會員信箱驗證狀態
	public void updateEmailState(String email) {
		MemberVO member = repository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Email不存在: " + email + "is not found!"));
		member.setEmailState(1); // 設置為已驗證
        repository.save(member);
	}

	// 更新會員資料
	public MemberVO updateMemberInfo(Integer memberId, MemberUpdateRequestDTO memberUpdateRequest) {
		MemberVO existingMember = repository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("會員帳號不存在"));

		if (memberUpdateRequest.getName() != null) {
			existingMember.setName(memberUpdateRequest.getName());
		}
		
		if (memberUpdateRequest.getNickname() != null) {
			existingMember.setNickname(memberUpdateRequest.getNickname());
		}
		if (memberUpdateRequest.getPhone() != null) {
			existingMember.setPhone(memberUpdateRequest.getPhone());
		}
		if (memberUpdateRequest.getGender() != null) {
			existingMember.setGender(memberUpdateRequest.getGender());
		}
		if (memberUpdateRequest.getBankAccount() != null) {
			String maskedBankAccount = maskBankAccount(memberUpdateRequest.getBankAccount());
			existingMember.setBankAccount(maskedBankAccount);
		}

		return repository.save(existingMember);
	}

	// 根據帳號查詢會員密碼
	public MemberVO findByAccount(String account, String password) {
		MemberVO member = repository.findByAccount(account)
				.orElseThrow(() -> new RuntimeException("會員帳號不存在，Account: " + account));

		// 驗證密碼是否正確
		if (!member.getPassword().equals(password)) {
			throw new IllegalArgumentException("密碼錯誤");
		}

		return member;
	}

	// 根據ID或帳號修改密碼
	public MemberVO updatePassword(Integer memberId, String oldPassword, String newPassword) {

		MemberVO member = repository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("會員帳號不存在"));

		// 驗證舊密碼是否正確
		if (!member.getPassword().equals(oldPassword)) {
			throw new IllegalArgumentException("帳號或密碼不正確");
		}
		// 加密
//		if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
//		    throw new IllegalArgumentException("舊密碼不正確");
//		}

		// 更新為新密碼
		member.setPassword(newPassword);
//		member.setPassword(passwordEncoder.encode(newPassword));

		return repository.save(member);
	}

	// 查詢所有會員
	public List<MemberVO> getAllMembers() {
		return repository.findAll();
	}

	// 查詢所有會員(後台會員管理)
	public List<MemberManageDTO> getAllMember(Integer status){
		return repository.findMembersByStatus( status);
	}

	// 更新會員照片
	public void updateMemberPhoto(Integer memberId, MultipartFile photo) {
		MemberVO member = repository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("會員帳號不存在"));
		processPhoto(member, photo);
		repository.save(member);
	}

	// 變更會員狀態
	public void updateMemberStatus(Integer memberId) {
		MemberVO member = repository.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException("會員帳號不存在"));
		member.setStatus(0);
		repository.save(member);
	}
	
	// 查詢Email是否存在
//	public MemberVO isEmailExists(String email) {	
//		MemberVO member = repository.findByEmail(email)
//				.orElseThrow(() -> new RuntimeException("Email不存在: " + email + "is not found!"));
//		return repository.findByEmail(email);
//	}

	// 查詢會員（依 ID）
	public MemberVO findMemberById(Integer memberId) {
		return repository.findById(memberId)
				.orElseThrow(() -> new RuntimeException("會員不存在，ID: " + memberId));
	}

	// 查詢會員（依帳號）
	public MemberVO findMemberByAccount(String account) {
		return repository.findByAccount(account).orElseThrow(() -> new RuntimeException("會員帳號不存在，Account: " + account));
	}

	// 通用方法：根據帳號查詢會員
	private MemberVO getMemberByAccount(String account) {
		return repository.findByAccount(account)
				.orElseThrow(() -> new MemberNotFoundException("會員帳號不存在，Account: " + account));
	}

	// 通用方法：檢查帳號是否存在
	public boolean doesAccountExist(String account) {
		return repository.findByAccount(account).isPresent();
	}
	

	// 通用方法：處理照片
	private void processPhoto(MemberVO member, MultipartFile photo) {
		try {
			if (photo != null && !photo.isEmpty()) {
				member.setPhoto(photo.getBytes());
			} else {
				throw new IllegalArgumentException("請上傳有效的照片");
			}
		} catch (IOException e) {
			throw new RuntimeException("照片處理失敗：" + e.getMessage(), e);
		}
	}

	// 通用方法：驗證會員資訊
	private void validateMemberInfo(MemberVO member) {
		if (member.getAccount() == null || member.getAccount().trim().isEmpty()) {
			throw new IllegalArgumentException("帳號不得為空");
		}
		if (member.getPassword() == null || member.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("密碼不得為空");
		}
		if (member.getName() == null || member.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("姓名不得為空");
		}
		if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("EMAIL不得為空");
		}
		if (member.getGender() == null) {
			throw new IllegalArgumentException("性別不得為空");
		}
		if (member.getNickname() == null || member.getNickname().trim().isEmpty()) {
			throw new IllegalArgumentException("暱稱不得為空");
		}
		if (member.getPhone() == null || member.getPhone().trim().isEmpty()) {
			throw new IllegalArgumentException("電話不得為空");
		}
	}

	// 通用方法：遮罩銀行帳號
	private String maskBankAccount(String bankAccount) {
		if (bankAccount == null || bankAccount.length() <= 4) {
			return bankAccount;
		}
		return bankAccount.replaceAll(".(?=.{4})", "*"); // 只保留最後 4 碼
	}

	// 根據 identifier 判斷是帳號還是會員 ID 並查找會員
	private MemberVO findMemberByIdentifier(String identifier) {
		boolean isNumber;
		Integer memberId = null;
		String account = null;

		try {
			memberId = Integer.parseInt(identifier);
			isNumber = true;
		} catch (NumberFormatException e) {
			account = identifier;
			isNumber = false;
		}

		return repository.findByAccountOrMemberId(isNumber, memberId, account)
				.orElseThrow(() -> new RuntimeException("帳號或密碼不正確"));
	}

	private void validateNotEmpty(String value, String fieldName) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
		}
	}
	
	// 根據會員編號更新會員點數
	 public void updateMemberPoints(Integer memberId, Integer points){
	  MemberVO member = repository.findById(memberId)
	    .orElseThrow(() -> new MemberNotFoundException("會員帳號不存在"));
	  member.setPoints(points);
	  repository.save(member);
	 }
	

}
