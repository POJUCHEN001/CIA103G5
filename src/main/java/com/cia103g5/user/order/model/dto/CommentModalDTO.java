package com.cia103g5.user.order.model.dto;

public class CommentModalDTO {
	private Integer prodNo;
	private String prodName;
	
	public CommentModalDTO() {
		super();
	}

	public CommentModalDTO(Integer prodNo, String prodName) {
		super();
		this.prodNo = prodNo;
		this.prodName = prodName;
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
	
	
	
}
