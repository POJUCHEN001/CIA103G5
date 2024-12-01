package com.cia103g5.user.orderDetails.model;

import java.io.Serializable;
import java.util.Set;


import com.cia103g5.user.order.model.OrdersVO;
import com.cia103g5.user.product.model.ProductVO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="order_detail")
public class OrderDetailVO {

	@EmbeddedId
	//配置複合主鍵
	private CompositeDetail compositekey;

	@Column(name="price")
	private Integer price;

	@Column(name="quantity")
	private Integer quantity;

	@Column(name="rate_content")
	private String rateContent;

	@Column(name="rate_score")
	private Integer rateScore;


	//新增退貨狀態
	@Column(name="is_return",nullable = false)
	private Byte isReturn;

	//新增退貨資訊
	@Column(name="return_info")
	private String returnInfo;

	//新增退貨資訊
	@Column(name="return_photo",columnDefinition = "longblob")
	private byte[] returnPhoto;

	//constructor
	public  OrderDetailVO() {

	}

	public OrderDetailVO(CompositeDetail compositekey, Integer price, Integer quantity, String rateContent,
			Integer rateScore, Byte isReturn, String returnInfo, byte[] returnPhoto) {
		super();
		this.compositekey = compositekey;
		this.price = price;
		this.quantity = quantity;
		this.rateContent = rateContent;
		this.rateScore = rateScore;
		this.isReturn = isReturn;
		this.returnInfo = returnInfo;
		this.returnPhoto = returnPhoto;
	}

	public CompositeDetail getCompositekey() {
		return compositekey;
	}

	public void setCompositekey(CompositeDetail compositekey) {
		this.compositekey = compositekey;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRateContent() {
		return rateContent;
	}

	public void setRateContent(String rateContent) {
		this.rateContent = rateContent;
	}

	public Integer getRateScore() {
		return rateScore;
	}

	public void setRateScore(Integer rateScore) {
		this.rateScore = rateScore;
	}


	public Byte getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Byte isReturn) {
		this.isReturn = isReturn;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public byte[] getReturnPhoto() {
		return returnPhoto;
	}

	public void setReturnPhoto(byte[] returnPhoto) {
		this.returnPhoto = returnPhoto;
	}



	//配置複合主鍵，實作serializable
	@Embeddable
	public static class CompositeDetail implements Serializable{

		private static final long serialVersionUID = 1L;

		//order_no本身也為FK
		@ManyToOne
		@JoinColumn(name="order_no",referencedColumnName="order_no")
		private OrdersVO ordersVO;

		//prod_no本身也為FK
		@ManyToOne
		@JoinColumn(name="prod_no",referencedColumnName = "prod_no")
		private ProductVO productVO;

		public CompositeDetail() {
			super();
		}

		public CompositeDetail(OrdersVO orderNo, ProductVO productVO) {
			super();
			this.ordersVO = orderNo;
			this.productVO = productVO;
		}

		public OrdersVO getOrdersVO() {
			return ordersVO;
		}

		public void setOrdersVO(OrdersVO orderNo) {
			this.ordersVO = orderNo;
		}

		public ProductVO getProductVO() {
			return productVO;
		}

		public void setProductVO(ProductVO productVO) {
			this.productVO = productVO;
		}

		@Override
		public int hashCode() {
			final int prime =31;
			int result =1;
			result =prime*result +((ordersVO == null) ? 0 : ordersVO.hashCode());
			result =prime*result +((productVO ==null) ? 0 : productVO.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this ==obj) {
				return true;
			}

			if(obj!=null && getClass()==obj.getClass()) {
				CompositeDetail compositeKey =(CompositeDetail)obj;

				if(ordersVO.equals(compositeKey.ordersVO)&& productVO.equals(compositeKey.productVO)) {
					return true;
				}
			}


			return false;
		}



	}






}
