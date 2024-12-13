package com.cia103g5.user.violation.model;

import java.io.Serializable;
import java.sql.Date;


import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name="prod_report")
public class ProductViolationRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "report_no", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reportNo;
	
	@Column(name = "mem_id", nullable = false)
	private Integer memId;
	
	@Column(name = "prod_no", nullable = false)
	private Integer prodNo;
	
	@Column(name = "admin_id")
	private Integer adminId;
	
	@Column(name = "admin_desc")
	private String adminDesc;
	
	@Column(name = "report_content")
	private String reportContent;
	
	@Column(name = "report_desc")
	private String reportDesc;
	
	@Column(name = "status", nullable = false)
	private Byte status;
	
	@Column(name = "create_time", updatable = false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@Column(name = "case_closing_time", updatable = false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date caseClosingTime;

	public ProductViolationRecord() {

	}

	public Integer getReportNo() {
		return reportNo;
	}

	public void setReportNo(Integer reportNo) {
		this.reportNo = reportNo;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getProdNo() {
		return prodNo;
	}

	public void setProdNo(Integer prodNo) {
		this.prodNo = prodNo;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminDesc() {
		return adminDesc;
	}

	public void setAdminDesc(String adminDesc) {
		this.adminDesc = adminDesc;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public String getReportDesc() {
		return reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCaseClosingTime() {
		return caseClosingTime;
	}

	public void setCaseClosingTime(Date caseClosingTime) {
		this.caseClosingTime = caseClosingTime;
	}
	
	
	
	
	
	

}
