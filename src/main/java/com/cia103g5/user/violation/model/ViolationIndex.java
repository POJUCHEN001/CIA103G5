package com.cia103g5.user.violation.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "violation_index")
public class ViolationIndex implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "violation_type_no")
	private Integer vioTypeNo;
	
	@Column(name = "violation_type")
	private String vioType;
	
	// 建構子
	public ViolationIndex() {
		super();
	}

	
	public ViolationIndex(Integer vioTypeNo, String vioType) {
		super();
		this.vioTypeNo = vioTypeNo;
		this.vioType = vioType;
	}


	// getter & setter
	public Integer getVioTypeNo() {
		return vioTypeNo;
	}

	public void setVioTypeNo(Integer vioTypeNo) {
		this.vioTypeNo = vioTypeNo;
	}

	public String getVioType() {
		return vioType;
	}

	public void setVioType(String vioType) {
		this.vioType = vioType;
	}


	@Override
	public String toString() {
		return "ViolationIndex [vioTypeNo=" + vioTypeNo + ", vioType=" + vioType + "]";
	}
	

}
