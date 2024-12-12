package com.cia103g5.user.order.model;

import java.time.LocalDate;


//用來存退貨表的資訊(一個Java物件，非DTO)
public class ReturnInfo {

	private Integer prodNo;
	private String reason;
	private Integer quantity;
	private LocalDate applyTime;
	
	private Integer price;//(單個商品的價格)用於取出資訊，查看退貨資訊時，在手動在service查詢存入資料
	private String name;//用於查看退貨訊息時，手動存入
		

	public ReturnInfo() {
		super();
	}


	public ReturnInfo(Integer prodNo, String reason, Integer quantity) {
		super();
		this.prodNo = prodNo;
		this.reason = reason;
		this.quantity = quantity;
	}


	public Integer getProdNo() {
		return prodNo;
	}


	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public LocalDate getApplyTime() {
		return applyTime;
	}


	public void setApplyTime(LocalDate applyTime) {
		this.applyTime = applyTime;
	}


	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
