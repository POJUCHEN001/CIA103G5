package com.cia103g5.user.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberUpdateRequestDTO {

	@NotBlank(message = "姓名不能為空！")
	private String name;
	
	private String nickname;
	
	@Pattern(regexp = "^[0-9]{10}$", message = "手機號碼格式不正確！")
	private String phone;
	
	private Integer gender;
	
	@Size(max = 20, message = "銀行帳戶號碼不能超過20個字！")
	private String bankAccount;


    @Override
	public String toString() {
		return "MemberUpdateRequestDTO [name=" + name + ", nickname=" + nickname + ", phone="
				+ phone + ", gender=" + gender + ", bankAccount=" + bankAccount + "]";
	}

}
