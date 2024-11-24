package com.cia103g5.user.memft.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

@Entity
@Table(name = "mem_ft")
@IdClass(MemFtVO.MemFtId.class) // 使用內部類作為組合鍵
public class MemFtVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Integer memId;
	private Integer ftId;
	private MemberVO memberVO;
	private FtVO ftVO;

	public MemFtVO() {
	}

	@Id
	@NotNull(message = "會員編號: 請勿空白")
	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	@Id
	@NotNull(message = "占卜師編號: 請勿空白")
	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	@ManyToOne
	@JoinColumn(name = "mem_id", insertable = false, updatable = false)
	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	@ManyToOne
	@JoinColumn(name = "ft_id", insertable = false, updatable = false)
	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	/**
	 * 靜態內部類：複合主鍵類
	 */
	public static class MemFtId implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer memId;
		private Integer ftId;

		public MemFtId() {
		}

		public Integer getMemId() {
			return memId;
		}

		public void setMemId(Integer memId) {
			this.memId = memId;
		}

		public Integer getFtId() {
			return ftId;
		}

		public void setFtId(Integer ftId) {
			this.ftId = ftId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(ftId, memId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MemFtId other = (MemFtId) obj;
			return Objects.equals(ftId, other.ftId) && Objects.equals(memId, other.memId);
		}

	}
}
