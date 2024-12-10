package com.cia103g5.user.member.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {

	@NotBlank(message = "帳號不能為空")
	private String account;

	@NotBlank(message = "密碼不能為空")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,20}$", message = "密碼必須包含大小寫字母及數字，且長度需在6到20字元之間")
	private String password;

	@NotBlank(message = "姓名不能為空")
	private String name;

	@NotBlank(message = "暱稱不能為空")
	private String nickname;

	@NotBlank(message = "電話不能為空")
	@Pattern(regexp = "\\d{10}", message = "電話號碼格式不正確")
	private String phone;

	@NotBlank(message = "電子信箱不能為空")
	@Email(message = "電子信箱格式不正確")
	private String email;

	@NotNull(message = "性別不能為空")
	private Integer gender;
	
	private String bankAccount;

	private MultipartFile photo;



	@Override
	public String toString() {
		return "MemberRequestDTO [account=" + account + ", password=" + password + ", name=" + name + ", nickname="
				+ nickname + ", phone=" + phone + ", email=" + email + ", gender=" + gender + ", bankAccount="
				+ bankAccount + ", photo=" + photo + "]";
	}

	

}
