package com.cia103g5.user.violation.model;

import java.io.Serializable;
import java.util.Date;

import com.cia103g5.user.member.model.MemberVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_violation_record")
public class MemberViolationRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主鍵
	@Column(name = "violation_record_no")
	private Integer vioRecordNo;
	
	@OneToOne
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	@Column(name = "mem_id")
	private MemberVO memId;
	
	@OneToOne
	@JoinColumn(name = "violation_type_no", referencedColumnName = "violation_type_no", nullable = false)
	@Column(name = "violation_type_no")
	private ViolationIndex vioType;
	
	@Column(name = "violated_time", insertable = false, updatable = false)
	private Date violatedTime;
	
	@Column(name = "violation_desc")
	private String violatedDesc;
	
	@Column(name = "punishment")
	private String punishment;
	
	@Column(name = "punish_date")
	private Date punishDate;
	
	@Column(name = "status")	// 未審核:0 已審核:1
	private Integer status;

	
	// 建構子
	public MemberViolationRecord() {
		super();
	}


	public MemberViolationRecord(Integer vioRecordNo, MemberVO memId, ViolationIndex vioType, Date violatedTime,
			String violatedDesc, String punishment, Date punishDate, Integer status) {
		super();
		this.vioRecordNo = vioRecordNo;
		this.memId = memId;
		this.vioType = vioType;
		this.violatedTime = violatedTime;
		this.violatedDesc = violatedDesc;
		this.punishment = punishment;
		this.punishDate = punishDate;
		this.status = status;
	}


	// getter & setter
	public Integer getVioRecordNo() {
		return vioRecordNo;
	}


	public void setVioRecordNo(Integer vioRecordNo) {
		this.vioRecordNo = vioRecordNo;
	}


	public MemberVO getMemId() {
		return memId;
	}


	public void setMemId(MemberVO memId) {
		this.memId = memId;
	}


	public ViolationIndex getVioType() {
		return vioType;
	}


	public void setVioType(ViolationIndex vioType) {
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


	public Date getPunishDate() {
		return punishDate;
	}


	public void setPunishDate(Date punishDate) {
		this.punishDate = punishDate;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "MemberViolationRecord [vioRecordNo=" + vioRecordNo + ", memId=" + memId + ", vioType=" + vioType
				+ ", violatedTime=" + violatedTime + ", violatedDesc=" + violatedDesc + ", punishment=" + punishment
				+ ", punishDate=" + punishDate + ", status=" + status + "]";
	}
	

}
