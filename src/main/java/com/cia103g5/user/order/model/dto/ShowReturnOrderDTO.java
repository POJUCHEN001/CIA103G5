package com.cia103g5.user.order.model.dto;

import java.time.LocalDate;
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

	public Byte getPayment() {
		return payment;
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

	public Date getEndedTime() {
		return endedTime;
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
