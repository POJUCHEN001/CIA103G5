package com.cia103g5.user.memft.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

/**
 * 複合表：會員收藏占卜師 mem_ft
 */

@Entity
@Table(name = "fav_ft")
public class MemFtVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MemFtKey id;

	@ManyToOne
	@MapsId("memId")
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id")
	private MemberVO memberVO;

	@ManyToOne
	@MapsId("ftId")
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id")
	private FtVO ftVO;

	public MemFtVO() {
	}

	public MemFtKey getId() {
		return id;
	}

	public void setId(MemFtKey id) {
		this.id = id;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	@Override
	public String toString() {
		return "MemFtVO [id=" + id + ", memberVO=" + memberVO + ", ftVO=" + ftVO + "]";
	}
	
	@Embeddable
	public static class MemFtKey implements Serializable {
		private static final long serialVersionUID = 1L;

		@Column(name = "mem_id")
		private Integer memId;

		@Column(name = "ft_id")
		private Integer ftId;

		public MemFtKey() {
		}

		public MemFtKey(Integer memId, Integer ftId) {
			this.memId = memId;
			this.ftId = ftId;
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
			if (obj == null || getClass() != obj.getClass())
				return false;
			MemFtKey other = (MemFtKey) obj;
			return Objects.equals(ftId, other.ftId) && Objects.equals(memId, other.memId);
		}

		@Override
		public String toString() {
			return "MemFtKey [memId=" + memId + ", ftId=" + ftId + "]";
		}

	}

}
