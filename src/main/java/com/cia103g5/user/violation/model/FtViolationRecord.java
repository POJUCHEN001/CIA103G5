package com.cia103g5.user.violation.model;

import java.io.Serializable;
import java.util.Date;

import com.cia103g5.user.ft.model.FtVO;

import jakarta.persistence.*;

@Entity
@Table(name = "violation_record_ft")
public class FtViolationRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "ft_violation_no")
	private Integer ftVioRecordNo;

	@OneToOne
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id", nullable = false)
	private FtVO ftId;

	@OneToOne
	@JoinColumn(name = "violation_type_no", referencedColumnName = "violation_type_no", nullable = false)
	private ViolationVO vioTypeNo;

	@Column(name = "violation_desc")
	private String violatedDesc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "violated_time", insertable = false, updatable = false)
	private Date violatedTime;

	@Column(name = "punishment")	// 尚未懲處:0 已警告:1 已停權:2
	private String punishment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "punishment_time")
	private Date punishDate;

	@Column(name = "status")	// 待處理:0 申訴中:1 已處理:2
	private Integer status;


	// 建構子
	public FtViolationRecord() {
		super();
	}

	public FtViolationRecord(Integer ftVioRecordNo, FtVO ftId, ViolationVO vioTypeNo, String violatedDesc,
			Date violatedTime, String punishment, Date punishDate, Integer status) {
		super();
		this.ftVioRecordNo = ftVioRecordNo;
		this.ftId = ftId;
		this.vioTypeNo = vioTypeNo;
		this.violatedDesc = violatedDesc;
		this.violatedTime = violatedTime;
		this.punishment = punishment;
		this.punishDate = punishDate;
		this.status = status;
	}


	// Getter & Setter

	public Integer getFtVioRecordNo() {
		return ftVioRecordNo;
	}

	public void setFtVioRecordNo(Integer ftVioRecordNo) {
		this.ftVioRecordNo = ftVioRecordNo;
	}

	public FtVO getFtId() {
		return ftId;
	}

	public void setFtId(FtVO ftId) {
		this.ftId = ftId;
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
		return "FtViolationRecord [ftVioRecordNo=" + ftVioRecordNo + ", ftId=" + ftId + ", vioTypeNo=" + vioTypeNo
				+ ", violatedTime=" + violatedTime + ", violatedDesc=" + violatedDesc + ", punishment=" + punishment
				+ ", status=" + status + "]";
	}


}
