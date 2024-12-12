package com.cia103g5.user.order.model.dto;

import java.util.Base64;

public class OrderSummaryDTO {
	
	private Integer orderNo; //來自訂單
	private Integer total; //來自訂單
	private Integer quantity;//來自訂單明細
	private Integer prodNo; //來自訂單明細(從一筆訂單中存第一個商品編號進去)
	private String prodName; //來自訂單明細、來自產品
	private Integer price; //來自訂單明細(單一一個商品的價格)
	private Byte orderState; //來自訂單
//	private byte[] photo; //來自於商品圖片
	private boolean comment;
	
	
	public OrderSummaryDTO() {
		super();
	}


	public OrderSummaryDTO(Integer orderNo, Integer total, Integer quantity, Integer prodNo, String prodName,
			Integer price, Byte orderState) {
		super();
		this.orderNo = orderNo;
		this.total = total;
		this.quantity = quantity;
		this.prodNo = prodNo;
		this.prodName = prodName;
		this.price = price;
		this.orderState = orderState;
		
	}


	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getProdNo() {
		return prodNo;
	}

	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}



	public Byte getOrderState() {
		return orderState;
	}


	

	public boolean isComment() {
		return comment;
	}


	public void setComment(boolean comment) {
		this.comment = comment;
	}


	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

//將byte[]轉換成base64，讓取得的時候，可以取得型態為base64(字串)的圖片
//	public String getPhoto() {		
//		return Base64.getEncoder().encodeToString(photo);
//	}
//
//
//	public void setPhoto(byte[] photo) {
//		this.photo = photo;
//	}


	@Override
	public String toString() {
		return "OrderSummaryDTO [orderNo=" + orderNo + ", total=" + total + ", quantity=" + quantity + ", prodNo="
				+ prodNo + ", prodName=" + prodName + ", price=" + price + ", orderState=" + orderState + "]";
	}

	
	
	
	
}
