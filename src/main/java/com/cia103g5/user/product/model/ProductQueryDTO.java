package com.cia103g5.user.product.model;

//接收來自前端的查詢條件
public class ProductQueryDTO {

	private String status;//前端為JSON，因此型別為String
    private String prodName;
    private String prodNo;
    
	public ProductQueryDTO() {
		super();
	}

	public ProductQueryDTO(String status, String prodName, String prodNo) {
		super();
		this.status = status;
		this.prodName = prodName;
		this.prodNo = prodNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdNo() {
		return prodNo;
	}

	public void setProdNo(String prodNo) {
		this.prodNo = prodNo;
	}
    
    
    
}
