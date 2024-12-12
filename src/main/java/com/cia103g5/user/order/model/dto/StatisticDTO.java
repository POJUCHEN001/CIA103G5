package com.cia103g5.user.order.model.dto;

//只存入統計相關的資料
public class StatisticDTO {

//	private Integer ftId;//占卜師編號
	private Long totalCount;//總筆數
	private Long totalAmount;//總金額
	private Double revenue; //平台月收益(service層再存入)
	private Double settlement; //每月撥款金額(service層再存入)
	
	
	public StatisticDTO() {
		super();
	}

	public StatisticDTO(Long totalCount, Long totalAmount) {
		super();
		this.totalCount = totalCount;
		this.totalAmount = totalAmount;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long  totalCount) {
		this.totalCount = totalCount;
	}

	public Long  getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long  totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double d) {
		this.revenue = d;
	}

	public Double getSettlement() {
		return settlement;
	}

	public void setSettlement(Double settlement) {
		this.settlement = settlement;
	}

	@Override
	public String toString() {
		return "StatisticDTO [totalCount=" + totalCount + ", totalAmount=" + totalAmount + ", revenue=" + revenue
				+ ", settlement=" + settlement + "]";
	}



	
	
	
}
