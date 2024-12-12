package com.cia103g5.user.order.util;

import java.sql.Timestamp;
import java.time.LocalDate;


import org.springframework.stereotype.Component;



//時間轉換的類(將使用者傳入的時間轉換成timestamp)
@Component
public class TimeTransfer {

	//轉換開始時間
	public  Timestamp getStartOfDay (String  date) {
		LocalDate localDate =LocalDate.parse(date);
		return Timestamp.valueOf(localDate.atStartOfDay());
		
	}
	
	//轉換結束時間
	public  Timestamp getEndOfDay(String date) {
		LocalDate localDate =LocalDate.parse(date);
		return Timestamp.valueOf(localDate.atTime(23, 59, 59, 999));
	}
	
	//轉換Timestamp到localDate
	public LocalDate getLocalDate (Timestamp date) {
		return date.toLocalDateTime().toLocalDate();
	}
	
	
	
	
}
