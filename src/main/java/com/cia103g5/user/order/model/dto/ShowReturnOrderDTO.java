package com.cia103g5.user.order.model.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

//取得退貨相關資訊的DTO
public class ShowReturnOrderDTO {

	private Integer orderNo;
	private Integer memId;
	private Integer ftId;
	private Byte payment;
	private Byte orderState;
	private Date endedTime;
	
	//在service層手動從該orderNo的一個ReturnInfo中查出applytime，手動設置
	private LocalDate applyTime;
	
	public ShowReturnOrderDTO() {
		super();
	}

	public ShowReturnOrderDTO(Integer orderNo, Integer memId, Integer ftId, Byte payment, Byte orderState,
			Date endedTime) {
		super();
		this.orderNo = orderNo;
		this.memId = memId;
		this.ftId = ftId;
		this.payment = payment;
		this.orderState = orderState;
		this.endedTime = endedTime;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public String getPayment() {
		String returnString =null;
		
		switch(payment) {
		case 0:
			returnString="信用卡";
			break;
		case 1:
			returnString="ATM轉帳";
			break;
		case 2:
			returnString="超商繳費";
			break;
		case 3 :
			returnString="貨到付款";			
		}
		
		return returnString;
	}

	public void setPayment(Byte payment) {
		this.payment = payment;
	}

	public Byte getOrderState() {
		return orderState;
	}

	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

	public LocalDate getEndedTime() {
		LocalDate returndate =null;
		if(endedTime!=null) {
		returndate =endedTime.toInstant()
                .atZone(ZoneId.systemDefault()) // 使用系統預設時區
                .toLocalDate();
		}
		
		return returndate;
	}

	public void setEndedTime(Date endedTime) {
		this.endedTime = endedTime;
	}

	public LocalDate getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(LocalDate applyTime) {
		this.applyTime = applyTime;
	}
	
	
	
	
}
