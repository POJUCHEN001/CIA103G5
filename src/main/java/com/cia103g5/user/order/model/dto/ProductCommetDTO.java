package com.cia103g5.user.order.model.dto;

//用來裝評論的DTO
public class ProductCommetDTO {
	
	private Integer orderNo;
	private Integer prodNo;
	private Integer rateScore;
	private String rateContent;
	
	
	public ProductCommetDTO() {
		super();
	}



	public ProductCommetDTO(Integer orderNo, Integer prodNo, Integer rateScore, String rateContent) {
		super();
		this.orderNo = orderNo;
		this.prodNo = prodNo;
		this.rateScore = rateScore;
		this.rateContent = rateContent;
	}



	public Integer getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}



	public Integer getProdNo() {
		return prodNo;
	}



	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}



	public Integer getRateScore() {
		return rateScore;
	}



	public void setRateScore(Integer rateScore) {
		this.rateScore = rateScore;
	}



	public String getRateContent() {
		return rateContent;
	}



	public void setRateContent(String rateContent) {
		this.rateContent = rateContent;
	}



	@Override
	public String toString() {
		return "ProductCommetDTO [orderNo=" + orderNo + ", prodNo=" + prodNo + ", rateScore=" + rateScore
				+ ", rateContent=" + rateContent + "]";
	}

	
	
}
