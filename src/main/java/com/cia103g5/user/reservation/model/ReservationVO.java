package com.cia103g5.user.reservation.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.cia103g5.user.avaliabletime.model.AvailableTimeVO;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftskill.model.FtSkillVO;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class ReservationVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rsv_no")
	private Integer rsvNo;

	@ManyToOne
	@JoinColumn(name = "mem_id", nullable = false)
	private MemberVO memberId;

	@ManyToOne
	@JoinColumn(name = "ft_id", nullable = false)
	private FtVO ftId;

	@ManyToOne
	@JoinColumn(name = "available_time_no", nullable = false)
	private AvailableTimeVO availableTimeNo;

	@ManyToOne
	@JoinColumn(name = "skill_no")
	private FtSkillVO skillNo;


	@Column(name = "rsv_status", nullable = false)
	private Byte rsvStatus;

	@Column(name = "price")
	private Integer price;


	@Column(name = "created_time", updatable = false, nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdTime;

	@Column(name = "payment_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime paymentTime;

	@Column(name = "payment", nullable = false)
	private Byte payment;

	@Column(name = "rating")
	private Integer rating;

	@Column(name = "rating_content", length = 800)
	private String ratingContent;

	@Column(name = "ft_feedback", length = 800)
	private String ftFeedback;

	@Column(name = "note", length = 1000)
	private String note;

	// Getters and Setters
	public Integer getRsvNo() {
		return rsvNo;
	}

	public void setRsvNo(Integer rsvNo) {
		this.rsvNo = rsvNo;
	}

	public MemberVO getMemberId() {
		return memberId;
	}

	public void setMemberId(MemberVO memberId) {
		this.memberId = memberId;
	}


	public FtVO getFtId() {
		return ftId;
	}

	public void setFtId(FtVO ftId) {
		this.ftId = ftId;
	}

	public AvailableTimeVO getAvailableTimeNo() {
		return availableTimeNo;
	}

	public void setAvailableTimeNo(AvailableTimeVO availableTimeNo) {
		this.availableTimeNo = availableTimeNo;
	}

	public FtSkillVO getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(FtSkillVO skillNo) {
		this.skillNo = skillNo;
	}

	public Byte getRsvStatus() {
		return rsvStatus;
	}

	public void setRsvStatus(Byte rsvStatus) {
		this.rsvStatus = rsvStatus;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Byte getPayment() {
		return payment;
	}

	public void setPayment(Byte payment) {
		this.payment = payment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getRatingContent() {
		return ratingContent;
	}

	public void setRatingContent(String ratingContent) {
		this.ratingContent = ratingContent;
	}

	public String getFtFeedback() {
		return ftFeedback;
	}

	public void setFtFeedback(String ftFeedback) {
		this.ftFeedback = ftFeedback;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRatingInfo() {
		if (this.rating == null) {
			return "none"; // Handle null values
		}
		if (this.rating == 1) {
			return "非常不滿意";
		} else if (this.rating == 2) {
			return "不滿意";
		} else if (this.rating == 3) {
			return "普通";
		} else if (this.rating == 4) {
			return "滿意";
		} else if (this.rating == 5) {
			return "非常滿意";
		} else {
			return "none";
		}
	}

	public String getPaymentStatus() {
		if (this.payment == null) {
			return "Unknown"; // Handle null values
		}
		if (this.payment == 1) {
			return "Paid";
		} else if (this.payment == 0) {
			return "Unpaid";
		} else {
			return "Unknown";
		}
	}

	public String getRsvStatusInfo() {
		if (this.rsvStatus == null) {
			return "Unknown"; // Handle null values
		}
		if (this.rsvStatus == 2) {
			return "預約取消";
		} else if (this.rsvStatus == 1) {
			return "預約成功";
		} else if (this.payment == 0) {
			return "審核中";
		} else {
			return "Unknown";
		}
	}

}
