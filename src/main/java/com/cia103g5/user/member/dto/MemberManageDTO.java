package com.cia103g5.user.member.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberManageDTO {
	
	private Integer memberId;
	private String name;
	private String account;
	private String email;
	private Date registeredTime;
	private Integer points;
	private Integer status;

}
