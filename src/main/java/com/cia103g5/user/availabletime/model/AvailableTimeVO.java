package com.cia103g5.user.availabletime.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.cia103g5.user.ft.model.FtVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "available_time", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ft_id", "start_time", "end_time" }) })

public class AvailableTimeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "available_time_no")
	private Integer availableTimeNo;

	@ManyToOne
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id", nullable = false)
	@NotNull(message = "占卜師編號: 請勿空白")
	private FtVO ftVO; // 占卜師資訊關聯

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "start_time", nullable = false)
	@NotNull(message = "開始時間: 請勿空白")
	@Future(message = "開始時間: 必須是未來時間")
	private LocalDateTime startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "end_time", nullable = false)
	@NotNull(message = "結束時間: 請勿空白")
	private LocalDateTime endTime;

	@Column(name = "status", columnDefinition = "TINYINT")
	private Integer status = 0; // 0:可預約 1:已預約 2:暫停預約

	public AvailableTimeVO() {
	}

	public AvailableTimeVO(LocalDateTime startTime) {
		if (startTime.getMinute() != 0) {
			throw new IllegalArgumentException("請選擇整點時間");
		}
		this.startTime = startTime;
		this.endTime = startTime.plusHours(1);
	}

	public Integer getAvailableTimeNo() {
		return availableTimeNo;
	}

	public void setAvailableTimeNo(Integer availableTimeNo) {
		this.availableTimeNo = availableTimeNo;
	}

	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		if (startTime.getMinute() != 0) {
			throw new IllegalArgumentException("請選擇整點時間");
		}
		this.startTime = startTime;
		this.endTime = startTime.plusHours(1);
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(availableTimeNo, endTime, startTime, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvailableTimeVO other = (AvailableTimeVO) obj;
		return java.util.Objects.equals(availableTimeNo, other.availableTimeNo)
				&& java.util.Objects.equals(endTime, other.endTime)
				&& java.util.Objects.equals(startTime, other.startTime)
				&& java.util.Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "AvailableTimeVO [availableTimeNo=" + availableTimeNo + ", ftVO=" + ftVO + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", status=" + status + "]";
	}

}
