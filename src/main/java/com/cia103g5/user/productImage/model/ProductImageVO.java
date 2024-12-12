package com.cia103g5.user.productImage.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.cia103g5.user.product.model.ProductVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="prod_image")
public class ProductImageVO  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="image_no")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer imageNo;
	
	@JsonIgnore
	@ManyToOne //FK
	@JoinColumn(name="prod_no",referencedColumnName = "prod_no") //前者為對應FK欄位名稱，後者為PK欄位名稱
	private ProductVO productVO;
	
	@Column(name="prod_pic",columnDefinition = "longblob")
	private byte[] prodPic;
	
	
	@Column(name="is_primary",nullable=false)
	private Byte isPrimary;
	
	@Column(name="created_time",updatable=false)
	private Timestamp createdTime;

	public ProductImageVO() {
		super();
	}


public ProductImageVO(Integer imageNo, ProductVO productVO, byte[] prodPic, Byte isPrimary, Timestamp createdTime) {
		super();
		this.imageNo = imageNo;
		this.productVO = productVO;
		this.prodPic = prodPic;
		this.isPrimary = isPrimary;
		this.createdTime = createdTime;
	}


public Integer getImageNo() {
		return imageNo;
	}

	public void setImageNo(Integer imageNo) {
		this.imageNo = imageNo;
	}

	public ProductVO getProductVO() {
		return productVO;
	}

	public void setProductVO(ProductVO productVO) {
		this.productVO = productVO;
	}

	public byte[] getProdPic() {
		return prodPic;
	}

	public void setProdPic(byte[] prodPic) {
		this.prodPic = prodPic;
	}

	public Byte getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Byte isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
}
