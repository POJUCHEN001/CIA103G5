package com.cia103g5.user.product.model;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.productImage.model.ProductImageVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="product")

public class ProductVO  implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id //PK
	@Column(name="prod_no")
	@GeneratedValue(strategy =GenerationType.IDENTITY)//自增
	private Integer prodNo;
		
	@ManyToOne
	@JoinColumn(name="ft_id",referencedColumnName = "ft_id",nullable = false)
	private FtVO ftId;
	
	
	@Column(name="prod_name")
	private String prodName;
	

	@Column(name="prod_desc")
	private String prodDesc;
	

	@Column(name="price")
	private Integer price;
	

	@Column(name="available_quantity")
	private Integer availableQuantity;
	
	
	@Column(name="sold_quantity")
	private Integer soldQuantity;
	
	
	@Column(name="rating")
	private Integer rating;

	
	@Column(name="rating_count")
	private Integer ratingCount;
	
	
	@Column(name="view_count")
	private Integer viewCount;
	
	
	@Column(name="listed_time",updatable = false)
	private Timestamp listedTime;
	
	
	@Column(name="status",nullable=false)
	private Byte status;
	
	//一對多的主表格作設定，mappedBy寫上product_image對應的屬性名
	@JsonIgnore
	@OneToMany(mappedBy ="productVO" ,cascade =CascadeType.ALL)	
	private Set <ProductImageVO> productImageVO;

//	一個無參數建構子
	public ProductVO() {
		super();
	}

//	一格有參數建構子
	public ProductVO(Integer prodNo, FtVO ftId,String prodName,String prodDesc,Integer price,Integer availableQuantity,
	Integer soldQuantity, Integer rating, Integer ratingCount, Integer viewCount, Timestamp listedTime,Byte status) {
	this.prodNo = prodNo;
	this.ftId = ftId;
	this.prodName = prodName;
	this.prodDesc = prodDesc;
	this.price = price;
	this.availableQuantity = availableQuantity;
	this.soldQuantity = soldQuantity;
	this.rating = rating;
	this.ratingCount = ratingCount;
	this.viewCount = viewCount;
	this.listedTime = listedTime;
	this.status = status;
}

	public Integer getProdNo() {
		return prodNo;
	}

	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}

	public FtVO getFtId() {
		return ftId;
	}

	public void setFtId(FtVO ftId) {
		this.ftId = ftId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Integer getSoldQuantity() {
		return soldQuantity;
	}

	public void setSoldQuantity(Integer soldQuantity) {
		this.soldQuantity = soldQuantity;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Timestamp getListedTime() {
		return listedTime;
	}

	public void setListedTime(Timestamp listedTime) {
		this.listedTime = listedTime;
	}

	public Byte getStatus() {
		return status;
	}
	

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<ProductImageVO> getProductImageVO() {
		return productImageVO;
	}

	public void setProductImageVO(Set<ProductImageVO> productImageVO) {
		this.productImageVO = productImageVO;
	}
	
	
}
