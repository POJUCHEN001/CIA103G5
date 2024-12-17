package com.cia103g5.user.ft.model;

import java.io.Serializable;
import java.util.Date;

import com.cia103g5.user.ftgrade.model.FtGrade;
import com.cia103g5.user.member.model.MemberVO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ft_info")
public class FtVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ft_id")
	private Integer ftId;

	@OneToOne	// 與 MemberVO 的一對一關係
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id", nullable = false)
	private MemberVO member; 

	@ManyToOne	// 與 FtGrade 的多對一關係
	@JoinColumn(name = "ft_rank", referencedColumnName = "ft_rank", nullable = false)	// 需要設, nullable = false?
	private FtGrade ftRank;

	@Column(name = "company_name", nullable = false)
	private String companyName;

	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

	@Column(name = "registered_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registeredTime;

	@Column(name = "approved_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedTime;

	@Column(name = "status") // 0待審核 1啟用占卜師 2停權三日 3永久停權
	private Byte status;

	@Column(name = "business_photo", columnDefinition = "LONGBLOB")
	private byte[] businessPhoto;

	@Column(name = "business_no")
	private String businessNo;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "can_post")
	private Byte canPost;	//	0停用/未啟用 1啟用

	@Column(name = "can_rev")
	private Byte canRev;		//	0停用/未啟用 1啟用

	@Column(name = "can_sell")
	private Byte canSell;	//	0停用/未啟用 1啟用

	@Column(name = "action_start_day")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actionStartedDay;	//	用Timestamp 可支持到年月日時分秒比較精準 對於停權與否的操作較為嚴謹

	@Column(name = "action_end_day")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actionEndedDay;	//	用Timestamp 理由同上

	@Column(name = "bank_account")
	private String bankAccount;

	@Column( name = "intro")
	private String intro;
	
	@Column(name = "price")
	private Integer price;

	// 建構子
	public FtVO() {
		super();
	}


}
