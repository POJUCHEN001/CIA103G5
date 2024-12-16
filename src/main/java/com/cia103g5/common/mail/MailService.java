package com.cia103g5.common.mail;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cia103g5.common.verificationcode.VerificationCodeGenerator;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public String sendVerificationCode(Collection<String> receivers, String subject) {

		// 驗證收件人清單
		if (receivers == null || receivers.isEmpty()) {
			throw new IllegalArgumentException("Receiver list cannot be null or empty.");
		}

		// 生成驗證碼
		String verificationCode = VerificationCodeGenerator.generateCode();

		// 設置郵件內容
		subject = "Your Verification Code";
		String content = String.format(
				"Dear user,\n\nYour verification code is: %s\n\nPlease use this code to complete your verification.\n\nBest regards,\nCIA103G5 Team",
				verificationCode);

		// 發送郵件
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(receivers.toArray(new String[0]));
		message.setSubject(subject);
		message.setText(content);
		message.setFrom("CIA103G5_FixLife<cia103.g5@gmail.com>");

//		mailSender.send(message);
//		System.out.println("Verification email sent successfully to: " + receivers);

		// 加入錯誤處理
		try {
			mailSender.send(message);
			System.out.println("Verification email sent successfully to: " + receivers);
		} catch (MailSendException e) {
			System.err.println("無效的收件地址或伺服器拒絕" + receivers);
			e.printStackTrace();
			throw new RuntimeException("Failed to send verification email.");
		} catch (Exception e) {
			System.err.println("Unexpected error occurred while sending email to: " + receivers);
			e.printStackTrace();
			throw new RuntimeException("Unexpected error occurred during email sending.");
		}

		// 返回生成的驗證碼，用於儲存進redis
		return verificationCode;
	}

	
	public void sendResetPasswordMessage(String email, String subject, String text) {
		// 發送郵件
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(subject);
		message.setText(text);
		
		 // 發送郵件
        mailSender.send(message);
	}
	
}
