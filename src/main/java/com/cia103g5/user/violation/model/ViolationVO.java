package com.cia103g5.user.violation.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "violation_index")
public class ViolationVO implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "violation_type_no")
	private Integer vioTypeNo;

	@Column(name = "violation_type")
	private String vioType;


	public ViolationVO() {
		super();
	}

	public ViolationVO(Integer vioTypeNo, String vioType) {
		super();
		this.vioTypeNo = vioTypeNo;
		this.vioType = vioType;
	}

	// Getter & Setter
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


}
