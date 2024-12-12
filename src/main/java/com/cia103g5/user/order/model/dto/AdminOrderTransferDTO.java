package com.cia103g5.user.order.model.dto;

import java.time.LocalDate;

//用來裝轉換好的ordersVO
public class AdminOrderTransferDTO {
	private Integer orderNo;
	private Integer memId;
	private Integer ftId;
	private Integer orderAmount;
	private Integer realAmount;
	private String orderState;
	private String shipStatus;
	private String payment;
	private LocalDate createdTime;
	private LocalDate endedTime;
	
	public AdminOrderTransferDTO() {
		super();
	}

	public AdminOrderTransferDTO(Integer orderNo, Integer memId, Integer ftId, Integer orderAmount, Integer realAmount,
			String orderState, String shipStatus, String payment, LocalDate createdTime, LocalDate endedTime) {
		super();
		this.orderNo = orderNo;
		this.memId = memId;
		this.ftId = ftId;
		this.orderAmount = orderAmount;
		this.realAmount = realAmount;
		this.orderState = orderState;
		this.shipStatus = shipStatus;
		this.payment = payment;
		this.createdTime = createdTime;
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

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Integer realAmount) {
		this.realAmount = realAmount;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(String shipStatus) {
		this.shipStatus = shipStatus;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
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
