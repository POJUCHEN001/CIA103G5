package com.cia103g5.user.order.model.dto;

//接收來自前端的查詢條件
public class OrderQureyDTO {

	private String startDate;//開始選擇的日期
	private String endDate;//結束選擇的日期
	private String memId;
	private String orderNo;
	private String orderState;
	private String shipStatus;
	
	public OrderQureyDTO() {
		super();
	}



	public OrderQureyDTO(String startDate, String endDate, String memId, String orderNo, String orderState,
			String shipStatus) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.memId = memId;
		this.orderNo = orderNo;
		this.orderState = orderState;
		this.shipStatus = shipStatus;
	}

	

	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	
	
	
}
