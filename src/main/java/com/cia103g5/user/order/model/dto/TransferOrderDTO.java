package com.cia103g5.user.order.model.dto;

import java.time.LocalDate;

public class TransferOrderDTO {
	
	private Integer orderNo;
	private Integer memId;
	private Byte payment;
	private Integer orderAmount;
	private Byte shipStatus;
	private Byte orderState;
	private LocalDate createdTime;
	private LocalDate endedTime;
	
	public TransferOrderDTO() {
		super();
	}

	

	public TransferOrderDTO(Integer orderNo, Integer memId, Byte payment, Integer orderAmount, Byte shipStatus,
			Byte orderState, LocalDate createdTime, LocalDate endedTime) {
		super();
		this.orderNo = orderNo;
		this.memId = memId;
		this.payment = payment;
		this.orderAmount = orderAmount;
		this.shipStatus = shipStatus;
		this.orderState = orderState;
		this.createdTime = createdTime;
		this.endedTime = endedTime;
	}


	
	

	public Integer getOrderAmount() {
		return orderAmount;
	}



	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
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

	public Byte getPayment() {
		return payment;
	}

	public void setPayment(Byte payment) {
		this.payment = payment;
	}

	public Byte getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Byte shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Byte getOrderState() {
		return orderState;
	}

	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

	public LocalDate getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDate createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDate getEndedTime() {
		return endedTime;
	}

	public void setEndedTime(LocalDate endedTime) {
		this.endedTime = endedTime;
	}
	
	
	
}
