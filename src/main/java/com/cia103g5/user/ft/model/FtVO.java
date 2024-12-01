package com.cia103g5.user.ft.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import com.cia103g5.user.ftgrade.model.FtGrade;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 資料庫自增
	@Column(name = "ft_id")
	private Integer ftId;

	@OneToOne // 與 MemberVO 的一對一關係
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	private MemberVO memberId;

	@OneToOne
	@JoinColumn(name = "ft_rank", referencedColumnName = "ft_rank", nullable = false) // 直接映射資料庫欄位
	private FtGrade ftRank;

	@Column(name = "company_name", nullable = false)
	private String companyName;

	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

	@Column(name = "registered_time", insertable = false, updatable = false)
	private Date registeredTime;

	@Column(name = "approved_time", insertable = false, updatable = false)
	private Date approvedTime;

	@Column(name = "status") // 0待審核或停用 1啟用占卜師 2永久停權
	private Integer status;

	@Column(name = "business_photo", columnDefinition = "LONGBLOB")
	private byte[] businessPhoto;

	@Column(name = "business_no")
	private String businessNo;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "can_post")
	private Integer canPost; // 0停用/未啟用 1啟用

	@Column(name = "can_rev")
	private Integer canRev; // 0停用/未啟用 1啟用

	@Column(name = "can_sell")
	private Integer canSell; // 0停用/未啟用 1啟用

	@Column(name = "action_start_day")
	private Date actionStartedDay; // 用Timestamp 可支持到年月日時分秒比較精準 對於停權與否的操作較為嚴謹

	@Column(name = "action_end_day")
	private Date actionEndedDay; // 用Timestamp 理由同上

	@Column(name = "bank_account")
	private String bankAccount;

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

	public FtVO(Integer ftId, MemberVO memberId, FtGrade ftRank, String companyName, byte[] photo, Date registeredTime,
			Date approvedTime, Integer status, byte[] businessPhoto, String businessNo, String nickname,
			Integer canPost, Integer canRev, Integer canSell, Timestamp actionStartedDay, Timestamp actionEndedDay,
			String bankAccount, String intro, Integer price) {
		super();
		this.ftId = ftId;
		this.memberId = memberId;
		this.ftRank = ftRank;
		this.companyName = companyName;
		this.photo = photo;
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

	public MemberVO getMemberId() {
		return memberId;
	}

	public void setMemberId(MemberVO memberId) {
		this.memberId = memberId;
	}

	public FtGrade getFtRank() {
		return ftRank;
	}

	public void setFtRank(FtGrade ftRank) {
		this.ftRank = ftRank;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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

	public Date getActionStartedDay() {
		return actionStartedDay;
	}

	public void setActionStartedDay(Date actionStartedDay) {
		this.actionStartedDay = actionStartedDay;
	}

	public Date getActionEndedDay() {
		return actionEndedDay;
	}

	public void setActionEndedDay(Timestamp actionEndedDay) {
		this.actionEndedDay = actionEndedDay;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
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

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	@Override
	public String toString() {
		return "FtVO [ftId=" + ftId + ", memberId=" + memberId + ", ftRank=" + ftRank + ", companyName=" + companyName
				+ ", photo=" + Arrays.toString(photo) + ", registeredTime=" + registeredTime + ", approvedTime="
				+ approvedTime + ", status=" + status + ", businessPhoto=" + Arrays.toString(businessPhoto)
				+ ", businessNo=" + businessNo + ", nickname=" + nickname + ", canPost=" + canPost + ", canRev="
				+ canRev + ", canSell=" + canSell + ", actionStartedDay=" + actionStartedDay + ", actionEndedDay="
				+ actionEndedDay + ", bankAccount=" + bankAccount + ", intro=" + intro + ", price=" + price + "]";
	}


}
