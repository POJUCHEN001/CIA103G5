package com.cia103g5.user.member;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseForMember {

	private String message;
	private String details;
	
	public ApiResponseForMember(String message, String details) {
		super();
		this.message = message;
		this.details = details;
	}
	
	
}
