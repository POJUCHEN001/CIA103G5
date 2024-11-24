package com.cia103g5.user.orderDetails.model;

import java.io.Serializable;
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

	//constructor
	public OrderDetailVO(CompositeDetail compositekey,Integer price,Integer quantity,String rateContent,Integer rateScore) {
		super();
		this.compositekey=compositekey;
		this.price =price;
		this.quantity =quantity;
		this.rateContent = rateContent;
		this.rateScore =rateScore;
	}

	//constructor
	public  OrderDetailVO() {
		
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



	//配置複合主鍵，實作serializable
	@Embeddable
	public static class CompositeDetail implements Serializable{
		
		private static final long serialVersionUID = 1L;
	
		@Column(name="order_no")
		private Integer orderNo;
		
		//prod_no本身也為fk
		@ManyToOne
		@JoinColumn(name="prod_no",referencedColumnName = "prod_no")
		private ProductVO productVO;

		public CompositeDetail() {
			super();
		}

		public CompositeDetail(Integer orderNo, ProductVO productVO) {
			super();
			this.orderNo = orderNo;
			this.productVO = productVO;
		}

		public Integer getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(Integer orderNo) {
			this.orderNo = orderNo;
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
			result =prime*result +((orderNo == null) ? 0 : orderNo.hashCode());
			result =prime*result +((productVO ==null) ? 0 : productVO.hashCode());		
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this ==obj) {
				return true;
			}
			
			if(obj!=null && getClass()==obj.getClass()) {
				CompositeDetail compositekey =(CompositeDetail)obj;
				
				if(orderNo.equals(compositekey.orderNo)&& productVO.equals(compositekey.productVO)) {
					return true;
				}
			}
			
			
			return false;
		}
		
		
		
	}
	
	
}
