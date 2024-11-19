package com.cia103g5.ft.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.cia103g5.mem.model.MemberInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ft_info")
public class FtInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ft_id")
	private Integer ftId;

	@OneToOne
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	private MemberInfo member; // 與 MemberInfo 的一對一關係

	@Column(name = "ft_rank")
	private Integer ftRank;

	@Column(name = "company_name")
	private String company_name;

	@Column(name = "profile_photo")
	private byte[] profile_photo;

	@Column(name = "registered_at")
	private Date registeredAt;

	@Column(name = "approved_at")
	private Date approvedAt;

	@Column(name = "status") // 0待審核或停用 1啟用 2永久停權
	private Integer status;

	@Column(name = "business_photo")
	private byte[] businessPhoto;

	@Column(name = "business_no")
	private String businessNo;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "can_post")
	private Integer canPost;	//	0停用/未啟用 1啟用

	@Column(name = "can_rev")
	private Integer canRev;		//	0停用/未啟用 1啟用

	@Column(name = "can_sell")
	private Integer canSell;	//	0停用/未啟用 1啟用

	@Column(name = "action_started_at")
	private Timestamp actionStartedAt;	//	用Timestamp 可支持到年月日時分秒比較精準 對於停權與否的操作較為嚴謹

	@Column(name = "action_ended_at")
	private Timestamp actionEndedAt;	//	用Timestamp 理由同上

	@Column(name = "bank_account")
	private Integer bankAccount;

	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public MemberInfo getMember() {
		return member;
	}

	public void setMember(MemberInfo member) {
		this.member = member;
	}

	public Integer getFtRank() {
		return ftRank;
	}

	public void setFtRank(Integer ftRank) {
		this.ftRank = ftRank;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public byte[] getProfile_photo() {
		return profile_photo;
	}

	public void setProfile_photo(byte[] profile_photo) {
		this.profile_photo = profile_photo;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

	public Date getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public byte[] getBusinessPhoto() {
		return businessPhoto;
	}

	public void setBusinessPhoto(byte[] businessPhoto) {
		this.businessPhoto = businessPhoto;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getCanPost() {
		return canPost;
	}

	public void setCanPost(Integer canPost) {
		this.canPost = canPost;
	}

	public Integer getCanRev() {
		return canRev;
	}

	public void setCanRev(Integer canRev) {
		this.canRev = canRev;
	}

	public Integer getCanSell() {
		return canSell;
	}

	public void setCanSell(Integer canSell) {
		this.canSell = canSell;
	}

	public Timestamp getActionStartedAt() {
		return actionStartedAt;
	}

	public void setActionStartedAt(Timestamp actionStartedAt) {
		this.actionStartedAt = actionStartedAt;
	}

	public Timestamp getActionEndedAt() {
		return actionEndedAt;
	}

	public void setActionEndedAt(Timestamp actionEndedAt) {
		this.actionEndedAt = actionEndedAt;
	}

	public Integer getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Integer bankAccount) {
		this.bankAccount = bankAccount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	

}
