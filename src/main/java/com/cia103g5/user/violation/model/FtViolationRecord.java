package com.cia103g5.user.violation.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ft_violation_record")
public class FtViolationRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "ft_violation_no")
	private Integer ftVioRecordNo;
	
	@OneToOne
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id", nullable = false)
	@Column(name = "ft_id")
	private Integer ftId;
	
	@OneToOne
	@JoinColumn(name = "violation_type_no", referencedColumnName = "violation_type_no", nullable = false)
	@Column(name = "violation_type_no")
	private String vioType;
	
	@Column(name = "violated_time", insertable = false, updatable = false)
	private Date violatedTime;
	
	@Column(name = "violation_desc")
	private String violatedDesc;
	
	@Column(name = "punishment")	// 尚未懲處:0 已警告:1 已停權:2
	private String punishment;
	
	@Column(name = "status")	// 待處理:0 申訴中:1 已處理:2
	private Integer status;

	
	// 建構子
	public FtViolationRecord() {
		super();
	}


	public FtViolationRecord(Integer ftVioRecordNo, Integer ftId, String vioType, Date violatedTime,
			String violatedDesc, String punishment, Integer status) {
		super();
		this.ftVioRecordNo = ftVioRecordNo;
		this.ftId = ftId;
		this.vioType = vioType;
		this.violatedTime = violatedTime;
		this.violatedDesc = violatedDesc;
		this.punishment = punishment;
		this.status = status;
	}


	public Integer getFtVioRecordNo() {
		return ftVioRecordNo;
	}


	public void setFtVioRecordNo(Integer ftVioRecordNo) {
		this.ftVioRecordNo = ftVioRecordNo;
	}


	public Integer getFtId() {
		return ftId;
	}


	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}


	public String getVioType() {
		return vioType;
	}


	public void setVioType(String vioType) {
		this.vioType = vioType;
	}


	public Date getViolatedTime() {
		return violatedTime;
	}


	public void setViolatedTime(Date violatedTime) {
		this.violatedTime = violatedTime;
	}


	public String getViolatedDesc() {
		return violatedDesc;
	}


	public void setViolatedDesc(String violatedDesc) {
		this.violatedDesc = violatedDesc;
	}


	public String getPunishment() {
		return punishment;
	}


	public void setPunishment(String punishment) {
		this.punishment = punishment;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "FtViolationRecord [ftVioRecordNo=" + ftVioRecordNo + ", ftId=" + ftId + ", vioType=" + vioType
				+ ", violatedTime=" + violatedTime + ", violatedDesc=" + violatedDesc + ", punishment=" + punishment
				+ ", status=" + status + "]";
	}
	
	
	
	

}
