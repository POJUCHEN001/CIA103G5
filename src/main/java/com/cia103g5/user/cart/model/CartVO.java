package com.cia103g5.user.cart.model;

import java.io.Serializable;

public class CartVO implements Serializable {

    private Integer prodNo; // 商品 ID
    private String prodName; // 商品名稱
    private Integer quantity; // 購買數量
    private Double price; // 商品價格
    private byte[] primaryImage; // 主圖片

    public CartVO() {}

    public CartVO(Integer prodNo, String prodName, Integer quantity, Double price, byte[] primaryImage) {
        this.prodNo = prodNo;
        this.prodName = prodName;
        this.quantity = quantity;
        this.price = price;
        this.primaryImage = primaryImage;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(byte[] primaryImage) {
        this.primaryImage = primaryImage;
    }
}
