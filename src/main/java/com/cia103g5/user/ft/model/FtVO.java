package com.cia103g5.user.ft.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
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
@Table(name = "ft_info")
public class FtVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // 資料庫自增
	@Column(name = "ft_id")
	private Integer ftId;

	@OneToOne // 與 MemberInfo 的一對一關係
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	@Column(name = "mem_id")
	private MemberVO member;

//	@OneToOne
	@Column(name = "ft_rank")
	private Integer ftRank;

	@Column(name = "company_name", nullable = false)
	private String company_name;

	@Column(name = "profile_photo")
	private byte[] profile_photo;

	@Column(name = "registered_time", insertable = false, updatable = false)
	private Date registeredTime;

	@Column(name = "approved_time", insertable = false, updatable = false)
	private Date approvedTime;

	@Column(name = "status") // 0待審核或停用 1啟用占卜師 2永久停權
	private Integer status;

	@Column(name = "business_photo", nullable = false)
	private byte[] businessPhoto;

	@Column(name = "business_no", nullable = false)
	private String businessNo;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "can_post")
	private Integer canPost; // 0停用/未啟用 1啟用

	@Column(name = "can_rev")
	private Integer canRev; // 0停用/未啟用 1啟用

	@Column(name = "can_sell")
	private Integer canSell; // 0停用/未啟用 1啟用

	@Column(name = "action_started_day")
	private Timestamp actionStartedDay; // 用Timestamp 可支持到年月日時分秒比較精準 對於停權與否的操作較為嚴謹

	@Column(name = "action_ended_day")
	private Timestamp actionEndedDay; // 用Timestamp 理由同上

	@Column(name = "bank_account")
	private Integer bankAccount;

	@Column(name = "intro")
	private String intro;

	@Column(name = "price")
	private Integer price;

	public Integer getFtId() {
		return ftId;
	}

	
	// 建構子
	public FtVO() {
		super();
	}

	public FtVO(Integer ftId, MemberVO member, Integer ftRank, String company_name, byte[] profile_photo,
			Date registeredTime, Date approvedTime, Integer status, byte[] businessPhoto, String businessNo,
			String nickname, Integer canPost, Integer canRev, Integer canSell, Timestamp actionStartedDay,
			Timestamp actionEndedDay, Integer bankAccount, String intro, Integer price) {
		super();
		this.ftId = ftId;
		this.member = member;
		this.ftRank = ftRank;
		this.company_name = company_name;
		this.profile_photo = profile_photo;
		this.registeredTime = registeredTime;
		this.approvedTime = approvedTime;
		this.status = status;
		this.businessPhoto = businessPhoto;
		this.businessNo = businessNo;
		this.nickname = nickname;
		this.canPost = canPost;
		this.canRev = canRev;
		this.canSell = canSell;
		this.actionStartedDay = actionStartedDay;
		this.actionEndedDay = actionEndedDay;
		this.bankAccount = bankAccount;
		this.intro = intro;
		this.price = price;
	}

	
	// getter & setter
	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public MemberVO getMember() {
		return member;
	}

	public void setMember(MemberVO member) {
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

	public Date getRegisteredTime() {
		return registeredTime;
	}

	public void setRegisteredTime(Date registeredTime) {
		this.registeredTime = registeredTime;
	}

	public Date getApprovedTime() {
		return approvedTime;
	}

	public void setApprovedTime(Date approvedTime) {
		this.approvedTime = approvedTime;
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

	public Timestamp getActionStartedDay() {
		return actionStartedDay;
	}

	public void setActionStartedDay(Timestamp actionStartedDay) {
		this.actionStartedDay = actionStartedDay;
	}

	public Timestamp getActionEndedDay() {
		return actionEndedDay;
	}

	public void setActionEndedDay(Timestamp actionEndedDay) {
		this.actionEndedDay = actionEndedDay;
	}

	public Integer getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Integer bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "FtVO [ftId=" + ftId + ", member=" + member + ", ftRank=" + ftRank + ", company_name=" + company_name
				+ ", profile_photo=" + Arrays.toString(profile_photo) + ", registeredTime=" + registeredTime
				+ ", approvedTime=" + approvedTime + ", status=" + status + ", businessPhoto="
				+ Arrays.toString(businessPhoto) + ", businessNo=" + businessNo + ", nickname=" + nickname
				+ ", canPost=" + canPost + ", canRev=" + canRev + ", canSell=" + canSell + ", actionStartedDay="
				+ actionStartedDay + ", actionEndedDay=" + actionEndedDay + ", bankAccount=" + bankAccount + ", intro="
				+ intro + ", price=" + price + "]";
	}

}
