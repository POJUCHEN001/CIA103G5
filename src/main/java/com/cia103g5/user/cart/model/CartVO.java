package com.cia103g5.user.cart.model;

import java.io.Serializable;

public class CartVO implements Serializable {

    private Integer prodNo; // 商品 ID
    private String prodName; // 商品名稱
    private Integer quantity; // 購買數量
    private Integer price; // 商品價格
    private Integer ftId;
    private String nickname;
   



    public CartVO() {
		super();
	}

	public CartVO(Integer prodNo, String prodName, Integer quantity, Integer price, Integer ftId, String nickname) {
		super();
		this.prodNo = prodNo;
		this.prodName = prodName;
		this.quantity = quantity;
		this.price = price;
		this.ftId = ftId;
		this.nickname = nickname;
	}

	// Getters and Setters
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFtId() {
        return ftId;
    }

    public void setFtId(Integer ftId) {
        this.ftId = ftId;
    }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
    
    
}
