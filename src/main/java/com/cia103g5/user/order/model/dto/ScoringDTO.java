package com.cia103g5.user.order.model.dto;

import java.util.Arrays;
import java.util.Base64;

//只傳送評分、評價內容、商品圖片、商品名稱
public class ScoringDTO {

	private Integer rateScore;//來自於訂單明細
	private String rateContent; //來自於訂單明細
	private String prodName;//來自訂單明細、來自產品
	private Integer prodNo; //來自於訂單明細、來自產品->用來取出在service當中來查詢圖片
	
//	private byte[] photo;//在service再手動存(直接在前端另發請求查主要圖片)
	
	
	
	public ScoringDTO() {
		super();
	}


	//映射時先不存入圖片，在service再手動查詢該圖片
	public ScoringDTO(Integer rateScore, String rateContent,String prodName,Integer prodNo) {
		super();
		this.rateScore = rateScore;
		this.rateContent = rateContent;
		this.prodName = prodName;
		this.prodNo =prodNo;
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


	public String getProdName() {
		return prodName;
	}


	public void setProdName(String prodName) {
		this.prodName = prodName;
	}


//	//getter取得已轉換成base64的型別
//	public String getPhoto() {
//		return Base64.getEncoder().encodeToString(photo);
//	}
//
//
//	public void setPhoto(byte[] photo) {
//		this.photo = photo;
//	}


	public Integer getProdNo() {
		return prodNo;
	}


	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}



	
	
	
	
}
