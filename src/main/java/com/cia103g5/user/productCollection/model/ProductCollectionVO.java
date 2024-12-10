package com.cia103g5.user.productCollection.model;



import java.io.Serializable;
import java.sql.Date;

<<<<<<< HEAD
import org.springframework.format.annotation.DateTimeFormat;

import com.cia103g5.user.productCollection.model.ProductCollectionVO.CompositeDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="prod_collection")
@IdClass(CompositeDetail.class)
public class ProductCollectionVO implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	
	    @Id
	    @Column(name = "prod_no", nullable = false)
	    private Integer prodNo;
	    
	    @Id
	    @Column(name = "mem_id", nullable = false)
	    private Integer memId;
	    
	    @Column(name = "collected_at")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    private Date collectedAt;
	    
	    
	    
	    
	    
	    
	    public ProductCollectionVO() {
		
		}

		public Integer getProdNo() {
			return prodNo;
		}

		public void setProdNo(Integer prodNo) {
			this.prodNo = prodNo;
		}

		public Integer getMemId() {
			return memId;
		}

		public void setMemId(Integer memId) {
			this.memId = memId;
		}

		public Date getCollectedAt() {
			return collectedAt;
		}

		public void setCollectedAt(Date collectedAt) {
			this.collectedAt = collectedAt;
		}

		public static class CompositeDetail implements Serializable {
=======
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.cia103g5.user.productCollection.model.ProductCollectionVO.CompositeDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name="prod_collection")
@IdClass(CompositeDetail.class)
public class ProductCollectionVO implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	
	    @Id
	    @Column(name = "prod_no", nullable = false)
	    private Integer prodNo;
	    
	    @Id
	    @Column(name = "mem_id", nullable = false)
	    private Integer memId;
	    
	    @Column(name = "collected_at")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    private Date collectedAt;
	    
	    
	    
	    
	    
	    
	    public ProductCollectionVO() {
		
		}

		public Integer getProdNo() {
			return prodNo;
		}

		public void setProdNo(Integer prodNo) {
			this.prodNo = prodNo;
		}

		public Integer getMemId() {
			return memId;
		}

		public void setMemId(Integer memId) {
			this.memId = memId;
		}

		public Date getCollectedAt() {
			return collectedAt;
		}

		public void setCollectedAt(Date collectedAt) {
			this.collectedAt = collectedAt;
		}

		static class CompositeDetail implements Serializable {
>>>>>>> branch 'Elio' of https://github.com/POJUCHEN001/CIA103G5.git
			private static final long serialVersionUID = 1L;

			private Integer prodNo;
			private Integer memId;
			
		
			public CompositeDetail() {
				
			}

			public CompositeDetail(Integer prodNo, Integer memId) {
				super();
				this.prodNo = prodNo;
				this.memId = memId;
			}

			public Integer getProdNo() {
				return prodNo;
			}

			public void setProdNo(Integer prodNo) {
				this.prodNo = prodNo;
			}

			public Integer getMemId() {
				return memId;
			}

			public void setMemId(Integer memId) {
				this.memId = memId;
			}

		
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((memId == null) ? 0 : memId.hashCode());
				result = prime * result + ((prodNo == null) ? 0 : prodNo.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;

				if (obj != null && getClass() == obj.getClass()) {
					CompositeDetail compositeKey = (CompositeDetail) obj;
					if (prodNo.equals(compositeKey.prodNo) && memId.equals(compositeKey.memId)) {
						return true;
					}
				}

				return false;
			}

		}


}
