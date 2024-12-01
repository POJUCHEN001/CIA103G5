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
@Table(name = "violation_record_member")
public class MemberViolationRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主鍵
	@Column(name = "violation_record_no")
	private Integer vioRecordNo;
	
	@OneToOne
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	private MemberVO memberId;
	
	@OneToOne
	@JoinColumn(name = "violation_type_no", referencedColumnName = "violation_type_no", nullable = false)
	private ViolationVO vioTypeNo;

	@Column(name = "violation_desc")
	private String violatedDesc;
	
	@Column(name = "violated_time", insertable = false, updatable = false)
	private Date violatedTime;
	
	@Column(name = "punishment")
	private String punishment;
	
	@Column(name = "punishment_time")
	private Date punishDate;
	
	@Column(name = "status")	// 未審核:0 已審核:1
	private Integer status;

	
	// 建構子
	
	public MemberViolationRecord() {
		super();
	}
	
	public MemberViolationRecord(Integer vioRecordNo, MemberVO memberId, ViolationVO vioTypeNo, String violatedDesc,
			Date violatedTime, String punishment, Date punishDate, Integer status) {
		super();
		this.vioRecordNo = vioRecordNo;
		this.memberId = memberId;
		this.vioTypeNo = vioTypeNo;
		this.violatedDesc = violatedDesc;
		this.violatedTime = violatedTime;
		this.punishment = punishment;
		this.punishDate = punishDate;
		this.status = status;
	}


	// Getter & Setter

	public Integer getVioRecordNo() {
		return vioRecordNo;
	}

	public void setVioRecordNo(Integer vioRecordNo) {
		this.vioRecordNo = vioRecordNo;
	}

	public MemberVO getMemberId() {
		return memberId;
	}

	public void setMemberId(MemberVO memberId) {
		this.memberId = memberId;
	}

	public ViolationVO getVioTypeNo() {
		return vioTypeNo;
	}

	public void setVioTypeNo(ViolationVO vioTypeNo) {
		this.vioTypeNo = vioTypeNo;
	}

	public String getViolatedDesc() {
		return violatedDesc;
	}

	public void setViolatedDesc(String violatedDesc) {
		this.violatedDesc = violatedDesc;
	}

	public Date getViolatedTime() {
		return violatedTime;
	}

	public void setViolatedTime(Date violatedTime) {
		this.violatedTime = violatedTime;
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
		return "MemberViolationRecord [vioRecordNo=" + vioRecordNo + ", memberId=" + memberId + ", vioTypeNo="
				+ vioTypeNo + ", violatedTime=" + violatedTime + ", violatedDesc=" + violatedDesc + ", punishment="
				+ punishment + ", punishDate=" + punishDate + ", status=" + status + "]";
	}


}
